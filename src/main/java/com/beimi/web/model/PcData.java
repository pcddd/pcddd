package com.beimi.web.model;

public class PcData implements java.io.Serializable{

    private static final long serialVersionUID = -2710593844038107782L;
    private boolean status ;
    private String msg ;
    private Object data ;
    private String code;

    public PcData(boolean status , String msg , Object data){
        this.status = status ;
        this.msg = msg ;
        this.data = data ;
    }

    public PcData(boolean status ,String code, String msg , Object data){
        this.status = status ;
        this.code=code;
        this.msg = msg ;
        this.data = data ;
    }


    public PcData(String msg , String code ){
        this.msg = msg ;
        this.code = code ;
    }

    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
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
