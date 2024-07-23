package com.hm.outfitrecommendation.entities;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Feedback {

    private Long itemId;
    private boolean positive;
    private int rating;
}
