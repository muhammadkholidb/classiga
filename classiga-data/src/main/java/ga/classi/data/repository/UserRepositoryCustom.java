package ga.classi.data.repository;

/**
 *
 * @author eatonmunoz
 */
public interface UserRepositoryCustom {

    Object[] findOneByEmailOrUsernameJoinUserGroup(String email, String username);
    
}
