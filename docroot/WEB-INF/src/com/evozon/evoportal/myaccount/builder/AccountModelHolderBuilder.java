package com.evozon.evoportal.myaccount.builder;

import com.evozon.evoportal.my_account.AccountModelHolder;

public interface AccountModelHolderBuilder {

	public AccountModelHolderBuilder buildFreeDaysModel();

	public AccountModelHolderBuilder buildDetailsModel();

	public AccountModelHolderBuilder buildAddresses();

	public AccountModelHolderBuilder buildUserFamily();

	public AccountModelHolderBuilder buildUserDepartments();

	public AccountModelHolder build();
}
