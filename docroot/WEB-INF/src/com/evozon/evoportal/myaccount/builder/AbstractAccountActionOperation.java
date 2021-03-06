package com.evozon.evoportal.myaccount.builder;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.evozon.evoportal.my_account.AccountModelHolder;
import com.evozon.evoportal.myaccount.worker.ActionWorker;
import com.liferay.compat.portal.util.PortalUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.model.User;

public abstract class AbstractAccountActionOperation implements ActionAccountOperation {

	private static Log logger = LogFactoryUtil.getLog(AbstractAccountActionOperation.class);

	protected final List<Validator> validationRules = new ArrayList<Validator>();

	protected final List<ActionWorker> actionWorkers = new ArrayList<ActionWorker>();

	private final ActionPhaseParameters actionPhaseParameters;

	protected AccountModelHolder newAccountModelHolder;

	protected AccountModelHolder oldAccountModelHolder;

	public AbstractAccountActionOperation(final ActionPhaseParameters pp) {
		this.actionPhaseParameters = pp;
	}

	public void execute() throws Exception {
		// TODO: add general behavior before executing the actual action
		executeDefaultLiferayProcess();
		if (SessionErrors.isEmpty(getActionRequest())) {
			executeInternalAction();
		}
	}

	public ValidationResult isActionValid() {
		ValidationResult validationResult = new ActionValidationResult();

		for (Validator validator : validationRules) {
			ValidationResult res = validator.validate();

			if (res.hasMessages()) {
				validationResult.addMessages(res.getValidationMessages());
			}

			if (res.hasErrors()) {
				validationResult.addErrors(res.getValidationErrors());

				if (logger.isDebugEnabled()) {
					logger.debug("Validation failure [" + validator + "].");
				}
			}
		}

		return validationResult;
	}

	protected void executeInternalAction() {
		for (ActionWorker worker : actionWorkers) {
			try {
				worker.execute(this);
			} catch (Exception e) {
				logger.error("Worker " + worker + " has failed for " + getClass().getSimpleName(), e);
			}
		}
	}

	public void addValidationRule(Validator validator) {
		this.validationRules.add(validator);
	}

	public void addValidationRules(List<Validator> validators) {
		if ((validators != null) && !validators.isEmpty()) {
			this.validationRules.addAll(validators);
		}
	}

	public void addActionWorker(ActionWorker actionWorker) {
		this.actionWorkers.add(actionWorker);
	}

	public void addActionWorkers(List<ActionWorker> actionWorkers) {
		if ((actionWorkers != null) && !actionWorkers.isEmpty()) {
			this.actionWorkers.addAll(actionWorkers);
		}
	}

	protected void executeDefaultLiferayProcess() throws Exception {
		ActionAccountOperation defaultActionOp = new DefaultAccountActionOperation(actionPhaseParameters);
		defaultActionOp.execute();
	}

	protected ActionRequest getActionRequest() {
		return actionPhaseParameters.getRequest();
	}

	protected ActionResponse getActionResponse() {
		return actionPhaseParameters.getResponse();
	}

	public Long getCompanyId() {
		return PortalUtil.getCompanyId(getActionRequest());
	}

	public void setNewAccountModelHolder(AccountModelHolder newAccountHolder) {
		this.newAccountModelHolder = newAccountHolder;
	}

	public AccountModelHolder getNewAccountModelHolder() {
		return newAccountModelHolder;
	}

	public void setOldAccountModelHolder(AccountModelHolder oldAccountHolder) {
		this.oldAccountModelHolder = oldAccountHolder;
	}

	public AccountModelHolder getOldAccountModelHolder(AccountModelHolder oldAccountHolder) {
		return oldAccountModelHolder;
	}

	public User getCurrentUser() throws PortalException, SystemException {
		return PortalUtil.getUser(getActionRequest());
	}

	public User getSelectedUser() throws PortalException, SystemException {
		return PortalUtil.getSelectedUser(getActionRequest());
	}

}
