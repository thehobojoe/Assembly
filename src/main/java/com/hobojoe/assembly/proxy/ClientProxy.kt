package com.hobojoe.assembly.proxy


import com.hobojoe.assembly.block.ModBlocks
import net.minecraft.client.resources.I18n
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Created by Joseph on 11/18/2016.
 */

@Mod.EventBusSubscriber
class ClientProxy : CommonProxy() {

    companion object {
        @JvmStatic
        @SubscribeEvent
        fun registerModels(event: ModelRegistryEvent) {
            ModBlocks.initModels()
        }
    }

    override fun localize(unlocalized: String, vararg args: Any) : String {
        return I18n.format(unlocalized, args)
    }
}
