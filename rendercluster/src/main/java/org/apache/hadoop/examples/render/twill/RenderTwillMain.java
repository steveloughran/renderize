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

package org.apache.hadoop.examples.render.twill;

import com.google.common.base.Preconditions;
import org.apache.hadoop.examples.render.Utils;
import org.apache.hadoop.examples.render.twill.args.RenderArgs;
import org.apache.hadoop.util.ExitUtil;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.twill.api.TwillController;
import org.apache.twill.api.TwillRunnerService;
import org.apache.twill.common.Services;
import org.apache.twill.yarn.YarnTwillRunnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class RenderTwillMain {
  public static Logger log = LoggerFactory.getLogger(RenderTwillMain.class);
  private static volatile TwillController controller;


  
  public void exec(String...args) {

    List<String> argsList = Arrays.asList(args);
    String command = Utils.join(argsList, ", ", false);
    try {

      log.info("Render Twill Main args = {}", command);
      RenderArgs params = new RenderArgs(args);
      params.parseAndValidate();


      String zkStr = params.zookeeper;

      YarnConfiguration conf = new YarnConfiguration();
      String rmAddr = conf.get(YarnConfiguration.RM_ADDRESS);
      Preconditions.checkState(!rmAddr.startsWith("0.0.0.0"),
          "Resource manager not defined " + rmAddr);
        
      final TwillRunnerService twillRunner =
          new YarnTwillRunnerService(
              conf, zkStr);
      twillRunner.startAndWait();

      RenderRunnable runnable = new RenderRunnable(() -> log.info(params.message));

      controller = null;

      Runtime.getRuntime().addShutdownHook(new Thread(() ->
      {
        if (controller != null) {
          controller.stopAndWait();
        }
        twillRunner.stopAndWait();
      }
      ));

      controller = twillRunner.prepare(runnable)
                 .addLogHandler(new Slf4JLogHandler(log))
                 .start();



      Services.getCompletionFuture(controller).get();
    } catch (Throwable e) {
      log.error("command {}\n" +
                "failed: {}", command, e, e);
      ExitUtil.terminate(-1, e);
    }

  }
  
  public static void main(String[] args) {
    List<String> argsList = Arrays.asList(args);
    String command = Utils.join(argsList, ", ", false);
    try {
      new RenderTwillMain().exec(args);
    } catch (Throwable e) {
      log.error("command {}\n" +
                "failed: {}", command, e, e);
      ExitUtil.terminate(-1, e);
    }
  }
}
