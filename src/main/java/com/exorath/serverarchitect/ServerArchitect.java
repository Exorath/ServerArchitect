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
import com.exorath.serverarchitect.loader.Loader;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by toonsev on 11/23/2016.
 */
public class ServerArchitect {
    private Map<String, Object> config;
    private Map<String, Loader> loadersByKey = new HashMap<>();

    public ServerArchitect(ConfigProvider configProvider) {
        this.config = configProvider.getConfig();
    }

    public void start() {
        for (Map.Entry<String, Loader> loaderByKey : loadersByKey.entrySet()) {
            Map<String, Object> configSection = (Map) config.get(loaderByKey.getKey());
            if (configSection == null)//There is no configuration for this loader
                continue;
            Loader loader = loaderByKey.getValue();

            loadPluginsFromLoader(loader, configSection);
            loadMapsFromLoader(loader, configSection);
            loadJarFromLoader(loader, configSection);
        }
    }

    private void loadPluginsFromLoader(Loader loader, Map<String, Object> configSection) {
        Map<String, Object> pluginSection = (Map) configSection.get("plugins");
        if (pluginSection != null)
            loader.loadPlugins(configSection, new File("plugins/"));
    }
    private void loadMapsFromLoader(Loader loader, Map<String, Object> configSection) {
        Map<String, Object> mapSection = (Map) configSection.get("maps");
        if (mapSection != null)
            loader.loadMaps(configSection, new File("./"));
    }

    private void loadJarFromLoader(Loader loader, Map<String, Object> configSection) {
        Map<String, Object> jarSection = (Map) configSection.get("jar");
        if (jarSection != null) {
            InputStream jarInput = loader.loadJar(configSection);
            if(jarInput == null)
                return;
            try {
                Files.copy(jarInput, new File("./server.jar").toPath());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
    public void withLoader(String key, Loader loader) {
        loadersByKey.put(key, loader);
    }

}
