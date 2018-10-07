package com.hobojoe.assembly.assembler

import com.hobojoe.assembly.AssemblyMod
import com.hobojoe.assembly.block.ModBlocks
import com.hobojoe.assembly.inventory.SlotGhost
import com.hobojoe.assembly.render.GuiRender
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation


class GuiAssembler(container: Container, private var playerInv: InventoryPlayer) :
    GuiContainer(container) {

    private val guiHeight = 250

    companion object {
        private val BG_TEXTURE = ResourceLocation(AssemblyMod.MODID, "textures/gui/assembler.png")
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
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

        for (slot in this.inventorySlots.inventorySlots) {
            if(slot is SlotGhost) {
                this.drawItemStack(slot.stack, mouseX, mouseY)
            }
        }
    }

    private fun drawItemStack(stack: ItemStack, x: Int, y: Int) {
        val grey = 0xff8a8a8a.toInt()
        val transparent = 0x808a8a8a.toInt()
        if(stack != ItemStack.EMPTY) {
            GuiRender.paintOverlay(x, y, 16, grey)
            GuiRender.paintItem(stack, x, y, mc, mc.renderItem, 200f)
            GuiRender.paintOverlay(x, y, 16, transparent)
        }
    }
}