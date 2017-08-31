package ga.classi.data.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    // Set default value using database native language, so it is database dependent. 
    // This is useful when executing database query from script which does not mention column to insert / update, 
    // so it will set default value from the database.
    @Column(name = "created_at", columnDefinition="bigint default 0")
    // Set default value from Java. This is useful when performing insert / update using 
    // JPA persist() / merge() method without having to set the values of all columns.
    private Long createdAt = 0L;

    @JsonIgnore
    @Column(name = "modified_at", columnDefinition="bigint default 0")
    private Long modifiedAt = 0L;

    public BaseEntity(Long id) {
        this.id = id;
    }

    @PrePersist
    protected void onCreate() {
        log.debug("onCreate() triggered ...");
        modifiedAt = createdAt = System.currentTimeMillis();
    }

    @PreUpdate
    protected void onUpdate() {
        log.debug("onUpdate() triggered ...");
        modifiedAt = System.currentTimeMillis();
    }
}
