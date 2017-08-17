package config;

import java.util.List;

public class GlobBuilder {
	
	/**
	 * Metodo que constroi o pattern para o glob
	 * @return
	 */
	public static String buildGlobPatternWith(final List<String> fileNames) {
		StringBuffer sb = new StringBuffer();
		sb.append("glob:**");
		
		/*  File names 	*/
		if (!fileNames.isEmpty()) {
			if (!fileNames.contains("*")) {
				sb.append("/{");
				for (int i = 0; i < fileNames.size(); i++) {
					sb.append(fileNames.get(i));
					if (i != fileNames.size() - 1) {
						sb.append(",");
					}
				}
				sb.append("}_[0-9]*.");
			} else {
				sb.append("/*");
			}
		}
		
		if (ProjectConfiguration.fileTypes.length > 0) {
			sb.append("{");
			for (int i = 0; i < ProjectConfiguration.fileTypes.length; i++) {
				sb.append(ProjectConfiguration.fileTypes[i]);
				if (i != ProjectConfiguration.fileTypes.length - 1) {
					sb.append(",");
				}
			}
			sb.append("}");
		}
		
		return sb.toString();
	}
}
