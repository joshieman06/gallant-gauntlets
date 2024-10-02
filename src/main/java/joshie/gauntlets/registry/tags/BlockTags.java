package joshie.gauntlets.registry.tags;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;


public final class BlockTags {
    public static final TagKey<Block> GAUNTLET_EFFICIENT = of("gauntlet_efficient");

    private BlockTags() {
    }

    private static TagKey<Block> of(String id) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(id));
    }
}