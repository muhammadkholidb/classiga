/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.data.helper;

import ga.classi.commons.constant.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author muhammad
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageQuery {
    
    public PageQuery(DTO dto) {
        this.start =  dto.getInteger(CommonConstants.START);
        this.length = dto.getInteger(CommonConstants.LENGTH);
        this.sortDirection = dto.getString(CommonConstants.SORT_ORDER);
        this.sortColumn = dto.getString(CommonConstants.SORT_COLUMN);
    }
    
    private Integer start; 
    private Integer length;
    private String sortDirection; 
    private String sortColumn;
}
