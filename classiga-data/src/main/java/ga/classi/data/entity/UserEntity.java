/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import ga.classi.commons.constant.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

// https://stackoverflow.com/questions/2292662/how-important-is-the-order-of-columns-in-indexes
// https://use-the-index-luke.com/sql/where-clause/the-equals-operator/concatenated-keys

/**
 *
 * @author muhammad
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = UserEntity.TABLE_NAME, 
    indexes = {
        @Index(columnList = "deleted"),
        @Index(columnList = "id, deleted"),
        @Index(columnList = "lower_full_name"),
        @Index(columnList = "lower_full_name, deleted"),
        @Index(columnList = "lower_username"),
        @Index(columnList = "lower_username, deleted"),
        @Index(columnList = "lower_email"),
        @Index(columnList = "lower_email, deleted")
    })
@DynamicInsert
@DynamicUpdate
public class UserEntity extends BaseEntity implements Serializable {

    /**
     *
     */
    private static final long  serialVersionUID  = 1L;
    
    public static final String TABLE_NAME        = "t_user";
    
    public static final String F_FULL_NAME       = "fullName";
    public static final String F_USERNAME        = "username";
    public static final String F_EMAIL           = "email";
    public static final String F_PASSWORD_HASH   = "passwordHash";
    public static final String F_SALT            = "salt";
    public static final String F_ACTIVE          = "active";
    public static final String F_LOWER_FULL_NAME = "lowerFullName";
    public static final String F_LOWER_USERNAME  = "lowerUsername";
    public static final String F_LOWER_EMAIL     = "lowerEmail";
    public static final String F_USER_GROUP      = "userGroup";

    @Column(name = "full_name", length = 128, nullable = false)
    private String fullName;
    
    @Column(name = "username", length = 128, nullable = false)
    private String username;

    @Column(name = "email", length = 128, nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "password_hash", length = 64, nullable = false)
    private String passwordHash;

    @JsonIgnore
    @Column(name = "salt", length = 32)
    private String salt;

    @Column(name = "active", length = 1, nullable = false)
    private String active;

    @JsonIgnore
    @Column(name = "lower_full_name", length = 128, nullable = false)
    private String lowerFullName;

    @JsonIgnore
    @Column(name = "lower_username", length = 128, nullable = false)
    private String lowerUsername;

    @JsonIgnore
    @Column(name = "lower_email", length = 128, nullable = false)
    private String lowerEmail;

    @Column(name = "avatar", length = 64)
    private String avatar;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_group_id", referencedColumnName = "id")
    private UserGroupEntity userGroup;
    
    public UserEntity(Long id) {
        super(id);
    }

    @Override
    protected void setValuesOnCreate() {
        lowerFullName = fullName.toLowerCase();
        lowerUsername = username.toLowerCase();
        lowerEmail = email.toLowerCase();
        active = CommonConstants.YES;
    }

    @Override
    protected void setValuesOnUpdate() {
        lowerFullName = fullName.toLowerCase();
        lowerUsername = username.toLowerCase();
        lowerEmail = email.toLowerCase();        
    }

    @Override
    protected StringBuilder getHashElements() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.fullName);
        sb.append(this.username);
        sb.append(this.email);
        sb.append(this.passwordHash);
        sb.append(StringUtils.defaultString(this.salt));
        sb.append(this.active);
        sb.append(this.lowerFullName);
        sb.append(this.lowerEmail);
        sb.append(this.lowerUsername);
        sb.append(StringUtils.defaultString(this.avatar));
        sb.append(this.userGroup.getId());
        return sb;
    }
    
}
