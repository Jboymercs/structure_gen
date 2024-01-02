package com.example.structure.gui.book;

import com.example.structure.init.ModItems;
import com.example.structure.util.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBook extends GuiScreen {
    protected static final int X = 276;
    protected static final int Y = 180;
    private int currentPage;
    private int prevPage = -1;
    private final int totalPages = 7;
    private static final ResourceLocation GUI_BASE_BOOK_BACKGROUND = new ResourceLocation(ModReference.MOD_ID + ":textures/gui/book_default.png");
    private static final ResourceLocation GUI_INDEX_BUTTON = new ResourceLocation(ModReference.MOD_ID + ":textures/gui/button.png");
    private static final ResourceLocation MOB_PICTURES = new ResourceLocation(ModReference.MOD_ID + ":textures/gui/cards.png");

    ItemStack book;
    GuiButton exitButton;
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPrevPage;
    private IndexButtonTest indexButtons;
    private IndexButtonTest indexButtons2;
    private IndexButtonTest indexButtons3;
    private IndexButtonTest indexButtons4;
    private IndexButtonTest indexButtons5;
    private IndexButtonTest indexButtons6;

    GuiBook self;
    private float time = 0;

    public GuiBook(ItemStack book) {
        this.book = book;
        self = this;
    }

    public void initGui() {
        super.initGui();
        this.exitButton = this.addButton(new GuiButton(0, (this.width ) / 2 - 49, 196, 98, 20, I18n.format("gui.done")));
        int t = (this.width - X) / 2;
        this.buttonNextPage = this.addButton(new NextPageButton(1, t + 240, 154, true));
        this.buttonPrevPage = this.addButton(new NextPageButton(2, t + 12, 154, false));
        this.indexButtons = this.addButton(new IndexButtonTest(3, t + 151, 18, 1));
        this.indexButtons2 = this.addButton(new IndexButtonTest(4, t + 151, 38, 2));
        this.indexButtons3 = this.addButton(new IndexButtonTest(5, t + 151, 58, 3));
        this.indexButtons4 = this.addButton(new IndexButtonTest(6, t + 151, 78, 4));
        this.indexButtons5 = this.addButton(new IndexButtonTest(7, t + 151, 98, 5));
        this.indexButtons6 = this.addButton(new IndexButtonTest(8, t + 151, 118, 6));

        this.updateButtons();
    }


    protected void drawGuiContainerForegroundLayer(int x, int y) {

        for (GuiButton guibutton : this.buttonList) {
            if (guibutton.isMouseOver()) {
                guibutton.drawButtonForegroundLayer(x - this.width, y - this.height);
                break;
            }
        }
    }


    public void drawScreen(int mouse_x, int mouse_y, float p_ticks){
        this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GUI_BASE_BOOK_BACKGROUND);
        int cornerX = (this.width - X) / 2;
        int cornerY = (this.height - Y) / 2;
        drawModalRectWithCustomSizedTexture((this.width - X) / 2, 2, 0, 0, 276, 180,  288, 224);
        this.drawPages(this.currentPage);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        if (this.prevPage != this.currentPage) {
            //this.cachedComponents = Lists.newArrayList(textcomponentstring);
            this.prevPage = this.currentPage;
        }
        super.drawScreen(mouse_x, mouse_y, p_ticks);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate(cornerX, cornerY, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        short short1 = 240;
        short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1, short2);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        this.drawGuiContainerForegroundLayer(mouse_x, mouse_y);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();

    }

    private void updateButtons() {
        this.buttonNextPage.visible = this.currentPage < this.totalPages - 1;
        this.buttonPrevPage.visible = this.currentPage > 0;
        this.indexButtons.visible = this.currentPage == 0;
        this.indexButtons2.visible = this.currentPage == 0;
        this.indexButtons3.visible = this.currentPage == 0;
        this.indexButtons4.visible = this.currentPage == 0;
        this.indexButtons5.visible = this.currentPage == 0;
        this.indexButtons6.visible = this.currentPage == 0;

    }


    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                this.mc.displayGuiScreen(null);
            }
            else if (button.id == 1)
            {
                if (this.currentPage < this.totalPages - 1)
                {
                    ++this.currentPage;
                }
            }
            else if (button.id == 2)
            {
                if (this.currentPage > 0)
                {
                    --this.currentPage;
                }
            } else if (button.id >= 3 && button.id < 70){
                IndexButtonTest button1 = (IndexButtonTest) button;
                this.currentPage = button1.getPage();
            }
            this.updateButtons();
        }
    }

    private void drawPages(int pages) {
        switch (pages) {
            case 0:
            default:
                GlStateManager.pushMatrix();
                this.createLineFromString(0, "desc.intro_0");
                this.createLineFromString(1, "desc.intro_1");
                this.createLineFromString(2, "desc.intro_2");
                this.createLineFromString(3, "desc.intro_3");
                this.createLineFromString(4, "desc.intro_4");
                this.createLineFromString(5, "desc.intro_5");
                this.createLineFromString(6, "desc.intro_6");
                this.drawItemStack(new ItemStack(ModItems.LAMENTED_EYE), (this.width - 75 * 2) / 2, 14);
                GlStateManager.popMatrix();
                break;

            case 1:
                GlStateManager.pushMatrix();
                this.createLineFromString(0, "desc.lame_0");
                this.createLineFromString(1, "desc.lame_1");
                this.createLineFromString(2, "desc.lame_2");
                this.createLineFromString(3, "desc.lame_3");
                this.createLineFromString(4, "desc.lame_4");
                this.createLineFromString(5, "desc.lame_5");
                this.createLineFromString(6, "desc.lame_6");
                this.createLineFromString(7, "desc.lame_7");
                this.createLineFromString(8, "desc.lame_8");
                this.createLineFromString(9, "desc.lame_9");
                this.createLineFromString(10, "desc.lame_10");
                this.createLineFromString(11, "desc.lame_11");
                this.createLineFromString(12, "desc.lame_12");
                this.createLineFromString(13, "desc.lame_13");
                this.mc.renderEngine.bindTexture(MOB_PICTURES);
                drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13,0, 58, 115, 58, 115, 464);
          GlStateManager.popMatrix();
                break;

            case 2:

                GlStateManager.pushMatrix();
                this.drawItemStack(new ItemStack(ModItems.PURPLE_CRYSTAL_ITEM), (this.width + 58 * 2) / 2,54);
                GlStateManager.popMatrix();
                break;

            case 3:

                GlStateManager.pushMatrix();
                this.drawItemStack(new ItemStack(ModItems.END_KEY), (this.width + 58 * 2) / 2,54);
                GlStateManager.popMatrix();
                break;
        }
    }


    private void createLineFromString(int line, String text) {
            String s = I18n.format(text);
        int k = this.fontRenderer.getStringWidth(s) / 2;
        int i = (this.width - X) / 2;
        GlStateManager.scale(0.6F, 0.6F, 0.0F);
        GlStateManager.translate(0.8F, 0.8F, 0F);
        this.fontRenderer.drawString(TextFormatting.BLACK + s, Math.round(i + 90 / 0.6F),  Math.round(70 + (line * 8) / 0.6F), 0);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
    }

    private void drawItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = null;
        if (!stack.isEmpty()) font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        GlStateManager.scale((float) 1.0, (float) 1.0, (float) 1.0);
        this.itemRender.zLevel = 5;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        GlStateManager.disableLighting();
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, null);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton
    {
        private final boolean isForward;

        public NextPageButton(int buttonId, int x, int y, boolean isForwardIn)
        {
            super(buttonId, x, y, 23, 13, "");
            this.isForward = isForwardIn;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
        {
            if (this.visible)
            {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(GUI_BASE_BOOK_BACKGROUND);
                int i = 110;
                int j = 190;

                if (flag)
                {
                    i += 23;
                }
                if (!this.isForward)
                {
                    j += 13;
                }
                drawModalRectWithCustomSizedTexture(this.x, this.y, i, j, 23, 13, 288, 224);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    static class IndexButtonTest extends GuiButton {

        private final int page;

        public IndexButtonTest(int buttonId, int x, int y, int pageIn) {
            super(buttonId, x, y, "");
            this.page = pageIn;
        }

        public int getPage() {
            return page;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if(this.visible) {
               boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               mc.getTextureManager().bindTexture(GUI_INDEX_BUTTON);
               int i = 0;
               int j = 15;
               if(flag) {
                   j += 15;
               }
                drawModalRectWithCustomSizedTexture(this.x, this.y, i, j, 105, 15, 105,  30);
            }
        }

    }

}
