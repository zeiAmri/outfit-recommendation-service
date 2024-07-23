package com.hm.outfitrecommendation.services;

import com.hm.outfitrecommendation.entities.InventoryItem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Setter
@Getter
@Service
public class InventoryService {
    private List<InventoryItem> inventory;

}
