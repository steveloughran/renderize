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

package org.apache.hadop.examples.render.yarntest

import groovy.util.logging.Slf4j
import org.apache.hadoop.examples.render.twill.Slf4JLogHandler
import org.apache.hadoop.examples.render.twill.args.Arguments
import org.apache.hadoop.examples.render.twill.args.RenderArgs
import org.apache.hadoop.examples.render.twill.runnables.StdoutRunnable
import org.apache.hadoop.examples.render.yarntest.YarnTestUtils
import org.apache.twill.api.TwillController
import org.apache.twill.api.TwillRunner
import org.apache.twill.api.logging.PrinterLogHandler
import org.apache.twill.common.Services

import org.junit.Test

import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Slf4j

class TestYarnLocal extends BaseYarnGroovyTest {


  @Test
  public void testYarnLocal()
  throws InterruptedException, ExecutionException, TimeoutException {
    TwillRunner runner = YarnTestUtils.twillRunner;

    def args = [Arguments.ARG_MESSAGE, "text message"]
    new RenderArgs(args)
    
    TwillController controller =
        runner.prepare(new StdoutRunnable())
          .withApplicationArguments(args)
//          .addLogHandler(new PrinterLogHandler(new PrintWriter(System.out)))
          .addLogHandler(new Slf4JLogHandler(log))
          .start();

    Services.getCompletionFuture(controller).get(1, TimeUnit.MINUTES);
  }

}
