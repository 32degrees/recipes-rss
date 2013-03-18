package halfpipe.recipes.rss;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.netflix.config.DynamicStringProperty;

/**
 * User: gibbsb
 * Date: 3/18/13
 * Time: 2:33 AM
 */
public class RibbonConfiguration {
    @JsonProperty
    public DynamicStringProperty listOfServers;
}
