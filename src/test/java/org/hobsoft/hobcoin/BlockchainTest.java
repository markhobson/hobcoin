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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code Blockchain}.
 */
public class BlockchainTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private Blockchain blockchain;
	
	@Before
	public void setUp()
	{
		Wallet wallet = new Wallet();
		blockchain = new Blockchain(wallet.address(), 100, 2);
	}
	
	@Test
	public void canAddBlock()
	{
		Block block = new Block(someTransaction(), blockchain.tail().hash())
			.mine(blockchain.difficulty());
		
		blockchain.add(block);
		
		assertThat(blockchain.height(), is(2));
	}
	
	// TODO: cannotAddBlockWithInvalidHash
	
	// TODO: cannotAddBlockWithPastTimestamp
	
	@Test
	public void cannotAddBlockWithInvalidPreviousHash()
	{
		Block block = new Block(someTransaction(), "123")
			.mine(blockchain.difficulty());
		
		thrown.expect(InvalidBlockException.class);
		
		blockchain.add(block);
	}
	
	@Test
	public void cannotAddUnminedBlock()
	{
		Block block = new Block(someTransaction(), blockchain.tail().hash());
		
		thrown.expect(InvalidBlockException.class);
		
		blockchain.add(block);
	}
	
	@Test
	public void cannotAddBlockWithSpentTransactionInput()
	{
		TransactionInput input = new TransactionInput(new TransactionOutputPoint("123", 4));
		Transaction transaction = new Transaction(singletonList(input), emptyList());
		Block block = new Block(transaction, blockchain.tail().hash())
			.mine(blockchain.difficulty());
		
		thrown.expect(InvalidTransactionException.class);
		
		blockchain.add(block);
	}
	
	@Test
	public void cannotAddBlockWithUnsignedTransactionInput()
	{
		TransactionInput input = new TransactionInput(blockchain.tail().transaction().outputPoints().iterator().next());
		Transaction transaction = new Transaction(singletonList(input), emptyList());
		Block block = new Block(transaction, blockchain.tail().hash())
			.mine(blockchain.difficulty());
		
		thrown.expect(InvalidTransactionException.class);
		
		blockchain.add(block);
	}
	
	private static Transaction someTransaction()
	{
		return new Transaction(emptyList(), emptyList());
	}
}
