package model;

import java.util.Arrays;

import enums.CompanyEnum;
import enums.ConcorrenciaEnum;
import enums.ConsolidadoTypeEnum;
import enums.ConsumoImediatoEnum;
import enums.POCTypeEnum;
import enums.ProductEnum;
import enums.PropriaEnum;
import enums.TabsEnum;

public class ConsolidadosFilter {
	// SOVI
	private ConsolidadoTypeEnum consolidadoType;
	// Agua
	private TabsEnum[] sheets;
	// Geladeira
	private POCTypeEnum poc;
	// SOVI AGUA FEMSA CRYSTAL CG 500ML PET
	private ProductEnum product;
	// Femsa
	private CompanyEnum company;
	// ci
	private ConsumoImediatoEnum consumoImediato;
	// pp
	private PropriaEnum propria;
	// co
	private ConcorrenciaEnum concorrencia;

	public ConsolidadosFilter() {
		super();
	}

	public ConsolidadosFilter(ConsolidadoTypeEnum consolidadoType, TabsEnum sheet, POCTypeEnum poc, ProductEnum product,
			CompanyEnum company, ConsumoImediatoEnum isConsumoImediato, PropriaEnum isPropria,
			ConcorrenciaEnum isConcorrencia) {
		super();
		this.consolidadoType = consolidadoType;
		this.sheets = new TabsEnum[] { sheet };
		this.poc = poc;
		this.product = product;
		this.company = company;
		this.consumoImediato = isConsumoImediato;
		this.propria = isPropria;
		this.concorrencia = isConcorrencia;
	}

	public ConsolidadosFilter(ConsolidadoTypeEnum consolidadoType, TabsEnum[] sheets, POCTypeEnum poc,
			ProductEnum product, CompanyEnum company, ConsumoImediatoEnum isConsumoImediato, PropriaEnum isPropria,
			ConcorrenciaEnum isConcorrencia) {
		super();
		this.consolidadoType = consolidadoType;
		this.sheets = sheets;
		this.poc = poc;
		this.product = product;
		this.company = company;
		this.consumoImediato = isConsumoImediato;
		this.propria = isPropria;
		this.concorrencia = isConcorrencia;
	}

	public ConsolidadoTypeEnum getConsolidadoType() {
		return consolidadoType;
	}

	public void setConsolidadoType(ConsolidadoTypeEnum consolidadoType) {
		this.consolidadoType = consolidadoType;
	}

	public TabsEnum[] getSheets() {
		return sheets;
	}

	public void setSheets(TabsEnum[] sheets) {
		this.sheets = sheets;
	}

	public POCTypeEnum getPoc() {
		return poc;
	}

	public void setPoc(POCTypeEnum poc) {
		this.poc = poc;
	}

	public ProductEnum getProduct() {
		return product;
	}

	public void setProduct(ProductEnum product) {
		this.product = product;
	}

	public CompanyEnum getCompany() {
		return company;
	}

	public void setCompany(CompanyEnum company) {
		this.company = company;
	}

	public ConsumoImediatoEnum getConsumoImediato() {
		return consumoImediato;
	}

	public void setConsumoImediato(ConsumoImediatoEnum consumoImediato) {
		this.consumoImediato = consumoImediato;
	}

	public PropriaEnum getPropria() {
		return propria;
	}

	public void setPropria(PropriaEnum propria) {
		this.propria = propria;
	}

	public ConcorrenciaEnum getConcorrencia() {
		return concorrencia;
	}

	public void setConcorrencia(ConcorrenciaEnum concorrencia) {
		this.concorrencia = concorrencia;
	}

	@Override
	public String toString() {
		return "ConsolidadosFilter [consolidadoType=" + consolidadoType + ", sheets=" + Arrays.toString(sheets)
				+ ", poc=" + poc + ", product=" + product + ", company=" + company + ", consumoImediato="
				+ consumoImediato + ", propria=" + propria + ", concorrencia=" + concorrencia + "]";
	}

}
