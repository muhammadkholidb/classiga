package ga.classi.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import ga.classi.data.entity.UserEntity;
import ga.classi.data.entity.UserGroupEntity;

/**
 *
 * @author eatonmunoz
 */
public interface UserRepository extends BaseDataRepository<UserEntity, Long>, UserRepositoryCustom {

    /**
     * 
     * @param pageable
     * @return 
     */
    @Query(value = "SELECT u FROM UserEntity u INNER JOIN FETCH u.userGroup ug ", 
            countQuery = "SELECT count(u) FROM UserEntity u INNER JOIN u.userGroup ug ")
    Page<UserEntity> findAllFetchUserGroup(Pageable pageable);

    /**
     * 
     * @param searchTermLowercase
     * @param pageable
     * @return 
     */
    @Query(value = "SELECT u FROM UserEntity u INNER JOIN FETCH u.userGroup ug "
            + " WHERE LOWER(u.firstName) LIKE CONCAT('%',?1,'%') "
            + " OR LOWER(u.lastName) LIKE CONCAT('%',?1,'%') "
            + " OR u.lowerUsername LIKE CONCAT('%',?1,'%') "
            + " OR u.lowerEmail LIKE CONCAT('%',?1,'%') "
            + " OR ug.lowerName LIKE CONCAT('%',?1,'%') ",
            countQuery = "SELECT COUNT(u) FROM UserEntity u INNER JOIN u.userGroup ug " 
            + " WHERE LOWER(u.firstName) LIKE CONCAT('%',?1,'%') "
            + " OR LOWER(u.lastName) LIKE CONCAT('%',?1,'%') "
            + " OR u.lowerUsername LIKE CONCAT('%',?1,'%') "
            + " OR u.lowerEmail LIKE CONCAT('%',?1,'%') "
            + " OR ug.lowerName LIKE CONCAT('%',?1,'%') ")
    Page<UserEntity> findAllFetchUserGroupFiltered(String searchTermLowercase, Pageable pageable);

    /**
     * 
     * @param userGroup
     * @return 
     */
    List<UserEntity> findByUserGroup(UserGroupEntity userGroup);

    /**
     * 
     * @param lowerUsername
     * @return 
     */
    UserEntity findOneByLowerUsername(String lowerUsername);

    /**
     * 
     * @param lowerEmail
     * @return 
     */
    UserEntity findOneByLowerEmail(String lowerEmail);

    /**
     * 
     * @param lowerEmail
     * @param lowerUsername
     * @return 
     */
    UserEntity findOneByLowerEmailOrLowerUsername(String lowerEmail, String lowerUsername);
    
}
