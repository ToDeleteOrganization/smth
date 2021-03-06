package com.evozon.evoportal.myaccount.worker;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.evozon.evoportal.evozonprojects.model.ProjectGroup;
import com.evozon.evoportal.evozonprojects.service.ProjectGroupLocalServiceUtil;
import com.evozon.evoportal.my_account.model.DetailsModel;
import com.evozon.evoportal.my_account.model.EvoAddressModel;
import com.evozon.evoportal.my_account.util.MyAccountUtil;
import com.evozon.evoportal.myaccount.builder.AbstractAccountActionOperation;
import com.evozon.evoportal.ws.pmreports.calls.PMReportsPersonCall;
import com.evozon.evoportal.ws.pmreports.model.PmResponseStatus;
import com.evozon.evoportal.ws.pmreports.util.PMReportsConstants;
import com.evozon.evoportal.ws.pmreports.util.PMReportsIntegrationUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;

public abstract class PMReportsIntegration extends ActionWorker {

	private static final Log logger = LogFactoryUtil.getLog(PMReportsIntegration.class);

	protected static final String DATE_FORMAT_MONTH_DAY_YEAR = "MM/dd/yyyy";

	private static Map<String, String> pmReportsDepartmentsAssociations = new LinkedHashMap<String, String>();

	private static final String HR_DEPARTMENT_NAME = "HR Department";

	static {
		pmReportsDepartmentsAssociations.put("HR Department", "HR");
		pmReportsDepartmentsAssociations.put("QA Department", "QA");
		pmReportsDepartmentsAssociations.put("Java Department", "Java");
		pmReportsDepartmentsAssociations.put("IT Support Department", "IT");
		pmReportsDepartmentsAssociations.put("Perl Department", "Perl");
		pmReportsDepartmentsAssociations.put(".Net Department", ".Net");
		pmReportsDepartmentsAssociations.put("Mobile Department", "Mobile");
		pmReportsDepartmentsAssociations.put("Ixxus Department", "Ixxus");
		pmReportsDepartmentsAssociations.put("Accounting Department", "Accounting");
		pmReportsDepartmentsAssociations.put("Sales&Marketing", "Sales");
		pmReportsDepartmentsAssociations.put("Games Department", "Games");
		pmReportsDepartmentsAssociations.put("Web Department", "Web Development");

		// these doesn't exist as departments, but as projects
		pmReportsDepartmentsAssociations.put("Navision", "Navision");
	}

	// TODO: remove user object from here, maybe add userId in newAccountModel objcet
	private String findPMReportsAssociatedDepartment(User forUser) {
		List<String> userDepartments = newAccountModelHolder.getUserDepartments();

		// if the user is part of the HR Department
		if (userDepartments.contains(HR_DEPARTMENT_NAME)) {
			return pmReportsDepartmentsAssociations.get(HR_DEPARTMENT_NAME);
		}

		// check user projects, ex. 'Navision' is a dep. in PM and a project in
		// EvoPortal.
		String pmReportsDepartmentName = StringPool.BLANK;

		List<ProjectGroup> userProjects = ProjectGroupLocalServiceUtil.findProjectsByUser(forUser.getUserId());
		for (ProjectGroup pGroup : userProjects) {
			String name = pGroup.getProjectGroupName();
			if (pmReportsDepartmentsAssociations.containsKey(name)) {
				pmReportsDepartmentName = name;
				break;
			}
		}

		if (!pmReportsDepartmentName.isEmpty()) {
			return pmReportsDepartmentName;
		}

		// last option check department names
		for (String departmentName : userDepartments) {
			if (pmReportsDepartmentsAssociations.containsKey(departmentName)) {
				pmReportsDepartmentName = pmReportsDepartmentsAssociations.get(departmentName);
				break;
			}
		}

		return pmReportsDepartmentName;
	}

	private MultiValueMap<String, Object> getPMReportsParameters(User forUser) {
		MultiValueMap<String, Object> mapModifications = new LinkedMultiValueMap<String, Object>();

		DetailsModel detailsModel = newAccountModelHolder.getDetailsModel();

		mapModifications.add(PMReportsConstants.USER_CNP, detailsModel.getCNP());
		mapModifications.add(PMReportsConstants.USER_EMAIL, detailsModel.getEmailAddress());
		mapModifications.add(PMReportsConstants.USER_LAST_NAME, detailsModel.getLastName());
		mapModifications.add(PMReportsConstants.USER_FIRST_NAME, detailsModel.getFirstName());

		String startDate = MyAccountUtil.convertDateToString(newAccountModelHolder.getFreeDaysModel().getStartDate(), DATE_FORMAT_MONTH_DAY_YEAR);
		mapModifications.add(PMReportsConstants.USER_DATE_HIRED, startDate);

		String pmDep = findPMReportsAssociatedDepartment(forUser);
		if (!pmDep.isEmpty()) {
			mapModifications.add(PMReportsConstants.DEPARTMENT_NAME, pmDep);
		}

		String countryCode = null;
		EvoAddressModel primaryAddress = newAccountModelHolder.getPrimaryAddress();
		if (primaryAddress != null) {
			mapModifications.add(PMReportsConstants.USER_ZIP_CODE, primaryAddress.getPostalCode());
			mapModifications.add(PMReportsConstants.USER_CITY_NAME, primaryAddress.getCity());

			countryCode = primaryAddress.getCountryCode();
			if (countryCode.isEmpty()) {
				countryCode = PMReportsConstants.USER_DEFAULT_COUNTRY_CODE;
			}

			mapModifications.add(PMReportsConstants.USER_STREET_NAME, primaryAddress.getStreetName());
			mapModifications.add(PMReportsConstants.USER_STREET_NUMBER, primaryAddress.getStreetNumber());
		}
		// The 'country code' is mandatory
		mapModifications.add(PMReportsConstants.USER_COUNTRY_CODE, (countryCode == null) ? PMReportsConstants.USER_DEFAULT_COUNTRY_CODE : countryCode);

		mapModifications.add(PMReportsConstants.USER_MOBILE_NUMBER, detailsModel.getPhoneNumber());
		mapModifications.add(PMReportsConstants.USER_PLATE_NUMBER, detailsModel.getLicensePlate());

		return mapModifications;
	}

	public void execute(AbstractAccountActionOperation accountAction) throws PortalException, SystemException {
		long companyId = accountAction.getCompanyId();
		User forUser = accountAction.getSelectedUser();
		setNewAccountModelHolder(accountAction.getNewAccountModelHolder());

		PmResponseStatus pmResponse = null;
		if (PMReportsIntegrationUtil.isPmReportsIntegrationActivated() && hasPMReportsParamatersModified()) {
			MultiValueMap<String, Object> pmParams = getPMReportsParameters(forUser);

			PMReportsPersonCall pmPersonCall = new PMReportsPersonCall(companyId);
			pmResponse = executeIntegration(pmPersonCall, pmParams);
		}

		logger.info(pmResponse);
	}

	public abstract PmResponseStatus executeIntegration(PMReportsPersonCall pmPersonCall, MultiValueMap<String, Object> pmParams);

	protected abstract boolean hasPMReportsParamatersModified();

}
