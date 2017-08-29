package model;

public class ReportCellTripleKey extends ReportCellKey {
	private String poc;
	
	public ReportCellTripleKey() {
		super();
	}

	public ReportCellTripleKey(String concat, String columnName) {
		super(concat, columnName);
	}

	public ReportCellTripleKey(String concat, String columnName, String poc) {
		super(concat, columnName);
		this.poc = poc;
	}
	
	
	
}
