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
	private static final String GENESIS_BLOCK_HASH = "0";
	
	private final List<Block> blocks;
	
	private final UnspentTransactionOutputs unspentTransactionOutputs;
	
	private final int difficulty;
	
	public Blockchain(PublicKey recipient, long amount, int difficulty)
	{
		blocks = new ArrayList<>();
		unspentTransactionOutputs = new UnspentTransactionOutputs();
		this.difficulty = difficulty;
		
		addQuietly(newGenesisBlock(recipient, amount));
	}
	
	@Override
	public Iterator<Block> iterator()
	{
		return blocks.iterator();
	}
	
	public int height()
	{
		return blocks.size();
	}
	
	public Block tail()
	{
		return blocks.isEmpty()
			? null
			: blocks.get(blocks.size() - 1);
	}
	
	/**
	 * Adds the specified block to the end of this blockchain.
	 * 
	 * @param block the block to add
	 * @return this blockchain
	 * @throws InvalidBlockException if the block's previous hash does not match the tail block's hash, or the block is
	 * not mined to the current difficultly
	 * @throws InvalidTransactionException if a transaction input within the block has already been spent 
	 */
	public Blockchain add(Block block)
	{
		validateBlock(block);
		
		return addQuietly(block);
	}
	
	public int difficulty()
	{
		return difficulty;
	}
	
	public List<UnspentTransactionOutput> unspentTransactionOutputs(PublicKey owner, long minimumAmount)
	{
		return unspentTransactionOutputs.unspentTransactionOutputs(owner, minimumAmount);
	}
	
	private void validateBlock(Block block)
	{
		// TODO: validate block hash
		
		// TODO: validate block timestamp
		
		if (!block.follows(tail()))
		{
			throw new InvalidBlockException("Previous hash does not match tail block");
		}
		
		if (!block.isMined(difficulty()))
		{
			throw new InvalidBlockException("Unmined block");
		}
		
		validateTransaction(block.transaction());
	}
	
	private void validateTransaction(Transaction transaction)
	{
		// TODO: validate transaction id
		
		transaction.inputs().forEach(this::validateTransactionInput);
	}
	
	private void validateTransactionInput(TransactionInput input)
	{
		unspentTransactionOutputs.find(input.transactionOutputPoint())
			.orElseThrow(() -> new InvalidTransactionException("Spent transaction input: " + input));
	}
	
	private Blockchain addQuietly(Block block)
	{
		blocks.add(block);
		unspentTransactionOutputs.removeSpentTransactionOutputs(block.transaction());
		unspentTransactionOutputs.addUnspentTransactionOutputs(block.transaction());
		return this;
	}
	
	private static Block newGenesisBlock(PublicKey recipient, long amount)
	{
		TransactionOutput output = new TransactionOutput(recipient, amount);
		Transaction transaction = new Transaction(emptyList(), singletonList(output));
		return new Block(transaction, GENESIS_BLOCK_HASH);
	}
}
