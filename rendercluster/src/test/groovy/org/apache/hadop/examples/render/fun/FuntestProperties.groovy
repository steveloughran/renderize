/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadop.examples.render.fun

import groovy.transform.CompileStatic

/**
 * Properties unique to the functional tests
 */
@CompileStatic
public interface FuntestProperties  {

  /**
   * Maven Property of location of test conf dir: {@value}
   */
  String CONF_DIR_PROP = "test.conf.dir"


  String KEY_TEST_NUM_WORKERS = "test.cluster.size"
  int DEFAULT_NUM_WORKERS = 1

  String KEY_TEST_ZK_HOSTS = "test.zkhosts";
  String DEFAULT_ZK_HOSTS = "localhost:2181";

  String KEY_FUNTESTS_ENABLED = "test.funtest.enabled"

  String CLIENT_CONFIG_FILENAME = "config.xml"
  
  String KEY_TEST_CONF_XML = "test.conf.xml"
  
  String KEY_TEST_CONF_DIR = "test.conf.dir"
  String KEY_TEST_DEST_DIR = "test.dest.dir"
}
