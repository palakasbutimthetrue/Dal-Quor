package tamaized.voidcraft.client.gui;

import tamaized.tammodized.common.helper.PacketHelper;
import tamaized.tammodized.common.helper.PacketHelper.PacketWrapper;
import tamaized.tammodized.common.helper.TranslateHelper;
import tamaized.voidcraft.common.gui.container.StarForgeContainer;
import tamaized.voidcraft.VoidCraft;
import tamaized.voidcraft.common.blocks.tileentity.TileEntityStarForge;
import tamaized.voidcraft.common.capabilities.CapabilityList;
import tamaized.voidcraft.client.gui.element.GUIElementList;
import tamaized.voidcraft.client.gui.element.GUIListElement;
import tamaized.voidcraft.network.ServerPacketHandler;
import tamaized.voidcraft.registry.VoidCraftBlocks;
import tamaized.voidcraft.registry.VoidCraftItems;
import tamaized.voidcraft.common.starforge.StarForgeEffectEntry;
import tamaized.voidcraft.common.starforge.StarForgeToolEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.input.Mouse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class StarForgeGUI extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation(VoidCraft.modid, "textures/gui/starforge.png");

	public TileEntityStarForge te;
	private GUIElementList scroll;
	private ItemStack dirtyStack = ItemStack.EMPTY;
	private ItemStack renderStackHover = ItemStack.EMPTY;

	public int mouseX = 0;
	public int mouseY = 0;

	private GuiButton button_Craft;

	private static final int BUTTON_CRAFT = 0;

	public StarForgeGUI(InventoryPlayer inventoryPlayer, TileEntityStarForge tileEntity) {
		super(new StarForgeContainer(inventoryPlayer, tileEntity));

		this.te = tileEntity;

		this.xSize = 347;
		this.ySize = 320;
	}

	@Override
	public void initGui() {
		super.initGui();
		setupScroll();

		buttonList.clear();
		buttonList.add(button_Craft = new GuiButton(BUTTON_CRAFT, guiLeft + (xSize / 2) + 10, height - 102, 170, 20, TranslateHelper.translate("voidcraft.gui.misc.button.forge")));
	}

	private void setupScroll() {
		// scroll = new GUIElementList(list, mc, 200, height, 32, height - 55 + 4, 36);
		// scroll = new GUIElementList(list, mc, 100, 225, 10, 35, 25);
		// scroll.setSlotXBoundsFromLeft(width - 190);
		// scroll.registerScrollButtons(7, 8);
		// height - 88 + 4
		scroll = new GUIElementList(this, te.buildPossibleEffectList(), guiLeft + (xSize / 2) + 10, 40, 170, height - 110, 22);

	}

	private void updateMouseValues() {
		mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
	}

	@Override
	public void handleMouseInput() throws IOException {
		updateMouseValues();
		scroll.handleMouseInput(mouseX, mouseY);
		super.handleMouseInput();
	}

	@Override
	public void actionPerformed(GuiButton button) {
		int xcoord = te.getPos().getX();
		int ycoord = te.getPos().getY();
		int zcoord = te.getPos().getZ();
		switch (button.id) {
			case BUTTON_CRAFT:
				try {
					PacketWrapper packet = PacketHelper.createPacket(VoidCraft.channel, VoidCraft.networkChannelName, ServerPacketHandler.getPacketTypeID(ServerPacketHandler.PacketType.STARFORGE_CRAFT));
					DataOutputStream stream = packet.getStream();
					stream.writeInt(xcoord);
					stream.writeInt(ycoord);
					stream.writeInt(zcoord);
					stream.writeInt(scroll.getSelectedIndex());
					packet.sendPacketToServer();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		ItemStack stack = te.SLOT_INPUT_TOOL.getStackInSlot(0);
		if (!ItemStack.areItemStacksEqual(dirtyStack, stack) || !ItemStack.areItemStackTagsEqual(dirtyStack, stack)) {
			setupScroll();
			dirtyStack = stack;
		}
		boolean hasSel = scroll.getSelected() != null;
		boolean enough = false;
		if (hasSel) {
			int index = scroll.getSelectedIndex();
			ArrayList<GUIListElement> list = scroll.getElements();
			if (index >= 0 && index < list.size()) {
				GUIListElement element = list.get(index);
				if (element instanceof StarForgeToolEntry) {
					if (te.SLOT_INPUT_TOOL.getStackInSlot(0).isEmpty() && te.SLOT_INPUT_COSMICMATERIAL.getStackInSlot(0).getCount() >= 4 && te.SLOT_INPUT_QUORIFRAGMENT.getStackInSlot(0).getCount() >= 1) {
						enough = true;
					}
				} else if (element instanceof StarForgeEffectEntry) {
					StarForgeEffectEntry entry = (StarForgeEffectEntry) element;
					if (!te.SLOT_INPUT_TOOL.getStackInSlot(0).isEmpty() && te.SLOT_INPUT_TOOL.getStackInSlot(0).hasCapability(CapabilityList.STARFORGE, null)) {
						boolean flag = true;
						for (ItemStack checkStack : entry.getRecipe().getInputs()) {
							ItemStackHandler slot = checkStack.getItem() == Item.getItemFromBlock(VoidCraftBlocks.cosmicMaterial) ? te.SLOT_INPUT_COSMICMATERIAL : checkStack.getItem() == VoidCraftItems.voidicDragonScale ? te.SLOT_INPUT_DRAGONSCALE : checkStack.getItem() == VoidCraftItems.quoriFragment ? te.SLOT_INPUT_QUORIFRAGMENT : checkStack.getItem() == VoidCraftItems.astralEssence ? te.SLOT_INPUT_ASTRALESSENCE : te.SLOT_INPUT_VOIDICPHLOG;
							if (slot.getStackInSlot(0).getCount() >= checkStack.getCount()) continue;
							flag = false;
						}
						if (flag) enough = true;
					}
				}
			}
		}
		button_Craft.enabled = hasSel && enough;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		updateMouseValues();
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.disableLighting();
		scroll.drawScreen(mouseX, mouseY, partialTicks);
		if (!renderStackHover.isEmpty()) {
			renderToolTip(renderStackHover, mouseX, mouseY);
			renderStackHover = ItemStack.EMPTY;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		String text = TranslateHelper.translate("voidcraft.gui.starforge.title");
		this.fontRenderer.drawString(text, this.xSize / 3 - this.fontRenderer.getStringWidth(text) / 2, this.ySize - 260, 0xAAAAAA);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GlStateManager.pushMatrix();
		{
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
			// drawTexturedModalRect(guiLeft + 78, guiTop + 66, 0, 0, xSize / 2, ySize / 2);
			drawTexturedModalRect(guiLeft + 10, guiTop + 66, 0, 0, xSize / 2, ySize / 2);
			this.updateScreen();
		}
		GlStateManager.popMatrix();

	}

	public void renderItemStack(ItemStack stack, int x, int y, int mx, int my) {
		if (itemRender != null) {
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableDepth();
			itemRender.renderItemIntoGUI(stack, x, y);
			// drawCenteredString(fontRenderer, ""+stack.stackSize, x, y, 0xFFFFFF);
			GlStateManager.disableDepth();
			if (stack.getCount() > 0) drawString(fontRenderer, "" + stack.getCount(), x + 11 - (6 * (Integer.valueOf(stack.getCount()).toString().length() - 1)), y + 9, 0xFFFFFF);
			GlStateManager.enableDepth();
			if (mx >= x && mx <= x + 16 && my >= y && my <= y + 16) renderStackHover = stack;
			RenderHelper.disableStandardItemLighting();
		}
	}

}