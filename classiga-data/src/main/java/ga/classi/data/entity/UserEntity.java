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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author eatonmunoz
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter 
@Getter
@Entity
@Table(name = UserEntity.TABLE_NAME, 
    indexes = {
        @Index(columnList = "deleted"),
        @Index(columnList = "deleted, id"),
        @Index(columnList = "deleted, lower_full_name"),
        @Index(columnList = "deleted, lower_username"),
        @Index(columnList = "deleted, lower_email"),
        @Index(columnList = "lower_full_name"),
        @Index(columnList = "lower_username"),
        @Index(columnList = "lower_email")
    })
@DynamicInsert
@DynamicUpdate
public class UserEntity extends BaseEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "t_user";

    @Column(name = "full_name", length = 128, nullable = false)
    private String fullName;
    
    @Column(name = "username", length = 128, nullable = false)
    private String username;

    @Column(name = "email", length = 128, nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 64, nullable = false)
    private String password;

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

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_group_id", referencedColumnName = "id")
    private UserGroupEntity userGroup;
    
    public UserEntity(Long id) {
        super(id);
    }

    @Override
    protected void setValuesOnCreate() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void setValuesOnUpdate() {
        // TODO Auto-generated method stub
        
    }
    
}
