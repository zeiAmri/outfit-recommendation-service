package com.hm.outfitrecommendation.entities;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class User {

    private UUID userId;
    private String email;
    private String name;
    private List<InventoryItem> purchaseHistory = new ArrayList<>();
    private List<Feedback> feedback = new ArrayList<>();

    // Adding methods to update purchase history and feedback
    public void addPurchase(InventoryItem purchase) {
        this.purchaseHistory.add(purchase);
    }

    public void addFeedback(Feedback feedback) {
        this.feedback.add(feedback);
    }
}
