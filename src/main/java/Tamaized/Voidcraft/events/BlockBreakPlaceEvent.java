package Tamaized.Voidcraft.events;

import Tamaized.Voidcraft.voidCraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockBreakPlaceEvent {

	@SubscribeEvent
	public void BreakBlockEvent(BlockEvent.BreakEvent e) {
		EntityPlayer player = e.getPlayer();
		World world = player.worldObj;
		if (world.provider.getDimension() == voidCraft.config.getDimensionIDxia() && !player.capabilities.isCreativeMode && (e.getState().getBlock() != Blocks.STANDING_SIGN && e.getState().getBlock() != Blocks.WALL_SIGN)) {
			e.setCanceled(true);
			if(player instanceof EntityPlayerMP) ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
		}
	}

	@SubscribeEvent
	public void BreakBlockEvent(BlockEvent.PlaceEvent e) {
		EntityPlayer player = e.getPlayer();
		World world = player.worldObj;
		if (world.provider.getDimension() == voidCraft.config.getDimensionIDxia() && !player.capabilities.isCreativeMode && (e.getState().getBlock() != Blocks.STANDING_SIGN && e.getState().getBlock() != Blocks.WALL_SIGN)) {
			e.setCanceled(true);
			if(player instanceof EntityPlayerMP) ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
		}
	}

}
