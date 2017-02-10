package no.rogfk.consultant.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;


@ApiModel
@Entry(objectClasses = {"inetOrgPerson", "organizationalPerson", "person", "top", "brfkInfo", "brfkConsultant"})
public final class Consultant {

    @Id
    @ApiModelProperty(value = "This will be automatically constructed", hidden = true)
    private Name dn;

    @Attribute(name = "cn")
    @ApiModelProperty(value = "This will be automatically generated", hidden = true)
    private String cn;

    @Attribute(name = "sn")
    private String lastName;

    @Attribute(name = "mobile")
    private String mobile;

    @Attribute(name = "givenname")
    private String firstName;

    @Attribute(name = "l")
    private String organization;

    @Attribute(name = "mail")
    private String mail;

    @Attribute(name = "userPassword")
    private String password;

    @Attribute(name = "brfkOwner")
    private Name owner;

    @ApiModelProperty(value = "This will be automatically set", hidden = true)
    @Attribute(name = "loginDisabled")
    private boolean loginDisabled;

    @Attribute(name = "brfkConsultantInviteTimeStamp")
    @ApiModelProperty(value = "This will be automatically generated", hidden = true)
    private String inviteTimeStamp;

    @Attribute(name = "brfkConsultantState")
    @ApiModelProperty(value = "This will be automatically generated", hidden = true)
    private String state;


    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }


    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoginDisabled() {
        return loginDisabled;
    }

    public void setLoginDisabled(boolean loginDisabled) {
        this.loginDisabled = loginDisabled;
    }

    public String getInviteTimeStamp() {
        return inviteTimeStamp;
    }

    public void setInviteTimeStamp(String inviteTimeStamp) {
        this.inviteTimeStamp = inviteTimeStamp;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDn() {
        return dn.toString();
    }

    public void setDn(String dn) {
        this.dn = LdapNameBuilder.newInstance(dn).build();
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public String getOwner() {
        if (owner != null) {
            return owner.toString();
        } else {
            return null;
        }
    }

    public void setOwner(String owner) {
        if (owner != null) {
            this.owner = LdapNameBuilder.newInstance(owner).build();
        } else {
            this.owner = null;
        }
    }

    public void setOwner(Name owner) {
        this.owner = owner;
    }


}
