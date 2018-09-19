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

/**
 * An unspent transaction output (UTXO) within a blockchain.
 */
class UnspentTransactionOutput extends TransactionOutput
{
	private final String transactionId;
	
	private final int transactionOutputIndex;
	
	UnspentTransactionOutput(String transactionId, int transactionOutputIndex, PublicKey recipient, long amount)
	{
		super(recipient, amount);
		
		this.transactionId = transactionId;
		this.transactionOutputIndex = transactionOutputIndex;
	}
	
	public String transactionId()
	{
		return transactionId;
	}
	
	public int transactionOutputIndex()
	{
		return transactionOutputIndex;
	}
}
