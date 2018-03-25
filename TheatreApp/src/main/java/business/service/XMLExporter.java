package business.service;

import business.model.Show;
import business.model.Ticket;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class XMLExporter implements Exporter {

    private final String EXPORT_PATH = "/exports/xml/";

    public boolean export(List<Ticket> tickets, String filename) {
        try {
            Show show = tickets.get(0).getShow();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root element (show)
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("show");
            doc.appendChild(rootElement);

            rootElement.setAttribute("id", Integer.toString(show.getId()));

            Element dateEl = doc.createElement("date");
            dateEl.appendChild(doc.createTextNode(show.getDate().toString()));
            rootElement.appendChild(dateEl);

            Element ticketsListEl = doc.createElement("tickets");
            rootElement.appendChild(ticketsListEl);

            for (Ticket ticket: tickets) {
                // ticket elements
                Element ticketEl = doc.createElement("ticket");
                ticketsListEl.appendChild(ticketEl);
                ticketEl.setAttribute("id", Integer.toString(ticket.getId()));

                Element rowEl = doc.createElement("row");
                rowEl.appendChild(doc.createTextNode(Integer.toString(ticket.getSeat().getRowNr())));
                ticketEl.appendChild(rowEl);

                Element seatEl = doc.createElement("seat");
                seatEl.appendChild(doc.createTextNode(Integer.toString(ticket.getSeat().getSeatNr())));
                ticketEl.appendChild(seatEl);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            String filepath = System.getProperty("user.dir") + EXPORT_PATH + filename + ".xml";
            System.out.println(filepath);
            File file = new File(filepath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);

            System.out.println("File saved!");

            return true;

        } catch (IOException e) {
            System.out.println("Error creating file!");
            e.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
        return false;
    }
}
