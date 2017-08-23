package utils;


public class CallCounter {
	public static long hTabCounter, hTabCounter_tgetRows, parseReportRowPrecoInfos, parseReportRowPrecoInfos_keycolumn, id_parallel, key_parallel, freeRows_parallel;
	private static long start;
	
	public static void printResults() {
		System.out.println("hTabCounter" + hTabCounter);
		System.out.println("hTabCounter_tgetRows" + hTabCounter_tgetRows);
		System.out.println("parseReportRowPrecoInfos" + parseReportRowPrecoInfos);
		System.out.println("parseReportRowPrecoInfos_keycolumn" + parseReportRowPrecoInfos_keycolumn);
		System.out.println("id_parallel" + id_parallel);
		System.out.println("key_parallel" + key_parallel);
		System.out.println("freeRows_parallel" + freeRows_parallel);
	}
	
	public static void start() {
		start = System.currentTimeMillis();
	}
	
	public static void stop() {
		long finish = System.currentTimeMillis();
		long longResult = finish - start;
		long secondsResult = longResult / 1000;
		System.out.println("\tTempo de execucao: " + secondsResult + " segundos");
		start = 0L;
	}
}
