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

import groovy.util.logging.Slf4j
import org.apache.hadoop.examples.render.tools.Slf4JLogHandler
import org.apache.twill.api.logging.LogEntry
import org.junit.Test

@Slf4j
class TestFormatter {
  @Test
  public void testLogEntry() throws Throwable {
    LogEntryImpl entry = entry()
    Slf4JLogHandler handler = new Slf4JLogHandler(log)
    def s = handler.formatLogEntry(entry)
    log.info s;
    assert s.contains("threadname2")
  }

  def LogEntryImpl entry() {
    LogEntryImpl entry = new LogEntryImpl(
        loggerName: "logger",
        host: "localhost",
        sourceClassName: "org.apache.hadop.examples.render.unit.TestFormatter",
        sourceMethodName: "testLogEntry",
        fileName: "n/a",
        threadName: "threadname2",
        message: "oops",
        lineNumber: 54,
        logLevel: LogEntry.Level.ERROR,
    )
    entry.timestamp = System.currentTimeMillis();
    entry
  }

  @Test
  public void testhandler() throws Throwable {
    Slf4JLogHandler handler = new Slf4JLogHandler(log)
    LogEntryImpl entry = entry()
    handler.onLog(entry)

  }
 
  
  
}
