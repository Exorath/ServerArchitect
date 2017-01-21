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

import org.junit.Test;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;

/**
 *
 * Created by toonsev on 1/19/2017.
 */
public class GitHubTesting {
    @Test
    public void testing(){
        GitHub gitHub = null;
        try {
            gitHub = GitHub.connectUsingOAuth(System.getenv("ghoath"));

            for (GHRepository ghRepository : gitHub.searchRepositories().user("Exorath").list()) {
                System.out.println(ghRepository.getName() + ":");
                for (GHRelease ghRelease : ghRepository.listReleases()) {
                    ghRelease.getAssets().forEach(ghAsset -> System.out.println("- " + ghAsset.getName()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
