package com.hm.outfitrecommendation.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
public class InventoryItem {

    private String itemId;
    private String name;
    private CategoryType category;
    private double price;
    private int availableQuantity;
}
