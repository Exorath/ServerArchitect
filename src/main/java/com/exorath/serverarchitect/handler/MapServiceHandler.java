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
import com.exorath.service.map.api.MapServiceAPI;
import com.exorath.service.map.res.DownloadMapReq;
import com.exorath.service.map.res.GetMapsReq;
import com.exorath.service.map.res.GetMapsRes;
import com.exorath.service.map.res.MapInfo;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by toonsev on 1/26/2017.
 */
public class MapServiceHandler implements ConfigHandler {
    @Override
    public void loadMaps(Map<String, Object> configSection, File mapsDir) {
        if (configSection == null)
            return;
        try {
            for (Map.Entry<String, Object> entry : configSection.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    Map<String, Object> mapsMap = (Map) entry.getValue();
                    String mapType = StringLoader.getValue(mapsMap, "type");
                    String address = StringLoader.getValue(mapsMap, "address");
                    String userId = StringLoader.getValue(mapsMap, "userId");
                    if (!configSection.containsKey("address")) {
                        System.out.println("ServerArchitect MapService: No map 'address' provided.");
                        System.exit(1);
                    }
                    if (!configSection.containsKey("userId")) {
                        System.out.println("ServerArchitect MapService: No map 'userId' provided.");
                        System.exit(1);
                    }
                    if (mapType == null || mapType.equalsIgnoreCase("single")) {
                        String mapId = (String) configSection.get("mapId");
                        String envId = (String) configSection.get("envId");
                        if (mapId == null || envId == null) {
                            System.out.println("no 'mapId' or 'envId' defined in architect.yml.");
                            System.exit(1);
                        }
                        String versionId = configSection.containsKey("versionId") ? (String) configSection.get("versionId") : null;
                        loadSingleMap(userId, mapId, envId, versionId, new MapServiceAPI(address), mapsDir);
                    } else if (mapType.equalsIgnoreCase("multi")) {
                        String prefix = (String) configSection.get("prefix");
                        String envId = (String) configSection.get("envId");
                        if (prefix == null || envId == null) {
                            System.out.println("no 'prefix' or 'envId' defined in architect.yml.");
                            System.exit(1);
                        }
                        loadMultipleMaps(userId, prefix, envId, new MapServiceAPI(address), mapsDir);
                    } else {
                        System.out.println("ServerArchitect: The map type " + mapType + " is not a valid mapType. Please use single or multi.");
                        System.exit(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void loadSingleMap(String userId, String mapId, String envId, String versionId, MapServiceAPI mapServiceAPI, File mapsDir) throws Exception {
        File mapDir = new File(mapsDir, mapId);
        mapServiceAPI.downloadMapToFolder(new DownloadMapReq(userId, mapId, envId, versionId), mapDir);
    }

    public boolean loadMultipleMaps(String userId, String prefix, String envId, MapServiceAPI mapServiceAPI, File mapsDir) throws Exception {
        GetMapsRes mapsRes = mapServiceAPI.getMaps(new GetMapsReq(userId, prefix, null, 1000));
        for (Map.Entry<String, MapInfo> entry : mapsRes.getMaps().entrySet())
            loadSingleMap(userId, entry.getKey(), envId, null, mapServiceAPI, mapsDir);
        return true;
    }
}
