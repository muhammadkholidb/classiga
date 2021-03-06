/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 
 * @author muhammad
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = MenuPermissionEntity.TABLE_NAME, uniqueConstraints = @UniqueConstraint(columnNames = {"user_group_id", "menu_code"})) // Let hibernate give its constraint name
@DynamicInsert
@DynamicUpdate
public class MenuPermissionEntity extends BaseEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "t_menu_permission";

    @Column(name = "menu_code", length = 64, nullable = false)
    private String menuCode;

    @Column(name = "can_view", length = 1, nullable = false)
    private String canView;

    @Column(name = "can_modify", length = 1, nullable = false)
    private String canModify;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_group_id", referencedColumnName = "id")
    private UserGroupEntity userGroup;

    public MenuPermissionEntity(Long id) {
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
        sb.append(this.menuCode);
        sb.append(this.canView);
        sb.append(this.canModify);
        sb.append(this.userGroup.getId());
        return sb;
    }
    
}
