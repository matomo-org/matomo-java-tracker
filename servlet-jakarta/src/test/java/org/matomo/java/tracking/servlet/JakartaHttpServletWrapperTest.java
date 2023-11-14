package org.matomo.java.tracking.servlet;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.Cookie;
import java.util.List;
import org.junit.jupiter.api.Test;

class JakartaHttpServletWrapperTest {

  @Test
  void wrapsHttpServletRequest() {

    MockHttpServletRequest servlet = new MockHttpServletRequest();
    servlet.setRequestURL(new StringBuffer("http://localhost"));
    servlet.setRemoteUser("remote-user");
    servlet.setHeaders(singletonMap("Accept-Language", "en-US,en;q=0.9,de;q=0.8"));
    servlet.setCookies(List.of(new Cookie("foo", "bar")));

    HttpServletRequestWrapper httpServletRequestWrapper =
        JakartaHttpServletWrapper.fromHttpServletRequest(servlet);

    assertThat(httpServletRequestWrapper.getRequestURL()).hasToString("http://localhost");
    assertThat(httpServletRequestWrapper.getRemoteUser()).hasToString("remote-user");
    assertThat(httpServletRequestWrapper.getHeaders())
        .containsEntry("accept-language", "en-US,en;q=0.9,de;q=0.8");
    assertThat(httpServletRequestWrapper.getCookies())
        .hasSize(1)
        .satisfiesExactly(cookieWrapper -> {
          assertThat(cookieWrapper.getName()).isEqualTo("foo");
          assertThat(cookieWrapper.getValue()).isEqualTo("bar");
        });
  }

}

