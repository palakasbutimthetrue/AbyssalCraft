/*******************************************************************************
 * AbyssalCraft
 * Copyright (c) 2012 - 2018 Shinoow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Contributors:
 *     Shinoow -  implementation
 ******************************************************************************/
package com.shinoow.abyssalcraft.common.world.biome;

import java.util.Random;

import com.shinoow.abyssalcraft.api.biome.IDarklandsBiome;
import com.shinoow.abyssalcraft.api.block.ACBlocks;
import com.shinoow.abyssalcraft.common.entity.*;
import com.shinoow.abyssalcraft.common.world.gen.WorldGenDLT;
import com.shinoow.abyssalcraft.common.world.gen.WorldGenNoTree;
import com.shinoow.abyssalcraft.lib.ACConfig;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeGenDarklandsHills extends Biome implements IDarklandsBiome {

	private WorldGenTrees WorldGenDarkTrees;

	public BiomeGenDarklandsHills(BiomeProperties par1)
	{
		super(par1);
		fillerBlock = ACBlocks.stone.getDefaultState();
		WorldGenDarkTrees = new WorldGenDLT(false);
		decorator.treesPerChunk = 1;
		spawnableMonsterList.add(new SpawnListEntry(EntityDepthsGhoul.class, 60, 1, 5));
		spawnableMonsterList.add(new SpawnListEntry(EntityAbyssalZombie.class, 60, 1, 3));
		spawnableMonsterList.add(new SpawnListEntry(EntityShadowCreature.class, 70, 3, 3));
		spawnableMonsterList.add(new SpawnListEntry(EntityShadowMonster.class, 50, 2, 2));
		spawnableMonsterList.add(new SpawnListEntry(EntityShadowBeast.class, 20, 1, 1));
	}

	@Override
	public void decorate(World par1World, Random par2Random, BlockPos pos)
	{
		super.decorate(par1World, par2Random, pos);
		int var5 = 3 + par2Random.nextInt(6);

		if(ACConfig.generateAbyssalniteOre)
			for (int rarity = 0; rarity < var5; ++rarity)
			{
				int veinSize = 1 + par2Random.nextInt(3);
				int x = par2Random.nextInt(16);
				int y = par2Random.nextInt(28) + 4;
				int z = par2Random.nextInt(16);

				new WorldGenMinable(ACBlocks.abyssalnite_ore.getDefaultState(), veinSize).generate(par1World, par2Random, pos.add(x, y, z));
			}

		for (int rarity = 0; rarity < 7; ++rarity)
		{
			int x = par2Random.nextInt(16);
			int y = par2Random.nextInt(64);
			int z = par2Random.nextInt(16);
			new WorldGenMinable(ACBlocks.stone.getDefaultState(), 32).generate(par1World, par2Random, pos.add(x, y, z));
		}
	}

	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random par1Random)
	{
		return par1Random.nextInt(5) == 0 ? new WorldGenNoTree() : par1Random.nextInt(10) == 0 ? WorldGenDarkTrees : new WorldGenNoTree();
	}

	@Override
	@SideOnly(Side.CLIENT)

	public int getSkyColorByTemp(float par1)
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos)
	{
		return 0x17375c;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getFoliageColorAtPos(BlockPos pos)
	{
		return 0x17375c;
	}
}
