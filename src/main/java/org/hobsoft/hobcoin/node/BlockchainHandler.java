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
package org.hobsoft.hobcoin.node;

import org.hobsoft.hobcoin.Blockchain;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Spring Webflux handler for getting the blockchain.
 */
@Component
public class BlockchainHandler
{
	private final Blockchain blockchain;
	
	public BlockchainHandler(Blockchain blockchain)
	{
		this.blockchain = blockchain;
	}
	
	public Mono<ServerResponse> get(ServerRequest request)
	{
		return ServerResponse.ok()
			.contentType(APPLICATION_JSON)
			.body(BodyInserters.fromObject(BlockchainView.of(blockchain)));
	}
}
