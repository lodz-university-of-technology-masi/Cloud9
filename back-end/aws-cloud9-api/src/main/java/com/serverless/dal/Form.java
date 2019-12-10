package com.serverless.dal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Form {
	
	 private String id;
	 private String name;
	 private String creationDate;
	 private Map<String,String> users;
	 
	 
	 public Form() {
		super();
		this.id = null;
		this.name = null;
		this.creationDate = null;
		this.users = new HashMap<String,String>();
	}
	 	@DynamoDBHashKey(attributeName = "id")
	    @DynamoDBAutoGeneratedKey
	    public String getId() {
	        return this.id;
	    }
	    public void setId(String id) {
	        this.id = id;
	    }

	    @DynamoDBRangeKey(attributeName = "name")
	    public String getName() {
	        return this.name;
	    }
	    
	    public void setName(String name) {
	        this.name = name;
	    }
	    
	    @DynamoDBAttribute(attributeName = "creationDate")
	    public String getCreationDate() {
	        return this.creationDate;
	    }
	    
	    public void setCreationDate(String creationDate) {
	        this.creationDate = creationDate;
	    }
	    
	    @DynamoDBAttribute(attributeName = "users")
	    public void addUsers(String userID, String recruiterID) {
	    	users.put(userID,recruiterID);
	    }
	    
	    public boolean deleteUsers(String userID) {
	    	if(users.remove(userID) == null) {
	    		return false;
	    	}
	    	else
	    		return true;
	    }
	    
	    public boolean findUser(String userID) {
	    	return users.containsKey(userID);
	    }
	    
	    public void setUsers(Map<String,String> users) {
	    	this.users = users;
	    }
	    
	    public Map<String,String> getUsers(){
	    	return new HashMap<String,String>(this.users);
	    }

	    
	    @Override
		public String toString() {
			return "Form [getId()=" + getId() + ", getName()=" + getName() + ", getCreationDate()=" + getCreationDate() + "]";
		}

}
