package interfaces;

import model.ReportCell;
import model.ReportCellKey;
import model.ReportTab;

public interface ReportTabBuilder {
	
	public ReportTab build();
	
	public void addDocumentName(final String fileName);
	
	public void addTabName(final String tabName);

	public void addCell(final ReportCell cell);

	public void addAndReset(final ReportCell cell);

	public void addNumberOfRows(final int rows);

	public void addNumberOfColumns(final int columns);

	public <K extends ReportCellKey> K buildLineKey(final K k);
	
}
