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
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.twill.api.AbstractTwillRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenderTwillRunnable extends AbstractTwillRunnable {
  private static final Logger log =
      LoggerFactory.getLogger(RenderTwillRunnable.class);
  private YarnConfiguration conf = new YarnConfiguration();

  public RenderTwillRunnable() {


  }


  public void run() {
    String[] aa = getContext().getApplicationArguments();
    RenderArgs args = new RenderArgs(aa);
    Path dest = args.dest;
    int w = args.width;
    int h = args.height;
    Renderer r = new Renderer(w, h);
    r.render(w / 4, h / 2, args.message);
    HadoopImageIO imageIO = new HadoopImageIO(conf);
    imageIO.writeJPEG(r.image, dest);
  }

  protected void out(String msg) {
    log.info(msg);
  }
}
