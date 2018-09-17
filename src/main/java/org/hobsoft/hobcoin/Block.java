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

import com.google.common.hash.Hashing;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * A block of data in the blockchain.
 */
public class Block
{
	private static final String GENESIS_HASH = "0";
	
	private final String previousHash;
	
	private final long timestamp;
	
	private final String data;
	
	private final String hash;
	
	public Block(String data, Block previous)
	{
		this.data = data;
		this.previousHash = Optional.ofNullable(previous)
			.map(Block::hash)
			.orElse(GENESIS_HASH);
		
		timestamp = new Date().getTime();
		hash = calculateHash();
	}
	
	public String hash()
	{
		return hash;
	}
	
	public boolean isValid(Block previous)
	{
		return hash.equals(calculateHash())
			&& (previous == null || previousHash.equals(previous.hash()));
	}
	
	private String calculateHash()
	{
		return Hashing.sha256()
			.hashString(previousHash + timestamp + data, UTF_8)
			.toString();
	}
}
