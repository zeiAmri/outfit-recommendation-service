package com.hm.outfitrecommendation.services;

import com.hm.outfitrecommendation.entities.EventType;
import com.hm.outfitrecommendation.entities.InventoryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecommendationServiceTest {
    private InventoryService inventoryService;
    private RecommendationService recommendationService;

    @BeforeEach
    public void setUp() {
        inventoryService = mock(InventoryService.class);
        recommendationService = new RecommendationService(inventoryService);
    }

    @Test
    public void testRecommendOutfit() {
        InventoryItem item1 = new InventoryItem("1", "Jacket", "Winter", 100, 10);
        InventoryItem item2 = new InventoryItem("2", "Shirt", "Casual", 50, 20);
        InventoryItem item3 = new InventoryItem("3", "Trousers", "Formal", 80, 15);

        when(inventoryService.getInventory()).thenReturn(Arrays.asList(item1, item2, item3));

        List<InventoryItem> recommendations = recommendationService.recommendOutfit(EventType.CASUAL_OUTING, 100);
        assertEquals(2, recommendations.size());
        assertEquals("Jacket", recommendations.get(0).getName());
        assertEquals("Shirt", recommendations.get(1).getName());
    }
}
