package com.hm.outfitrecommendation.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.hm.outfitrecommendation.model.*;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RecommendationServiceTest {

    private RecommendationService recommendationService;
    private List<InventoryItem> inventory1;
    private List<InventoryItem> inventory2;
    private List<Purchase> purchaseHistory;

    @BeforeEach
    public void setUp() {
        inventory1 = new ArrayList<>();
        inventory2 = new ArrayList<>();
        purchaseHistory = new ArrayList<>();

        // Setup sample inventory items
        inventory1.add(new InventoryItem("item1", "scarf", EventType.WINTER, CategoryType.OUTERWEAR, 200, 10));
        inventory1.add(new InventoryItem("item2", "top", EventType.SUMMER, CategoryType.TOP, 50, 10));
        inventory1.add(new InventoryItem("item6", "top", EventType.SUMMER, CategoryType.TOP, 50, 10));
        inventory1.add(new InventoryItem("item7", "shorts", EventType.SUMMER, CategoryType.BOTTOM, 200, 10));
        inventory1.add(new InventoryItem("item3", "pants", EventType.CASUAL, CategoryType.BOTTOM, 100, 10));
        inventory2.add(new InventoryItem("item4", "shoes", EventType.FORMAL, CategoryType.FOOTWEAR, 150, 10));
        inventory2.add(new InventoryItem("item5", "necklace", EventType.SPRING, CategoryType.ACCESSORY, 30, 10));

        // Setup sample purchase history
        purchaseHistory.add(new Purchase("user1", inventory1));

        // Initialize service with sample data
        recommendationService = new RecommendationService(inventory1, purchaseHistory);
    }

    @Test
    public void testGetPersonalizedRecommendations() {
        User user = new User("user1", "dummydums@example.com", true);
        List<List<InventoryItem>> recommendations = recommendationService.getRecommendations(user, EventType.SUMMER,
                300);

        // Validate the recommendations
        assertFalse(recommendations.isEmpty());
        boolean hasTop = recommendations.stream()
                .flatMap(List::stream)
                .anyMatch(item -> item.getCategory() == CategoryType.TOP);
        assertTrue(hasTop);
    }

    @Test
    public void testGetGeneralRecommendations() {
        User user = new User("user2", "dummydums@example.com", false);
        List<List<InventoryItem>> recommendations = recommendationService.getRecommendations(user, EventType.WINTER,
                300);

        // Validate the recommendations
        assertFalse(recommendations.isEmpty());
        boolean hasOuterwear = recommendations.stream()
                .flatMap(List::stream)
                .anyMatch(item -> item.getCategory() == CategoryType.OUTERWEAR);
        assertTrue(hasOuterwear);
    }

    @Test
    public void testGenerateOutfitRecommendations() {
        List<InventoryItem> items = new ArrayList<>();
        items.add(new InventoryItem("item6", "Jacket", EventType.WINTER, CategoryType.OUTERWEAR, 200, 10));
        items.add(new InventoryItem("item7", "Shirt", EventType.SUMMER, CategoryType.TOP, 100, 10));
        items.add(new InventoryItem("item2", "Top", EventType.SUMMER, CategoryType.TOP, 50, 10));
        items.add(new InventoryItem("item3", "Pants", EventType.CASUAL, CategoryType.BOTTOM, 100, 10));
        items.add(new InventoryItem("item4", "Shoes", EventType.FORMAL, CategoryType.FOOTWEAR, 150, 10));

        List<List<InventoryItem>> recommendations = recommendationService.generateOutfitRecommendations(items, 300);

        // Check that there are valid combinations within the budget
        assertFalse(recommendations.isEmpty());

        // Example: Checking if a specific combination exists
        List<InventoryItem> expectedCombination = List.of(
                new InventoryItem("item2", "Top", EventType.SUMMER, CategoryType.TOP, 50, 10),
                new InventoryItem("item3", "Pants", EventType.CASUAL, CategoryType.BOTTOM, 100, 10),
                new InventoryItem("item4", "Shoes", EventType.FORMAL, CategoryType.FOOTWEAR, 150, 10));

        boolean containsExpectedCombination = recommendations.stream().anyMatch(
                recipe -> recipe.containsAll(expectedCombination) && recipe.size() == expectedCombination.size());

        assertTrue(containsExpectedCombination);
    }

    @Test
    public void testGetRecommendationsWithTightBudget() {
        User user = new User("user3", "dummyDummy@example.com", false);
        List<List<InventoryItem>> recommendations = recommendationService.getRecommendations(user, EventType.WINTER,
                200);

        // Validate the recommendations
        assertFalse(recommendations.isEmpty());
        boolean hasOuterwear = recommendations.stream()
                .flatMap(List::stream)
                .anyMatch(item -> item.getCategory() == CategoryType.OUTERWEAR);
        assertTrue(hasOuterwear);
    }

    @Test
    public void testEmptyInventory() {
        recommendationService = new RecommendationService(new ArrayList<>(), purchaseHistory);
        User user = new User("user1", "dummy@example.com", true);
        List<List<InventoryItem>> recommendations = recommendationService.getRecommendations(user, EventType.FORMAL,
                300);

        // Validate the recommendations
        assertTrue(recommendations.isEmpty());
    }

    @Test
    public void testNoPurchaseHistory() {
        purchaseHistory = new ArrayList<>();
        recommendationService = new RecommendationService(inventory2, purchaseHistory);
        User user = new User("user1", "dummy2@example.com", true);
        List<List<InventoryItem>> recommendations = recommendationService.getRecommendations(user, EventType.FORMAL,
                300);

        System.out.println("Recommendations with no purchase history: " + recommendations);
        assertFalse(recommendations.isEmpty());
        boolean hasFootwear = recommendations.stream()
                .flatMap(List::stream)
                .anyMatch(item -> item.getCategory() == CategoryType.FOOTWEAR);
        assertTrue(hasFootwear);
    }
}