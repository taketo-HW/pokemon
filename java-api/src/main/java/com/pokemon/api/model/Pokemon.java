package com.pokemon.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Pokemon {
    private int id;
    private String name;
    private int height;
    private int weight;
    private List<PokemonType> types;
    private List<PokemonAbility> abilities;
    private PokemonSprites sprites;

    // コンストラクタ
    public Pokemon() {
    }

    public Pokemon(int id, String name, int height, int weight,
            List<PokemonType> types, List<PokemonAbility> abilities,
            PokemonSprites sprites) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.types = types;
        this.abilities = abilities;
        this.sprites = sprites;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<PokemonType> getTypes() {
        return types;
    }

    public void setTypes(List<PokemonType> types) {
        this.types = types;
    }

    public List<PokemonAbility> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<PokemonAbility> abilities) {
        this.abilities = abilities;
    }

    public PokemonSprites getSprites() {
        return sprites;
    }

    public void setSprites(PokemonSprites sprites) {
        this.sprites = sprites;
    }

    // 内部クラス
    public static class PokemonType {
        private TypeInfo type;

        public PokemonType() {
        }

        public PokemonType(TypeInfo type) {
            this.type = type;
        }

        public TypeInfo getType() {
            return type;
        }

        public void setType(TypeInfo type) {
            this.type = type;
        }

        public static class TypeInfo {
            private String name;

            public TypeInfo() {
            }

            public TypeInfo(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    public static class PokemonAbility {
        private AbilityInfo ability;
        @JsonProperty("is_hidden")
        private boolean isHidden;

        public PokemonAbility() {
        }

        public PokemonAbility(AbilityInfo ability, boolean isHidden) {
            this.ability = ability;
            this.isHidden = isHidden;
        }

        public AbilityInfo getAbility() {
            return ability;
        }

        public void setAbility(AbilityInfo ability) {
            this.ability = ability;
        }

        public boolean isHidden() {
            return isHidden;
        }

        public void setHidden(boolean hidden) {
            isHidden = hidden;
        }

        public static class AbilityInfo {
            private String name;

            public AbilityInfo() {
            }

            public AbilityInfo(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    public static class PokemonSprites {
        @JsonProperty("front_default")
        private String frontDefault;
        private OtherSprites other;

        public PokemonSprites() {
        }

        public PokemonSprites(String frontDefault, OtherSprites other) {
            this.frontDefault = frontDefault;
            this.other = other;
        }

        public String getFrontDefault() {
            return frontDefault;
        }

        public void setFrontDefault(String frontDefault) {
            this.frontDefault = frontDefault;
        }

        public OtherSprites getOther() {
            return other;
        }

        public void setOther(OtherSprites other) {
            this.other = other;
        }

        public static class OtherSprites {
            @JsonProperty("official-artwork")
            private OfficialArtwork officialArtwork;

            public OtherSprites() {
            }

            public OtherSprites(OfficialArtwork officialArtwork) {
                this.officialArtwork = officialArtwork;
            }

            public OfficialArtwork getOfficialArtwork() {
                return officialArtwork;
            }

            public void setOfficialArtwork(OfficialArtwork officialArtwork) {
                this.officialArtwork = officialArtwork;
            }

            public static class OfficialArtwork {
                @JsonProperty("front_default")
                private String frontDefault;

                public OfficialArtwork() {
                }

                public OfficialArtwork(String frontDefault) {
                    this.frontDefault = frontDefault;
                }

                public String getFrontDefault() {
                    return frontDefault;
                }

                public void setFrontDefault(String frontDefault) {
                    this.frontDefault = frontDefault;
                }
            }
        }
    }
}
