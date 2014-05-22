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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

/**
 * Support for reading/writing images from HDFS.
 * 
 * For an API that's existed since 2001, the javax.imageio package is
 * woefully short of examples.
 */
public class HFDSImageIO {

  public static final String JPEG = "jpeg";
  final Configuration conf;
  final FileSystem fs;

  public HFDSImageIO(Configuration conf,
      FileSystem fs) {
    this.conf = Preconditions.checkNotNull(conf);
    this.fs = Preconditions.checkNotNull(fs);
  }

  public ImageInputStream openForReading(Path path) throws IOException {
    FSDataInputStream fsDataInputStream = fs.open(path);
    return new MemoryCacheImageInputStream(fsDataInputStream);
  }

  public ImageOutputStream openForWriting(Path path, boolean overwrite) throws
      IOException {

    FSDataOutputStream fsDataOutputStream = fs.create(path, overwrite);
    return new MemoryCacheImageOutputStream(fsDataOutputStream);
  }

  public void writeJPEG(BufferedImage image, Path path, boolean overwrite,
      float compression) throws IOException {
    ImageOutputStream out = openForWriting(path, overwrite);
    writeJPEG(image, out, compression);
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
    IIOMetadata imageMetaData = writer.getDefaultImageMetadata(
        new ImageTypeSpecifier(image), null);
    ImageWriteParam params = writer.getDefaultWriteParam();
    params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    params.setCompressionQuality(compression);
    writer.setOutput(out);
    writer.write(imageMetaData,
        new IIOImage(image, null, null),
        null);
  }

  public BufferedImage readJPEG(Path path) throws IOException {
    ImageInputStream in = openForReading(path);
    return readJPEG(in);
  }

  public BufferedImage readJPEG(ImageInputStream in) throws IOException {
    Iterator<ImageReader> readerIterator =
        ImageIO.getImageReadersBySuffix(JPEG);
    Preconditions.checkState(readerIterator.hasNext(),
        "JVM lacks JPEG reader");
    ImageReader reader = readerIterator.next();
    reader.setInput(in);
    BufferedImage image = reader.read(0, null);
    return image;
  }
  
  
}
