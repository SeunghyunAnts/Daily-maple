package com.example.dailymaple;

public class CharacterInfo {
    private String nickname;
//    private int level;
    public CharacterInfo(String nickname){
        this.nickname = nickname;
//        this.level = level;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
//    public void setLevel(int level){
//        this.level = level;
//    }
    public String getNickname(){
        return this.nickname;
    }
//    public int getLevel(){
//        return  this.level;
//    }

}
