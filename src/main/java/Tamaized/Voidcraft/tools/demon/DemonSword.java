package Tamaized.Voidcraft.tools.demon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import Tamaized.Voidcraft.tools.BasicVoidItemSword;

public class DemonSword extends BasicVoidItemSword {

	public DemonSword(ToolMaterial material, String n) {
		super(material, n);
	}

	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase){
        par1ItemStack.damageItem(1, par3EntityLivingBase);
        par2EntityLivingBase.addPotionEffect(new PotionEffect(9, 20*20)); //Confuse
        par2EntityLivingBase.addPotionEffect(new PotionEffect(18, 20*20)); //Weakness
        par2EntityLivingBase.addPotionEffect(new PotionEffect(15, 20*20)); //Blind
        par2EntityLivingBase.addPotionEffect(new PotionEffect(19, 20*20)); //Poison
        par2EntityLivingBase.addPotionEffect(new PotionEffect(20, 20*20)); //Wither
        par2EntityLivingBase.setFire(100); //Fire
        return true;
    }
}