package com.hm.outfitrecommendation.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Purchase {
    private String userId;
    private List<InventoryItem> item;
}
