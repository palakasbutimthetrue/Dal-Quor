package Tamaized.Voidcraft.entity.nonliving;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import Tamaized.Voidcraft.entity.EntityVoidNPC;
import Tamaized.Voidcraft.entity.boss.render.bossBar.IVoidBossData;
import Tamaized.Voidcraft.sound.BossMusicManager;
import Tamaized.Voidcraft.xiaCastle.logic.battle.EntityVoidNPCAIBase;
import Tamaized.Voidcraft.xiaCastle.logic.battle.IBattleHandler;

public abstract class EntityVoidBoss extends EntityVoidNPC implements IVoidBossData {

	private IBattleHandler handler;
	private int phase = 0;
	private boolean ready = false;
	private boolean active = false;

	private ArrayList<EntityAIBase> ai = new ArrayList<EntityAIBase>();
	private ArrayList<Class> filter = new ArrayList<Class>();

	public EntityVoidBoss(World world) {
		super(world);
		filter.addAll(getFilters());
		this.isImmuneToFire = immuneToFire();
		this.hurtResistantTime = 10;
		this.setSize(sizeWidth(), sizeHeight());
		for (Class c : filter)
			this.tasks.addTask(6, new EntityAIWatchClosest(this, c, 64.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
	}

	public EntityVoidBoss(World world, IBattleHandler handler) {
		this(world);
		this.handler = handler;
	}

	public void start() {
		if (phase == 0) ready = true;
		else doDamage((int) this.getMaxHealth());
		active = true;
	}

	public boolean hasStartedFight() {
		return phase > 0;
	}
	
	public int getCurrentPhase(){
		return phase;
	}

	public boolean isActive() {
		return active;
	}

	public boolean displayBossMode() {
		return (maxPhases() >= phase && phase > 0);
	}

	public void doDamage(int a) {
		this.setHealth(this.getHealth() - a);
		triggerOnDamage(phase);
	}

	@Override
	public float getPercentHP() {
		return this.getHealth() / this.getMaxHealth();
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		handlerUpdate();
	}

	private void handlerUpdate() {
		if (!worldObj.isRemote) {
			if (handler == null) {
				setDead();
				return;
			}
			updateAI();
		}
	}

	private void updateAI() {
		if (worldObj.isRemote) return;
		if (ready) {
			phase++;
			preInitPhase(phase);
			ready = false;
		}

		if (phase > maxPhases()) {
			if (this.getHealth() <= 0.0F) {
				this.trueDeathUpdate();
			}
		} else {
			updatePhase(phase);
		}
	}

	private void preInitPhase(int p) {
		Iterator iter = ai.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			if (o instanceof EntityVoidNPCAIBase) {
				EntityVoidNPCAIBase ai = (EntityVoidNPCAIBase) o;
				ai.kill();
			}
			if (o instanceof EntityAIBase) tasks.removeTask((EntityAIBase) o);
			iter.remove();
		}
		tasks.taskEntries.clear();
		targetTasks.taskEntries.clear();
		canDie = false;
		if (p > maxPhases()) {
			canDie = true;
			onDeathUpdate();
		}else{
			initPhase(p);
		}
	}
	
	protected void addAI(Class<? extends EntityVoidNPCAIBase> c){
		try{
		Constructor<? extends EntityVoidNPCAIBase> ctor = c.getConstructor(EntityVoidBoss.class, ArrayList.class);
		EntityVoidNPCAIBase newAI = ctor.newInstance(this, filter);
		ai.add(newAI);
		newAI.Init();
		this.tasks.addTask(1, newAI);
		}catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
			e.printStackTrace();
		}
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource p_70645_1_) { // Switch phases when we fake death
		if (phase > maxPhases()) {
			this.setHealth(0);
			this.isDead = true;
			super.onDeath(p_70645_1_);
			setDead();
		} else {
			ready = true;
			updateAI();
		}
	}

	private void trueDeathUpdate() {
		active = false;
		++this.deathTime;

		if (this.deathTime >= 20) {
			int i;

			if (!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && this.canDropLoot() && this.worldObj.getGameRules().getBoolean("doMobLoot")) {
				i = this.getExperiencePoints(this.attackingPlayer);

				while (i > 0) {
					int j = EntityXPOrb.getXPSplit(i);
					i -= j;
					this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
				}
			}

			this.isDead = true;

			for (i = 0; i < 20; ++i) {
				double d2 = this.rand.nextGaussian() * 0.02D;
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d2, d0, d1);
			}
		}
	}

	@Override
	protected void onDeathUpdate() { // Intercept deathUpdate to keep entity alive
		if (ready) return;
		this.isDead = false;
		onDeath(DamageSource.generic);
	}

	@Override
	public void setDead() { // True death
		BossMusicManager.StopTheSound();
		Iterator iter = ai.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			if (o instanceof EntityVoidNPCAIBase) {
				EntityVoidNPCAIBase ai = (EntityVoidNPCAIBase) o;
				ai.kill();
			}
			if (o instanceof EntityAIBase) tasks.removeTask((EntityAIBase) o);
			iter.remove();
		}
		phase = 100;
		setHealth(0);
		this.isDead = true;
	}

	/**
	 * Checks whether target entity is alive.
	 */
	@Override
	public boolean isEntityAlive() {
		return maxPhases() >= phase;
	}

	@Override
	protected void despawnEntity() {
	}

	@Override
	protected void collideWithEntity(Entity par1Entity) {
	}

	@Override
	public void applyEntityCollision(Entity par1Entity) {
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(999.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(999.0D);
	}

	protected abstract void initPhase(int phase);

	protected abstract void updatePhase(int phase);

	protected abstract ArrayList<Class> getFilters();

	protected abstract boolean immuneToFire();

	protected abstract float sizeWidth();

	protected abstract float sizeHeight();

	protected abstract int maxPhases();

	protected abstract void triggerOnDamage(int phase);

}