/*
 * pragmatickm-password-model - Passwords nested within SemanticCMS pages and elements.
 * Copyright (C) 2013, 2014, 2015, 2016, 2019, 2020, 2021  AO Industries, Inc.
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

import com.aoapps.collections.AoCollections;
import com.aoapps.lang.NullArgumentException;
import static com.aoapps.lang.Strings.nullIfEmpty;
import com.aoapps.net.URIDecoder;
import com.aoapps.net.URIEncoder;
import com.semanticcms.core.model.Element;
import com.semanticcms.core.model.PageRef;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Password extends Element {

	public static class CustomField {

		private final PageRef pageRef;
		private final String element;
		private final String value;

		public CustomField(PageRef pageRef, String element, String value) {
			value = nullIfEmpty(value);
			if(pageRef == null && value == null) throw new IllegalArgumentException("At least one of page or value must be provided.");
			this.pageRef = pageRef;
			this.element = nullIfEmpty(element);
			this.value = value;
		}

		@Override
		public String toString() {
			if(value != null) return value;
			String pageToString = pageRef.toString();
			if(element == null) return pageToString;
			// TODO: encodeIRIComponent to do this in one shot?
			String elementIri = URIDecoder.decodeURI(URIEncoder.encodeURIComponent(element));
			int sbLen =
				pageToString.length()
				+ 1 // '#'
				+ elementIri.length();
			StringBuilder sb = new StringBuilder(sbLen);
			sb.append(pageToString).append('#').append(elementIri);
			assert sb.length() == sbLen;
			return sb.toString();
		}

		@Override
		public boolean equals(Object o) {
			if(!(o instanceof CustomField)) return false;
			CustomField other = (CustomField)o;
			return
				Objects.equals(pageRef, other.pageRef)
				&& Objects.equals(element, other.element)
				&& Objects.equals(value, other.value)
			;
		}

		@Override
		public int hashCode() {
			return Objects.hash(
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

	private volatile String href;
	private volatile String username;
	private volatile String password;
	private Map<String, CustomField> customFields;
	private Map<String, String> secretQuestions;

	@Override
	public Password freeze() {
		synchronized(lock) {
			if(!frozen) {
				customFields = AoCollections.optimalUnmodifiableMap(customFields);
				secretQuestions = AoCollections.optimalUnmodifiableMap(secretQuestions);
				super.freeze();
			}
		}
		return this;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		checkNotFrozen();
		this.href = nullIfEmpty(href);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		checkNotFrozen();
		this.username = nullIfEmpty(username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		checkNotFrozen();
		this.password = nullIfEmpty(password);
	}

	@SuppressWarnings("ReturnOfCollectionOrArrayField") // Returning unmodifiable
	public Map<String, CustomField> getCustomFields() {
		synchronized(lock) {
			if(customFields == null) return Collections.emptyMap();
			if(frozen) return customFields;
			return AoCollections.unmodifiableCopyMap(customFields);
		}
	}

	public void addCustomField(String name, PageRef pageRef, String element, String value) {
		name = nullIfEmpty(name);
		NullArgumentException.checkNotNull(name, "name");
		synchronized(lock) {
			checkNotFrozen();
			if(customFields == null) customFields = new LinkedHashMap<>();
			if(customFields.put(name, new CustomField(pageRef, element, value)) != null) throw new IllegalStateException("Duplicate custom field: " + name);
			if(pageRef != null) addPageLink(pageRef);
		}
	}

	@SuppressWarnings("ReturnOfCollectionOrArrayField") // Returning unmodifiable
	public Map<String, String> getSecretQuestions() {
		synchronized(lock) {
			if(secretQuestions == null) return Collections.emptyMap();
			if(frozen) return secretQuestions;
			return AoCollections.unmodifiableCopyMap(secretQuestions);
		}
	}

	public void addSecretQuestion(String question, String answer) {
		question = nullIfEmpty(question);
		answer = nullIfEmpty(answer);
		NullArgumentException.checkNotNull(question, "question");
		NullArgumentException.checkNotNull(answer, "answer");
		synchronized(lock) {
			checkNotFrozen();
			if(secretQuestions == null) secretQuestions = new LinkedHashMap<>();
			if(secretQuestions.put(question, answer) != null) throw new IllegalStateException("Duplicate secret question: " + question);
		}
	}

	@Override
	public String getLabel() {
		//if(username != null) return username;
		//if(href != null) return href;
		String p = password;
		if(p != null) return p;
		return "Password";
	}

	@Override
	protected String getDefaultIdPrefix() {
		return "password";
	}
}
