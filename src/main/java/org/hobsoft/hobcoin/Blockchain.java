/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hobsoft.hobcoin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A linked list of blocks.
 */
public class Blockchain implements Iterable<Block>
{
	private final List<Block> blocks;
	
	public Blockchain()
	{
		blocks = new ArrayList<>();
	}
	
	@Override
	public Iterator<Block> iterator()
	{
		return blocks.iterator();
	}
	
	public Block tail()
	{
		return blocks.isEmpty()
			? null
			: blocks.get(blocks.size() - 1);
	}
	
	public Blockchain add(Block block)
	{
		blocks.add(block);
		return this;
	}
	
	public boolean isValid()
	{
		Block previous = null;
		
		for (Block block : blocks)
		{
			if (!block.isValid(previous))
			{
				return false;
			}
			
			previous = block;
		}
		
		return true;
	}
}
