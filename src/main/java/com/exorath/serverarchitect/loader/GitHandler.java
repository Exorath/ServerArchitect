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

package com.exorath.serverarchitect.loader;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by toonsev on 11/24/2016.
 */
public class GitHandler implements ConfigHandler {
    private static final String TYPE_ID = "type";
    private static final String URI_ID = "uri";
    private File repositoriesPath;
    private Map<String, Repository> mapsRepoByDirName = new HashMap<>();

    public GitHandler() throws IOException, GitAPIException {
        repositoriesPath = File.createTempFile("gitHandlerRepos", "");
        if (!repositoriesPath.delete())
            throw new IOException("Could not delete temporary file " + repositoriesPath);
    }

    @Override
    public void loadMaps(Map<String, Object> configSection, File mapsDir) throws GitAPIException {
        MapType type = configSection.containsKey(TYPE_ID) ? MapType.valueOf((String) configSection.get(TYPE_ID)) : MapType.MAP_IN_REPO;
        String repoUri = (String) configSection.get(URI_ID);
        if(repoUri == null)
            throw new NullPointerException("config.yml git handler should contain repository uri for map");
        cloneGitRepoToTemp(repoUri);
        switch(type){
            case ALL_MAPS_IN_REPO:
                break;

            case MAP_IN_REPO:

                break;
        }

        //move all maps from temp to mapsDir

    }

    @Override
    public void loadJar(Map<String, Object> configSection, File jarFile) {

    }

    private void cloneGitRepoToTemp(String uri) throws GitAPIException{
        String fileName = String.valueOf(uri.hashCode());
        File repoLocation = new File(repositoriesPath, fileName);
        try (Git result = Git.cloneRepository()
                .setURI(uri)
                .setDirectory(repoLocation)
                .call()) {
            Repository repo = result.getRepository();
            mapsRepoByDirName.put(fileName, repo);
        }
    }

}
