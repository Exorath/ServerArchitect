/*
 * Copyright 2016 Exorath
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

package com.exorath.serverarchitect;

import com.exorath.serverarchitect.configProvider.ConfigProvider;
import com.exorath.serverarchitect.handler.ConfigHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by toonsev on 11/23/2016.
 */
public class ServerArchitect {
    private Map<String, Object> handlersConfig;
    private Map<String, ConfigHandler> handlersByKey = new HashMap<>();

    public ServerArchitect(ConfigProvider configProvider) {
        Map<String, Object> topLevelConfig = configProvider.getConfig();
        if(topLevelConfig != null)
            handlersConfig = (Map) topLevelConfig.get("handlers");
    }

    public void start() {
        if(handlersConfig == null)
            return;
        for (Map.Entry<String, ConfigHandler> loaderByKey : handlersByKey.entrySet()) {
            System.out.println("ServerArchitect is now loading " + loaderByKey.getKey() + " config sections.");
            Map<String, Object> configSection = (Map) handlersConfig.get(loaderByKey.getKey());
            if (configSection == null)//There is no configuration for this configHandler
                continue;
            ConfigHandler configHandler = loaderByKey.getValue();

            loadPluginsFromLoader(configHandler, configSection);
            loadMapsFromLoader(configHandler, configSection);
            loadJarFromLoader(configHandler, configSection);
        }
    }

    private void loadPluginsFromLoader(ConfigHandler configHandler, Map<String, Object> configSection) {
        Map<String, Object> pluginSection = (Map) configSection.get("plugins");
        if (pluginSection != null)
            configHandler.loadPlugins(pluginSection, new File("plugins/"));
    }

    private void loadMapsFromLoader(ConfigHandler configHandler, Map<String, Object> configSection) {
        Map<String, Object> mapSection = (Map) configSection.get("maps");
        if (mapSection != null)
            configHandler.loadMaps(mapSection, new File("./"));
    }

    private void loadJarFromLoader(ConfigHandler configHandler, Map<String, Object> configSection) {
        Map<String, Object> jarSection = (Map) configSection.get("jar");
        if (jarSection != null)
            configHandler.loadJar(jarSection, new File("server.jar"));
    }

    public void withHandler(String key, ConfigHandler configHandler) {
        handlersByKey.put(key, configHandler);
    }

}
