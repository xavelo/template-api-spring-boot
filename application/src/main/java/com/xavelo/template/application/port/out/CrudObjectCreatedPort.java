package com.xavelo.template.application.port.out;

import com.xavelo.template.application.domain.CrudObject;

public interface CrudObjectCreatedPort {
    void publishCrudObectCreated(CrudObject crud);
}
