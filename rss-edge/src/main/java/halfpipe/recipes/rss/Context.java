package halfpipe.recipes.rss;

import halfpipe.context.DefaultContext;
import halfpipe.context.MetricsContext;
import halfpipe.mgmt.resources.GCResource;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * User: spencergibb
 * Date: 3/17/13
 * Time: 8:32 PM
 */
@Configuration
@ComponentScan(basePackageClasses = {Context.class, GCResource.class}, excludeFilters = {
        @ComponentScan.Filter(Controller.class),
        @ComponentScan.Filter(Configuration.class)
})
@ImportResource("classpath:META-INF/spring/applicationContext-defaultSecurity.xml")
@Import({DefaultContext.class, MetricsContext.class})
public class Context {

    @Bean
    public GCResource garbageCollectionTask() {
        return new GCResource();
    }

}
