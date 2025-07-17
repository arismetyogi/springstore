package com.arism.mapper;

import com.arism.dto.CartItemDto;
import com.arism.dto.OrderDto;
import com.arism.model.Cart;
import com.arism.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "items") // based on the properties set on model/dto
    OrderDto toDto(Order order);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "items", source = "orderItems")
    Order toEntity(OrderDto orderDto);
}
