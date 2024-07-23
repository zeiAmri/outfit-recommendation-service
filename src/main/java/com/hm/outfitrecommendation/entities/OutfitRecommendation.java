package com.hm.outfitrecommendation.entities;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OutfitRecommendation {

    private Long recommendationId;
    private UUID userId;
    private EventType eventType;
    private double budget;
    private List<InventoryItem> items;
}
