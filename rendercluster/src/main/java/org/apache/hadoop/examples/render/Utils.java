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

package org.apache.hadoop.examples.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

  /**
   * Take a collection, return a list containing the string value of every
   * element in the collection.
   * @param c collection
   * @return a stringified list
   */
  public static Logger log = LoggerFactory.getLogger(Utils.class);

  public static <T> List<String> collectionToStringList(Collection<T> c) {
    List<String> l = new ArrayList<>(c.size());
    Stream<String> stringified = c.stream().map(Object::toString);
    return stringified.collect(Collectors.toList());
  }

  public static <T> String join(Collection<T> collection,
      String separator,
      boolean trailing) {
    StringBuilder b = new StringBuilder();

    collection.stream().forEach(
        (o) ->
            b.append(o.toString()).append(separator)
    );

/*
    collection.stream().map(
        ((o) -> o.toString() + separator))
              .reduce("", ((l, r) -> l + r));
*/


    return (trailing || b.length()==0)?
           b.toString()
                    : (b.substring(0, b.length() - 1));
  }
}
