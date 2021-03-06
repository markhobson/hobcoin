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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code Transaction}.
 */
public class TransactionTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void canCreateTransaction()
	{
		TransactionInput input = new TransactionInput(new TransactionOutputPoint("123", 4));
		TransactionOutput output = new TransactionOutput(new Wallet().address(), 4);
		
		Transaction transaction = new Transaction(singletonList(input), singletonList(output));
		
		assertThat("id", transaction.id(), notNullValue(String.class));
		assertThat("inputs", transaction.inputs(), contains(input));
		assertThat("outputs", transaction.outputs(), contains(output));
	}
	
	@Test
	public void cannotCreateTransactionWithNoInputs()
	{
		TransactionOutput output = new TransactionOutput(new Wallet().address(), 4);

		thrown.expect(IllegalArgumentException.class);
		
		new Transaction(emptyList(), singletonList(output));
	}
	
	@Test
	public void cannotCreateTransactionWithNoOutputs()
	{
		TransactionInput input = new TransactionInput(new TransactionOutputPoint("123", 4));

		thrown.expect(IllegalArgumentException.class);
		
		new Transaction(singletonList(input), emptyList());
	}
}
