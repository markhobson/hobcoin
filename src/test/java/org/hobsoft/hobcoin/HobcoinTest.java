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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Integration test for Hobcoin.
 */
public class HobcoinTest
{
	@Test
	public void canTransferCoins()
	{
		Wallet alice = new Wallet();
		Wallet bob = new Wallet();

		Blockchain blockchain = new Blockchain(alice.address(), 15);
		
		Transaction tx = alice.transfer(blockchain, bob.address(), 10);
		Block block = new Block(tx, blockchain.tail().hash());
		block.mine(blockchain.difficulty());
		blockchain.add(block);
		
		assertThat("alice", alice.amount(blockchain), is(5L));
		assertThat("bob", bob.amount(blockchain), is(10L));
	}
}
