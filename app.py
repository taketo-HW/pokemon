from flask import Flask, render_template, jsonify, request
import requests
import os

app = Flask(__name__)

# Java APIのベースURL
JAVA_API_BASE_URL = 'http://pokemon-api:8080/api/v1'

@app.route('/')
def index():
    """メインページを表示"""
    return render_template('index.html')

@app.route('/api/pokemon/<pokemon_id>')
def get_pokemon(pokemon_id):
    """ポケモン情報を取得するAPIエンドポイント（Java API経由）"""
    try:
        # Java APIからポケモン情報を取得
        response = requests.get(f'{JAVA_API_BASE_URL}/pokemon/{pokemon_id}', timeout=10)
        
        if response.status_code == 200:
            return response.json()
        else:
            return {'error': 'ポケモンが見つかりませんでした'}, 404
            
    except requests.RequestException as e:
        return {'error': f'APIリクエストエラー: {str(e)}'}, 500

@app.route('/api/pokemon-list/<int:generation>')
def get_pokemon_list(generation):
    """世代別ポケモン一覧を取得するAPIエンドポイント（Java API経由）"""
    try:
        # Java APIから世代別ポケモン一覧を取得
        response = requests.get(f'{JAVA_API_BASE_URL}/pokemon/generation/{generation}', timeout=60)
        
        if response.status_code == 200:
            return response.json()
        else:
            return {'error': 'ポケモン一覧の取得に失敗しました'}, 500
            
    except requests.RequestException as e:
        return {'error': f'一覧取得エラー: {str(e)}'}, 500

@app.route('/api/pokemon/random')
def get_random_pokemon():
    """ランダムポケモンを取得するAPIエンドポイント（Java API経由）"""
    try:
        # Java APIからランダムポケモンを取得
        response = requests.get(f'{JAVA_API_BASE_URL}/pokemon/random', timeout=10)
        
        if response.status_code == 200:
            return response.json()
        else:
            return {'error': 'ランダムポケモンの取得に失敗しました'}, 500
            
    except requests.RequestException as e:
        return {'error': f'ランダムポケモン取得エラー: {str(e)}'}, 500

@app.route('/api/pokemon/search')
def search_pokemon():
    """ポケモン検索APIエンドポイント（Java API経由）"""
    try:
        name = request.args.get('name')
        if not name:
            return {'error': '検索名が指定されていません'}, 400
            
        # Java APIからポケモン検索を実行
        response = requests.get(f'{JAVA_API_BASE_URL}/pokemon/search', 
                              params={'name': name}, timeout=30)
        
        if response.status_code == 200:
            return response.json()
        else:
            return {'error': 'ポケモン検索に失敗しました'}, 500
            
    except requests.RequestException as e:
        return {'error': f'検索エラー: {str(e)}'}, 500

@app.route('/api/health')
def health_check():
    """ヘルスチェックエンドポイント"""
    try:
        # Java APIのヘルスチェック
        java_response = requests.get(f'{JAVA_API_BASE_URL}/health', timeout=5)
        java_status = "UP" if java_response.status_code == 200 else "DOWN"
    except:
        java_status = "DOWN"
    
    return jsonify({
        'status': 'UP',
        'service': 'Pokemon Web App',
        'java_api_status': java_status,
        'version': '1.0.0'
    })

if __name__ == '__main__':
    # Dockerコンテナ内で動作するように設定
    port = int(os.environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port, debug=False)
