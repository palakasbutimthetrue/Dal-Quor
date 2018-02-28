package tamaized.voidcraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamaized.voidcraft.VoidCraft;
import tamaized.voidcraft.common.gui.container.VoidInfuserContainer;
import tamaized.voidcraft.common.machina.tileentity.TileEntityVoidInfuser;

@SideOnly(Side.CLIENT)
public class VoidInfuserGUI extends GuiContainer {

	public TileEntityVoidInfuser te;

	private static final ResourceLocation TEXTURE = new ResourceLocation(VoidCraft.modid, "textures/gui/voidinfuser.png");

	public VoidInfuserGUI(InventoryPlayer inventoryPlayer, TileEntityVoidInfuser tileEntity) {
		super(new VoidInfuserContainer(inventoryPlayer, tileEntity));
		te = tileEntity;
		xSize = 347;
		ySize = 320;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	public void updateScreen() {

		{
			float scale = 46;
			int k = (int) (((float) te.getFluidAmount() / (float) te.getMaxFluidAmount()) * scale);
			drawTexturedModalRect(guiLeft + 93, guiTop + 131 - k, 0, 497 - (k), 12, k + 1);
		}

		{
			float scale = 26;
			int k = (int) (((float) te.cookingTick / (float) te.finishTick) * scale);
			drawTexturedModalRect(guiLeft + 188, guiTop + 102, 0, 435, k, 15);
		}

		super.updateScreen();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		String text = I18n.format("voidcraft.gui.infuser.title");
		fontRenderer.drawString(text, xSize / 2 - fontRenderer.getStringWidth(text) / 2, ySize - 260, 0xAAAAAA);
		text = I18n.format("voidcraft.gui.misc.fluid") + ":";
		fontRenderer.drawString(text, (xSize / 2 - fontRenderer.getStringWidth(text) / 2) - 100, ySize / 2 - 65, 0x7700FF);
		text = te.getFluidAmount() + "/";
		fontRenderer.drawString(text, (xSize / 2 - fontRenderer.getStringWidth(text)) - 85, ySize / 2 - 55, 0x7700FF);
		text = "" + te.getMaxFluidAmount();
		fontRenderer.drawString(text, (xSize / 2 - fontRenderer.getStringWidth(text)) - 85, ySize / 2 - 45, 0x7700FF);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft + 78, guiTop + 66, 0, 0, xSize / 2, ySize / 2);
		updateScreen();
		GlStateManager.popMatrix();
	}
}