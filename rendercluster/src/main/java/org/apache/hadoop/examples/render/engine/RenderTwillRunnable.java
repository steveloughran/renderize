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

package org.apache.hadoop.examples.render.engine;

import org.apache.hadoop.examples.render.twill.args.RenderArgs;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.twill.api.AbstractTwillRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RenderTwillRunnable extends AbstractTwillRunnable {
  private static final Logger log =
      LoggerFactory.getLogger(RenderTwillRunnable.class);


  @Override
  public void run() {
    try {
      String[] arguments = getContext().getApplicationArguments();
      RenderArgs params = new RenderArgs(arguments);
      params.parse();
      Path image = params.image;
      Path dest = params.dest;
      
      int x = params.width;
      int y = params.height;
      String message = params.message;
      out("================================");
      log.info("Rendering {} into {} at ({},{}): {}", image, dest, x, y, message);
      out("================================");
      Renderer renderer = new Renderer(x, y);
      int x0 = x/4;
      int y0 = y/2;
          
          
      renderer.render(x0, y0, message);
      YarnConfiguration conf = new YarnConfiguration();
      FileSystem fs = FileSystem.get(conf);
      fs.delete(dest, false);
      HadoopImageIO imageIO = new HadoopImageIO(conf, fs);

      
      imageIO.writeJPEG(renderer.image, dest, true, 0.8f);
    } catch (IOException e) {
      log.error("Failed to render: {}", e, e);
      throw new RuntimeException(e);
    }
  }

  protected void out(String msg) {
    log.info(msg);
  }
}
