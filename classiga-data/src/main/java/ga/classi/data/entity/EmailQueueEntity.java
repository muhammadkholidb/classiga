/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Data;

/**
 * 
 * @author muhammad
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = EmailQueueEntity.TABLE_NAME,
    indexes = {
            @Index(columnList = "to"),
            @Index(columnList = "to, id"),
            @Index(columnList = "to, status"),
            @Index(columnList = "to, subject"),
            @Index(columnList = "status"),
            @Index(columnList = "status, id"),
            @Index(columnList = "deleted"),
            @Index(columnList = "deleted, id")
    })
@DynamicInsert
@DynamicUpdate
public class EmailQueueEntity extends BaseEntity implements Serializable {

    private static final long  serialVersionUID  = 1L;

    public static final String TABLE_NAME = "t_email_queue";

    @Column(name = "to", length = 128, nullable = false)
    private String to;

    @Column(name = "subject", length = 128, nullable = false)
    private String subject;

    @Column(name = "template", length = 64)
    private String template;

    @Column(name = "message", length = 1024)
    private String message;
    
    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "data", length = 1024)
    private String data;

    public EmailQueueEntity(Long id) {
        super(id);
    }

    @Override
    protected void setValuesOnCreate() {

    }

    @Override
    protected void setValuesOnUpdate() {

    }

    @Override
    protected StringBuilder getHashElements() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.to);
        sb.append(this.subject);
        sb.append(this.template);
        sb.append(this.message);
        sb.append(this.status);
        sb.append(this.data);
        return sb;
    }

}
