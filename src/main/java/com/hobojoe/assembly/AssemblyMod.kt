package com.hobojoe.assembly

import com.hobojoe.assembly.proxy.CommonProxy
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry

@Mod(modid = AssemblyMod.MODID, modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object AssemblyMod {

    const val MODID = "assembly"
    const val name = "Assembly"

    @Mod.Instance(MODID)
    lateinit var instance: AssemblyMod

    @SidedProxy(serverSide = "com.hobojoe.assembly.proxy.CommonProxy", clientSide = "com.hobojoe.assembly.proxy.ClientProxy")
    lateinit var proxy: CommonProxy

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        println(name + " is loading!")
        NetworkRegistry.INSTANCE.registerGuiHandler(this, GuiHandler())
        //config stuff
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {}

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        //config stuff
    }


    object NBT {
        val SAVED_INVENTORY_TAG = "Items"
        val MOST_SIG_UUID = "MostSigUUID"
        val LEAST_SIG_UUID = "LeastSigUUID"
    }
}