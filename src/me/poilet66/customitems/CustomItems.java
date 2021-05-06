package me.poilet66.customitems;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import me.poilet66.customitems.Command.GiveItemCommand;
import me.poilet66.customitems.Command.GiveItemTabCompleter;
import me.poilet66.customitems.Listener.ItemUseListener;
import me.poilet66.customitems.PAPI.CustomItemsExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class CustomItems extends JavaPlugin {

    private ItemRegister IR;
    private CooldownManager CM;
    private PlayerParticlesAPI ppAPI;

    @Override
    public void onEnable() {
        loadConfig();
        this.IR = new ItemRegister(this);
        this.CM = new CooldownManager(this);
        getServer().getPluginManager().registerEvents(new ItemUseListener(this), this);
        getCommand("giveitem").setExecutor(new GiveItemCommand(this));
        getCommand("giveitem").setTabCompleter(new GiveItemTabCompleter(this));
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new CustomItemsExpansion(this).register();
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlayerParticles")) {
            this.ppAPI = PlayerParticlesAPI.getInstance();
        }
        getLogger().info("Enabled.");
    }

    @Override
    public void onDisable() {

    }

    public CooldownManager getCM() {
        return this.CM;
    }

    public ItemRegister getIR() {
        return this.IR;
    }

    public PlayerParticlesAPI getPpAPI() {
        return this.ppAPI;
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }
}
