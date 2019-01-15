package com.revolut.testtask.controller;


import java.util.List;
import java.util.Optional;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.revolut.testtask.exception.DAOException;
import com.revolut.testtask.exception.DuplicateAccountException;
import com.revolut.testtask.exception.InsufficientBalanceException;
import com.revolut.testtask.exception.InvalidParamException;
import com.revolut.testtask.model.Account;
import com.revolut.testtask.model.dto.ResultDto;
import com.revolut.testtask.service.AccountService;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountRestController {
	
    AccountService accountController = AccountService.getInstance();
    
    private static Logger logger = Logger.getLogger(AccountRestController.class);

    /**
	 * Get all existing accounts
	 * @return Response containing resultDto object,if everything is OK then httpStatus will be 200,otherwise internal server error with appropriate error message. 
	 */
    @GET
    @Path("/getall")
    public Response getAllAccounts() {
    	List<Account> result = null;
		try {
			result = accountController.listAccounts();
			logger.trace(String.format("getAllAccounts Returning result,size:%d",result.size()));
	        return Response.ok(new ResultDto<List<Account>>(result)).build();
		} catch (DAOException e) {
			logger.trace(String.format("getAllAccounts failed:%s",e.getMessage()));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ResultDto<Account>(e.getMessage())).build();
		}
		
    }
    /**
	 * get an account by it's id
	 * @param accountId the account id to fetch
	 * @return Response containing resultDto object,if everything is OK then httpStatus will be 200 and data filed will contain an account,otherwise bad request or internal server error with appropriate error message.
	 */
    @GET
    @Path("/{accountId}")
    public Response getAccountById(@PathParam("accountId") long accountId) {
        try {
			Optional<Account> accountOptional = accountController.getAccountById(accountId);
			if(accountOptional.isPresent()) {
		        return Response.ok(new ResultDto<Account>(accountOptional.get())).build();
			}else {
				return Response.status(Status.BAD_REQUEST).entity(new ResultDto<Account>("No account found")).build();
			}
		} catch (InvalidParamException e) {
			logger.trace(String.format("getAccountById failed:%s",e.getMessage()));
			return Response.status(Status.BAD_REQUEST).entity(new ResultDto<Account>(e.getMessage())).build();
		}catch (DAOException e) {
			logger.trace(String.format("getAccountById failed:%s",e.getMessage()));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ResultDto<Account>(e.getMessage())).build();
		} 
    }
    
    /**
	 * Add an account to current accounts list
	 * @param account the Account object
	 * @return Response containing resultDto object,if everything is OK then httpStatus will be 200 and data filed will contain an account,otherwise bad request or internal server error with appropriate error message.
	 */
    @PUT
    @Path("/add")
    public Response saveAccount(Account account) {
        try {
			account = accountController.saveAccount(account);
	        return Response.ok(new ResultDto<Account>(account)).build();
		} catch (DuplicateAccountException | InvalidParamException e) {
			logger.trace(String.format("saveAccount failed:%s",e.getMessage()));
			return Response.status(Status.BAD_REQUEST).entity(new ResultDto<Account>(e.getMessage())).build();
		}  catch (DAOException e) {
			logger.trace(String.format("saveAccount failed:%s",e.getMessage()));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ResultDto<Account>(e.getMessage())).build();
		}
    }
    
    /**
	 * Remove an account from accounts list
	 * @param accountId accountId to remove
	 * @return Response containing resultDto object,if everything is OK then httpStatus will be 200 and resultDto.success is true,otherwise bad request or internal server error with appropriate error message.
	 */
    @DELETE
    @Path("/{accountId}")
    public Response deleteAccountById(@PathParam("accountId") long accountId) {
        try {
			accountController.deleteAccount(accountId);
			ResultDto<String> resultDto = new ResultDto<>();
			resultDto.setSuccess(true);
	        return Response.ok(resultDto).build();
		} catch (InvalidParamException e) {
			logger.trace(String.format("deleteAccountById failed:%s",e.getMessage()));
			return Response.status(Status.BAD_REQUEST).entity(new ResultDto<Account>(e.getMessage())).build();
		}catch (DAOException e) {
			logger.trace(String.format("deleteAccountById failed:%s",e.getMessage()));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ResultDto<Account>(e.getMessage())).build();
		} 
    }
    
    /**
	 * Transfer money from an account to another
	 * @param sourceAccountId Source account id
	 * @param destinationAccountId Destination account id
	 * @param amount Amount to withdraw from source account and deposit to destination account
	 * @return Response containing resultDto object,if everything is OK then httpStatus will be 200 and resultDto.success is true,otherwise bad request or internal server error with appropriate error message.
	 */
    @POST
    @Path("/transfer/{sourceAccountId}/{destinationAccountId}/{amount}")
    public Response transferMoney(@PathParam("sourceAccountId") long sourceAccountId,@PathParam("destinationAccountId") long destinationAccountId,@PathParam("amount") long amount) {
        try {
			accountController.transferMoney(sourceAccountId, destinationAccountId, amount);
			ResultDto<String> resultDto = new ResultDto<>();
			resultDto.setSuccess(true);
	        return Response.ok(resultDto).build();
		} catch (InvalidParamException e) {
			logger.trace(String.format("transferMoney failed:%s",e.getMessage()));
			return Response.status(Status.BAD_REQUEST).entity(new ResultDto<Account>(e.getMessage())).build();
		}catch (InsufficientBalanceException e) {
			logger.trace(String.format("transferMoney failed:%s",e.getMessage()));
			return Response.status(Status.NOT_ACCEPTABLE).entity(new ResultDto<Account>(e.getMessage())).build();
		}catch (DAOException e) {
			logger.trace(String.format("transferMoney failed:%s",e.getMessage()));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ResultDto<Account>(e.getMessage())).build();
		} 
    }
}
