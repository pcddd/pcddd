package com.beimi.web.model;

import com.beimi.util.UKTools;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Document(indexName = "beimi", type = "uk_lotter")
@Entity
public class Lottery implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @Id
    private String id = UKTools.getUUID().toLowerCase();
    private int curNo;//期数

    private long nextOpenSec;//距离下一次开奖倒计时


    private String curRes;//开奖数字
    private String nextTime;//开奖时间

    private int stauts; //1正常 2封盘 3停售

    private int curPoint; // 当前元宝

    public Lottery(int curNo,String curRes,int stauts,int sec,String nextTime){
        this.curNo = curNo;
        this.curRes = curRes;
        this.nextOpenSec = sec;
        this.stauts = stauts;
        this.nextTime = nextTime;
    }
    public Lottery(){}
    /**
     * @return the id
     */
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "assigned")
    public String getId() {
        return id;
    }

    public void setNextOpenSec(long nextOpenSec) {
        this.nextOpenSec = nextOpenSec;
    }

    public long getNextOpenSec() {
        return nextOpenSec;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStauts(int stauts) {
        this.stauts = stauts;
    }

    public int getStauts() {
        return stauts;
    }

    public void setCurPoint(int curPoint) {
        this.curPoint = curPoint;
    }

    public int getCurPoint() {
        return curPoint;
    }

    public Lottery addNextSec(long sec){
        this.nextOpenSec = sec;
        return this;
    }

    public int getCurNo() {
        return curNo;
    }

    public void setCurNo(int curNo) {
        this.curNo = curNo;
    }

    public void setCurRes(String curRes) {
        this.curRes = curRes;
    }

    public String getCurRes() {
        return curRes;
    }

    public void setNextTime(String nextTime) {
        this.nextTime = nextTime;
    }

    public String getNextTime() {
        return nextTime;
    }


    @Override
    public String toString() {
        return "status=" + stauts + "|" +
                "curNo=" + curNo + "|" +
                "curRes=" + curRes + "|" +
                "nextTime=" + nextTime + "|" ;
//                "preStartTime=" + preStartTime + "|" +
//                "preEndTime=" + preEndTime;
    }
}
