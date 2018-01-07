package ga.classi.data.repository;

import ga.classi.data.entity.UserGroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for UserGroupEntity.
 * @author eatonmunoz
 */
@Repository
public interface UserGroupRepository extends BaseDataRepository<UserGroupEntity, Long> {
    
    /**
     * Returns filtered UserGroupEntity data.
     * @param searchTermLowercase The search term to filter the data of UserGroupEntity.
     * @param deleted The value of "deleted" column.
     * @param pageable The pagination data.
     * @return A page of UserGroupEntity.
     */
    @Query(value = "SELECT ug FROM UserGroupEntity ug WHERE ug.deleted = ?1 AND (ug.lowerName LIKE CONCAT('%',?2,'%') OR LOWER(ug.description) LIKE CONCAT('%',?2,'%'))")
    Page<UserGroupEntity> findByDeletedSearch(String deleted, String searchTermLowercase, Pageable pageable);

    /**
     * Finds UserGroupEntity by lowercased name.
     * @param lowerName The lowercased name.
     * @param deleted The value of "deleted" column.
     * @return The UserGroupEntity by lowercased name.
     */
    UserGroupEntity findOneByLowerNameAndDeleted(String lowerName, String deleted);

    /**
     * Finds UserGroupEntity by ID with the list of its MenuPermissionsEntity.
     * @param id The ID of the UserGroupEntity to find.
     * @param deleted The value of "deleted" column.
     * @return The UserGroupEntity by ID with the list of its MenuPermissionsEntity.
     */
    @Query("SELECT ug FROM UserGroupEntity ug LEFT JOIN FETCH ug.menuPermissions mp WHERE ug.id = ?1 AND ug.deleted = ?2")
    UserGroupEntity findByIdAndDeletedFetchMenuPermissions(Long id, String deleted);

    /**
     * Finds UserGroupEntity name by ID.
     * @param id The ID of the UserGroupEntity to find.
     * @return The name of the UserGroupEntity for the specified ID.
     */
    @Query("SELECT ug.name FROM UserGroupEntity ug WHERE ug.id = ?1 ") 
    String findNameById(Long id);
    
}
