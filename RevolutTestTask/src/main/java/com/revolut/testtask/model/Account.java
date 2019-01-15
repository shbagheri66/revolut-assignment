package com.revolut.testtask.model;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.revolut.testtask.exception.InsufficientBalanceException;

public class Account {
	
	public Account() {
	}
	
	public Account(Long id,String ownerName, Long balance) {
		this.id = id;
		this.ownerName = ownerName;
		this.balance = balance;
	}
	
	public Account(String ownerName, Long balance) {
		this.ownerName = ownerName;
		this.balance = balance;
	}
	
	@JsonProperty(required = true)
	private long id;
	
	@JsonProperty(required = true)
    private String ownerName;
	
	@JsonProperty(required = true)
    private Long balance;

	@JsonProperty(required = false)
    private Date creationDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Long getBalance() {
		return balance;
	}

	private void setBalance(Long balance) {
		this.balance = balance;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void deposit(long amount) {
		synchronized (this.balance) {
			setBalance(getBalance()+amount);
		}
	}
	
	public void withdraw(long amount) throws InsufficientBalanceException {
		synchronized (this.balance) {
			if(this.balance>= amount)
				setBalance(getBalance()-amount);
			else
				throw new InsufficientBalanceException(String.format("Account %d,Current balance:%d less than %d",this.getId(), this.getBalance(),amount));
		}
	}
}
