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

import groovy.transform.CompileStatic
import org.apache.hadoop.examples.render.engine.JpegRenderRunnable
import org.apache.hadoop.examples.render.tools.Utils
import org.apache.hadoop.examples.render.twill.Slf4JLogHandler
import org.apache.hadoop.examples.render.twill.args.Arguments
import org.apache.hadoop.examples.render.twill.args.RenderArgs
import org.apache.hadop.examples.render.TestKeys
import org.apache.hadop.examples.render.tools.UtilsForTests
import org.apache.twill.api.TwillController
import org.apache.twill.common.Services
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@CompileStatic
class TestTwillJpegRenderer extends BaseYarnGroovyTest {
  private static final Logger log = LoggerFactory.getLogger(
      TestTwillJpegRenderer.class);
  public static final String s = "Hello from Berlin BuzzWords!"

  @Test
  public void testTwillJpegRenderer()
  throws InterruptedException, ExecutionException, TimeoutException {


    File destFile = destImageFile
    destFile.delete()
    def src = new File(TestKeys.TEST_IMAGE_JPG)


    List<String> args = [
        Arguments.ARG_MESSAGE, s,
        Arguments.ARG_Y, "200",
        Arguments.ARG_IMAGE, Utils.fileToURIString(src),
        Arguments.ARG_DEST, Utils.fileToURIString(destFile),
    ]
    new RenderArgs(args)

    TwillController controller =
        twillRunner.prepare(new JpegRenderRunnable())
                   .withApplicationArguments(args)
                   .addLogHandler(new Slf4JLogHandler(log))
                   .start();

    Services.getCompletionFuture(controller).get(1, TimeUnit.MINUTES);
  }



}
