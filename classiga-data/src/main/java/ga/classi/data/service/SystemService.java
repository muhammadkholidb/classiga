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
import ga.classi.commons.data.error.ExceptionCode;
import ga.classi.commons.data.helper.Dto;
import ga.classi.commons.data.helper.DtoUtils;
import ga.classi.data.entity.SystemEntity;
import ga.classi.data.error.ErrorMessageConstants;
import ga.classi.data.helper.DataValidation;
import ga.classi.data.repository.SystemRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SystemService extends AbstractServiceHelper {

    @Autowired
    private SystemRepository systemRepository;

    @Transactional(readOnly = true)
    public Dto getAllSystem(Dto dtoInput) {
        log.info("Get all system ...");

        // No validation
        List<SystemEntity> list = systemRepository.findAll();

        return buildResultByEntityList(list);
    }

    @Transactional(readOnly = true)
    public Dto getSystemByDataKey(Dto dtoInput) {

        // Validate parameters
        DataValidation.containsRequiredData(dtoInput, "dataKey");

        String strKey = dtoInput.get("dataKey");

        SystemEntity systemEntity = systemRepository.findByDataKey(strKey);
        if (systemEntity == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.SYSTEM_NOT_FOUND, new Object[]{strKey});
        }

        return buildResultByEntity(systemEntity);
    }

    @Transactional(readOnly = true)
    public Dto getSystemById(Dto dtoInput) {

        // Validate parameters
        DataValidation.containsRequiredData(dtoInput, "id");

        String strId = dtoInput.get("id");

        // Validate values
        DataValidation.validateNumeric(strId, "System ID");

        SystemEntity systemEntity = systemRepository.findOne(Long.valueOf(strId));
        if (systemEntity == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.SYSTEM_NOT_FOUND);
        }

        return buildResultByEntity(systemEntity);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Dto editSystemList(Dto dtoInput) {

        // Validate parameters
        DataValidation.containsRequiredData(dtoInput, "systems");

        String strSystems = dtoInput.get("systems");

        // Validate values
        DataValidation.validateJSONArray(strSystems, "Systems");

        JSONArray arrSystems = (JSONArray) JSONValue.parse(strSystems);

        List<Dto> updatedList = new ArrayList<Dto>();

        for (Object object : arrSystems) {

            JSONObject jsonSystem = (JSONObject) object;

            // Validate parameters
            DataValidation.containsRequiredData(jsonSystem, "id", "dataValue");

            String strId = String.valueOf(jsonSystem.get("id"));
            String strValue = String.valueOf(jsonSystem.get("dataValue"));

            // Validate values
            DataValidation.validateNumeric(strId, "System ID");
            DataValidation.validateEmpty(strValue, "Data Value");

            SystemEntity findSystem = systemRepository.findOne(Long.valueOf(strId));
            if (findSystem == null) {
                throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.SYSTEM_NOT_FOUND);
            }

            findSystem.setDataValue(strValue);

            SystemEntity updated = systemRepository.save(findSystem);

            Dto dtoSystemUpdated = DtoUtils.toDto(updated);
            Dto dto = DtoUtils.omit(dtoSystemUpdated, "createdAt", "modifiedAt");

            updatedList.add(dto);
        }
        return buildResultByDtoList(updatedList);
    }

}
