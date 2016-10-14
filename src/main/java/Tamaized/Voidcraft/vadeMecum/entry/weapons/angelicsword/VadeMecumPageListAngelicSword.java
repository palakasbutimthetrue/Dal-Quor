package Tamaized.Voidcraft.vadeMecum.entry.weapons.angelicsword;

import Tamaized.Voidcraft.vadeMecum.IVadeMecumPage;
import Tamaized.Voidcraft.vadeMecum.entry.weapons.angelicsword.pages.VadeMecumPageAngelicSword1;
import Tamaized.Voidcraft.vadeMecum.entry.weapons.angelicsword.pages.VadeMecumPageAngelicSword2;

public class VadeMecumPageListAngelicSword {

	public static final IVadeMecumPage[] getPageList() {
		return new IVadeMecumPage[] {
				new VadeMecumPageAngelicSword1(),
				new VadeMecumPageAngelicSword2() };
	}

}