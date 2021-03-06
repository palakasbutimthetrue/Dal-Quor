package tamaized.dalquor.common.world.dim.dalquor;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamaized.dalquor.common.entity.boss.dragon.EntityVoidicDragon;
import tamaized.dalquor.common.entity.mob.dalquor.EntityHashalaq;

import java.util.ArrayList;
import java.util.List;

public class BiomeGenDream extends Biome {

	public static final List<Class<? extends EntityLiving>> allowedEntities = new ArrayList<Class<? extends EntityLiving>>();

	public BiomeGenDream(BiomeProperties prop, SpawnListEntry... list) {
		super(prop);

		spawnableMonsterList.clear();
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCaveCreatureList.clear();

		for (SpawnListEntry entry : list)
			addEntry(entry);

		addEntry(new SpawnListEntry(EntityVoidicDragon.class, 10, 0, 1));
		addEntry(new SpawnListEntry(EntityHashalaq.class, 25, 0, 1));

	}

	private void addEntry(SpawnListEntry entry) {
		if (!allowedEntities.contains(entry.entityClass))
			allowedEntities.add(entry.entityClass);
		spawnableMonsterList.add(entry);
	}

	@SideOnly(Side.CLIENT)
	public int getSkyColorByTemp(float par1) {
		return 0x7700FF;
	}

	@Override
	public boolean canRain() {
		return true;
	}

}
