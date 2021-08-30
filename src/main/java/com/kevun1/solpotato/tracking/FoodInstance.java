package com.kevun1.solpotato.tracking;

import com.kevun1.solpotato.ConfigHandler;
import com.kevun1.solpotato.SOLPotato;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Optional;

public final class FoodInstance {
	public final Item item;
	
	public FoodInstance(Item item) {
		this.item = item;
	}
	
	public static FoodInstance substituted(Item item) {
		FoodInstance food = new FoodInstance(item);
		return ConfigHandler.substitutionMap.getOrDefault(food, food);
	}
	
	@Nullable
	public static FoodInstance decode(String encoded) {
		ResourceLocation name = new ResourceLocation(encoded);
		
		// TODO it'd be nice to store (and maybe even count) references to missing items, in case the mod is added back in later
		Item item = ForgeRegistries.ITEMS.getValue(name);
		if (item == null) {
			SOLPotato.LOGGER.warn("attempting to load item into food list that is no longer registered: " + encoded + " (removing from list)");
			return null;
		}
		
		if (!item.isFood()) {
			SOLPotato.LOGGER.warn("attempting to load item into food list that is no longer edible: " + encoded + " (ignoring in case it becomes edible again later)");
		}
		
		return new FoodInstance(item);
	}
	
	@Nullable
	public String encode() {
		return Optional.ofNullable(ForgeRegistries.ITEMS.getKey(item))
			.map(ResourceLocation::toString)
			.orElse(null);
	}
	
	@Override
	public int hashCode() {
		return item.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FoodInstance)) return false;
		FoodInstance other = (FoodInstance) obj;
		
		return item.equals(other.item);
	}
	
	public Item getItem() {
		return item;
	}

	@Override
	public String toString() {
		String enc = encode();
		if (enc == null)
			return "null";
		else
			return enc;
	}
}
