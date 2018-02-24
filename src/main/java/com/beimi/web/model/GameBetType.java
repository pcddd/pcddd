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

    private String value;

    private String orgi;

    private String roomtype;

    private int minbet;

    private int maxbet;

    public void setMinbet(int minbet) {
        this.minbet = minbet;
    }

    public int getMinbet() {
        return minbet;
    }

    public void setMaxbet(int maxbet) {
        this.maxbet = maxbet;
    }

    public int getMaxbet() {
        return maxbet;
    }

    private String type; // 1-大小单双 2-猜数字 3-特殊玩法

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOrgi(String orgi) {
        this.orgi = orgi;
    }

    public String getOrgi() {
        return orgi;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
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
