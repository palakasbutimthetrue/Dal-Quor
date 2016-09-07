package Tamaized.Voidcraft.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import Tamaized.TamModized.items.TamItem;
import Tamaized.Voidcraft.entity.boss.herobrine.extra.EntityHerobrineFireball;
import Tamaized.Voidcraft.entity.ghost.EntityGhostPlayerBase;
import Tamaized.Voidcraft.handlers.SkinHandler.PlayerNameAlias;
import Tamaized.Voidcraft.world.SchematicLoader;

public class Debugger extends TamItem {

	public Debugger(CreativeTabs tab, String n, int maxStackSize) {
		super(tab, n, maxStackSize);
	}

	SchematicLoader sut = new SchematicLoader();

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			// EntityGhostPlayerBase entity = EntityGhostPlayerBase.newInstance(worldIn, PlayerNameAlias.Soaryn);
			// EntityHerobrineCreeper entity = new EntityHerobrineCreeper(worldIn);
			// entity.setPositionAndRotation(pos.getX()+0.5, pos.getY() + 1, pos.getZ()+0.5, 0, 0);
			// entity.rotationYawHead = entity.rotationYaw = entity.prevRotationYaw = entity.prevRotationYawHead = entity.prevRenderYawOffset = entity.renderYawOffset = 0;
			// worldIn.spawnEntityInWorld(entity);
		}
		return EnumActionResult.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (worldIn.isRemote) return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
		Vec3d vec = playerIn.getLook(1.0f);
		EntityHerobrineFireball entity = new EntityHerobrineFireball(worldIn, playerIn, vec.xCoord, vec.yCoord, vec.zCoord);
		// EntityGhostPlayer entity = new EntityGhostPlayer(worldIn, PlayerNameAlias.Cpw11);
		// entity.setPositionAndRotation(x, y, z, yaw, pitch);
		worldIn.spawnEntityInWorld(entity);
		// playerIn.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(playerIn.getMaxHealth()+1);
		// playerIn.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 20 * 20));
		// playerIn.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(playerIn.getMaxHealth()-20);
		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
		p_77624_3_.add(TextFormatting.DARK_PURPLE + "Debug Tool");
	}

}
