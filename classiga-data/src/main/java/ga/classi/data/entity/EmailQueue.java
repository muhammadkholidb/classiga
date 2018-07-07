package ga.classi.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ga.classi.commons.helper.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author eatonmunoz
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter 
@Getter
@Entity
@Table(name = EmailQueue.TABLE_NAME,
    indexes = {
            @Index(columnList = "type"),
            @Index(columnList = "type, id"),
            @Index(columnList = "status"),
            @Index(columnList = "status, id"),
            @Index(columnList = "deleted"),
            @Index(columnList = "deleted, id")
    })
@DynamicInsert
@DynamicUpdate
public class EmailQueue extends BaseEntity implements Serializable {

    private static final long  serialVersionUID  = 1L;

    public static final String TABLE_NAME = "t_email_queue";

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "message", length = 1024)
    private String message;

    @Column(name = "data", length = 1024)
    private String data;

    public EmailQueue(Long id) {
        super(id);
    }

    @Override
    protected void setValuesOnCreate() {

    }

    @Override
    protected void setValuesOnUpdate() {

    }
    
}
