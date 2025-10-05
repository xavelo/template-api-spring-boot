package com.xavelo.template.application.port.in;

import com.xavelo.template.api.contract.model.ItemCreateRequestDto;
import com.xavelo.template.api.contract.model.ItemDto;
import com.xavelo.template.api.contract.model.ItemPageDto;

/**
 * Application port that exposes CRUD operations for {@link ItemDto} resources.
 */
public interface ItemUseCase {

    ItemPageDto listItems(Integer page, Integer size, String sort);

    ItemDto getItem(String itemId);

    ItemDto createItem(ItemCreateRequestDto request);

}
