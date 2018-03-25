package business.service;

public class XMLExportService extends ExportService {

    public Exporter getExporter() {
        return new XMLExporter();
    }
}
