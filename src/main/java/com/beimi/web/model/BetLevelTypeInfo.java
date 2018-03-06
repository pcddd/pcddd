package com.beimi.web.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "bm_bet_level")
@org.hibernate.annotations.Proxy(lazy = false)
public class BetLevelTypeInfo implements java.io.Serializable {

    private static final long serialVersionUID = -8988042477190235026L;

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "assigned")
    private String id;

    private String bettypeid;
    private String orgi;
    private String roomtype;
    private String type;
    private String value;
    private int minbet;
    private int maxbet;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
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

