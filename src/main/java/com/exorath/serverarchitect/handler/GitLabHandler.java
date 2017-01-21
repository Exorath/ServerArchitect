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

package com.exorath.serverarchitect.handler;

import java.io.File;
import java.util.Map;

/**
 * Created by toonsev on 11/24/2016.
 */
public class GitLabHandler implements ConfigHandler {
    private static final String TYPE_ID = "type";


    @Override
    public void loadPlugins(Map<String, Object> configSection, File pluginsDir) {

    }

    @Override
    public void loadMaps(Map<String, Object> configSection, File mapsDir) {
        MapType type = configSection.containsKey(TYPE_ID) ? MapType.valueOf((String) configSection.get(TYPE_ID)) : MapType.MAP_IN_REPO;
        switch(type){
            case ALL_MAPS_IN_REPO:
                //todo fix this
                break;

            case MAP_IN_REPO:

                break;
        }

    }

}
