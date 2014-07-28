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
import java.util.concurrent.ConcurrentHashMap;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.sonar.api.config.Settings;
import org.sonar.api.security.Authenticator;
import org.sonar.api.security.ExternalGroupsProvider;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.SecurityRealm;

public class CasSecurityRealm extends SecurityRealm {

  public static final String KEY = "cas";

  private CasAuthenticator authenticator;
  private CasUserProvider usersProvider;
  private CasGroupProvider groupsProvider;

  public CasSecurityRealm(Settings settings) {
    Map<String, AttributePrincipal> principalMap = new ConcurrentHashMap<String, AttributePrincipal>();
    this.authenticator = new CasAuthenticator();
    this.usersProvider = new CasUserProvider(settings, principalMap);
    this.groupsProvider = new CasGroupProvider(settings, principalMap);
  }

  @Override
  public Authenticator doGetAuthenticator() {
    return authenticator;
  }

  @Override
  public ExternalUsersProvider getUsersProvider() {
    return usersProvider;
  }
  
  @Override
  public ExternalGroupsProvider getGroupsProvider() {
    return groupsProvider;
  }

  @Override
  public String getName() {
    return KEY;
  }

}
