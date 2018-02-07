package com.beimi.web.model;

public class Lottery {
    private String no;
    private String preno;
    private String prenum;
    private String nexttime;
    private String nexttimestr;
    private String nowtime;

    public Lottery(String no,String preno,String prenum,String nexttime,String nexttimestr,String nowtime){
        this.no = no;
        this.preno = preno;
        this.prenum = prenum;
        this.nexttime = nexttime;
        this.nexttimestr = nexttimestr;
        this.nowtime = nowtime;
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
