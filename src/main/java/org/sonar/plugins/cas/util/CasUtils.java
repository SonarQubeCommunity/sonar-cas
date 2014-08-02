/*
 * Sonar CAS Plugin
 * Copyright (C) 2012 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.cas.util;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CasUtils implements CasPluginConstants {

  @SuppressWarnings("rawtypes")
  public static List<String> resolveAttributeValues(String attributeName, Map<String, Object> attributes) {
    if (attributeName == null || attributes == null) {
      return null;
    }
    if (!attributes.containsKey(attributeName)) {
      return null;
    }

    Object attributeValue = attributes.get(attributeName);
    if (attributeValue == null) {
      return null;
    }

    List<String> values = new ArrayList<String>();
    if (attributeValue instanceof Collection) {
      for (Object value : (Collection) attributeValue) {
        if (value != null && StringUtils.isNotBlank(value.toString())) {
          values.add(value.toString());
        }
      }
    } else if (StringUtils.isNotBlank(attributeValue.toString())) {
      values.add(attributeValue.toString());
    }

    if (values.isEmpty()) {
      return null;
    }
    return values;
  }

  public static String resolveAttributeValue(String attributeName, Map<String, Object> attributes) {
    List<String> values = resolveAttributeValues(attributeName, attributes);
    if (values == null) {
      return null;
    }
    return values.get(0);
  }

  public static String resolveAttributeValue(Settings settings, String attributeProperty, Map<String, Object> attributes) {
    if (attributeProperty == null || !settings.hasKey(attributeProperty)) {
      return null;
    }
    return resolveAttributeValue(settings.getString(attributeProperty), attributes);
  }

}
