package business.service;

import business.model.Ticket;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter implements Exporter {

    private final String TICKET_HEADER = "id, show, date, row, seat\n";
    private final String COMMA_SEPARATOR = ", ";
    private final String NEWLINE = "\n";
    private final String EXPORT_PATH = "/exports/csv/";

    public boolean export(List<Ticket> tickets, String filename) {
        FileWriter fileWriter = null;
        try {
            String filepath = System.getProperty("user.dir") + EXPORT_PATH + filename + ".csv";
            System.out.println(filepath);
            File file = new File(filepath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            fileWriter = new FileWriter(file.getAbsoluteFile());

            fileWriter.append(TICKET_HEADER);
            for (Ticket ticket : tickets) {
                fileWriter.append(Integer.toString(ticket.getId()));
                fileWriter.append(COMMA_SEPARATOR);
                fileWriter.append(ticket.getShow().getTitle());
                fileWriter.append(COMMA_SEPARATOR);
                fileWriter.append(ticket.getShow().getDate().toString());
                fileWriter.append(COMMA_SEPARATOR);
                fileWriter.append(Integer.toString(ticket.getSeat().getRowNr()));
                fileWriter.append(COMMA_SEPARATOR);
                fileWriter.append(Integer.toString(ticket.getSeat().getSeatNr()));
                fileWriter.append(NEWLINE);
            }

            System.out.println("CSV file created successfully!");
            return true;

        } catch (IOException e) {
            System.out.println("Error in CSV FileWriter!");
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.out.println("Error while flushing/closing FileWriter!");
                e.printStackTrace();

            }

        }
        return false;
    }
}
