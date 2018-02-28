package com.beimi.web.model;

import com.beimi.util.UKTools;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanling on 2018/2/9.
 */
@Document(indexName = "beimi", type = "uk_game_betdetail")
@Entity
public class BetGameDetail {
    private static final long serialVersionUID = 1L;

    @Id
    private String id = UKTools.getUUID().toLowerCase();

    private int type; // 1北京 2加拿大

    private String orgi;// 标识

    private String tokenId;

//    private int goldcoins;		//下注金额
//
//    private String lotterTypeId;//投注类型
//
//    private String lotterName;//投注类型名称

    private ArrayList<PcBetEntity> pcBetEntityList;

    public int periods;//期数

    private int status = -1; //0未开奖 1已开奖

//    public PlayUser playUser;//用户信息

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "assigned")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPcBetEntityList(ArrayList<PcBetEntity> pcBetEntityList) {
        this.pcBetEntityList = pcBetEntityList;
    }

    public ArrayList<PcBetEntity> getPcBetEntityList() {
        return pcBetEntityList;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrgi() {
        return orgi;
    }

    public void setOrgi(String orgi) {
        this.orgi = orgi;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

//    public int getGoldcoins() {
//        return goldcoins;
//    }
//
//    public void setGoldcoins(int diamonds) {
//        this.goldcoins = diamonds;
//    }
//
//    public String getLotterTypeId() {
//        return lotterTypeId;
//    }
//
//    public void setLotterTypeId(String lotterTypeId) {
//        this.lotterTypeId = lotterTypeId;
//    }
//
//    public String getLotterName() {
//        return lotterName;
//    }
//
//    public void setLotterName(String lotterName) {
//        this.lotterName = lotterName;
//    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

//    public PlayUser getPlayUser() {
//        return playUser;
//    }

//    public void setPlayUser(PlayUser playUser) {
//        this.playUser = playUser;
//    }
}
