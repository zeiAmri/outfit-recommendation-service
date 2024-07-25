package com.hm.outfitrecommendation.controller;

import com.hm.outfitrecommendation.dto.RecommendationRequest;
import com.hm.outfitrecommendation.model.*;
import com.hm.outfitrecommendation.services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OutfitRecommendationController {
    @Autowired
    private RecommendationService recommendationService;
    @PostMapping(value = "/recommendations")
    public List<List<InventoryItem>> getRecommendations(@RequestBody RecommendationRequest request) {
        User user = new User(request.getUserId(), "dummy@example.com", request.isRegisteredCustomer());
        return recommendationService.getRecommendations(user, request.getEventType(), request.getBudget());
    }
}
