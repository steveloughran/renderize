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

package org.apache.hadoop.examples.render.twill.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class RenderArgs implements Arguments {


  
  private final Collection<String> arguments;
  public final JCommander commander;

  public RenderArgs(String[] args) {
    this(Arrays.asList(args));
  }

  public RenderArgs(Collection<String> arguments) {
    Preconditions.checkNotNull(arguments);
    arguments.stream().forEach(Preconditions::checkNotNull);
    this.arguments = arguments; 
    commander = new JCommander(this);
    parse();
  }

  public Collection<String> getArguments() {
    return arguments;
  }

  @Parameter(names = "help", help = true)
  public boolean help;
  
  @Parameter(names = ARG_DEFINE, arity = 1, description = "Definitions")
  public final List<String> definitions = new ArrayList<>();
  
  @Parameter(names = {ARG_ZOOKEEPER, ARG_ZK})
  public String zookeeper;
  
  @Parameter(names = ARG_DEST,description = "dest dir",
      converter = PathArgumentConverter.class)
  public Path dest;

  @Parameter(names = {ARG_MESSAGE},
      description = "message to echo")
  public String message = "";
  
  @Parameter(names = {ARG_IMAGE},
      converter = PathArgumentConverter.class)
  public Path image;

  @Parameter(names = ARG_WIDTH)
  public int width;

  @Parameter(names = ARG_HEIGHT)
  public int height;

  @Parameter(names = ARG_X)
  public int x = -1;

  @Parameter(names = ARG_Y)
  public int y = -1;
  
  
  public void parse() {
    
    String[] args = arguments.toArray(new String[arguments.size()]);
    
    commander.parse(args);

  }
  
  
  public void validateForMain() {

    checkSet(ARG_ZOOKEEPER, zookeeper);
    checkSet(ARG_RESOURCE_MANAGER, zookeeper);
    checkInRange(ARG_HEIGHT, height);
    checkInRange(ARG_WIDTH, width);

  }

  private void checkInRange(String name, int val) {
    Preconditions.checkArgument(val > 0,
        "Argument " + name + " out of range: " + val);
  }
  

  private void checkSet(String name, String val) {
    Preconditions.checkArgument(StringUtils.isNotEmpty(val),
        "Missing argument " + name);
  }


  public int getRenderX(int width) {
    if (x >= 0) {
      return x;
    } else {
      return width / 4;
    }
  }

  public int getRenderY(int height) {
    if (y >= 0) {
      return y;
    } else {
      return height / 2;
    }
  }
  
}
