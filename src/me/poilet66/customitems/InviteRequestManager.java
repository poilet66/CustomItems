package me.poilet66.customitems;

import me.poilet66.customitems.Objects.RequestInvite;
import org.bukkit.Bukkit;

import java.util.*;

public class InviteRequestManager {

    private final CustomItems main;

    private HashMap<RequestInvite, Long> inviteMap = new HashMap<>();

    public InviteRequestManager(CustomItems main) {
        this.main = main;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
            validateInvites();
        }, 0L, 20L);
    }

    public void addInvite(RequestInvite newInvite, int amount) {
        for(RequestInvite oldInvites : inviteMap.keySet()) {
            if(oldInvites.getSender().equals(newInvite.getSender()) && oldInvites.getReceiver().equals(newInvite.getReceiver())) { //if old sender/receiver
                if(!oldInvites.getItem().equals(newInvite.getItem())) {
                    inviteMap.remove(oldInvites);
                    inviteMap.put(newInvite, System.currentTimeMillis() + (amount * 1000L)); //update item if different
                    return;
                }
            }
        }
        //if no sender/receiver
        inviteMap.put(newInvite, System.currentTimeMillis() + (amount * 1000L));
    }

    public void removeInvite(RequestInvite invite) {
        if(inviteMap.containsKey(invite)) {
            inviteMap.remove(invite);
        }
    }

    public boolean inviteExpired(RequestInvite invite) {
        if(!this.inviteMap.keySet().contains(invite)) return true; //no cooldowns

        long expireTime = inviteMap.get(invite);
        if(System.currentTimeMillis() < expireTime) return false; //cooldown not expired yet

        return true;
    }

    private void validateInvites() {
        if(inviteMap.keySet().isEmpty()) {
            return;
        }
        Iterator<RequestInvite> it = inviteMap.keySet().iterator();
        while(it.hasNext()) {
            RequestInvite invite = it.next();
            if(inviteExpired(invite)) {
                inviteMap.remove(invite);
            }
        }
    }

    public Set<RequestInvite> getInviteList() {
        return this.inviteMap.keySet();
    }
}