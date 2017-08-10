package enums;

public enum MessageHierarchyEnum {
	LEVEL_1((short) 1), LEVEL_2((short) 2), LEVEL_3((short) 3), LEVEL_4((short) 4);

	private short level;

	private MessageHierarchyEnum(short level) {
		this.level = level;
	}

	public short getLevel() {
		return level;
	}

}
