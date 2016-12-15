package Tamaized.Voidcraft.starforge.tools;

import java.util.List;

import Tamaized.TamModized.tools.TamSpade;
import Tamaized.Voidcraft.capabilities.CapabilityList;
import Tamaized.Voidcraft.capabilities.starforge.IStarForgeCapability;
import Tamaized.Voidcraft.starforge.effects.IStarForgeEffect;
import Tamaized.Voidcraft.starforge.effects.IStarForgeEffect.Tier;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StarForgeShovel extends TamSpade {

	public StarForgeShovel(CreativeTabs tab, ToolMaterial material, String n) {
		super(tab, material, n);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new ICapabilitySerializable<NBTTagCompound>() {

			IStarForgeCapability inst = CapabilityList.STARFORGE.getDefaultInstance();

			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
				return capability == CapabilityList.STARFORGE;
			}

			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
				return capability == CapabilityList.STARFORGE ? CapabilityList.STARFORGE.<T> cast(inst) : null;
			}

			@Override
			public NBTTagCompound serializeNBT() {
				return (NBTTagCompound) CapabilityList.STARFORGE.getStorage().writeNBT(CapabilityList.STARFORGE, inst, null);
			}

			@Override
			public void deserializeNBT(NBTTagCompound nbt) {
				CapabilityList.STARFORGE.getStorage().readNBT(CapabilityList.STARFORGE, inst, null, nbt);
			}

		};
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		IStarForgeCapability cap = stack.getCapability(CapabilityList.STARFORGE, null);
		if (cap != null) {
			IStarForgeEffect t1 = cap.getEffect(Tier.ONE);
			IStarForgeEffect t2 = cap.getEffect(Tier.TWO);
			IStarForgeEffect t3 = cap.getEffect(Tier.THREE);
			tooltip.add(TextFormatting.DARK_AQUA + "Tier 1:");
			tooltip.add("    " + TextFormatting.DARK_AQUA + (t1 == null ? "Empty" : t1.getName()));
			tooltip.add(TextFormatting.DARK_PURPLE + "Tier 2:");
			tooltip.add("    " + TextFormatting.DARK_PURPLE + (t2 == null ? "Empty" : t2.getName()));
			tooltip.add(TextFormatting.DARK_RED + "Tier 3:");
			tooltip.add("    " + TextFormatting.DARK_RED + (t3 == null ? "Empty" : t3.getName()));
		}
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged ? true : (oldStack.isEmpty() || newStack.isEmpty()) ? true : oldStack.getItem() != newStack.getItem();
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		IStarForgeCapability cap = stack.getCapability(CapabilityList.STARFORGE, null);
		if (cap != null) cap.update(worldIn, stack);
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		IStarForgeCapability cap = stack.getCapability(CapabilityList.STARFORGE, null);
		return super.getStrVsBlock(stack, state) + (cap == null ? 0.0F : cap.getSpeedMod());
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		IStarForgeCapability cap = stack.getCapability(CapabilityList.STARFORGE, null);
		if (cap != null) cap.onEntityHit(attacker, target);
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			IStarForgeCapability cap = stack.getCapability(CapabilityList.STARFORGE, null);
			if (cap != null){
				RayTraceResult ray = rayTrace(worldIn, (EntityPlayer) entityLiving, false);
				cap.onBlockBreak(entityLiving, worldIn, state, pos, ray == null ? null : ray.sideHit);
			}
		}
		return true;
	}

	public EnumActionResult doItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		IStarForgeCapability cap = stack.getCapability(CapabilityList.STARFORGE, null);
		return cap == null ? EnumActionResult.PASS : cap.onRightClickBlock(playerIn, worldIn, worldIn.getBlockState(pos), pos, facing) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack stack = playerIn.getHeldItem(hand);
		IStarForgeCapability cap = stack.getCapability(CapabilityList.STARFORGE, null);
		return cap == null ? new ActionResult(EnumActionResult.PASS, stack) : cap.onRightClick(playerIn) ? new ActionResult(EnumActionResult.SUCCESS, stack) : new ActionResult(EnumActionResult.PASS, stack);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@Override
	public int getItemEnchantability() {
		return 0;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack stack) {
		return false;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = playerIn.getHeldItem(hand);

		if (!playerIn.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
			return doItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ) == EnumActionResult.PASS ? EnumActionResult.FAIL : EnumActionResult.SUCCESS;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (facing != EnumFacing.DOWN && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && block == Blocks.GRASS) {
				IBlockState iblockstate1 = Blocks.GRASS_PATH.getDefaultState();
				worldIn.playSound(playerIn, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

				if (!worldIn.isRemote) {
					worldIn.setBlockState(pos, iblockstate1, 11);
				}

				return EnumActionResult.SUCCESS;
			} else {
				return doItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
			}
		}
	}

}