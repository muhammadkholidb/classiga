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

import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author muhammad
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = UserSessionEntity.TABLE_NAME, 
    indexes = {
        @Index(columnList = "deleted"),
        @Index(columnList = "id, deleted"),
        @Index(columnList = "token"),
        @Index(columnList = "token, deleted")
    })
@DynamicInsert
@DynamicUpdate
public class UserSessionEntity extends BaseEntity implements Serializable {

    /**
     *
     */
    private static final long  serialVersionUID  = 1L;
    

    public static final String TABLE_NAME = "t_user_session";

    public static final String F_USER = "user";
    public static final String F_TOKEN = "token";
    public static final String F_USER_AGENT = "userAgent";
    public static final String F_IP_ADDRESS = "ipAddress";

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
    
    @Column(name = "token", length = 40, nullable = false)
    private String token;

    @Column(name = "user_agent", length = 256)
    private String userAgent;

    @Column(name = "ip_address", length = 48)
    private String ipAddress;

    @Override
    protected void setValuesOnCreate() {
    }

    @Override
    protected void setValuesOnUpdate() {
    }

    @Override
    protected StringBuilder getHashElements() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.user.getId());
        sb.append(this.token);
        sb.append(StringUtils.defaultString(this.userAgent));
        sb.append(StringUtils.defaultString(this.ipAddress));
        return sb;
    }
}
