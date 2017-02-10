package no.rogfk.consultant.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@ApiModel
@Entry(objectClasses = {"inetOrgPerson", "organizationalPerson", "person", "top", "brfkInfo"})
public final class HostEmployee {
    @Id
    @ApiModelProperty(value = "This will be automatically constructed", hidden = true)
    private Name dn;

    @Attribute(name = "fullname")
    private String fullname;

    @Attribute(name = "mobile")
    private String mobile;

    @Attribute(name = "mail")
    private String mail;

    public String getDn() {
        return dn.toString();
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
