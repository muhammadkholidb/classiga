package ga.classi.data.repository;

import ga.classi.data.entity.UserGroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author eatonmunoz
 */
@Repository
public interface UserGroupRepository extends BaseDataRepository<UserGroupEntity, Long> {
    
    /**
     * 
     * @param searchTermLowercase
     * @param pageable
     * @return 
     */
    @Query(value = "SELECT ug FROM UserGroupEntity ug WHERE ug.lowerName LIKE CONCAT('%',?1,'%') OR LOWER(ug.description) LIKE CONCAT('%',?1,'%')")
    Page<UserGroupEntity> findAllFiltered(String searchTermLowercase, Pageable pageable);

    /**
     * 
     * @param lowerName
     * @return 
     */
    UserGroupEntity findOneByLowerName(String lowerName);

    /**
     * 
     * @param id
     * @return 
     */
    @Query("SELECT ug FROM UserGroupEntity ug LEFT JOIN FETCH ug.menuPermissions mp WHERE ug.id = ?1 ")
    UserGroupEntity findByIdFetchMenuPermissions(Long id);

    /**
     * 
     * @param id
     * @return 
     */
    @Query("SELECT ug.name FROM UserGroupEntity ug WHERE ug.id = ?1 ") 
    String findNameById(Long id);
    
}
