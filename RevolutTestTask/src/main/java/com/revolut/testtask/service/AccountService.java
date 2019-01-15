package com.revolut.testtask.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.revolut.testtask.dao.DAOAccount;
import com.revolut.testtask.exception.DAOException;
import com.revolut.testtask.exception.DuplicateAccountException;
import com.revolut.testtask.exception.InsufficientBalanceException;
import com.revolut.testtask.exception.InvalidParamException;
import com.revolut.testtask.model.Account;

public class AccountService {
	private static AccountService instance;
	
	DAOAccount daoAccount = DAOAccount.getInstance();
    
    private static Logger logger = Logger.getLogger(AccountService.class);
	
	private AccountService() {
	}
	/**
	 * Get all existing accounts
	 * @return An array list containing all accounts sorted by creationDate
	 * @throws DAOException 
	 */
	public List<Account> listAccounts() throws DAOException {
		List<Account> result = daoAccount.getAllAccounts();
		logger.trace(String.format("Returning accounts,Size:%d", result.size()));
		return result;
	}
	/**
	 * get an account by it's id
	 * @param accountId the account id to fetch
	 * @return if account id exists,the related account otherwise nothing
	 * @throws DAOException 
	 * @throws InvalidParamException if the acountId is not valid 
	 */
	public Optional<Account> getAccountById(long accountId) throws DAOException,InvalidParamException {
		logger.trace(String.format("getAccount,id:%d", accountId));
		if(accountId<=0) {
			throw new InvalidParamException("Input is null");
		}else {
			return daoAccount.getAccountById(accountId);
		}
	}
	/**
	 * Add an account to current accounts list
	 * @param account the Account object
	 * @return Account object after save if the account does not exist
	 * @throws DAOException
	 * @throws DuplicateAccountException if account already exists
	 * @throws InvalidParamException if the input parameters are invalid
	 */
	public Account saveAccount(Account account) throws DAOException,DuplicateAccountException,InvalidParamException{
		if(account==null) {
			throw new InvalidParamException("Input is null");
		}else if(account.getId()<=0) {
			throw new InvalidParamException("Invalid accountId");
		}else if(account.getOwnerName()==null || "".equals(account.getOwnerName())) {
			throw new InvalidParamException("Invalid ownerName");
		}else if(account.getBalance()==null || account.getBalance()<0) {
			throw new InvalidParamException("Invalid balance");
		}else {
			logger.trace(String.format("saveAccount, id:%d , ownerName:%s , balance:%d", account.getId(),account.getOwnerName(),account.getBalance()));
			return daoAccount.saveAccount(account);
		}
	}
	/**
	 * Remove an account from accounts list
	 * @param accountId accountId to remove
	 * @return Account object after save if the account does not exist
	 * @throws DAOException
	 * @throws InvalidParameterException if account does not exist
	 * @throws InvalidParamException if accountId is invalid
	 */
	public void deleteAccount(long accountId) throws DAOException,InvalidParamException{
		logger.trace(String.format("delete account,id:%d", accountId));
		if(accountId<=0) {
			throw new InvalidParamException("Invalid accountId");
		}else {
			daoAccount.deleteAccount(accountId);
		}
	}
	/**
	 * Transfer money from an account to another
	 * @param sourceAccountId Source account id
	 * @param destinationAccountId Destination account id
	 * @param amount Amount to withdraw from source account and deposit to destination account
	 * @throws DAOException
	 * @throws InsufficientBalanceException if the source account's balance is not enough to transfer
	 * @throws InvalidParamException if the input parameters are invalid
	 */
	public void transferMoney(long sourceAccountId,long destinationAccountId, long amount) throws DAOException,InsufficientBalanceException,InvalidParamException {
		logger.trace(String.format("Transfer money, source:%d , destination:%d , amount:%d", sourceAccountId,destinationAccountId,amount));
		if(sourceAccountId<=0) {
			throw new InvalidParamException("Invalid sourceAccountId:"+sourceAccountId);
		}else if(destinationAccountId<=0) {
			throw new InvalidParamException("Invalid destinationAccountId:"+destinationAccountId);
		}else if(amount<=0) {
			throw new InvalidParamException("Invalid amount");
		}else {
			Optional<Account> sourceAccountOpt = daoAccount.getAccountById(sourceAccountId);
			Optional<Account> destAccountOpt = daoAccount.getAccountById(destinationAccountId);
			if(!sourceAccountOpt.isPresent()) {
				throw new InvalidParamException("Source account not found:"+sourceAccountId);
			}else if(!destAccountOpt.isPresent()){
				throw new InvalidParamException("Destination account not found:"+destinationAccountId);
			}else {
				sourceAccountOpt.get().withdraw(amount);
				destAccountOpt.get().deposit(amount);
			}
		}
	}
	/**
	 * Returning an instance of this class
	 */
	public static AccountService getInstance() {
	    if (instance == null) {
	      synchronized (AccountService.class){
	        if (instance == null) {
	          instance = new AccountService();
	        }
	      }
	    }
	    return instance ;
	}
	
	
}
