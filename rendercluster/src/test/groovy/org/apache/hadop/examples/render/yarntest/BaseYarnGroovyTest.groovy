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

package org.apache.hadop.examples.render.yarntest

import org.apache.hadoop.examples.render.yarntest.YarnTestUtils
import org.apache.hadop.examples.render.tools.UtilsForTests
import org.apache.twill.api.TwillRunner
import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.junit.rules.TestName

class BaseYarnGroovyTest extends groovy.test.GroovyAssert {

  @ClassRule
  public static TemporaryFolder tmpFolder = new TemporaryFolder();

  @BeforeClass
  public static void init() throws IOException {
    YarnTestUtils.initOnce(tmpFolder.newFolder());
  }
  
  @AfterClass
  public static void killTwill() {
    UtilsForTests.killJavaProcesses("TwillLauncher", 9)
  }
  
  public TwillRunner getTwillRunner() {
    TwillRunner runner = YarnTestUtils.twillRunner;
    assert runner != null;
    return runner;
  }


  @Rule
  public TestName name = new TestName();


  public File getDestImageFile() {
    new File(
        "target/images/${name.methodName}/${name.methodName}.jpg").absoluteFile
  }

}
