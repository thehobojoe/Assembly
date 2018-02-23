package com.hobojoe.assembly.proxy

import com.hobojoe.assembly.assembler.BlockAssembler
import com.hobojoe.assembly.block.ModBlocks
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.util.text.translation.I18n
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Created by Joseph on 11/18/2016.
 */

@Mod.EventBusSubscriber
open class CommonProxy {

    companion object {
        @JvmStatic
        @SubscribeEvent
        fun registerBlocks(event: RegistryEvent.Register<Block>) {
            event.registry.register(BlockAssembler())
        }

        @JvmStatic
        @SubscribeEvent
        fun registerItems(event: RegistryEvent.Register<Item>) {
            event.registry.register(ItemBlock(ModBlocks.assembler).setRegistryName(ModBlocks.assembler.registryName))
        }
    }

    open fun localize(unlocalized: String, vararg args: Any) : String {
        return I18n.translateToLocalFormatted(unlocalized, args)
    }
}
