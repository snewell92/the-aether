package com.aetherteam.aether.client.event.listeners;

import com.aetherteam.aether.client.AetherClient;
import com.aetherteam.aether.client.event.hooks.MenuHooks;
import com.aetherteam.cumulus.api.MenuHelper;
import com.aetherteam.cumulus.client.CumulusClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class MenuListener {
    /**
     * @see AetherClient#eventSetup(IEventBus)
     */
    public static void listen(IEventBus bus) {
        bus.addListener(MenuListener::onGuiInitialize);
    }

    /**
     * @see MenuHooks#setupToggleWorldButton(Screen)
     * @see MenuHooks#setupQuickLoadButton(Screen)
     */
    public static void onGuiInitialize(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (screen instanceof TitleScreen titleScreen) {
            MenuHooks.setCustomSplashText(titleScreen);

            Button toggleWorldButton = MenuHooks.setupToggleWorldButton(screen);
            if (toggleWorldButton != null) {
                event.addListener(toggleWorldButton);
            }

            Button quickLoadButton = MenuHooks.setupQuickLoadButton(screen);
            if (quickLoadButton != null) {
                event.addListener(quickLoadButton);
            }
        }
    }
}
