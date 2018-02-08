package com.beimi.web.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

/**
 * Created by fanling on 2018/2/8.
 */
@Document(indexName = "beimi", type = "uk_type_group")
@Entity
@Table(name = "bm_type_group")
@org.hibernate.annotations.Proxy(lazy = false)
public class TypeGroup implements java.io.Serializable{
    private String id ;
    private String name;
    private String value;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
