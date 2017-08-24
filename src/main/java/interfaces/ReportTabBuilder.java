package interfaces;

import model.ReportCell;
import model.ReportTab;

public interface ReportTabBuilder {
	public ReportTab build();

	public void addCell(final ReportCell cell);

	public void addAndReset();

	public void addNumberOfRows(int rows);

	public void addNumberOfColumns(int columns);
}
