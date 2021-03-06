package com.beimi.web.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import com.beimi.util.UKTools;
import com.beimi.util.event.UserEvent;


/**
 * @author jaddy0302 Rivulet User.java 2010-3-17
 * 
 */
@Document(indexName = "beimi", type = "uk_playuser")
@Entity
@Table(name = "bm_playuser")
@org.hibernate.annotations.Proxy(lazy = false)
public class PlayUser implements UserEvent , java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
    @Id
	private String id = UKTools.getUUID().toLowerCase();
	
	private String username ;
	private String password ;
	private String email ;
	private String uname ;

	private String gender;
	private String mobile ;
	private String nickname ;
	private String secureconf = "5";
	private String usertype ; //

	private String country;
	private String region;
	private String useragent ;
	private String isp;
	
	private String playertype ;

	private int level;

	private String level_name;

	private String orgi ;
	private String creater;
	private Date createtime = new Date();
	private Date updatetime = new Date();
	private Date passupdatetime = new Date();

	private String token;

	private String openid ;	//微信
	private String qqid ;
	
	private String ostype ;	//客户端类型 IOS/ANDROID
	private String browser ;//客户端浏览器
	
	private String memo;
	private String city ;	//城市
	private String province ;//省份
	private boolean login ;		//是否登录
	private boolean online ; 	//是否在线
	private String status ;		//
	
	private int disabled ;	//是否禁用
	
	private boolean datastatus ;//数据状态，是否已删除	
	private boolean headimg ; 	//是否上传头像
	
	private String playerlevel ;//玩家级别 ， 等级
	private int experience  ;	//玩家经验
	private boolean secquestion ;//是否设置密保问题
	
	
	
	private Date lastlogintime = new Date();	//最后登录时间
	

	private int cards;			//房卡数量
	private int goldcoins;		//金币数量
	private int diamonds;		//钻石数量

	private String lotterType;//投注类型

	private String periods;//期数

	private String personalword;

	private String withdrawals_password;

	private String real_name;

	private String bank_name;

	private String bank_no;
	private String open_card_address;
	private String headportrait;

	public void setHeadportrait(String headportrait) {
		this.headportrait = headportrait;
	}

	public String getHeadportrait() {
		return headportrait;
	}

	public void setOpen_card_address(String open_card_address) {
		this.open_card_address = open_card_address;
	}

	public String getOpen_card_address() {
		return open_card_address;
	}

	public void setBank_no(String bank_no) {
		this.bank_no = bank_no;
	}

	public String getBank_no() {
		return bank_no;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setWithdrawals_password(String withdrawals_password) {
		this.withdrawals_password = withdrawals_password;
	}

	public String getWithdrawals_password() {
		return withdrawals_password;
	}

	/**
	 *对金币+房卡+id进行RSA签名 ， 任何对ID,cards ， goldcoins 进行修改之前，都需要做签名验证，
	 *签名验证通过后才能进行修改，修改之后，重新签名 
	 */
	private String sign ;

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	/**
	 * @return the id
	 */
	@Id
	@Column(length = 32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "assigned")	
	public String getId() {
		return id;
	}

	public String getLevel_name() {
		return level_name;
	}

	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}

	public void setPersonalword(String personalword) {
		this.personalword = personalword;
	}

	public String getPersonalword() {
		return personalword;
	}

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "lotter_type")
	public String getLotterType() {
		return lotterType;
	}

	public void setLotterType(String lotterType) {
		this.lotterType = lotterType;
	}

	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public String getUname() {
		return uname;
	}


	public void setUname(String uname) {
		this.uname = uname;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public String getSecureconf() {
		return secureconf;
	}


	public void setSecureconf(String secureconf) {
		this.secureconf = secureconf;
	}


	public String getUsertype() {
		return usertype;
	}


	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}


	public String getOrgi() {
		return orgi;
	}


	public void setOrgi(String orgi) {
		this.orgi = orgi;
	}


	public String getCreater() {
		return creater;
	}


	public void setCreater(String creater) {
		this.creater = creater;
	}


	public Date getCreatetime() {
		return createtime;
	}


	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}


	public Date getPassupdatetime() {
		return passupdatetime;
	}


	public void setPassupdatetime(Date passupdatetime) {
		this.passupdatetime = passupdatetime;
	}


	public Date getUpdatetime() {
		return updatetime;
	}


	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}


	public String getMemo() {
		return memo;
	}


	public void setMemo(String memo) {
		this.memo = memo;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getProvince() {
		return province;
	}


	public void setProvince(String province) {
		this.province = province;
	}


	public boolean isLogin() {
		return login;
	}


	public void setLogin(boolean login) {
		this.login = login;
	}


	public boolean isOnline() {
		return online;
	}


	public void setOnline(boolean online) {
		this.online = online;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public boolean isDatastatus() {
		return datastatus;
	}


	public void setDatastatus(boolean datastatus) {
		this.datastatus = datastatus;
	}


	public boolean isHeadimg() {
		return headimg;
	}


	public void setHeadimg(boolean headimg) {
		this.headimg = headimg;
	}


	public String getPlayerlevel() {
		return playerlevel;
	}


	public void setPlayerlevel(String playerlevel) {
		this.playerlevel = playerlevel;
	}


	public int getExperience() {
		return experience;
	}


	public void setExperience(int experience) {
		this.experience = experience;
	}


	public boolean isSecquestion() {
		return secquestion;
	}


	public void setSecquestion(boolean secquestion) {
		this.secquestion = secquestion;
	}


	public Date getLastlogintime() {
		return lastlogintime;
	}


	public void setLastlogintime(Date lastlogintime) {
		this.lastlogintime = lastlogintime;
	}


	public void setId(String id) {
		this.id = id;
	}


	public int getCards() {
		return cards;
	}


	public void setCards(int cards) {
		this.cards = cards;
	}


	public int getGoldcoins() {
		return goldcoins;
	}


	public void setGoldcoins(int goldcoins) {
		this.goldcoins = goldcoins;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}


	public int getDiamonds() {
		return diamonds;
	}


	public void setDiamonds(int diamonds) {
		this.diamonds = diamonds;
	}


	public String getOpenid() {
		return openid;
	}


	public void setOpenid(String openid) {
		this.openid = openid;
	}


	public String getQqid() {
		return qqid;
	}


	public void setQqid(String qqid) {
		this.qqid = qqid;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getRegion() {
		return region;
	}


	public void setRegion(String region) {
		this.region = region;
	}


	public String getIsp() {
		return isp;
	}


	public void setIsp(String isp) {
		this.isp = isp;
	}


	public String getOstype() {
		return ostype;
	}


	public void setOstype(String ostype) {
		this.ostype = ostype;
	}


	public int isDisabled() {
		return disabled;
	}


	public void setDisabled(int disabled) {
		this.disabled = disabled;
	}


	public String getUseragent() {
		return useragent;
	}


	public void setUseragent(String useragent) {
		this.useragent = useragent;
	}


	public String getBrowser() {
		return browser;
	}


	public void setBrowser(String browser) {
		this.browser = browser;
	}


	public String getPlayertype() {
		return playertype;
	}


	public void setPlayertype(String playertype) {
		this.playertype = playertype;
	}
}
