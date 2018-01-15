package ga.classi.data.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ga.classi.commons.helper.CommonConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    @Version
    @Column(name = "version", columnDefinition = "INT NOT NULL DEFAULT 0")
    private Integer version;

    @JsonIgnore
    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @JsonIgnore
    @Column(name = "modified_at", nullable = false)
    private Long modifiedAt;

    @JsonIgnore
    @Column(name = "deleted", length = 1, nullable = false)
    private String deleted;

    public BaseEntity(Long id) {
        this.id = id;
    }

    public void setDeleted() {
        this.deleted = CommonConstants.YES;
    }
    
    public Boolean isDeleted() {
        return CommonConstants.YES.equals(this.deleted);
    }

    protected abstract void setValuesOnCreate();
    
    protected abstract void setValuesOnUpdate();
    
    @PrePersist
    public void onCreate() {
        log.debug("Execute onCreate() ..."); 
        modifiedAt = createdAt = System.currentTimeMillis();
        deleted = CommonConstants.NO;
        setValuesOnCreate();
    }

    @PreUpdate
    public void onUpdate() {
        log.debug("Execute onUpdate() ..."); 
        modifiedAt = System.currentTimeMillis();
        setValuesOnUpdate();
    }
    
}
