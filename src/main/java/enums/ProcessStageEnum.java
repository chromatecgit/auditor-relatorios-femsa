package enums;

public enum ProcessStageEnum {
	DIMENSIONS(0), PAGING_100(100), FULL(0);
	private int linesToBeRead;

	private ProcessStageEnum(int linesToBeRead) {
		this.linesToBeRead = linesToBeRead;
	}

	public int getLinesToBeRead() {
		return linesToBeRead;
	}

}
