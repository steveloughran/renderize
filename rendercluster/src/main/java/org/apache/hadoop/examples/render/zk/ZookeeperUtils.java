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

package org.apache.hadoop.examples.render.zk;

import com.google.common.net.HostAndPort;
import org.apache.hadoop.examples.render.Utils;
import org.apache.hadoop.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZookeeperUtils {

  /**
   * Split a quorum list into a list of hostnames and ports
   * @param hostPortQuorumList split to a list of hosts and ports
   * @return a list of values
   */
  public static List<HostAndPort> splitToHostsAndPorts(String hostPortQuorumList) {
    // split an address hot
    String[] strings = StringUtils.getStrings(hostPortQuorumList);
    List<HostAndPort> list = new ArrayList<>(strings.length);
    for (String s : strings) {
      list.add(HostAndPort.fromString(s.trim()));
    }
    return list;
  }

  /**
   * Build up to a hosts only list
   * @param hostAndPorts
   * @return a list of the hosts only
   */
  public static String buildHostsOnlyList(List<HostAndPort> hostAndPorts) {
    StringBuilder sb = new StringBuilder();
    for (HostAndPort hostAndPort : hostAndPorts) {
      sb.append(hostAndPort.getHostText()).append(",");
    }
    if (sb.length() > 0) {
      sb.delete(sb.length() - 1, sb.length());
    }
    return sb.toString();
  }

  public static String buildQuorumEntry(HostAndPort hostAndPort,
    int defaultPort) {
    String s = hostAndPort.toString();
    if (hostAndPort.hasPort()) {
      return s;
    } else {
      return s + ":" + defaultPort;
    }
  }

  /**
   * Build a quorum list, injecting a ":defaultPort" ref if needed on
   * any entry without one
   * @param hostAndPorts
   * @param defaultPort
   * @return
   */
  public static String buildQuorum(List<HostAndPort> hostAndPorts, int defaultPort) {
    List<String> entries = new ArrayList<>(hostAndPorts.size());
    for (HostAndPort hostAndPort : hostAndPorts) {
      entries.add(buildQuorumEntry(hostAndPort, defaultPort));
    }
    return Utils.join(entries, ",", false);
  }
  
  public static String convertToHostsOnlyList(String quorum) {
    List<HostAndPort> hostAndPorts = splitToHostsAndPortsStrictly(quorum);
    return ZookeeperUtils.buildHostsOnlyList(hostAndPorts);
  }

  public static List<HostAndPort> splitToHostsAndPortsStrictly(String quorum){
    List<HostAndPort> hostAndPorts =
        ZookeeperUtils.splitToHostsAndPorts(quorum);
    if (hostAndPorts.isEmpty()) {
      throw new RuntimeException("empty zookeeper quorum");
    }
    return hostAndPorts;
  }
  
  public static int getFirstPort(String quorum, int defVal)  {
    List<HostAndPort> hostAndPorts = splitToHostsAndPortsStrictly(quorum);
    int port = hostAndPorts.get(0).getPortOrDefault(defVal);
    return port;

  }
}
