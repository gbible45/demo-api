package com.demo.api.portal.users.domain;

import com.demo.api.framework.domain.AbstractUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PortalUser extends AbstractUser<PortalUser, Long> {

    private static final long serialVersionUID = 2716710947175132319L;

    public static final int NAME_MIN = 1;
    public static final int NAME_MAX = 50;

    @NotBlank(message = "{com.blank.name}", groups = {SignUpValidation.class, UpdateValidation.class})
    @Size(min=NAME_MIN, max=NAME_MAX, groups = {SignUpValidation.class, UpdateValidation.class})
    @Column(nullable = false, length = NAME_MAX)
    private String name;

    @Column(nullable = false, columnDefinition = "char(1) default 'N'")
    private String changePasswordYn = "N";

    @Column(nullable = false, columnDefinition = "char(1) default 'N'")
    private String delUserYn = "N";

    public PortalUser() {}

    public PortalUser(long id) {
        setIdForClient(id);
    }

    public PortalUser(String email) {
        super.setEmail(email);
    }

    public PortalUser(String email, String password, String name) {
        super(email, password);
        this.name = name;
    }

    public PortalUser(String email, String password, String name, String role) {
        super(email, password);
        this.name = name;
        this.getRoles().add(role);
    }

    public PortalUser(String email, String password, String name, Set<String> roles) {
        super(email, password);
        this.name = name;
        this.setRoles(roles);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChangePasswordYn() {
        return changePasswordYn;
    }

    public void setChangePasswordYn(String changePasswordYn) {
        this.changePasswordYn = changePasswordYn;
    }

    public String getDelUserYn() {
        return delUserYn;
    }

    public void setDelUserYn(String delUserYn) {
        this.delUserYn = delUserYn;
    }

    public static Page<PortalUser> toDTO(Page<PortalUser> poPage) {
        for(PortalUser user : poPage.getContent()) {
            user.toDTO();
        }
        return poPage;
    }

    public static List<PortalUser> toDTO(List<PortalUser> portalUsers) {
        for(PortalUser user : portalUsers) {
            user.toDTO();
        }
        return portalUsers;
    }

    public PortalUser toDTO() {
        this.decorate(this).hideConfidentialFields();
        return this;
    }

    @Override
    public String toString() {
        return "PortalUser{" +
                "name='" + name + '\'' +
                ", username=" + getUsername() +
                ", roles=" + getRoles() +
                '}';
    }

}