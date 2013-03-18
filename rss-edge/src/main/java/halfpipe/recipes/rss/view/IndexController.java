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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.Future;

/**
 * User: gibbsb
 * Date: 3/17/13
 * Time: 11:33 PM
 */
@Controller
public class IndexController {
    private static final Log LOG = Log.forThisClass();

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

        // Add a RSS feed
        } else if (url != null) {
            url = URLEncoder.encode(url, "UTF-8");
            HystrixCommand<String> addCommand = new AddRSSCommand(url);
            Future<String> future = addCommand.queue();
            future.get();
        }


        // Get RSS feeds
        HystrixCommand<String> getCommand = new GetRSSCommand();
        Future<String> future = getCommand.queue();
        String responseString = future.get();
        //String responseString = "{ \"user\": \"default\", \"subscriptions\": [ { \"url\": \"http://rss.msnbc.msn.com/id/3032091/device/rss/rss.xml\", \"title\": \"NBCNews.com: Top NBCNews headlines\", \"items\": [ { \"title\": \"Why American men still choose to be priests\", \"link\": \"http://usnews.nbcnews.com/_news/2013/03/17/17297531-im-not-going-to-see-pearl-jam-anymore-seminarians-prepare-for-life-as-priests?lite\", \"description\": \"<p><a href=\\\"http://usnews.nbcnews.com/_news/2013/03/17/17297531-im-not-going-to-see-pearl-jam-anymore-seminarians-prepare-for-life-as-priests?lite\\\"><img align=\\\"left\\\" border=\\\"0\\\" src=\\\"http://msnbcmedia.msn.com/j/MSNBC/Components/Photo/_new/130313-seminary-student-jsw-1111a.thumb.jpg\\\"  style=\\\"margin:0 5px 5px 0\\\" /></a>Battling scandal and waning enthusiasm in the United States and Europe, the Catholic Church will rely on these aspiring priests and their peers to keep the faith.</p><br clear=\\\"all\\\" />\" }, { \"title\": \"New pope's Sunday surprises delight, startle\", \"link\": \"http://worldnews.nbcnews.com/_news/2013/03/17/17345676-impromptu-appearance-off-the-cuff-address-popes-sunday-surprises-delight?lite\", \"description\": \"<p><a href=\\\"http://worldnews.nbcnews.com/_news/2013/03/17/17345676-impromptu-appearance-off-the-cuff-address-popes-sunday-surprises-delight?lite\\\"><img align=\\\"left\\\" border=\\\"0\\\" src=\\\"http://msnbcmedia.msn.com/j/MSNBC/Components/Photo/_new/130317_pope_sunday.thumb.jpg\\\" alt=\\\"A crowd of more than 150,000 people roared in delight as Pope Francis made the first Sunday window appearance of his papacy in St. Peter's Square.\\\" style=\\\"margin:0 5px 5px 0\\\" /></a>A crowd of more than 150,000 people roared in delight as Pope Francis made the first Sunday window appearance of his papacy in St. Peter's Square.</p><br clear=\\\"all\\\" />\" }, { \"title\": \"Guilty verdicts in Steubenville high school rape trial\", \"link\": \"http://usnews.nbcnews.com/_news/2013/03/17/17346127-verdict-in-steubenville-high-school-rape-trial?lite\", \"description\": \"Two Ohio high school football players have been found guilty of raping a drunken 16-year-old girl in a case that roiled a small city and stirred reaction from activists online.\" }, { \"title\": \"On the Brink: Israel to grill Obama over Iran\", \"link\": \"http://worldnews.nbcnews.com/_news/2013/03/17/17306968-on-the-brink-israel-to-grill-obama-over-possible-military-strike-on-iran?lite\", \"description\": \"<p><a href=\\\"http://worldnews.nbcnews.com/_news/2013/03/17/17306968-on-the-brink-israel-to-grill-obama-over-possible-military-strike-on-iran?lite\\\"><img align=\\\"left\\\" border=\\\"0\\\" src=\\\"http://msnbcmedia.msn.com/j/MSNBC/Components/Photo/_new/130314-fletcher-iran-ahmadeinejad.thumb.jpg\\\" alt=\\\"Israelâ€™s Prime Minister Benjamin Netanyahu will have one key question for President Barack Obama when they meet Wednesday:cher.</p><br clear=\\\"all\\\" />\" }, { \"title\": \"NCAA tourney bracket unveiled; Louisville No. 1 overall\", \"link\": \"http://nbcsports.msnbc.com/id/51218547/ns/sports-college_basketball/\", \"description\": \"<p><a href=\\\"http://nbcsports.msnbc.com/id/51218547/ns/sports-college_basketball/\\\"><img align=\\\"left\\\" border=\\\"0\\\" src=\\\"http://msnbcmedia.msn.com/j/NBCSports/Sections/College basketball/Photos/130317_indiana.thumb.jpg\\\" alt=\\\"Big East tournament champion Louisville earned the No. 1 overall seed and top spot in the Midwest Region.\\\" style=\\\"margin:0 5px 5px 0\\\" /></a>The 2013 NCAA tournament bracket was unveiled on Sunday night, with Louisville earning the No. 1 overall seed.</p><br clear=\\\"all\\\" />\" } ] }, { \"url\": \"http://feeds.feedburner.com/TechCrunch/\", \"title\": \"TechCrunch\", \"items\": [ { \"title\": \"Messaging Service WhatsApp To Extend Subscription Model To iOS This Year, But Don't Hold Your Breath For A Desktop App\", \"link\": \"http://feedproxy.google.com/~r/Techcrunch/~3/JxzgmbxuOoE/\", \"description\": \"<img width=\\\"100\\\" height=\\\"70\\\" src=\\\"http://tctechcrunch2011.files.wordpress.com/2011/04/whatsapp.png?w=100&amp;h=70&amp;crop=1\\\" class=\\\"attachment-tc-carousel-river-thumb wp-post-image\\\" alt=\\\"whatsapp\\\" style=\\\"float: left; margin: 0 10px 7px 0;\\\" /><a target=\\\"_blank\\\" href=\\\"http://www.whatsapp.com/?l=en\\\">WhatsApp</a>, the popular mobile messaging app that eschews advertising in favor of a paid model, is getting ready to bring its iOS app in line with the apps it makes for other platforms by turni.\\n\" }, { \"title\": \"Note-Taking Service Google Keep Briefly Appears Before Disappearing Again\", \"link\": \"http://feedproxy.google.com/~r/Techcrunch/~3/uN9ClkkuFl4/\", \"description\": \"<img width=\\\"100\\\" height=\\\"70\\\" src=\\\"http://tctechcrunch2011.files.wordpress.com/2013/03/googlekeep.jpg?w=100&amp;h=70&amp;crop=1\\\" class=\\\"attachment-tc-carousel-river-thumb wp-post-image\\\" alt=\\\"GoogleKeep\\\" style=\\\"float: left; margin: 0 10px 7px 0;\\\" />Note-taking app Google Keep briefly went live earlier today at <a target=\\\"_blank\\\" href=\\\"https://drive.google.com/keep/u/1/\\\">http://drive.google.com/keep/</a> before disappearing, but not before <a target=\\\"_blank\\\" href=\\\"http://www.androidpolice.com/2013/03/17/google-keep-is-live-google-launches-a-note-taking-service/\\\">Android Police spotted it </a>and took screenshots.\" }, { \"title\": \"Uber, Lyft, SideCar, And The So-Called Safety Problem\", \"link\": \"http://feedproxy.google.com/~r/Techcrunch/~3/ZWcNk3mmKPk/\", \"description\": \"<img width=\\\"100\\\" height=\\\"70\\\" src=\\\"http://tctechcrunch2011.files.wordpress.com/2013/03/3090392251_911be4dfaf.jpg?w=100&amp;h=70&amp;crop=1\\\" class=\\\"attachment-tc-carousel-river-thumb wp-post-image\\\" alt=\\\"woop woop police\\\" style=\\\"float: left; margin: 0 10px 7px 0;\\\" />Uber is unsafe. Lyft, SideCar, and other ride-sharing services are unsafe. At the very least, there is the question of their safety for passengers. And why? Because they represent a new type of technology, a new way of doing things, and that is inherently scary. \" }, { \"title\": \"Military ID Verification Service, Troop ID, Raises $2.1 Million\", \"link\": \"http://feedproxy.google.com/~r/Techcrunch/~3/ZK_eW07HUXg/\", \"description\": \"<img width=\\\"100\\\" height=\\\"70\\\" src=\\\"http://tctechcrunch2011.files.wordpress.com/2013/03/logo2.png?w=100&amp;h=70&amp;crop=1\\\" class=\\\"attachment-tc-carousel-river-thumb wp-post-image\\\" alt=\\\"logo\\\" style=\\\"float: left; margin: 0 10px 7px 0;\\\" />Iraq War veteran and Troop ID founder, Blake Hall, has scored a cool $2.1 million from notable investors, such as David Tisch and Mark McLaughlin. The former elite soldier has raised a total of $5.7M for his identity service that verifies military credentials and partners them with brands.\" }, { \"title\": \"Sharp's Financial Woes Continue As It Misses Deadline For $120M Qualcomm Investment\", \"link\": \"http://feedproxy.google.com/~r/Techcrunch/~3/f5UCLa7r3Tw/\", \"description\": \"<img width=\\\"100\\\" height=\\\"70\\\" src=\\\"http://tctechcrunch2011.files.wordpress.com/2013/03/sharp.jpg?w=100&amp;h=70&amp;crop=1\\\" class=\\\"attachment-tc-carousel-river-thumb wp-post-image\\\" alt=\\\"sharp\\\" style=\\\"float: left; margin: 0 10px 7px 0;\\\" />In another setback for the Japanese electronics giant, <a target=\\\"_blank\\\" href=\\\"http://sharp-world.com/corporate/ir/topics/pdf/130318.pdf\\\">Sharp said it will miss a March 29 deadline</a> f\" } ] }, { \"url\": \"http://rss.cnn.com/rss/edition.rss\", \"title\": \"CNN.com - Top Stories\", \"items\": [ { \"title\": \"Bono: Fight to end extreme poverty\", \"link\": \"http://edition.cnn.com/2013/03/17/opinion/elliott-bono-ted-poverty/index.html?eref=edition\", \"description\": \"Bono says if the current trajectory can hold, extreme poverty could be eliminated by 2030\" }, { \"title\": \"Ohio rape case a perfect storm\", \"link\": \"http://edition.cnn.com/2013/03/17/opinion/simmons-steubenville-verdict/index.html?eref=edition\", \"description\": \"The Steubenville, Ohio, rape case has made national headlines because it represents a perfect storm of inappropriate sexual conduct, high-school football heroes, social media and viral YouTube videos. \" }, { \"title\": \"How will history judge the Iraq War?\", \"link\": \"http://edition.cnn.com/video/#/video/bestoftv/2013/03/17/exp-sotu-how-will-history-judge-the-iraq-war.cnn?eref=edition\", \"description\": \"Ten years after the U.S.-led invasion of Iraq, it's unclear how the war will be judged through the lens of history. \" }, { \"title\": \"Sudan judges taught to amputate?\", \"link\": \"http://edition.cnn.com/2013/03/16/opinion/opinion-amnesty-international-sudan-amputations/index.html?eref=edition\", \"description\": \"Sudan's Deputy Chief Justice recently made the alarming announcement that his government might start training judges to cut off the hands and legs of convicted criminals, if doctors refuse to carry out amputations as punishment.\" }, { \"title\": \"Why Mars water revelation is big deal\", \"link\": \"http://edition.cnn.com/2013/03/14/opinion/bell-mars-water/index.html?eref=edition\", \"description\": \"An announcement on Tuesday marked, quite literally, a watershed moment in the history of solar system exploration. NASA scientists said an analysis of drilled rock samples collected by the Curiosity rover shows that ancient Mars could have supported living microbes.\" } ] }, { \"url\": \"http://rss.cnn.com/rss/money_autos.rss\", \"title\": \"Auto news - CNNMoney.com\", \"items\": [ { \"title\": \"Toyota recalling 200,000 FJ Cruisers for seatbelt issue\", \"link\": \"http://money.cnn.com/2013/03/15/autos/toyota-fj-cruiser-recall/index.html?section=money_autos\", \"description\": \"Front seatbelt mountings, which are attached to the back doors, can begin to come loose with repeated door slams.\" }, { \"title\": \"Honda recalling 180,000 vehicles for brake problem\", \"link\": \"http://money.cnn.com/2013/03/14/autos/honda-acura-recall/index.html?section=money_autos\", \"description\": \"Read full story for latest details.\" }, { \"title\": \"Can General Motors remake its rental car?\", \"link\": \"http://features.blogs.fortune.cnn.com/2013/03/14/can-general-motors-remake-its-rental-car/?section=money_autos\", \"description\": \"The Chevrolet Impala -- a favorite of dreary rental lots -- is getting a do-or-die makeover.\" }, { \"title\": \"Henrik Fisker resigns from plug-in car company Fisker \", \"link\": \"http://money.cnn.com/2013/03/13/autos/henrik-fisker-resigns/index.html?section=money_autos\", \"description\": \"Read full story for latest details.\" }, { \"title\": \"Million-dollar cars from Amelia Island auctions\", \"link\": \"http://money.cnn.com/gallery/autos/2013/03/13/ameila-island-million-dollar-cars/index.html?section=money_autos\", \"description\": \"Rolls-Royce, Ferrari and Duesenberg are among the cars that sold for top-dollar at the Florida collector car event.\" } ] } ] }\n";
        // When a user has only 1 subcription, middle tier returns a jsonobject instead of an array
        /*final JSONObject jo = new JSONObject(responseString);
        JSONArray subscriptions = new JSONArray();
        if (jo.has("subscriptions")) {
            if (jo.get("subscriptions") instanceof JSONObject) {
                subscriptions.put(jo.get("subscriptions"));
            } else {
                subscriptions = jo.getJSONArray("subscriptions");
            }
        }*/

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
