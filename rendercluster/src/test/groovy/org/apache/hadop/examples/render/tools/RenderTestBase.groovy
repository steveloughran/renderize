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

import groovy.util.logging.Slf4j
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.hadoop.examples.render.twill.RenderTwillMain
import org.apache.hadoop.examples.render.twill.args.Arguments
import org.apache.hadoop.io.IOUtils
import org.apache.hadoop.service.Service
import org.apache.hadoop.service.ServiceOperations
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadoop.yarn.server.MiniYARNCluster
import org.apache.hadoop.yarn.server.resourcemanager.scheduler.ResourceScheduler
import org.apache.hadoop.yarn.server.resourcemanager.scheduler.fifo.FifoScheduler
import org.apache.slider.core.zk.BlockingZKWatcher
import org.apache.slider.core.zk.MiniZooKeeperCluster
import org.apache.slider.core.zk.ZKIntegration
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.rules.Timeout

@Slf4j
class RenderTestBase extends groovy.test.GroovyAssert {

  public static final YarnConfiguration CONFIG = new YarnConfiguration();
  static final int DEFAULT_TEST_TIMEOUT_SECONDS = 10 * 60
  private static String rmAddress;

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
  public static void setupYarn() {
    miniCluster = new MiniYARNCluster(
        "yarn",
        4,
        1,
        1)
    run(miniCluster)    
    rmAddress = miniCluster.config.get(YarnConfiguration.RM_ADDRESS)
    log.info("Resource manager at $rmAddress")
  }

  @AfterClass 
  public static void teardownYarn() {
    Log commonslog = LogFactory.getLog(RenderTestBase)
    ServiceOperations.stopQuietly(commonslog, miniCluster)
  }

  @Before
  public void setup() {
    //give our thread a name
    Thread.currentThread().name = "JUnit"
  }

  
  public static void run(Service svc) {
    svc.init(CONFIG)
    svc.start();
  }
  public static void describe(String s) {
    log.info("");
    log.info("===============================");
    log.info(s);
    log.info("===============================");
    log.info("");
  }
  
  public static String getRMAddress() {
    assert rmAddress != null
    return rmAddress
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

  public static void exec(List<String> args) {
    def standardArgs = [
        Arguments.ARG_ZK, zookeeperQuorum,
    ]
    standardArgs.addAll(args)
    
    RenderTwillMain renderer = new RenderTwillMain(CONFIG);
    renderer.exec(standardArgs);

  }

}
