package tamaized.dalquor.common.gui.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class SlotCantPlace extends SlotItemHandlerBypass { // TODO TamModized

	public SlotCantPlace(IItemHandler p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	public boolean isItemValid(ItemStack p_75214_1_) {
		return false;
	}

}
