package com.hobojoe.assembly.assembler

import com.hobojoe.assembly.inventory.InventoryCraftingMatrix
import net.minecraft.inventory.InventoryCraftResult
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler

class TileEntityAssembler : TileEntity() {

    var craftMatrix = InventoryCraftingMatrix()
    var result = InventoryCraftResult()
    var inventory = ItemStackHandler(27)


    override fun writeToNBT(compound: NBTTagCompound?): NBTTagCompound {
        compound?.setTag("inventory", inventory.serializeNBT())
        compound?.setTag("craftMatrix", craftMatrix.writeToNBT())
        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound?) {
        inventory.deserializeNBT(compound?.getCompoundTag("inventory"))
        craftMatrix.readFromNBT(compound?.getCompoundTag("craftMatrix"))
        super.readFromNBT(compound)
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing)
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        return if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            (inventory as T)
        } else super.getCapability(capability, facing)
    }
}