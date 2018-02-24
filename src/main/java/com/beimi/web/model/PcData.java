package com.beimi.web.model;

public class PcData implements java.io.Serializable{

    private static final long serialVersionUID = -2710593844038107782L;
    private String msg ;
    private Object data ;
    private String code;

    public PcData(String code, String msg , Object data){
        this.code=code;
        this.msg = msg ;
        this.data = data ;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}
