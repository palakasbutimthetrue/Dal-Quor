package tamaized.voidcraft.client.blocks.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tamaized.voidcraft.VoidCraft;
import tamaized.voidcraft.client.blocks.model.ModelVoidicCharger;
import tamaized.voidcraft.client.event.ClientRenderTicker;
import tamaized.voidcraft.common.machina.tileentity.TileEntityVoidicCharger;

public class RenderVoidicCharger extends TileEntitySpecialRenderer<TileEntityVoidicCharger> {

	private static final ResourceLocation texture = new ResourceLocation(VoidCraft.modid, "textures/models/blocks/voidiccharger.png");

	private final ModelVoidicCharger model;

	public RenderVoidicCharger() {
		this.model = new ModelVoidicCharger();
	}

	@Override
	public void render(TileEntityVoidicCharger te, double x, double y, double z, float partialTicks, int destroyStage, float p_192841_10_) {
		GL11.glPushMatrix();
		GlStateManager.disableLighting();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		ResourceLocation textures = (texture);
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		GL11.glPushMatrix();
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		float f = (float) ClientRenderTicker.tick + partialTicks;
		this.model.render((Entity) null, 0.0F, f * 1.0f, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
		GlStateManager.enableLighting();
		GL11.glPopMatrix();
	}

}