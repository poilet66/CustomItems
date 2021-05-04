package me.poilet66.customitems.Items;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public abstract class CustomAttackItem extends CustomItemBase {

    public CustomAttackItem(String ID, ItemStack item) {
        super(ID, item, CustomItemType.ATTACK_ITEM);
    }

    public CustomAttackItem(String ID) {
        super(ID, CustomItemType.ATTACK_ITEM);
    }

    /**
     * Function that when overridden gives each specific item its own functionality, they will automatically be called in the listener class.
     * @param event - Event to be passed into function, can treat it like a normal event being passed into eventlistener
     */
    public abstract void onUse(EntityDamageByEntityEvent event);
}
