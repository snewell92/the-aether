package com.aetherteam.aether.data.resources.registries;

import com.aetherteam.aether.Aether;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

public class AetherNoises {
	public static final ResourceKey<NoiseParameters> TEMPERATURE = createKey("temperature");
	public static final ResourceKey<NoiseParameters> VEGETATION = createKey("vegetation");

	private static ResourceKey<NoiseParameters> createKey(String name) {
        return ResourceKey.create(Registry.NOISE_REGISTRY, new ResourceLocation(Aether.MODID, name));
    }

//	public static void bootstrap(BootstapContext<NoiseParameters> context) {
//		register(context, TEMPERATURE, -8, 1.5, 0.0, 1.0, 0.0, 0.0, 0.0);
//		register(context, VEGETATION, -7, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0);
//    }
//
//	public static void register(BootstapContext<NoiseParameters> context, ResourceKey<NormalNoise.NoiseParameters> key, int firstOctave, double firstAmplitude, double... amplitudes) {
//		context.register(key, new NormalNoise.NoiseParameters(firstOctave, firstAmplitude, amplitudes));
//	}
}