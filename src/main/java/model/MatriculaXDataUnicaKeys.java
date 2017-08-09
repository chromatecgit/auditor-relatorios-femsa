package model;

public class MatriculaXDataUnicaKeys {
	private String fileName;
	private String id;
	private String date;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

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
		return "MatriculaXDataUnicaKeys [fileName=" + fileName + ", id=" + id + ", date=" + date + "]";
	}

}
