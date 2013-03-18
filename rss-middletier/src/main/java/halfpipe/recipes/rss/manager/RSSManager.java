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
package halfpipe.recipes.rss.manager;

import com.netflix.client.ClientFactory;
import com.netflix.karyon.spi.HealthCheckHandler;
import com.netflix.niws.client.http.HttpClientRequest;
import com.netflix.niws.client.http.HttpClientResponse;
import com.netflix.niws.client.http.RestClient;
import halfpipe.logging.Log;
import halfpipe.recipes.rss.*;
import halfpipe.recipes.rss.impl.RSSImpl;
import halfpipe.recipes.rss.impl.RSSItemImpl;
import halfpipe.recipes.rss.impl.SubscriptionsImpl;
import halfpipe.recipes.rss.model.RSS;
import halfpipe.recipes.rss.model.RSSItem;
import halfpipe.recipes.rss.model.RSSStore;
import halfpipe.recipes.rss.model.Subscriptions;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * RSS Manager that
 *  1) Fetches content from RSS feeds using Ribbon
 *  2) Parses RSS feeds
 *  3) Persists feed urls into
 *      a) Cassandra using Astyanax (or)
 *      b) InMemoryStore
 */
@Service
public class RSSManager implements HealthCheckHandler {
    private static final Log LOG = Log.forThisClass();

    @Inject
    private RSSStore store;

    /*private RSSManager() {
        if (RSSConstants.RSS_STORE_CASSANDRA.equals(
                DynamicPropertyFactory.getInstance().getStringProperty(RSSConstants.RSS_STORE, RSSConstants.RSS_STORE_CASSANDRA).get())) {
            //store = new CassandraStoreImpl();
            store = new InMemoryStoreImpl();
        } else {
            store = new InMemoryStoreImpl();
        }
    }*/

    /**
     * Fetches the User subscriptions
     */
    public Subscriptions getSubscriptions(String userId) throws Exception {
        List<String> feedUrls = store.getSubscribedUrls(userId);
        List<RSS> feeds = new ArrayList<RSS>(feedUrls.size());
        for (String feedUrl: feedUrls) {
            RSS rss = fetchRSSFeed(feedUrl);
            if (rss.getItems() != null && !rss.getItems().isEmpty()) {
                feeds.add(rss);
            }
        }

        return new SubscriptionsImpl(userId, feeds);
    }

    /**
     * Add subscription
     */
    public void addSubscription(String user, String decodedUrl) throws Exception {
        if (decodedUrl == null) throw new IllegalArgumentException("url cannot be null");
        store.subscribeUrl(user, decodedUrl);
    }

    /**
     * Delete subscription
     */
    public void deleteSubscription(String user, String decodedUrl) throws Exception {
        if (decodedUrl == null) throw new IllegalArgumentException("url cannot be null");
        store.unsubscribeUrl(user, decodedUrl);
    }

    /**
     * Fetch the RSS feed content using Ribbon
     */
    private RSS fetchRSSFeed(String url) {

        RestClient client = (RestClient) ClientFactory.getNamedClient(RSSConstants.MIDDLETIER_REST_CLIENT);
        HttpClientResponse response;
        String rssData = null;

        try {
            HttpClientRequest request = HttpClientRequest.newBuilder().setUri(new URI(url)).build();
            response = client.execute(request);

            if (response != null) {
                rssData  = IOUtils.toString(response.getRawEntity(), Charsets.UTF_8);
                LOG.info("Status code for " + response.getRequestedURI() + " : " + response.getStatus());
            }
        } catch (URISyntaxException e) {
            LOG.error("Exception occurred when setting the URI", e);
        } catch (Exception e) {
            LOG.error("Exception occurred when executing the HTTP request", e);
        }

        return parseRSS(url, rssData);
    }

    /**
     * Parses the RSS feeds and return back a POJO
     */
    private RSS parseRSS(String url, String rss) {
        // Error case
        if (rss == null) return new RSSImpl();
        
        RSS rssItems = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            try {
                InputSource is = new InputSource(new StringReader(rss));
                Document dom = db.parse(is);
                Element docEle = dom.getDocumentElement();
                
                List<RSSItem> items = new ArrayList<RSSItem>();
                String title = docEle.getElementsByTagName("title").item(0).getTextContent();
                NodeList nl  = docEle.getElementsByTagName("item");
                if (nl != null && nl.getLength() > 0) {
                    for (int i = 0 ; i < nl.getLength(); i++) {
                        Element el = (Element) nl.item(i);
                        items.add(new RSSItemImpl(el.getElementsByTagName("title").item(0).getTextContent(), el.getElementsByTagName("link").item(0).getTextContent(), el.getElementsByTagName("description").item(0).getTextContent()));
                    }
                }
                rssItems = new RSSImpl(url, title, items);
                
            } catch (SAXException e) {
                LOG.error("Exception occurred during parsing the RSS feed", e);
            } catch (IOException e) {
                LOG.error("Exception occurred during fetching the RSS feed", e);
            }
        } catch (ParserConfigurationException e) {
            LOG.error("Exception occurred during parsing the RSS feed", e);
        }

        if (rssItems == null) {
            rssItems = new RSSImpl();
        }
        return rssItems;
    }

    public int getStatus() {
        return store == null ? 500 : 200;
    }
}