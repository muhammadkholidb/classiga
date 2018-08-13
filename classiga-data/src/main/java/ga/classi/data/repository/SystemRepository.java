/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.repository;

import ga.classi.data.entity.SystemEntity;
import org.springframework.stereotype.Repository;

/**
 *
 * @author muhammad
 */
@Repository
public interface SystemRepository extends BaseDataRepository<SystemEntity, Long> {
    
    SystemEntity findByDataKey(String dataKey);
    
}
