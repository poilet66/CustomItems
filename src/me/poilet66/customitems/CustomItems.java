package me.poilet66.customitems;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import me.poilet66.customitems.Command.GiveItemCommand;
import me.poilet66.customitems.Command.GiveItemTabCompleter;
import me.poilet66.customitems.Command.InviteCommand;
import me.poilet66.customitems.Command.ReloadCommand;
import me.poilet66.customitems.Listener.ItemUseListener;
import me.poilet66.customitems.PAPI.CustomItemsExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class CustomItems extends JavaPlugin {

    private ItemRegister IR;
    private CooldownManager CM;
    private PlayerParticlesAPI ppAPI;
    private InviteRequestManager IRM;

    @Override
    public void onEnable() {
        loadConfig();
        this.IR = new ItemRegister(this);
        this.CM = new CooldownManager(this);
        this.IRM = new InviteRequestManager(this);
        getServer().getPluginManager().registerEvents(new ItemUseListener(this), this);
        getCommand("ciinvite").setExecutor(new InviteCommand(this));
        getCommand("giveitem").setExecutor(new GiveItemCommand(this));
        getCommand("cireload").setExecutor(new ReloadCommand(this));
        getCommand("giveitem").setTabCompleter(new GiveItemTabCompleter(this));
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new CustomItemsExpansion(this).register();
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

    public InviteRequestManager getIRM() {
        return this.IRM;
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }
}
