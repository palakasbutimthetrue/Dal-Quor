package Tamaized.Voidcraft.entity.boss.twins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import Tamaized.Voidcraft.entity.EntityVoidNPC;
import Tamaized.Voidcraft.sound.VoidSoundEvents;

public class EntityBossZol extends EntityVoidNPC{
	
	double xAdd = 0;
	double zAdd = 0;
	
    public EntityBossZol(World par1World) {
    	super(par1World);
    	
    	this.isImmuneToFire = true;
    	this.hurtResistantTime = 10;
        this.setSize(0.6F, 1.8F);
         
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 50.0F));
        //this.tasks.addTask(6, new EntityAILookIdle(this));
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.setInvul(true);
         
       
	}

    @Override
    public boolean canBePushed(){
        return false;
    }
    
    @Override
    protected void collideWithEntity(Entity par1Entity){}
    
    @Override
    public void applyEntityCollision(Entity par1Entity){}

	
    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(999.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(999.0D);
    }
    
    @Override
    protected SoundEvent getAmbientSound(){
    	return VoidSoundEvents.EntityMobZolSoundEvents.ambientSound;
    }
    
    @Override
    protected SoundEvent getHurtSound(){
    	return VoidSoundEvents.EntityMobZolSoundEvents.hurtSound;
    }
    
    @Override
    protected SoundEvent getDeathSound(){
    	return VoidSoundEvents.EntityMobZolSoundEvents.deathSound;
    }
    
    @Override
    protected float getSoundVolume(){
    	return 0.5F;
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
    	
    }

    @Override
	public ITextComponent getDisplayName() {
		return new TextComponentString("Zol");
	}

    
    
}