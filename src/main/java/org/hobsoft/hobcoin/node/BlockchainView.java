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

import java.util.List;
import java.util.stream.StreamSupport;

import org.hobsoft.hobcoin.Blockchain;

import static java.util.stream.Collectors.toList;

/**
 * JSON representation of a blockchain.
 */
public class BlockchainView
{
	private List<BlockView> blocks;
	
	public List<BlockView> getBlocks()
	{
		return blocks;
	}
	
	public void setBlocks(List<BlockView> blocks)
	{
		this.blocks = blocks;
	}
	
	public static BlockchainView of(Blockchain blockchain)
	{
		BlockchainView view = new BlockchainView();
		view.setBlocks(StreamSupport.stream(blockchain.spliterator(), false)
			.map(BlockView::of)
			.collect(toList())
		);
		return view;
	}
}
