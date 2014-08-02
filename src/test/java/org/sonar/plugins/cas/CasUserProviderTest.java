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
import org.jasig.cas.client.authentication.AttributePrincipalImpl;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.UserDetails;
import org.sonar.plugins.cas.util.CasPluginConstants;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CasUserProviderTest implements CasPluginConstants {
  @Test
  public void should_get_username_from_cas_attribute() {
    CasUserProvider provider = new CasUserProvider(new Settings(), new HashMap<String, AttributePrincipal>());
    HttpServletRequest request = mock(HttpServletRequest.class);
    Assertion casAssertion = mock(Assertion.class);
    when(casAssertion.getPrincipal()).thenReturn(new AttributePrincipalImpl("goldorak"));
    when(request.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION)).thenReturn(casAssertion);

    ExternalUsersProvider.Context context = new ExternalUsersProvider.Context(null, request);
    UserDetails user = provider.doGetUserDetails(context);

    assertThat(user.getName()).isEqualTo("goldorak");
  }

  @Test
  public void should_not_return_user_id_missing_cas_attribute() {
    CasUserProvider provider = new CasUserProvider(new Settings(), new HashMap<String, AttributePrincipal>());
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION)).thenReturn(null);

    ExternalUsersProvider.Context context = new ExternalUsersProvider.Context(null, request);
    UserDetails user = provider.doGetUserDetails(context);

    assertThat(user).isNull();
  }

  @Test
  public void should_provide_user_details_from_saml11_attributes() {
    Settings settings = new Settings()
      .setProperty(PROPERTY_SAML11_ATTRIBUTE_NAME, "name")
      .setProperty(PROPERTY_SAML11_ATTRIBUTE_EMAIL, "email");

    CasUserProvider provider = new CasUserProvider(settings, new HashMap<String, AttributePrincipal>());
    HttpServletRequest request = mock(HttpServletRequest.class);
    Assertion casAssertion = mock(Assertion.class);
    Map<String, Object> attributes = new HashMap<String, Object>();
    attributes.put("name", "Mr. Robot");
    attributes.put("email", "robot@example.com");
    when(casAssertion.getPrincipal()).thenReturn(new AttributePrincipalImpl("goldorak", attributes));
    when(request.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION)).thenReturn(casAssertion);

    ExternalUsersProvider.Context context = new ExternalUsersProvider.Context(null, request);
    UserDetails details = provider.doGetUserDetails(context);

    assertThat(details.getName()).isEqualTo("Mr. Robot");
    assertThat(details.getEmail()).isEqualTo("robot@example.com");
  }
}
