package halfpipe.recipes.rss.view;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.hystrix.HystrixCommand;
import com.yammer.metrics.annotation.Timed;
import halfpipe.logging.Log;
import halfpipe.recipes.rss.hystrix.AddRSSCommand;
import halfpipe.recipes.rss.hystrix.DeleteRSSCommand;
import halfpipe.recipes.rss.hystrix.GetRSSCommand;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.Future;

/**
 * User: spencergibb
 * Date: 3/17/13
 * Time: 11:33 PM
 */
@Controller
public class IndexController {
    private static final Logger LOG = Log.forThisClass();

    @RequestMapping("/")
    @Timed
    public ModelAndView index(@RequestParam(value = "delFeedUrl", required = false) String delFeedUrl,
                              @RequestParam(value = "url", required = false) String url) throws Exception {
        LOG.warn("IndexController: Passing through...");

        HashMap<String, Object> map = Maps.newHashMap();

        // Delete a RSS feed
        if (delFeedUrl != null) {
            HystrixCommand<String> deleteCommand = new DeleteRSSCommand(delFeedUrl);
            Future<String> future = deleteCommand.queue();
            future.get();
            return new ModelAndView("redirect:/", map);

        // Add a RSS feed
        } else if (url != null) {
            url = URLEncoder.encode(url, "UTF-8");
            HystrixCommand<String> addCommand = new AddRSSCommand(url);
            Future<String> future = addCommand.queue();
            future.get();
            return new ModelAndView("redirect:/", map);
        }

        // Get RSS feeds
        HystrixCommand<String> getCommand = new GetRSSCommand();
        Future<String> future = getCommand.queue();
        String responseString = future.get();

        if (!Strings.isNullOrEmpty(responseString)) {
            JSONObject o = (JSONObject) JSONValue.parse(responseString);
            Object subscriptions = o.get("subscriptions");
            if (subscriptions == null) {
                map.put("subscriptions", Lists.newArrayList());
            } else {
                map.put("subscriptions", subscriptions);
            }
        } else {
            map.put("subscriptions", Lists.newArrayList());
        }


        return new ModelAndView("index", map);
    }
}
