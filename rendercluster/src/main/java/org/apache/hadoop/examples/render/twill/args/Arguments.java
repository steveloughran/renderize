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

/**
 * Here are all the arguments that may be parsed by the client or server
 * command lines. 
 */
public interface Arguments {

  String ARG_DEBUG = "--debug";
  String ARG_DEST = "--dest";
  String ARG_DEFINE = "-D";
  String ARG_EXITCODE = "--exitcode";
  String ARG_FILESYSTEM = "--fs";
  String ARG_HELP = "--help";

  String ARG_IMAGE = "--image";
  String ARG_MANAGER = "--manager";
  String ARG_RESOURCE_MANAGER = "--rm";
  String ARG_MESSAGE = "--text";
  String ARG_VERBOSE = "--verbose";
  String ARG_WAIT = "--wait";
  String ARG_ZOOKEEPER = "--zookeeper";
  String ARG_ZK = "--zk";

  String ARG_WIDTH = "--width";
  String ARG_HEIGHT = "--height";

  String ARG_X = "--x";
  String ARG_Y = "--y";

}
