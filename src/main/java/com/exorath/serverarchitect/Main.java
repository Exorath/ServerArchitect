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

import com.exorath.serverarchitect.configProvider.YamlConfigProvider;
import com.exorath.serverarchitect.handler.GitHubHandler;
import com.exorath.serverarchitect.handler.MapServiceHandler;
import com.exorath.serverarchitect.handler.S3Handler;

/**
 * Created by toonsev on 11/23/2016.
 */
public class Main {
    public static void main(String[] args) {
        String configLoc = System.getenv("SA_CONFIG_FILE");
        if(configLoc == null){
            System.out.println("No SA_CONFIG_FILE environment field, defaulting to architect.yml.");
            configLoc = "architect.yml";
        }
        ServerArchitect architect = new ServerArchitect(new YamlConfigProvider(configLoc));
        architect.withHandler("amazons3", new S3Handler());
        architect.withHandler("github", new GitHubHandler());
        architect.withHandler("mapservice", new MapServiceHandler());
        try {
            architect.start();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);//Exit unsuccessfully, that way the caller knows the build failed.
        }
    }
}
