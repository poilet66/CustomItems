package me.poilet66.customitems;

import me.poilet66.customitems.Command.GiveItemCommand;
import me.poilet66.customitems.Listener.ItemUseListener;
import me.poilet66.customitems.PAPI.CustomItemsExpansion;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class CustomItems extends JavaPlugin {

    private ItemRegister IR;
    private CooldownManager CM;

    @Override
    public void onEnable() {
        loadConfig();
        this.IR = new ItemRegister(this);
        this.CM = new CooldownManager(this);
        getServer().getPluginManager().registerEvents(new ItemUseListener(this), this);
        getCommand("giveitem").setExecutor(new GiveItemCommand(this));
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

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }
}
