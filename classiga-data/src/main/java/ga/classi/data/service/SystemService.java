/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.service;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.DTO;
import ga.classi.commons.data.error.Errors;
import ga.classi.commons.data.utility.DTOUtils;
import ga.classi.data.entity.SystemEntity;
import ga.classi.data.helper.DataValidator;
import ga.classi.data.repository.SystemRepository;
import lombok.extern.slf4j.Slf4j;

import static ga.classi.commons.constant.RequestDataConstants.*;

@Slf4j
@Service
public class SystemService extends AbstractServiceHelper {

    @Autowired
    private SystemRepository systemRepository;

    @Transactional(readOnly = true)
    public DTO getAllSystem(DTO dtoInput) {
        log.info("Get all system ...");

        // No validation
        List<SystemEntity> list = systemRepository.findAll();

        return buildResultByEntityList(list);
    }

    @Transactional(readOnly = true)
    public DTO getSystemByDataKey(DTO dtoInput) {

        // Validate parameters
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(DATA_KEY);

        String strKey = dtoInput.get(DATA_KEY);

        SystemEntity systemEntity = systemRepository.findByDataKey(strKey);
        if (systemEntity == null) {
            throw new DataException(Errors.SYSTEM_NOT_FOUND, new Object[]{strKey});
        }

        return buildResultByEntity(systemEntity);
    }

    @Transactional(readOnly = true)
    public DTO getSystemById(DTO dtoInput) {

        // Validate parameters
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(ID);

        // Validate values
        String strId = validator.validateNumber(ID);

        SystemEntity systemEntity = systemRepository.findOne(Long.valueOf(strId));
        if (systemEntity == null) {
            throw new DataException(Errors.SYSTEM_NOT_FOUND);
        }

        return buildResultByEntity(systemEntity);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public DTO editSystemList(DTO dtoInput) {

        // Validate parameters
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(SYSTEMS);

        // Validate values
        String strSystems = validator.validateJSONArray(SYSTEMS);

        JSONArray arrSystems = (JSONArray) JSONValue.parse(strSystems);

        List<DTO> updatedList = new ArrayList<>();

        for (Object object : arrSystems) {

            JSONObject jsonSystem = (JSONObject) object;

            // Validate parameters
            DataValidator validatorSystem = new DataValidator(new DTO(jsonSystem));
            validatorSystem.containsRequiredData(ID, DATA_VALUE);

            // Validate values
            String strId = validatorSystem.validateNumber(ID);
            String strValue = validatorSystem.validateEmptyString(DATA_VALUE);

            SystemEntity findSystem = systemRepository.findOne(Long.valueOf(strId));
            if (findSystem == null) {
                throw new DataException(Errors.SYSTEM_NOT_FOUND);
            }

            findSystem.setDataValue(strValue);

            SystemEntity updated = systemRepository.save(findSystem);

            DTO dtoSystemUpdated = DTOUtils.toDTO(updated);
            DTO dto = DTOUtils.omit(dtoSystemUpdated, "createdAt", "modifiedAt");

            updatedList.add(dto);
        }
        return buildResultByDTOList(updatedList);
    }

}
