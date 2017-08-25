package interfaces;

import model.ReportCell;
import model.ReportTab;

public interface ReportTabBuilder {
	
	public ReportTab build();
	
	public void addDocumentName(final String fileName);

	public void addCell(final ReportCell cell);

	public void addAndReset();

	public void addNumberOfRows(final int rows);

	public void addNumberOfColumns(final int columns);
}
