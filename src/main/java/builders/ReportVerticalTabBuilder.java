package builders;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.math.NumberUtils;

import interfaces.ReportTabBuilder;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportTab;
import utils.MyLogPrinter;
import utils.ReportTabBuilderIndexVO;

// FIXME: REFACTOR
// Transformar os metodos de addCell e addAndReset em leitores de linhas inteira
// usando os tableHeaders como parametros. Fazer o filtro das colunas desejadas ANTES
// e usar o endereco de cada uma para gravar somente os valores desejados tambem.
// Procurar NAO usar nome de coluna SOVI ou PRECO para qualquer verificacao
// pois pode quebrar facilmente em algum modulo futuro.
// OBS.: A metodo de merge das abas esta colocando Soja junto de Sucos!
public class ReportVerticalTabBuilder implements ReportTabBuilder {

	private ReportTab tab;
	// Column Letter Index X Column Name
	private Map<String, String> tableHeaders;
	private String[] filters;
	// Concat X Line Index
	private ReportTabBuilderIndexVO indexVO;
	private String currentSKU;
	private String currentPoc;

	public ReportVerticalTabBuilder(final String[] filters) {
		this.tab = new ReportTab();
		this.tab.setCells(new TreeMap<ReportCellKey, ReportCell>());
		this.tableHeaders = new LinkedHashMap<>();
		this.currentSKU = "";
		this.indexVO = new ReportTabBuilderIndexVO();
		this.filters = filters;
		this.currentPoc = "";
	}

	@Override
	public ReportTab build() {
		MyLogPrinter.printBuiltMessage("ReportVerticalTabBuilder_orphan_cells");
		return this.tab;
	}

	@Override
	public void addCell(ReportCell cell) {
		if (cell.getLineIndex() == 1) {
			this.tableHeaders.put(cell.getColumnIndex(), cell.getValue());
		} else {
			String cellValue = this.tableHeaders.get(cell.getColumnIndex());
			if (cellValue != null) {
				if (cellValue.equalsIgnoreCase("CONCAT")) {
					indexVO.setConcat(cell.getValue());
					indexVO.setLineIndex(cell.getLineIndex());
					this.currentSKU = "";
				} else if (cellValue.equals("PRODUTO")) {
					this.currentSKU = cell.getValue();
				} else if (cellValue.equals("POC")) {
					this.currentPoc = cell.getValue();
				} else if (cell.getLineIndex() == indexVO.getLineIndex()) {
					if (!this.isInFilter(cellValue)) {
						this.addAndReset(cell, cellValue);
					}
				} else {
					MyLogPrinter.addToBuiltMessage("Valor inválido em :" + cell.getAddress());
				}
			}
		}
	}

	private boolean isInFilter(String cellValue) {
		if (filters != null) {
			for (String filter : filters) {
				if (cellValue.contains(filter))
					return true;
			}
		}
		return false;
	}

	@Override
	public void addAndReset(ReportCell cell, String correspondingHeader) {
		ReportCellKey cellKey = new ReportCellKey();
		cellKey.setConcat(this.indexVO.getConcat());
		//TODO: Melhorar isso aqui
		if (correspondingHeader.equals("SOVI") || correspondingHeader.equals("PRECO")) {
			cellKey.setColumnName(this.currentSKU);
		} else {
			cellKey.setColumnName(correspondingHeader);
		}
		
		if (correspondingHeader.equalsIgnoreCase("SOVI")) {
			cell.getPocInfos().merge(this.currentPoc, Integer.parseInt(cell.getValue()), (ov, nv) -> {
				return nv + ov;
			});
			this.currentPoc = "";
		}
		
		this.tab.getCells().merge(cellKey, cell, (oc, nc) -> {
			if (cellKey.getColumnName().startsWith("SOVI")) {
				//Somente realizar essa operacao com base na coluna de produtos, que comecam com SOVI
				oc.getPocInfos().forEach((k,v) -> {
					nc.getPocInfos().merge(k, v , (x, y) -> {
						return x +y;
					});
				});
				if (NumberUtils.isCreatable(nc.getValue())) {
					Integer sum = NumberUtils.toInt(oc.getValue()) + (NumberUtils.toInt(nc.getValue()));
					nc.setValue(sum.toString());
				} else {
					nc.setValue(oc.getValue());
				}
			}
			return nc;
		});
		
	}

	@Override
	public void addNumberOfRows(int rows) {
		this.tab.setNumberOfRows(rows);
	}

	@Override
	public void addNumberOfColumns(int columns) {
		this.tab.setNumberOfColumns(columns);
	}

	@Override
	public void addDocumentName(String fileName) {
		this.tab.setFileName(fileName);
	}

	@Override
	public void addTabName(String tabName) {
		this.tab.setTabName(tabName);
	}

}
