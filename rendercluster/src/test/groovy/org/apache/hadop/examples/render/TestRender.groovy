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

package org.apache.hadop.examples.render

import org.apache.hadoop.examples.render.twill.RenderTwillMain
import org.apache.hadoop.examples.render.twill.args.Arguments
import org.apache.hadop.examples.render.tools.RenderTestBase
import org.junit.Test

class TestRender extends RenderTestBase implements Arguments {

  @Test
  public void testNoRMBinding() throws Throwable {
    shouldFail(IllegalStateException) {
      RenderTwillMain renderer = new RenderTwillMain();
      renderer.exec(ARG_ZK, microZKCluster.zkQuorum);
    }
  }

  @Test
  public void testExec() throws Throwable {
    exec([])
    
  }
  
}
