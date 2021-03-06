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
String className = (String)request.getAttribute("addresses.className");
long classPK = (Long)request.getAttribute("addresses.classPK");

List<Address> addresses = Collections.emptyList();

int[] addressesIndexes = null;

String addressesIndexesParam = ParamUtil.getString(request, "addressesIndexes");

if (Validator.isNotNull(addressesIndexesParam)) {
	addresses = new ArrayList<Address>();

	addressesIndexes = StringUtil.split(addressesIndexesParam, 0);

	for (int addressesIndex : addressesIndexes) {
		addresses.add(new AddressImpl());
	}
}
else {
	if (classPK > 0) {
		addresses = AddressServiceUtil.getAddresses(className, classPK);

		addressesIndexes = new int[addresses.size()];

		for (int i = 0; i < addresses.size() ; i++) {
			addressesIndexes[i] = i;
		}
	}

	if (addresses.isEmpty()) {
		addresses = new ArrayList<Address>();

		addresses.add(new AddressImpl());

		addressesIndexes = new int[] {0};
	}

	if (addressesIndexes == null) {
		addressesIndexes = new int[0];
	}
}

boolean isAddressEnalbed = Boolean.FALSE;
request.setAttribute(com.liferay.portal.util.WebKeys.FORM_NAVIGATOR_SECTION_SHOW + "addresses", isAddressEnalbed);

%>

<liferay-ui:error-marker key="errorSection" value="addresses" />

<h3><liferay-ui:message key="addresses" /></h3>

<div class="portlet-msg-info">
	<liferay-ui:message key="street-and-city-are-required-fields.-postal-code-could-be-required-in-some-countries" />
</div>

<liferay-ui:error exception="<%= AddressCityException.class %>" message="please-enter-a-valid-city" />
<liferay-ui:error exception="<%= AddressStreetException.class %>" message="please-enter-a-valid-street" />
<liferay-ui:error exception="<%= AddressZipException.class %>" message="please-enter-a-valid-postal-code" />
<liferay-ui:error exception="<%= NoSuchCountryException.class %>" message="please-select-a-country" />
<liferay-ui:error key="<%= NoSuchListTypeException.class.getName() + className + ListTypeConstants.ADDRESS %>" message="please-select-a-type" />
<liferay-ui:error exception="<%= NoSuchRegionException.class %>" message="please-select-a-region" />

<aui:fieldset cssClass="addresses">

	<%
	for (int i = 0; i < addressesIndexes.length; i++) {
		int addressesIndex = addressesIndexes[i];

		Address address = addresses.get(i);

		long countryId = ParamUtil.getLong(request, "addressCountryId" + addressesIndex, address.getCountryId());
		long regionId = ParamUtil.getLong(request, "addressRegionId" + addressesIndex, address.getRegionId());
	%>

		<aui:model-context bean="<%= address %>" model="<%= Address.class %>" />

		<div class="lfr-form-row">
			<div class="row-fields">
				<aui:column columnWidth="50">
					<aui:input name='<%= "addressId" + addressesIndex %>' type="hidden" value="<%= address.getAddressId() %>" />

					<aui:input fieldParam='<%= "addressStreet1_" + addressesIndex %>' id='<%= "addressStreet1_" + addressesIndex %>' name="street1" label="street-name"/>

					<aui:input fieldParam='<%= "addressStreet2_" + addressesIndex %>' id='<%= "addressStreet2_" + addressesIndex %>' name="street2" label="street-number"/>

					<aui:select label="country" name='<%= "addressCountryId" + addressesIndex %>' />

					<aui:select label="region" name='<%= "addressRegionId" + addressesIndex %>' />
				</aui:column>

				<aui:column columnWidth="50">
					<aui:select label="type" listType="<%= className + ListTypeConstants.ADDRESS %>" name='<%= "addressTypeId" + addressesIndex %>' />

					<aui:input fieldParam='<%= "addressZip" + addressesIndex %>' id='<%= "addressZip" + addressesIndex %>' label="postal-code" name="zip" />

					<aui:input fieldParam='<%= "addressCity" + addressesIndex %>' id='<%= "addressCity" + addressesIndex %>' name="city" />

					<aui:input checked="<%= address.isPrimary() %>" cssClass="primary-ctrl" id='<%= "addressPrimary" + addressesIndex %>' label="primary" name="addressPrimary" type="radio" value="<%= addressesIndex %>" />

					<aui:input cssClass="mailing-ctrl" fieldParam='<%= "addressMailing" + addressesIndex %>' id='<%= "addressMailing" + addressesIndex %>' name="mailing" />
				</aui:column>
			</div>
		</div>

		<aui:script use="liferay-dynamic-select">
			new Liferay.DynamicSelect(
				[
					{
						select: '<portlet:namespace />addressCountryId<%= addressesIndex %>',
						selectData: Liferay.Address.getCountries,
						selectDesc: 'name',
						selectId: 'countryId',
						selectVal: '<%= countryId %>'
					},
					{
						select: '<portlet:namespace />addressRegionId<%= addressesIndex %>',
						selectData: Liferay.Address.getRegions,
						selectDesc: 'name',
						selectId: 'regionId',
						selectVal: '<%= regionId %>'
					}
				]
			);
		</aui:script>

	<%
	}
	%>

	<aui:input name="addressesIndexes" type="hidden" value="<%= StringUtil.merge(addressesIndexes) %>" />
</aui:fieldset>

<aui:script use="liferay-auto-fields,liferay-dynamic-select">
	Liferay.once(
		'formNavigator:reveal<portlet:namespace />addresses',
		function() {
			var addresses = new Liferay.AutoFields(
				{
					contentBox: '#<portlet:namespace />addresses > fieldset',
					fieldIndexes: '<portlet:namespace />addressesIndexes',
					on: {
						'clone': function(event) {
							var row = event.row;
							var guid = event.guid;

							var dynamicSelects = row.one('select[data-componentType=dynamic_select]');

							if (dynamicSelects) {
								dynamicSelects.detach('change');
							}

							new Liferay.DynamicSelect(
								[
									{
										select: '<portlet:namespace />addressCountryId' + guid,
										selectData: Liferay.Address.getCountries,
										selectDesc: 'name',
										selectId: 'countryId',
										selectVal: ''
									},
									{
										select: '<portlet:namespace />addressRegionId' + guid,
										selectData: Liferay.Address.getRegions,
										selectDesc: 'name',
										selectId: 'regionId',
										selectVal: ''
									}
								]
							);
						}
					}
				}
			).render();
		}
	);
</aui:script>