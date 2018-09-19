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

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * A keypair used to address and sign individuals on the blockchain.
 */
public class Wallet
{
	private final KeyPair keyPair;
	
	public Wallet()
	{
		keyPair = generateKeyPair();
	}
	
	public PublicKey address()
	{
		return keyPair.getPublic();
	}
	
	public long amount(Blockchain blockchain)
	{
		return blockchain.unspentTransactionOutputs(address(), 0)
			.stream()
			.mapToLong(TransactionOutput::amount)
			.sum();
	}
	
	public Transaction transfer(Blockchain blockchain, PublicKey recipient, long amount)
	{
		List<UnspentTransactionOutput> unspentOutputs = blockchain.unspentTransactionOutputs(address(), amount);

		long unspentOutputsAmount = unspentOutputs.stream()
			.mapToLong(UnspentTransactionOutput::amount)
			.sum();
		
		List<TransactionInput> inputs = unspentOutputs.stream()
			.map(TransactionInput::new)
			.map(input -> input.sign(keyPair.getPrivate()))
			.collect(toList());
		
		List<TransactionOutput> outputs = new ArrayList<>();
		outputs.add(new TransactionOutput(recipient, amount));
		outputs.add(new TransactionOutput(address(), unspentOutputsAmount - amount));
		
		return new Transaction(inputs, outputs);
	}
	
	private static KeyPair generateKeyPair()
	{
		try
		{
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			keyGen.initialize(ecSpec, random);
			
			return keyGen.generateKeyPair();
		}
		catch (GeneralSecurityException exception)
		{
			throw new HobcoinException("Error generating wallet keypair", exception);
		}
	}
}
