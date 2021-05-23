package me.poilet66.customitems.Items;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public abstract class CustomProjectileItem extends CustomItemBase{

    public CustomProjectileItem(String ID, ItemStack item) {
        super(ID, item, CustomItemType.PROJECTILE);
    }

    public CustomProjectileItem(String ID) {
        super(ID, CustomItemType.PROJECTILE);
    }

    /**
     * Function that when overridden gives each specific item its own functionality, they will automatically be called in the listener class.
     * @param event - Event to be passed into function, can treat it like a normal event being passed into eventlistener
     */
    public abstract void onUse(ProjectileHitEvent event);

    public abstract void onHit(EntityDamageByEntityEvent event);

    public abstract  void onThrow(ProjectileLaunchEvent event);


}
