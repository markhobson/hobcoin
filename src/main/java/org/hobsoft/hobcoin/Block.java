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

import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.logging.Level.INFO;

/**
 * A block of data in the blockchain.
 */
public class Block
{
	private static final String GENESIS_HASH = "0";
	
	private static final Logger LOG = Logger.getLogger(Block.class.getName());
	
	private final String previousHash;
	
	private final long timestamp;
	
	private final Transaction transaction;
	
	private int nonce;
	
	private String hash;
	
	public Block(Transaction transaction, Block previous)
	{
		this.transaction = transaction;
		this.previousHash = Optional.ofNullable(previous)
			.map(Block::hash)
			.orElse(GENESIS_HASH);
		
		timestamp = new Date().getTime();
		nonce = 0;
		hash = calculateHash();
	}
	
	public Transaction transaction()
	{
		return transaction;
	}
	
	public String hash()
	{
		return hash;
	}
	
	public boolean isValid(Block previous, int difficulty)
	{
		return hash.equals(calculateHash())
			&& (previous == null || previousHash.equals(previous.hash()))
			&& isMined(difficulty);
	}
	
	public void mine(int difficulty)
	{
		LOG.log(INFO, "Mining to difficulty {0}...", difficulty);
		long start = System.currentTimeMillis();
		
		while (!isMined(difficulty))
		{
			nonce++;
			hash = calculateHash();
		}
		
		LOG.log(INFO, "Mined block {0} in {1}ms", new Object[] {hash, System.currentTimeMillis() - start});
	}
	
	private boolean isMined(int difficulty)
	{
		String targetHashPrefix = Strings.repeat("0", difficulty);
		
		return hash.startsWith(targetHashPrefix);
	}
	
	private String calculateHash()
	{
		return Hashing.sha256()
			.hashObject(this, (from, into) -> into
				.putString(from.previousHash, UTF_8)
				.putLong(from.timestamp)
				.putLong(from.nonce)
				.putString(from.transaction.id(), UTF_8)
			)
			.toString();
	}
}
