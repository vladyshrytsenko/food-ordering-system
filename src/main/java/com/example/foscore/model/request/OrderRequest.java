package com.example.foscore.model.request;

import com.example.foscore.model.entity.Order;
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

    private List<String> mealNames;
    private List<String> dessertNames;
    private List<String> drinkNames;
    private Boolean iceCubes;
    private Boolean lemon;

    public static Order toEntity(OrderRequest request) {
        return Order.builder()
            .iceCubes(request.getIceCubes())
            .lemon(request.getLemon())
            .build();
    }
}
