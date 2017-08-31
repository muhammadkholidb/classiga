package ga.classi.data.helper;

import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author eatonmunoz
 */
public class QueryHelper {

    // Read https://stackoverflow.com/questions/29348742/spring-data-jpa-creating-specification-query-fetch-joins
    /**
     * Checks whether the query is for "COUNT" query. 
     *
     * @param cq
     * @return
     */
    public static boolean isQueryCount(CriteriaQuery<?> cq) {
        // Join fetch should be applied only for query to fetch the "data", not for "count" query to do pagination.
        // Handled this by checking the criteriaQuery.getResultType(), if it's long that means query is
        // for count so not appending join fetch else append it.
        Class<?> resultType = cq.getResultType();
        if (resultType.equals(Long.class) || resultType.equals(long.class)) {
            return true;
        }
        return false;
    }

}
