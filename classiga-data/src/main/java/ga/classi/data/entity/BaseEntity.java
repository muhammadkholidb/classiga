package ga.classi.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ga.classi.commons.helper.CommonConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter 
@Getter 
@MappedSuperclass
public abstract class BaseEntity {

    // Read http://www.baeldung.com/intro-to-project-lombok
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @JsonIgnore
    @Version
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    private Date modifiedAt;

    @JsonIgnore
    @Column(name = "deleted", length = 1)
    private String deleted;

    public BaseEntity(Long id) {
        this.id = id;
    }

    @PrePersist
    public void onCreate() {
        createdAt = new Date();
        deleted = CommonConstants.NO;
        initValuesOnCreate();
    }

    protected abstract void initValuesOnCreate();
    
}
