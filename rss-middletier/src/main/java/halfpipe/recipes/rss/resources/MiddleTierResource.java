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
package halfpipe.recipes.rss.resources;

import com.yammer.metrics.annotation.Timed;
import halfpipe.logging.Log;
import halfpipe.recipes.rss.Subscriptions;
import halfpipe.recipes.rss.manager.RSSManager;
import com.netflix.servo.DefaultMonitorRegistry;
import com.netflix.servo.monitor.*;
import com.netflix.servo.stats.StatsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

/**
 * Rest entry points for fetching/adding/deleting RSS feeds.
 * RSS Edge service will be calling these APIs
 */
@Component
@Path("/middletier")
public class MiddleTierResource {
    private static final Log LOG = Log.forThisClass();

    @GET
    @Path("/rss/user/{user}")
    @Produces({MediaType.APPLICATION_JSON})
    @Timed
    public Response fetchSubscriptions (final @PathParam("user") String user) {
        try {
            Subscriptions subscriptions = RSSManager.getInstance().getSubscriptions(user);
            return Response.ok(subscriptions).build();
        } catch (Exception e) {
            LOG.error("Exception occurred when fetching subscriptions", e);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/rss/user/{user}")
    @Produces({MediaType.APPLICATION_JSON})
    @Timed
    public Response subscribe (
            final @QueryParam("url") String url,
            final @PathParam("user") String user) {
        try {
            String decodedUrl = URLDecoder.decode(url, "UTF-8");
            RSSManager.getInstance().addSubscription(user, decodedUrl);

            return Response.ok().build();
        } catch (Exception e) {
            LOG.error("Exception occurred during subscription", e);
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/rss/user/{user}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response unsubscribe (
            final @QueryParam("url") String url,
            final @PathParam("user") String user) {
        try {
            String decodedUrl = URLDecoder.decode(url, "UTF-8");
            RSSManager.getInstance().deleteSubscription(user, decodedUrl);

            return Response.ok().build();
        } catch (Exception e) {
            LOG.error("Exception occurred during un-subscription", e);
            return Response.serverError().build();
        }
    }
}