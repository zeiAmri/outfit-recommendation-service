package com.hm.outfitrecommendation.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hm.outfitrecommendation.model.*;
import com.hm.outfitrecommendation.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class RecommendationService {
    private List<InventoryItem> inventory;
    private List<Purchase> purchaseHistory;

    // Constructor for dependency injection
    public RecommendationService(List<InventoryItem> inventory, List<Purchase> purchaseHistory) {
        this.inventory = inventory;
        this.purchaseHistory = purchaseHistory;

    }

    public List<List<InventoryItem>> getRecommendations(User user, EventType eventType, double budget) {
        if (!inventory.isEmpty()) {
            if (user.isRegistered() && !purchaseHistory.isEmpty()) {
                return getPersonalizedRecommendations(user, eventType, budget);
            } else {
                return getGeneralRecommendations(eventType, budget);
            }
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    private List<List<InventoryItem>> getPersonalizedRecommendations(User user, EventType eventType, double budget) {
        List<InventoryItem> userPurchaseHistory = purchaseHistory.stream()
                .filter(purchase -> purchase.getUserId().equals(user.getUserId()))
                .flatMap(purchase -> purchase.getItem().stream())
                .collect(Collectors.toList());

        List<InventoryItem> recommendations = inventory.stream()
                .filter(item -> item.getEventType().equals(eventType) && item.getQuantity() > 0)
                .filter(item -> isSimilarToUserHistory(item, userPurchaseHistory))
                .collect(Collectors.toList());

        return generateOutfitRecommendations(recommendations, budget);
    }

    private boolean isSimilarToUserHistory(InventoryItem item, List<InventoryItem> userPurchaseHistory) {
        return userPurchaseHistory.stream()
                .anyMatch(purchase -> purchase.getCategory().equals(item.getCategory()) ||
                        purchase.getEventType().equals(item.getEventType()));
    }

    private List<List<InventoryItem>> getGeneralRecommendations(EventType eventType, double budget) {
        List<InventoryItem> recommendations = inventory.stream()
                .filter(item -> item.getEventType().equals(eventType))
                .collect(Collectors.toList());

        return generateOutfitRecommendations(recommendations, budget);
    }

    public List<List<InventoryItem>> generateOutfitRecommendations(List<InventoryItem> items, double budget) {
        List<List<InventoryItem>> results = new ArrayList<>();

        // Group items by category
        Map<CategoryType, List<InventoryItem>> itemsByCategory = items.stream()
                .collect(Collectors.groupingBy(InventoryItem::getCategory));

        // Initial step: add one item from each category
        List<InventoryItem> initialCombination = new ArrayList<>();
        double initialPrice = 0.0;
        for (CategoryType category : CategoryType.values()) {
            List<InventoryItem> categoryItems = itemsByCategory.getOrDefault(category, Collections.emptyList());
            if (!categoryItems.isEmpty()) {
                // Choose the cheapest item in this category
                InventoryItem cheapestItem = categoryItems.stream()
                        .min(Comparator.comparingDouble(InventoryItem::getPrice))
                        .orElse(null);
                if (cheapestItem != null && initialPrice + cheapestItem.getPrice() <= budget) {
                    initialCombination.add(cheapestItem);
                    initialPrice += cheapestItem.getPrice();
                }
            }
        }

        if (initialPrice <= budget) {
            results.add(new ArrayList<>(initialCombination));
        }

        // Allow for mix-and-match within remaining budget
        List<InventoryItem> remainingItems = new ArrayList<>(items);
        remainingItems.removeAll(initialCombination);
        findCombinations(remainingItems, budget - initialPrice, 0, new ArrayList<>(initialCombination), results);

        return results;
    }

    private void findCombinations(List<InventoryItem> items, double remainingBudget, int start,
            List<InventoryItem> currentCombination, List<List<InventoryItem>> results) {
        double totalPrice = currentCombination.stream().mapToDouble(InventoryItem::getPrice).sum();

        if (totalPrice <= remainingBudget) {
            results.add(new ArrayList<>(currentCombination));
        }

        for (int i = start; i < items.size(); i++) {
            InventoryItem item = items.get(i);
            currentCombination.add(item);
            findCombinations(items, remainingBudget, i + 1, currentCombination, results);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }

}
