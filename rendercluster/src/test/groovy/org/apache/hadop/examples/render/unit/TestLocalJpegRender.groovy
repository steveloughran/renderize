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
import org.apache.hadop.examples.render.TestKeys
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.imageio.ImageIO

class TestLocalJpegRender extends BaseUnitTest {
  private static final Logger log = LoggerFactory.getLogger(
      TestLocalJpegRender.class);

  @Test
  public void testLocalJpegRenderDirect() throws Throwable {
    File dest = createDestFile()

    HadoopImageIO imageIO = createImageIO()
    Renderer renderer = renderTestImage(imageIO)

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
  public void testLocalJpegRenderViaHadoopFilesystem() throws Throwable {
    File dest = createDestFile()

    HadoopImageIO imageIO = createImageIO()
    Renderer renderer = renderTestImage(imageIO)

    imageIO.writeJPEG(renderer.image,
        Utils.fileToPath(dest), true, 0.8f);
    assertDestValid(dest)
    def image = readFile(imageIO, dest)
    assertImagesMatch(image, renderer)
    image.raster
  }


  def Renderer renderTestImage(HadoopImageIO imageIO) {
    def src = new File(TestKeys.TEST_IMAGE_JPG)
    assert src.exists();
    def jpeg = imageIO.readJPEG(
        Utils.fileToPath(src))
    
    Renderer renderer = new Renderer(jpeg);
    renderer.render(30, 100, name.methodName);
    renderer
  }


}
