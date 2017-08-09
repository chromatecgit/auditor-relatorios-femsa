package model;

import java.io.InputStream;

public class AuditorFile {
	private String key;
	private InputStream data;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public InputStream getData() {
		return data;
	}

	public void setData(InputStream data) {
		this.data = data;
	}

}
