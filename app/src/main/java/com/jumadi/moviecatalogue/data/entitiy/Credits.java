package com.jumadi.moviecatalogue.data.entitiy;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Credits {

    @SerializedName("cast")
    private List<CastEntity> casts;
    @SerializedName("crew")
    private List<CrewEntity> crews;

    public Credits() {}

    public Credits(List<CastEntity> casts, List<CrewEntity> crews) {
        this.casts = casts;
        this.crews = crews;
    }

    public List<CastEntity> getCasts() {
        return casts;
    }

    public void setCasts(List<CastEntity> casts) {
        this.casts = casts;
    }

    public List<CrewEntity> getCrews() {
        return crews;
    }

    public void setCrews(List<CrewEntity> crews) {
        this.crews = crews;
    }

    public static class CastEntity {

        @SerializedName("character")
        private String character;
        @SerializedName("name")
        private String  name;
        @SerializedName("profile_path")
        private String profilePath;

        public String getCharacter() {
            return character;
        }

        public void setCharacter(String character) {
            this.character = character;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePath() {
            return profilePath;
        }

        public void setProfilePath(String profilePath) {
            this.profilePath = profilePath;
        }

        @Override
        public String toString() {
            return "name: "+name+
                    "character: "+character+
                    "profile_path: "+profilePath;
        }
    }

    static class CrewEntity {

        @SerializedName("name")
        private String  name;
        @SerializedName("job")
        private String job;
        @SerializedName("profile_path")
        private String profilePath;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getprofilePath() {
            return profilePath;
        }

        public void setProfilePath(String profilePath) {
            this.profilePath = profilePath;
        }

        @Override
        public String toString() {
            return "name: "+name+
                    "job: "+job+
                    "profile_path: "+profilePath;
        }
    }
}
