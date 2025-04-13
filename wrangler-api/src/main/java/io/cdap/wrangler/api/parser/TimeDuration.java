/*
 * Copyright © 2025 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


/**
 * Token implementation for parsing and storing time durations like "2s", "500ms", etc.
 */



package io.cdap.wrangler.api.parser;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class TimeDuration implements Token {
  private static final Pattern PATTERN = Pattern.compile("(\\d+(?:\\.\\d+)?)(ms|s|m|h)", Pattern.CASE_INSENSITIVE);
  private final long milliseconds;
  private final String rawValue;

  public TimeDuration(String value) {
    this.rawValue = value;
    Matcher matcher = PATTERN.matcher(value.trim());
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid time duration format: " + value);
    }

    double number = Double.parseDouble(matcher.group(1));
    String unit = matcher.group(2).toLowerCase(Locale.ENGLISH);

    switch (unit) {
      case "ms":
        milliseconds = (long) number;
        break;
      case "s":
        milliseconds = (long) (number * 1000);
        break;
      case "m":
        milliseconds = (long) (number * 60 * 1000);
        break;
      case "h":
        milliseconds = (long) (number * 60 * 60 * 1000);
        break;
      default:
        throw new IllegalArgumentException("Unknown time duration unit: " + unit);
    }
  }

  public long getMilliseconds() {
    return milliseconds;
  }

  @Override
  public Object value() {
    return milliseconds;
  }

  @Override
  public TokenType type() {
    return TokenType.TIME_DURATION; // make sure you add this in TokenType enum
  }

  @Override
  public JsonElement toJson() {
    return new JsonPrimitive(milliseconds);
  }
}




