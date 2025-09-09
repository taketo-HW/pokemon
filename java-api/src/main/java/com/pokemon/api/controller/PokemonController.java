package com.pokemon.api.controller;

import com.pokemon.api.model.Pokemon;
import com.pokemon.api.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class PokemonController {

    @Autowired
    private PokemonService pokemonService;

    /**
     * ポケモンIDまたは名前でポケモン情報を取得
     */
    @GetMapping("/pokemon/{idOrName}")
    public Mono<ResponseEntity<Pokemon>> getPokemon(@PathVariable String idOrName) {
        return pokemonService.getPokemonByIdOrName(idOrName)
                .map(pokemon -> ResponseEntity.ok(pokemon))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * 世代別ポケモン一覧を取得
     */
    @GetMapping("/pokemon/generation/{generation}")
    public Mono<ResponseEntity<List<Pokemon>>> getPokemonByGeneration(@PathVariable int generation) {
        if (generation < 1 || generation > 5) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return pokemonService.getPokemonByGeneration(generation)
                .collectList()
                .map(pokemonList -> ResponseEntity.ok(pokemonList))
                .defaultIfEmpty(ResponseEntity.ok(List.of()))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * ランダムなポケモンを取得
     */
    @GetMapping("/pokemon/random")
    public Mono<ResponseEntity<Pokemon>> getRandomPokemon() {
        return pokemonService.getRandomPokemon()
                .map(pokemon -> ResponseEntity.ok(pokemon))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * ポケモン名で検索
     */
    @GetMapping("/pokemon/search")
    public Mono<ResponseEntity<List<Pokemon>>> searchPokemon(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return pokemonService.searchPokemonByName(name.trim())
                .collectList()
                .map(pokemonList -> ResponseEntity.ok(pokemonList))
                .defaultIfEmpty(ResponseEntity.ok(List.of()))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * タイプ別ポケモン一覧を取得
     */
    @GetMapping("/pokemon/type/{type}")
    public Mono<ResponseEntity<List<Pokemon>>> getPokemonByType(@PathVariable String type) {
        return pokemonService.getPokemonByType(type)
                .collectList()
                .map(pokemonList -> ResponseEntity.ok(pokemonList))
                .defaultIfEmpty(ResponseEntity.ok(List.of()))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * ヘルスチェック
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Pokemon API",
                "version", "1.0.0"));
    }

    /**
     * 利用可能なAPIエンドポイント一覧
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> apiInfo = Map.of(
                "service", "Pokemon API Service",
                "version", "1.0.0",
                "endpoints", Map.of(
                        "GET /api/v1/pokemon/{idOrName}", "ポケモン情報取得",
                        "GET /api/v1/pokemon/generation/{generation}", "世代別ポケモン一覧",
                        "GET /api/v1/pokemon/random", "ランダムポケモン",
                        "GET /api/v1/pokemon/search?name={name}", "ポケモン名検索",
                        "GET /api/v1/pokemon/type/{type}", "タイプ別ポケモン一覧",
                        "GET /api/v1/health", "ヘルスチェック"));
        return ResponseEntity.ok(apiInfo);
    }
}
