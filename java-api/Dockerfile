# マルチステージビルド
FROM eclipse-temurin:17-jdk-alpine AS build

# Mavenをインストール
RUN apk add --no-cache maven

# 作業ディレクトリを設定
WORKDIR /app

# Maven設定ファイルをコピー
COPY pom.xml .
COPY src ./src

# 依存関係をダウンロード（キャッシュ最適化）
RUN mvn dependency:go-offline -B

# アプリケーションをビルド
RUN mvn clean package -DskipTests

# 実行用イメージ
FROM eclipse-temurin:17-jre-alpine

# 作業ディレクトリを設定
WORKDIR /app

# ビルドしたJARファイルをコピー
COPY --from=build /app/target/pokemon-api-1.0.0.jar app.jar

# ポート8080を公開
EXPOSE 8080

# アプリケーションを起動
ENTRYPOINT ["java", "-jar", "app.jar"]
