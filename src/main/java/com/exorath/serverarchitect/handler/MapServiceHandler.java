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

import com.exorath.serverarchitect.lib.StringLoader;

import java.io.File;
import java.util.Map;

/**
 * Created by toonsev on 1/26/2017.
 */
public class MapServiceHandler implements ConfigHandler{
    @Override
    public void loadMaps(Map<String, Object> configSection, File mapsDir) {
        if(configSection == null)
            return;

        for (Map.Entry<String, Object> entry : configSection.entrySet()) {
            if(entry.getValue() instanceof Map){
                String mapType = StringLoader.getValue(configSection, "type");
                if(mapType == null || mapType.equalsIgnoreCase("single")){
                    //single map by id
                }else if(mapType.equalsIgnoreCase("multi")){
                    //Prefixed filter
                }else{
                    System.out.println("ServerArchitect: The map type " + mapType + " is not a valid mapType. Please use single or multi.");
                    System.exit(1);
                }
            }
        }
    }
}
