package model;

public class ReportSymmetryResults {
	private ReportCellKey key;
	private ReportCell cell;

	public ReportSymmetryResults(ReportCellKey key, ReportCell cell) {
		super();
		this.key = key;
		this.cell = cell;
	}

	public ReportCellKey getKey() {
		return key;
	}

	public void setKey(ReportCellKey key) {
		this.key = key;
	}

	public ReportCell getCell() {
		return cell;
	}

	public void setCell(ReportCell cell) {
		this.cell = cell;
	}

}
