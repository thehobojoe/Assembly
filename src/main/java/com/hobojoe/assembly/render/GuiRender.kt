package com.hobojoe.assembly.render

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.RenderItem
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12


object GuiRender {


    @SideOnly(Side.CLIENT)
    fun paintOverlay(x: Int, y: Int, size: Int, color: Int) {
        RenderHelper.enableGUIStandardItemLighting()
        RenderHelper.disableStandardItemLighting()
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        Gui.drawRect(x, y, x + size, y + size, color)
        RenderHelper.disableStandardItemLighting()
        GL11.glEnable(GL11.GL_DEPTH_TEST)
    }

    fun paintItem(itemStack: ItemStack?, x: Int, y: Int,
                  mc: Minecraft, itemRenderer: RenderItem, zLevel: Float) {
        if (itemStack == null)
            return  // I might want to have a "null" image, like background
        // image.
        GL11.glPushMatrix()
        itemRenderer.zLevel = zLevel
        GL11.glEnable(GL12.GL_RESCALE_NORMAL)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        RenderHelper.enableStandardItemLighting()
        RenderHelper.enableGUIStandardItemLighting() // Fixes funny lighting

        itemRenderer.renderItemAndEffectIntoGUI(null, itemStack, x, y)
        itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, x, y, "")
        //RenderHelper.disableStandardItemLighting();
        // GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        // GL11.glDisable(GL11.GL_DEPTH_TEST);
        itemRenderer.zLevel = 0.0f
        GL11.glPopMatrix()
    }

    @SideOnly(Side.CLIENT)
    fun getMouseX(minecraft: Minecraft): Int {
        val resolution = ScaledResolution(minecraft)
        val width = resolution.scaledWidth
        return Mouse.getX() * width / minecraft.displayWidth
    }

    @SideOnly(Side.CLIENT)
    fun getMouseY(minecraft: Minecraft): Int {
        val resolution = ScaledResolution(minecraft)
        val height = resolution.scaledHeight
        return height - Mouse.getY() * height / minecraft.displayHeight - 1
    }

    @SideOnly(Side.CLIENT)
    fun getHoveredSlot(guiLeft: Int, guiTop: Int): Slot? {
        val gui = Minecraft.getMinecraft().currentScreen as GuiContainer? ?: return null

        val container = Minecraft.getMinecraft().player.openContainer
        val mouseX = getMouseX(Minecraft.getMinecraft()) - guiLeft
        val mouseY = getMouseY(Minecraft.getMinecraft()) - guiTop

        return getHoveredSlot(container, mouseX, mouseY, guiLeft, guiTop)
    }

    fun getHoveredSlot(container: Container, mouseX: Int,
                       mouseY: Int, guiLeft: Int, guiTop: Int): Slot? {
        // Utils.debug("Getting slot at: (%s, %s)", mouseX, mouseY);
        for (i in 0 until container.inventorySlots.size) {
            val slot = container.getSlot(i)
            if (isMouseOverSlot(slot, mouseX - guiLeft, mouseY - guiTop)) {
                return slot
            }

        }
        return null
    }

    fun isMouseOverSlot(slot: Slot?, mouseX: Int, mouseY: Int): Boolean {
        if (slot == null)
            return false
        val xMin = slot.xPos
        val yMin = slot.yPos
        return (mouseX >= xMin - 1 && mouseX < xMin + 16 + 1
                && mouseY >= yMin - 1 && mouseY < yMin + 16 + 1)
    }
}