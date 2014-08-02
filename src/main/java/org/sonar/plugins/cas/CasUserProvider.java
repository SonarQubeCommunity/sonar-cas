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
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.sonar.api.config.Settings;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.UserDetails;
import org.sonar.plugins.cas.util.CasPluginConstants;
import org.sonar.plugins.cas.util.CasUtils;

import java.util.Map;

public class CasUserProvider extends ExternalUsersProvider implements CasPluginConstants {

  private Settings settings;
  private Map<String, AttributePrincipal> principalMap;

  public CasUserProvider(Settings settings, Map<String, AttributePrincipal> principalMap) {
    this.settings = settings;
    this.principalMap = principalMap;
  }

  @Override
  public UserDetails doGetUserDetails(Context context) {
    Assertion assertion = (Assertion) context.getRequest().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
    if (assertion == null || assertion.getPrincipal() == null) {
      return null;
    }

    UserDetails details = new UserDetails();
    details.setName(CasUtils.resolveAttributeValue(settings, PROPERTY_SAML11_ATTRIBUTE_NAME, assertion.getPrincipal().getAttributes()));
    details.setEmail(CasUtils.resolveAttributeValue(settings, PROPERTY_SAML11_ATTRIBUTE_EMAIL, assertion.getPrincipal().getAttributes()));
    if (details.getName() == null) {
      details.setName(assertion.getPrincipal().getName());
    }

    principalMap.put(assertion.getPrincipal().getName(), assertion.getPrincipal());
    return details;
  }

}
