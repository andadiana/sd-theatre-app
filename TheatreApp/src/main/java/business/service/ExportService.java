package business.service;

import business.model.Show;
import business.model.Ticket;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public abstract class ExportService {

    public boolean exportSoldTickets(Show show) {
        Exporter exporter = getExporter();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timeStr = dateFormat.format(currentTime);
        String filename = show.getTitle() + "_" + timeStr;

        ServiceProvider serviceProvider = new ServiceProviderImpl();
        TicketService ticketService = serviceProvider.getTicketService();
        List<Ticket> ticketList = ticketService.findSoldTicketsForShow(show);
        return exporter.export(ticketList, filename);
    }

    public abstract Exporter getExporter();
}
