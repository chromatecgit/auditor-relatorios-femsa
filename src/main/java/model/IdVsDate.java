package model;

public class IdVsDate {
	private String id;
	private String date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "IdVsDate [id=" + id + ", date=" + date + "]";
	}

}
