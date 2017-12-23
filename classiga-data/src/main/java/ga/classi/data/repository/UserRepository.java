package ga.classi.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import ga.classi.data.entity.UserEntity;
import ga.classi.data.entity.UserGroupEntity;

/**
 * Repository for UserEntity.
 * @author eatonmunoz
 */
public interface UserRepository extends BaseDataRepository<UserEntity, Long>, UserRepositoryCustom {

    /**
     * Returns all UserEntity data with its UserGroupEntity.
     * @param pageable The pagination data.
     * @return A page of UserEntity.
     */
    @Query(value = "SELECT u FROM UserEntity u INNER JOIN FETCH u.userGroup ug ", 
            countQuery = "SELECT COUNT(u) FROM UserEntity u INNER JOIN u.userGroup ug ")
    Page<UserEntity> findAllFetchUserGroup(Pageable pageable);

    /**
     * Returns filtered UserEntity data with its UserGroupEntity.
     * @param lowerSearchTerm The search term to filter the data of UserEntity.
     * @param pageable The pagination data.
     * @return A page of UserEntity.
     */
    @Query(value = "SELECT u FROM UserEntity u INNER JOIN FETCH u.userGroup ug "
            + " WHERE u.lowerFullName LIKE CONCAT('%',?1,'%') "
            + " OR u.lowerUsername LIKE CONCAT('%',?1,'%') "
            + " OR u.lowerEmail LIKE CONCAT('%',?1,'%') "
            + " OR ug.lowerName LIKE CONCAT('%',?1,'%') ",
            countQuery = "SELECT COUNT(u) FROM UserEntity u INNER JOIN u.userGroup ug " 
            + " WHERE u.lowerFullName LIKE CONCAT('%',?1,'%') "
            + " OR u.lowerUsername LIKE CONCAT('%',?1,'%') "
            + " OR u.lowerEmail LIKE CONCAT('%',?1,'%') "
            + " OR ug.lowerName LIKE CONCAT('%',?1,'%') ")
    Page<UserEntity> findAllFetchUserGroupFiltered(String lowerSearchTerm, Pageable pageable);

    /**
     * Finds UserEntity by UserGroupEntity.
     * @param userGroup The UserGroupEntity for finding the UserEntity.
     * @return List of UserEntity for specified UserGroupEntity.
     */
    List<UserEntity> findByUserGroup(UserGroupEntity userGroup);

    /**
     * Counts UserEntity by UserGroupEntity.
     * @param userGroup The UserGroupEntity for counting the UserEntity.
     * @return The total number of UserEntity for specified UserGroupEntity.
     */
    Long countByUserGroup(UserGroupEntity userGroup);
    
    /**
     * Finds one UserEntity by lowercased username.
     * @param lowerUsername The lowercased username.
     * @return The UserEntity by specified lowercased username.
     */
    UserEntity findOneByLowerUsername(String lowerUsername);

    /**
     * Finds one UserEntity by lowercased email.
     * @param lowerEmail The lowercased email.
     * @return The UserEntity by specified lowercased email.
     */
    UserEntity findOneByLowerEmail(String lowerEmail);

    /**
     * Finds one UserEntity by lowercased email or lowercased username.
     * @param lowerEmail The lowercased email.
     * @param lowerUsername The lowercased username.
     * @return The UserEntity with the specified lowercased email or lowercased username.
     */
    UserEntity findOneByLowerEmailOrLowerUsername(String lowerEmail, String lowerUsername);
    
}
