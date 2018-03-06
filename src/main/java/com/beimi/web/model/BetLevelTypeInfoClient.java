package com.beimi.web.model;

public class BetLevelTypeInfoClient {
    
    private String bettypeid;
    private String orgi;
    private String roomtype;
    private String type;
    private String value;
    private int minbet;
    private int maxbet;

    //to client
    private String name;
    private String rescode;

    public BetLevelTypeInfoClient(){}

    public BetLevelTypeInfoClient( String bettypeid, String orgi, String roomtype, String type, String value, int minbet, int maxbet, String name, String rescode){
        this.bettypeid = bettypeid;
        this.orgi = orgi;
        this.roomtype = roomtype;
        this.type = type;
        this.value = value;
        this.minbet = minbet;
        this.maxbet = maxbet;
        this.name = name;
        this.rescode = rescode;
    }

    public BetLevelTypeInfoClient( BetLevelTypeInfo betLevelTypeInfo, String name, String rescode){
        this.bettypeid = betLevelTypeInfo.getId();
        this.orgi = betLevelTypeInfo.getOrgi();
        this.roomtype = betLevelTypeInfo.getRoomtype();
        this.type = betLevelTypeInfo.getType();
        this.value = betLevelTypeInfo.getValue();
        this.minbet = betLevelTypeInfo.getMinbet();
        this.maxbet = betLevelTypeInfo.getMaxbet();
        this.name = name;
        this.rescode = rescode;
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setRescode(String rescode) {
        this.rescode = rescode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRescode() {
        return rescode;
    }

    public String getName() {
        return name;
    }

    public void setOrgi(String orgi) {
        this.orgi = orgi;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public String getOrgi() {
        return orgi;
    }

    public int getMaxbet() {
        return maxbet;
    }

    public int getMinbet() {
        return minbet;
    }

    public String getValue() {
        return value;
    }

    public String getBettypeid() {
        return bettypeid;
    }

    public void setBettypeid(String bettypeid) {
        this.bettypeid = bettypeid;
    }

    public void setMaxbet(int maxbet) {
        this.maxbet = maxbet;
    }

    public void setMinbet(int minbet) {
        this.minbet = minbet;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

