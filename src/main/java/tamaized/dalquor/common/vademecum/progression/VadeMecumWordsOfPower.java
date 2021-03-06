package tamaized.dalquor.common.vademecum.progression;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import tamaized.dalquor.common.capabilities.CapabilityList;
import tamaized.dalquor.common.capabilities.vadeMecum.IVadeMecumCapability;
import tamaized.dalquor.common.capabilities.vadeMecum.IVadeMecumCapability.Category;
import tamaized.dalquor.common.capabilities.vadeMecum.IVadeMecumCapability.CategoryDataWrapper;
import tamaized.dalquor.common.damagesources.DamageSourceAcid;
import tamaized.dalquor.common.damagesources.DamageSourceFrost;
import tamaized.dalquor.common.damagesources.DamageSourceLit;
import tamaized.dalquor.common.damagesources.DamageSourceVoidicInfusion;
import tamaized.dalquor.common.entity.companion.EntityCompanion;
import tamaized.dalquor.common.entity.companion.EntityCompanionFireElemental;
import tamaized.dalquor.common.entity.nonliving.EntityCasterLightningBolt;
import tamaized.dalquor.common.entity.nonliving.EntitySpellImplosion;
import tamaized.dalquor.common.entity.nonliving.EntitySpellRune;
import tamaized.dalquor.common.entity.nonliving.ProjectileDisintegration;
import tamaized.dalquor.common.helper.SheatheHelper;
import tamaized.dalquor.common.potion.PotionSheathe;
import tamaized.dalquor.registry.ModAdvancements;
import tamaized.dalquor.registry.ModBlocks;
import tamaized.dalquor.registry.ModFluids;
import tamaized.dalquor.registry.ModItems;
import tamaized.dalquor.registry.ModPotions;
import tamaized.tammodized.common.helper.ExplosionDamageHelper;
import tamaized.tammodized.common.helper.RayTraceHelper;
import tamaized.tammodized.common.particles.ParticleHelper;
import tamaized.tammodized.common.particles.ParticlePacketHandlerRegistry;
import tamaized.tammodized.common.particles.network.ParticleFluffPacketHandler;
import tamaized.tammodized.registry.TamModizedParticles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class VadeMecumWordsOfPower {

	private static final Map<Category, CategoryDataWrapper> categoryMap = new HashMap<>();
	private static final CategoryDataWrapper NullWrapper = new CategoryDataWrapper(CategoryDataWrapper.Element.NULL, "null", ItemStack.EMPTY);

	static {
		categoryMap.put(IVadeMecumCapability.Category.INTRO, new CategoryDataWrapper(CategoryDataWrapper.Element.NULL, "dalquor.ritual.INTRO", ItemStack.EMPTY));
		categoryMap.put(IVadeMecumCapability.Category.TOME, new CategoryDataWrapper(CategoryDataWrapper.Element.NULL, "dalquor.ritual.TOME", ItemStack.EMPTY));

		categoryMap.put(IVadeMecumCapability.Category.Flame, new CategoryDataWrapper(CategoryDataWrapper.Element.FIRE, "dalquor.ritual.Flame", new ItemStack(Items.FLINT)));
		categoryMap.put(IVadeMecumCapability.Category.FireSheathe, new CategoryDataWrapper(CategoryDataWrapper.Element.FIRE, "dalquor.ritual.FireSheathe", new ItemStack(Items.MAGMA_CREAM)));
		categoryMap.put(IVadeMecumCapability.Category.Fireball, new CategoryDataWrapper(CategoryDataWrapper.Element.FIRE, "dalquor.ritual.Fireball", new ItemStack(Items.FIRE_CHARGE)));
		categoryMap.put(IVadeMecumCapability.Category.FireTrap, new CategoryDataWrapper(CategoryDataWrapper.Element.FIRE, "dalquor.ritual.FireTrap", new ItemStack(Blocks.MAGMA)));
		categoryMap.put(IVadeMecumCapability.Category.ExplosionFire, new CategoryDataWrapper(CategoryDataWrapper.Element.FIRE, "dalquor.ritual.ExplosionFire", new ItemStack(Blocks.TNT)));
		categoryMap.put(IVadeMecumCapability.Category.RingOfFire, new CategoryDataWrapper(CategoryDataWrapper.Element.FIRE, "dalquor.ritual.RingOfFire", new ItemStack(Items.GUNPOWDER)));

		categoryMap.put(IVadeMecumCapability.Category.Shock, new CategoryDataWrapper(CategoryDataWrapper.Element.AIR, "dalquor.ritual.Shock", new ItemStack(Items.FEATHER)));
		categoryMap.put(IVadeMecumCapability.Category.ShockSheathe, new CategoryDataWrapper(CategoryDataWrapper.Element.AIR, "dalquor.ritual.ShockSheathe", new ItemStack(Blocks.CARPET)));
		categoryMap.put(IVadeMecumCapability.Category.LitStrike, new CategoryDataWrapper(CategoryDataWrapper.Element.AIR, "dalquor.ritual.LitStrike", new ItemStack(Blocks.END_ROD)));
		categoryMap.put(IVadeMecumCapability.Category.LitTrap, new CategoryDataWrapper(CategoryDataWrapper.Element.AIR, "dalquor.ritual.LitTrap", new ItemStack(Items.GHAST_TEAR)));
		categoryMap.put(IVadeMecumCapability.Category.ExplosionLit, new CategoryDataWrapper(CategoryDataWrapper.Element.AIR, "dalquor.ritual.ExplosionLit", new ItemStack(Items.CHORUS_FRUIT_POPPED)));
		categoryMap.put(IVadeMecumCapability.Category.RingOfLit, new CategoryDataWrapper(CategoryDataWrapper.Element.AIR, "dalquor.ritual.RingOfLit", new ItemStack(Blocks.GLOWSTONE)));

		categoryMap.put(IVadeMecumCapability.Category.Freeze, new CategoryDataWrapper(CategoryDataWrapper.Element.WATER, "dalquor.ritual.Freeze", new ItemStack(Items.SNOWBALL)));
		categoryMap.put(IVadeMecumCapability.Category.FrostSheathe, new CategoryDataWrapper(CategoryDataWrapper.Element.WATER, "dalquor.ritual.FrostSheathe", new ItemStack(Blocks.SNOW)));
		categoryMap.put(IVadeMecumCapability.Category.IceSpike, new CategoryDataWrapper(CategoryDataWrapper.Element.WATER, "dalquor.ritual.IceSpike", new ItemStack(Blocks.ICE)));
		categoryMap.put(IVadeMecumCapability.Category.FrostTrap, new CategoryDataWrapper(CategoryDataWrapper.Element.WATER, "dalquor.ritual.FrostTrap", new ItemStack(Blocks.PRISMARINE)));
		categoryMap.put(IVadeMecumCapability.Category.ExplosionFrost, new CategoryDataWrapper(CategoryDataWrapper.Element.WATER, "dalquor.ritual.ExplosionFrost", PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionType.getPotionTypeForName("water"))));
		categoryMap.put(IVadeMecumCapability.Category.RingOfFrost, new CategoryDataWrapper(CategoryDataWrapper.Element.WATER, "dalquor.ritual.RingOfFrost", new ItemStack(Items.PRISMARINE_CRYSTALS)));

		categoryMap.put(IVadeMecumCapability.Category.AcidSpray, new CategoryDataWrapper(CategoryDataWrapper.Element.EARTH, "dalquor.ritual.AcidSpray", new ItemStack(Blocks.DIRT)));
		categoryMap.put(IVadeMecumCapability.Category.AcidSheathe, new CategoryDataWrapper(CategoryDataWrapper.Element.EARTH, "dalquor.ritual.AcidSheathe", new ItemStack(Items.SLIME_BALL)));
		categoryMap.put(IVadeMecumCapability.Category.Disint, new CategoryDataWrapper(CategoryDataWrapper.Element.EARTH, "dalquor.ritual.Disint", new ItemStack(Blocks.MOSSY_COBBLESTONE)));
		categoryMap.put(IVadeMecumCapability.Category.AcidTrap, new CategoryDataWrapper(CategoryDataWrapper.Element.EARTH, "dalquor.ritual.AcidTrap", new ItemStack(Blocks.SLIME_BLOCK)));
		categoryMap.put(IVadeMecumCapability.Category.ExplosionAcid, new CategoryDataWrapper(CategoryDataWrapper.Element.EARTH, "dalquor.ritual.ExplosionAcid", new ItemStack(Items.POISONOUS_POTATO)));
		categoryMap.put(IVadeMecumCapability.Category.RingOfAcid, new CategoryDataWrapper(CategoryDataWrapper.Element.EARTH, "dalquor.ritual.RingOfAcid", new ItemStack(Items.FISH, 1, 3)));

		categoryMap.put(IVadeMecumCapability.Category.VoidicTouch, new CategoryDataWrapper(CategoryDataWrapper.Element.VOID, "dalquor.ritual.VoidicTouch", new ItemStack(ModItems.voidcrystal)));
		categoryMap.put(IVadeMecumCapability.Category.VoidicSheathe, new CategoryDataWrapper(CategoryDataWrapper.Element.VOID, "dalquor.ritual.VoidicSheathe", new ItemStack(ModBlocks.voidbrick)));
		categoryMap.put(IVadeMecumCapability.Category.Implosion, new CategoryDataWrapper(CategoryDataWrapper.Element.VOID, "dalquor.ritual.Implosion", new ItemStack(ModBlocks.realityHole)));

		categoryMap.put(IVadeMecumCapability.Category.Invoke, new CategoryDataWrapper(CategoryDataWrapper.Element.VOID, "dalquor.ritual.Invoke", new ItemStack(ModBlocks.ethericPlatform)));
		categoryMap.put(IVadeMecumCapability.Category.SummonFireElemental, new CategoryDataWrapper(CategoryDataWrapper.Element.FIRE, "dalquor.ritual.SummonFireElemental", new ItemStack(Items.FLINT_AND_STEEL)));

		categoryMap.put(IVadeMecumCapability.Category.Voice, new CategoryDataWrapper(CategoryDataWrapper.Element.NULL, "dalquor.ritual.Voice", ItemStack.EMPTY));
		categoryMap.put(IVadeMecumCapability.Category.VoidicControl, new CategoryDataWrapper(CategoryDataWrapper.Element.NULL, "dalquor.ritual.VoidicControl", ItemStack.EMPTY));
		categoryMap.put(IVadeMecumCapability.Category.ImprovedCasting, new CategoryDataWrapper(CategoryDataWrapper.Element.NULL, "dalquor.ritual.ImprovedCasting", ItemStack.EMPTY));
		categoryMap.put(IVadeMecumCapability.Category.Empowerment, new CategoryDataWrapper(CategoryDataWrapper.Element.NULL, "dalquor.ritual.Empowerment", ItemStack.EMPTY));
		categoryMap.put(IVadeMecumCapability.Category.Tolerance, new CategoryDataWrapper(CategoryDataWrapper.Element.NULL, "dalquor.ritual.Tolerance", ItemStack.EMPTY));
		categoryMap.put(IVadeMecumCapability.Category.TotalControl, new CategoryDataWrapper(CategoryDataWrapper.Element.NULL, "dalquor.ritual.TotalControl", ItemStack.EMPTY));
		categoryMap.put(IVadeMecumCapability.Category.Dreams, new CategoryDataWrapper(CategoryDataWrapper.Element.NULL, "dalquor.ritual.Dreams", ItemStack.EMPTY));

	}

	public static CategoryDataWrapper getCategoryData(Category c) {
		return c == null ? NullWrapper : categoryMap.getOrDefault(c, NullWrapper);
	}

	private static boolean isEmptyBlock(World world, BlockPos pos) {
		IBlockState iblockstate = world.getBlockState(pos);
		return iblockstate.getMaterial() == Material.AIR || !iblockstate.isFullCube();
	}

	public static void invoke(World world, EntityPlayer caster) { // TODO: clean all this up, make methods/classes/helpers and so on for all this junk
		IVadeMecumCapability cap = caster.getCapability(CapabilityList.VADEMECUM, null);
		if (cap == null || world.isRemote)
			return;
		IVadeMecumCapability.Category power = cap.getCurrentActive();
		if (power == null || !IVadeMecumCapability.isActivePower(power) || !cap.hasCategory(power))
			return;
		if ((cap.getStackInSlot(power).isEmpty() || cap.getStackInSlot(power).getCount() <= 0) && !caster.inventory.hasItemStack(getCategoryData(power).getStack())) {
			caster.sendMessage(new TextComponentTranslation("dalquor.VadeMecum.spells.nomats", I18n.format(getCategoryData(power).getName()).trim())); // TODO: wat, AoV handles this correctly, look there later
			return;
		}
		boolean useCharge = false;
		power = cap.getCurrentActive();
		if (power != null && cap.hasCategory(power)) {
			boolean doCast = false;
			CategoryDataWrapper wrapper = getCategoryData(power);
			if (wrapper != null && wrapper != NullWrapper) {
				if (wrapper.getElement() != CategoryDataWrapper.Element.VOID) {
					int rand = world.rand.nextInt(100) + 1;
					if (rand <= cap.getFailureChance()) {
						ExplosionDamageHelper.explode(null, world, world.newExplosion(null, caster.posX, caster.posY, caster.posZ, 7.0F, true, true), 7.0F, caster.posX, caster.posY, caster.posZ);
						world.playBroadcastSound(1023, caster.getPosition(), 0);
						if (caster instanceof EntityPlayerMP)
							ModAdvancements.familiarity.trigger((EntityPlayerMP) caster);
						useCharge = true;
					} else {
						doCast = true;
					}
				} else {
					doCast = true;
				}
			}
			if (doCast) {
				HashSet<Entity> exclude = new HashSet<>();
				RayTraceResult result;
				switch (power) {
					case SummonFireElemental: {
						EntityCompanion companion = new EntityCompanionFireElemental(world);
						companion.tame(caster);
						cap.summonCompanion(companion);
						int i = MathHelper.floor(caster.posX) - 2;
						int j = MathHelper.floor(caster.posZ) - 2;
						int k = MathHelper.floor(caster.getEntityBoundingBox().minY);
						theLoop:
						for (int l = 0; l <= 4; ++l) {
							for (int i1 = 0; i1 <= 4; ++i1) {
								BlockPos pos = new BlockPos(i + l, k - 1, j + i1);
								if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && world.getBlockState(pos).isSideSolid(world, pos, EnumFacing.UP) && isEmptyBlock(world, pos.up()) && isEmptyBlock(world, pos.up(2))) {
									companion.setLocationAndAngles((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), companion.rotationYaw, companion.rotationPitch);
									break theLoop;
								}
							}
						}
						world.spawnEntity(companion);
						useCharge = true;
					}
					break;
					case Flame: {
						exclude.add(caster);
						result = RayTraceHelper.tracePath(world, caster, 32, 1, exclude);
						if (result != null) {
							if (result.entityHit != null) {
								result.entityHit.setFire(10);
							} else {
								BlockPos pos = result.getBlockPos();
								switch (result.sideHit) {
									default:
									case UP: {
										pos = pos.add(0, 1, 0);
									}
									break;
									case DOWN: {
										pos = pos.add(0, -1, 0);
									}
									break;
									case NORTH: {
										pos = pos.add(0, 0, -1);
									}
									break;
									case SOUTH: {
										pos = pos.add(0, 0, 1);
									}
									break;
									case EAST: {
										pos = pos.add(1, 0, 0);
									}
									break;
									case WEST: {
										pos = pos.add(-1, 0, 0);
									}
									break;
								}
								if (world.isAirBlock(pos))
									world.setBlockState(pos, Blocks.FIRE.getDefaultState());
							}
						}
						useCharge = true;
					}
					break;
					case FireSheathe: {
						SheatheHelper.castSheathe(caster, PotionSheathe.Type.Fire, 20 * 90);
						useCharge = true;
					}
					break;
					case Fireball: {
						Vec3d vec = caster.getLookVec();
						EntityFireball entity = new EntityLargeFireball(world, caster, vec.x, vec.y, vec.z);
						entity.shootingEntity = caster;
						world.spawnEntity(entity);
						useCharge = true;
					}
					break;
					case FireTrap: {
						useCharge = castRune(world, caster, new EntitySpellRune(world, EntitySpellRune.DamageType.FIRE, 10, 5, 0xFF5733));
					}
					break;
					case ExplosionFire: {
						world.createExplosion(caster, caster.posX, caster.posY, caster.posZ, 10.0F, false);
						useCharge = true;
					}
					break;
					case RingOfFire: {
						for (BlockPos pos : createCircle(caster.getPosition()))
							if ((world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos)) && (!world.isAirBlock(pos.down()) && world.isBlockFullCube(pos.down())))
								world.setBlockState(pos, Blocks.FIRE.getDefaultState());
						useCharge = true;
					}
					break;
					case Shock: {
						exclude.add(caster);
						RayTraceResult ray = RayTraceHelper.tracePath(world, caster, 2, 1, exclude);
						if (ray != null && ray.entityHit != null && ray.entityHit instanceof EntityLivingBase) {
							ray.entityHit.attackEntityFrom(new DamageSourceLit(), 5);
							useCharge = true;
						}
					}
					break;
					case ShockSheathe: {
						SheatheHelper.castSheathe(caster, PotionSheathe.Type.Lit, 20 * 90);
						useCharge = true;
					}
					break;
					case LitStrike: {
						exclude.add(caster);
						result = RayTraceHelper.tracePath(world, caster, 32, 1, exclude);
						if (result != null) {
							if (result.entityHit != null) {
								EntityCasterLightningBolt entitylightningbolt = new EntityCasterLightningBolt(world, caster, result.entityHit.posX, result.entityHit.posY, result.entityHit.posZ, false);
								world.addWeatherEffect(entitylightningbolt);
							} else {
								BlockPos bp = result.getBlockPos();
								if (bp != null) {
									EntityCasterLightningBolt entitylightningbolt = new EntityCasterLightningBolt(world, caster, bp.getX(), bp.getY() + 1, bp.getZ(), false);
									world.addWeatherEffect(entitylightningbolt);
								}
							}
							useCharge = true;
						}
					}
					break;
					case LitTrap: {
						useCharge = castRune(world, caster, new EntitySpellRune(world, EntitySpellRune.DamageType.SHOCK, 10, 5, 0xFFFFFF));
					}
					break;
					case ExplosionLit: {
						List<Entity> damageList = world.getEntitiesWithinAABBExcludingEntity(caster, new AxisAlignedBB(caster.posX - 5, caster.posY - 5, caster.posZ - 5, caster.posX + 5, caster.posY + 5, caster.posZ + 5));
						for (Entity e : damageList) {
							if (!(e instanceof EntityLivingBase))
								continue;
							e.attackEntityFrom(new DamageSourceLit(), 10);
						}
						for (int index = 0; index < 1000; index++) {
							ParticleHelper.sendPacketToClients(world, TamModizedParticles.fluff, caster.getPositionVector().addVector(0.5, 0, 0.5), 64, new ParticleHelper.ParticlePacketHelper(TamModizedParticles.fluff, ((ParticleFluffPacketHandler) ParticlePacketHandlerRegistry.getHandler(TamModizedParticles.fluff)).new ParticleFluffData(new Vec3d(world.rand.nextDouble() * 0.8D - 0.4D, world.rand.nextDouble() * 0.8D - 0.4D, world.rand.nextDouble() * 0.8D - 0.4D), world.rand.nextInt(20 * 3), -0.10f, world.rand.nextFloat() * 0.95f + 0.05f, 0xFFFFFFFF)));
						}
						useCharge = true;
					}
					break;
					case RingOfLit: {
						for (BlockPos pos : createCircle(caster.getPosition())) {
							EntityCasterLightningBolt entitylightningbolt = new EntityCasterLightningBolt(world, caster, pos.getX(), pos.getY(), pos.getZ(), false);
							world.addWeatherEffect(entitylightningbolt);
						}
						useCharge = true;
					}
					break;
					case Freeze: {
						exclude.add(caster);
						RayTraceResult ray = RayTraceHelper.tracePath(world, caster, 2, 1, exclude);
						if (ray != null && ray.entityHit != null && ray.entityHit instanceof EntityLivingBase) {
							((EntityLivingBase) ray.entityHit).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 7, 5));
							useCharge = true;
						}
					}
					break;
					case FrostSheathe: {
						SheatheHelper.castSheathe(caster, PotionSheathe.Type.Frost, 20 * 90);
						useCharge = true;
					}
					break;
					case IceSpike: { // TODO
						/*exclude.add(caster);
						result = RayTraceHelper.tracePath(world, caster, 32, 1, exclude);
						if (result != null) {
							if (result.entityHit != null) {
								BlockPos pos = result.entityHit.getPosition();
								if ((world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos)) && (!world.isAirBlock(pos.down()) && world.getBlockState(pos.down()).isFullCube())) {
									world.setBlockState(pos, VoidCraft.blocks.iceSpike.getDefaultState().withProperty(BlockSpellIceSpike.FACING, EnumFacing.getHorizontal(world.rand.nextInt(4))));
									TileEntity te = world.getTileEntity(pos);
									if (te instanceof TileEntitySpellIceSpike)
										((TileEntitySpellIceSpike) te).setCaster(caster);
								}
							} else {
								BlockPos bp = result.getBlockPos();
								if (bp != null) {
									if (world.getBlockState(bp).getBlock().isReplaceable(world, bp)) {
										world.setBlockState(bp, VoidCraft.blocks.iceSpike.getDefaultState().withProperty(BlockSpellIceSpike.FACING, EnumFacing.getHorizontal(world.rand.nextInt(4))));
										TileEntity te = world.getTileEntity(bp);
										if (te instanceof TileEntitySpellIceSpike)
											((TileEntitySpellIceSpike) te).setCaster(caster);
									} else if ((world.isAirBlock(bp.up()) || world.getBlockState(bp.up()).getBlock().isReplaceable(world, bp.up())) && (!world.isAirBlock(bp) && world.getBlockState(bp).isFullCube())) {
										world.setBlockState(bp.up(), VoidCraft.blocks.iceSpike.getDefaultState().withProperty(BlockSpellIceSpike.FACING, EnumFacing.getHorizontal(world.rand.nextInt(4))));
										TileEntity te = world.getTileEntity(bp.up());
										if (te instanceof TileEntitySpellIceSpike)
											((TileEntitySpellIceSpike) te).setCaster(caster);
									}
								}
							}
							useCharge = true;
						}*/
					}
					break;
					case FrostTrap: {
						useCharge = castRune(world, caster, new EntitySpellRune(world, EntitySpellRune.DamageType.FROST, 10, 5, 0x00FFFF));
					}
					break;
					case ExplosionFrost: {
						List<Entity> damageList = world.getEntitiesWithinAABBExcludingEntity(caster, new AxisAlignedBB(caster.posX - 5, caster.posY - 5, caster.posZ - 5, caster.posX + 5, caster.posY + 5, caster.posZ + 5));
						for (Entity e : damageList) {
							if (!(e instanceof EntityLivingBase))
								continue;
							e.attackEntityFrom(new DamageSourceFrost(), 10);
							((EntityLivingBase) e).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 7, 5));
						}
						for (int index = 0; index < 1000; index++) {
							ParticleHelper.sendPacketToClients(world, TamModizedParticles.fluff, caster.getPositionVector().addVector(0.5, 0, 0.5), 64, new ParticleHelper.ParticlePacketHelper(TamModizedParticles.fluff, ((ParticleFluffPacketHandler) ParticlePacketHandlerRegistry.getHandler(TamModizedParticles.fluff)).new ParticleFluffData(new Vec3d(world.rand.nextDouble() * 0.8D - 0.4D, world.rand.nextDouble() * 0.8D - 0.4D, world.rand.nextDouble() * 0.8D - 0.4D), world.rand.nextInt(20 * 3), -0.10f, world.rand.nextFloat() * 0.95f + 0.05f, 0x00FFFFFF)));
						}
						useCharge = true;
					}
					break;
					case RingOfFrost: { // TODO
						/*for (BlockPos pos : createCircle(caster.getPosition()))
							if ((world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos)) && (!world.isAirBlock(pos.down()) && world.getBlockState(pos.down()).isFullCube())) {
								world.setBlockState(pos, VoidCraft.blocks.iceSpike.getDefaultState().withProperty(BlockSpellIceSpike.FACING, EnumFacing.getHorizontal(world.rand.nextInt(4))));
								TileEntity te = world.getTileEntity(pos);
								if (te instanceof TileEntitySpellIceSpike)
									((TileEntitySpellIceSpike) te).setCaster(caster);
							}
						useCharge = true;*/
					}
					break;
					case AcidSpray: {
						Vec3d vec = caster.getLook(1f);
						int damageRange = 5;
						List<Entity> damageList = world.getEntitiesWithinAABBExcludingEntity(caster, new AxisAlignedBB(caster.posX - (vec.x * damageRange), caster.posY - (vec.y * damageRange), caster.posZ - (vec.z * damageRange), caster.posX + (vec.x * damageRange), caster.posY + (vec.y * damageRange), caster.posZ + (vec.z * damageRange)));
						for (Entity e : damageList) {
							e.attackEntityFrom(new DamageSourceAcid(), 5);
						}
						for (int index = 0; index < 300; index++) {
							ParticleHelper.sendPacketToClients(world, TamModizedParticles.fluff, caster.getPositionVector().addVector(0, 1.5f, 0), 64, new ParticleHelper.ParticlePacketHelper(TamModizedParticles.fluff, ((ParticleFluffPacketHandler) ParticlePacketHandlerRegistry.getHandler(TamModizedParticles.fluff)).new ParticleFluffData(new Vec3d(vec.x * 0.15 + (world.rand.nextFloat() * 0.10) - 0.05, vec.y * 0.15 + (world.rand.nextFloat() * 0.10) - 0.05, vec.z * 0.15 + (world.rand.nextFloat() * 0.10) - 0.05), world.rand.nextInt(20 * 3), 0, world.rand.nextFloat() * 0.45f + 0.05f, 0x00FF00FF)));
						}
						useCharge = true;
					}
					break;
					case AcidSheathe: {
						SheatheHelper.castSheathe(caster, PotionSheathe.Type.Acid, 20 * 90);
						useCharge = true;
					}
					break;
					case Disint: {
						ProjectileDisintegration disint = new ProjectileDisintegration(world, caster, caster.posX, caster.posY, caster.posZ);
						disint.setDamageRangeSpeed(15, 0.0F, 0.5D);
						world.spawnEntity(disint);
						useCharge = true;
					}
					break;
					case AcidTrap: {
						useCharge = castRune(world, caster, new EntitySpellRune(world, EntitySpellRune.DamageType.ACID, 10, 5, 0x00FF00));
					}
					break;
					case ExplosionAcid: {
						List<Entity> damageList = world.getEntitiesWithinAABBExcludingEntity(caster, new AxisAlignedBB(caster.posX - 5, caster.posY - 5, caster.posZ - 5, caster.posX + 5, caster.posY + 5, caster.posZ + 5));
						for (Entity e : damageList) {
							if (!(e instanceof EntityLivingBase))
								continue;
							e.attackEntityFrom(new DamageSourceAcid(), 10);
						}
						for (int index = 0; index < 1000; index++) {
							ParticleHelper.sendPacketToClients(world, TamModizedParticles.fluff, caster.getPositionVector().addVector(0.5, 0, 0.5), 64, new ParticleHelper.ParticlePacketHelper(TamModizedParticles.fluff, ((ParticleFluffPacketHandler) ParticlePacketHandlerRegistry.getHandler(TamModizedParticles.fluff)).new ParticleFluffData(new Vec3d(world.rand.nextDouble() * 0.8D - 0.4D, world.rand.nextDouble() * 0.8D - 0.4D, world.rand.nextDouble() * 0.8D - 0.4D), world.rand.nextInt(20 * 3), -0.10f, world.rand.nextFloat() * 0.95f + 0.05f, 0x00FF00FF)));
						}
						useCharge = true;
					}
					break;
					case RingOfAcid: {
						for (BlockPos pos : createCircle(caster.getPosition()))
							if ((world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos)) && (!world.isAirBlock(pos.down()) && world.getBlockState(pos.down()).isFullCube())) {
								world.setBlockState(pos, ModFluids.acidFluidBlock.getDefaultState());
							}
						useCharge = true;
					}
					break;
					case VoidicTouch: {
						exclude.add(caster);
						RayTraceResult ray = RayTraceHelper.tracePath(world, caster, 2, 1, exclude);
						if (ray != null && ray.entityHit != null && ray.entityHit instanceof EntityLivingBase) {
							ray.entityHit.attackEntityFrom(new DamageSourceVoidicInfusion(), 5);
							((EntityLivingBase) ray.entityHit).addPotionEffect(new PotionEffect(ModPotions.voidicInfusion, 20 * 3));
							useCharge = true;
						}
					}
					break;
					case VoidicSheathe: {
						SheatheHelper.castSheathe(caster, PotionSheathe.Type.Void, 20 * 90);
						useCharge = true;
					}
					break;
					case Implosion: {
						List<Entity> damageList = world.getEntitiesWithinAABBExcludingEntity(caster, new AxisAlignedBB(caster.posX - 5, caster.posY - 5, caster.posZ - 5, caster.posX + 5, caster.posY + 5, caster.posZ + 5));
						for (Entity e : damageList) {
							if (!(e instanceof EntityLivingBase))
								continue;
							world.spawnEntity(new EntitySpellImplosion(world, e));
						}
						useCharge = true;
					}
					break;
					case Invoke: {
						caster.addPotionEffect(new PotionEffect(ModPotions.voidicInfusion, 20 * 5));
						useCharge = true;
					}
					break;
					default: {
					}
					break;
				}
			}
		}

		if (useCharge) {
			if (!cap.getStackInSlot(power).isEmpty()) {
				cap.getStackInSlot(power).shrink(1);
				if (cap.getStackInSlot(power).getCount() <= 0)
					cap.setStackSlot(power, ItemStack.EMPTY);
			} else {
				for (int i = 0; i < caster.inventory.getSizeInventory(); ++i) {
					ItemStack itemstack = caster.inventory.getStackInSlot(i);
					if (itemstack.isItemEqual(getCategoryData(power).getStack())) {
						itemstack.shrink(1);
						break;
					}
				}
			}
		}
	}

	public static void invoke(World world, IVadeMecumCapability.Category power, EntityLivingBase caster, EntityLivingBase target) {
		if (world.isRemote)
			return;
		if (power != null) {
			switch (power) {
				case Flame: {
					target.setFire(10);
					BlockPos pos = target.getPosition();
					if (world.isAirBlock(pos))
						world.setBlockState(pos, Blocks.FIRE.getDefaultState());
				}
				break;
				case FireSheathe: {
					SheatheHelper.castSheathe(caster, PotionSheathe.Type.Fire, 20 * 90);
				}
				break;
				case Fireball: {
					double d5 = target.posX - caster.posX;
					double d6 = target.getEntityBoundingBox().minY + (double) (target.height / 2.0F) - (caster.posY + (double) (caster.height / 2.0F));
					double d7 = target.posZ - caster.posZ;
					EntityFireball entity = new EntityLargeFireball(world, caster, d5, d6, d7);
					entity.shootingEntity = caster;
					entity.posY += 1;
					world.spawnEntity(entity);
				}
				break;
				case FireTrap: {
					// castRune(world, caster, new EntitySpellRune(world, EntitySpellRune.DamageType.FIRE, 10, 5, 0xFF5733));
				}
				break;
				case ExplosionFire: {
					world.createExplosion(caster, caster.posX, caster.posY, caster.posZ, 10.0F, false);
				}
				break;
				case RingOfFire: {
					for (BlockPos pos : createCircle(caster.getPosition()))
						if ((world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos)) && (!world.isAirBlock(pos.down()) && world.isBlockFullCube(pos.down())))
							world.setBlockState(pos, Blocks.FIRE.getDefaultState());
				}
				break;
				case Shock: {
					target.attackEntityFrom(new DamageSourceLit(), 5);
				}
				break;
				case ShockSheathe: {
					SheatheHelper.castSheathe(caster, PotionSheathe.Type.Lit, 20 * 90);
				}
				break;
				case LitStrike: {
					if (target instanceof EntityPlayer) {
						EntityCasterLightningBolt entitylightningbolt = new EntityCasterLightningBolt(world, caster, target.posX, target.posY, target.posZ, false);
						world.addWeatherEffect(entitylightningbolt);
					}
				}
				break;
				case LitTrap: {
					// useCharge = castRune(world, caster, new EntitySpellRune(world, EntitySpellRune.DamageType.SHOCK, 10, 5, 0xFFFFFF));
				}
				break;
				case ExplosionLit: {
					List<Entity> damageList = world.getEntitiesWithinAABBExcludingEntity(caster, new AxisAlignedBB(caster.posX - 5, caster.posY - 5, caster.posZ - 5, caster.posX + 5, caster.posY + 5, caster.posZ + 5));
					for (Entity e : damageList) {
						if (!(e instanceof EntityLivingBase))
							continue;
						e.attackEntityFrom(new DamageSourceLit(), 10);
					}
					for (int index = 0; index < 1000; index++) {
						ParticleHelper.sendPacketToClients(world, TamModizedParticles.fluff, caster.getPositionVector().addVector(0.5, 0, 0.5), 64, new ParticleHelper.ParticlePacketHelper(TamModizedParticles.fluff, ((ParticleFluffPacketHandler) ParticlePacketHandlerRegistry.getHandler(TamModizedParticles.fluff)).new ParticleFluffData(new Vec3d(world.rand.nextDouble() * 0.8D - 0.4D, world.rand.nextDouble() * 0.8D - 0.4D, world.rand.nextDouble() * 0.8D - 0.4D), world.rand.nextInt(20 * 3), -0.10f, world.rand.nextFloat() * 0.95f + 0.05f, 0xFFFFFFFF)));
					}
				}
				break;
				case RingOfLit: {
					if (target instanceof EntityPlayer) {
						for (BlockPos pos : createCircle(caster.getPosition())) {
							EntityCasterLightningBolt entitylightningbolt = new EntityCasterLightningBolt(world, caster, pos.getX(), pos.getY(), pos.getZ(), false);
							world.addWeatherEffect(entitylightningbolt);
						}
					}
				}
				break;
				case Freeze: {
					target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 7, 5));
				}
				break;
				case FrostSheathe: {
					SheatheHelper.castSheathe(caster, PotionSheathe.Type.Frost, 20 * 90);
				}
				break;
				case IceSpike: { // TODO
					/*BlockPos pos = target.getPosition();
					if ((world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos)) && (!world.isAirBlock(pos.down()) && world.getBlockState(pos.down()).isFullCube())) {
						world.setBlockState(pos, VoidCraft.blocks.iceSpike.getDefaultState().withProperty(BlockSpellIceSpike.FACING, EnumFacing.getHorizontal(world.rand.nextInt(4))));
						TileEntity te = world.getTileEntity(pos);
						if (te instanceof TileEntitySpellIceSpike)
							((TileEntitySpellIceSpike) te).setCaster(caster);
					}*/
				}
				break;
				case FrostTrap: {
					// useCharge = castRune(world, caster, new EntitySpellRune(world, EntitySpellRune.DamageType.FROST, 10, 5, 0x00FFFF));
				}
				break;
				case ExplosionFrost: {
					List<Entity> damageList = world.getEntitiesWithinAABBExcludingEntity(caster, new AxisAlignedBB(caster.posX - 5, caster.posY - 5, caster.posZ - 5, caster.posX + 5, caster.posY + 5, caster.posZ + 5));
					for (Entity e : damageList) {
						if (!(e instanceof EntityLivingBase))
							continue;
						e.attackEntityFrom(new DamageSourceFrost(), 10);
						((EntityLivingBase) e).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 7, 5));
					}
					for (int index = 0; index < 1000; index++) {
						ParticleHelper.sendPacketToClients(world, TamModizedParticles.fluff, caster.getPositionVector().addVector(0.5, 0, 0.5), 64, new ParticleHelper.ParticlePacketHelper(TamModizedParticles.fluff, ((ParticleFluffPacketHandler) ParticlePacketHandlerRegistry.getHandler(TamModizedParticles.fluff)).new ParticleFluffData(new Vec3d(world.rand.nextDouble() * 0.8D - 0.4D, world.rand.nextDouble() * 0.8D - 0.4D, world.rand.nextDouble() * 0.8D - 0.4D), world.rand.nextInt(20 * 3), -0.10f, world.rand.nextFloat() * 0.95f + 0.05f, 0x00FFFFFF)));
					}
				}
				break;
				case RingOfFrost: { // TODO
					/*for (BlockPos pos : createCircle(caster.getPosition()))
						if ((world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos)) && (!world.isAirBlock(pos.down()) && world.getBlockState(pos.down()).isFullCube())) {
							world.setBlockState(pos, VoidCraft.blocks.iceSpike.getDefaultState().withProperty(BlockSpellIceSpike.FACING, EnumFacing.getHorizontal(world.rand.nextInt(4))));
							TileEntity te = world.getTileEntity(pos);
							if (te instanceof TileEntitySpellIceSpike)
								((TileEntitySpellIceSpike) te).setCaster(caster);
						}*/
				}
				break;
				case AcidSpray: {
					Vec3d vec = caster.getLook(1f);
					int damageRange = 5;
					List<Entity> damageList = world.getEntitiesWithinAABBExcludingEntity(caster, new AxisAlignedBB(caster.posX - (vec.x * damageRange), caster.posY - (vec.y * damageRange), caster.posZ - (vec.z * damageRange), caster.posX + (vec.x * damageRange), caster.posY + (vec.y * damageRange), caster.posZ + (vec.z * damageRange)));
					for (Entity e : damageList) {
						e.attackEntityFrom(new DamageSourceAcid(), 5);
					}
					for (int index = 0; index < 300; index++) {
						ParticleHelper.sendPacketToClients(world, TamModizedParticles.fluff, caster.getPositionVector().addVector(0, 1.5f, 0), 64, new ParticleHelper.ParticlePacketHelper(TamModizedParticles.fluff, ((ParticleFluffPacketHandler) ParticlePacketHandlerRegistry.getHandler(TamModizedParticles.fluff)).new ParticleFluffData(new Vec3d(vec.x * 0.15 + (world.rand.nextFloat() * 0.10) - 0.05, vec.y * 0.15 + (world.rand.nextFloat() * 0.10) - 0.05, vec.z * 0.15 + (world.rand.nextFloat() * 0.10) - 0.05), world.rand.nextInt(20 * 3), 0, world.rand.nextFloat() * 0.45f + 0.05f, 0x00FF00FF)));
					}
				}
				break;
				case AcidSheathe: {
					SheatheHelper.castSheathe(caster, PotionSheathe.Type.Acid, 20 * 90);
				}
				break;
				case Disint: {
					ProjectileDisintegration disint = new ProjectileDisintegration(world, caster, caster.posX, caster.posY, caster.posZ);
					disint.setDamageRangeSpeed(15, 0.0F, 0.5D);
					world.spawnEntity(disint);
				}
				break;
				case AcidTrap: {
					// useCharge = castRune(world, caster, new EntitySpellRune(world, EntitySpellRune.DamageType.ACID, 10, 5, 0x00FF00));
				}
				break;
				case ExplosionAcid: {
					List<Entity> damageList = world.getEntitiesWithinAABBExcludingEntity(caster, new AxisAlignedBB(caster.posX - 5, caster.posY - 5, caster.posZ - 5, caster.posX + 5, caster.posY + 5, caster.posZ + 5));
					for (Entity e : damageList) {
						if (!(e instanceof EntityLivingBase))
							continue;
						e.attackEntityFrom(new DamageSourceAcid(), 10);
					}
					for (int index = 0; index < 1000; index++) {
						ParticleHelper.sendPacketToClients(world, TamModizedParticles.fluff, caster.getPositionVector().addVector(0.5, 0, 0.5), 64, new ParticleHelper.ParticlePacketHelper(TamModizedParticles.fluff, ((ParticleFluffPacketHandler) ParticlePacketHandlerRegistry.getHandler(TamModizedParticles.fluff)).new ParticleFluffData(new Vec3d(world.rand.nextDouble() * 0.8D - 0.4D, world.rand.nextDouble() * 0.8D - 0.4D, world.rand.nextDouble() * 0.8D - 0.4D), world.rand.nextInt(20 * 3), -0.10f, world.rand.nextFloat() * 0.95f + 0.05f, 0x00FF00FF)));
					}
				}
				break;
				case RingOfAcid: {
					for (BlockPos pos : createCircle(caster.getPosition()))
						if ((world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos)) && (!world.isAirBlock(pos.down()) && world.getBlockState(pos.down()).isFullCube())) {
							world.setBlockState(pos, ModFluids.acidFluidBlock.getDefaultState());
						}
				}
				break;
				case VoidicTouch: {
					target.attackEntityFrom(new DamageSourceVoidicInfusion(), 5);
					target.addPotionEffect(new PotionEffect(ModPotions.voidicInfusion, 20 * 3));
				}
				break;
				case VoidicSheathe: {
					SheatheHelper.castSheathe(caster, PotionSheathe.Type.Void, 20 * 90);
				}
				break;
				case Implosion: {
					List<Entity> damageList = world.getEntitiesWithinAABBExcludingEntity(caster, new AxisAlignedBB(caster.posX - 5, caster.posY - 5, caster.posZ - 5, caster.posX + 5, caster.posY + 5, caster.posZ + 5));
					for (Entity e : damageList) {
						if (!(e instanceof EntityLivingBase))
							continue;
						world.spawnEntity(new EntitySpellImplosion(world, e));
					}
				}
				break;
				default: {
				}
				break;
			}

		}

	}

	private static BlockPos[] createCircle(BlockPos pos) {
		return new BlockPos[]{

				pos.add(3, 0, 1), pos.add(3, 0, 0), pos.add(3, 0, -1),

				pos.add(-3, 0, 1), pos.add(-3, 0, 0), pos.add(-3, 0, -1),

				pos.add(1, 0, -3), pos.add(0, 0, -3), pos.add(-1, 0, -3),

				pos.add(1, 0, 3), pos.add(0, 0, 3), pos.add(-1, 0, 3),

				pos.add(2, 0, 2), pos.add(-2, 0, 2), pos.add(2, 0, -2), pos.add(-2, 0, -2)

		};
	}

	private static boolean castRune(World world, EntityPlayer player, EntitySpellRune rune) {
		if (!world.isRemote) {
			HashSet<Entity> exclude = new HashSet<>();
			exclude.add(player);
			RayTraceResult result = RayTraceHelper.tracePath(world, player, 32, 1, exclude);
			if (result != null) {
				if (result.entityHit == null) {
					BlockPos bp = result.getBlockPos();
					BlockPos pos = bp;
					if (bp != null) {
						switch (result.sideHit) {
							case UP:
								pos = pos.add(0, 1, 0);
								rune.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
								world.spawnEntity(rune);
								return true;
							default:
								return false;
						}
					}
				}
			}
		}
		return false;
	}

}
