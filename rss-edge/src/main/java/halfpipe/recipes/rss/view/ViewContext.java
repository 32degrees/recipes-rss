package halfpipe.recipes.rss.view;

import halfpipe.context.AbstractViewContext;
import halfpipe.context.MetricsContext;
import halfpipe.mgmt.view.MgmtControllers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * User: gibbsb
 * Date: 3/17/13
 * Time: 11:25 PM
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = {ViewContext.class, MgmtControllers.class}, excludeFilters = {
        @ComponentScan.Filter(Configuration.class)
})
//TODO: @EnableMetrics
@Import({MetricsContext.class})
public class ViewContext extends AbstractViewContext {
    //TODO: is this needed, can I set a welcome page?
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/static/index.html");
    }

    //TODO: import FreeMarkerContext?
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer config = new FreeMarkerConfigurer();
        config.setTemplateLoaderPath("classpath:freemarker"); //TODO: freemarker config
        return config;
    }

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver view = new FreeMarkerViewResolver();
        view.setCache(true);
        view.setPrefix("");
        view.setSuffix(".ftl");
        return view;
    }
}
