package com.xavelo.template.application.port.in;

import com.xavelo.template.api.contract.model.CrudObjectCreateRequestDto;
import com.xavelo.template.api.contract.model.CrudObjectDto;
import com.xavelo.template.api.contract.model.CrudObjectPageDto;

/**
 * Application port that exposes CRUD operations for {@link CrudObjectDto} resources.
 */
public interface CrudUseCase {

    CrudObjectPageDto listCrudObjects(Integer page, Integer size, String sort);

    CrudObjectDto getCrudObject(String crudObjectId);

    CrudObjectDto createCrudObject(CrudObjectCreateRequestDto request);

}
