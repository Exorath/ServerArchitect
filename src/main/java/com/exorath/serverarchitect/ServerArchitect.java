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
import com.exorath.serverarchitect.loader.ConfigHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by toonsev on 11/23/2016.
 */
public class ServerArchitect {
    private Map<String, Object> config;
    private Map<String, ConfigHandler> loadersByKey = new HashMap<>();

    public ServerArchitect(ConfigProvider configProvider) {
        this.config = configProvider.getConfig();
    }

    public void start() {
        for (Map.Entry<String, ConfigHandler> loaderByKey : loadersByKey.entrySet()) {
            Map<String, Object> configSection = (Map) config.get(loaderByKey.getKey());
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
            configHandler.loadPlugins(configSection, new File("plugins/"));
    }

    private void loadMapsFromLoader(ConfigHandler configHandler, Map<String, Object> configSection) {
        Map<String, Object> mapSection = (Map) configSection.get("maps");
        if (mapSection != null)
            configHandler.loadMaps(configSection, new File("./"));
    }

    private void loadJarFromLoader(ConfigHandler configHandler, Map<String, Object> configSection) {
        Map<String, Object> jarSection = (Map) configSection.get("jar");
        if (jarSection != null)
            configHandler.loadJar(configSection, new File("server.jar"));
    }

    public void withLoader(String key, ConfigHandler configHandler) {
        loadersByKey.put(key, configHandler);
    }

}
