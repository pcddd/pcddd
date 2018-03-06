package com.beimi.web.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

@Document(indexName = "beimi", type = "uk_type_group")
@Entity
@Table(name = "bm_type_group")
@org.hibernate.annotations.Proxy(lazy = false)
public class GameBetType implements java.io.Serializable {

    private static final long serialVersionUID = -8988042477190235025L;

    private String id ;

    private String name;

    private String type;

    private String rescode;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRescode(String rescode) {
        this.rescode = rescode;
    }

    public String getName() {
        return name;
    }

    public String getRescode() {
        return rescode;
    }

    public String getType() {
        return type;
    }

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
