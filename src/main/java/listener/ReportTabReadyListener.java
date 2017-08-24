package listener;

import interfaces.ReportTabBuilder;

public interface ReportTabReadyListener {
	void onArrivalOf(ReportTabBuilder tabBuilder);
}
