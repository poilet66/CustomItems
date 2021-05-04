package me.poilet66.customitems.Items;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class CustomInteractItem extends CustomItemBase {

    public CustomInteractItem(String ID, ItemStack item) {
        super(ID, item, CustomItemType.INTERACT_ITEM);
    }

    public abstract void onUse(PlayerInteractEvent event);
}
