
# ポケモン図鑑 Webアプリケーション

PokeAPIを使用した簡易的なポケモン図鑑Webアプリケーションです。Dockerを使用してコンテナ化されており、**Java Spring Boot API**と**Python Flask Webアプリケーション**の2つのサービスで構成されています。

## 機能

- ポケモンの検索（名前またはID）
- ランダムポケモンの表示
- **ポケモン一覧表示（世代別）**
- **ページネーション機能**
- **一覧からポケモン詳細への遷移**
- 人気ポケモンのクイックアクセス
- レスポンシブデザイン
- 美しいUI/UX

## アーキテクチャ

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Web Browser   │───▶│  Flask Web App  │──▶│  Java API       │
│   (Frontend)    │    │   (Port 5000)   │    │  (Port 8080)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                        │
                                ▼                        ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   HTML/CSS/JS   │    │   PokeAPI       │
                       │   (Static)      │    │   (External)    │
                       └─────────────────┘    └─────────────────┘
```

## 技術スタック

### フロントエンド
- **HTML5, CSS3, JavaScript (ES6+)**
- レスポンシブデザイン
- モダンなUI/UX

### バックエンド
- **Python Flask** (Webアプリケーション)
- **Java Spring Boot** (APIサービス)
- **WebFlux** (リアクティブプログラミング)

### インフラ
- **Docker & Docker Compose**
- **Maven** (Java依存関係管理)
- **PokeAPI** (外部データソース)

## セットアップと実行

### 前提条件

- Docker
- Docker Compose

### 実行方法

1. リポジトリをクローン
```bash
git clone <repository-url>
cd pokemon
```

2. Docker Composeでアプリケーションを起動
```bash
docker-compose up --build
```

3. ブラウザでアクセス
```
http://localhost:5000
```

### 個別のDockerコマンドでの実行

```bash
# イメージをビルド
docker build -t pokemon-pokedex .

# コンテナを実行
docker run -p 5000:5000 pokemon-pokedex
```

## プロジェクト構造

```
pokemon/
├── app.py                 # Flask Webアプリケーション
├── requirements.txt       # Python依存関係
├── Dockerfile            # Flask用Docker設定
├── docker-compose.yml    # Docker Compose設定
├── templates/
│   └── index.html        # HTMLテンプレート
├── static/
│   ├── style.css         # CSSスタイル
│   └── script.js         # JavaScript
├── java-api/             # Java Spring Boot API
│   ├── pom.xml           # Maven設定
│   ├── Dockerfile        # Java用Docker設定
│   └── src/main/java/com/pokemon/api/
│       ├── PokemonApiApplication.java  # メインアプリケーション
│       ├── controller/
│       │   └── PokemonController.java  # RESTコントローラー
│       ├── service/
│       │   └── PokemonService.java     # ビジネスロジック
│       ├── model/
│       │   └── Pokemon.java           # データモデル
│       └── config/
│           └── WebClientConfig.java    # WebClient設定
└── README.md             # このファイル
```

## APIエンドポイント

### Flask Webアプリケーション (Port 5000)
- `GET /` - メインページ
- `GET /api/pokemon/<pokemon_id>` - ポケモン情報取得
- `GET /api/pokemon-list/<generation>` - 世代別ポケモン一覧
- `GET /api/pokemon/random` - ランダムポケモン
- `GET /api/pokemon/search?name=<name>` - ポケモン検索
- `GET /api/health` - ヘルスチェック

### Java Spring Boot API (Port 8080)
- `GET /api/v1/` - API情報
- `GET /api/v1/pokemon/{idOrName}` - ポケモン情報取得
- `GET /api/v1/pokemon/generation/{generation}` - 世代別ポケモン一覧
- `GET /api/v1/pokemon/random` - ランダムポケモン
- `GET /api/v1/pokemon/search?name={name}` - ポケモン検索
- `GET /api/v1/pokemon/type/{type}` - タイプ別ポケモン一覧
- `GET /api/v1/health` - ヘルスチェック

## 使用しているAPI

- **PokeAPI**: https://pokeapi.co/api/v2/pokemon/{id_or_name}
  - 無料のポケモンデータAPI
  - ポケモンの詳細情報、画像、タイプ、特性などを提供

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。