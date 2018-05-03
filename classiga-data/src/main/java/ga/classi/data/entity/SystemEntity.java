package ga.classi.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
@Table(name = SystemEntity.TABLE_NAME, uniqueConstraints = @UniqueConstraint(columnNames = { "data_key" }))
@DynamicInsert
@DynamicUpdate
public class SystemEntity extends BaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "t_system";

    @Column(name = "data_key", length = 64, nullable = false)
    private String dataKey;

    @Column(name = "data_value", length = 64, nullable = false)
    private String dataValue;

    public SystemEntity(Long id) {
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
    
}
