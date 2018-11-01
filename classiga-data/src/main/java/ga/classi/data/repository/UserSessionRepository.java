/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.repository;

import org.springframework.stereotype.Repository;

import ga.classi.data.entity.UserSessionEntity;

/**
 *
 * @author muhammad
 */
@Repository
public interface UserSessionRepository extends BaseDataRepository<UserSessionEntity, Long> {
    
}
