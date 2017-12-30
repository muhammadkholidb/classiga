package ga.classi.data.service;

import ga.classi.commons.data.helper.Dto;

public interface CommonServiceBase {

    Dto getAll(Dto dtoInput);
    
    Dto getOne(Dto dtoInput);
    
    Dto add(Dto dtoInput);
    
    Dto edit(Dto dtoInput);
    
    void remove(Dto dtoInput);
}
