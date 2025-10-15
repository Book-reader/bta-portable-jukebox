package bookreader.portable_jukebox.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import bookreader.portable_jukebox.gui.menu.MenuPortableJukebox;
import bookreader.portable_jukebox.iface.DisplayPortableJukeboxScreen;
import bookreader.portable_jukebox.packet.OpenGuiPacketS2C;
import net.minecraft.core.crafting.ContainerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import turniplabs.halplibe.helper.network.NetworkHandler;

@Mixin(value = PlayerServer.class, remap = false)
public abstract class PlayerServerMixin extends Player implements DisplayPortableJukeboxScreen, ContainerListener {
    public PlayerServerMixin(World world)
	{
        super(world);
    }

    @Shadow
    private int currentWindowId = 0;
    @Shadow
    private void getNextWindowId() {}

    public void bta_portable_jukebox$displayPortableJukeboxScreen(ItemStack stack)
    {
        this.getNextWindowId();
        NetworkHandler.sendToPlayer(this, new OpenGuiPacketS2C(stack, this.currentWindowId));
        this.craftingInventory.onCraftGuiClosed(this);
        this.craftingInventory = new MenuPortableJukebox(this.inventory, stack);
        this.craftingInventory.containerId = this.currentWindowId;
        this.craftingInventory.addSlotListener(this);
	}
}
