package io.github.tecses1.UltimatePVP;

import org.bukkit.block.Block;

public class BlockData{
	public Block block;
	public int hp = 0;
	
	public BlockData(Block b, int health) {
		this.block = b;
		this.hp = health;
	}

}