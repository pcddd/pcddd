package com.beimi.web.model;

import java.beans.Transient;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PcBetEntity {
    private int goldcoins;		//下注金额

    private String betLotterTypeId;//投注类型id

    private String betLotterName;//投注类型名称

    private double getGold;        //中奖金额

    private Date createTime = new Date();        //下注时间

    private int isWin = -1;            //是否中奖 -1待开奖 1中奖 0未中

    private String realResult;    //开奖结果

    private String lotterName; //开奖类型

    private int periods; //期数

    private int orgi; //游戏类型

    private String newtime;

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

    public void setCreateTime(Date createTime) {
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

    public Date getCreateTime() {
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

    public void setBetLotterName(String betLotterName) {
        this.betLotterName = betLotterName;
    }

    public void setBetLotterTypeId(String betLotterTypeId) {
        this.betLotterTypeId = betLotterTypeId;
    }

    public String getBetLotterName() {
        return betLotterName;
    }

    public String getBetLotterTypeId() {
        return betLotterTypeId;
    }

    public int getGoldcoins() {
        return goldcoins;
    }

    public String getLotterName() {
        return lotterName;
    }

    @Transient
    public String getNewtime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.newtime = simpleDateFormat.format(this.getCreateTime());
        return newtime;
    }

    public void setNewtime(String newtime) {
        this.newtime = newtime;
    }

    @Override
    public String toString() {
        if (getGold > 0){
            //只记录胜利的记录
            String typeName;
            try{
                typeName = "单点:" + Integer.parseInt(betLotterName) + ",";
            }catch (Exception e){
                typeName = betLotterName + ",";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(orgi == 1 ? "北京28," : "加拿大28,")
                    .append(periods + "期,")
                    .append(typeName)
                    .append("下注" + goldcoins + "元宝,")
                    .append("中奖" + getGold + "元宝.");
            return sb.toString();
        }else{
            String typeName;
            try{
                typeName = "单点:" + Integer.parseInt(betLotterName) + ",";
            }catch (Exception e){
                typeName = betLotterName + ",";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(orgi == 1 ? "北京28," : "加拿大28,")
                    .append(periods + "期,")
                    .append(typeName)
                    .append("下注" + goldcoins + "元宝.");
            return sb.toString();
        }
    }
}
