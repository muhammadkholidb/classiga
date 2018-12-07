/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.entity;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = UserGroupEntity.TABLE_NAME, 
    indexes = {
        @Index(columnList = "deleted"),
        @Index(columnList = "id, deleted"),
        @Index(columnList = "lower_name"),
        @Index(columnList = "lower_name, deleted")
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
    protected void setValuesOnCreate() {
        // Set values on create
    }

    @Override
    protected void setValuesOnUpdate() {
        // Set values on update
    }

    @Override
    protected StringBuilder getHashElements() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append(this.lowerName);
        sb.append(StringUtils.defaultString(this.description));
        sb.append(this.active);
        return sb;
    }
 
}
