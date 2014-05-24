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
import org.apache.hadoop.examples.render.twill.RenderRunnable
import org.apache.hadop.examples.render.tools.GroovyRenderRunnable
import org.apache.twill.api.TwillController
import org.apache.twill.api.TwillRunner
import org.apache.twill.api.logging.PrinterLogHandler
import org.apache.twill.common.Services
import org.apache.twill.yarn.BaseYarnTest
import org.apache.twill.yarn.YarnTestUtils
import org.junit.Test

import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Slf4j

class TestYarnLocal extends BaseYarnTest {


  @Test
  public void testInitFail()
  throws InterruptedException, ExecutionException, TimeoutException {
    TwillRunner runner = YarnTestUtils.getTwillRunner();
    def instance = GroovyRenderRunnable.instance { System.out.println("hello") }
    TwillController controller = runner.prepare(
        instance
    )
      .addLogHandler(
        new PrinterLogHandler(new PrintWriter(System.out)))
      .start();

    Services.getCompletionFuture(controller).get(2, TimeUnit.MINUTES);
  }

}
