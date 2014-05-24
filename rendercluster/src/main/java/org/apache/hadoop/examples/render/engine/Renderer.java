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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Renders a bitmap
 */
public class Renderer {
  
  public Font font;

  BufferedImage image;
  int width;
  int height;

  public Renderer(int width, int height) {
    this.width = width;
    this.height = height;
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    chooseFont(Font.SANS_SERIF, 0, 48);
    clear();
  }
  
  public void chooseFont(String fontname, int attrs, int size) {
    font = new Font(fontname, attrs, size);
  }

  public void clear() {
    Graphics2D gc = image.createGraphics();
    gc.setColor(Color.black);
    gc.drawRect(0, 0, width, height);
    gc.dispose();
  }

  public void render(int x, int y, String text) {

    Graphics2D gc = image.createGraphics();
    gc.setColor(Color.white);
    gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    gc.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    gc.setFont(font);
    gc.drawString(text, x, y);
    gc.dispose();

  }

  public BufferedImage getImage() {
    return image;
  }
}
