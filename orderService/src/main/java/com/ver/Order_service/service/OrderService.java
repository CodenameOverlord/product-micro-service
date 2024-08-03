package com.ver.Order_service.service;

import com.ver.Order_service.dtos.OrderLineItemsDto;
import com.ver.Order_service.dtos.OrderRequest;
import com.ver.Order_service.model.Order;
import com.ver.Order_service.model.OrderLineItems;
import com.ver.Order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList = orderRequest
                .getOrderLineItemsDtoList()
                .stream()
                .map(this::toOrderLineDto).collect(Collectors.toList());
        order.setOrderLineItemsList(orderLineItemsList);
        
        orderRepository.save(order);
    }

    private OrderLineItems toOrderLineDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
