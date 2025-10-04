package com.xavelo.template.application.port.in;

import com.xavelo.template.api.contract.model.CrudObjectCreateRequestDto;
import com.xavelo.template.api.contract.model.CrudObjectDto;
import com.xavelo.template.api.contract.model.CrudObjectPageDto;
import com.xavelo.template.api.contract.model.CrudObjectPatchRequestDto;
import com.xavelo.template.api.contract.model.CrudObjectUpdateRequestDto;

/**
 * Application port that exposes CRUD operations for {@link CrudObjectDto} resources.
 */
public interface CrudUseCase {

    CrudObjectPageDto listCrudObjects(Integer page, Integer size, String sort);

    CrudObjectDto getCrudObject(String crudObjectId);

    CrudObjectDto createCrudObject(CrudObjectCreateRequestDto request);

    CrudObjectDto replaceCrudObject(String crudObjectId, CrudObjectUpdateRequestDto request);

    CrudObjectDto updateCrudObject(String crudObjectId, CrudObjectPatchRequestDto request);

    void deleteCrudObject(String crudObjectId);
}
