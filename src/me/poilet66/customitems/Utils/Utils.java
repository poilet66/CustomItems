package me.poilet66.customitems.Utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Utils {

    public static void switchPosition(Entity player1, Entity player2) {
        Location player1Loc = player1.getLocation();
        Location player2Loc = player2.getLocation();
        player1.teleport(player2Loc);
        player2.teleport(player1Loc);
    }

    /**
     *
     * @param player - Player to check inv of
     * @return - Returns number of first slot that is empty, if full inv returns -1
     */
    public static int getFirstEmptySlot(Player player) {
        Inventory inv = player.getInventory();
        for(int i = 0; i <= 35; i++) {
            if(inv.getItem(i) == null) {
                return i;
            }
            continue;
        }
        return -1;
    }

    public static void setAtFirst(Player player, ItemStack item) {
        int amountLeftToGive = item.getAmount();
        Inventory inv = player.getInventory();
        for(int slot = 0; slot < inv.getSize(); slot++) {
            if(inv.getItem(slot) != null) {
                if(inv.getItem(slot).isSimilar(item)) {
                    int amountToFill = inv.getItem(slot).getType().getMaxStackSize() - inv.getItem(slot).getAmount();
                    if(amountToFill == 0) { //if full stack
                        continue;
                    }
                    if(inv.getItem(slot).getAmount() + amountLeftToGive < inv.getItem(slot).getType().getMaxStackSize()) {
                        inv.getItem(slot).setAmount(inv.getItem(slot).getAmount() + amountLeftToGive);
                        player.updateInventory();
                        return;
                    }
                    if(inv.getItem(slot).getAmount() + amountLeftToGive > inv.getItem(slot).getType().getMaxStackSize()) {
                        int oldAmount = inv.getItem(slot).getAmount();
                        inv.getItem(slot).setAmount(inv.getItem(slot).getMaxStackSize());
                        player.updateInventory();
                        amountLeftToGive -= (inv.getItem(slot).getMaxStackSize() - oldAmount);
                    }
                }
            }
        }
        //if still items to give
        if(amountLeftToGive > 0) {
            item.setAmount(amountLeftToGive);
            if(player.getInventory().firstEmpty() != -1) {
                player.getInventory().setItem(player.getInventory().firstEmpty(), item);
                return;
            }
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
        }
    }

    /**
     *
     * @param player - player to give item to
     * @param item - item to add
     * @return - true if no slots were found
     */
    public static boolean setItemAtFirstSlot(Player player, ItemStack item) {
        Inventory inv = player.getInventory();
        for(int i = 0; i <= 35; i++) {
            if(inv.getItem(i).isSimilar(item)) { //if they already have one in inv
                item.setAmount(item.getAmount() + 1);
                inv.setItem(i, item);
                return false;
            }
        }
        int firstEmptySlot = getFirstEmptySlot(player);
        if(firstEmptySlot != -1) { //if they didnt already have one in inv
            inv.setItem(firstEmptySlot, item);
            return false;
        }
        return true;
    }

    public static double getDistanceBetween(Player player1, Player player2) {
        return player1.getLocation().distance(player2.getLocation());
    }

}
