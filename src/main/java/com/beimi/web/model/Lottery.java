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
    private String no;//期数
    private String preno;//上一期数
    private String prenum;//开奖数字
    private String nexttime;//开奖时间
    private String nexttimestr;//
    private String nowtime;//系统当前时间

    public Lottery(String no,String preno,String prenum,String nexttime,String nexttimestr,String nowtime){
        this.no = no;
        this.preno = preno;
        this.prenum = prenum;
        this.nexttime = nexttime;
        this.nexttimestr = nexttimestr;
        this.nowtime = nowtime;
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

    public String getNexttime() {
        return nexttime;
    }

    public String getNexttimestr() {
        return nexttimestr;
    }

    public String getNo() {
        return no;
    }

    public String getNowtime() {
        return nowtime;
    }

    public String getPreno() {
        return preno;
    }

    public String getPrenum() {
        return prenum;
    }

    public void setNexttime(String nexttime) {
        this.nexttime = nexttime;
    }

    public void setNexttimestr(String nexttimestr) {
        this.nexttimestr = nexttimestr;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setNowtime(String nowtime) {
        this.nowtime = nowtime;
    }

    public void setPreno(String preno) {
        this.preno = preno;
    }

    public void setPrenum(String prenum) {
        this.prenum = prenum;
    }
}
