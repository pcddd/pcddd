package com.beimi.web.model;

import com.beimi.util.UKTools;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "bm_message_sys")
@org.hibernate.annotations.Proxy(lazy = false)
public class SysMesModel {

    @Id
    private String id = UKTools.getUUID().toLowerCase();

    private String content;

    private String title;

    private int create_time;

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
