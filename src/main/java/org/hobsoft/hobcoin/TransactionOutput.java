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

import com.google.common.hash.Hashing;

/**
 * An output from a transaction that credits the recipient.
 */
public class TransactionOutput
{
	private final PublicKey recipient;
	
	private final long amount;
	
	private final String hash;
	
	public TransactionOutput(PublicKey recipient, long amount)
	{
		this.recipient = recipient;
		this.amount = amount;
		
		hash = calculateHash();
	}
	
	public PublicKey recipient()
	{
		return recipient;
	}
	
	public long amount()
	{
		return amount;
	}
	
	public String hash()
	{
		return hash;
	}
	
	private String calculateHash()
	{
		return Hashing.sha256()
			.hashObject(this, (from, into) -> into
				.putBytes(recipient.getEncoded())
				.putLong(amount)
			)
			.toString();
	}
}
