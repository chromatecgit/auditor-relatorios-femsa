package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import enums.CompanyEnum;
import enums.POCTypeEnum;
import enums.ProductEnum;
import enums.TabsEnum;

public class HVMap implements Comparable<HVMap> {
	protected String id;
	protected List<HVMapInfos> mapInfos;

	public HVMap() {
		super();
		this.mapInfos = new ArrayList<>();
	}

	public HVMap(String id, List<HVMapInfos> mapInfos) {
		super();
		this.id = id;
		this.mapInfos = mapInfos;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<HVMapInfos> getMapInfos() {
		return mapInfos;
	}

	public void setMapInfos(List<HVMapInfos> mapInfos) {
		this.mapInfos = mapInfos;
	}

	@Override
	public String toString() {
		return "\n\tHVMapByID [id=" + id + ", mapInfos=" + mapInfos + "]";
	}

	public List<HVMapInfos> findMapsBySKU(final String sku) {
		return this.mapInfos.stream().filter(infos -> sku.equals(infos.getSku())).collect(Collectors.toList());
	}

	@Override
	public int compareTo(HVMap map) {
		return this.id.compareTo(map.getId());
	}
	
	//TODO: Fazer uma especializacao no metodo que busca os SKUs, porque a Consolidada não tem
	public List<HVMapInfos> consolidateSoviBySKU() {
		Map<String, Integer> skuSoviMap = this.mapInfos.stream().collect(
				Collectors.groupingBy(HVMapInfos::getSku, Collectors.reducing(0, HVMapInfos::getSovi, Integer::sum)));

		return this.parseMapToInfoMap(skuSoviMap);
	}
	//TODO: Fazer uma especializacao no metodo que busca os SKUs, porque a Consolidada não tem
	public List<HVMapInfos> consolidatePrecoBySKU() {
		Map<String, Double> skuPrecoMap = this.mapInfos.stream().collect(
				Collectors.groupingBy(HVMapInfos::getSku, Collectors.reducing(0.0, HVMapInfos::getPreco, Double::sum)));

		return this.parseDoubleMapToInfoMap(skuPrecoMap);
	}
	
	private List<HVMapInfos> findHVMapInfosByTabName(String tabName) {
		return this.mapInfos.stream().filter(e -> e.getTabName().equals(tabName)).collect(Collectors.toList());
	}
	

	private List<HVMapInfos> parseMapToInfoMap(Map<String, Integer> skuSoviMap) {
		List<HVMapInfos> hvMapInfosList = new ArrayList<>();

		for (String key : skuSoviMap.keySet()) {
			HVMapInfos hvMapInfos = new HVMapInfos();
			hvMapInfos.setSku(key);
			hvMapInfos.setSovi(skuSoviMap.get(key));
			hvMapInfosList.add(hvMapInfos);
		}

		return hvMapInfosList;
	}
	
	private List<HVMapInfos> parseDoubleMapToInfoMap(Map<String, Double> skuPrecoMap) {
		List<HVMapInfos> hvMapInfosList = new ArrayList<>();

		for (String key : skuPrecoMap.keySet()) {
			HVMapInfos hvMapInfos = new HVMapInfos();
			hvMapInfos.setSku(key);
			hvMapInfos.setPreco(skuPrecoMap.get(key));
			hvMapInfosList.add(hvMapInfos);
		}

		return hvMapInfosList;
	}
	//TODO: Fazer uma especializacao no metodo que busca os SKUs, porque a Consolidada não tem
	public List<String> getAllSKUs() {
		List<String> result = this.mapInfos.stream().map(e -> e.getSku()).collect(Collectors.toList());
		Collections.sort(result);
		return result;
	}
	
	public List<HVMapInfos> applyConsolidadaFilter(ConsolidadosFilter consolidadosFilter) {
		//System.out.println("FILTRO: " + consolidadosFilter);
		List<HVMapInfos> selected = new ArrayList<>();
		//FILTRO DE ABA
		for (TabsEnum tabEnum : consolidadosFilter.getSheets()) {
			selected.addAll(this.findHVMapInfosByTabName(tabEnum.getTabName()));
		}
		//FILTRO DE POC
		if (!consolidadosFilter.getPoc().equals(POCTypeEnum.ALL)) {
			selected = this.filterHVMapInfosByPOC(selected, 
										consolidadosFilter.getPoc(),
										consolidadosFilter.getPropria().isPropria(),
										consolidadosFilter.getConcorrencia().isConcorrencia());
		}
		//FILTRO DE PRODUTO
		if (!consolidadosFilter.getProduct().equals(ProductEnum.ALL)) {
			selected = this.filterHVMapInfosByProduct(selected, consolidadosFilter.getProduct());
		}
		
		//FILTRO DE CIA
		if (!consolidadosFilter.getCompany().equals(CompanyEnum.ALL)) {
			selected = this.filterHVMapInfosByCompany(selected, consolidadosFilter.getCompany());
		}
		
		// FILTRO DE CI
		if (consolidadosFilter.getConsumoImediato().isCI()) {
			selected = this.filterHVMapInfosByCI(selected);
		}
		//System.out.println("SELECIONADOS: " + selected);
		return selected;
	}

	private List<HVMapInfos> filterHVMapInfosByCI(List<HVMapInfos> list) {
		List<HVMapInfos> selected = new ArrayList<>();
		for (HVMapInfos infos: list) {
			if (isCI(infos.getSku())) {
				selected.add(infos);
			}
		}
		return selected;
	}
	
	private boolean isCI(String sku) {
		String[] skuPieces = sku.split(" ");
		for (String piece : skuPieces) {
			if (piece.matches("^\\d+(\\.\\d+)?M?L")) {
				if (piece.contains("M")) {
					return true;
				} else {
					double result = Double.valueOf(piece.replace("M", "").replaceAll("L", ""));
					return result >= 1 ? false : true;
				}
			}
		}
		return false;
	}

	private List<HVMapInfos> filterHVMapInfosByCompany(List<HVMapInfos> list, CompanyEnum company) {
		List<HVMapInfos> selected = new ArrayList<>();
		for (HVMapInfos infos: list) {
			if (infos.getSku().contains(company.getCia())) {
				selected.add(infos);
			}
		}
		return selected;
	}

	private List<HVMapInfos> filterHVMapInfosByProduct(List<HVMapInfos> list, ProductEnum productEnum) {
		List<HVMapInfos> selected = new ArrayList<>();
		for (HVMapInfos infos: list) {
			for (String synonym : productEnum.getSynonyms()) {
				if (infos.getSku().contains(synonym)) {
					selected.add(infos);
				}
			}
			
		}
		return selected;
	}

	private List<HVMapInfos> filterHVMapInfosByPOC(final List<HVMapInfos> list, POCTypeEnum poc, boolean onlyPropria,
			boolean onlyConcorrencia) {
		List<HVMapInfos> selected = new ArrayList<>();
		for (HVMapInfos infos : list) {
			if (infos.getPoc().contains(poc.getCompleteName())) {
				if (onlyConcorrencia == true && onlyPropria == true) {
					if (infos.getPoc().contains("Proprio") || infos.getPoc().contains("Concorrencia")) {
						selected.add(infos);
					}
				} else {
					selected.add(infos);
				}
			}
		}
		
		return selected;
	}


}
