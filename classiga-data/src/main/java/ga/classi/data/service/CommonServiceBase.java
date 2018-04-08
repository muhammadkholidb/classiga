package ga.classi.data.service;

import ga.classi.commons.data.helper.DTO;

public interface CommonServiceBase {

    DTO getAll(DTO dtoInput);
    
    DTO getOne(DTO dtoInput);
    
    DTO add(DTO dtoInput);
    
    DTO edit(DTO dtoInput);
    
    void remove(DTO dtoInput);
}
