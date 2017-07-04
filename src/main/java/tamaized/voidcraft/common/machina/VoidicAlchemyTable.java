package tamaized.voidcraft.common.machina;

import tamaized.tammodized.common.blocks.TamBlockContainer;
import tamaized.voidcraft.common.gui.GuiHandler;
import tamaized.voidcraft.VoidCraft;
import tamaized.voidcraft.common.machina.tileentity.TileEntityVoidicAlchemy;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class VoidicAlchemyTable extends TamBlockContainer {

	public VoidicAlchemyTable(CreativeTabs tab, Material material, String n, float hardness) {
		super(tab, material, n, hardness, SoundType.METAL);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0F, 0F, 0F, 1F, 0.75F, 1F);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityVoidicAlchemy();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) FMLNetworkHandler.openGui(playerIn, VoidCraft.instance, GuiHandler.getTypeID(GuiHandler.Type.VoidicAlchemy), worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isTopSolid(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		for (int i = 0; i < 2; i++) {
			double d0 = (double) ((float) pos.getX() + 0.4F + rand.nextFloat() * 0.2F);
			double d1 = (double) ((float) pos.getY() + 0.0F + rand.nextFloat() * 0.3F);
			double d2 = (double) ((float) pos.getZ() + 0.4F + rand.nextFloat() * 0.2F);
			worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, 0.0D, 0.5D, 0.0D);
			worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, 0.0D, -1.5D, 0.0D);
			worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, 1.0D, 0.5D, 0.0D);
			worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, -1.0D, 0.5D, 0.0D);
			worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, 0.0D, 0.5D, 1.0D);
			worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, 0.0D, 0.5D, -1.0D);
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityVoidicAlchemy) {
			((TileEntityVoidicAlchemy) tileentity).dropInventoryItems(worldIn, pos);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}
	
}