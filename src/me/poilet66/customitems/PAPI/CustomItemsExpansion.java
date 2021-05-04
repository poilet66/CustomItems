package me.poilet66.customitems.PAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.poilet66.customitems.CustomItems;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CustomItemsExpansion extends PlaceholderExpansion {

    private CustomItems plugin;

    public CustomItemsExpansion(CustomItems main) {
        this.plugin = main;
    }

    @Override
    public boolean canRegister() {
        return plugin != null;
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return "poilet66";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifer) {
        if(player == null) {
            return "";
        }
        if(identifer.equals("cooldown")) {
            if(plugin.getCM().hasCooldown(player)) {
                float timeLeft = (plugin.getCM().getCooldownExpireTime(player) - System.currentTimeMillis()) / 1000f;
                return Float.toString(timeLeft);
            }
            return "0.000";
        }
        return null;
    }
}
