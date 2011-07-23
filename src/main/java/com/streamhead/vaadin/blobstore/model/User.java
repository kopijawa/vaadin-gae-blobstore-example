package com.streamhead.vaadin.blobstore.model;

import java.io.Serializable;

import javax.persistence.Id;

public class User implements Serializable {

	@Id Long id;
	String name;
	String uploadKey;
	String blobKey;
	
	private User() { }
	
	public User(String name) {
		super();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBlobKey() {
		return blobKey;
	}
	public void setBlobKey(String blobKey) {
		this.blobKey = blobKey;
	}
	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (blobKey == null) {
			if (other.blobKey != null)
				return false;
		} else if (!blobKey.equals(other.blobKey))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (uploadKey == null) {
			if (other.uploadKey != null)
				return false;
		} else if (!uploadKey.equals(other.uploadKey))
			return false;
		return true;
	}

	public String generateUploadKey() {
		this.uploadKey = String.valueOf(Math.round(Math.random() * 100000f)); // you probably want something more robust here
		return uploadKey;
	}
}
