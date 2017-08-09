package utils;

import config.ProjectConfiguration;

public class GlobBuilder {
	
	/**
	 * Metodo que constroi o pattern para o glob
	 * 
	 * @return
	 */
	public static String buildGlobPattern() {
		StringBuffer sb = new StringBuffer();
		sb.append("**");
		
		/*  Whitelist	*/
		if (ProjectConfiguration.whitelistFolders.length > 0) {
			sb.append("/{");
			for (int i = 0; i < ProjectConfiguration.whitelistFolders.length; i++) {
				sb.append(ProjectConfiguration.whitelistFolders[i]);
				if (i != ProjectConfiguration.whitelistFolders.length - 1) {
					sb.append(",");
				}
			}
			sb.append("}**");
		}
		
		/*  Endossos 	*/
		if (ProjectConfiguration.endossos.length > 0) {
			sb.append("/{");
			for (int i = 0; i < ProjectConfiguration.endossos.length; i++ ) {
				sb.append("endosso ").append(ProjectConfiguration.endossos[i]);
				if (i != ProjectConfiguration.endossos.length - 1) {
					sb.append(",");
				}
			}
			sb.append("}**");
		} 
		
		/*  File names 	*/
		if (ProjectConfiguration.fileNames.length > 0) {
			if (ProjectConfiguration.endossos.length <= 0) {
				sb.append("/*[0-9]_{");
			} else {
				sb.append("/*[a-z,A-Z]_{");
			}
			
			for (int i = 0; i < ProjectConfiguration.fileNames.length; i++) {
				sb.append(ProjectConfiguration.fileNames[i]).append(".");
				if (i != ProjectConfiguration.fileNames.length - 1) {
					sb.append(",");
				}
			}
			sb.append("}");
		}
		
		/*  File types	*/
//		if (ProjectConfiguration.fileTypes.length > 0) {
//			sb.append("{");
//			for (int i = 0; i < ProjectConfiguration.fileTypes.length; i++) {
//				sb.append(ProjectConfiguration.fileTypes[i]);
//				if (i != ProjectConfiguration.fileTypes.length - 1) {
//					sb.append(",");
//				}
//			}
//			sb.append("}");
//		}
		
		return sb.toString();
	}
}
