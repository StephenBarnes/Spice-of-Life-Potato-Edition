package com.kevun1.solpotato.utils;

import com.kevun1.solpotato.SOLPotato;
import com.kevun1.solpotato.tracking.FoodInstance;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubstitutionParser {
    public static Map<FoodInstance, FoodInstance> parse(List<String> unparsed) {
        Map<FoodInstance, FoodInstance> substitutionMap = new HashMap<>();

        for (String substitutionString : unparsed) {
            String[] s = substitutionString.split(",", 0);
            if (s.length != 2) {
                SOLPotato.LOGGER.warn("Invalid substitution specification: " + substitutionString);
                continue;
            }

            FoodInstance oldFood, newFood;
            try {
                oldFood = parseFoodString(s[0]);
                newFood = parseFoodString(s[1]);            	
            } catch (Exception e) {
            	SOLPotato.LOGGER.warn(e.getMessage());
            	continue;
            }
            
            substitutionMap.put(oldFood, newFood);
        }
        return substitutionMap;
    }
    
    private static FoodInstance parseFoodString(String name) throws Exception {
    	Item item;
    	try {
            item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
        }
        catch (ResourceLocationException e) {
            throw new Exception("Invalid item name: " + name);
        }
        if (item == null) {
            throw new Exception("Invalid item name: " + name);
        }
        if (!item.isFood()) {
            throw new Exception("Item is not food: " + name);
        }
        FoodInstance food = new FoodInstance(item);
        if (food.encode() == null) {
            throw new Exception("Item does not exist: " + name);
        }
        return food;
    }
}
