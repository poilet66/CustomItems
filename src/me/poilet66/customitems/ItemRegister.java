package me.poilet66.customitems;

import me.poilet66.customitems.Items.CustomAttackItem;
import me.poilet66.customitems.Items.CustomItemBase;
import me.poilet66.customitems.Items.CustomProjectileItem;
import me.poilet66.customitems.Objects.CustomAbilityEvent;
import me.poilet66.customitems.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemRegister {

    private final CustomItems main;

    public HashMap<ItemStack, CustomItemBase> itemMap = new HashMap<>(); //maybe change to an arraylist of ItemAbstract?
    public HashMap<String, CustomItemBase> newItemMap = new HashMap<>();

    public static final NamespacedKey itemIDKey = new NamespacedKey(Bukkit.getPluginManager().getPlugin("CustomItems"), "CustomItemID"); //TODO: HeebieJeebies

    public ItemRegister(CustomItems main) {
        this.main = main;
        registerItems();
    }

    //Add new items here
    public void registerItems() {

        //Add test ingot

        ItemStack ingotItem = new ItemStack(Material.IRON_INGOT);
        ItemMeta meta = ingotItem.getItemMeta();
        meta.getPersistentDataContainer().set(ItemRegister.itemIDKey, PersistentDataType.STRING, "INGOT");
        ingotItem.setItemMeta(meta);

        registerItem(new CustomAttackItem("INGOT", ingotItem) {

            @Override
            public void onUse(EntityDamageByEntityEvent event) {
                if(!(event.getDamager() instanceof Player)) {
                    return;
                }
                Player player = (Player) event.getDamager();
                if(isInstanceOf(player.getInventory().getItemInMainHand())) {
                    player.sendMessage(ChatColor.RED + "Wagwan babes");
                }
            }

            @Override
            public void onRegister() {

            }
        });

        registerItem(new CustomProjectileItem("SNOWBALL") { //TODO: amount of hits, enchant glint

            private int maxDistance; //default config value will be used if no value is set
            private Long cooldown;

            private void getMaxDistanceFromConfig() {
                if(main.getConfig().getInt(this.getID() + ".MaxDistance") == 0) { //if a cooldown has been set in config
                    return;
                }
                this.maxDistance = main.getConfig().getInt(this.getID() + ".MaxDistance");
            }

            private void getCooldownFromConfig() {
                if(main.getConfig().getInt(this.getID() + ".Cooldown") == 0) { //if a cooldown has been set in config
                    return;
                }
                this.cooldown = main.getConfig().getLong(this.getID() + ".Cooldown");
            }

            @Override
            public void onUse(ProjectileHitEvent event) {
                //if not snowball
                if(!(event.getEntity() instanceof Snowball)) {
                    return;
                }
                Snowball projectile = (Snowball) event.getEntity();
                //if not a custom snowball
                if(!(this.isInstanceOf(projectile.getItem())))  {
                    return;
                }
                //if not thrown by player
                if(!(projectile.getShooter() instanceof Player)) {
                    return;
                }
                Player shooter = (Player) projectile.getShooter();
                //if didnt hit a player
                if(event.getHitEntity() == null) {
                    refundItem(shooter);
                    return;
                }
                //if it hit an entity that wasnt a player
                if(!(event.getHitEntity() instanceof Player)) {
                    ((Player) projectile.getShooter()).sendMessage(ChatColor.RED + "You can't switch places with that.");
                    refundItem(shooter);
                    return;
                }
                Player victim = (Player) event.getHitEntity();
                //If not on cooldown
                if(main.getCM().hasCooldown(shooter)) {
                    float expireTimeLeft = (main.getCM().getCooldownExpireTime(shooter) - System.currentTimeMillis()) / 1000f;
                    shooter.sendMessage(String.format(ChatColor.RED + "You are still on ability item cooldown for %s%.1f%s seconds", ChatColor.YELLOW, expireTimeLeft, ChatColor.RED));
                    refundItem(shooter);
                    return;
                }
                //If in range
                if(Math.abs(Utils.getDistanceBetween(shooter, victim)) > maxDistance && maxDistance != -1) {
                    shooter.sendMessage(String.format(ChatColor.RED + "You were too far away, max distance is %s and you were %.1f blocks away", maxDistance, Utils.getDistanceBetween(shooter, victim)));
                    refundItem(shooter);
                    return;
                }
                //Call ability event
                CustomAbilityEvent customItemEvent = new CustomAbilityEvent(shooter, this);
                Bukkit.getPluginManager().callEvent(customItemEvent);
                if(customItemEvent.isCancelled()) {
                    return;
                }
                //Do the thing
                shooter.sendMessage(String.format(ChatColor.GREEN + "You switched places with %s", victim.getDisplayName()));
                victim.sendMessage(ChatColor.YELLOW + "Someone switched places with you!");
                Utils.switchPosition(shooter, victim); //TODO: Give both players particle effect (Random Tube Around Player)
                //Add cooldown if not infinite
                if(cooldown != -1L) {
                    main.getCM().addPlayerCooldown(shooter, cooldown * 1000L);
                }
            }

            @Override
            public void onHit(EntityDamageByEntityEvent event) {
                if(!(event.getDamager() instanceof Snowball)) {
                    return;
                }
                Snowball snowball = (Snowball) event.getDamager();
                if(isInstanceOf(snowball.getItem())) {
                    event.setCancelled(true);
                }
            }

            @Override
            public void onRegister() {
                getMaxDistanceFromConfig();
                getCooldownFromConfig();
            }
        });

        //Add test ingot

        ItemStack barItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta barmeta = barItem.getItemMeta();
        barmeta.getPersistentDataContainer().set(ItemRegister.itemIDKey, PersistentDataType.STRING, "BAR");
        barItem.setItemMeta(barmeta);

        registerItem(new CustomAttackItem("BAR", barItem) {

            @Override
            public void onUse(EntityDamageByEntityEvent event) {
                if(!(event.getDamager() instanceof Player)) {
                    return;
                }
                Player player = (Player) event.getDamager();
                if(isInstanceOf(player.getInventory().getItemInMainHand())) {
                    player.sendMessage(ChatColor.RED + "Wagwan babes");
                }
            }

            @Override
            public void onRegister() {

            }
        });


    }

    private void registerItem(CustomItemBase customItem) {
        newItemMap.putIfAbsent(customItem.getID(), customItem);
        //newItemMap.put(customItem.getID(), customItem);
        Bukkit.getLogger().info(String.format("adding %s : %s to newItemMap", customItem.getID(), customItem.getItem().getType()));
        //itemMap.put(customItem.getItem(), customItem);
        itemMap.putIfAbsent(customItem.getItem(), customItem);
        Bukkit.getLogger().info(String.format("adding %s : %s to itemMap", customItem.getID(), customItem.getItem().getType()));
    }

}
