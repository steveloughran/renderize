/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.hadop.examples.render

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.examples.render.zk.MiniZooKeeperCluster
import org.apache.hadoop.yarn.conf.YarnConfiguration

@Slf4j
@CompileStatic
class MicroZKCluster implements Closeable {

  public static final String HOSTS = "127.0.0.1"
  MiniZooKeeperCluster zkCluster
  File baseDir
  String zkBindingString
  Configuration conf
  int port

  MicroZKCluster() {
    this(new YarnConfiguration())
  }

  MicroZKCluster(Configuration conf) {
    this.conf = conf
  }

  void createCluster() {
    zkCluster = new MiniZooKeeperCluster(conf)
    baseDir = File.createTempFile("zookeeper", ".dir")
    baseDir.delete()
    baseDir.mkdirs()
    port = zkCluster.startup(baseDir)
    zkBindingString = HOSTS + ":" + port
    log.info("Created $this")
  }

  @Override
  void close() throws IOException {
    zkCluster?.shutdown();
    baseDir?.deleteDir()
  }

  @Override
  String toString() {
    return "Micro ZK cluster as $zkBindingString data=$baseDir"
  }
}
