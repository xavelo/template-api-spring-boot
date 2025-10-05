package com.xavelo.template.application.port.out;

import com.xavelo.template.application.domain.Item;

public interface ItemCreatedPort {
    void publishItemCreated(Item item);
}
