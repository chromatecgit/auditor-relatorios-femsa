package model;

import java.util.ArrayList;
import java.util.List;

public class SoviConsolidadaCellAppendix {
	private List<String> pocs;
	private List<String> skus;

	public SoviConsolidadaCellAppendix() {
		super();
		this.pocs = new ArrayList<>();
		this.skus = new ArrayList<>();
	}

	public List<String> getPocs() {
		return pocs;
	}

	public void setPocs(List<String> pocs) {
		this.pocs = pocs;
	}

	public List<String> getSkus() {
		return skus;
	}

	public void setSkus(List<String> skus) {
		this.skus = skus;
	}

	@Override
	public String toString() {
		return "SoviConsolidadaCellAppendix [pocs=" + pocs + ", skus=" + skus + "]";
	}

}
