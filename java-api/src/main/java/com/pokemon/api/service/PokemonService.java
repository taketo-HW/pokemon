package com.pokemon.api.service;

import com.pokemon.api.model.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class PokemonService {

    private final WebClient webClient;
    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2";

    @Autowired
    public PokemonService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(POKEAPI_BASE_URL)
                .build();
    }

    /**
     * ポケモンIDまたは名前でポケモン情報を取得
     */
    public Mono<Pokemon> getPokemonByIdOrName(String idOrName) {
        return webClient
                .get()
                .uri("/pokemon/{idOrName}", idOrName.toLowerCase())
                .retrieve()
                .bodyToMono(Pokemon.class)
                .timeout(java.time.Duration.ofSeconds(10))
                .onErrorResume(throwable -> {
                    System.err.println("ポケモン取得エラー: " + throwable.getMessage());
                    return Mono.empty();
                });
    }

    /**
     * 世代別ポケモン一覧を取得
     */
    public Flux<Pokemon> getPokemonByGeneration(int generation) {
        Map<Integer, int[]> generationRanges = Map.of(
                1, new int[] { 1, 151 },
                2, new int[] { 152, 251 },
                3, new int[] { 252, 386 },
                4, new int[] { 387, 493 },
                5, new int[] { 494, 649 });

        int[] range = generationRanges.get(generation);
        if (range == null) {
            return Flux.error(new IllegalArgumentException("無効な世代です: " + generation));
        }

        int startId = range[0];
        int endId = range[1];

        return Flux.range(startId, endId - startId + 1)
                .flatMap(this::getPokemonById, 5) // 並列度5で処理
                .filter(pokemon -> pokemon != null)
                .timeout(java.time.Duration.ofMinutes(2));
    }

    /**
     * ポケモンIDでポケモン情報を取得
     */
    private Mono<Pokemon> getPokemonById(int id) {
        return webClient
                .get()
                .uri("/pokemon/{id}", id)
                .retrieve()
                .bodyToMono(Pokemon.class)
                .timeout(java.time.Duration.ofSeconds(5))
                .onErrorResume(throwable -> {
                    System.err.println("ポケモンID " + id + " の取得に失敗: " + throwable.getMessage());
                    return Mono.empty();
                });
    }

    /**
     * ランダムなポケモンを取得（第1世代）
     */
    public Mono<Pokemon> getRandomPokemon() {
        int randomId = (int) (Math.random() * 151) + 1;
        return getPokemonById(randomId);
    }

    /**
     * ポケモン検索（名前の部分一致）
     */
    public Flux<Pokemon> searchPokemonByName(String name) {
        return getPokemonByGeneration(1)
                .filter(pokemon -> pokemon.getName().toLowerCase().contains(name.toLowerCase()))
                .take(20); // 最大20件まで
    }

    /**
     * タイプ別ポケモン一覧を取得
     */
    public Flux<Pokemon> getPokemonByType(String type) {
        return webClient
                .get()
                .uri("/type/{type}", type.toLowerCase())
                .retrieve()
                .bodyToMono(Map.class)
                .flatMapMany(response -> {
                    List<Map<String, Object>> pokemonList = (List<Map<String, Object>>) ((Map<String, Object>) response
                            .get("pokemon")).get("results");

                    return Flux.fromIterable(pokemonList)
                            .take(20) // 最大20件まで
                            .flatMap(pokemon -> {
                                String pokemonName = (String) ((Map<String, Object>) pokemon).get("name");
                                return getPokemonByIdOrName(pokemonName);
                            });
                })
                .timeout(java.time.Duration.ofMinutes(1));
    }
}
