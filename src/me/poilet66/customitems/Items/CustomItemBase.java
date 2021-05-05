package me.poilet66.customitems.Items;

import me.poilet66.customitems.CooldownManager;
import me.poilet66.customitems.CustomItems;
import me.poilet66.customitems.ItemRegister;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class CustomItemBase {

    private final CustomItems main = (CustomItems) Bukkit.getPluginManager().getPlugin("CustomItems"); //TODO: HeebieJeebies

    private ItemStack item;
    private CustomItemType type;
    private String customID;
    private boolean enchantGlint;

    //Possible parameters
    private int cooldownLength;

    public CustomItemBase(String ID, ItemStack item, CustomItemType type) {
        this.type = type;
        this.item = item;
        this.customID = ID.toUpperCase();
        onRegister();
    }

    public CustomItemBase(String ID, CustomItemType type) {
        this.type = type;
        this.customID = ID.toUpperCase();
        this.item = generateItemFromConfig(ID);
        onRegister();
    }

    /**
     * Called on creation of the new item, used so functions can be ran on registry
     */
    public abstract void onRegister();

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public boolean isInstanceOf(ItemStack item) {
        if(!item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if(!meta.getPersistentDataContainer().has(ItemRegister.itemIDKey, PersistentDataType.STRING)) {
            return false;
        }
        if(meta.getPersistentDataContainer().get(ItemRegister.itemIDKey, PersistentDataType.STRING).equals(this.customID)) {
            return true;
        }
        return false;
    }

    public String getID() {
        return this.customID;
    }

    private boolean getEnchantGlintFromConfig(String itemID) {
        FileConfiguration config = main.getConfig();
        return config.getBoolean(itemID + ".Enchanted", false);
    }

    private ItemStack generateItemFromConfig(String itemID) {
        FileConfiguration config = main.getConfig();
        if(config.get(itemID) == null) {
            return null;
        }
        try {
            ItemStack ret = new ItemStack(Objects.requireNonNull(Material.matchMaterial(config.getString(itemID + ".Material"))));
            ItemMeta meta = ret.getItemMeta();
            if(config.getString(itemID + ".DisplayName") != null) {
                //meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(itemID + ".DisplayName")));
                meta.setDisplayName(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', config.getString(itemID + ".DisplayName")));
            }
            if(config.getStringList(itemID + ".Lore") != null) {
                List<String> formattedLore = new ArrayList<>();
                for(String line : config.getStringList(itemID + ".Lore")) {
                    formattedLore.add(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', line));
                    //formattedLore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(formattedLore);
            }
            if(getEnchantGlintFromConfig(itemID)) {
                meta.addEnchant(Enchantment.DURABILITY, 0, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            meta.getPersistentDataContainer().set(ItemRegister.itemIDKey, PersistentDataType.STRING, itemID);
            ret.setItemMeta(meta);
            return ret;
        } catch(NullPointerException npe) {
            main.getLogger().info(String.format("Malformed config for item %s, disabling item", itemID));
            return null;
        }
    }

    protected void refundItem(Player player) {
        if(player.getInventory().getItemInMainHand().getType() != Material.AIR) { //if they didnt use last time
            if(!isInstanceOf(player.getInventory().getItemInMainHand())) {
                return;
            }
            ItemStack item = player.getInventory().getItemInMainHand();
            item.setAmount(item.getAmount() + 1);
            return;
        }
        player.getInventory().setItemInMainHand(generateItemFromConfig(this.customID));
    }

    protected void consumeItem(Player player) {
        if(!isInstanceOf(player.getInventory().getItemInMainHand())) {
            return;
        }
        if(player.getInventory().getItemInMainHand().getAmount() == 1) {
            player.getInventory().setItemInMainHand(null);
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        item.setAmount(item.getAmount() - 1);
        player.getInventory().setItemInMainHand(item);
    }

}

