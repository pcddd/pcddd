package com.beimi.web.model;

import com.beimi.util.UKTools;
import com.beimi.util.event.UserEvent;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "bm_pc_room")
@org.hibernate.annotations.Proxy(lazy = false)
public class PcRoomInfo implements UserEvent, java.io.Serializable{

    private static final long serialVersionUID = -89880424771902350L;

    private String id = UKTools.getUUID();
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

    private String orgi;
    private String roomtype;
    private String name;
    private String detail;
    private String roomid;
    private int players;
    private String img;
    private int maxgold;
    private int mingold;

    public void setMaxgold(int maxgold) {
        this.maxgold = maxgold;
    }

    public void setMingold(int mingold) {
        this.mingold = mingold;
    }

    public int getMaxgold() {
        return maxgold;
    }

    public int getMingold() {
        return mingold;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrgi(String orgi) {
        this.orgi = orgi;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public int getPlayers() {
        return players;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDetail() {
        return detail;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getOrgi() {
        return orgi;
    }

    public String getRoomid() {
        return roomid;
    }

    public String getRoomtype() {
        return roomtype;
    }
}
