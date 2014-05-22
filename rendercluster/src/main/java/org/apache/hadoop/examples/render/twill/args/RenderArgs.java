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
import org.apache.hadoop.examples.render.Utils;
import org.apache.hadoop.fs.Path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class RenderArgs implements Arguments {


  public static final String TEXT = "--text";
  private final String[] args;
  public final JCommander commander;

  public RenderArgs(String[] args) {
    this.args = args;
    commander = new JCommander(this);
  }

  public RenderArgs(Collection<String> argsList) {
    this.args = argsList.toArray(new String[argsList.size()]);
    commander = new JCommander(this);
  }

  @Parameter(names = "help", help = true)
  public boolean help;
  
  @Parameter(names = ARG_DEFINE, arity = 1, description = "Definitions")
  public final List<String> definitions = new ArrayList<>();
  
  @Parameter(names = {ARG_ZOOKEEPER, ARG_ZK})
  public String zookeeper;

  @Parameter(names = {TEXT},
      description = "message to echo")
  public String message;
  
  @Parameter(names = {"--image"},
      converter = PathArgumentConverter.class)
  public Path image;

  
  public void parseAndValidate() {
    commander.parse(args);

    checkSet(ARG_ZOOKEEPER, zookeeper);
    checkSet(ARG_ZOOKEEPER, zookeeper);
  }

  private void checkSet(String name, String val) {
    Preconditions.checkArgument(StringUtils.isNotEmpty(val),
        "Missing argument " + name);
  }


}
