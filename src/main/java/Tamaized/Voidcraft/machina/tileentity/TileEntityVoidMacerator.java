package Tamaized.Voidcraft.machina.tileentity;

import Tamaized.Voidcraft.voidCraft;
import Tamaized.Voidcraft.api.voidicpower.TileEntityVoidicPowerInventory;
import Tamaized.Voidcraft.machina.VoidMacerator;
import Tamaized.Voidcraft.machina.addons.TERecipesMacerator.MaceratorRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityVoidMacerator extends TileEntityVoidicPowerInventory {

	public static final int SLOT_INPUT = 0;
	public static final int SLOT_OUTPUT = 1;
	public static final int[] SLOTS_ALL = new int[] { 0, 1 };

	public int cookingTick = 0;
	public int finishTick = 0;
	private MaceratorRecipe recipe;

	private Item lastCookingItem = null;

	public TileEntityVoidMacerator() {
		super(2);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return (oldState.getBlock() != newSate.getBlock());
	}

	public void readNBT(NBTTagCompound nbt) {
		cookingTick = nbt.getInteger("cookingTick");
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setInteger("cookingTick", cookingTick);
		return nbt;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void onUpdate() {
		boolean cooking = false;
		if (lastCookingItem == null || getStackInSlot(SLOT_INPUT) == null || lastCookingItem != getStackInSlot(SLOT_INPUT).getItem()) {
			cookingTick = 0;
			lastCookingItem = (getStackInSlot(SLOT_INPUT) != null) ? getStackInSlot(SLOT_INPUT).getItem() : null;
		}

		if (voidicPower > 0 && canCook()) {
			cooking = true;
			voidicPower--;
		}

		if (!worldObj.isRemote) {
			if (cooking) {
				cookingTick++;
				if (cookingTick >= (finishTick = recipe.getRequiredPower())) {
					cookingTick = 0;
					bakeItem();
					markDirty();
				}
			}

			IBlockState state = worldObj.getBlockState(pos);
			if (state.getBlock() instanceof VoidMacerator) {
				VoidMacerator theMacerator = (VoidMacerator) state.getBlock();
				if (theMacerator != null) {
					if (theMacerator.getIsActive(state) && !cooking) theMacerator.setState(false, worldObj, pos);
					if (!theMacerator.getIsActive(state) && cooking) theMacerator.setState(true, worldObj, pos);
				}
			}
		}
	}

	private void bakeItem() {
		if (canCook()) {
			if (getStackInSlot(SLOT_OUTPUT) == null) {
				setInventorySlotContents(SLOT_OUTPUT, recipe.getOutput().copy());
			} else if (getStackInSlot(SLOT_OUTPUT).isItemEqual(recipe.getOutput())) {
				getStackInSlot(SLOT_OUTPUT).stackSize += recipe.getOutput().stackSize;
			}

			getStackInSlot(SLOT_INPUT).stackSize--;

			if (getStackInSlot(SLOT_INPUT).stackSize <= 0) {
				setInventorySlotContents(SLOT_INPUT, null);
			}
		}
	}

	private boolean canCook() {
		if (getStackInSlot(SLOT_INPUT) == null) return false;
		recipe = voidCraft.teRecipes.macerator.getRecipe(new ItemStack[] { getStackInSlot(SLOT_INPUT) });
		if (recipe == null) return false;
		if (getStackInSlot(SLOT_OUTPUT) == null) return true;
		if (!getStackInSlot(SLOT_OUTPUT).isItemEqual(recipe.getOutput())) return false;
		int result = getStackInSlot(SLOT_OUTPUT).stackSize + recipe.getOutput().stackSize;
		return (result <= getInventoryStackLimit() && result <= recipe.getOutput().getMaxStackSize());
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return i != SLOT_OUTPUT;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return SLOTS_ALL;
	}

	@Override
	public String getName() {
		return "teVoidMacerator";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getMaxPower() {
		return 50000;
	}

	@Override
	public int maxPowerTransfer() {
		return 160;
	}

	@Override
	public boolean canOutputPower(EnumFacing face) {
		return false;
	}

	@Override
	public boolean canInputPower(EnumFacing face) {
		return true;
	}

	@Override
	protected boolean canExtractSlot(int i, ItemStack stack) {
		return i == SLOT_OUTPUT;
	}

	@Override
	protected boolean canInsertSlot(int i, ItemStack stack) {
		return i == SLOT_INPUT;
	}
}
