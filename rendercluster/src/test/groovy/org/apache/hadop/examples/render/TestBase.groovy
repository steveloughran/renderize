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

package org.apache.hadop.examples.render

import groovy.util.logging.Slf4j
import org.apache.hadoop.examples.render.zk.BlockingZKWatcher
import org.apache.hadoop.examples.render.zk.ZKIntegration
import org.apache.hadoop.io.IOUtils
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass

@Slf4j
class TestBase extends Assert {

  public static final YarnConfiguration CONFIG = new YarnConfiguration();

  static {
    CONFIG.setInt(YarnConfiguration.RM_AM_MAX_ATTEMPTS, 100);
    CONFIG.setBoolean(YarnConfiguration.NM_PMEM_CHECK_ENABLED, false);
    CONFIG.setBoolean(YarnConfiguration.NM_VMEM_CHECK_ENABLED, false);
    CONFIG.setInt(YarnConfiguration.RM_SCHEDULER_MINIMUM_ALLOCATION_MB,
        1);
  }
  protected static MicroZKCluster microZKCluster;

  @BeforeClass
  public static void setupZookeeper() throws IOException, InterruptedException {
    if (microZKCluster == null) {
      microZKCluster = new MicroZKCluster(CONFIG);
      microZKCluster.createCluster();
    }
    createZKIntegrationInstance(microZKCluster.getZkBindingString(),
        "zookeeper", true, false, 5000);
  }

  @AfterClass
  public static void teardownZookeeper() {
    IOUtils.closeStream(microZKCluster);
    microZKCluster = null;
  }

  public static ZKIntegration createZKIntegrationInstance(String zkQuorum,
      String clusterName,
      boolean createClusterPath,
      boolean canBeReadOnly,
      int timeout) throws IOException, InterruptedException {

    BlockingZKWatcher watcher = new BlockingZKWatcher();
    ZKIntegration zki = ZKIntegration.newInstance(zkQuorum,
        "bigdataborat",
        clusterName,
        createClusterPath,
        canBeReadOnly, watcher);
    zki.init();
    //here the callback may or may not have occurred.
    //optionally wait for it
    if (timeout > 0) {
      watcher.waitForZKConnection(timeout);
    }
    //if we get here, the binding worked
    log.info("Connected: {}", zki);
    return zki;
  }
}
