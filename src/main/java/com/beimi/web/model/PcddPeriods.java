package com.beimi.web.model;

import com.beimi.util.UKTools;
import com.beimi.util.event.UserEvent;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
@Document(indexName = "beimi", type = "uk_game_pcddperiods")
@Entity
@Table(name = "bm_lotter")
@org.hibernate.annotations.Proxy(lazy = false)
public class PcddPeriods implements UserEvent, java.io.Serializable {

    private static final long serialVersionUID = 10000001L;
    @Id
    private String id = UKTools.getUUID().toLowerCase();
    private int type;
    private int periods;
    private int status;
    private String res;
    private String opentime;
    private String resname;
    private int colorid = 4;

    public PcddPeriods(int type,int periods,int status,String res,String opentime){
        this.type = type;
        this.periods = periods;
        this.status = status;
        this.res = res;
        this.opentime = opentime;
    }

    public void setColorid(int colorid) {
        this.colorid = colorid;
    }

    public int getColorid() {
        return colorid;
    }

    public PcddPeriods(){

    }

    public void setResname(String resname) {
        this.resname = resname;
    }

    public String getResname() {
        return resname;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setId(String  id) {
        this.id = id;
    }

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "assigned")
    public String  getId() {
        return id;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public int getType() {
        return type;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public int getPeriods() {
        return periods;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRes() {
        return res;
    }

    @Override
    public String toString() {
        return "***** " + type + "|" + periods + "|" + "|" + status + "|" + res + "|" + opentime;
    }
}
