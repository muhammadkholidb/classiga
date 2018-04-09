package ga.classi.data.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import ga.classi.data.entity.BaseEntity;

@NoRepositoryBean
public interface BaseDataRepository<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
   
    // Read https://docs.spring.io/spring-data/jpa/docs/current/reference/html
    
    /**
     * Deletes by ID in the list.
     * @param ids The IDs whose entities to remove from database.
     * @return List of the removed entities.
     */
    List<T> deleteByIdIn(List<ID> ids);

    /**
     * Finds by ID in the list.
     * @param ids The IDs whose entities to find.
     * @return List of entities for the specified IDs.
     */
    List<T> findByIdIn(List<ID> ids);

    Page<T> findByDeleted(String deleted, Pageable pageable);

    T findOneByIdAndDeleted(Long id, String deleted);

    T findOneByRowHash(String rowHash);

    T findOneByRowHashAndDeleted(String rowHash, String deleted);
    
}
