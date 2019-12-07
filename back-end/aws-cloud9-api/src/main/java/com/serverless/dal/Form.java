package com.serverless.dal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Form {
	
	 private String id;
	 private String name;
	 private String creationDate;
	 private List<Map<String,String>> users;
	 //private Map<String,String> users[];
	 
	 
	 public Form() {
		super();
		this.id = null;
		this.name = null;
		this.creationDate = null;
		this.users = new ArrayList<Map<String,String>>();
		//this.users = null;
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
	    	Map<String, String> map = new HashMap<String,String>();
	    	map.put("userID",userID);
	    	map.put("recruitedID", recruiterID);
	    	this.users.add(map);
	    }
	    public void deleteUsers(String userID) {
	    	for(int i = 0; i<users.size(); i++) {
	    		
	    		if(users.get(i).get("userID").equals(userID)) {
	    			users.remove(i);
	    			break;
	    		}
	    	}
	    }
	    
	    public boolean findUser(String userID) {
	    	System.out.println(userID);
	    	System.out.println(users.size());
	    	System.out.println(this.users.size());
	    	for(int i = 0; i<this.users.size(); i++) {
	    		System.out.println(this.users.get(i).get("userID"));
	    		if(this.users.get(i).get("userID").equals(userID)) {
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	    public void setUsers(List<Map<String,String>> users) {
	    	this.users = users;
	    }
	    public List<Map<String,String>> getUsers(){
	    	return new ArrayList<Map<String,String>>(this.users);
	    	//return this.users;
	    }
	    
	    /*public Map<String,String>[] getUsers(){
	    	return this.users;
	    }*/
	    
	    @Override
		public String toString() {
			return "Form [getId()=" + getId() + ", getName()=" + getName() + ", getCreationDate()=" + getCreationDate() + "]";
		}

}
