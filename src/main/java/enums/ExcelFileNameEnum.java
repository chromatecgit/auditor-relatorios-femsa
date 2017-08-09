package enums;

import java.util.ArrayList;
import java.util.List;

public enum ExcelFileNameEnum {
	SOVI,
	SOVI_VERTICAL,
	PRECO,
	PRECO_VERTICAL,
	CONSOLIDADA,
	CONSOLIDADA_SOVI,
	PRODUTIVIDADE;
	
	public String[] getAllAsStrings() {
		List<String> stringVersion = new ArrayList<>();
		for (ExcelFileNameEnum item : ExcelFileNameEnum.values()) {
			stringVersion.add(item.toString());		
		}
		return stringVersion.toArray(new String[0]);
	}
}
