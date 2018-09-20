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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * A list of unspent transaction outputs (UTXOs) within a blockchain.
 */
class UnspentTransactionOutputs
{
	private final Map<TransactionOutputPoint, UnspentTransactionOutput> unspentOutputs;
	
	UnspentTransactionOutputs()
	{
		unspentOutputs = new HashMap<>();
	}
	
	public Optional<UnspentTransactionOutput> find(TransactionOutputPoint outputPoint)
	{
		return Optional.ofNullable(unspentOutputs.get(outputPoint));
	}
	
	public List<UnspentTransactionOutput> find(List<TransactionOutputPoint> outputPoints)
	{
		return outputPoints.stream()
			.map(unspentOutputs::get)
			.collect(toList());
	}
	
	public List<UnspentTransactionOutput> unspentTransactionOutputs(PublicKey owner, long minimumAmount)
	{
		List<UnspentTransactionOutput> ownerUnspentOutputs = unspentOutputs.values()
			.stream()
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
		unspentOutputs.keySet().removeAll(transaction.inputs()
			.stream()
			.map(TransactionInput::transactionOutputPoint)
			.collect(toList())
		);
	}
	
	public void addUnspentTransactionOutputs(Transaction transaction)
	{
		for (TransactionOutputPoint outputPoint : transaction.outputPoints())
		{
			TransactionOutput output = transaction.output(outputPoint);
			
			UnspentTransactionOutput unspentOutput = new UnspentTransactionOutput(outputPoint, output.recipient(),
				output.amount());
			
			unspentOutputs.put(outputPoint, unspentOutput);
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
}
