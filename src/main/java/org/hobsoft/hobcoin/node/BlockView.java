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
package org.hobsoft.hobcoin.node;

import org.hobsoft.hobcoin.Block;

/**
 * JSON representation of a block.
 */
public class BlockView
{
	private String previousHash;
	
	private long timestamp;
	
	private long nonce;
	
	private String hash;
	
	public String getPreviousHash()
	{
		return previousHash;
	}
	
	public void setPreviousHash(String previousHash)
	{
		this.previousHash = previousHash;
	}
	
	public long getTimestamp()
	{
		return timestamp;
	}
	
	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
	
	public long getNonce()
	{
		return nonce;
	}
	
	public void setNonce(long nonce)
	{
		this.nonce = nonce;
	}
	
	public String getHash()
	{
		return hash;
	}
	
	public void setHash(String hash)
	{
		this.hash = hash;
	}
	
	public static BlockView of(Block block)
	{
		BlockView view = new BlockView();
		view.setPreviousHash(block.previousHash());
		view.setTimestamp(block.timestamp());
		view.setNonce(block.nonce());
		view.setHash(block.hash());
		return view;
	}
}
