package utils;


public class CallCounter {
	public static long hTabCounter, hTabCounter_tgetRows, parseReportRowPrecoInfos, parseReportRowPrecoInfos_keycolumn, id_parallel, key_parallel, freeRows_parallel;
	
	public static void printResults() {
		System.out.println("hTabCounter" + hTabCounter);
		System.out.println("hTabCounter_tgetRows" + hTabCounter_tgetRows);
		System.out.println("parseReportRowPrecoInfos" + parseReportRowPrecoInfos);
		System.out.println("parseReportRowPrecoInfos_keycolumn" + parseReportRowPrecoInfos_keycolumn);
		System.out.println("id_parallel" + id_parallel);
		System.out.println("key_parallel" + key_parallel);
		System.out.println("freeRows_parallel" + freeRows_parallel);
	}
}
