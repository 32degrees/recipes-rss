package halfpipe.recipes.rss.manager;

import com.netflix.karyon.spi.HealthCheckHandler;

public class MiddleTierHealthCheckHandler implements HealthCheckHandler {

    public int getStatus() {
        return 0;//TODO: RSSManager.getInstance().getStatus();
    }

}
