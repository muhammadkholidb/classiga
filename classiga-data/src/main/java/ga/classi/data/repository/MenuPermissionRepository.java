package ga.classi.data.repository;

import ga.classi.data.entity.MenuPermissionEntity;
import ga.classi.data.entity.UserGroupEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Repository for MenuPermissionEntity.
 * @author eatonmunoz
 */
@Repository
public interface MenuPermissionRepository extends BaseDataRepository<MenuPermissionEntity, Long> {

    /**
     * Finds MenuPermissionEntity by UserGroupEntity.
     * @param userGroup UserGroupEntity to be used to get list of MenuPermissionsEntity.
     * @return List of MenuPermissionsEntity.
     */
    List<MenuPermissionEntity> findByUserGroup(UserGroupEntity userGroup);

    /**
     * Deletes MenuPermissionsEntity by UserGroupEntity.
     * @param userGroup UserGroupEntity to be used to remove the related MenuPermissionsEntity.
     * @return List of removed MenuPermissionEntity.
     */
    List<MenuPermissionEntity> deleteByUserGroup(UserGroupEntity userGroup);
    
}
