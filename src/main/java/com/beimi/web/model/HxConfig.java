package com.beimi.web.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "bm_hx_config")
@org.hibernate.annotations.Proxy(lazy = false)
public class HxConfig implements java.io.Serializable  {

    private static final long serialVersionUID = 5656780412103L;

    private String id;

    private String orgname;

    private String appname;

    private String clientid;

    private String clientsecret;

    private String token;

    public String getAppname() {
        return appname;
    }

    public String getClientid() {
        return clientid;
    }

    public String getClientsecret() {
        return clientsecret;
    }

    public String getOrgname() {
        return orgname;
    }

    public String getToken() {
        return token;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public void setClientid(String clientId) {
        this.clientid = clientId;
    }

    public void setClientsecret(String clientSecret) {
        this.clientid = clientSecret;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public void setToken(String token) {
        this.token = token;
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
}
