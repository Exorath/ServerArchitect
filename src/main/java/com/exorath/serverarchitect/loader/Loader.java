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

import java.io.File;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by toonsev on 11/23/2016.
 */
public interface Loader {
    default void loadPlugins(Map<String, Object> configSection, File pluginsDir){};

    default void loadMaps(Map<String, Object> configSection, File mapsDir){};

    default InputStream loadJar(Map<String, Object> configSection){return null;};
}
