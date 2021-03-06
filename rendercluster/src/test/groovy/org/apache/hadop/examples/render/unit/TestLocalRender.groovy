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

package org.apache.hadop.examples.render.unit

import org.apache.hadoop.examples.render.engine.HadoopImageIO
import org.apache.hadoop.examples.render.engine.Renderer
import org.apache.hadoop.examples.render.tools.Utils
import org.apache.hadoop.fs.FileSystem as HadoopFS
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class TestLocalRender extends BaseUnitTest {
  private static final Logger log = LoggerFactory.getLogger(
      TestLocalRender.class);


  public static final int WIDTH = 512;
  public static final int HEIGHT = 256;

  @Test
  public void testRenderDirect() throws Throwable {
    File dest = createDestFile()

    Renderer renderer = renderTestImage()
    HadoopImageIO imageIO = createImageIO()

    def outputStream = new FileOutputStream(dest);
    def imageOutputStream = ImageIO.createImageOutputStream(outputStream)
    try {
      imageIO.writeJPEG(renderer.image, imageOutputStream, 0.8f)
    } finally {
      outputStream.close()
    }

/*
    imageIO.writeJPEG(renderer.image,
        Utils.fileToPath(dest), true, 0.8f);*/
    assertDestValid(dest)
    def image = readFile(imageIO, dest)
    assertImagesMatch(image, renderer)
  }


  @Test
  public void testRenderViaHadoopFilesystem() throws Throwable {
    File dest = createDestFile()

    Renderer renderer = renderTestImage()
    HadoopImageIO imageIO = createImageIO()

    imageIO.writeJPEG(renderer.image,
        Utils.fileToPath(dest), true, 0.8f);
    assertDestValid(dest)
    def image = readFile(imageIO, dest)
    assertImagesMatch(image, renderer)
    image.raster
  }


  def Renderer renderTestImage() {
    Renderer renderer = new Renderer(WIDTH, HEIGHT);
    renderer.render(30, 100, name.methodName);
    renderer
  }


}
