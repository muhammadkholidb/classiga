package ga.classi.data.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
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

    private Join<UserEntity, UserGroupEntity> joinUserGroup;
    private Fetch<UserEntity, UserGroupEntity> fetchUserGroup;

    private void requireOnceJoinUserGroup(Root<UserEntity> root, CriteriaQuery<?> query) {

        if (QueryHelper.isQueryCount(query)) {
            if (joinUserGroup == null) {
                joinUserGroup = root.join("userGroup");
            }
        } else {
            if (fetchUserGroup == null) {
                fetchUserGroup = root.fetch("userGroup");
            }
        }
    }

    public static Specification<UserEntity> filtered(final String searchTerm) {
        
        return new Specification<UserEntity>() {

            @SuppressWarnings("unchecked")
            @Override
            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                Join<UserEntity, UserGroupEntity> joinUserGroup = null;

                if (QueryHelper.isQueryCount(query)) {
                        
                    log.debug("Query count ...");
                    log.debug("Join ...");
                    
                    joinUserGroup = root.join("userGroup");
                    
                    for (Join<UserEntity, ?> join : root.getJoins()) {
                        log.debug("Attribute: {}", join.getAttribute().getName());
                        log.debug("Join type: {}", join.getJoinType());
                    }
                                        
                } else {

                    log.debug("Not query count ...");
                    log.debug("Fetch ...");
                    
                    Fetch<UserEntity, UserGroupEntity> fetchUserGroup = root.fetch("userGroup");
                    joinUserGroup = (Join<UserEntity, UserGroupEntity>) fetchUserGroup;
                    
                    for (Fetch<UserEntity, ?> fetch : root.getFetches()) {
                        log.debug("Attribute: {}", fetch.getAttribute().getName());
                        log.debug("Join type: {}", fetch.getJoinType());
                    }
                }

                
                if (searchTerm != null) {

                    List<Predicate> predicates = new ArrayList<Predicate>();
                    
                    predicates.add(cb.like(cb.lower(root.get("firstName").as(String.class)), "%" + searchTerm.toLowerCase() + "%"));
                    predicates.add(cb.like(cb.lower(root.get("lastName").as(String.class)), "%" + searchTerm.toLowerCase() + "%"));
                    predicates.add(cb.like(root.get("lowerUsername").as(String.class), "%" + searchTerm.toLowerCase() + "%"));
                    predicates.add(cb.like(root.get("lowerEmail").as(String.class), "%" + searchTerm.toLowerCase() + "%"));
                    predicates.add(cb.like(joinUserGroup.get("lowerName").as(String.class), "%" + searchTerm.toLowerCase() + "%"));
                    
                    cb.or(predicates.toArray(new Predicate[predicates.size()]));
                }
                
                return null;
            }
        };
    }
    
    public Specification<UserEntity> hasFirstNameLike(final String firstName) {

        return new Specification<UserEntity>() {

            @Override
            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                requireOnceJoinUserGroup(root, query);

                if (firstName != null && !firstName.isEmpty()) {
                    return cb.like(cb.lower(root.get("firstName").as(String.class)), "%" + firstName.toLowerCase() + "%");
                }
                return null;
            }
        };
    }

    public Specification<UserEntity> hasLastNameLike(final String lastName) {

        return new Specification<UserEntity>() {

            @Override
            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                requireOnceJoinUserGroup(root, query);

                if (lastName != null && !lastName.isEmpty()) {
                    return cb.like(cb.lower(root.get("lastName").as(String.class)), "%" + lastName.toLowerCase() + "%");
                }
                return null;
            }
        };
    }

    public Specification<UserEntity> hasUsernameLike(final String username) {

        return new Specification<UserEntity>() {

            @Override
            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                requireOnceJoinUserGroup(root, query);

                if (username != null && !username.isEmpty()) {
                    return cb.like(root.get("lowerUsername").as(String.class), "%" + username.toLowerCase() + "%");
                }
                return null;
            }
        };
    }

    public Specification<UserEntity> hasEmailLike(final String email) {

        return new Specification<UserEntity>() {

            @Override
            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                requireOnceJoinUserGroup(root, query);

                if (email != null && !email.isEmpty()) {
                    return cb.like(root.get("lowerEmail").as(String.class), "%" + email.toLowerCase() + "%");
                }
                return null;
            }
        };
    }

    public Specification<UserEntity> hasUserGroupNameLike(final String userGroupName) {

        return new Specification<UserEntity>() {

            @SuppressWarnings("unchecked")
            @Override
            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

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
            }
        };
    }

}
