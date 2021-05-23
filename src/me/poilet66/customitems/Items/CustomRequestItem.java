package me.poilet66.customitems.Items;

import me.poilet66.customitems.Objects.RequestInvite;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class CustomRequestItem extends CustomItemBase{

    private final Integer inviteLength;

    public CustomRequestItem(String ID) {
        super(ID, CustomItemType.REQUESTABLE);
        this.inviteLength = main.getConfig().getInt("InviteLength", 15);
    }

    public CustomRequestItem(String ID, ItemStack item) {
        super(ID, item, CustomItemType.REQUESTABLE);
        this.inviteLength = main.getConfig().getInt("InviteLength", 15);
    }

    public abstract void onInteract(PlayerInteractAtEntityEvent event);

    public abstract void onAccept(UUID sender, UUID receiver);

    protected void addRequest(Player sender, Player recipient) { //TODO: Add custom name parameter/field
        UUID senderUUID = sender.getUniqueId();
        UUID recipientUUID = recipient.getUniqueId();

        RequestInvite invite = new RequestInvite(senderUUID, recipientUUID, this);
        main.getIRM().addInvite(invite, inviteLength);

        sender.sendMessage(String.format(ChatColor.GREEN + "Sent request to %s", recipient.getDisplayName()));

        TextComponent recipientMessage = new TextComponent(String.format("%s sent you a request ", sender.getDisplayName()));
        recipientMessage.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
        TextComponent acceptButton = new TextComponent("[Accept]");
        acceptButton.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        acceptButton.setBold(true);
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/ciinvite accept %s", sender.getName())));
        TextComponent denyButton = new TextComponent("[Deny]");
        denyButton.setColor(net.md_5.bungee.api.ChatColor.RED);
        denyButton.setBold(true);
        denyButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/ciinvite deny %s", sender.getName())));
        recipientMessage.addExtra(acceptButton);
        recipientMessage.addExtra(new TextComponent(" "));
        recipientMessage.addExtra(denyButton);

        recipient.spigot().sendMessage(recipientMessage);
    }

    //TODO: write private boolean getResponse

}
