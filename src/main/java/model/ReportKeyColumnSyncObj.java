package model;

public class ReportKeyColumnSyncObj {
	private String oldKey;
	private String newKey;

	public ReportKeyColumnSyncObj() {
		super();
	}

	public ReportKeyColumnSyncObj(String oldKey, String newKey) {
		super();
		this.oldKey = oldKey;
		this.newKey = newKey;
	}

	public String getOldKey() {
		return oldKey;
	}

	public void setOldKey(String oldKey) {
		this.oldKey = oldKey;
	}

	public String getNewKey() {
		return newKey;
	}

	public void setNewKey(String newKey) {
		this.newKey = newKey;
	}
	
	public boolean isEmpty() {
		if ((oldKey != null && !oldKey.isEmpty()) && (newKey != null && !newKey.isEmpty())) {
			return true;
		} else {
			return false;
		}
	}

}