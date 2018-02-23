package com.hobojoe.assembly.util

import com.hobojoe.assembly.AssemblyMod
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.item.ItemStack
import java.util.UUID


object NBTHelper {

    //NBT Helper methods

    fun hasUUID(itemStack: ItemStack): Boolean {
        return has_tag(itemStack, AssemblyMod.NBT.MOST_SIG_UUID) && has_tag(
            itemStack,
            AssemblyMod.NBT.LEAST_SIG_UUID
        )
    }

    fun setUUID(itemStack: ItemStack) {
        initNBTCompound(itemStack)

        if (!hasUUID(itemStack)) {
            val itemUUID = UUID.randomUUID()

            setLong(itemStack, AssemblyMod.NBT.MOST_SIG_UUID, itemUUID.mostSignificantBits)
            setLong(itemStack, AssemblyMod.NBT.LEAST_SIG_UUID, itemUUID.leastSignificantBits)
        }
    }

    fun getUUID(itemStack: ItemStack): UUID? {
        initNBTCompound(itemStack)

        return if (hasUUID(itemStack)) {
            UUID(
                itemStack.tagCompound!!.getLong(AssemblyMod.NBT.MOST_SIG_UUID),
                itemStack.tagCompound!!.getLong(AssemblyMod.NBT.LEAST_SIG_UUID)
            )
        } else null
    }

    fun initNBTCompound(itemStack: ItemStack) {
        if (itemStack.tagCompound == null) {
            itemStack.tagCompound = NBTTagCompound()
        }
    }

    fun has_tag(itemStack: ItemStack?, tag: String): Boolean {
        return itemStack != null && !itemStack.isEmpty && itemStack.hasTagCompound() && itemStack.tagCompound!!.hasKey(
            tag
        )
    }

    fun setLong(itemStack: ItemStack, tag: String, value: Long?) {
        initNBTCompound(itemStack)
        val tagCompound = itemStack.tagCompound
        tagCompound!!.setLong(tag, value!!)
    }
}