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

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

/**
 * A linked list of blocks.
 */
public class Blockchain implements Iterable<Block>
{
	private static final int MINING_DIFFICULTY = 5;
	
	private final List<Block> blocks;
	
	private final UnspentTransactionOutputs unspentTransactionOutputs;
	
	public Blockchain(PublicKey recipient, long amount)
	{
		blocks = new ArrayList<>();
		unspentTransactionOutputs = new UnspentTransactionOutputs();
		
		add(newGenesisBlock(recipient, amount));
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
		// TODO: validate block
		
		blocks.add(block);
		unspentTransactionOutputs.removeSpentTransactionOutputs(block.transaction());
		unspentTransactionOutputs.addUnspentTransactionOutputs(block.transaction());
		return this;
	}
	
	public boolean isValid()
	{
		Block previous = null;
		
		for (Block block : blocks)
		{
			if (!block.isValid(previous, MINING_DIFFICULTY))
			{
				return false;
			}
			
			previous = block;
		}
		
		return true;
	}

	public int difficulty()
	{
		return MINING_DIFFICULTY;
	}
	
	public List<UnspentTransactionOutput> unspentTransactionOutputs(PublicKey owner, long minimumAmount)
	{
		return unspentTransactionOutputs.unspentTransactionOutputs(owner, minimumAmount);
	}
	
	private static Block newGenesisBlock(PublicKey recipient, long amount)
	{
		TransactionOutput output = new TransactionOutput(recipient, amount);
		Transaction transaction = new Transaction(emptyList(), singletonList(output));
		return new Block(transaction, null);
	}
}
