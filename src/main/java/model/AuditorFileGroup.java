package model;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class AuditorFileGroup {
	
	private List<AuditorFile> auditorFiles;

	public List<String> obtainKeys() {
		return this.auditorFiles.stream().map(e -> e.getKey()).collect(Collectors.toList());
	}

	public List<InputStream> obtainValues() {
		return this.auditorFiles.stream().map(e -> e.getData()).collect(Collectors.toList());
	}

	public String obtainKeyBy(final InputStream data) {
		return this.auditorFiles.stream().filter(e -> e.getData().equals(data)).findFirst().orElse(new AuditorFile())
				.getKey();
	}

	public InputStream obtainValueBy(final String key) {
		return this.auditorFiles.stream().filter(e -> e.getKey().equals(key)).findFirst().orElse(new AuditorFile())
				.getData();
	}

	public InputStream obtainValueBy(final InputStream data) {
		return this.auditorFiles.stream().filter(e -> e.getData().equals(data)).findFirst().orElse(new AuditorFile())
				.getData();
	}

	public AuditorFile obtainFileByKey(final String key) {
		return this.auditorFiles.stream().filter(e -> e.getKey().equals(key)).findFirst().orElse(new AuditorFile());
	}

	public Long obtainContentSize() {
		return this.auditorFiles.stream().count();
	}

	public List<AuditorFile> getAuditorFiles() {
		return auditorFiles;
	}

	public void setAuditorFiles(List<AuditorFile> auditorFiles) {
		this.auditorFiles = auditorFiles;
	}

}
