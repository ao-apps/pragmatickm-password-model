/*
 * pragmatickm-password-model - Passwords nested within SemanticCMS pages and elements.
 * Copyright (C) 2013, 2014, 2015, 2016  AO Industries, Inc.
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
 * along with pragmatickm-password-model.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.pragmatickm.password.model;

import com.aoindustries.util.StringUtility;
import com.semanticcms.core.model.Element;

public class PasswordTable extends Element {

	private static final String DEFAULT_HEADER = "Passwords";

	private String header = DEFAULT_HEADER;

	@Override
	public PasswordTable freeze() {
		super.freeze();
		return this;
	}

	public String getHeader() {
		synchronized(lock) {
			return header;
		}
	}

	public void setHeader(String header) {
		synchronized(lock) {
			checkNotFrozen();
			this.header = StringUtility.nullIfEmpty(header);
		}
	}

	@Override
	public String getLabel() {
		synchronized(lock) {
			return header==null ? DEFAULT_HEADER : header;
		}
	}

	@Override
	protected String getDefaultIdPrefix() {
		return "password-table";
	}
}
