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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code TransactionOutputPoint}.
 */
public class TransactionOutputPointTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void cannotCreatePointWithNullTransactionId()
	{
		thrown.expect(NullPointerException.class);
		
		new TransactionOutputPoint(null, 4);
	}
	
	@Test
	public void cannotCreatePointWithNegativeTransactionOutputIndex()
	{
		thrown.expect(IndexOutOfBoundsException.class);
		
		new TransactionOutputPoint("123", -1);
	}
	
	@Test
	public void canCalculateHashcode()
	{
		TransactionOutputPoint outputPoint1 = new TransactionOutputPoint("123", 4);
		TransactionOutputPoint outputPoint2 = new TransactionOutputPoint("123", 4);
		
		assertThat(outputPoint1.hashCode(), is(outputPoint2.hashCode()));
	}
	
	@Test
	public void canDetermineEquality()
	{
		TransactionOutputPoint outputPoint1 = new TransactionOutputPoint("123", 4);
		TransactionOutputPoint outputPoint2 = new TransactionOutputPoint("123", 4);
		
		assertThat(outputPoint1, is(outputPoint2));
	}
}
