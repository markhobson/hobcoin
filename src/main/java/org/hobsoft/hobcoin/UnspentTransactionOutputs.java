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

import static java.util.stream.Collectors.toList;

/**
 * A list of unspent transaction outputs (UTXOs) within a blockchain.
 */
class UnspentTransactionOutputs
{
	private final List<UnspentTransactionOutput> unspentOutputs;
	
	UnspentTransactionOutputs()
	{
		unspentOutputs = new ArrayList<>();
	}
	
	public List<UnspentTransactionOutput> unspentTransactionOutputs(PublicKey owner, long minimumAmount)
	{
		List<UnspentTransactionOutput> ownerUnspentOutputs = unspentOutputs.stream()
			.filter(out -> out.recipient().equals(owner))
			.collect(toList());
		
		if (minimumAmount > 0)
		{
			ownerUnspentOutputs = atLeast(ownerUnspentOutputs, minimumAmount);
		}
		
		return ownerUnspentOutputs;
	}
	
	public void removeSpentTransactionOutputs(Transaction transaction)
	{
		transaction.inputs()
			.forEach(input -> removeSpentTransactionOutput(input.transactionId(), input.transactionOutputIndex()));
	}
	
	public void addUnspentTransactionOutputs(Transaction transaction)
	{
		for (int index = 0; index < transaction.outputs().size(); index++)
		{
			TransactionOutput output = transaction.outputs().get(index);
			
			UnspentTransactionOutput unspentOutput = new UnspentTransactionOutput(transaction.id(), index,
				output.recipient(), output.amount());
			
			unspentOutputs.add(unspentOutput);
		}
	}
	
	private static List<UnspentTransactionOutput> atLeast(List<UnspentTransactionOutput> allUnspentOutputs,
		long minimumAmount)
	{
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
	
	private void removeSpentTransactionOutput(String transactionId, int transactionOutputIndex)
	{
		unspentOutputs.removeIf(unspentOutput -> unspentOutput.transactionId().equals(transactionId)
			&& unspentOutput.transactionOutputIndex() == transactionOutputIndex
		);
	}
}
