package me.poilet66.customitems.Objects;

import me.poilet66.customitems.Items.CustomRequestItem;

import java.util.UUID;

public class RequestInvite {

    private UUID inviteSender, inviteReceiver;
    private CustomRequestItem item;

    public RequestInvite(UUID inviteSender, UUID inviteReceiver, CustomRequestItem item) {
        this.inviteReceiver = inviteReceiver;
        this.inviteSender = inviteSender;
        this.item = item;
    }

    public UUID getSender() {
        return this.inviteSender;
    }

    public UUID getReceiver() {
        return this.inviteReceiver;
    }

    public CustomRequestItem getItem() {
        return this.item;
    }
}
