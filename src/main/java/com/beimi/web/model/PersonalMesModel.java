package com.beimi.web.model;

import com.beimi.util.UKTools;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "bm_message_personl")
@org.hibernate.annotations.Proxy(lazy = false)
public class PersonalMesModel {

    @Id
    private String id = UKTools.getUUID().toLowerCase();

    private String content;

    private String title;

    private int create_time;

    private String userid;

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

    public String getUserid() {
        return userid;
    }

    public int getCreate_time() {
        return create_time;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
