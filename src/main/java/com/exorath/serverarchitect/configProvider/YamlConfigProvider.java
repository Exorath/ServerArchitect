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

package com.exorath.serverarchitect.configProvider;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by toonsev on 11/23/2016.
 */
public class YamlConfigProvider implements ConfigProvider {
    private String fileName;
    public YamlConfigProvider(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, Object> getConfig() {
        try {
            return (Map) new Yaml().load(new FileInputStream(new File(fileName)));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Can't build server without " + fileName + ".");
        }
    }
}
