package com.hm.outfitrecommendation.services;

import com.hm.outfitrecommendation.entities.CategoryType;
import com.hm.outfitrecommendation.entities.EventType;
import com.hm.outfitrecommendation.entities.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {
    private final Map<UUID, User> userDatabase = new HashMap<>();
    private List<InventoryItem> inventory = new ArrayList<>();
    public RecommendationService() {
        // Initialize with mock data for testing
        initializeInventory();
    }

    public List<InventoryItem> recommendOutfit(UUID userId, EventType eventType, boolean registeredCustomer, double budget) {
        /*List<InventoryItem> recommendations = new ArrayList<>();
        for (InventoryItem item : inventoryService.getInventory()) {
            if (item.getPrice() <= budget && itemMatchesEventType(item, eventType)) {
                if (item.getAvailableQuantity() == 0) {
                    System.out.println(item.getName() + " is out of stock.");
                } else {
                    if (item.getAvailableQuantity() < 10) {
                        System.out.println(item.getName() + " is running low on stock.");
                    }
                    recommendations.add(item);
                }
            }
        }
        return recommendations;*/
        if (registeredCustomer) {
            User user = userDatabase.get(userId);
            if (user != null && user.isRecurrent()) {
                return getPersonalizedRecommendations(user, eventType, budget);
            }
        }
        return getGeneralRecommendations(eventType, budget);
    }

    private List<Map<String, Object>> getPersonalizedRecommendations(User user, EventType eventType, double budget) {
        // Get items the user has given positive feedback on
        Set<Long> likedItemIds = user.getFeedback().stream()
                .filter(Feedback::isPositive)
                .map(Feedback::getItemId)
                .collect(Collectors.toSet());

        // Get items the user has purchased before
        Set<Long> purchasedItemIds = user.getPurchaseHistory().stream()
                .map(Purchase::getItemId)
                .collect(Collectors.toSet());

        // Filter inventory based on user's preferences and feedback
        List<Map<String, Object>> recommendations = new ArrayList<>();
        for (InventoryItem item : inventory) {
            if (item.getPrice() <= budget && itemMatchesEventType(item, eventType)) {
                boolean isPreferred = likedItemIds.contains(item.getId()) || purchasedItemIds.contains(item.getId());
                recommendations.add(createRecommendationMap(item, isPreferred));
            }
        }

        // Sort recommendations to prioritize preferred items
        recommendations.sort((a, b) -> {
            boolean aPreferred = (boolean) a.get("preferred");
            boolean bPreferred = (boolean) b.get("preferred");
            return Boolean.compare(bPreferred, aPreferred);
        });

        return recommendations;
    }

    private List<Map<String, Object>> getGeneralRecommendations(EventType eventType, double budget) {
        List<Map<String, Object>> recommendations = new ArrayList<>();
        for (InventoryItem item : inventory) {
            if (item.getPrice() <= budget && itemMatchesEventType(item, eventType)) {
                recommendations.add(createRecommendationMap(item));
            }
        }
        return recommendations;
    }


    private boolean itemMatchesEventType(InventoryItem item, EventType eventType) {
        switch (eventType) {
            case CASUAL_OUTING:
                return item.getCategory() == CategoryType.CASUAL;
            case WEDDING:
                return item.getCategory() == CategoryType.FORMAL || item.getCategory() == CategoryType.WEDDING;
            case CHRISTMAS:
                return item.getCategory() == CategoryType.WINTER || item.getCategory() == CategoryType.CHRISTMAS;
            default:
                return false;
        }
    }

    private List<Outfit> generateOutfits(List<InventoryItem> items, Set<Long> likedItemIds, double budget) {
        List<Outfit> outfits = new ArrayList<>();
        Map<CategoryType, List<InventoryItem>> itemsByCategory = items.stream()
                .collect(Collectors.groupingBy(InventoryItem::getCategory));

        List<InventoryItem> accessories = itemsByCategory.getOrDefault(CategoryType.ACCESSORY, new ArrayList<>());
        List<InventoryItem> clothingItems = items.stream()
                .filter(item -> item.getCategory() != CategoryType.ACCESSORY)
                .collect(Collectors.toList());

        for (InventoryItem clothingItem : clothingItems) {
            for (InventoryItem accessory : accessories) {
                double totalPrice = clothingItem.getPrice() + accessory.getPrice();
                if (totalPrice <= budget) {
                    outfits.add(new Outfit(Arrays.asList(clothingItem, accessory), totalPrice, likedItemIds.contains(clothingItem.getId())));
                }
            }
        }

        // Handle cases where the budget is too low
        if (outfits.isEmpty() && !clothingItems.isEmpty()) {
            InventoryItem cheapestItem = Collections.min(clothingItems, Comparator.comparingDouble(InventoryItem::getPrice));
            outfits.add(new Outfit(Collections.singletonList(cheapestItem), cheapestItem.getPrice(), likedItemIds.contains(cheapestItem.getId())));
        }

        return outfits;
    }

    private void initializeInventory() {
        inventory.add(new InventoryItem(1L, "Casual Trousers", CategoryType.CASUAL, 50.0, 15));
        inventory.add(new InventoryItem(2L, "Formal Shirt", CategoryType.FORMAL, 30.0, 10));
        inventory.add(new InventoryItem(3L, "Winter Jacket", CategoryType.WINTER, 100.0, 5));
        inventory.add(new InventoryItem(4L, "Casual Shoes", CategoryType.CASUAL, 70.0, 8));
        inventory.add(new InventoryItem(5L, "Watch", CategoryType.ACCESSORY, 25.0, 20));
    }
}

