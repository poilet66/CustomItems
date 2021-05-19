package me.poilet66.customitems;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.particles.FixedParticleEffect;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.styles.ParticleStyle;
import me.poilet66.customitems.Items.CustomAttackItem;
import me.poilet66.customitems.Items.CustomItemBase;
import me.poilet66.customitems.Items.CustomProjectileItem;
import me.poilet66.customitems.API.CustomAbilityEvent;
import me.poilet66.customitems.Utils.Utils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemFlag;
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
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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

        registerItem(new CustomProjectileItem("SWITCHER") { //TODO: amount of hits

            private int maxDistance; //default config value will be used if no value is set

            private void getMaxDistanceFromConfig() {
                if(main.getConfig().getInt(this.getID() + ".MaxDistance") == 0) { //if a cooldown has been set in config
                    return;
                }
                this.maxDistance = main.getConfig().getInt(this.getID() + ".MaxDistance");
            }

            @Override
            public void onUse(ProjectileHitEvent event) {
                //if not snowball
                if(!(event.getEntity() instanceof Snowball)) {
                    return;
                }
                Snowball projectile = (Snowball) event.getEntity();
                //if not thrown by player
                if(!(projectile.getShooter() instanceof Player)) {
                    return;
                }
                Player shooter = (Player) projectile.getShooter();
                //if not a custom snowball
                if(!(this.isInstanceOf(projectile.getItem())))  {
                    return;
                }
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
                //If not in range
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
                Utils.switchPosition(shooter, victim);

                if(Bukkit.getPluginManager().isPluginEnabled("PlayerParticles")) {
                    PlayerParticlesAPI ppAPI = PlayerParticlesAPI.getInstance();
                    ppAPI.addActivePlayerParticle(victim, ParticleEffect.WITCH, DefaultStyles.SPHERE);
                    ppAPI.addActivePlayerParticle(shooter, ParticleEffect.WITCH, DefaultStyles.SPHERE);

                    Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                        @Override
                        public void run() {
                            ppAPI.removeActivePlayerParticles(victim, ParticleEffect.WITCH);
                            ppAPI.removeActivePlayerParticles(shooter, ParticleEffect.WITCH);
                        }
                    }, 10L);
                }
            }

            @Override
            public void onHit(EntityDamageByEntityEvent event) {
                if(!(event.getDamager() instanceof Snowball)) {
                    return;
                }
                Snowball snowball = (Snowball) event.getDamager();
                if(!(snowball.getShooter() instanceof  Player)) {
                    return;
                }
                Player player = (Player) snowball.getShooter();
                if(isInstanceOf(snowball.getItem()) && main.getCM().hasCooldown(player)) {
                    event.setCancelled(true);
                }
            }

            @Override
            public void onThrow(ProjectileLaunchEvent event) {
                if(!(event.getEntity() instanceof Snowball)) {
                    return;
                }
                Snowball snowball = (Snowball) event.getEntity();
                if(!(snowball.getShooter() instanceof Player)) {
                    return;
                }
                Player shooter = (Player) snowball.getShooter();
                if(!isInstanceOf(snowball.getItem())) {
                    return;
                }
                if(main.getCM().hasCooldown(shooter)) {
                    float expireTimeLeft = (main.getCM().getCooldownExpireTime(shooter) - System.currentTimeMillis()) / 1000f;
                    shooter.sendMessage(String.format(ChatColor.RED + "You are still on ability item cooldown for %s%.1f%s seconds", ChatColor.YELLOW, expireTimeLeft, ChatColor.RED));
                    event.setCancelled(true);
                    return;
                }
                //Add cooldown if not infinite
                if(this.cooldown != -1L) {
                    main.getCM().addPlayerCooldown(shooter, cooldown * 1000L);
                }
            }

            @Override
            public void onRegister() {
                getMaxDistanceFromConfig();
                getCooldownFromConfig();
            }
        });

        //Add timewarp pearl
        registerItem(new CustomProjectileItem("TIMEWARP_PEARL") {

            private int warpDelay;
            private HashMap<UUID, Location> playerWarpLocations = new HashMap<>();

            private void getWarpDelayFromConfig() {
                if(main.getConfig().getInt(this.getID() + ".WarpDelay") == 0) { //if a cooldown has been set in config
                    return;
                }
                this.warpDelay = main.getConfig().getInt(this.getID() + ".WarpDelay");
            }

            @Override
            public void onUse(ProjectileHitEvent event) {

            }

            @Override
            public void onHit(EntityDamageByEntityEvent event) {

            }

            @Override
            public void onThrow(ProjectileLaunchEvent event) {
                if(!(event.getEntity() instanceof EnderPearl)) {
                    return;
                }
                EnderPearl pearl = (EnderPearl) event.getEntity();
                if(!(pearl.getShooter() instanceof Player)) {
                    return;
                }
                Player shooter = (Player) pearl.getShooter();
                if(!isInstanceOf(pearl.getItem())) {
                    return;
                }
                if(main.getCM().hasCooldown(shooter)) {
                    float expireTimeLeft = (main.getCM().getCooldownExpireTime(shooter) - System.currentTimeMillis()) / 1000f;
                    shooter.sendMessage(String.format(ChatColor.RED + "You are still on ability item cooldown for %s%.1f%s seconds", ChatColor.YELLOW, expireTimeLeft, ChatColor.RED));
                    event.setCancelled(true);
                    return;
                }
                //Add cooldown if not infinite
                if(this.cooldown != -1L) {
                    main.getCM().addPlayerCooldown(shooter, cooldown * 1000L);
                }
                this.playerWarpLocations.put(shooter.getUniqueId(), shooter.getLocation());
                int beamID = 0;
                if(Bukkit.getPluginManager().isPluginEnabled("PlayerParticles")) {
                    PlayerParticlesAPI ppAPI = PlayerParticlesAPI.getInstance();
                    ppAPI.addActivePlayerParticle(shooter, ParticleEffect.CRIMSON_SPORE, DefaultStyles.NORMAL);
                    beamID = ppAPI.createFixedParticleEffect(shooter, shooter.getLocation(),  new ParticlePair(shooter.getUniqueId(), ppAPI.getPPlayer(shooter).getFixedParticles().size(), ParticleEffect.REVERSE_PORTAL, DefaultStyles.BEAM, null, null, null, null)).getId();
                }
                shooter.sendMessage(String.format("%sYou will be warped back to your original location in %s%s %sseconds", ChatColor.GREEN, ChatColor.YELLOW, warpDelay, ChatColor.GREEN));
                int finalBeamID = beamID;
                Bukkit.getScheduler().runTaskLater(main, () -> {
                    if(playerWarpLocations.get(shooter.getUniqueId()) == null) {
                        Bukkit.getLogger().info(String.format("Warp location for %s was null", shooter.getDisplayName()));
                        refundItem(shooter);
                        return;
                    }
                    CustomAbilityEvent customItemEvent = new CustomAbilityEvent(shooter, this);
                    Bukkit.getPluginManager().callEvent(customItemEvent);
                    if(customItemEvent.isCancelled()) {
                        return;
                    }
                    shooter.teleport(playerWarpLocations.get(shooter.getUniqueId())); //TODO: Play sound effect
                    playerWarpLocations.remove(shooter.getUniqueId());
                    shooter.sendMessage(ChatColor.GREEN + "You were warped back to your original location.");
                    if(Bukkit.getPluginManager().isPluginEnabled("PlayerParticles")) {
                        PlayerParticlesAPI ppAPI = PlayerParticlesAPI.getInstance();
                        ppAPI.addActivePlayerParticle(shooter, ParticleEffect.WITCH, DefaultStyles.SPHERE);
                        ppAPI.removeFixedEffect(shooter, finalBeamID);

                        Bukkit.getScheduler().runTaskLater(main, () -> {
                            ppAPI.removeActivePlayerParticles(shooter, DefaultStyles.SPHERE);
                            ppAPI.removeActivePlayerParticles(shooter, ParticleEffect.CRIMSON_SPORE);
                        }, 10L);
                    }
                }, warpDelay * 20L);
            }

            @Override
            public void onRegister() {
                getWarpDelayFromConfig();
                getCooldownFromConfig();
            }
        });


    }

    private void registerItem(CustomItemBase customItem) {
        newItemMap.put(customItem.getID(), customItem);
        itemMap.put(customItem.getItem(), customItem);
    }

}
