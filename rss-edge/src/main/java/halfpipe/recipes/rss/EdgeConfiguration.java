package halfpipe.recipes.rss;

import com.fasterxml.jackson.annotation.JsonProperty;
import halfpipe.configuration.Configuration;
import org.springframework.stereotype.Component;

/**
 * User: spencergibb
 * Date: 3/17/13
 * Time: 9:22 PM
 */
@Component
public class EdgeConfiguration extends Configuration {

    @JsonProperty
    public RestClientConfiguration middleTierClient;
}
