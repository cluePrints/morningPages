package com.mpages.domain;

import java.math.BigDecimal;
import java.util.Collection;

public interface IKit extends IGift {
	public Collection<IGift> getContents();
	public BigDecimal getPrice();
}
