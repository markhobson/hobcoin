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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import com.google.common.hash.Hashing;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * An input to a transaction that is an output of a previous transaction signed by the sender.
 */
public class TransactionInput
{
	private final TransactionOutputPoint transactionOutputPoint;
	
	private final byte[] signature;
	
	private final String hash;
	
	public TransactionInput(UnspentTransactionOutput unspentTransactionOutput)
	{
		this(unspentTransactionOutput.transactionOutputPoint());
	}
	
	public TransactionInput(TransactionOutputPoint transactionOutputPoint)
	{
		this(transactionOutputPoint, null);
	}
	
	private TransactionInput(TransactionOutputPoint transactionOutputPoint, byte[] signature)
	{
		this.transactionOutputPoint = transactionOutputPoint;
		this.signature = signature;
		
		hash = calculateHash();
	}
	
	public TransactionOutputPoint transactionOutputPoint()
	{
		return transactionOutputPoint;
	}
	
	public String hash()
	{
		return hash;
	}
	
	public byte[] signature()
	{
		return signature;
	}
	
	public boolean signed()
	{
		return signature != null;
	}
	
	public TransactionInput sign(PrivateKey privateKey)
	{
		try
		{
			Signature signer = Signature.getInstance("SHA256withECDSA");
			signer.initSign(privateKey);
			signer.update(data());
			byte[] signature = signer.sign();
			
			return new TransactionInput(transactionOutputPoint, signature);
		}
		catch (GeneralSecurityException | IOException exception)
		{
			throw new HobcoinException("Error signing transaction input", exception);
		}
	}
	
	public boolean verify(PublicKey address) throws GeneralSecurityException, IOException
	{
		Signature verifier = Signature.getInstance("SHA256withECDSA");
		verifier.initVerify(address);
		verifier.update(data());
		return verifier.verify(signature);
	}
	
	private byte[] data() throws IOException
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bytes);
		out.writeUTF(transactionOutputPoint.transactionId());
		return bytes.toByteArray();
	}

	private String calculateHash()
	{
		return Hashing.sha256()
			.hashObject(this, (from, into) -> into
				.putString(transactionOutputPoint.transactionId(), UTF_8)
				.putInt(transactionOutputPoint.transactionOutputIndex())
			)
			.toString();
	}
}
