/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class QueryCreator {

  private static final TrackingParameterMethod[] TRACKING_PARAMETER_METHODS =
      initializeTrackingParameterMethods();

  private final TrackerConfiguration trackerConfiguration;

  private static TrackingParameterMethod[] initializeTrackingParameterMethods() {
    Field[] declaredFields = MatomoRequest.class.getDeclaredFields();
    List<TrackingParameterMethod> methods = new ArrayList<>(declaredFields.length);
    for (Field field : declaredFields) {
      if (field.isAnnotationPresent(TrackingParameter.class)) {
        addMethods(methods, field, field.getAnnotation(TrackingParameter.class));
      }
    }
    return methods.toArray(new TrackingParameterMethod[0]);
  }

  private static void addMethods(
      Collection<TrackingParameterMethod> methods,
      Member member,
      TrackingParameter trackingParameter
  ) {
    try {
      for (PropertyDescriptor pd : Introspector
          .getBeanInfo(MatomoRequest.class)
          .getPropertyDescriptors()) {
        if (member.getName().equals(pd.getName())) {
          String regex = trackingParameter.regex();
          methods.add(TrackingParameterMethod
              .builder()
              .parameterName(trackingParameter.name())
              .method(pd.getReadMethod())
              .pattern(regex == null || regex.isEmpty() || regex.trim().isEmpty() ? null :
                  Pattern.compile(trackingParameter.regex()))
              .build());
        }
      }
    } catch (IntrospectionException e) {
      throw new MatomoException("Could not initialize read methods", e);
    }
  }

  String createQuery(
      @NonNull
      MatomoRequest request,
      @Nullable
      String authToken
  ) {
    StringBuilder query = new StringBuilder(100);
    if (request.getSiteId() == null) {
      appendAmpersand(query);
      query.append("idsite=").append(trackerConfiguration.getDefaultSiteId());
    }
    if (authToken != null) {
      if (authToken.length() != 32) {
        throw new IllegalArgumentException("Auth token must be exactly 32 characters long");
      }
      query.append("token_auth=").append(authToken);
    }
    for (TrackingParameterMethod method : TRACKING_PARAMETER_METHODS) {
      appendParameter(method, request, query);
    }
    if (request.getCustomTrackingParameters() != null) {
      for (Entry<String, Collection<Object>> entry : request
          .getCustomTrackingParameters()
          .entrySet()) {
        for (Object value : entry.getValue()) {
          if (value != null && !value.toString().trim().isEmpty()) {
            appendAmpersand(query);
            query.append(encode(entry.getKey())).append('=').append(encode(value.toString()));
          }
        }
      }
    }
    if (request.getDimensions() != null) {
      int i = 0;
      for (Object dimension : request.getDimensions()) {
        appendAmpersand(query);
        query.append("dimension").append(i + 1).append('=').append(dimension);
        i++;
      }
    }
    return query.toString();
  }

  private static void appendAmpersand(StringBuilder query) {
    if (query.length() != 0) {
      query.append('&');
    }
  }

  private static void appendParameter(
      TrackingParameterMethod method, MatomoRequest request, StringBuilder query
  ) {
    try {
      Object parameterValue = method.getMethod().invoke(request);
      if (parameterValue != null) {
        method.validateParameterValue(parameterValue);
        appendAmpersand(query);
        query.append(method.getParameterName()).append('=');
        if (parameterValue instanceof Boolean) {
          query.append((boolean) parameterValue ? '1' : '0');
        } else if (parameterValue instanceof Charset) {
          query.append(((Charset) parameterValue).name());
        } else if (parameterValue instanceof Instant) {
          query.append(((Instant) parameterValue).getEpochSecond());
        } else {
          String parameterValueString = parameterValue.toString();
          if (!parameterValueString.trim().isEmpty()) {
            query.append(encode(parameterValueString));
          }
        }
      }
    } catch (Exception e) {
      throw new MatomoException("Could not append parameter", e);
    }
  }

  @NonNull
  private static String encode(
      @NonNull
      String parameterValue
  ) {
    try {
      return URLEncoder.encode(parameterValue, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new MatomoException("Could not encode parameter", e);
    }
  }


}
