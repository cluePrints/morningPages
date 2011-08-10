package com.mpages.domain;

import java.math.BigDecimal;
import java.util.Collection;

public class SimpleKit implements IKit, IEntity{
	private int id;
	private String name;
	private String locale;
	private Collection<IGift> contents;
	private BigDecimal price;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public Collection<IGift> getContents() {
		return contents;
	}
	public void setContents(Collection<IGift> contents) {
		this.contents = contents;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
