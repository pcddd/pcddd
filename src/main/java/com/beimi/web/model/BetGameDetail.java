package com.beimi.web.model;

import com.beimi.util.UKTools;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

    private int diamonds;		//下注金额

    public String lotterTypeId;//投注类型

    public String lotterName;//投注类型名称

    public int periods;//期数

    public PlayUser playUser;//用户信息

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

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public String getLotterTypeId() {
        return lotterTypeId;
    }

    public void setLotterTypeId(String lotterTypeId) {
        this.lotterTypeId = lotterTypeId;
    }

    public String getLotterName() {
        return lotterName;
    }

    public void setLotterName(String lotterName) {
        this.lotterName = lotterName;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public PlayUser getPlayUser() {
        return playUser;
    }

    public void setPlayUser(PlayUser playUser) {
        this.playUser = playUser;
    }
}
