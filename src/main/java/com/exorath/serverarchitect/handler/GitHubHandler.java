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


import com.mashape.unirest.http.Unirest;
import org.kohsuke.github.GHAsset;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
            if (!configSection.containsKey("name") || !configSection.containsKey("jar")) {
                System.out.println("GitHub plugin section does not contain 'name' or 'jar' field");
                System.exit(400);
            }
            String name = (String) configSection.get("name");
            String jarFilter = (String) configSection.get("jar");
            String oauth = configSection.containsKey("oauth") ? (String) configSection.get("oauth") : null;
            if(oauth.equals("ENV"))
                oauth = System.getenv("GITHUB_OAUTH");
            GitHub gitHub = oauth == null ? GitHub.connectAnonymously() : GitHub.connectUsingOAuth(oauth);
            if (gitHub == null) {
                System.out.println("GitHub plugin section " + name + "section does not contain 'oauth' field");
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
            System.out.print("Found GitHub plugin jar " + jarAsset.getName() + " which will be downloaded from " + jarAsset.getUrl()+ "...");

            InputStream downloadStream = Unirest.get(jarAsset.getUrl().toString())
                    .queryString("access_token", oauth)
                    .header("Accept", "application/octet-stream")
                    .asBinary().getBody();
            ReadableByteChannel rbc = Channels.newChannel(downloadStream);
            FileOutputStream fos = new FileOutputStream(new File(pluginsDir, jarAsset.getName()));
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            System.out.println(" Downloaded.");


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
}
