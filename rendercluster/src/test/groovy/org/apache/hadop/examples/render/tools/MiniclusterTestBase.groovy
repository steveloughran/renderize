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

package org.apache.hadop.examples.render.tools

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.hadoop.io.IOUtils
import org.apache.hadoop.service.Service
import org.apache.hadoop.service.ServiceOperations
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadoop.yarn.server.MiniYARNCluster
import org.apache.hadoop.yarn.server.resourcemanager.scheduler.ResourceScheduler
import org.apache.hadoop.yarn.server.resourcemanager.scheduler.fifo.FifoScheduler
import org.apache.slider.core.zk.MiniZooKeeperCluster
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.rules.Timeout
import org.apache.hadoop.fs.FileSystem as HadoopFS

/**
 * Base class for tests that use the miniclusters
 */
@Slf4j
//@CompileStatic

abstract class MiniclusterTestBase extends RenderTestBase {

  public static final YarnConfiguration CONFIG = new YarnConfiguration();
  static final int DEFAULT_TEST_TIMEOUT_SECONDS = 10 * 60

  static {
    CONFIG.setInt(YarnConfiguration.RM_AM_MAX_ATTEMPTS, 100);
    CONFIG.setBoolean(YarnConfiguration.NM_PMEM_CHECK_ENABLED, false);
    CONFIG.setBoolean(YarnConfiguration.NM_VMEM_CHECK_ENABLED, false);
    CONFIG.setInt(YarnConfiguration.RM_SCHEDULER_MINIMUM_ALLOCATION_MB,
        1);
    CONFIG.setClass(YarnConfiguration.RM_SCHEDULER,
        FifoScheduler.class, ResourceScheduler.class);
  }
  protected static MiniZooKeeperCluster microZKCluster;
  protected static MiniYARNCluster miniCluster


  @Rule
  public Timeout testTimeout = new Timeout(
          DEFAULT_TEST_TIMEOUT_SECONDS * 1000)
  

  @BeforeClass
  public static void setupZookeeper() throws IOException, InterruptedException {
    if (microZKCluster == null) {
      microZKCluster = new MiniZooKeeperCluster(1)
      run(microZKCluster)
    }
    zookeeper = zookeeperQuorum;
    createZKIntegrationInstance(zookeeperQuorum,
        "zookeeper", true, false, 5000);
  }

  @AfterClass
  public static void teardownZookeeper() {
    IOUtils.closeStream(microZKCluster);
    microZKCluster = null;
  }

  public static String getZookeeperQuorum() {
    return microZKCluster.zkQuorum;
  }
  
  @BeforeClass
  public static void setupFileSystem() {
    filesystemURL = HadoopFS.getDefaultUri(
        CONFIG).toString()
  }
  
  @BeforeClass 
  public static void setupYarn() {
    miniCluster = new MiniYARNCluster(
        "yarn",
        1,
        1,
        1)
    run(miniCluster)    
    String rmAddress = miniCluster.config.get(YarnConfiguration.RM_ADDRESS)
    log.info("Resource manager at $rmAddress")
    resourceManager = rmAddress;
  }

  @AfterClass 
  public static void teardownYarn() {
    Log commonslog = LogFactory.getLog(MiniclusterTestBase)
    ServiceOperations.stopQuietly(commonslog, miniCluster)
  }


  public static void run(Service svc) {
    run(CONFIG, svc)
  }
  
  public static void render(List<String> args) {
    render(CONFIG, args)
  }


}
