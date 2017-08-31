package ga.classi.data.specifications;

import ga.classi.data.entity.UserGroupEntity;
import ga.classi.data.helper.Dto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author eatonmunoz
 */
public final class UserGroupSpecifications {
    
    private UserGroupSpecifications() {}
    
    public static Specification<UserGroupEntity> filtered(final Dto inputDto) {
        
        return new Specification<UserGroupEntity>() {

            @Override
            public Predicate toPredicate(Root<UserGroupEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                
                String name = inputDto.get("name");
                String active = inputDto.get("active");
                
                List<Predicate> predicates = new ArrayList<Predicate>();
                
                if (name != null && !name.isEmpty()) {
                    predicates.add(cb.like(root.get("lowerName").as(String.class), "%" + name.toLowerCase() + "%"));
                }
                
                if (active != null && !active.isEmpty()) {
                    predicates.add(cb.equal(root.get("active"), active.toLowerCase()));
                }
                
                return cb.and(predicates.toArray(new Predicate[predicates.size()])); 
            }
        };
    }
    
}
