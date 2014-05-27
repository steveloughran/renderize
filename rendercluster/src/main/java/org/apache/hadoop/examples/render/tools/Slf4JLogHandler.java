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

package org.apache.hadoop.examples.render.tools;

import com.google.common.base.Splitter;
import org.apache.twill.api.logging.LogEntry;
import org.apache.twill.api.logging.LogHandler;
import org.slf4j.Logger;

import java.util.Date;

/**
 * Log the remote log entry to the local Slf4J logger, using the
 * default or predfined {@link String#format(String, Object...)}format string.
 * This is slower than normal log expansion -but it is only invoked if the local
 * log level requests it. The String format operation allows formats to
 * reposition values, selectively exclude values and choose the format option
 * of each field. The index values are:
 * <pre>
 *  utc,                           // 1 -calculated timestamp string
 *  logEntry.getLogLevel().name(),
 *  getShortenLoggerName(logEntry.getLoggerName()),
 *  logEntry.getThreadName(),
 *  logEntry.getHost(),             // 5
 *  getSimpleClassName(logEntry.getSourceClassName()), 
 *  logEntry.getSourceMethodName(),
 *  logEntry.getFileName(),
 *  logEntry.getLineNumber(),        //8
 *  logEntry.getMessage(),                             
 *  logEntry.getLoggerName(),
 *  logEntry.getSourceClassName()
 * </pre>
 */
public class Slf4JLogHandler implements LogHandler {

  /**
   * The default log format: {@value}
   */
  public static final String DEFAULT_LOG_FORMAT =
      "[%4$s@%5$s] %3$5s %6$s:%7$s(%8$s:%9$d) - %10$s";

  /**
   * instance format string
   */
  private String format = DEFAULT_LOG_FORMAT;

  /**
   * Logger to log to
   */
  private final Logger log;


  /**
   * Create instance using the default log format
   * @param log Slf4J logger
   */
  public Slf4JLogHandler(Logger log) {
    this.log = log;
  }

  /**
   * Format log
   * @param log Slf4J logger
   * @param format Sprintf format string 
   */
  public Slf4JLogHandler(Logger log, String format) {
    this.format = format;
    this.log = log;
  }

  /**
   * Log an entry, mapping the remote log level to the local output, and only
   * logging if the local log level is enabled
   * @param logEntry log entry to log
   */
  @Override
  public void onLog(LogEntry logEntry) {
    switch (logEntry.getLogLevel()) {
      case TRACE:
        if (log.isTraceEnabled()) log.trace(formatLogEntry(logEntry));
        break;
      case DEBUG:
        if (log.isDebugEnabled()) log.debug(formatLogEntry(logEntry));
        break;
      case INFO:
        if (log.isInfoEnabled()) log.info(formatLogEntry(logEntry));
        break;
      case WARN:
        if (log.isWarnEnabled()) log.warn(formatLogEntry(logEntry));
        break;
      case ERROR:
        if (log.isErrorEnabled()) log.error(formatLogEntry(logEntry));
    }

  }

  /**
   * Format the log entry
   * @param logEntry entry
   * @return
   */
  public String formatLogEntry(LogEntry logEntry) {
    String utc = timestampToUTC(logEntry.getTimestamp());
    return String.format(format,
        utc,                                              // 1
        logEntry.getLogLevel().name(),
        getShortenLoggerName(logEntry.getLoggerName()),
        logEntry.getThreadName(),
        logEntry.getHost(),   // 5
        getSimpleClassName(logEntry.getSourceClassName()), 
        logEntry.getSourceMethodName(),
        logEntry.getFileName(),
        logEntry.getLineNumber(), //8
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
