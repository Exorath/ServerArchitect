/*
 * Copyright 2017 Exorath
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

package com.exorath.serverarchitect.handler;

import java.io.File;
import java.util.Map;

/**
 * Created by toonsev on 1/25/2017.
 */
public class WebHandler implements ConfigHandler{
    @Override
    public void loadPlugins(Map<String, Object> configSection, File pluginsDir) {

    }

    @Override
    public void loadMaps(Map<String, Object> configSection, File mapsDir) {

    }

    @Override
    public void loadJar(Map<String, Object> configSection, File jarFile) {
        if(configSection.containsKey("url"));
        
        //https://ci.getbukkit.org/job/Spigot/74/artifact/spigot-1.11.2.jar
    }
}
