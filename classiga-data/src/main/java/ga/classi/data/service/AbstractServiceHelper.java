package ga.classi.data.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import ga.classi.commons.data.helper.Dto;
import ga.classi.commons.data.helper.DtoUtils;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.data.entity.BaseEntity;

/**
 *
 * @author eatonmunoz
 */
public abstract class AbstractServiceHelper {

    private static final Integer DEFAULT_MAX_ROWS = 1000;    // Doesn't need to be configurable, just set the preferred value.
    
    /**
     *
     * @param countRows
     * @param totalRows
     * @param totalPages
     * @param content
     * @return
     */
    protected Dto buildResultByDtoList(int countRows, int totalRows, int totalPages, List<Dto> content) {
        Dto dto = new Dto();
        dto.put(CommonConstants.COUNT_ROWS, countRows);
        dto.put(CommonConstants.TOTAL_ROWS, totalRows); 
        dto.put(CommonConstants.TOTAL_PAGES, totalPages);
        dto.put(CommonConstants.CONTENT, content);
        return dto;
    }

    /**
     * 
     * @param countRows
     * @param totalRows
     * @param totalPages
     * @param entities
     * @param excludeContentKeys
     * @return 
     */
    protected Dto buildResultByEntityList(int countRows, int totalRows, int totalPages, List<? extends BaseEntity> entities, String... excludeContentKeys) {
        return buildResultByDtoList(countRows, totalRows, totalPages, DtoUtils.toDtoList(entities, excludeContentKeys));
    }
    
    /**
     * 
     * @param content
     * @return 
     */
    protected Dto buildResultByDtoList(List<Dto> content) {
        Dto dto = new Dto();
        dto.put(CommonConstants.CONTENT, content);
        return dto;
    }
    
    /**
     * 
     * @param entities
     * @param excludeContentKeys
     * @return 
     */
    protected Dto buildResultByEntityList(List<? extends BaseEntity> entities, String... excludeContentKeys) {
        return buildResultByDtoList(DtoUtils.toDtoList(entities, excludeContentKeys));
    }
    
    /**
     *
     * @param content
     * @return
     */
    protected Dto buildResultByDto(Dto content) {
        Dto dto = new Dto();
        dto.put(CommonConstants.CONTENT, content);
        return dto;
    }

    /**
     * 
     * @param <E>
     * @param entity
     * @param excludeContentKeys
     * @return 
     */
    protected <E extends BaseEntity> Dto buildResultByEntity(E entity, String... excludeContentKeys) {
        return buildResultByDto(DtoUtils.toDto(entity, excludeContentKeys));
    }
    
    /**
     *
     * @param page
     * @param excludeContentKeys
     * @return
     */
    protected Dto buildResultByPage(Page<? extends BaseEntity> page, String... excludeContentKeys) {
        return buildResultByEntityList(
                page.getNumberOfElements(),
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.getContent(), 
                excludeContentKeys);
    }

    /**
     * Creates a new {@link PageRequest} using Dto as the input. The keys must be there in the Dto are: start, length, sortOrder, sortColumn.
     * 
     * @param dto
     * @param defaultSortDirection
     * @param defaultSortColumn
     * @return
     */
    protected PageRequest createPageRequest(Dto dto, Direction defaultSortDirection, String defaultSortColumn) {

        return createPageRequest(
                dto.getIntegerValue(CommonConstants.START), 
                dto.getIntegerValue(CommonConstants.LENGTH), 
                dto.getStringValue(CommonConstants.SORT_ORDER), 
                dto.getStringValue(CommonConstants.SORT_COLUMN), 
                defaultSortDirection, 
                defaultSortColumn);        
    }

    /**
     * Creates a new {@link PageRequest}.
     * 
     * @param start
     * @param length
     * @param sortOrder
     * @param sortColumn
     * @param defaultSortDirection
     * @param defaultSortColumn
     * @return
     */
    protected PageRequest createPageRequest(Integer start, Integer length, String sortOrder, String sortColumn, Direction defaultSortDirection, String defaultSortColumn) {
        
        int page = (start == null) ? 0 : (start / length);
        int size = (length == null) ? DEFAULT_MAX_ROWS : length;   // Returns default max rows
        Direction sortDirection = (sortOrder == null) ? defaultSortDirection : Direction.fromString(sortOrder);
        String sortColumnName = (sortColumn == null) ? defaultSortColumn : sortColumn;
        
        return new PageRequest(page, size, new Sort(sortDirection, sortColumnName));
    }
}
