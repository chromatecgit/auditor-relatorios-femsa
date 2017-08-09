package model;

import java.util.ArrayList;
import java.util.List;

import enums.ExcelFileNameEnum;
import utils.FileRepository;

public class Module {
	protected List<AuditorFile> files = new ArrayList<>();
	
	protected List<AuditorFile> fetchFiles(ExcelFileNameEnum[] files) {
		return this.files.add(this.getFilesFromRepository(files));
	}
	
	private AuditorFile getFileFromRepository(String key) {
		AuditorFile obtainFileMapWith = FileRepository.obtainFileMapWith(key);
		return ;
	}
	
	private AuditorFile getFilesFromRepository(String[] keys) {
		AuditorFile obtainFileMapWith = FileRepository.obtainFileMapWith(key);
		return ;
	}
}
