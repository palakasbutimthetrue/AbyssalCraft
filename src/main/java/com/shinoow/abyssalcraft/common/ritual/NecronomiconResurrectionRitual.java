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
package com.shinoow.abyssalcraft.common.ritual;

import com.shinoow.abyssalcraft.api.block.ACBlocks;
import com.shinoow.abyssalcraft.api.item.ACItems;
import com.shinoow.abyssalcraft.api.ritual.NecronomiconRitual;
import com.shinoow.abyssalcraft.common.caps.INecromancyCapability;
import com.shinoow.abyssalcraft.common.caps.NecromancyCapability;
import com.shinoow.abyssalcraft.common.entity.EntityDreadSpawn;
import com.shinoow.abyssalcraft.common.entity.EntityGreaterDreadSpawn;
import com.shinoow.abyssalcraft.common.entity.EntityLesserDreadbeast;
import com.shinoow.abyssalcraft.lib.util.blocks.IRitualAltar;
import com.shinoow.abyssalcraft.lib.util.blocks.IRitualPedestal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NecronomiconResurrectionRitual extends NecronomiconRitual {

	public NecronomiconResurrectionRitual() {
		super("resurrection", 2, -1, 1000F, true, new Object[]{ new ItemStack[]{new ItemStack(ACItems.crystal_shard, 1, 3), new ItemStack(ACItems.crystal, 1, 3),
				new ItemStack(ACBlocks.crystal_cluster, 1, 3)}, new ItemStack[]{new ItemStack(ACItems.crystal_shard, 1, 5), new ItemStack(ACItems.crystal, 1, 5),
						new ItemStack(ACBlocks.crystal_cluster, 1, 5)}, new ItemStack[]{new ItemStack(ACItems.crystal_shard, 1, 6), new ItemStack(ACItems.crystal, 1, 6),
								new ItemStack(ACBlocks.crystal_cluster, 1, 6)}, new ItemStack[]{new ItemStack(ACItems.crystal_shard, 1, 4), new ItemStack(ACItems.crystal, 1, 4),
										new ItemStack(ACBlocks.crystal_cluster, 1, 4)}, new ItemStack[]{new ItemStack(ACItems.crystal_shard, 1, 7), new ItemStack(ACItems.crystal, 1, 7),
												new ItemStack(ACBlocks.crystal_cluster, 1, 7)}, new ItemStack[]{new ItemStack(ACItems.crystal_shard, 1, 2), new ItemStack(ACItems.crystal, 1, 2),
														new ItemStack(ACBlocks.crystal_cluster, 1, 2)}});
		sacrifice = Items.NAME_TAG;
	}

	@Override
	public boolean requiresItemSacrifice(){
		return true;
	}

	public boolean checkSurroundings(World world, BlockPos pos, int size){
		ItemStack[] offers = new ItemStack[8];
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		TileEntity ped1 = world.getTileEntity(new BlockPos(x - 3, y, z));
		TileEntity ped2 = world.getTileEntity(new BlockPos(x, y, z - 3));
		TileEntity ped3 = world.getTileEntity(new BlockPos(x + 3, y, z));
		TileEntity ped4 = world.getTileEntity(new BlockPos(x, y, z + 3));
		TileEntity ped5 = world.getTileEntity(new BlockPos(x - 2, y, z + 2));
		TileEntity ped6 = world.getTileEntity(new BlockPos(x - 2, y, z - 2));
		TileEntity ped7 = world.getTileEntity(new BlockPos(x + 2, y, z + 2));
		TileEntity ped8 = world.getTileEntity(new BlockPos(x + 2, y, z - 2));
		if(ped1 != null && ped2 != null && ped3 != null && ped4 != null && ped5 != null && ped6 != null && ped7 != null && ped8 != null)
			if(ped1 instanceof IRitualPedestal && ped2 instanceof IRitualPedestal && ped3 instanceof IRitualPedestal
					&& ped4 instanceof IRitualPedestal && ped5 instanceof IRitualPedestal && ped6 instanceof IRitualPedestal
					&& ped7 instanceof IRitualPedestal && ped8 instanceof IRitualPedestal){
				offers[0] = ((IRitualPedestal)ped1).getItem();
				offers[1] = ((IRitualPedestal)ped2).getItem();
				offers[2] = ((IRitualPedestal)ped3).getItem();
				offers[3] = ((IRitualPedestal)ped4).getItem();
				offers[4] = ((IRitualPedestal)ped5).getItem();
				offers[5] = ((IRitualPedestal)ped6).getItem();
				offers[6] = ((IRitualPedestal)ped7).getItem();
				offers[7] = ((IRitualPedestal)ped8).getItem();
				for(ItemStack stack : offers)
					if(mismatch(stack, getOfferings()[0], size))
						return false;
				return true;
			}
		return false;
	}

	private boolean mismatch(ItemStack stack, Object obj, int size){

		if(stack.isEmpty()) return false;

		if(obj instanceof ItemStack[])
			return stack.getItem() != ((ItemStack[])obj)[size].getItem();

		return false;
	}

	@Override
	public boolean canCompleteRitual(World world, BlockPos pos, EntityPlayer player) {

		TileEntity altar = world.getTileEntity(pos);

		ItemStack stack = ItemStack.EMPTY;

		if(altar instanceof IRitualAltar)
			stack = ((IRitualAltar) altar).getItem();

		if(!stack.isEmpty() && stack.getItem() == Items.NAME_TAG){
			INecromancyCapability cap = NecromancyCapability.getCap(player);
			return cap.getDataForName(stack.getDisplayName()) != null && checkSurroundings(world, pos, cap.getSizeForName(stack.getDisplayName()));
		}

		return false;
	}

	@Override
	protected void completeRitualClient(World world, BlockPos pos, EntityPlayer player) {
		TileEntity altar = world.getTileEntity(pos);

		ItemStack stack = ItemStack.EMPTY;

		if(altar instanceof IRitualAltar)
			stack = ((IRitualAltar) altar).getItem();

		if(!stack.isEmpty() && stack.getItem() == Items.NAME_TAG){
			INecromancyCapability cap = NecromancyCapability.getCap(player);
			cap.clearEntry(stack.getDisplayName());
		}
	}

	@Override
	protected void completeRitualServer(World world, BlockPos pos, EntityPlayer player) {
		TileEntity altar = world.getTileEntity(pos);

		ItemStack stack = ItemStack.EMPTY;

		if(altar instanceof IRitualAltar)
			stack = ((IRitualAltar) altar).getItem();

		if(!stack.isEmpty() && stack.getItem() == Items.NAME_TAG){
			INecromancyCapability cap = NecromancyCapability.getCap(player);
			Entity e = EntityList.createEntityFromNBT(cap.getDataForName(stack.getDisplayName()), world);
			if(e instanceof EntityLiving){
				EntityLiving entity = getEntity(e, cap.getSizeForName(stack.getDisplayName()));
				world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY() + 2, pos.getZ(), true));
				entity.setLocationAndAngles(pos.getX(), pos.getY() + 1, pos.getZ(), entity.rotationYaw, entity.rotationPitch);
				entity.setHealth(entity.getMaxHealth());
				world.spawnEntity(entity);
				cap.clearEntry(stack.getDisplayName());
				((IRitualAltar) altar).setItem(ItemStack.EMPTY);
			}
		}
	}

	private EntityLiving getEntity(Entity e, int size){

		int i = e.getEntityData().getInteger("Reanimations");

		if(i > 3){
			boolean b = false;

			switch(i){
			case 4:
				b = e.world.rand.nextFloat() < 0.9;
				break;
			case 5:
				b = e.world.rand.nextFloat() < 0.75;
				break;
			case 6:
				b = e.world.rand.nextFloat() < 0.6;
				break;
			case 7:
				b = e.world.rand.nextFloat() < 0.45;
				break;
			case 8:
				b = e.world.rand.nextFloat() < 0.3;
				break;
			case 9:
				b = e.world.rand.nextFloat() < 0.15;
				break;
			case 10:
				b = true;
				break;
			}

			if(b){

				EntityLiving e1 = new EntityDreadSpawn(e.world);

				switch(size){
				case 0:
					e1 = new EntityDreadSpawn(e.world);
					break;
				case 1:
					e1 = new EntityGreaterDreadSpawn(e.world);
					break;
				case 2:
					e1 = new EntityLesserDreadbeast(e.world);
					break;
				}

				e1.setCustomNameTag(e.getCustomNameTag());
				return e1;
			}
		}

		e.getEntityData().setInteger("Reanimations", i + 1);

		return (EntityLiving) e;
	}
}
