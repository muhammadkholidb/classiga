package ga.classi.data.repository;

import ga.classi.data.entity.MenuPermissionEntity;
import ga.classi.data.entity.UserGroupEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author eatonmunoz
 */
@Repository
public interface MenuPermissionRepository extends BaseDataRepository<MenuPermissionEntity, Long> {

    /**
     * 
     * @param userGroup
     * @return 
     */
    List<MenuPermissionEntity> findByUserGroup(UserGroupEntity userGroup);

    /**
     * 
     * @param userGroup
     * @return 
     */
    List<MenuPermissionEntity> deleteByUserGroup(UserGroupEntity userGroup);
    
}
