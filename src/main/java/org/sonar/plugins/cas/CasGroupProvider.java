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
package org.sonar.plugins.cas;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.sonar.api.config.Settings;
import org.sonar.api.security.ExternalGroupsProvider;
import org.sonar.plugins.cas.util.CasPluginConstants;
import org.sonar.plugins.cas.util.CasUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CasGroupProvider extends ExternalGroupsProvider implements CasPluginConstants {

  private Settings settings;
  private Map<String, AttributePrincipal> principalMap;

  public CasGroupProvider(Settings settings, Map<String, AttributePrincipal> principalMap) {
    this.settings = settings;
    this.principalMap = principalMap;
  }

  @Override
  public Collection<String> doGetGroups(String username) {
    AttributePrincipal principal = principalMap.get(username);
    if (principal == null || principal.getAttributes() == null) {
      return null;
    }

    Set<String> groups = new HashSet<String>();
    String[] groupAttributes = settings.getStringArray(PROPERTY_SAML11_ATTRIBUTE_GROUPS);
    for (String groupAttribute : groupAttributes) {
      List<String> values = CasUtils.resolveAttributeValues(groupAttribute, principal.getAttributes());
      if (values != null) {
        groups.addAll(values);
      }
    }

    principalMap.remove(username);
    if (groups.isEmpty()) {
      return null;
    }
    return groups;
  }

}
