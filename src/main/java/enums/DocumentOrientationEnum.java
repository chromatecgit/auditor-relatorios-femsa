package enums;

public enum DocumentOrientationEnum {
	HORIZONTAL("H"),
	VERTICAL("V");

	private String orientation;

	DocumentOrientationEnum(String orientation) {
		this.orientation = orientation;
	}

	public String getOrientation() {
		return orientation;
	}

}
