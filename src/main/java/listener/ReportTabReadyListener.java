package listener;

import utils.ReportTabBuilder;

public interface ReportTabReadyListener {
	void onArrivalOf(ReportTabBuilder tabBuilder);
}
