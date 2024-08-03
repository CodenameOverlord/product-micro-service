package com.ver.Order_service.service;

import com.ver.Order_service.dtos.InventoryResponse;
import com.ver.Order_service.dtos.OrderLineItemsDto;
import com.ver.Order_service.dtos.OrderRequest;
import com.ver.Order_service.model.Order;
import com.ver.Order_service.model.OrderLineItems;
import com.ver.Order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList = orderRequest
                .getOrderLineItemsDtoList()
                .stream()
                .map(this::toOrderLineDto).collect(Collectors.toList());
        order.setOrderLineItemsList(orderLineItemsList);
        //check whether the quantity is present in the inventory service before
        //placing the order in the orderService
        List<String> skuCodes = order
                .getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .collect(Collectors.toList());

        InventoryResponse[] inventoryResponseArray = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        assert inventoryResponseArray != null;
        boolean allProductsInStock = Arrays
                .stream(inventoryResponseArray)
                .allMatch(InventoryResponse::getIsInStock);
        if(allProductsInStock)
            orderRepository.save(order);
        else{
            throw new IllegalArgumentException("Product is not present in the stock, please try again later.");
        }
    }

    private OrderLineItems toOrderLineDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
