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

public interface CasPluginConstants {

  static final String PROPERTY_CREATE_USERS = "sonar.authenticator.createUsers";
  static final String PROPERTY_SECURITY_REALM = "sonar.security.realm";

  static final String PROPERTY_PROTOCOL = "sonar.cas.protocol";
  static final String PROPERTY_SONAR_SERVER_URL = "sonar.cas.sonarServerUrl";
  static final String PROPERTY_CAS_URL_PREFIX = "sonar.cas.casServerUrlPrefix";
  static final String PROPERTY_CAS_LOGIN_URL = "sonar.cas.casServerLoginUrl";
  static final String PROPERTY_CAS_LOGOUT_URL = "sonar.cas.casServerLogoutUrl";
  static final String PROPERTY_LOGOUT_REDIRECT = "sonar.cas.logoutRedirect";
  static final String PROPERTY_SEND_GATEWAY = "sonar.cas.sendGateway";

  static final String PROPERTY_SAML11_TOLERANCE = "sonar.cas.saml11.toleranceMilliseconds";
  static final String PROPERTY_SAML11_ATTRIBUTE_NAME = "sonar.cas.saml11.attribute.name";
  static final String PROPERTY_SAML11_ATTRIBUTE_EMAIL = "sonar.cas.saml11.attribute.email";
  static final String PROPERTY_SAML11_ATTRIBUTE_GROUPS = "sonar.cas.saml11.attribute.groups";

  static final String PROTOCOL_CAS1 = "cas1";
  static final String PROTOCOL_CAS2 = "cas2";
  static final String PROTOCOL_SAML11 = "saml11";

}
