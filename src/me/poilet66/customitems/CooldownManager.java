package me.poilet66.customitems;

import me.poilet66.customitems.CustomItems;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    private final CustomItems main;

    private HashMap<UUID, Long> abilityCooldowns;

    public CooldownManager(CustomItems main) {
        this.main = main;
        this.abilityCooldowns = new HashMap<>();
    }

    public void addPlayerCooldown(Player player, Long amount) {
        abilityCooldowns.put(player.getUniqueId(), System.currentTimeMillis() + amount);
    }

    public boolean hasCooldown(Player player) {
        UUID playerID = player.getUniqueId();
        if(!this.abilityCooldowns.containsKey(playerID)) return false; //no cooldowns

        long expireTime = abilityCooldowns.get(playerID);
        if(System.currentTimeMillis() < expireTime) return true; //cooldown not expired yet

        abilityCooldowns.remove(playerID); //cooldown expired
        return false;
    }

    public Long getCooldownExpireTime(Player player) {
        if(!abilityCooldowns.containsKey(player.getUniqueId())) return -1L;
        return abilityCooldowns.get(player.getUniqueId());
    }

    public void removePlayerCooldown(Player player) {
        if(abilityCooldowns.containsKey(player.getUniqueId())) {
            abilityCooldowns.remove(player.getUniqueId());
        }
    }

}
