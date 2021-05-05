package me.poilet66.customitems.API;

import me.poilet66.customitems.Items.CustomItemBase;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomAbilityEvent extends Event implements Cancellable {

    private Player player;
    private CustomItemBase customItem;
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean isCancelled;

    public CustomAbilityEvent(Player player, CustomItemBase item) {
        this.player = player;
        this.customItem = item;
        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Player getPlayer() {
        return this.player;
    }

    public CustomItemBase getCustomItem() {
        return this.customItem;
    }

}
