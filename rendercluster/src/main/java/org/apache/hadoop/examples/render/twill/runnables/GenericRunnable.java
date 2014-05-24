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

package org.apache.hadoop.examples.render.twill.runnables;

import com.google.common.base.Preconditions;
import org.apache.hadoop.examples.render.twill.RenderTask;
import org.apache.twill.api.AbstractTwillRunnable;
import org.apache.twill.api.TwillContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericRunnable extends AbstractTwillRunnable {
  public static Logger log = LoggerFactory.getLogger(GenericRunnable.class);

  private final RenderTask task;

  public GenericRunnable(RenderTask task) {
    Preconditions.checkNotNull(task, "null task argument");
    this.task = task;
  }

  
  
  @Override
  public void run() {
    try {
      TwillContext ctx = getContext();
      task.run(ctx);
    } catch (RuntimeException e) {
      log.error("Exception in task {}", e);
      throw e;
    } catch (Exception e) {
      log.error("Exception in task {}", e);
      throw new RuntimeException(e);
    }

  }

  @Override
  public void stop() {
    super.stop();
  }
}
