package com.beimi.web.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "bm_type_bet")
@org.hibernate.annotations.Proxy(lazy = false)
public class GameBetFatherType implements java.io.Serializable {

    private static final long serialVersionUID = -8988042477190235026L;

    private String id;
    private String name;
    private String betchild;
    private int minbet;
    private int maxbet;

    public void setName(String name) {
        this.name = name;
    }

    public void setBetchild(String betchild) {
        this.betchild = betchild;
    }

    public void setMaxbet(int maxbet) {
        this.maxbet = maxbet;
    }

    public void setMinbet(int minbet) {
        this.minbet = minbet;
    }

    public String getName() {
        return name;
    }

    public int getMaxbet() {
        return maxbet;
    }

    public int getMinbet() {
        return minbet;
    }

    public String getBetchild() {
        return betchild;
    }

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
