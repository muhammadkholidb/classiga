/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import ga.classi.data.entity.UserEntity;
import ga.classi.data.entity.UserGroupEntity;
import ga.classi.data.helper.QueryHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserSpecifications {

    private static Join<UserEntity, UserGroupEntity>  joinUserGroup;
    private static Fetch<UserEntity, UserGroupEntity> fetchUserGroup;

    private static void requireOnceJoinUserGroup(Root<UserEntity> root, CriteriaQuery<?> query) {

        if (QueryHelper.isQueryCount(query)) {
            if (joinUserGroup == null) {
                joinUserGroup = root.join(UserEntity.F_USER_GROUP);
            }
        } else {
            if (fetchUserGroup == null) {
                fetchUserGroup = root.fetch(UserEntity.F_USER_GROUP);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static Specification<UserEntity> filtered(final String searchTerm) {

        return (root, query, cb) -> {

            if (QueryHelper.isQueryCount(query)) {

                log.debug("Query count ...");
                log.debug("Join ...");

                joinUserGroup = root.join(UserEntity.F_USER_GROUP);

                for (Join<UserEntity, ?> join : root.getJoins()) {
                    log.debug("Attribute: {}", join.getAttribute().getName());
                    log.debug("Join type: {}", join.getJoinType());
                }

            } else {

                log.debug("Not query count ...");
                log.debug("Fetch ...");

                fetchUserGroup = root.fetch("userGroup");
                joinUserGroup = (Join<UserEntity, UserGroupEntity>) fetchUserGroup;

                for (Fetch<UserEntity, ?> fetch : root.getFetches()) {
                    log.debug("Attribute: {}", fetch.getAttribute().getName());
                    log.debug("Join type: {}", fetch.getJoinType());
                }
            }

            if (searchTerm != null) {

                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.like(cb.lower(root.get("firstName").as(String.class)), "%" + searchTerm.toLowerCase() + "%"));
                predicates.add(cb.like(cb.lower(root.get("lastName").as(String.class)), "%" + searchTerm.toLowerCase() + "%"));
                predicates.add(cb.like(root.get("lowerUsername").as(String.class), "%" + searchTerm.toLowerCase() + "%"));
                predicates.add(cb.like(root.get("lowerEmail").as(String.class), "%" + searchTerm.toLowerCase() + "%"));
                predicates.add(cb.like(joinUserGroup.get("lowerName").as(String.class), "%" + searchTerm.toLowerCase() + "%"));

                cb.or(predicates.toArray(new Predicate[predicates.size()]));
            }

            return null;

        };
    }

    public Specification<UserEntity> hasFirstNameLike(final String firstName) {

        return (root, query, cb) -> {

            requireOnceJoinUserGroup(root, query);

            if (firstName != null && !firstName.isEmpty()) {
                return cb.like(cb.lower(root.get("firstName").as(String.class)), "%" + firstName.toLowerCase() + "%");
            }
            return null;
        };

    }

    public Specification<UserEntity> hasLastNameLike(final String lastName) {

        return (root, query, cb) -> {

            requireOnceJoinUserGroup(root, query);

            if (lastName != null && !lastName.isEmpty()) {
                return cb.like(cb.lower(root.get("lastName").as(String.class)), "%" + lastName.toLowerCase() + "%");
            }
            return null;

        };
    }

    public Specification<UserEntity> hasUsernameLike(final String username) {

        return (root, query, cb) -> {

            requireOnceJoinUserGroup(root, query);

            if (username != null && !username.isEmpty()) {
                return cb.like(root.get("lowerUsername").as(String.class), "%" + username.toLowerCase() + "%");
            }
            return null;

        };
    }

    public Specification<UserEntity> hasEmailLike(final String email) {

        return (root, query, cb) -> {

            requireOnceJoinUserGroup(root, query);

            if (email != null && !email.isEmpty()) {
                return cb.like(root.get("lowerEmail").as(String.class), "%" + email.toLowerCase() + "%");
            }
            return null;

        };
    }

    @SuppressWarnings("unchecked")
    public Specification<UserEntity> hasUserGroupNameLike(final String userGroupName) {

        return (root, query, cb) -> {

            requireOnceJoinUserGroup(root, query);

            Join<UserEntity, UserGroupEntity> join;

            if (QueryHelper.isQueryCount(query)) {
                join = joinUserGroup;
            } else {
                join = (Join<UserEntity, UserGroupEntity>) fetchUserGroup;
            }

            if (userGroupName != null && !userGroupName.isEmpty()) {
                return cb.like(join.get("lowerName").as(String.class), "%" + userGroupName.toLowerCase() + "%");
            }

            return null;

        };
    }

}
