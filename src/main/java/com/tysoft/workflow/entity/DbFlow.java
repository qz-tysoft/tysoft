package com.tysoft.workflow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "uflo_dbflow")
public class DbFlow {
	@Id
	@Column(name = "ID", length = 60)
	private String id;
	@Column(name = "filename")
	private String filename;
	
	@Type(type="text") 
	@Column(name = "content")
	private String content;
	
	@Column(name = "sdatetime", length = 20)
	private Long sdatetime;
	
	
	public Long getSdatetime() {
		return sdatetime;
	}
	public void setSdatetime(Long sdatetime) {
		this.sdatetime = sdatetime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
