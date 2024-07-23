package com.hm.outfitrecommendation.controller;

import com.hm.outfitrecommendation.dto.RecommendationRequest;
import com.hm.outfitrecommendation.entities.InventoryItem;
import com.hm.outfitrecommendation.services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations/")
public class Controller {
    @Autowired
    private RecommendationService recommendationService;
    @PostMapping
    public List<InventoryItem> getRecommendations(@RequestBody RecommendationRequest request) {
        return recommendationService.recommendOutfit(request.getEventType(), request.getBudget());
    }
}
