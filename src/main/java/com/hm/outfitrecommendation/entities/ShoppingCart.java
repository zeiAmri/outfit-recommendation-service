package com.hm.outfitrecommendation.entities;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ShoppingCart {

    private Long cartId;
    private UUID userId;
    private List<String> items;
    private double totalPrice;
}
