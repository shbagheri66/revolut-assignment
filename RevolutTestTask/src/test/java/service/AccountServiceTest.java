package service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.revolut.testtask.exception.DAOException;
import com.revolut.testtask.exception.DuplicateAccountException;
import com.revolut.testtask.exception.InsufficientBalanceException;
import com.revolut.testtask.exception.InvalidParamException;
import com.revolut.testtask.model.Account;
import com.revolut.testtask.service.AccountService;

public class AccountServiceTest {
	AccountService accountService = AccountService.getInstance();
	
	@Test
	public void testListAccounts() throws DAOException {
		assertTrue(accountService.listAccounts().size()>=0);
	}
	
	@Test(expected=InvalidParamException.class)
	public void testGetAccountWithInvalidAccountId() throws DAOException,InvalidParamException {
		accountService.getAccountById(0);
	}
	
	@Test
	public void testGetAccountNoSuchAccount() throws DAOException,InvalidParamException {
		assertFalse(accountService.getAccountById(1000).isPresent());
	}
	
	@Test(expected=InvalidParamException.class)
	public void testSaveAccountInvalidId() throws DAOException,DuplicateAccountException,InvalidParamException {
		Account account = new Account("Shahab",2000l);
		accountService.saveAccount(account);
	}
	
	@Test(expected=InvalidParamException.class)
	public void testSaveAccountInvalidOwnerName() throws DAOException,DuplicateAccountException,InvalidParamException {
		Account account = new Account(1000l,null,2000l);
		accountService.saveAccount(account);
	}
	
	@Test(expected=InvalidParamException.class)
	public void testSaveAccountInvalidBalance() throws DAOException,DuplicateAccountException,InvalidParamException {
		Account account = new Account(1000l,"Shahab",null);
		accountService.saveAccount(account);
	}
	
	@Test(expected=InvalidParamException.class)
	public void testSaveAccountInvalidBalance2() throws DAOException,DuplicateAccountException,InvalidParamException {
		Account account = new Account(1000l,"Shahab",-1l);
		accountService.saveAccount(account);
	}
	
	@Test
	public void testSaveAccount() throws DAOException,DuplicateAccountException,InvalidParamException {
		Account account = new Account(1000l,"Shahab",2000l);
		Account savedAccount = accountService.saveAccount(account);
		assertNotNull(savedAccount.getCreationDate());
	}
	
	@Test(expected=DuplicateAccountException.class)
	public void testSaveDuplicateAccount() throws DAOException,DuplicateAccountException,InvalidParamException {
		Account account = new Account(1000l,"Shahab",2000l);
		accountService.saveAccount(account);
		Account account2 = new Account(1000l,"John",3000l);
		accountService.saveAccount(account2);
	}
	
	@Test(expected=InvalidParamException.class)
	public void testDeleteAccountInvalidId() throws DAOException,InvalidParamException {
		accountService.deleteAccount(0);
	}
	
	@Test(expected=InvalidParamException.class)
	public void testDeleteAccountNoSuchId() throws DAOException,InvalidParamException {
		accountService.deleteAccount(2233);
	}
	
	@Test
	public void testDeleteAccount() throws DAOException,InvalidParamException, DuplicateAccountException {
		Account account = new Account(1022l,"Shahab",2000l);
		accountService.saveAccount(account);
		accountService.deleteAccount(account.getId());
	}
	
	@Test(expected=InvalidParamException.class)
	public void testTransferMoneyInvalidSourceAccountId() throws DAOException,InvalidParamException, InsufficientBalanceException {
		accountService.transferMoney(0, 1000, 1000);
	}
	
	@Test(expected=InvalidParamException.class)
	public void testTransferMoneyInvalidDestinationAccountId() throws DAOException,InvalidParamException, InsufficientBalanceException {
		accountService.transferMoney(1000, 0, 1000);
	}
	
	@Test(expected=InvalidParamException.class)
	public void testTransferMoneyInvalidSourceAccountNotFound() throws DAOException,InvalidParamException, InsufficientBalanceException {
		accountService.transferMoney(2000, 1000, 1000);
	}
	
	@Test(expected=InvalidParamException.class)
	public void testTransferMoneyInvalidDestinationAccountNotFound() throws DAOException,InvalidParamException, InsufficientBalanceException {
		accountService.transferMoney(1000, 2000, 1000);
	}
	
	@Test(expected=InsufficientBalanceException.class)
	public void testTransferMoneyInsufficientBalance() throws DAOException,InvalidParamException, InsufficientBalanceException, DuplicateAccountException {
		Account account1 = new Account(3000l,"Shahab",2000l);
		Account account2 = new Account(3001l,"Sara",4000l);
		accountService.saveAccount(account1);
		accountService.saveAccount(account2);
		accountService.transferMoney(3000, 3001, 2001);
	}
	
	@Test
	public void testTransferMoney() throws DAOException,InvalidParamException, InsufficientBalanceException, DuplicateAccountException {
		Account account1 = new Account(3002l,"Amir",2000l);
		Account account2 = new Account(3003l,"Farhad",4000l);
		accountService.saveAccount(account1);
		accountService.saveAccount(account2);
		accountService.transferMoney(3002, 3003, 2000);
		assertTrue(account1.getBalance()==0 && account2.getBalance()==6000);
	}
	
	@Test
	public void testTransferMoneyInCuncurrentTime() throws DAOException,InvalidParamException, InsufficientBalanceException, DuplicateAccountException, InterruptedException {
		Account account1 = new Account(3005l,"Amir",2000l);
		Account account2 = new Account(3006l,"Farhad",4000l);
		Account account3 = new Account(3007l,"Ashkan",6000l);
		accountService.saveAccount(account1);
		accountService.saveAccount(account2);
		accountService.saveAccount(account3);

		Runnable task1 = () -> { try {
			accountService.transferMoney(3005, 3006, 2000);
		} catch (DAOException | InsufficientBalanceException | InvalidParamException e) {} };
		
		Runnable task2 = () -> { try {
			accountService.transferMoney(3006, 3007, 4000);
		} catch (DAOException | InsufficientBalanceException | InvalidParamException e) {} };

		Runnable task3 = () -> { try {
			accountService.transferMoney(3007, 3005, 6000);
		} catch (DAOException | InsufficientBalanceException | InvalidParamException e) {} };
		
		new Thread(task1).start();
		new Thread(task2).start();
		new Thread(task3).start();
		//wating for all threads to do their job
		Thread.sleep(1000);
		
		assertTrue(account1.getBalance()==6000 && account2.getBalance()==2000 && account3.getBalance()==4000);
	}
	
}
