package com.hm.outfitrecommendation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hm.outfitrecommendation.model.InventoryItem;

import lombok.Data;

@Data
public class RecommendationResponse {

    @JsonProperty("recommendedItems")
    List<List<InventoryItem>> recommendedItems;

}
