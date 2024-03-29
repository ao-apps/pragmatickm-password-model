/*
 * pragmatickm-password-model - Passwords nested within SemanticCMS pages and elements.
 * Copyright (C) 2013, 2014, 2015, 2016, 2020, 2021, 2022  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of pragmatickm-password-model.
 *
 * pragmatickm-password-model is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * pragmatickm-password-model is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with pragmatickm-password-model.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.pragmatickm.password.model;

import com.aoapps.lang.Strings;
import com.semanticcms.core.model.Element;

public class PasswordTable extends Element {

  private static final String DEFAULT_HEADER = "Passwords";

  private volatile String header = DEFAULT_HEADER;

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    checkNotFrozen();
    this.header = Strings.nullIfEmpty(header);
  }

  @Override
  public String getLabel() {
    String h = header;
    return h == null ? DEFAULT_HEADER : h;
  }

  @Override
  protected String getDefaultIdPrefix() {
    return "password-table";
  }
}
