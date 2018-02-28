package com.beimi.web.model;

public class PcBetEntity {
    private int goldcoins;		//下注金额

    private String lotterTypeId;//投注类型

    private String lotterName;//投注类型名称

    private double getGold;        //中奖金额

    private long createTime;        //下注时间

    private int isWin = -1;            //是否中奖 -1待开奖 1中奖 0未中

    private String realResult;    //开奖结果

    private int periods; //期数

    private int orgi; //游戏类型

    public void setOrgi(int orgi) {
        this.orgi = orgi;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public int getOrgi() {
        return orgi;
    }

    public int getPeriods() {
        return periods;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setGetGold(double getGold) {
        this.getGold = getGold;
    }

    public double getGetGold() {
        return getGold;
    }

    public int getIsWin() {
        return isWin;
    }

    public void setIsWin(int isWin) {
        this.isWin = isWin;
    }

    public void setRealResult(String realResult) {
        this.realResult = realResult;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getRealResult() {
        return realResult;
    }

    public void setGoldcoins(int goldcoins) {
        this.goldcoins = goldcoins;
    }

    public void setLotterName(String lotterName) {
        this.lotterName = lotterName;
    }

    public void setLotterTypeId(String lotterTypeId) {
        this.lotterTypeId = lotterTypeId;
    }

    public int getGoldcoins() {
        return goldcoins;
    }

    public String getLotterName() {
        return lotterName;
    }

    public String getLotterTypeId() {
        return lotterTypeId;
    }
}