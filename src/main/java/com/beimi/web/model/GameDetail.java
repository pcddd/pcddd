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
@Document(indexName = "beimi", type = "uk_game_detail")
@Entity
public class GameDetail {
    private static final long serialVersionUID = 1L;

    @Id
    private String id = UKTools.getUUID().toLowerCase();

    private String userId;

    private int diamonds;		//钻石数量

    public String lotterType;//投注类型

    public String periods;//期数

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public String getLotterType() {
        return lotterType;
    }

    public void setLotterType(String lotterType) {
        this.lotterType = lotterType;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }
}
