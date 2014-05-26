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
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.examples.render.twill.RenderTwillMain
import org.apache.hadoop.examples.render.twill.args.Arguments
import org.apache.hadoop.service.Service
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.slider.core.zk.BlockingZKWatcher
import org.apache.slider.core.zk.ZKIntegration
import org.junit.Before

/**
 * Base class for tests
 */
@Slf4j
//@CompileStatic
class RenderTestBase extends groovy.test.GroovyAssert {

  protected static String resourceManager;
  protected static String zookeeper;
  protected static String filesystemURL;


  def static void render(YarnConfiguration config, List<String> args) {
    assert zookeeper != null
    assert resourceManager != null
    assert filesystemURL != null
    List<String> standardArgs = [
        Arguments.ARG_ZK, zookeeper,
    ]
    standardArgs.addAll(args)
    // check
    
    standardArgs.eachWithIndex { String entry, int i ->
      assert entry!=null : "Null entry at $i"
    } 
   
    RenderTwillMain renderer = new RenderTwillMain(config);
    renderer.exec(standardArgs);
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
    log.info("Connected: {}", zki.toString());
    return zki;
  }


  @Before
  public void setup() {
    //give our thread a name
    Thread.currentThread().name = "JUnit"
  }


  protected static void run(Configuration conf, Service svc) {
    svc.init(conf)
    svc.start();
  }


}
