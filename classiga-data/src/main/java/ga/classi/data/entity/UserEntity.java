package ga.classi.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author eatonmunoz
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter 
@Getter
@Entity
@Table(name = UserEntity.TABLE_NAME, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"lower_username"}),
    @UniqueConstraint(columnNames = {"lower_email"})
})
@DynamicInsert
@DynamicUpdate
public class UserEntity extends BaseEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "mn_user";

    @Column(name = "first_name", length = 128, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 128)
    private String lastName;

    @Column(name = "username", length = 128, nullable = false)
    private String username;

    @Column(name = "email", length = 128, nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @JsonIgnore
    @Column(name = "salt", length = 32, nullable = false)
    private String salt;

    @Column(name = "active", length = 1, nullable = false)
    private String active;

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
    
}
