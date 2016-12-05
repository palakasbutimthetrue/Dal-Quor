package Tamaized.Voidcraft.entity.boss.xia.finalphase.render;

import Tamaized.Voidcraft.voidCraft;
import Tamaized.Voidcraft.entity.boss.xia.finalphase.EntityWitherbrine;
import Tamaized.Voidcraft.entity.boss.xia.finalphase.render.layer.LayerWitherbrineAura;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderWitherbrine extends RenderLiving<EntityWitherbrine> {

	private static final ResourceLocation INVULNERABLE_WITHER_TEXTURES = new ResourceLocation(voidCraft.modid, "textures/entity/witherbrine/wither_invulnerable.png");
	private static final ResourceLocation WITHER_TEXTURES = new ResourceLocation(voidCraft.modid, "textures/entity/witherbrine/wither.png");

	public RenderWitherbrine(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelWitherbrine(0.0F), 1.0F);
		this.addLayer(new LayerWitherbrineAura(this));
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityWitherbrine entity) {
		int i = entity.getInvulTime();
		return i > 0 && (i > 80 || i / 5 % 2 != 1) ? INVULNERABLE_WITHER_TEXTURES : WITHER_TEXTURES;
	}

	/**
	 * Allows the render to do state modifications necessary before the model is rendered.
	 */
	protected void preRenderCallback(EntityWitherbrine entitylivingbaseIn, float partialTickTime) {
		float f = 2.0F;
		int i = entitylivingbaseIn.getInvulTime();

		if (i > 0) {
			f -= ((float) i - partialTickTime) / 220.0F * 0.5F;
		}

		GlStateManager.scale(f, f, f);
	}
}