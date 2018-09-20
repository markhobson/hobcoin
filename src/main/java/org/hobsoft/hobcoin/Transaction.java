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

import java.util.List;
import java.util.stream.IntStream;

import com.google.common.hash.Hashing;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A blockchain transaction of one or more inputs to one or more outputs.
 */
public class Transaction
{
	private final String id;
	
	private final List<TransactionInput> inputs;
	
	private final List<TransactionOutput> outputs;
	
	Transaction(List<TransactionInput> inputs, List<TransactionOutput> outputs)
	{
		this.inputs = inputs;
		this.outputs = outputs;
		
		id = calculateHash();
	}
	
	public String id()
	{
		return id;
	}
	
	public List<TransactionInput> inputs()
	{
		return inputs;
	}
	
	public List<TransactionOutput> outputs()
	{
		return outputs;
	}
	
	public TransactionOutput output(TransactionOutputPoint outputPoint)
	{
		checkArgument(id.equals(outputPoint.transactionId()), "Invalid transaction id: " + outputPoint);
		
		return outputs.get(outputPoint.transactionOutputIndex());
	}
	
	public List<TransactionOutputPoint> outputPoints()
	{
		return IntStream.range(0, outputs.size())
			.mapToObj(index -> new TransactionOutputPoint(id, index))
			.collect(toList());
	}
	
	private String calculateHash()
	{
		return Hashing.sha256()
			.hashObject(this, (from, into) ->
			{
				inputs.forEach(input -> into.putString(input.hash(), UTF_8));
				outputs.forEach(output -> into.putString(output.hash(), UTF_8));
			})
			.toString();
	}
}
