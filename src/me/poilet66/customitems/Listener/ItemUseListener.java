package me.poilet66.customitems.Listener;

import me.poilet66.customitems.CustomItems;

import me.poilet66.customitems.Items.CustomAttackItem;
import me.poilet66.customitems.Items.CustomInteractItem;
import me.poilet66.customitems.Items.CustomItemBase;
import me.poilet66.customitems.Items.CustomProjectileItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ItemUseListener implements Listener {

    private final CustomItems main;

    public ItemUseListener(CustomItems main) {
        this.main = main;
    }

    @EventHandler
    public void onAttackItemUse(EntityDamageByEntityEvent event) {
        for(Map.Entry<ItemStack, CustomItemBase> entry : main.getIR().itemMap.entrySet()) { //TODO: find a way to directly call element rather than looping through?
            if(entry.getValue() instanceof  CustomProjectileItem) {
                CustomProjectileItem projectileItem = (CustomProjectileItem) entry.getValue();
                projectileItem.onHit(event);
            }
            if(entry.getValue() instanceof CustomAttackItem) {
                CustomAttackItem attackItem = (CustomAttackItem) entry.getValue();
                attackItem.onUse(event);
            }
        }
    }

    @EventHandler
    public void onInteractItemUse(PlayerInteractEvent event) {
        for(Map.Entry<ItemStack, CustomItemBase> entry : main.getIR().itemMap.entrySet()) { //TODO: find a way to directly call element rather than looping through?
            if(entry.getValue() instanceof CustomInteractItem) {
                CustomInteractItem attackItem = (CustomInteractItem) entry.getValue();
                attackItem.onUse(event);
            }
        }
    }

    @EventHandler
    public void onProjectileItemUse(ProjectileHitEvent event) {
        for(Map.Entry<ItemStack, CustomItemBase> entry : main.getIR().itemMap.entrySet()) {
            if(entry.getValue() instanceof CustomProjectileItem) {
                CustomProjectileItem projectileItem = (CustomProjectileItem) entry.getValue();
                projectileItem.onUse(event);
            }
        }
    }

    @EventHandler
    public void onProjectileItemThrow(ProjectileLaunchEvent event) {
        for(Map.Entry<ItemStack, CustomItemBase> entry : main.getIR().itemMap.entrySet()) {
            if(entry.getValue() instanceof  CustomProjectileItem) {
                CustomProjectileItem projectileItem = (CustomProjectileItem) entry.getValue();
                projectileItem.onThrow(event);
            }
        }
    }

}
