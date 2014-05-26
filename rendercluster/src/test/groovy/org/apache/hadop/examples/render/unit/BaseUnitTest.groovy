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
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.junit.Rule
import org.junit.rules.TestName
import org.apache.hadoop.fs.FileSystem as HadoopFS


import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class BaseUnitTest extends groovy.test.GroovyAssert {
  @Rule
  public TestName name = new TestName()

  public File getDestImageFile() {
    new File(
        "target/images/${name.methodName}/${name.methodName}.jpg").absoluteFile
  }

  static def void assertDestValid(File dest) {
    assert dest.file;
    assert dest.length() > 0
  }

  static void assertImagesMatch(BufferedImage image, Renderer renderer) {
    assert image.width == renderer.width
    assert image.height == renderer.height
  }

  def File createDestFile() {
    File dest = destImageFile
    dest.delete()
    dest.parentFile.mkdirs();
    dest
  }

  static def HadoopImageIO createImageIO() {
    YarnConfiguration conf = new YarnConfiguration();

    HadoopImageIO imageIO = new HadoopImageIO(conf);
    imageIO
  }

  def BufferedImage readFile(HadoopImageIO imageIO, File file) {
    FileInputStream fis = new FileInputStream(file)

    try {
      def image = imageIO.readJPEG(ImageIO.createImageInputStream(fis))
      return image;
    } finally {
      fis.close()
    }
  }
}
