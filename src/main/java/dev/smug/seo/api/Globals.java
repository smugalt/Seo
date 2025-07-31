package dev.smug.seo.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

public interface Globals {
    MinecraftClient mc = MinecraftClient.getInstance();
    ClientWorld world = mc.world;
    ClientPlayerEntity player = mc.player;
}
