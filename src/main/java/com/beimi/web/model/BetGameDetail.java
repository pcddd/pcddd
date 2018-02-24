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

    private String tokenId;

    private int diamonds;		//钻石数量

    public String lotterTypeId;//投注类型

    public int periods;//期数

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

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }
}
