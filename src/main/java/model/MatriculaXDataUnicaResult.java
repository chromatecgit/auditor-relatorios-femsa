package model;

import java.util.ArrayList;
import java.util.List;

public class MatriculaXDataUnicaResult {
	private String fileName;
	private List<IdVsDate> idVsDateList;

	public MatriculaXDataUnicaResult() {
		super();
		this.idVsDateList = new ArrayList<>();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<IdVsDate> getIdVsDateList() {
		return idVsDateList;
	}

	public void setIdVsDateList(List<IdVsDate> idVsDateList) {
		this.idVsDateList = idVsDateList;
	}

	@Override
	public String toString() {
		return "MatriculaXDataUnicaResult [fileName=" + fileName + ", idVsDateList=" + idVsDateList + "]";
	}

}
