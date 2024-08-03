package com.ver.Order_service.dtos;

import com.ver.Order_service.model.OrderLineItems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    List<OrderLineItemsDto> orderLineItemsDtoList;

}
