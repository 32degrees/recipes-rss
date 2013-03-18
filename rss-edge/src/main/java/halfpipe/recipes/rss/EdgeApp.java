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
package halfpipe.recipes.rss;

import com.netflix.config.ConfigurationManager;
import halfpipe.Application;
import halfpipe.recipes.rss.view.ViewContext;

/**
 * Edge Server
 * 
 * @author Chris Fregly (chris@fregly.com)
 */
public class EdgeApp extends Application<Context> {

	public static void main(final String[] args) throws Exception {
        //TODO: move to configuration
        ConfigurationManager.getConfigInstance().setProperty("middletierClient.ribbon.OkToRetryOnAllOperations", "false");
        ConfigurationManager.getConfigInstance().setProperty("middletierClient.ribbon.ConnectTimeout", "100000");
        ConfigurationManager.getConfigInstance().setProperty("middletierClient.ribbon.ReadTimeout", "100000");
        //ConfigurationManager.getConfigInstance().setProperty("middletierClient.ribbon.listOfServers", "localhost:9191");
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.RSSGet.execution.isolation.thread.timeoutInMilliseconds", "100000");
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.RSSAdd.execution.isolation.thread.timeoutInMilliseconds", "100000");
		new EdgeApp().run(args);
	}

    @Override
    protected Class<?> getViewContext() {
        return ViewContext.class;
    }
}
