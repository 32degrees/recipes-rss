/*
 * Copyright 2012 Netflix, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package halfpipe.recipes.rss.hystrix;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.netflix.client.ClientFactory;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.niws.client.http.HttpClientRequest;
import com.netflix.niws.client.http.HttpClientRequest.Verb;
import com.netflix.niws.client.http.HttpClientResponse;
import com.netflix.niws.client.http.RestClient;
import halfpipe.logging.Log;
import halfpipe.recipes.rss.RSSConstants;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.net.URI;

/**
 * Calls the middle tier Get RSS entry point
 */
public class GetRSSCommand extends HystrixCommand<String> {
    private static final Logger LOG = Log.forThisClass();

	public GetRSSCommand() {
        super (
            Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey(RSSConstants.HYSTRIX_RSS_GET_GROUP))
                .andCommandKey(HystrixCommandKey.Factory.asKey(RSSConstants.HYSTRIX_RSS_GET_COMMAND_KEY))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(RSSConstants.HYSTRIX_RSS_THREAD_POOL)
            )
        );
	}

	@Override
	protected String run() {
		try {
			// The named client param must match the prefix for the ribbon
			// configuration specified in the edge.properties file
			RestClient client = (RestClient) ClientFactory.getNamedClient(RSSConstants.MIDDLETIER_REST_CLIENT);

			HttpClientRequest request = HttpClientRequest.newBuilder()
					.setVerb(Verb.GET)
					.setUri(new URI("/"
							+ RSSConstants.MIDDLETIER_WEB_RESOURCE_ROOT_PATH
							+ RSSConstants.RSS_ENTRY_POINT)
                    )
					.build();
			HttpClientResponse response = client.executeWithLoadBalancer(request);

			return IOUtils.toString(response.getRawEntity(), Charsets.UTF_8);
		} catch (Exception e) {
            Throwables.propagate(e);
		}
        return null;
	}

	@Override
	protected String getFallback() {
        LOG.warn("calling fallback");
		return "{ \"user\": \"default\", \"subscriptions\": [ { \"url\": \"/\", \"title\": \"RSS Reader News\", \"items\": [ { \"title\": \"This is default data for RSS Reader\", \"link\": \"/\", \"description\": \"For some reason, RSS Reader Edge app couldn't find your rss feeds.  Here is something to look at in the meantime.\" } ] } ] }";
	}
}