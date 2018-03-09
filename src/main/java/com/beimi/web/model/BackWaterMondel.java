package com.beimi.web.model;


import com.beimi.util.UKTools;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bm_pcbackwater")
@org.hibernate.annotations.Proxy(lazy = false)
public class BackWaterMondel {
    private String id = UKTools.getUUID().toLowerCase();

    private int gametype;
    private int roomtype;
    private String userid;
    private long watergold;
    private Date createtime = new Date();
    private long totallosegold;
    private long totalwingold;
    private long totalbetgold;
    private int status = 0; //0待处理 1已处理 2不达标
    private int combinationgold;//组合
    private int bill;

    public void setBill(int bill) {
        this.bill = bill;
    }

    public int getBill() {
        return bill;
    }

    public long getTotallosegold() {
        return totallosegold;
    }

    public void setTotalbetgold(long totalbetgold) {
        this.totalbetgold = totalbetgold;
    }

    public void setTotallosegold(long totallosegold) {
        this.totallosegold = totallosegold;
    }

    public void setTotalwingold(long totalwingold) {
        this.totalwingold = totalwingold;
    }

    public long getTotalbetgold() {
        return totalbetgold;
    }

    public long getTotalwingold() {
        return totalwingold;
    }

    public void setCombinationgold(int combinationgold) {
        this.combinationgold = combinationgold;
    }

    public int getCombinationgold() {
        return combinationgold;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }


    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setRoomtype(int roomtype) {
        this.roomtype = roomtype;
    }

    public void setGametype(int gametype) {
        this.gametype = gametype;
    }

    public void setWatergold(long watergold) {
        this.watergold = watergold;
    }

    public String getUserid() {
        return userid;
    }

    public int getRoomtype() {
        return roomtype;
    }

    public int getGametype() {
        return gametype;
    }

    public long getWatergold() {
        return watergold;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  BackWaterMondel){
            BackWaterMondel rhs = (BackWaterMondel)obj;
            return rhs.getUserid().equals(userid);
        }
        return super.equals(obj);
    }
}
