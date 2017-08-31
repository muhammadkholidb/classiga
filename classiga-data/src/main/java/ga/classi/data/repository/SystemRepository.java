/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.classi.data.repository;

import ga.classi.data.entity.SystemEntity;
import org.springframework.stereotype.Repository;

/**
 *
 * @author eatonmunoz
 */
@Repository
public interface SystemRepository extends BaseDataRepository<SystemEntity, Long> {
    
    SystemEntity findByDataKey(String dataKey);
    
}
