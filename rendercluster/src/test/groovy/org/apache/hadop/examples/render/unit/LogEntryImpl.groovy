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

import org.apache.twill.api.logging.LogEntry
import org.apache.twill.api.logging.LogThrowable

class LogEntryImpl implements LogEntry {
  
  String loggerName, host, sourceClassName, sourceMethodName, fileName
  String threadName, message;
  long timestamp;
  int lineNumber;
  LogEntry.Level logLevel;
  LogThrowable throwable
  StackTraceElement[] stackTraces;

  String getLoggerName() {
    return loggerName
  }

  void setLoggerName(String loggerName) {
    this.loggerName = loggerName
  }

  String getHost() {
    return host
  }

  void setHost(String host) {
    this.host = host
  }

  String getSourceClassName() {
    return sourceClassName
  }

  void setSourceClassName(String sourceClassName) {
    this.sourceClassName = sourceClassName
  }

  String getSourceMethodName() {
    return sourceMethodName
  }

  void setSourceMethodName(String sourceMethodName) {
    this.sourceMethodName = sourceMethodName
  }

  String getFileName() {
    return fileName
  }

  void setFileName(String filename) {
    this.fileName = filename
  }

  String getThreadName() {
    return threadName
  }

  void setThreadName(String threadName) {
    this.threadName = threadName
  }

  String getMessage() {
    return message
  }

  void setMessage(String message) {
    this.message = message
  }

  long getTimestamp() {
    return timestamp
  }

  void setTimestamp(long timestamp) {
    this.timestamp = timestamp
  }

  int getLineNumber() {
    return lineNumber
  }

  void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber
  }

  LogEntry.Level getLogLevel() {
    return logLevel
  }

  void setLogLevel(LogEntry.Level level) {
    this.logLevel = level
  }

  LogThrowable getThrowable() {
    return throwable
  }

  void setThrowable(LogThrowable throwable) {
    this.throwable = throwable
  }

  StackTraceElement[] getStackTraces() {
    return stackTraces
  }

  void setStackTraces(StackTraceElement[] stackTraces) {
    this.stackTraces = stackTraces
  }
}
