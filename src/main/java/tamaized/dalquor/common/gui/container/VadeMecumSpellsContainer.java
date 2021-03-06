package tamaized.dalquor.common.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamaized.dalquor.common.capabilities.vadeMecum.IVadeMecumCapability;
import tamaized.dalquor.common.gui.slots.SlotVadeMecumSpell;
import tamaized.dalquor.common.vademecum.progression.VadeMecumWordsOfPower;

public class VadeMecumSpellsContainer extends Container {

	private int page;
	private final InventoryPlayer inventory;
	private final IVadeMecumCapability capability;
	private int index = 0;

	public VadeMecumSpellsContainer(InventoryPlayer inv, IVadeMecumCapability cap) {
		page = cap.getPage();
		inventory = inv;
		capability = cap;
		initSlots(0, 0);
	}

	public void initSlots(int left, int top) {
		int xLoc = 143 - left;
		int yLoc = 30 - top;

		inventorySlots.clear();
		inventoryItemStacks.clear();

		for (index = 15 * page; index < (15 * (page + 1)); index++) {
			if (capability.getAvailableActivePowers().size() - 1 < index)
				break;
			IVadeMecumCapability.Category cat = capability.getAvailableActivePowers().get(index);
			addSlotToContainer(new SlotVadeMecumSpell(capability, cat, VadeMecumWordsOfPower.getCategoryData(cat).getStack().getItem(), index, xLoc + (135 * ((int) Math.floor((index - (15 * page)) / 5) - 1)), yLoc + (25 * ((index - (15 * page)) % 5) - 1)));
		}

		int xPos = 32;
		int yPos = 8;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventory, j + (i * 9) + 9, xPos + j * 18, yPos + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventory, i, xPos + i * 18, 58 + yPos));
		}

		addSlotToContainer(new Slot(inventory, inventory.getSizeInventory() - 1, xPos - 24, yPos) {
			@SideOnly(Side.CLIENT)
			@Override
			public String getSlotTexture() {
				return "minecraft:items/empty_armor_slot_shield";
			}
		});
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int hoverSlot) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot) inventorySlots.get(hoverSlot);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			final int maxSlots = (index - (15 * page));
			if (hoverSlot < maxSlots) {
				if (!mergeItemStack(itemstack1, maxSlots, maxSlots + 36, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			} else {
				if (mergeItemStack(itemstack1, 0, maxSlots, false)) {

				} else if (hoverSlot >= maxSlots && hoverSlot < maxSlots + 27) {
					if (!mergeItemStack(itemstack1, maxSlots + 27, maxSlots + 36, false)) {
						return ItemStack.EMPTY;
					}
				} else if (hoverSlot >= maxSlots + 27 && hoverSlot < maxSlots + 36) {
					if (!mergeItemStack(itemstack1, maxSlots, maxSlots + 27, false)) {
						return ItemStack.EMPTY;
					}
				} else {
					if (!mergeItemStack(itemstack1, maxSlots, maxSlots + 36, false)) {
						return ItemStack.EMPTY;
					}
				}
			}

			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
}