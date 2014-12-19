package de.gwt.hardworking.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String name;
	@Persistent
	private String duration;
	@Persistent
	private String date;
	@Persistent
	private String comment;
	@Persistent
	private String doer;
	@Persistent
	private boolean confirmed;
	@Persistent
	private String entryDate;

	public Task() {
	}

	public Task(String name, String duration, String doer,
			String date, String comment, boolean confirmed, String entryDate) {

		
		this.name = name;
		this.duration = duration;
		this.doer = doer;
		this.date = date;
		this.comment = comment;
		this.confirmed = confirmed;
		this.entryDate = entryDate;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getname() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDoer() {
		return doer;
	}

	public void setDoer(String doer) {
		this.doer = doer;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String creationDate) {
		this.entryDate = creationDate;
	}

	public String getShortName() {

		if (getname().contains("@")) {
			String[] split = getname().split("@");
			return split[0];
		}
		return "";
	}

}