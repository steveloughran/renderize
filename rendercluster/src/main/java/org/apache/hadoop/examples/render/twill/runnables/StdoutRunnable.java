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

import org.apache.hadoop.examples.render.twill.args.RenderArgs;
import org.apache.twill.api.AbstractTwillRunnable;

public class StdoutRunnable extends AbstractTwillRunnable{
  public static final String MESSAGE = "message";

  @Override
  public void run() {
    String[] arguments = getContext().getApplicationArguments();
    RenderArgs parms = new RenderArgs(arguments);
    parms.parse();
    String msg = parms.message;
    out("================================");
    out(msg);
    out("================================");
  }

  protected void out(String msg) {
    System.out.println(msg);
  }
}
