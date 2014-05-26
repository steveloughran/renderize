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

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Load and render a JPEG
 */
public class JpegRenderRunnable extends AbstractTwillRunnable {
  private static final Logger log =
      LoggerFactory.getLogger(JpegRenderRunnable.class);
  private YarnConfiguration conf = new YarnConfiguration();

  public void run1() {
      String[] aa = getContext().getApplicationArguments();
      RenderArgs args = new RenderArgs(aa);
      Path dest = args.dest;
      String message = args.message;
      HadoopImageIO imageIO = new HadoopImageIO(conf);
      BufferedImage jpeg = imageIO.readJPEG(args.image);
      Renderer renderer = new Renderer(jpeg);
      int width = jpeg.getWidth();
      int height = jpeg.getHeight();
      int x = args.getRenderX(width);
      int y = args.getRenderY(height);
      renderer.render(x, y, message);
      imageIO.writeJPEG(renderer.image, dest);

  }


public void run() {
  String[] aa = getContext().getApplicationArguments();
  RenderArgs args = new RenderArgs(aa);
  HadoopImageIO imageIO = new HadoopImageIO(conf);
  BufferedImage jpeg = imageIO.readJPEG(args.image);
  Renderer renderer = new Renderer(jpeg);
  int width = jpeg.getWidth();
  int height = jpeg.getHeight();
  int x = args.getRenderX(width);
  int y = args.getRenderY(height);
  renderer.render(x, y, args.message);
  imageIO.writeJPEG(renderer.image, args.dest);
}
  
}
