package model;

public class KeyColumnsMap {
	private String name;
	private int index;
	
	public KeyColumnsMap() {
		super();
	}

	public KeyColumnsMap(String name, int index) {
		super();
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "KeyColumnsMap [name=" + name + ", index=" + index + "]";
	}
	
}
