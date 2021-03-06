package tamaized.dalquor.common.entity.companion;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.ai.EntityAIFollowOwnerFlying;
import net.minecraft.entity.ai.EntityAILandOnOwnersShoulder;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EntityVoidParrot extends EntityShoulderRiding implements EntityFlying {

	private static final Set<Item> TAME_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS); // TODO
	private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityVoidParrot.class, DataSerializers.VARINT);
	private static final java.util.Map<Class<? extends Entity>, SoundEvent> MIMIC_SOUNDS = Maps.newHashMapWithExpectedSize(32);
	private static final Predicate<EntityLiving> CAN_MIMIC = p_apply_1_ -> p_apply_1_ != null && MIMIC_SOUNDS.containsKey(p_apply_1_.getClass());

	static {
		registerMimicSound(EntityBlaze.class, SoundEvents.E_PARROT_IM_BLAZE);
		registerMimicSound(EntityCaveSpider.class, SoundEvents.E_PARROT_IM_SPIDER);
		registerMimicSound(EntityCreeper.class, SoundEvents.E_PARROT_IM_CREEPER);
		registerMimicSound(EntityElderGuardian.class, SoundEvents.E_PARROT_IM_ELDER_GUARDIAN);
		registerMimicSound(EntityDragon.class, SoundEvents.E_PARROT_IM_ENDERDRAGON);
		registerMimicSound(EntityEnderman.class, SoundEvents.E_PARROT_IM_ENDERMAN);
		registerMimicSound(EntityEndermite.class, SoundEvents.E_PARROT_IM_ENDERMITE);
		registerMimicSound(EntityEvoker.class, SoundEvents.E_PARROT_IM_EVOCATION_ILLAGER);
		registerMimicSound(EntityGhast.class, SoundEvents.E_PARROT_IM_GHAST);
		registerMimicSound(EntityHusk.class, SoundEvents.E_PARROT_IM_HUSK);
		registerMimicSound(EntityIllusionIllager.class, SoundEvents.E_PARROT_IM_ILLUSION_ILLAGER);
		registerMimicSound(EntityMagmaCube.class, SoundEvents.E_PARROT_IM_MAGMACUBE);
		registerMimicSound(EntityPigZombie.class, SoundEvents.E_PARROT_IM_ZOMBIE_PIGMAN);
		registerMimicSound(EntityPolarBear.class, SoundEvents.E_PARROT_IM_POLAR_BEAR);
		registerMimicSound(EntityShulker.class, SoundEvents.E_PARROT_IM_SHULKER);
		registerMimicSound(EntitySilverfish.class, SoundEvents.E_PARROT_IM_SILVERFISH);
		registerMimicSound(EntitySkeleton.class, SoundEvents.E_PARROT_IM_SKELETON);
		registerMimicSound(EntitySlime.class, SoundEvents.E_PARROT_IM_SLIME);
		registerMimicSound(EntitySpider.class, SoundEvents.E_PARROT_IM_SPIDER);
		registerMimicSound(EntityStray.class, SoundEvents.E_PARROT_IM_STRAY);
		registerMimicSound(EntityVex.class, SoundEvents.E_PARROT_IM_VEX);
		registerMimicSound(EntityVindicator.class, SoundEvents.E_PARROT_IM_VINDICATION_ILLAGER);
		registerMimicSound(EntityWitch.class, SoundEvents.E_PARROT_IM_WITCH);
		registerMimicSound(EntityWither.class, SoundEvents.E_PARROT_IM_WITHER);
		registerMimicSound(EntityWitherSkeleton.class, SoundEvents.E_PARROT_IM_WITHER_SKELETON);
		registerMimicSound(EntityWolf.class, SoundEvents.E_PARROT_IM_WOLF);
		registerMimicSound(EntityZombie.class, SoundEvents.E_PARROT_IM_ZOMBIE);
		registerMimicSound(EntityZombieVillager.class, SoundEvents.E_PARROT_IM_ZOMBIE_VILLAGER);
	}

	public float flap;
	public float flapSpeed;
	public float oFlapSpeed;
	public float oFlap;
	public float flapping = 1.0F;
	private boolean partyParrot;
	private BlockPos jukeboxPosition;

	public EntityVoidParrot(World worldIn) {
		super(worldIn);
		this.setSize(0.5F, 0.9F);
		this.moveHelper = new EntityFlyHelper(this);
	}

	private static boolean playMimicSound(World p_192006_0_, Entity p_192006_1_) {
		if (!p_192006_1_.isSilent() && p_192006_0_.rand.nextInt(50) == 0) {
			List<EntityLiving> list = p_192006_0_.<EntityLiving>getEntitiesWithinAABB(EntityLiving.class, p_192006_1_.getEntityBoundingBox().grow(20.0D), CAN_MIMIC);

			if (!list.isEmpty()) {
				EntityLiving entityliving = list.get(p_192006_0_.rand.nextInt(list.size()));

				if (!entityliving.isSilent()) {
					SoundEvent soundevent = MIMIC_SOUNDS.get(entityliving.getClass());
					p_192006_0_.playSound((EntityPlayer) null, p_192006_1_.posX, p_192006_1_.posY, p_192006_1_.posZ, soundevent, p_192006_1_.getSoundCategory(), 0.7F, getPitch(p_192006_0_.rand));
					return true;
				}
			}

			return false;
		} else {
			return false;
		}
	}

	public static void playAmbientSound(World p_192005_0_, Entity p_192005_1_) {
		if (!p_192005_1_.isSilent() && !playMimicSound(p_192005_0_, p_192005_1_) && p_192005_0_.rand.nextInt(200) == 0) {
			p_192005_0_.playSound((EntityPlayer) null, p_192005_1_.posX, p_192005_1_.posY, p_192005_1_.posZ, getAmbientSound(p_192005_0_.rand), p_192005_1_.getSoundCategory(), 1.0F, getPitch(p_192005_0_.rand));
		}
	}

	private static SoundEvent getAmbientSound(Random p_192003_0_) {
		if (p_192003_0_.nextInt(1000) == 0) {
			List<SoundEvent> list = new ArrayList<SoundEvent>(MIMIC_SOUNDS.values());
			SoundEvent ret = list.get(p_192003_0_.nextInt(list.size()));
			return ret == null ? SoundEvents.ENTITY_PARROT_AMBIENT : ret;
		} else {
			return SoundEvents.ENTITY_PARROT_AMBIENT;
		}
	}

	private static float getPitch(Random p_192000_0_) {
		return (p_192000_0_.nextFloat() - p_192000_0_.nextFloat()) * 0.2F + 1.0F;
	}

	public static void registerMimicSound(Class<? extends Entity> cls, SoundEvent sound) {
		MIMIC_SOUNDS.put(cls, sound);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);

		if (!this.isTamed() && TAME_ITEMS.contains(itemstack.getItem())) {
			if (!player.capabilities.isCreativeMode) {
				itemstack.shrink(1);
			}

			if (!this.isSilent()) {
				this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PARROT_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			}

			if (!this.world.isRemote) {
				if (this.rand.nextInt(10) == 0) {
					this.setTamedBy(player);
					this.playTameEffect(true);
					this.world.setEntityState(this, (byte) 7);
				} else {
					this.playTameEffect(false);
					this.world.setEntityState(this, (byte) 6);
				}
			}

			return true;
		} else {
			if (!this.world.isRemote && !this.isFlying() && this.isTamed() && this.isOwner(player)) {
				this.aiSit.setSitting(!this.isSitting());
			}

			return super.processInteract(player, hand);
		}
	}

	@Override
	public boolean getCanSpawnHere() { // TODO
		/*for (int x = -15; x <= 15; x++)
			for (int z = -15; z <= 15; z++)
				for (int y = -5; y <= 5; y++) {
					BlockPos pos = new BlockPos(MathHelper.floor(this.posX + x), MathHelper.floor(this.getEntityBoundingBox().minY + y), MathHelper.floor(this.posZ + z));
					IBlockState state = world.getBlockState(pos);
					if (state.getBlock() == VoidCraftBlocks.etherealPlant)
						return true;
				}*/
		return false;

	}

	/**
	 * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
	 * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
	 */
	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.setVariant(this.rand.nextInt(5));
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	protected void initEntityAI() {
		this.aiSit = new EntityAISit(this);
		this.tasks.addTask(0, new EntityAIPanic(this, 1.25D));
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(2, new EntityAIFollowOwnerFlying(this, 1.0D, 5.0F, 1.0F));
		this.tasks.addTask(2, new EntityAIWanderAvoidWaterFlying(this, 1.0D));
		this.tasks.addTask(3, new EntityAILandOnOwnersShoulder(this));
		this.tasks.addTask(3, new EntityAIFollow(this, 1.0D, 3.0F, 7.0F));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.4000000059604645D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
	}

	/**
	 * Returns new PathNavigateGround instance
	 */
	@Override
	protected PathNavigate createNavigator(World worldIn) {
		PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
		pathnavigateflying.setCanOpenDoors(false);
		pathnavigateflying.setCanFloat(true);
		pathnavigateflying.setCanEnterDoors(true);
		return pathnavigateflying;
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.6F;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		playMimicSound(this.world, this);

		if (this.jukeboxPosition == null || this.jukeboxPosition.distanceSq(this.posX, this.posY, this.posZ) > 12.0D || this.world.getBlockState(this.jukeboxPosition).getBlock() != Blocks.JUKEBOX) {
			this.partyParrot = false;
			this.jukeboxPosition = null;
		}

		super.onLivingUpdate();
		this.calculateFlapping();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPartying(BlockPos pos, boolean p_191987_2_) {
		this.jukeboxPosition = pos;
		this.partyParrot = p_191987_2_;
	}

	@SideOnly(Side.CLIENT)
	public boolean isPartying() {
		return this.partyParrot;
	}

	private void calculateFlapping() {
		this.oFlap = this.flap;
		this.oFlapSpeed = this.flapSpeed;
		this.flapSpeed = (float) ((double) this.flapSpeed + (double) (this.onGround ? -1 : 4) * 0.3D);
		this.flapSpeed = MathHelper.clamp(this.flapSpeed, 0.0F, 1.0F);

		if (!this.onGround && this.flapping < 1.0F) {
			this.flapping = 1.0F;
		}

		this.flapping = (float) ((double) this.flapping * 0.9D);

		if (!this.onGround && this.motionY < 0.0D) {
			this.motionY *= 0.6D;
		}

		this.flap += this.flapping * 2.0F;
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
	 * the animal type)
	 */
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return false;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
	}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
	}

	/**
	 * Returns true if the mob is currently able to mate with the specified mob.
	 */
	@Override
	public boolean canMateWith(EntityAnimal otherAnimal) {
		return false;
	}

	@Override
	@Nullable
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
	}

	@Override
	@Nullable
	public SoundEvent getAmbientSound() {
		return getAmbientSound(this.rand);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return SoundEvents.ENTITY_PARROT_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_PARROT_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_PARROT_STEP, 0.15F, 1.0F);
	}

	@Override
	protected float playFlySound(float p_191954_1_) {
		this.playSound(SoundEvents.ENTITY_PARROT_FLY, 0.15F, 1.0F);
		return p_191954_1_ + this.flapSpeed / 2.0F;
	}

	@Override
	protected boolean makeFlySound() {
		return true;
	}

	/**
	 * Gets the pitch of living sounds in living entities.
	 */
	@Override
	protected float getSoundPitch() {
		return getPitch(this.rand);
	}

	@Override
	public SoundCategory getSoundCategory() {
		return SoundCategory.NEUTRAL;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities when colliding.
	 */
	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (!(entityIn instanceof EntityPlayer)) {
			super.collideWithEntity(entityIn);
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else {
			if (this.aiSit != null) {
				this.aiSit.setSitting(false);
			}

			return super.attackEntityFrom(source, amount);
		}
	}

	public int getVariant() {
		return MathHelper.clamp(this.dataManager.get(VARIANT), 0, 4);
	}

	public void setVariant(int p_191997_1_) {
		this.dataManager.set(VARIANT, p_191997_1_);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(VARIANT, 0);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("Variant", this.getVariant());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setVariant(compound.getInteger("Variant"));
	}

	@Nullable
	@Override
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_PARROT;
	}

	public boolean isFlying() {
		return !this.onGround;
	}

}
