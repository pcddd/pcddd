package com.beimi.web.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "bm_game_playway")
@org.hibernate.annotations.Proxy(lazy = false)
public class GamePlayway implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8988042477190235024L;
	private String id ;
	private String name ;
	private String code ;
	private Date createtime ;
	private String creater;


    private String detail;//详情
	
	private int score;
	private int mincoins ;	//最小金币数量
	private int maxcoins ;	//最大金币数量
	
	private Date updatetime ;
	private int gametype ;

	private int players ;	//游戏人数

	private int roomtype;
	private String roomtitle ; 	//玩法标题

	private String img;//背景图片

	private String section_1;
	private String section_2;
	private String section_3;
	private int composability; //组合率

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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public void setGametype(int gametype) {
		this.gametype = gametype;
	}

	public int getGametype() {
		return gametype;
	}

	public void setComposability(int composability) {
		this.composability = composability;
	}

	public int getComposability() {
		return composability;
	}

	public int getPlayers() {
		return players;
	}
	public void setPlayers(int players) {
		this.players = players;
	}

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getRoomtype() {
		return roomtype;
	}
	public void setRoomtype(int roomtype) {
		this.roomtype = roomtype;
	}

	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getMincoins() {
		return mincoins;
	}
	public void setMincoins(int mincoins) {
		this.mincoins = mincoins;
	}
	public int getMaxcoins() {
		return maxcoins;
	}
	public void setMaxcoins(int maxcoins) {
		this.maxcoins = maxcoins;
	}


	public String getRoomtitle() {
		return roomtitle;
	}

	public void setRoomtitle(String roomtitle) {
		this.roomtitle = roomtitle;
	}

	public void setSection_1(String section_1) {
		this.section_1 = section_1;
	}

	public void setSection_2(String section_2) {
		this.section_2 = section_2;
	}

	public void setSection_3(String section_3) {
		this.section_3 = section_3;
	}

	public String getSection_1() {
		return section_1;
	}

	public String getSection_2() {
		return section_2;
	}

	public String getSection_3() {
		return section_3;
	}
}
