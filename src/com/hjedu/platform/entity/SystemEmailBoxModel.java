package com.hjedu.platform.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * 邮箱配置表
 * 用户模块
 *
 */
@Entity
@Table(name = "y_systememailbox")
public class SystemEmailBoxModel
        implements Serializable {

    @Id
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "address", length = 600)
    private String address;
    @Column(name = "alias", length = 600)
    private String alias;
    @Column(name = "smtpHost", length = 400)
    private String smtpHost;
    @Column(name = "smtpUrn", length = 400)
    private String smtpUrn;
    @Column(name = "smtpPwd", length = 400)
    private String smtpPwd;
    @Column(name = "smtpPort", length = 400)
    private String smtpPort;
    @Column(name = "auth")
    private boolean auth;
    @Column(name = "ifSsl")
    private boolean ssl;

    public SystemEmailBoxModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSmtpHost() {
        return this.smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpUrn() {
        return this.smtpUrn;
    }

    public void setSmtpUrn(String smtpUrn) {
        this.smtpUrn = smtpUrn;
    }

    public String getSmtpPwd() {
        return this.smtpPwd;
    }

    public void setSmtpPwd(String smtpPwd) {
        this.smtpPwd = smtpPwd;
    }

    public String getSmtpPort() {
        return this.smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public boolean getAuth() {
        return this.auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean getSsl() {
        return this.ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }
}
