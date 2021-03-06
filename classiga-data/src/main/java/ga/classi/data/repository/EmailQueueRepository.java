/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.repository;

import ga.classi.data.entity.EmailQueueEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author muhammad
 */
@Repository
public interface EmailQueueRepository extends BaseDataRepository<EmailQueueEntity, Long> {
    
    List<EmailQueueEntity> findByStatusAndDeleted(Integer status, String deleted);
    
}
