package com.hm.outfitrecommendation.dto;

import com.hm.outfitrecommendation.entities.EventType;
import lombok.Data;
import java.util.UUID;

@Data
public class RecommendationRequest {
    private UUID userId;
    private boolean registeredCustomer;
    private EventType eventType;
    private double budget;
}
