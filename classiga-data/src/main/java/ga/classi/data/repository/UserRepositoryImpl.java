/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author muhammad
 */
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public Object[] findOneByEmailOrUsernameJoinUserGroup(String email, String username) {
        Query query = em.createQuery("SELECT a, b "
                + " FROM UserEntity a, UserGroupEntity b "
                + " WHERE a.userGroupId = b.id "
                + " AND (a.lowerEmail = LOWER(:email) OR a.lowerUsername = LOWER(:username)) ").setMaxResults(1);
        query.setParameter("email", email);
        query.setParameter("username", username);
        List<Object[]> list = query.getResultList();
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return new Object[0];
    }

}
