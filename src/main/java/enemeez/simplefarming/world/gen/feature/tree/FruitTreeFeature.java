package enemeez.simplefarming.world.gen.feature.tree;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import enemeez.simplefarming.config.DimensionConfig;
import enemeez.simplefarming.config.GenConfig;
import enemeez.simplefarming.init.ModBlocks;
import enemeez.simplefarming.util.WorldGenHelper;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class FruitTreeFeature extends Feature<NoFeatureConfig> {
	public FruitTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory) {
		super(configFactory);
	}

	private static BlockState getLeaves(int verify) {
		switch (verify) {
		case 1:
			return ModBlocks.apple_leaves.getDefaultState();
		case 2:
			return ModBlocks.apricot_leaves.getDefaultState();
		case 3:
			return ModBlocks.banana_leaves.getDefaultState();
		case 4:
			return ModBlocks.plum_leaves.getDefaultState();
		case 5:
			return ModBlocks.orange_leaves.getDefaultState();
		case 6:
			return ModBlocks.pear_leaves.getDefaultState();
		case 7:
			return ModBlocks.cherry_leaves.getDefaultState();
		case 8:
			return ModBlocks.mango_leaves.getDefaultState();
		default:
			return ModBlocks.olive_leaves.getDefaultState();
		}
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random, BlockPos pos, NoFeatureConfig config) {
		if (random.nextInt(GenConfig.tree_chance.get()) != 0 || DimensionConfig.blacklist.get().contains(world.getDimension().getType().getId())
				|| !DimensionConfig.whitelist.get().contains(world.getDimension().getType().getId())) {
			return false;
		}
		if (WorldGenHelper.isValidGround(world.getBlockState(pos.down()), world, pos) && world.getBlockState(pos).getMaterial().isReplaceable()) {
			int type = random.nextInt(9) + 1;
			generateTree(world, pos, random, type);
			return true;
		}
		return false;
	}

	public static void generateTree(IWorld world, BlockPos pos, Random random, int verify) {
		BlockState trunk = ModBlocks.fruit_log.getDefaultState();
		BlockState leaves = getLeaves(verify);

		for (int y = 0; y < 4; y++) {
			if (world.getBlockState(pos.up(y)).getMaterial().isReplaceable() || y == 0)
				world.setBlockState(pos.up(y), trunk, 3);
		}

		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				if (world.getBlockState(pos.up(3).add(x, 0, z)).getMaterial().isReplaceable())
					world.setBlockState(pos.up(3).add(x, 0, z), leaves, 3);
			}
		}

		for (int x = -1; x < 2; x++) {
			if (x != 0) {
				if (world.getBlockState(pos.up(2).add(x, 0, 0)).getMaterial().isReplaceable())
					world.setBlockState(pos.up(2).add(x, 0, 0), leaves, 3);
			} else {
				if (world.getBlockState(pos.up(2).add(x, 0, 1)).getMaterial().isReplaceable())
					world.setBlockState(pos.up(2).add(x, 0, 1), leaves, 3);
				if (world.getBlockState(pos.up(2).add(x, 0, -1)).getMaterial().isReplaceable())
					world.setBlockState(pos.up(2).add(x, 0, -1), leaves, 3);
			}
		}

		if (world.getBlockState(pos.up(4)).getMaterial().isReplaceable())
			world.setBlockState(pos.up(4), leaves, 3);
	}

}