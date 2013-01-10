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

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.sonar.api.config.Settings;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.UserDetails;

public class CasUserProvider extends ExternalUsersProvider {

  public static final String PROPERTY_ATTRIBUTE_NAME = "sonar.cas.saml11.attribute.name";
  public static final String PROPERTY_ATTRIBUTE_EMAIL = "sonar.cas.saml11.attribute.email";

  private Settings settings;

  public CasUserProvider(Settings settings) {
	this.settings = settings;
  }

  @Override
  public UserDetails doGetUserDetails(Context context) {
    Assertion assertion = (Assertion) context.getRequest().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
    if (assertion==null || assertion.getPrincipal()==null) {
      return null;
    }
    UserDetails details = new UserDetails();
    details.setName(resolveAttributeValue(PROPERTY_ATTRIBUTE_NAME, assertion.getPrincipal().getAttributes()));
    details.setEmail(resolveAttributeValue(PROPERTY_ATTRIBUTE_EMAIL, assertion.getPrincipal().getAttributes()));
    if (details.getName()==null) {
      details.setName(assertion.getPrincipal().getName());
    }
    return details;
  }

  private String resolveAttributeValue(String attributeProperty, Map<String, Object> attributes) {
    if (attributeProperty==null || attributes==null) {
      return null;
    }
    if (!settings.hasKey(attributeProperty)) {
      return null;
    }
    String attributeName = settings.getString(attributeProperty);
    if (StringUtils.isBlank(attributeName)) {
      return null;
    }
    if (!attributes.containsKey(attributeName)) {
      return null;
    }
    Object attributeValue = attributes.get(attributeName);
    if (attributeValue==null) {
        return null;
    }
    if (!(attributeValue instanceof String)) {
      return null;
    }
    if (StringUtils.isBlank((String) attributeValue)) {
      return null;
    }
    return (String) attributeValue;
  }
}
