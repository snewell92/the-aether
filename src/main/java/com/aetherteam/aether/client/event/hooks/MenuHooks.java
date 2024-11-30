package com.aetherteam.aether.client.event.hooks;

import com.aetherteam.aether.AetherConfig;
import com.aetherteam.aether.api.AetherMenus;
import com.aetherteam.aether.client.AetherMenuUtil;
import com.aetherteam.aether.client.AetherMusicManager;
import com.aetherteam.aether.client.WorldDisplayHelper;
import com.aetherteam.aether.client.gui.component.menu.DynamicMenuButton;
import com.aetherteam.cumulus.CumulusConfig;
import com.aetherteam.cumulus.api.MenuHelper;
import com.aetherteam.cumulus.client.CumulusClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.client.event.ScreenEvent;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.function.Predicate;

public class MenuHooks {
    /**
     * If the current date is July 22nd, displays the Aether's anniversary splash text.
     *
     * @see com.aetherteam.aether.client.event.listeners.MenuListener#onGuiInitialize(ScreenEvent.Init.Post)
     */
    public static void setCustomSplashText(TitleScreen screen) {
        Predicate<Calendar> condition = (calendar) -> calendar.get(Calendar.MONTH) + 1 == 7 && calendar.get(Calendar.DATE) == 22;
        CumulusClient.MENU_HELPER.setCustomSplash(screen, condition, "Happy anniversary to the Aether!");
    }

    /**
     * Sets up the button for toggling the world preview display.
     *
     * @param screen The current {@link Screen}.
     * @return The created {@link Button}.
     * @see com.aetherteam.aether.client.event.listeners.MenuListener#onGuiInitialize(ScreenEvent.Init.Post)
     */
    @Nullable
    public static Button setupToggleWorldButton(Screen screen) {
        if (screen instanceof TitleScreen) {
            DynamicMenuButton dynamicMenuButton = new DynamicMenuButton(new Button.Builder(Component.translatable("gui.aether.menu.button.world_preview"), (pressed) -> {
                AetherConfig.CLIENT.enable_world_preview.set(!AetherConfig.CLIENT.enable_world_preview.get());
                AetherConfig.CLIENT.enable_world_preview.save();
                WorldDisplayHelper.toggleWorldPreview();
            }).bounds(screen.width - 24 - getButtonOffset(), 4, 20, 20).tooltip(Tooltip.create(Component.translatable("gui.aether.menu.preview"))));
            dynamicMenuButton.setDisplayConfigs(AetherConfig.CLIENT.enable_world_preview_button);
            return dynamicMenuButton;
        }
        return null;
    }

    /**
     * Sets up the button for quick-loading into a world when the world preview is active.
     *
     * @param screen The current {@link Screen}.
     * @return The created {@link Button}.
     * @see com.aetherteam.aether.client.event.listeners.MenuListener#onGuiInitialize(ScreenEvent.Init.Post)
     */
    @Nullable
    public static Button setupQuickLoadButton(Screen screen) {
        if (screen instanceof TitleScreen) {
            DynamicMenuButton dynamicMenuButton = new DynamicMenuButton(new Button.Builder(Component.translatable("gui.aether.menu.button.quick_load"), (pressed) -> {
                WorldDisplayHelper.enterLoadedLevel();
                Minecraft.getInstance().getMusicManager().stopPlaying();
                Minecraft.getInstance().getSoundManager().stop();
                AetherMusicManager.stopPlaying();
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }).bounds(screen.width - 24 - getButtonOffset(), 4, 20, 20).tooltip(Tooltip.create(Component.translatable("gui.aether.menu.load"))));
            dynamicMenuButton.setOffsetConfigs(AetherConfig.CLIENT.enable_world_preview_button);
            dynamicMenuButton.setDisplayConfigs(AetherConfig.CLIENT.enable_world_preview, AetherConfig.CLIENT.enable_quick_load_button);
            return dynamicMenuButton;
        }
        return null;
    }

    /**
     * @return An {@link Integer} offset for buttons, dependent on whether Cumulus' menu switcher button is enabled,
     * as determined by {@link CumulusConfig.Client#enable_menu_api} and {@link CumulusConfig.Client#enable_menu_list_button}.
     */
    private static int getButtonOffset() {
        return CumulusConfig.CLIENT.enable_menu_api.get() && CumulusConfig.CLIENT.enable_menu_list_button.get() ? 62 : 0;
    }
}
