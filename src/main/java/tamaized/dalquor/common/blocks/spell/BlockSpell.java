package tamaized.dalquor.common.blocks.spell;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tamaized.tammodized.common.blocks.TamBlockContainer;
import tamaized.dalquor.common.blocks.spell.tileentity.TileEntitySpell;

public abstract class BlockSpell extends TamBlockContainer {

	public BlockSpell(CreativeTabs tab, Material material, String n, float hardness) {
		super(tab, material, n, hardness, SoundType.GLASS);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return createNewTileEntitySpell(worldIn, meta);
	}

	protected abstract TileEntitySpell createNewTileEntitySpell(World world, int meta);

}
