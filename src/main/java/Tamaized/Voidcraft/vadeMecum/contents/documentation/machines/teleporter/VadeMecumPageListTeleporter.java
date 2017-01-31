package Tamaized.Voidcraft.vadeMecum.contents.documentation.machines.teleporter;

import Tamaized.Voidcraft.VoidCraft;
import Tamaized.Voidcraft.capabilities.vadeMecum.IVadeMecumCapability;
import Tamaized.Voidcraft.vadeMecum.IVadeMecumPage;
import Tamaized.Voidcraft.vadeMecum.IVadeMecumPageProvider;
import Tamaized.Voidcraft.vadeMecum.VadeMecumCraftingNormal;
import Tamaized.Voidcraft.vadeMecum.VadeMecumPage;
import Tamaized.Voidcraft.vadeMecum.VadeMecumPageCrafting;
import net.minecraft.item.ItemStack;

public class VadeMecumPageListTeleporter implements IVadeMecumPageProvider {

	public IVadeMecumPage[] getPageList(IVadeMecumCapability cap) {
		return new IVadeMecumPage[] {
				new VadeMecumPage(new ItemStack(VoidCraft.blocks.realityTeleporterBlock).getDisplayName(), VoidCraft.modid+".VadeMecum.docs.desc.realityTeleporterBlock.pg1"),
				new VadeMecumPage("", VoidCraft.modid+".VadeMecum.docs.desc.realityTeleporterBlock.pg2"),
				new VadeMecumPage("", VoidCraft.modid+".VadeMecum.docs.desc.realityTeleporterBlock.pg3"),
				new VadeMecumPage("", VoidCraft.modid+".VadeMecum.docs.desc.realityTeleporterBlock.pg4"),
				new VadeMecumPageCrafting(new VadeMecumCraftingNormal(VoidCraft.modid+".VadeMecum.recipe.normal", new ItemStack[] {
						new ItemStack(VoidCraft.blocks.blockVoidbrick),
						new ItemStack(VoidCraft.items.emeraldDust),
						new ItemStack(VoidCraft.blocks.blockVoidbrick),
						new ItemStack(VoidCraft.items.voidCloth),
						new ItemStack(VoidCraft.blocks.realityHole),
						new ItemStack(VoidCraft.items.voidCloth),
						new ItemStack(VoidCraft.blocks.blockVoidbrick),
						new ItemStack(VoidCraft.blocks.voidicCharger),
						new ItemStack(VoidCraft.blocks.blockVoidbrick) }, new ItemStack(VoidCraft.blocks.realityTeleporterBlock, 1))) };
	}

}
