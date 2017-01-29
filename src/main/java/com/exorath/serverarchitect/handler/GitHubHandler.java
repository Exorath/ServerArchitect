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
import com.mashape.unirest.http.Unirest;
import com.sun.corba.se.spi.orbutil.fsm.Input;
import org.kohsuke.github.GHAsset;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

/**
 * YML config example:
 * github
 * Created by toonsev on 1/19/2017.
 */
public class GitHubHandler implements ConfigHandler {
    @Override
    public void loadPlugins(Map<String, Object> configSection, File pluginsDir) {
        for (Map.Entry<String, Object> pluginMapById : configSection.entrySet()) {
            if (pluginMapById.getValue() instanceof Map) {
                loadPlugin((Map) pluginMapById.getValue(), pluginsDir);
            }
        }
    }

    private void loadPlugin(Map<String, Object> configSection, File pluginsDir) {
        try {
            String name = StringLoader.getValue(configSection, "name");
            String jarFilter = StringLoader.getValue(configSection, "jar");
            String oauth = StringLoader.getValue(configSection, "oauth");

            if (name == null || jarFilter == null) {
                System.out.println("GitHub plugin section does not contain 'name' or 'jar' field");
                System.exit(400);
            }
            GitHub gitHub = null;
            if(oauth == null){
                System.out.println("GitHub is authenticating anonymously.");
                gitHub = GitHub.connectAnonymously();
            }else{
                System.out.println("GitHub is connecting with oauth.");
                gitHub = GitHub.connectUsingOAuth(oauth);
            }
            if (gitHub == null) {
                System.out.println("GitHub plugin section " + name + "section failed to create a github connection.");
                System.exit(400);
            }
            GHRepository ghRepository = gitHub.getRepository(name);
            if (ghRepository == null) {
                System.out.println("GitHub repository " + name + " doesn't exist.");
                System.exit(404);
            }
            GHAsset jarAsset = getJar(ghRepository, jarFilter);
            if (jarAsset == null) {
                System.out.println("GitHub jar release " + name + " doesn't exist.");
                System.exit(404);
            }
            System.out.print("Found GitHub plugin jar " + jarAsset.getName() + " which will be downloaded from " + jarAsset.getUrl() + "...");

            try (InputStream downloadStream = Unirest.get(jarAsset.getUrl().toString())
                    .queryString("access_token", oauth)
                    .header("Accept", "application/octet-stream")
                    .asBinary().getBody()) {
                    File pluginFile = new File(pluginsDir, jarAsset.getName());
                    try (FileOutputStream fileOutputStream = new FileOutputStream(pluginFile)) {
                        copyStream(downloadStream, fileOutputStream);
                        if(!pluginFile.setReadable(true, false))
                            throw new IllegalStateException("Failed to make a plugin jar readable");

                        System.out.println(" Downloaded (size: " + pluginFile.length() + " bytes).");
                }
            }
            System.out.println("GitHub Rate-Limit:" + gitHub.getRateLimit().remaining + " API calls remaining");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(400);
        }
    }

    private GHAsset getJar(GHRepository repository, String jarRegex) throws IOException {
        for (GHRelease ghRelease : repository.listReleases()) {
            for (GHAsset asset : ghRelease.getAssets()) {
                if (asset.getName().matches(jarRegex)) {
                    return asset;
                }
            }
        }
        return null;
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
}
