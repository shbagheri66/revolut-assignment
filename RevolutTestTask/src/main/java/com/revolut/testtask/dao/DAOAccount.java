package com.revolut.testtask.dao;

import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.revolut.testtask.exception.DAOException;
import com.revolut.testtask.exception.DuplicateAccountException;
import com.revolut.testtask.exception.InvalidParamException;
import com.revolut.testtask.model.Account;

public class DAOAccount {

	private static DAOAccount instance;
	private Map<Long, Account> accountsMap = new HashMap<>();
	
	private DAOAccount() {
	}
	
	/**
	 * get all existing accounts
	 * @return An array list containing all accounts sorted by creationDate
	 * @throws DAOException 
	 */
	public List<Account> getAllAccounts() throws DAOException {
		try {
			List<Account> result = accountsMap.values().stream()
	                .collect(Collectors.toList());
			result.sort(Comparator.comparing(Account::getCreationDate));
			return result;
		} catch (Exception e) {
			throw new DAOException("Fetch list of accounts failed", e);
		}
	}
	
	/**
	 * get an account by it's id
	 * @param accountId the account id to fetch
	 * @return if account id exists,the related account otherwise nothing
	 * @throws DAOException 
	 */
	public Optional<Account> getAccountById(long accountId) throws DAOException {
		try {
			if(accountsMap.containsKey(accountId))
				return Optional.of(accountsMap.get(accountId));
			else
				return Optional.empty();
		} catch (Exception e) {
			throw new DAOException("Get accountById failed", e);
		}
	}
	
	/**
	 * Add an account to current accounts list
	 * @param account the Account object
	 * @return Account object after save if the account does not exist
	 * @throws DAOException
	 * @throws DuplicateAccountException if account already exists
	 */
	public Account saveAccount(Account account) throws DAOException,DuplicateAccountException {
		try {
			synchronized (accountsMap) {
				if(accountsMap.containsKey(account.getId())) {
					throw new DuplicateAccountException("Account already exist with id:"+account.getId());
				}else {
					account.setCreationDate(new Date());
					accountsMap.put(account.getId(), account);
					return account;
				}
			}
		} catch (DuplicateAccountException e) {
			throw e;
		}catch (Exception e) {
			throw new DAOException("Account creation failed", e);
		} 
	}
	
	/**
	 * Remove an account from accounts list
	 * @param accountId accountId to remove
	 * @return Account object after save if the account does not exist
	 * @throws DAOException
	 * @throws InvalidParameterException if account does not exist
	 */
	public void deleteAccount(long accountId) throws DAOException,InvalidParamException {
		try {
			synchronized (accountsMap) {
				if(accountsMap.containsKey(accountId)) {
					accountsMap.remove(accountId);
				}else {
					throw new InvalidParamException("Account does not exist with id:"+accountId);
				}
			}
		} catch (InvalidParamException e) {
			throw e;
		} catch (Exception e) {
			throw new DAOException("Remove account failed", e);
		} 
	}
	/**
	 * Returning an instance of this class
	 */
	public static DAOAccount getInstance() {
	    if (instance == null) {
	      synchronized (DAOAccount.class){
	        if (instance == null) {
	          instance = new DAOAccount();
	        }
	      }
	    }
	    return instance ;
	}
}

