package com.hm.outfitrecommendation.model;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String userId;
    private String email;
    private boolean isRegistered;

}
