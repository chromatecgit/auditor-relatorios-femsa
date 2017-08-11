package model;

import java.math.BigDecimal;

public class ValoresIguaisItem {
	private String index;
	private Long value;
	private BigDecimal dispersion;

	public ValoresIguaisItem() {

	}

	public ValoresIguaisItem(String index, Long value) {
		super();
		this.index = index;
		this.value = value;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public BigDecimal getDispersion() {
		return dispersion;
	}

	public void setDispersion(BigDecimal dispersion) {
		this.dispersion = dispersion;
	}

	@Override
	public String toString() {
		return "ValoresIguaisItem [index=" + index + ", value=" + value + ", dispersion=" + dispersion + "]";
	}

}
