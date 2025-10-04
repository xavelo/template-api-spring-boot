package com.xavelo.template.adapter.in.http.crud;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.api.contract.api.CrudApi;
import com.xavelo.template.api.contract.model.CrudObjectCreateRequestDto;
import com.xavelo.template.api.contract.model.CrudObjectDto;
import com.xavelo.template.api.contract.model.CrudObjectPageDto;
import com.xavelo.template.api.contract.model.CrudObjectPatchRequestDto;
import com.xavelo.template.api.contract.model.CrudObjectUpdateRequestDto;
import com.xavelo.template.application.port.in.CrudUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Adapter
public class CrudController implements CrudApi {

    private static final Logger logger = LogManager.getLogger(CrudController.class);

    private final CrudUseCase crudUseCase;

    public CrudController(CrudUseCase crudUseCase) {
        this.crudUseCase = crudUseCase;
    }

    @CountAdapterInvocation(name = "crud-list", direction = AdapterMetrics.Direction.IN, type = AdapterMetrics.Type.HTTP)
    @Override
    public ResponseEntity<CrudObjectPageDto> listCrudObjects(Integer page, Integer size, String sort) {
        CrudObjectPageDto response = crudUseCase.listCrudObjects(page, size, sort);
        logger.info("Listing CrudObjects page={}, size={}, sort={}", page, size, sort);
        return ResponseEntity.ok(response);
    }

    @CountAdapterInvocation(name = "crud-get", direction = AdapterMetrics.Direction.IN, type = AdapterMetrics.Type.HTTP)
    @Override
    public ResponseEntity<CrudObjectDto> getCrudObject(String crudObjectId) {
        CrudObjectDto crudObject = crudUseCase.getCrudObject(crudObjectId);
        logger.info("Retrieved CrudObject {}", crudObjectId);
        return ResponseEntity.ok(crudObject);
    }

    @CountAdapterInvocation(name = "crud-create", direction = AdapterMetrics.Direction.IN, type = AdapterMetrics.Type.HTTP)
    @Override
    public ResponseEntity<Void> createCrudObject(CrudObjectCreateRequestDto crudObjectCreateRequestDto) {
        CrudObjectDto created = crudUseCase.createCrudObject(crudObjectCreateRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{crudObjectId}")
            .buildAndExpand(created.getId())
            .toUri();
        logger.info("Created CrudObject {}", created.getId());
        return ResponseEntity.created(location).build();
    }

    @CountAdapterInvocation(name = "crud-replace", direction = AdapterMetrics.Direction.IN, type = AdapterMetrics.Type.HTTP)
    @Override
    public ResponseEntity<CrudObjectDto> replaceCrudObject(String crudObjectId, CrudObjectUpdateRequestDto crudObjectUpdateRequestDto) {
        CrudObjectDto replaced = crudUseCase.replaceCrudObject(crudObjectId, crudObjectUpdateRequestDto);
        logger.info("Replaced CrudObject {}", crudObjectId);
        return ResponseEntity.ok(replaced);
    }

    @CountAdapterInvocation(name = "crud-update", direction = AdapterMetrics.Direction.IN, type = AdapterMetrics.Type.HTTP)
    @Override
    public ResponseEntity<CrudObjectDto> updateCrudObject(String crudObjectId, CrudObjectPatchRequestDto crudObjectPatchRequestDto) {
        CrudObjectDto updated = crudUseCase.updateCrudObject(crudObjectId, crudObjectPatchRequestDto);
        logger.info("Updated CrudObject {}", crudObjectId);
        return ResponseEntity.ok(updated);
    }

    @CountAdapterInvocation(name = "crud-delete", direction = AdapterMetrics.Direction.IN, type = AdapterMetrics.Type.HTTP)
    @Override
    public ResponseEntity<Void> deleteCrudObject(String crudObjectId) {
        crudUseCase.deleteCrudObject(crudObjectId);
        logger.info("Deleted CrudObject {}", crudObjectId);
        return ResponseEntity.noContent().build();
    }
}
