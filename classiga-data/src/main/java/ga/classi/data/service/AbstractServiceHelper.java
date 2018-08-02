/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import ga.classi.commons.data.helper.DTO;
import ga.classi.commons.data.helper.DTOUtils;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.data.entity.BaseEntity;

/**
 * A parent class with a set of methods to make standard data services.
 * @author muhammad
 */
public abstract class AbstractServiceHelper {

    private static final Integer DEFAULT_MAX_ROWS = 1000;    // Doesn't need to be configurable, just set the preferred value.
    
    /**
     * Builds a standard service result using list of Dto.
     * @param countRows The rows count. 
     * @param totalRows The total rows.
     * @param totalPages The total pages.
     * @param content The content of the result.
     * @return The standardized service result.
     */
    protected DTO buildResultByDTOList(int countRows, int totalRows, int totalPages, List<DTO> content) {
        DTO dto = new DTO();
        dto.put(CommonConstants.COUNT_ROWS, countRows);
        dto.put(CommonConstants.TOTAL_ROWS, totalRows); 
        dto.put(CommonConstants.TOTAL_PAGES, totalPages);
        dto.put(CommonConstants.CONTENT, content);
        return dto;
    }

    /**
     * Builds a standard service result using list of entities.
     * @param countRows The rows count.
     * @param totalRows The total rows.
     * @param totalPages The total pages.
     * @param entities The list of entities for the result content.
     * @param excludeContentKeys The keys to exclude from the entities.
     * @return The standardized service result.
     */
    protected DTO buildResultByEntityList(int countRows, int totalRows, int totalPages, List<? extends BaseEntity> entities, String... excludeContentKeys) {
        return buildResultByDTOList(countRows, totalRows, totalPages, DTOUtils.toDTOList(entities, excludeContentKeys));
    }
    
    /**
     * Builds a standard service result using list of Dto.
     * @param content The content of the result.
     * @return The standardized service result.
     */
    protected DTO buildResultByDTOList(List<DTO> content) {
        DTO dto = new DTO();
        dto.put(CommonConstants.CONTENT, content);
        return dto;
    }
    
    /**
     * Builds a standard service result using list of entities.
     * @param entities The list of entities for the result content.
     * @param excludeContentKeys The keys to exclude from the entities.
     * @return The standardized service result.
     */
    protected DTO buildResultByEntityList(List<? extends BaseEntity> entities, String... excludeContentKeys) {
        return buildResultByDTOList(DTOUtils.toDTOList(entities, excludeContentKeys));
    }
    
    /**
     * Builds a standard service result using a Dto.
     * @param content The content of the result.
     * @return The standardized service result.
     */
    protected DTO buildResultByDTO(DTO content) {
        DTO dto = new DTO();
        dto.put(CommonConstants.CONTENT, content);
        return dto;
    }

    /**
     * Builds a standard service result using an entity.
     * @param <E> An object extends BaseEntity.
     * @param entity The entity for the result content.
     * @param excludeContentKeys The keys to exclude from the entities.
     * @return The standardized service result.
     */
    protected <E extends BaseEntity> DTO buildResultByEntity(E entity, String... excludeContentKeys) {
        return buildResultByDTO(DTOUtils.toDTO(entity, excludeContentKeys));
    }
    
    /**
     * Builds a standard service result using page.
     * @param page The page of entities.
     * @param excludeContentKeys The keys to exclude from the entities.
     * @return The standardized service result.
     */
    protected DTO buildResultByPage(Page<? extends BaseEntity> page, String... excludeContentKeys) {
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
     * @param dto The Dto containing keys specified in this method's javadoc.
     * @param defaultSortDirection The default sort direction.
     * @param defaultSortColumn The default sort column.
     * @return A new PageRequest object.
     */
    protected PageRequest createPageRequest(DTO dto, Direction defaultSortDirection, String defaultSortColumn) {

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
     * @param start The start page.
     * @param length The length of the rows.
     * @param sortOrder The sort order.
     * @param sortColumn The sort column.
     * @param defaultSortDirection The default sort direction.
     * @param defaultSortColumn The default sort column.
     * @return A new PageRequest object.
     */
    protected PageRequest createPageRequest(Integer start, Integer length, String sortOrder, String sortColumn, Direction defaultSortDirection, String defaultSortColumn) {
        
        int page = (start == null) ? 0 : (start / length);
        int size = (length == null) ? DEFAULT_MAX_ROWS : length;   // Returns default max rows
        Direction sortDirection = (sortOrder == null) ? defaultSortDirection : Direction.fromString(sortOrder);
        String sortColumnName = (sortColumn == null) ? defaultSortColumn : sortColumn;
        
        return new PageRequest(page, size, new Sort(sortDirection, sortColumnName));
    }
}
