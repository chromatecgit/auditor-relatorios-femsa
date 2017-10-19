package builders;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import interfaces.ReportTabBuilder;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportTab;
import utils.MyLogPrinter;

// FIXME: REFACTOR
// Transformar os metodos de addCell e addAndReset em leitores de linhas inteira
// usando os tableHeaders como parametros. Fazer o filtro das colunas desejadas ANTES
// e usar o endereco de cada uma para gravar somente os valores desejados tambem.
// Procurar NAO usar nome de coluna SOVI ou PRECO para qualquer verificacao
// pois pode quebrar facilmente em algum modulo futuro.
// OBS.: A metodo de merge das abas esta colocando Soja junto de Sucos!
public class ReportVerticalTabBuilder implements ReportTabBuilder {

	private ReportTab tab;
	private Map<String, String> tableHeaders;
	private Map<String, ReportCell> cells;
	private String[] excludeThese;
	private String[] columnsToBeIndexed;
	private int lastLine;

	public ReportVerticalTabBuilder(final String[] excludeThese, final String[] columnsToBeIndexed) {
		this.tab = new ReportTab();
		this.tab.setCells(new TreeMap<ReportCellKey, ReportCell>());
		this.tableHeaders = new LinkedHashMap<>();
		this.excludeThese = excludeThese;
		this.columnsToBeIndexed = columnsToBeIndexed;
	}

	@Override
	public void addCell(final ReportCell cell) {
		if (cell.getLineIndex() == 1) {
			this.tableHeaders.put(cell.getValue(), cell.getColumnIndex());
			this.lastLine = 1;
		} else if (this.lastLine == cell.getLineIndex()) {
			this.cells.put(cell.getColumnIndex(), cell);
		} else if (this.lastLine != cell.getLineIndex()) {
			this.lastLine = cell.getLineIndex();
			this.addAndReset(cell);
		} else {
			MyLogPrinter.addToBuiltMessage("Valor invalido em :" + cell.getAddress());
		}
	}

	@Override
	public void addAndReset(final ReportCell cell) {
		if (this.cells.isEmpty()) {
			this.cells.put(cell.getColumnIndex(), cell);
		} else {
			ReportCellKey cellKey = this.buildLineKey();
		}
	}
	//Colocar na interface;
	@Override
	public ReportCellKey buildLineKey() {
		ReportCellKey cellKey = new ReportCellKey();
		for (String columnName :  this.columnsToBeIndexed) {
			String columnIndex = this.tableHeaders.get(columnName);
			
		}
		
		return null;
	}
	
	@Override
	public ReportTab build() {
		MyLogPrinter.printBuiltMessage("ReportVerticalTabBuilder_orphan_cells");
		return this.tab;
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
