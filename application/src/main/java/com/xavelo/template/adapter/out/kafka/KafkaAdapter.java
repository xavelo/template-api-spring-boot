package com.xavelo.template.adapter.out.kafka;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.template.application.domain.CrudObject;
import com.xavelo.template.application.port.out.CrudObjectCreatedPort;

@Adapter
public class KafkaAdapter implements CrudObjectCreatedPort {

    @Override
    public void publishCrudObectCreated(CrudObject crud) {

    }
}
