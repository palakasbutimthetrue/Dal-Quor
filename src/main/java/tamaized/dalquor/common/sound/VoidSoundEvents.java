package tamaized.dalquor.common.sound;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tamaized.dalquor.DalQuor;

@Mod.EventBusSubscriber
public class VoidSoundEvents {

	public static class MiscSoundEvents {
		public static SoundEvent chain = null;
	}

	public static class ArmorSoundEvents {
		public static SoundEvent voidcrystal = SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
		public static SoundEvent xia = SoundEvents.ITEM_ARMOR_EQUIP_GOLD;
	}

	public static class MusicSoundEvents {
		public static SoundEvent mcMusic_end = SoundEvents.MUSIC_END;
		public static SoundEvent fleshmaker = null;
		public static SoundEvent gop1 = null;
		public static SoundEvent gop2 = null;
		public static SoundEvent inferno = null;
		public static SoundEvent darkness = null;
		public static SoundEvent deathwyrm = null;
		public static SoundEvent titan = null;
		public static SoundEvent crystalcove = null;
	}

	public static class EntityMobDolSoundEvents {
		public static SoundEvent hurtSound = null;
		public static SoundEvent deathSound = null;
		public static SoundEvent ambientSound = null;
	}

	public static class EntityMobZolSoundEvents {
		public static SoundEvent hurtSound = null;
		public static SoundEvent deathSound = null;
		public static SoundEvent ambientSound = null;
	}

	public static class EntityMobHerobrineSoundEvents {
		public static SoundEvent hurtSound = null;
		public static SoundEvent deathSound = null;
		public static SoundEvent ambientSound = null;
	}

	public static class EntityMobVoidBossSoundEvents {
		public static SoundEvent hurtSound = SoundEvents.ENTITY_WITHER_HURT;
		public static SoundEvent deathSound = SoundEvents.ENTITY_WITHER_DEATH;
		public static SoundEvent ambientSound = SoundEvents.ENTITY_WITHER_AMBIENT;
	}

	public static class EntityMobWraithSoundEvents {
		public static SoundEvent hurtSound = null;
		public static SoundEvent deathSound = null;
		public static SoundEvent ambientSound = null;
	}

	public static class EntityMobLichSoundEvents {
		public static SoundEvent hurtSound = null;
		public static SoundEvent deathSound = null;
		public static SoundEvent ambientSound = null;
	}

	public static class EntityMobSpectreChainSoundEvents {
		public static SoundEvent hurtSound = null;
		public static SoundEvent deathSound = null;
		public static SoundEvent ambientSound = null;
	}

	public static class EntityMobWrathSoundEvents {
		public static SoundEvent hurtSound = SoundEvents.ENTITY_BLAZE_HURT;
		public static SoundEvent deathSound = SoundEvents.ENTITY_BLAZE_DEATH;
		public static SoundEvent ambientSound = SoundEvents.ENTITY_BLAZE_AMBIENT;
	}

	public static class EntityMobXiaSoundEvents {
		public static SoundEvent hurtSound = null;
		public static SoundEvent deathSound = null;
		public static SoundEvent ambientSound = null;
	}

	public static class EntityMobXia2SoundEvents {
		public static SoundEvent hurtSound = null;
		public static SoundEvent deathSound = null;
		public static SoundEvent ambientSound = null;
	}

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		EntityMobWraithSoundEvents.hurtSound = registerSound(event, "wraith.wraithHurt");
		EntityMobWraithSoundEvents.deathSound = registerSound(event, "wraith.wraithDeath");
		EntityMobWraithSoundEvents.ambientSound = registerSound(event, "wraith.wraithLive");

		EntityMobLichSoundEvents.hurtSound = EntityMobWraithSoundEvents.hurtSound;
		EntityMobLichSoundEvents.deathSound = registerSound(event, "lich.lichDeath");
		EntityMobLichSoundEvents.ambientSound = registerSound(event, "lich.lichLive");

		EntityMobSpectreChainSoundEvents.hurtSound = EntityMobWraithSoundEvents.hurtSound;
		EntityMobSpectreChainSoundEvents.deathSound = EntityMobWraithSoundEvents.deathSound;
		EntityMobSpectreChainSoundEvents.ambientSound = registerSound(event, "spectrechain.chainLive");

		MiscSoundEvents.chain = registerSound(event, "random.chain");

		MusicSoundEvents.fleshmaker = registerSound(event, "music.fleshmaker");
		MusicSoundEvents.gop1 = registerSound(event, "music.gop1");
		MusicSoundEvents.gop2 = registerSound(event, "music.gop2");
		MusicSoundEvents.inferno = registerSound(event, "music.inferno");
		MusicSoundEvents.darkness = registerSound(event, "music.darkness");
		MusicSoundEvents.deathwyrm = registerSound(event, "music.deathwyrm");
		MusicSoundEvents.titan = registerSound(event, "music.titan");
		MusicSoundEvents.crystalcove = registerSound(event, "music.crystalcove");
	}

	private static SoundEvent registerSound(RegistryEvent.Register<SoundEvent> event, String soundName) {
		SoundEvent sound = new SoundEvent(new ResourceLocation(DalQuor.modid, soundName)).setRegistryName(soundName);
		event.getRegistry().register(sound);
		return sound;
	}

}
