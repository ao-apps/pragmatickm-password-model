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

import com.aoindustries.lang.ObjectUtils;
import com.semanticcms.core.model.Element;
import com.semanticcms.core.model.PageRef;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Password extends Element {

	public static class CustomField {

		private final PageRef pageRef;
		private final String element;
		private final String value;

		public CustomField(PageRef pageRef, String element, String value) {
			if(pageRef == null && value == null) throw new IllegalArgumentException("At least one of page or value must be provided.");
			this.pageRef = pageRef;
			this.element = element;
			this.value = value;
		}

		@Override
		public String toString() {
			if(value != null) return value;
			if(element == null) return pageRef.toString();
			return pageRef.toString() + '#' + element;
		}

		@Override
		public boolean equals(Object o) {
			if(!(o instanceof CustomField)) return false;
			CustomField other = (CustomField)o;
			return
				ObjectUtils.equals(pageRef, other.pageRef)
				&& ObjectUtils.equals(element, other.element)
				&& ObjectUtils.equals(value, other.value)
			;
		}

		@Override
		public int hashCode() {
			return ObjectUtils.hash(
				pageRef,
				element,
				value
			);
		}

		public PageRef getPageRef() {
			return pageRef;
		}

		public String getElement() {
			return element;
		}

		public String getValue() {
			return value;
		}
	}

	private String href;
	private String username;
	private String password;
	private Map<String,CustomField> customFields;
	private Map<String,String> secretQuestions;

	@Override
	public Password freeze() {
		super.freeze();
		return this;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		checkNotFrozen();
		if(href!=null && href.isEmpty()) href = null;
		this.href = href;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		checkNotFrozen();
		if(username!=null && username.isEmpty()) username = null;
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		checkNotFrozen();
		this.password = password;
	}

	public Map<String,CustomField> getCustomFields() {
		if(customFields == null) return Collections.emptyMap();
		return Collections.unmodifiableMap(customFields);
	}

	public void addCustomField(String name, PageRef pageRef, String element, String value) {
		checkNotFrozen();
		if(customFields == null) customFields = new LinkedHashMap<String,CustomField>();
		if(customFields.put(name, new CustomField(pageRef, element, value)) != null) throw new IllegalStateException("Duplicate custom field: " + name);
		if(pageRef != null) addPageLink(pageRef);
	}

	public Map<String,String> getSecretQuestions() {
		if(secretQuestions == null) return Collections.emptyMap();
		return Collections.unmodifiableMap(secretQuestions);
	}

	public void addSecretQuestion(String question, String answer) {
		checkNotFrozen();
		if(secretQuestions == null) secretQuestions = new LinkedHashMap<String,String>();
		if(secretQuestions.put(question, answer) != null) throw new IllegalStateException("Duplicate secret question: " + question);
	}

	@Override
	public String getLabel() {
		if(username != null) return username;
		if(href != null) return href;
		return "Password";
	}

	@Override
	public String getListItemCssClass() {
		return "list-item-password";
	}

	@Override
	protected String getDefaultIdPrefix() {
		return "password";
	}
}
