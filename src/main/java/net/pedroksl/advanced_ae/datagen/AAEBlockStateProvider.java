package net.pedroksl.advanced_ae.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.pedroksl.advanced_ae.AdvancedAE;

public class AAEBlockStateProvider extends BlockStateProvider {
	public AAEBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, AdvancedAE.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
	}

	private void blockWithItem(Block blockRegistryObject) {
		simpleBlockWithItem(blockRegistryObject, cubeAll(blockRegistryObject));
	}
}
