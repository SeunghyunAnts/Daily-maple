package com.example.dailymaple;

public class CharacterInfo {
    private String character_id;
    private String nickname;
    private String img_url;
//    private int level;
    public CharacterInfo(String character_id, String nickname, String img_url){
        this.character_id = character_id;
        this.nickname = nickname;
        this.img_url = img_url;
//        this.level = level;
    }
    public CharacterInfo(String character_id, String nickname){
        this.character_id = character_id;
        this.nickname = nickname;
        this.img_url = "";
//        this.level = level;
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
//    public void setLevel(int level){
//        this.level = level;
//    }
    public String getCharacterId(){
        return this.character_id;
    }
    public String getNickname(){
        return this.nickname;
    }
    public String getImgUrl(){
        return this.img_url;
    }
//    public int getLevel(){
//        return  this.level;
//    }

}
