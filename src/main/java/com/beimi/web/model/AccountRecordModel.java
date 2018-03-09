package com.beimi.web.model;

import com.beimi.util.UKTools;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fanling on 2018/2/9.
 */
@Entity
@Table(name = "bm_accountrecord")
@org.hibernate.annotations.Proxy(lazy = false)
public class AccountRecordModel {
    private static final long serialVersionUID = 12340971213L;

    @Id
    private String id = UKTools.getUUID().toLowerCase();

    private String userid;

    private String point;

    private String point_desc;

    private Date createtime = new Date();

    public AccountRecordModel(){}

    public AccountRecordModel(String userid, String point, String point_desc){
        this.userid = userid;
        this.point = point;
        this.point_desc = point_desc;
    }

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

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public void setPoint_desc(String point_desc) {
        this.point_desc = point_desc;
    }

    public String getUserid() {
        return userid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public String getPoint() {
        return point;
    }

    public String getPoint_desc() {
        return point_desc;
    }
}
