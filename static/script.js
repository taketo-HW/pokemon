// APIのベースURL（Flaskアプリケーション経由）
const API_BASE_URL = '/api';

// 世代別のポケモン範囲
const GENERATIONS = {
    1: { start: 1, end: 151, name: '第1世代' },
    2: { start: 152, end: 251, name: '第2世代' },
    3: { start: 252, end: 386, name: '第3世代' },
    4: { start: 387, end: 493, name: '第4世代' },
    5: { start: 494, end: 649, name: '第5世代' }
};

// ページネーション設定
const POKEMON_PER_PAGE = 20;
let currentPage = 1;
let totalPages = 1;
let currentGeneration = 1;
let pokemonList = [];

// DOM要素の取得
const searchInput = document.getElementById('pokemonSearch');
const searchBtn = document.getElementById('searchBtn');
const randomBtn = document.getElementById('randomBtn');
const loading = document.getElementById('loading');
const error = document.getElementById('error');
const pokemonInfo = document.getElementById('pokemonInfo');

// 一覧関連の要素
const loadListBtn = document.getElementById('loadListBtn');
const generationSelect = document.getElementById('generationSelect');
const listLoading = document.getElementById('listLoading');
const pokemonListElement = document.getElementById('pokemonList');
const pokemonListGrid = document.getElementById('pokemonListGrid');
const prevPageBtn = document.getElementById('prevPage');
const nextPageBtn = document.getElementById('nextPage');
const pageInfo = document.getElementById('pageInfo');

// ポケモン情報表示用の要素
const pokemonImg = document.getElementById('pokemonImg');
const pokemonName = document.getElementById('pokemonName');
const pokemonId = document.getElementById('pokemonId');
const pokemonTypes = document.getElementById('pokemonTypes');
const pokemonHeight = document.getElementById('pokemonHeight');
const pokemonWeight = document.getElementById('pokemonWeight');
const pokemonAbilities = document.getElementById('pokemonAbilities');

// イベントリスナーの設定
searchBtn.addEventListener('click', searchPokemon);
randomBtn.addEventListener('click', getRandomPokemon);
searchInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        searchPokemon();
    }
});

// 一覧関連のイベントリスナー
loadListBtn.addEventListener('click', loadPokemonList);
generationSelect.addEventListener('change', (e) => {
    currentGeneration = parseInt(e.target.value);
    currentPage = 1;
    if (!pokemonListElement.classList.contains('hidden')) {
        loadPokemonList();
    }
});
prevPageBtn.addEventListener('click', () => {
    if (currentPage > 1) {
        currentPage--;
        displayPokemonListPage();
    }
});
nextPageBtn.addEventListener('click', () => {
    if (currentPage < totalPages) {
        currentPage++;
        displayPokemonListPage();
    }
});

// 人気ポケモンボタンのイベントリスナー
document.querySelectorAll('.pokemon-btn').forEach(btn => {
    btn.addEventListener('click', (e) => {
        const pokemonId = e.target.getAttribute('data-id');
        fetchPokemonById(pokemonId);
    });
});

// デバッグ用：DOM要素の存在確認
console.log('DOM要素の確認:');
console.log('searchBtn:', searchBtn);
console.log('randomBtn:', randomBtn);
console.log('loadListBtn:', loadListBtn);
console.log('pokemonInfo:', pokemonInfo);
console.log('pokemonListElement:', pokemonListElement);

// ポケモン検索関数
async function searchPokemon() {
    const query = searchInput.value.trim();
    if (!query) {
        showError('ポケモン名またはIDを入力してください');
        return;
    }
    
    await fetchPokemon(query);
}

// ランダムポケモン取得関数
async function getRandomPokemon() {
    const randomId = Math.floor(Math.random() * 151) + 1; // 第1世代のポケモン（1-151）
    await fetchPokemonById(randomId);
}

// ポケモン情報取得関数（IDまたは名前）
async function fetchPokemon(query) {
    showLoading();
    hideError();
    hidePokemonInfo();
    
    try {
        const response = await fetch(`${API_BASE_URL}/pokemon/${query.toLowerCase()}`);
        
        if (!response.ok) {
            throw new Error('ポケモンが見つかりませんでした');
        }
        
        const pokemonData = await response.json();
        displayPokemon(pokemonData);
        
    } catch (err) {
        showError(err.message);
    } finally {
        hideLoading();
    }
}

// ポケモン情報取得関数（ID指定）
async function fetchPokemonById(id) {
    showLoading();
    hideError();
    hidePokemonInfo();
    
    try {
        const response = await fetch(`${API_BASE_URL}/pokemon/${id}`);
        
        if (!response.ok) {
            throw new Error('ポケモンが見つかりませんでした');
        }
        
        const pokemonData = await response.json();
        displayPokemon(pokemonData);
        
    } catch (err) {
        showError(err.message);
    } finally {
        hideLoading();
    }
}

// ポケモン情報表示関数
function displayPokemon(pokemon) {
    // 基本情報の設定
    pokemonName.textContent = pokemon.name.charAt(0).toUpperCase() + pokemon.name.slice(1);
    pokemonId.textContent = pokemon.id;
    
    // 画像の設定
    pokemonImg.src = pokemon.sprites.other['official-artwork'].front_default || 
                     pokemon.sprites.front_default || 
                     pokemon.sprites.other.dream_world.front_default;
    pokemonImg.alt = pokemon.name;
    
    // タイプの設定
    pokemonTypes.innerHTML = '';
    pokemon.types.forEach(type => {
        const typeElement = document.createElement('span');
        typeElement.className = `type-badge type-${type.type.name}`;
        typeElement.textContent = type.type.name.charAt(0).toUpperCase() + type.type.name.slice(1);
        pokemonTypes.appendChild(typeElement);
    });
    
    // 高さと重さの設定（デシメートルとヘクトグラムから変換）
    pokemonHeight.textContent = `${pokemon.height / 10}m`;
    pokemonWeight.textContent = `${pokemon.weight / 10}kg`;
    
    // 特性の設定
    pokemonAbilities.innerHTML = '';
    pokemon.abilities.forEach(ability => {
        const abilityElement = document.createElement('span');
        abilityElement.className = 'ability-badge';
        abilityElement.textContent = ability.ability.name.charAt(0).toUpperCase() + ability.ability.name.slice(1);
        if (ability.is_hidden) {
            abilityElement.classList.add('hidden-ability');
        }
        pokemonAbilities.appendChild(abilityElement);
    });
    
    showPokemonInfo();
}

// UI表示制御関数
function showLoading() {
    loading.classList.remove('hidden');
}

function hideLoading() {
    loading.classList.add('hidden');
}

function showError(message) {
    error.querySelector('p').textContent = message;
    error.classList.remove('hidden');
}

function hideError() {
    error.classList.add('hidden');
}

function showPokemonInfo() {
    pokemonInfo.classList.remove('hidden');
}

function hidePokemonInfo() {
    pokemonInfo.classList.add('hidden');
}

// ポケモン一覧読み込み関数
async function loadPokemonList() {
    showListLoading();
    hideError();
    
    try {
        const response = await fetch(`${API_BASE_URL}/pokemon-list/${currentGeneration}`);
        
        if (!response.ok) {
            throw new Error('ポケモン一覧の取得に失敗しました');
        }
        
        pokemonList = await response.json();
        
        // ページネーション計算
        totalPages = Math.ceil(pokemonList.length / POKEMON_PER_PAGE);
        currentPage = 1;
        
        // 一覧を表示
        displayPokemonListPage();
        showPokemonList();
        
    } catch (err) {
        showError('ポケモン一覧の読み込みに失敗しました: ' + err.message);
    } finally {
        hideListLoading();
    }
}

// 個別のポケモンデータを取得
async function fetchPokemonData(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/pokemon/${id}`);
        if (response.ok) {
            return await response.json();
        }
    } catch (err) {
        console.error(`ポケモンID ${id} の取得に失敗:`, err);
    }
    return null;
}

// ポケモン一覧ページを表示
function displayPokemonListPage() {
    const startIndex = (currentPage - 1) * POKEMON_PER_PAGE;
    const endIndex = startIndex + POKEMON_PER_PAGE;
    const pagePokemon = pokemonList.slice(startIndex, endIndex);
    
    pokemonListGrid.innerHTML = '';
    
    pagePokemon.forEach(pokemon => {
        if (pokemon) {
            const pokemonCard = createPokemonListCard(pokemon);
            pokemonListGrid.appendChild(pokemonCard);
        }
    });
    
    // ページネーション更新
    updatePagination();
}

// ポケモン一覧カードを作成
function createPokemonListCard(pokemon) {
    const card = document.createElement('div');
    card.className = 'pokemon-list-card';
    card.addEventListener('click', () => {
        displayPokemon(pokemon);
        // 詳細表示エリアにスクロール
        pokemonInfo.scrollIntoView({ behavior: 'smooth' });
    });
    
    const img = pokemon.sprites.other['official-artwork']?.front_default || 
                pokemon.sprites.front_default || 
                pokemon.sprites.other?.dream_world?.front_default || '';
    
    const types = pokemon.types.map(type => 
        `<span class="type-badge type-${type.type.name}">${type.type.name.charAt(0).toUpperCase() + type.type.name.slice(1)}</span>`
    ).join('');
    
    card.innerHTML = `
        <div class="card-image">
            <img src="${img}" alt="${pokemon.name}" loading="lazy">
        </div>
        <div class="card-info">
            <div class="card-id">No. ${pokemon.id}</div>
            <div class="card-name">${pokemon.name.charAt(0).toUpperCase() + pokemon.name.slice(1)}</div>
            <div class="card-types">${types}</div>
        </div>
    `;
    
    return card;
}

// ページネーション更新
function updatePagination() {
    pageInfo.textContent = `ページ ${currentPage} / ${totalPages}`;
    prevPageBtn.disabled = currentPage <= 1;
    nextPageBtn.disabled = currentPage >= totalPages;
}

// UI表示制御関数（一覧用）
function showListLoading() {
    listLoading.classList.remove('hidden');
}

function hideListLoading() {
    listLoading.classList.add('hidden');
}

function showPokemonList() {
    pokemonListElement.classList.remove('hidden');
}

function hidePokemonList() {
    pokemonListElement.classList.add('hidden');
}

// ページ読み込み時にランダムポケモンを表示
window.addEventListener('load', () => {
    getRandomPokemon();
});
