package com.xavelo.template.adapter.in.http.crud;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.api.contract.api.CrudApi;
import com.xavelo.template.api.contract.model.CrudObjectCreateRequestDto;
import com.xavelo.template.api.contract.model.CrudObjectDto;
import com.xavelo.template.api.contract.model.CrudObjectPageDto;
import com.xavelo.template.application.port.in.CrudUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Adapter
@RestController
public class CrudController implements CrudApi {

    private static final Logger logger = LogManager.getLogger(CrudController.class);

    private final CrudUseCase crudUseCase;

    public CrudController(CrudUseCase crudUseCase) {
        this.crudUseCase = crudUseCase;
    }

    @Override
    @CountAdapterInvocation(name = "crud-create", direction = AdapterMetrics.Direction.IN, type = AdapterMetrics.Type.HTTP)
    public ResponseEntity<Void> createCrudObject(CrudObjectCreateRequestDto crudObjectCreateRequestDto) {
        CrudObjectDto created = crudUseCase.createCrudObject(crudObjectCreateRequestDto);
        logger.info("Created CrudObject {}", created.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{crudObjectId}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    @CountAdapterInvocation(name = "crud-list", direction = AdapterMetrics.Direction.IN, type = AdapterMetrics.Type.HTTP)
    public ResponseEntity<CrudObjectPageDto> listCrudObjects(Integer page, Integer size, String sort) {
        CrudObjectPageDto list = crudUseCase.listCrudObjects(page, size, sort);
        logger.info("Listing CrudObjects page={}, size={}, sort={}", page, size, sort);
        return ResponseEntity.ok(list);
    }

    @Override
    @CountAdapterInvocation(name = "crud-get", direction = AdapterMetrics.Direction.IN, type = AdapterMetrics.Type.HTTP)
    public ResponseEntity<CrudObjectDto> getCrudObject(String crudObjectId) {
        CrudObjectDto crudObject = crudUseCase.getCrudObject(crudObjectId);
        logger.info("Retrieved CrudObject {}", crudObjectId);
        return ResponseEntity.ok(crudObject);
    }



}
