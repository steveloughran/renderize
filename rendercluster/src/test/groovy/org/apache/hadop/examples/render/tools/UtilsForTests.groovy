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

package org.apache.hadop.examples.render.tools

import groovy.util.logging.Slf4j
import org.apache.hadoop.conf.Configuration

@Slf4j

class UtilsForTests {
  public static String sysprop(String key) {
    def property = System.getProperty(key)
    if (!property) {
      throw new RuntimeException("Undefined property $key")
    }
    return property
  }

  protected static void describe(String s) {
    log.info("");
    log.info("===============================");
    log.info(s);
    log.info("===============================");
    log.info("");
  }

  public static void killJavaProcesses(String grepString, int signal) {

    GString bashCommand = "jps -l| grep ${grepString} | awk '{print \$1}' | xargs kill $signal"
    log.info("Bash command = $bashCommand")
    Process bash = ["bash", "-c", bashCommand].execute()
    bash.waitFor()

    log.info(bash.in.text)
    log.warn(bash.err.text)
  }

  /**
   * Get a time option in seconds if set, otherwise the default value (also in seconds).
   * This operation picks up the time value as a system property if set -that
   * value overrides anything in the test file
   * @param conf
   * @param key
   * @param defVal
   * @return
   */
  public static int getTimeOptionMillis(
      Configuration conf,
      String key,
      int defValMillis) {
    int val = conf.getInt(key, 0)
    val = Integer.getInteger(key, val)
    int time = 1000 * val
    if (time == 0) {
      time = defValMillis
    }
    return time;
  }
}
