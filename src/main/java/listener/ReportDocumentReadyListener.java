package listener;

import model.ReportDocument;

public interface ReportDocumentReadyListener {
	void onArrivalOf(ReportDocument document);
}
