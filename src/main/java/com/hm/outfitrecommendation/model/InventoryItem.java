package com.hm.outfitrecommendation.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryItem {

    private String itemId;
    private String name;
    private EventType eventType;
    private CategoryType category;
    private double price;
    private int quantity;

    @Override
    public String toString() {
        return "InventoryItem{" +
                "itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", eventType=" + eventType +
                ", category=" + category +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
