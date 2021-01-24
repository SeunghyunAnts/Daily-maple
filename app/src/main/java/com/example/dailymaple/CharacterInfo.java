package com.example.dailymaple;

import java.io.Serializable;

public class CharacterInfo implements Serializable {
    private String character_id;
    private String nickname;
    private String img_url;
    private String level;
    public CharacterInfo(String character_id, String nickname, String img_url, String level){
        this.character_id = character_id;
        this.nickname = nickname;
        this.img_url = img_url;
        this.level = level;
    }
    public CharacterInfo(String character_id, String nickname){
        this.character_id = character_id;
        this.nickname = nickname;
        this.img_url = "";
        this.level = "";
    }
    public void setCharacterId(String character_id){
        this.character_id = character_id;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public void setImgUtl(String img_url){
        this.img_url = img_url;
    }
    public void setLevel(String level){
        this.level = level;
    }
    public String getCharacterId(){
        return this.character_id;
    }
    public String getNickname(){
        return this.nickname;
    }
    public String getImgUrl(){
        return this.img_url;
    }
    public String getLevel(){
        return  this.level;
    }

}
