package com.hobojoe.assembly.assembler

import com.hobojoe.assembly.AssemblyMod
import com.hobojoe.assembly.block.ModBlocks
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation

class GuiAssembler(container: Container, private var playerInv: InventoryPlayer) :
    GuiContainer(container) {

    private val guiHeight = 250

    companion object {
        private val BG_TEXTURE = ResourceLocation(AssemblyMod.MODID, "textures/gui/assembler.png")
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1f, 1f, 1f, 1f)
        mc.textureManager.bindTexture(BG_TEXTURE)

        val x = (width - xSize) / 2
        val y = (height - guiHeight) / 2
        drawTexturedModalRect(x, y, 0, 0, xSize, guiHeight)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        val name = I18n.format(ModBlocks.assembler.unlocalizedName + ".name")
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, ((ySize-guiHeight) / 2) + 4, 0x404040)
        fontRenderer.drawString(playerInv.displayName.unformattedText, 8, ((ySize-guiHeight) / 2) + 158, 0x404040)
    }
}