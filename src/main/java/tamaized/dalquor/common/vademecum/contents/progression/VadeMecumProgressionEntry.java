package tamaized.dalquor.common.vademecum.contents.progression;

import net.minecraft.item.ItemStack;
import tamaized.dalquor.client.gui.VadeMecumGUI;
import tamaized.dalquor.common.capabilities.CapabilityList;
import tamaized.dalquor.common.vademecum.VadeMecumEntry;
import tamaized.dalquor.proxy.ClientProxy;
import tamaized.dalquor.registry.ModItems;

public class VadeMecumProgressionEntry extends VadeMecumEntry {

	public enum Entry {
		RitualBlocks, RitualList, Tome, InfusionControl, Potions, Voice, VoidicControl, ImprovedCasting, Empowerment, Tolerance, TotalControl, Dreams
	}

	public static int getEntryID(Entry e) {
		return e.ordinal();
	}

	public static Entry getEntryFromID(int id) {
		return id >= Entry.values().length ? null : Entry.values()[id];
	}

	private final ItemStack activeVade;

	public VadeMecumProgressionEntry() {
		super("progressionMainEntry", "dalquor.VadeMecum.title.progression", null, null);
		activeVade = new ItemStack(ModItems.vadeMecum);
		activeVade.getCapability(CapabilityList.VADEMECUMITEM, null).setBookState(true);
	}

	@Override
	public void init(VadeMecumGUI gui) { // TODO
		clearButtons();
		/*addButton(gui, getEntryID(Entry.RitualBlocks), "dalquor.VadeMecum.progression.title.ritualBlock", new ItemStack(Item.getItemFromBlock(VoidCraft.blocks.ritualBlock)));
		if (gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.INTRO)) {
			addButton(gui, getEntryID(Entry.RitualList), "dalquor.VadeMecum.progression.title.ritualList", new ItemStack(VoidCraft.blocks.ritualBlock));
			if (gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.TOME)) {
				addButton(gui, getEntryID(Entry.Tome), "dalquor.VadeMecum.progression.title.tome", activeVade);
			}
		}
		if (gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.VoidicControl)) {
			addButton(gui, getEntryID(Entry.InfusionControl), "dalquor.VadeMecum.progression.title.infusionControl", new ItemStack(VoidCraft.items.voidcrystal));
		}
		if (gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.Flame) ||

				gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.Freeze) ||

				gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.Shock) ||

				gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.AcidSpray) ||

				gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.Implosion)) {
			addButton(gui, getEntryID(Entry.Potions), "dalquor.VadeMecum.progression.title.potions", new ItemStack(VoidCraft.items.obsidianFlaskFire));
		}
		if (gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.Voice)) {
			if (!gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.VoidicControl)) {
				addButton(gui, getEntryID(Entry.VoidicControl), "dalquor.VadeMecum.progression.title.voidicControl", new ItemStack(VoidCraft.items.voidcrystal));
			}
			if (!gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.ImprovedCasting)) {
				addButton(gui, getEntryID(Entry.ImprovedCasting), "dalquor.VadeMecum.progression.title.improvedCasting", new ItemStack(VoidCraft.items.voidCloth));
			} else {
				if (gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.VoidicControl)) {
					if (!gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.Empowerment)) {
						addButton(gui, getEntryID(Entry.Empowerment), "dalquor.VadeMecum.progression.title.empowerment", new ItemStack(VoidCraft.items.etherealFruit_redstone));
					} else {
						if (!gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.Tolerance)) {
							addButton(gui, getEntryID(Entry.Tolerance), "dalquor.VadeMecum.progression.title.tolerance", new ItemStack(VoidCraft.items.etherealFruit_gold));
						} else {
							if (!gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.TotalControl)) {
								addButton(gui, getEntryID(Entry.TotalControl), "dalquor.VadeMecum.progression.title.totalcontrol", activeVade);
							} else {
								if (!gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.Dreams)) {
									addButton(gui, getEntryID(Entry.Dreams), "dalquor.VadeMecum.progression.title.dreams", new ItemStack(VoidCraft.items.quoriFragment));
								}
							}
						}
					}
				}
			}
		} else {
			if (gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.Flame)) {
				if (gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.Freeze)) {
					if (gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.AcidSpray)) {
						if (gui.getPlayerStats().hasCategory(IVadeMecumCapability.Category.Shock)) {
							addButton(gui, getEntryID(Entry.Voice), "dalquor.VadeMecum.progression.title.voice", activeVade);
						}
					}
				}
			}
		}*/
	}

	@Override
	protected void actionPerformed(VadeMecumGUI gui, int id, int mouseButton) {
		switch (getEntryFromID(id)) {
			case RitualBlocks:
				gui.changeEntry(ClientProxy.vadeMecumEntryList.Progression.RITUALBLOCKS);
				break;
			case RitualList:
				gui.changeEntry(ClientProxy.vadeMecumEntryList.Progression.RITUALLIST);
				break;
			case Tome:
				gui.changeEntry(ClientProxy.vadeMecumEntryList.Progression.TOME);
				break;
			case InfusionControl:
				gui.changeEntry(ClientProxy.vadeMecumEntryList.Progression.INFUSIONCONTROL);
				break;
			case Potions:
				gui.changeEntry(ClientProxy.vadeMecumEntryList.Progression.POTIONS);
				break;
			case Voice:
				gui.changeEntry(ClientProxy.vadeMecumEntryList.Progression.VOICE);
				break;
			case VoidicControl:
				gui.changeEntry(ClientProxy.vadeMecumEntryList.Progression.VOIDICCONTROL);
				break;
			case ImprovedCasting:
				gui.changeEntry(ClientProxy.vadeMecumEntryList.Progression.IMPROVEDCASTING);
				break;
			case Empowerment:
				gui.changeEntry(ClientProxy.vadeMecumEntryList.Progression.EMPOWERMENT);
				break;
			case Tolerance:
				gui.changeEntry(ClientProxy.vadeMecumEntryList.Progression.TOLERANCE);
				break;
			case TotalControl:
				gui.changeEntry(ClientProxy.vadeMecumEntryList.Progression.TOTALCONTROL);
				break;
			case Dreams:
				gui.changeEntry(ClientProxy.vadeMecumEntryList.Progression.DREAMS);
				break;
			default:
				gui.changeEntry(ClientProxy.vadeMecumEntryList);
				break;
		}
		// gui.sendPacketUpdates(VadeMecumPacketHandler.RequestType.ACTIVE_CURRENT_SET, IVadeMecumCapability.getActivePowerID(IVadeMecumCapability.ActivePower.TEST));
		// gui.sendPacketUpdates(VadeMecumPacketHandler.RequestType.ACTIVE_CURRENT_SET, -1);
	}
}
