package ga.classi.data.entity;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = UserGroupEntity.TABLE_NAME, 
    indexes = {
        @Index(columnList = "deleted"),
        @Index(columnList = "deleted, id"),
        @Index(columnList = "deleted, lower_name"),
        @Index(columnList = "lower_name")
    })
@DynamicInsert
@DynamicUpdate
public class UserGroupEntity extends BaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "t_user_group";
   
    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @JsonIgnore
    @Column(name = "lower_name", length = 128, nullable = false)
    private String lowerName;

    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "active", length = 1, nullable = false)
    private String active;

    @JsonBackReference
    @OneToMany(mappedBy = "userGroup")
    private List<UserEntity> users;

    @JsonBackReference
    @OneToMany(mappedBy = "userGroup")
    private List<MenuPermissionEntity> menuPermissions;

    public UserGroupEntity(Long id) {
        super(id);
    }

    @Override
    protected void initValuesOnCreate() {
        // Initialize values on create 
    }
 
}
