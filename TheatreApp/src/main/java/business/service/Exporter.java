package business.service;

import business.model.Ticket;

import java.util.List;

public interface Exporter {

    public boolean export(List<Ticket> tickets, String filename);
}
