package business.service;

public class CSVExportService extends ExportService {

    public Exporter getExporter() {
        return new CSVExporter();
    }
}
