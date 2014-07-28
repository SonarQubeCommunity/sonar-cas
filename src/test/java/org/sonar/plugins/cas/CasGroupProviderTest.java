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

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.authentication.AttributePrincipalImpl;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.plugins.cas.util.CasPluginConstants;

public class CasGroupProviderTest implements CasPluginConstants {

  @Test
  public void should_provide_groups_from_saml11_attributes() {
    Settings settings = new Settings()
      .setProperty(PROPERTY_SAML11_ATTRIBUTE_GROUPS, "groups,roles");

    Map<String, Object> attributes = new HashMap<String, Object>();
    attributes.put("groups", Arrays.asList(new String[] { "group1", "group2" }));
    attributes.put("roles", Arrays.asList(new String[] { "role1", "role2" }));
    
    Map<String, AttributePrincipal> principalMap = new HashMap<String, AttributePrincipal>();
    principalMap.put("goldorak", new AttributePrincipalImpl("goldorak", attributes));
    
    CasGroupProvider provider = new CasGroupProvider(settings, principalMap);
    Collection<String> groups = provider.doGetGroups("goldorak");

    assertThat(groups).doesNotHaveDuplicates();
    assertThat(groups).contains("group1", "group2", "role1", "role2");
  }

}
