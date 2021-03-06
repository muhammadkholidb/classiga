/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.data.error;

public enum ExceptionCode {
    
    /**
     * Entity not found
     */
    E1001, 
    
    /**
     * Can't remove cause other entity
     */
    E1002, 
    
    /**
     * Entity already exists
     */
    E1003,

    /**
     * Parameter not found
     */
    E1004,
    
    /**
     * (Validation) invalid data type or format
     */
    E1005,
    
    /**
     * (Validation) invalid data length
     */
    E1006, 
    
    /**
     * Error import dataset
     */
    E1007,
    
    /**
     * Data status not active
     */
    E1008,
    
    /**
     * Unsatisfied entity value condition
     */
    E1009
    
}
