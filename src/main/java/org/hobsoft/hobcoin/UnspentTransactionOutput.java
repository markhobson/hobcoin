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
import java.util.List;

/**
 * An unspent transaction output (UTXO) within a blockchain.
 */
class UnspentTransactionOutput extends TransactionOutput
{
	private final TransactionOutputPoint transactionOutputPoint;
	
	UnspentTransactionOutput(TransactionOutputPoint transactionOutputPoint, PublicKey recipient, long amount)
	{
		super(recipient, amount);
		
		this.transactionOutputPoint = transactionOutputPoint;
	}
	
	public TransactionOutputPoint transactionOutputPoint()
	{
		return transactionOutputPoint;
	}
	
	public static List<UnspentTransactionOutput> atLeast(List<UnspentTransactionOutput> allUnspentOutputs,
		long minimumAmount)
	{
		if (minimumAmount == 0)
		{
			return allUnspentOutputs;
		}
		
		List<UnspentTransactionOutput> unspentOutputs = new ArrayList<>();
		long amount = 0;
		
		for (int index = 0; index < allUnspentOutputs.size() && amount < minimumAmount; index++)
		{
			UnspentTransactionOutput unspentOutput = allUnspentOutputs.get(index);
			
			unspentOutputs.add(unspentOutput);
			amount += unspentOutput.amount();
		}
		
		return unspentOutputs;
	}
}
