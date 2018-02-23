package com.hobojoe.assembly.plugins

import com.hobojoe.assembly.inventory.ContainerAssembler
import mezz.jei.api.recipe.VanillaRecipeCategoryUid
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo
import net.minecraft.inventory.Slot

class AssemblerRecipeTransferInfo : IRecipeTransferInfo<ContainerAssembler> {

    override fun getContainerClass(): Class<ContainerAssembler> {
        return ContainerAssembler::class.java
    }

    override fun canHandle(container: ContainerAssembler?): Boolean {
        return true
    }

    override fun getRecipeSlots(container: ContainerAssembler?): MutableList<Slot> {
        val slots = ArrayList<Slot>()
        for (i in 1..9) {
            slots.add(container!!.getSlot(i))
        }
        return slots
    }

    override fun getInventorySlots(container: ContainerAssembler?): MutableList<Slot> {
        val slots = ArrayList<Slot>()
        for (i in 10..72) {
            slots.add(container!!.getSlot(i))
        }
        return slots
    }

    override fun getRecipeCategoryUid(): String {
        return VanillaRecipeCategoryUid.CRAFTING
    }


}