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

import com.google.common.base.Preconditions;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

/**
 * Support for reading/writing images from HDFS.
 * 
 * For an API that's existed since 2001, the javax.imageio package is
 * woefully short of examples.
 */
public class HadoopImageIO {
  private static final Logger log =
      LoggerFactory.getLogger(HadoopImageIO.class);

  public static final String JPEG = "jpeg";
  final Configuration conf;
  final FileSystem fs;

  public HadoopImageIO(Configuration conf,
      FileSystem fs) {
    this.conf = Preconditions.checkNotNull(conf);
    this.fs = Preconditions.checkNotNull(fs);
  }

  /**
   * Create an instance using the default FS
   * @param conf
   * @throws IOException
   */
  public HadoopImageIO(Configuration conf) {
    this.conf = Preconditions.checkNotNull(conf);
    try {
      fs = FileSystem.get(conf);
    } catch (IOException e) {
      log.error("Failed to create FS: " +e, e);
      throw new RuntimeException(e);
    }
  }

  public ImageInputStream openForReading(Path path) throws IOException {
    FSDataInputStream fsDataInputStream = fs.open(path);
    return ImageIO.createImageInputStream(fsDataInputStream);
  }

  public ImageOutputStream openForWriting(Path path, boolean overwrite) throws
      IOException {

    FSDataOutputStream fsDataOutputStream = fs.create(path, overwrite);
    return ImageIO.createImageOutputStream(fsDataOutputStream);
  }

  public void writeJPEG(BufferedImage image, Path path)  {
    try {
      writeJPEG(image, path, true, 0.8f);
    } catch (IOException e) {
      log.error("Failed to write {}: {}", path.toString(), e, e);
      throw new RuntimeException(e);

    }

  }

    public void writeJPEG(BufferedImage image, Path path, boolean overwrite,
      float compression) throws IOException {

    try(FSDataOutputStream fsDataOutputStream = fs.create(path, overwrite)) {
      ImageOutputStream out = ImageIO.createImageOutputStream(fsDataOutputStream);
      writeJPEG(image, out, compression);
      fsDataOutputStream.flush();
      fsDataOutputStream.close();
    }
  }

  public void writeJPEG(BufferedImage image,
      ImageOutputStream out,
      float compression) throws IOException {
    Preconditions.checkArgument(compression >= 0 && compression < 1.0f);
    Iterator<ImageWriter> writerIterator =
        ImageIO.getImageWritersBySuffix(JPEG);
    Preconditions.checkState(writerIterator.hasNext(),
        "JVM lacks JPEG writer");
    ImageWriter writer = writerIterator.next();
    IIOMetadata imageMetaData = null;
    imageMetaData = writer.getDefaultImageMetadata(
        new ImageTypeSpecifier(image), null);
    ImageWriteParam params = writer.getDefaultWriteParam();
    params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    params.setCompressionQuality(compression);
    
    writer.setOutput(out);
    writer.write(imageMetaData,
        new IIOImage(image, null, null),
        null);
    out.flush();
    out.close();
  }

  public BufferedImage readJPEG(Path path) {
    try {
      ImageInputStream in = openForReading(path);
      return readJPEG(in);
    } catch (IOException e) {
      log.error("Failed to write {}: {}",path.toString(), e, e);
      throw new RuntimeException(e);
    }
  }

  public BufferedImage readJPEG(ImageInputStream in) throws IOException {
    try {
      Iterator<ImageReader> readerIterator =
          ImageIO.getImageReadersBySuffix(JPEG);
      Preconditions.checkState(readerIterator.hasNext(),
          "JVM lacks JPEG reader");
      ImageReader reader = readerIterator.next();
      reader.setInput(in);
      BufferedImage image = reader.read(0, null);
      return image;
    } finally {
      in.close();
    }
  }
  
  
}
