<%--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/html/portlet/users_admin/init.jsp" %>

<%
Contact selContact = (Contact)request.getAttribute("user.selContact");
%>

<h3><liferay-ui:message key="instant-messenger" /></h3>

<c:choose>
	<c:when test="<%= selContact != null %>">
		<aui:model-context bean="<%= selContact %>" model="<%= Contact.class %>" />

		<aui:fieldset>
			<aui:input label="aim" name="aimSn" />

			<div class="instant-messenger">
				<aui:input label="icq" name="icqSn" />

				<c:if test="<%= Validator.isNotNull(selContact.getIcqSn()) %>">
					<img alt="" src="http://web.icq.com/whitepages/online?icq=<%= HtmlUtil.escapeAttribute(selContact.getIcqSn()) %>&img=5" />
				</c:if>
			</div>

			<aui:input label="jabber" name="jabberSn" />

			<div class="instant-messenger">
				<aui:input label="skype" name="skypeSn" />

				<c:if test="<%= Validator.isNotNull(selContact.getSkypeSn()) %>">
					<a href="callto://<%= HtmlUtil.escapeAttribute(selContact.getSkypeSn()) %>"><img alt="<liferay-ui:message key="call-this-user" />" src="http://mystatus.skype.com/smallicon/<%= HtmlUtil.escapeAttribute(selContact.getSkypeSn()) %>" /></a>
				</c:if>
			</div>

			<aui:input label="windows-live-messenger" name="msnSn" />

			<div class="instant-messenger">
				<aui:input label="yim" name="ymSn" />

				<c:if test="<%= Validator.isNotNull(selContact.getYmSn()) %>">
					<img alt="" src="http://opi.yahoo.com/online?u=<%= HtmlUtil.escapeAttribute(selContact.getYmSn()) %>&m=g&t=0" />
				</c:if>
			</div>
		</aui:fieldset>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-info"><liferay-ui:message key="this-section-will-be-editable-after-creating-the-user" /></div>
	</c:otherwise>
</c:choose>