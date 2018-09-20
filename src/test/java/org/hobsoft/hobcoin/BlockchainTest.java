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

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
	
	private Wallet wallet;
	
	private Blockchain blockchain;
	
	@Before
	public void setUp()
	{
		wallet = new Wallet();
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
		Transaction transaction = new Transaction(singletonList(input), singletonList(someTransactionOutput()));
		Block block = new Block(transaction, blockchain.tail().hash())
			.mine(blockchain.difficulty());
		
		thrown.expect(InvalidTransactionException.class);
		
		blockchain.add(block);
	}
	
	@Test
	public void cannotAddBlockWithUnsignedTransactionInput()
	{
		TransactionInput input = new TransactionInput(blockchain.tail().transaction().outputPoints().iterator().next());
		Transaction transaction = new Transaction(singletonList(input), singletonList(someTransactionOutput()));
		Block block = new Block(transaction, blockchain.tail().hash())
			.mine(blockchain.difficulty());
		
		thrown.expect(InvalidTransactionException.class);
		
		blockchain.add(block);
	}
	
	@Test
	public void cannotAddBlockWithUnverifiedTransactionInput() throws Exception
	{
		TransactionInput input = new TransactionInput(blockchain.tail().transaction().outputPoints().iterator().next())
			.sign(somePrivateKey());
		Transaction transaction = new Transaction(singletonList(input), singletonList(someTransactionOutput()));
		Block block = new Block(transaction, blockchain.tail().hash())
			.mine(blockchain.difficulty());
		
		thrown.expect(InvalidTransactionException.class);
		
		blockchain.add(block);
	}
	
	@Test
	public void cannotAddBlockWithNonZeroTransactionAmount()
	{
		Transaction transaction = wallet.transfer(blockchain, new Wallet().address(), 100);
		TransactionOutput output = new TransactionOutput(transaction.outputs().iterator().next().recipient(), 200);
		Transaction unbalancedTransaction = new Transaction(transaction.inputs(), singletonList(output));
		Block block = new Block(unbalancedTransaction, blockchain.tail().hash())
			.mine(blockchain.difficulty());
		
		thrown.expect(InvalidTransactionException.class);
		
		blockchain.add(block);
	}
	
	private Transaction someTransaction()
	{
		PublicKey recipient = new Wallet().address();
		long amount = wallet.amount(blockchain);
		
		return wallet.transfer(blockchain, recipient, amount);
	}
	
	private static TransactionOutput someTransactionOutput()
	{
		return new TransactionOutput(new Wallet().address(), 1);
	}

	private static PrivateKey somePrivateKey() throws NoSuchAlgorithmException
	{
		return KeyPairGenerator.getInstance("EC").generateKeyPair().getPrivate();
	}
}
