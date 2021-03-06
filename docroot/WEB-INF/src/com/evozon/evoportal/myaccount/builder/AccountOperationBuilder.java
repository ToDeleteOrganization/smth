package com.evozon.evoportal.myaccount.builder;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;

import com.evozon.evoportal.my_account.AccountModelHolder;
import com.evozon.evoportal.my_account.model.DetailsModel;
import com.evozon.evoportal.my_account.model.FreeDaysModel;
import com.evozon.evoportal.my_account.util.MyAccountConstants;
import com.evozon.evoportal.myaccount.builder.validators.BirthdayValidator;
import com.evozon.evoportal.myaccount.builder.validators.CNPContentValidator;
import com.evozon.evoportal.myaccount.builder.validators.CNPDuplicateValidator;
import com.evozon.evoportal.myaccount.builder.validators.DateHiredValidator;
import com.evozon.evoportal.myaccount.builder.validators.DeleteAccountPermissionValidator;
import com.evozon.evoportal.myaccount.builder.validators.EmailAddressValidator;
import com.evozon.evoportal.myaccount.builder.validators.FirstNameValidator;
import com.evozon.evoportal.myaccount.builder.validators.FunctieCIMValidator;
import com.evozon.evoportal.myaccount.builder.validators.JobTitleValidator;
import com.evozon.evoportal.myaccount.builder.validators.LastNameValidator;
import com.evozon.evoportal.myaccount.builder.validators.LicensePlateValidator;
import com.evozon.evoportal.myaccount.builder.validators.PhoneNumberValidator;
import com.evozon.evoportal.myaccount.builder.validators.ScreenNameValidator;
import com.evozon.evoportal.myaccount.builder.validators.SiteValidator;
import com.evozon.evoportal.myaccount.builder.validators.UserProjectValidator;
import com.evozon.evoportal.myaccount.worker.AddUserExpandoPropertiesUpdate;
import com.evozon.evoportal.myaccount.worker.AddUserPMReportsIntegration;
import com.evozon.evoportal.myaccount.worker.AddUserPhoneNumberUpdate;
import com.evozon.evoportal.myaccount.worker.UpdateUserPMReportsIntegration;
import com.liferay.compat.portal.util.PortalUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.User;

public final class AccountOperationBuilder {

	public static final String DEFAULT = "DEFAULT";

	private static final String INTERNSHIP_USER_TYPE = "internship";

	private static final String SCHOLARSHIP_USER_TYPE = "scholarship";

	private static final String USER_TYPE = "user_type";

	public static ActionAccountOperation buildAccountActionOperation(ActionPhaseParameters app) throws PortalException, SystemException {
		ActionAccountOperation operation = null;

		String command = ParamUtil.getString(app.getRequest(), Constants.CMD, AccountOperationBuilder.DEFAULT);
		if (Constants.ADD.equals(command)) {
			operation = AccountOperationBuilder.buildAddAccountActionOperation(app);

		} else if (Constants.UPDATE.equals(command)) {
			operation = AccountOperationBuilder.buildUpdateAccountActionOperation(app);

		} else if (Constants.DEACTIVATE.equals(command) || MyAccountConstants.CANCEL_DEACTIVATION.equals(command)) {
			operation = AccountOperationBuilder.buildDeactivateAccountOperation(app);

		} else if (Constants.RESTORE.equals(command) || MyAccountConstants.CANCEL_ACTIVATION.equals(command)) {
			operation = AccountOperationBuilder.buildActivateAccountOperation(app);

		} else if (Constants.DELETE.equals(command)) {
			operation = AccountOperationBuilder.buildDeleteAccountActionOperation(app);

		} else {
			operation = new DefaultAccountActionOperation(app);

		}
		return operation;
	}

	private static ActionAccountOperation buildActivateAccountOperation(ActionPhaseParameters app) {
		AbstractAccountActionOperation restoreAccountCommandOperation = new ActivateAccountOperation(app);
		return restoreAccountCommandOperation;
	}

	private static ActionAccountOperation buildDeactivateAccountOperation(ActionPhaseParameters app) throws PortalException, SystemException {
		AbstractAccountActionOperation deactivateAccountCommandOperation = new DeactivateAccountOperation(app);
		deactivateAccountCommandOperation.addValidationRule(new UserProjectValidator(app.getRequest()));
		return deactivateAccountCommandOperation;
	}

	private static ActionAccountOperation buildDeleteAccountActionOperation(ActionPhaseParameters app) throws PortalException, SystemException {
		AbstractAccountActionOperation deleteAccountCommandOperation = new DeleteAccountOperation(app);
		User currentUser = PortalUtil.getUser(app.getRequest());
		deleteAccountCommandOperation.addValidationRule(new DeleteAccountPermissionValidator(currentUser));
		return deleteAccountCommandOperation;
	}

	private static ActionAccountOperation buildUpdateAccountActionOperation(final ActionPhaseParameters app) throws PortalException, SystemException {
		AbstractAccountActionOperation updateAccountCommandOperation = new UpdateAccountOperation(app);
		AccountModelHolderStrategy accountFactory = AccountOperationBuilder.createUpdateAccountBuilder(app.getRequest());
		AccountModelHolder newAccountHolder = accountFactory.buildNewAccountModelHolder();
		updateAccountCommandOperation.setOldAccountModelHolder(accountFactory.buildOldAccountModelHolder());
		updateAccountCommandOperation.setNewAccountModelHolder(newAccountHolder);
		// TODO: customize validators for update operation
		updateAccountCommandOperation.addValidationRules(AccountOperationBuilder.getAddAccountValidators(newAccountHolder));
		
		updateAccountCommandOperation.addActionWorker(new UpdateUserPMReportsIntegration());
		updateAccountCommandOperation.addActionWorker(new UpdateUserExpandoProperties());
		updateAccountCommandOperation.addActionWorker(new UpdatePersonalDetails());
		
		return updateAccountCommandOperation;
	}

	// TODO: add familyValidation for update
	private static ActionAccountOperation buildAddAccountActionOperation(final ActionPhaseParameters app) {
		AbstractAccountActionOperation addAccountCommandOperation = new AddAccountOperation(app);
		AccountModelHolderStrategy accountModelFactory = AccountOperationBuilder.createAddAccountBuilder(app.getRequest());
		AccountModelHolder newAccountModel = accountModelFactory.buildNewAccountModelHolder();
		addAccountCommandOperation.setNewAccountModelHolder(newAccountModel);
		addAccountCommandOperation.addValidationRules(AccountOperationBuilder.getAddAccountValidators(newAccountModel));

		addAccountCommandOperation.addActionWorker(new AddUserPMReportsIntegration());
		addAccountCommandOperation.addActionWorker(new AddUserExpandoPropertiesUpdate());
		addAccountCommandOperation.addActionWorker(new AddUserPhoneNumberUpdate());

		return addAccountCommandOperation;
	}

	private static List<Validator> getAddAccountValidators(final AccountModelHolder newAccountModel) {
		List<Validator> validators = new ArrayList<Validator>();

		DetailsModel detailsModel = newAccountModel.getDetailsModel();
		FreeDaysModel freeDaysModel = newAccountModel.getFreeDaysModel();

		validators.add(new ScreenNameValidator(detailsModel.getScreenName()));
		validators.add(new EmailAddressValidator(detailsModel.getEmailAddress()));
		validators.add(new LastNameValidator(detailsModel.getLastName()));
		validators.add(new FirstNameValidator(detailsModel.getFirstName()));
		validators.add(new LicensePlateValidator(detailsModel.getLicensePlate()));
		validators.add(new PhoneNumberValidator(detailsModel.getPhoneNumber()));
		validators.add(new BirthdayValidator(freeDaysModel.getStartDate(), detailsModel.getBirthdayDate()));
		validators.add(new CNPContentValidator(detailsModel.getCNP()));
		validators.add(new CNPDuplicateValidator(detailsModel.getCNP()));
		validators.add(new FunctieCIMValidator(detailsModel.getFunctieCIM()));
		validators.add(new DateHiredValidator(freeDaysModel.getStartDate()));
		validators.add(new JobTitleValidator(detailsModel.getJobTitle()));
		validators.add(new SiteValidator(newAccountModel.getUserDepartments()));

		return validators;
	}

	private static AccountModelHolderStrategy createUpdateAccountBuilder(ActionRequest request) throws PortalException, SystemException {
		User selectedUser = PortalUtil.getSelectedUser(request);
		AccountModelHolderStrategy accountFactory = new UpdateAccountModelHolderStrategy();
		accountFactory.setNewAccountModelHolderBuilder(new RequestAccountModelHolderBuilder(request));
		accountFactory.setOldAccountModelHolderBuilder(new UserAccountModelHolderBuilder(selectedUser));
		return accountFactory;
	}

	private static AccountModelHolderStrategy createAddAccountBuilder(ActionRequest request) {
		AccountModelHolderStrategy accountFactory = new AddAccountModelHolderStrategy();

		String userType = ParamUtil.getString(request, AccountOperationBuilder.USER_TYPE);
		if (AccountOperationBuilder.INTERNSHIP_USER_TYPE.equals(userType)) {
			accountFactory.setNewAccountModelHolderBuilder(new CreateInternshipAccountModelHolderBuilder(request));

		} else if (AccountOperationBuilder.SCHOLARSHIP_USER_TYPE.equals(userType)) {
			accountFactory.setNewAccountModelHolderBuilder(new CreateScholarshipAccountModelHolderBuilder(request));

		} else {
			accountFactory.setNewAccountModelHolderBuilder(new CreateCIMAccountModelHolderBuilder(request));
		}

		return accountFactory;
	}

}
