# Outfit Recommendation System

## Overview

The Outfit Recommendation System is a backend service designed to suggest outfit combinations for various events based on user preferences, purchase history, and available inventory. It generates personalized outfit recommendations for registered users and general recommendations for others.

## Features

- **Personalized Recommendations**: Uses user purchase history to suggest outfits.
- **General Recommendations**: Suggests outfits based on event types for non-registered users.
- **Budget Handling**: Considers user budget to provide outfit recommendations within the specified price range.
- **Outfit Combinations**: Generates multiple outfit combinations, taking into account categories and remaining budget.

## Architecture

The system consists of the following components:

1. **Recommendation Service**: Core logic for generating outfit recommendations.
2. **Model Classes**: Represents data entities like `InventoryItem`, `Purchase`, `User`, and `RecommendationRequest`.
3. **Endpoints**: API endpoints to receive user inputs and return recommendations.

**Architecture Diagram:**

   ![Architecture Diagram](images/Architecture-diagram.png)

**ERD:**

![Architecture Diagram](images/ERD-diagram.png)

## Getting Started

### Setup

1. **Clone the Repository**

   ```sh
   git clone https://github.com/your-username/your-repository.git
   cd your-repository

### Improvement:

In order to get multiple recipes back in the outfit recommendation response, we can change the ```generateOutfitRecommendations()``` to the following:
```
public List<List<InventoryItem>> generateOutfitRecommendations(List<InventoryItem> items, double budget) {
        List<List<InventoryItem>> results = new ArrayList<>();

        // Group items by category
        Map<CategoryType, List<InventoryItem>> itemsByCategory = items.stream()
                .collect(Collectors.groupingBy(InventoryItem::getCategory));

        // Step 1: Generate initial combinations with one item from each category
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

        // Step 2: Generate new combinations with the remaining items
        List<InventoryItem> remainingItems = new ArrayList<>(items);
        remainingItems.removeAll(initialCombination);
        findCombinations(remainingItems, budget, new ArrayList<>(), results);

        return results;
    }

    private void findCombinations(List<InventoryItem> items, double budget,
            List<InventoryItem> currentCombination, List<List<InventoryItem>> results) {
        double totalPrice = currentCombination.stream().mapToDouble(InventoryItem::getPrice).sum();

        if (totalPrice <= budget && !currentCombination.isEmpty()) {
            results.add(new ArrayList<>(currentCombination));
        }

        for (int i = 0; i < items.size(); i++) {
            InventoryItem item = items.get(i);
            if (currentCombination.isEmpty() || item.getPrice() + totalPrice <= budget) {
                currentCombination.add(item);
                findCombinations(items.subList(i + 1, items.size()), budget, currentCombination, results);
                currentCombination.remove(currentCombination.size() - 1);
            }
        }
    }
```

I'm not that satisfied with ```recursion```, hence why I did not add it to the final solution (is not optimal for real life scenario, where we might have more than 100K items).
