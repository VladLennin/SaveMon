package com.example.SaveMon.enums;

public enum AgeZones {
    TEENAGERS("Teenagers",12,20),
    ADULT_1("ADULT(1 period)",20,36),
    ADULT_2("ADULT(2 period)",36,60),
    OLD("OLD",60,100);

    private String name;
    private int ageFrom;
    private int ageTo;

    public static AgeZones getByName(String name){
        for (int i = 0; i < AgeZones.values().length; i++) {
            if(AgeZones.values()[i].name.equals(name)){
                return AgeZones.values()[i];
            }
        }
        return TEENAGERS;
    }

    AgeZones(String name, int ageFrom, int ageTo) {
        this.name = name;
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
    }
    public String getName(){
        return name;
    }
}
