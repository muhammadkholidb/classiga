package ga.classi.data.repository;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author eatonmunoz
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface BaseDataRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
   
    // Read https://docs.spring.io/spring-data/jpa/docs/current/reference/html
    
    /**
     * 
     * @param ids 
     * @return  
     */
    List<T> deleteByIdIn(List<ID> ids);

    /**
     * 
     * @param ids
     * @return 
     */
    List<T> findByIdIn(List<ID> ids);
    
}
