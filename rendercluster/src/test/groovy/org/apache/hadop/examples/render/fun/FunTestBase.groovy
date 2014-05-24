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

package org.apache.hadop.examples.render.fun

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadop.examples.render.tools.RenderTestBase
import org.apache.slider.common.tools.SliderUtils
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.rules.Timeout
import org.apache.hadoop.fs.FileSystem as HadoopFS

import static org.apache.slider.common.SliderXMLConfKeysForTesting.DEFAULT_TEST_TIMEOUT_SECONDS
import static org.apache.slider.common.SliderXMLConfKeysForTesting.DEFAULT_YARN_RAM_REQUEST
import static org.apache.slider.common.SliderXMLConfKeysForTesting.KEY_TEST_TIMEOUT
import static org.apache.slider.common.SliderXMLConfKeysForTesting.KEY_TEST_YARN_RAM_REQUEST

/**
 * Functional test base: needs to be pointed at the test cluster
 */
@Slf4j
//@CompileStatic

abstract class FunTestBase extends RenderTestBase implements FuntestProperties {

  public static final String CONF_DIR = sysprop(FuntestProperties.CONF_DIR_PROP)

  public static final boolean FUNTESTS_ENABLED
  public static final int TEST_TIMEOUT
  public static final String YARN_RAM_REQUEST

  public static final YarnConfiguration CONFIG
  public static final File CONF_DIRECTORY = new File(
      CONF_DIR).canonicalFile
  public static final File CONF_XML_FILE = new File(CONF_DIRECTORY,
      CLIENT_CONFIG_FILENAME).canonicalFile
  static HadoopFS hdfs

  public static YarnConfiguration loadSliderConf(File confFile) {
    URI confURI = confFile.toURI();
    YarnConfiguration conf = new YarnConfiguration()
    def confXmlUrl = confURI.toURL()
    conf.addResource(confXmlUrl)
    conf.set(KEY_TEST_CONF_XML, confXmlUrl.toString())
    conf.set(KEY_TEST_CONF_DIR, CONF_DIRECTORY.absolutePath)
    return conf
  }

  static {
    CONFIG = loadSliderConf(CONF_XML_FILE);

    TEST_TIMEOUT = getTimeOptionMillis(CONFIG,
        KEY_TEST_TIMEOUT,
        1000 * DEFAULT_TEST_TIMEOUT_SECONDS)
    FUNTESTS_ENABLED =
        CONFIG.getBoolean(KEY_FUNTESTS_ENABLED, true)

    YARN_RAM_REQUEST = CONFIG.get(
        KEY_TEST_YARN_RAM_REQUEST,
        DEFAULT_YARN_RAM_REQUEST)

  }


  @Rule
  public final Timeout testTimeout = new Timeout(TEST_TIMEOUT);

  @BeforeClass
  public static void setupTestBase() {
    if (SliderUtils.maybeInitSecurity(CONFIG)) {
      log.debug("Security enabled")
      SliderUtils.forceLogin()
    } else {
      log.info "Security off, making cluster dirs broadly accessible"
    }
    filesystemURL = HadoopFS.getDefaultUri(CONFIG).toString()
    hdfs = HadoopFS.get(CONFIG)
    resourceManager = CONFIG.get(YarnConfiguration.RM_ADDRESS)
    assert null != resourceManager

    zookeeper = CONFIG.get(KEY_TEST_ZK_HOSTS)
    assert null != zookeeper

    log.info("Test using ${filesystemURL} " +
        "\nYARN RM @ ${resourceManager}" +
        "\nZK $zookeeper")
  }

  public static void render(List<String> args) {
    render(CONFIG, args)
  }
}
