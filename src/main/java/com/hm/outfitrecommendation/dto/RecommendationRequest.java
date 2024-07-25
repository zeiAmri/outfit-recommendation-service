package com.hm.outfitrecommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hm.outfitrecommendation.model.EventType;

import lombok.Data;
import java.util.UUID;

@Data
public class RecommendationRequest {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("isRegistered")
    private boolean registeredCustomer;
    @JsonProperty("eventType")
    private EventType eventType;
    @JsonProperty("budget")
    private double budget;
}
