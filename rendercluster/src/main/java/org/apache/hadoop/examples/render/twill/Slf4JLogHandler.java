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

package org.apache.hadoop.examples.render.twill;

import com.google.common.base.Splitter;
import org.apache.twill.api.logging.LogEntry;
import org.apache.twill.api.logging.LogHandler;
import org.slf4j.Logger;

import java.util.Date;

public class Slf4JLogHandler implements LogHandler {

  private final Logger log;


  public Slf4JLogHandler(Logger log) {
    this.log = log;
  }

  @Override
  public void onLog(LogEntry logEntry) {
    String message = formatLogEntry(logEntry);
    switch (logEntry.getLogLevel()) {
      case TRACE:
        log.trace(message);
        break;
      case DEBUG:
        log.debug(message);
        break;
      case INFO:
        log.info(message);
        break;
      case WARN:
        log.warn(message);
        break;
      case ERROR:
        log.error(message);
    }

  }

  /**
   * Format the log entry
   * @param logEntry entry
   * @return
   */
  public static String formatLogEntry(LogEntry logEntry) {
    String utc = timestampToUTC(logEntry.getTimestamp());
    return String.format(
        "[%4$s@%5$s] %3$5s %6$s:%7$s(%8$s:%9$d) - %10$s\n",
        
        utc,                                              // 1
        logEntry.getLogLevel().name(),
        getShortenLoggerName(logEntry.getLoggerName()),
        logEntry.getThreadName(),
        logEntry.getHost(),   // 5
        getSimpleClassName(logEntry.getSourceClassName()), 
        logEntry.getSourceMethodName(),
        logEntry.getFileName(),
        logEntry.getLineNumber(), //9
        logEntry.getMessage(),                             
        logEntry.getLoggerName(),
        logEntry.getSourceClassName()

    );
  }

  @SuppressWarnings("CallToDateToString")
  private static String timestampToUTC(long timestamp) {
    return new Date(timestamp).toString();
  }

  private static String getShortenLoggerName(String loggerName) {
    StringBuilder builder = new StringBuilder();
    String previous = null;
    for (String part : Splitter.on('.').split(loggerName)) {
      if (previous != null) {
        builder.append(previous.charAt(0)).append('.');
      }
      previous = part;
    }
    return builder.append(previous).toString();
  }

  private static String getSimpleClassName(String className) {
    return className.substring(className.lastIndexOf('.') + 1);
  }

}
