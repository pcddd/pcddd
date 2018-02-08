package com.beimi.web.model;

import com.beimi.util.UKTools;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Document(indexName = "beimi", type = "uk_lotter")
@Entity
public class Lottery {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @Id
    private String id = UKTools.getUUID().toLowerCase();
    private int no;//期数
    private int preno;//上一期数
    private String prenum;//开奖数字
    private String nexTime;//开奖时间
    private String preStartTime;//
    private String preEndTime;//
//    private String nowtime;//系统当前时间

    public Lottery(int no,int preno,String prenum,String nexttime,String preStartTime,String preEndTime){
        this.no = no;
        this.preno = preno;
        this.prenum = prenum;
        this.nexTime = nexttime;
        this.preStartTime = preStartTime;
        this.preEndTime = preEndTime;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getNexTime() {
        return nexTime;
    }

    public String getPreEndTime() {
        return preEndTime;
    }

    public String getPreStartTime() {
        return preStartTime;
    }

    public int getNo() {
        return no;
    }

//    public String getNowtime() {
//        return nowtime;
//    }

    public int getPreno() {
        return preno;
    }

    public String getPrenum() {
        return prenum;
    }

    public void setNexTime(String nexTime) {
        this.nexTime = nexTime;
    }

    public void setPreEndTime(String preEndTime) {
        this.preEndTime = preEndTime;
    }

    public void setPreStartTime(String preStartTime) {
        this.preStartTime = preStartTime;
    }

    public void setNo(int no) {
        this.no = no;
    }

//    public void setNowtime(String nowtime) {
//        this.nowtime = nowtime;
//    }

    public void setPreno(int preno) {
        this.preno = preno;
    }

    public void setPrenum(String prenum) {
        this.prenum = prenum;
    }

    @Override
    public String toString() {
        return "no=" + no + "|" +
                "preno=" + preno + "|" +
                "prenum=" + prenum + "|" +
                "nextTime=" + nexTime + "|" +
                "preStartTime=" + preStartTime + "|" +
                "preEndTime=" + preEndTime;
    }
}
