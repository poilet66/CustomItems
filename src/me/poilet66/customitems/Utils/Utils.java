package me.poilet66.customitems.Utils;

import org.bukkit.Location;
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
