package me.poilet66.customitems.Utils;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import jdk.nashorn.internal.ir.Block;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

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

    public final static Set<Player> getPlayersInRadius(Player origin) {
        Set<Player> ret = new HashSet<>();
        for(Entity entity : origin.getNearbyEntities(50, 50, 50)) {
            if(entity instanceof Player) {
                ret.add((Player) entity);
            }
        }
        ret.add(origin);
        return ret;
    }

    public static Color mapDistanceToColor(final double input, final double maxDistance) {

        double percentOfMax = (input / maxDistance);
        if(percentOfMax > 1D) {
            percentOfMax = 1D;
        }

        int redValue;
        int greenValue;

        if(percentOfMax < 0.5D) {
            greenValue = 255;
            redValue = (int) (255 * (percentOfMax * 2));
        } else {
            greenValue = (int) (255 * ((1D - percentOfMax) / 0.5D));
            redValue = 255;
        }

        return Color.fromRGB(redValue, greenValue, 0);
    }

    public static void drawLineBetweenTwoPlayers(Player player1, Player player2, double maxDistance) {
        Vector differenceVector = player1.getLocation().toVector().subtract(player2.getLocation().toVector());
        int step = 10;
        Vector stepVector = new Vector(differenceVector.getX() / step, differenceVector.getY() / step, differenceVector.getZ() / step);
        Vector previousVector = player2.getLocation().add(0D, 0.5D, 0D).toVector();
        for(int i = 0; i <= step; i++) {
            Bukkit.getWorld(player2.getWorld().getName()).spawnParticle(Particle.REDSTONE, previousVector.toLocation(player2.getWorld()), 0, 0,0, 0, 0, new Particle.DustOptions(mapDistanceToColor(Math.abs(player2.getLocation().distance(player1.getLocation())), maxDistance), 1));
            previousVector.add(stepVector);
        }

    }

    /*public static boolean canBothPlayersPVP(Player player1, Player player2) { //TODO: Worldguard pvp support
        Location player1Loc = player1.getLocation();
        Location player2Loc = player2.getLocation();

        if(!player1.getWorld().equals(player2.getWorld())) {
            return false;
        }
        if(canPvp(player1) && canPvp(player2)) {
            return true;
        }
        return false;
    }

    public static boolean canPvp(Player player) {
        Location playerLoc = player.getLocation();
        if(Bukkit.getPluginManager().getPlugin("WorldGuard") == null) { //if worldguard not installed
            return true;
        }
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager rm = regionContainer.get((World) player.getWorld());
        ApplicableRegionSet set = rm.getApplicableRegions(BlockVector3.at(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ()));
        for(ProtectedRegion region : set) {
            if(region != null) {
                if(region.getFlag(Flags.PVP) == StateFlag.State.DENY) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }*/
}
