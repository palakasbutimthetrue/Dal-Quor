package Tamaized.Voidcraft.registery;

import cpw.mods.fml.common.registry.GameRegistry;
import Tamaized.Voidcraft.common.voidCraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import Tamaized.Voidcraft.tools.VoidAxe;
import Tamaized.Voidcraft.tools.VoidHoe;
import Tamaized.Voidcraft.tools.VoidPickaxe;
import Tamaized.Voidcraft.tools.VoidSpade;
import Tamaized.Voidcraft.tools.VoidSword;
import Tamaized.Voidcraft.tools.angelicSword;
import Tamaized.Voidcraft.tools.archSword;
import Tamaized.Voidcraft.tools.chainSword;
import Tamaized.Voidcraft.tools.demonSword;
import Tamaized.Voidcraft.tools.moltenSword;
import Tamaized.Voidcraft.tools.spectreAxe;
import Tamaized.Voidcraft.tools.spectrePickaxe;

public class Tools extends RegistryBase {
	
	public static Item voidPickaxe;
	public static Item voidSpade;
	public static Item voidAxe;
	public static Item voidSword;
	public static Item voidHoe;
	public static Item angelicSword;
	public static Item chainSword;
	public static Item moltenSword;
	public static Item archSword;
	public static Item demonSword;
	public static Item spectrePickaxe;
	public static Item spectreAxe;

	@Override
	public void preInit() {
		// Tools
		// void
		voidPickaxe = new VoidPickaxe(voidCraft.materials.voidTools).setCreativeTab(voidCraft.tabs.tabVoid).setUnlocalizedName("voidPickaxe").setTextureName("VoidCraft:voidcrystal_Pickaxe");
		voidSpade = new VoidSpade(voidCraft.materials.voidTools).setCreativeTab(voidCraft.tabs.tabVoid).setUnlocalizedName("voidShovel").setTextureName("VoidCraft:voidcrystal_Shovel");
		voidAxe = new VoidAxe(voidCraft.materials.voidTools).setCreativeTab(voidCraft.tabs.tabVoid).setUnlocalizedName("voidAxe").setTextureName("VoidCraft:voidcrystal_Axe");
		voidSword = new VoidSword(voidCraft.materials.voidTools).setCreativeTab(voidCraft.tabs.tabVoid).setUnlocalizedName("voidSword").setTextureName("VoidCraft:voidcrystal_Sword");
		voidHoe = new VoidHoe(voidCraft.materials.voidTools).setCreativeTab(voidCraft.tabs.tabVoid).setUnlocalizedName("voidHoe").setTextureName("VoidCraft:voidcrystal_Hoe");

		// spectre
		angelicSword = new angelicSword(voidCraft.materials.spectreTools).setCreativeTab(voidCraft.tabs.tabVoid).setUnlocalizedName("angelicSword").setTextureName("VoidCraft:angelicSword");
		spectrePickaxe = new spectrePickaxe(voidCraft.materials.spectreTools).setCreativeTab(voidCraft.tabs.tabVoid).setUnlocalizedName("spectrePickaxe").setTextureName("VoidCraft:spectre_Pickaxe");
		spectreAxe = new spectreAxe(voidCraft.materials.spectreTools).setCreativeTab(voidCraft.tabs.tabVoid).setUnlocalizedName("spectreAxe").setTextureName("VoidCraft:spectre_Axe");
		// chain
		chainSword = new chainSword(voidCraft.materials.chainTools).setCreativeTab(voidCraft.tabs.tabVoid).setUnlocalizedName("chainSword").setTextureName("VoidCraft:bindSword");
		// molten
		moltenSword = new moltenSword(voidCraft.materials.MoltenTools).setCreativeTab(voidCraft.tabs.tabVoid).setUnlocalizedName("moltenSword").setTextureName("VoidCraft:moltenVoidSword");
		// arch
		archSword = new archSword(voidCraft.materials.ArchTools).setCreativeTab(voidCraft.tabs.tabVoid).setUnlocalizedName("archSword").setTextureName("VoidCraft:archAngelicSword");
		// demon
		demonSword = new demonSword(voidCraft.materials.DemonTools).setCreativeTab(voidCraft.tabs.tabVoid).setUnlocalizedName("demonSword").setTextureName("VoidCraft:demonSword");
	}

	@Override
	public void init() {
		GameRegistry.registerItem(voidSword, voidSword.getUnlocalizedName());
		GameRegistry.registerItem(voidPickaxe, voidPickaxe.getUnlocalizedName());
		GameRegistry.registerItem(voidAxe, voidAxe.getUnlocalizedName());
		GameRegistry.registerItem(voidSpade, voidSpade.getUnlocalizedName());
		GameRegistry.registerItem(voidHoe, voidHoe.getUnlocalizedName());
		GameRegistry.registerItem(spectrePickaxe, spectrePickaxe.getUnlocalizedName());
		GameRegistry.registerItem(spectreAxe, spectreAxe.getUnlocalizedName());
		GameRegistry.registerItem(angelicSword, angelicSword.getUnlocalizedName());
		GameRegistry.registerItem(chainSword, chainSword.getUnlocalizedName());
		GameRegistry.registerItem(moltenSword, moltenSword.getUnlocalizedName());
		GameRegistry.registerItem(archSword, archSword.getUnlocalizedName());
		GameRegistry.registerItem(demonSword, demonSword.getUnlocalizedName());

		GameRegistry.addRecipe(new ItemStack(voidPickaxe, 1), "XXX", " S ", " S ", 'X', voidCraft.items.voidcrystal, 'S', Items.stick);
		GameRegistry.addRecipe(new ItemStack(voidAxe, 1), " XX", " SX", " S ", 'X', voidCraft.items.voidcrystal, 'S', Items.stick);
		GameRegistry.addRecipe(new ItemStack(voidSpade, 1), " X ", " S ", " S ", 'X', voidCraft.items.voidcrystal, 'S', Items.stick);
		GameRegistry.addRecipe(new ItemStack(voidHoe, 1), " XX", " S ", " S ", 'X', voidCraft.items.voidcrystal, 'S', Items.stick);
		GameRegistry.addRecipe(new ItemStack(voidSword, 1), " X ", " X ", " S ", 'X', voidCraft.items.voidcrystal, 'S', Items.stick);
		// +spectre
		GameRegistry.addRecipe(new ItemStack(spectrePickaxe, 1), "XXX", "XSX", "XXX", 'S', new ItemStack(voidPickaxe, 1, voidCraft.WILDCARD_VALUE), 'X', voidCraft.items.ectoplasm);
		GameRegistry.addRecipe(new ItemStack(spectreAxe, 1), "XXX", "XSX", "XXX", 'S', new ItemStack(voidAxe, 1, voidCraft.WILDCARD_VALUE), 'X', voidCraft.items.ectoplasm);
		GameRegistry.addRecipe(new ItemStack(angelicSword, 1), "XXX", "XSX", "XXX", 'S', new ItemStack(voidSword, 1, voidCraft.WILDCARD_VALUE), 'X', voidCraft.items.ectoplasm);
		// +molten and beyond
		GameRegistry.addRecipe(new ItemStack(moltenSword), "XXX", "XSX", "XXX", 'X', voidCraft.items.MoltenvoidChain, 'S', new ItemStack(chainSword, 1, voidCraft.WILDCARD_VALUE));
		GameRegistry.addRecipe(new ItemStack(chainSword), "XXX", "XSX", "XXX", 'X', voidCraft.items.voidChain, 'S', new ItemStack(voidSword, 1, voidCraft.WILDCARD_VALUE));
		GameRegistry.addRecipe(new ItemStack(archSword), "SSS", "XYZ", "SSS", 'S', voidCraft.items.MoltenvoidChain, 'X', new ItemStack(chainSword, 1, voidCraft.WILDCARD_VALUE), 'Y', new ItemStack(angelicSword, 1, voidCraft.WILDCARD_VALUE), 'Z', new ItemStack(moltenSword, 1, voidCraft.WILDCARD_VALUE));
	}

	@Override
	public void postInit() {
		// TODO Auto-generated method stub

	}

}