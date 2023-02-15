package testproj;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


public class Main {

    private static final String REMOTE_SERVICE_URI = "https://cbr.ru/scripts/XML_daily.asp?date_req=";  //

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss z");
        String formattedDate = "02/03/2002";

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL(REMOTE_SERVICE_URI+formattedDate).openStream());

        List<String> elements = new ArrayList<>();
        List<Currency> list = new ArrayList<>();

        Node root = ((org.w3c.dom.Document) doc).getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("Valute")) {
                NodeList nodeList1 = node.getChildNodes();
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node node_ = nodeList1.item(j);
                    if (Node.ELEMENT_NODE == node_.getNodeType()) {
                        elements.add(node_.getTextContent());
                    }
                }
                list.add(new Currency(
                        //elements.get(0),
                        Integer.parseInt(elements.get(0)),
                        elements.get(1),
                        Integer.parseInt(elements.get(2)),
                        elements.get(3),
                        Double.parseDouble(elements.get(4).replace(",","."))));
                elements.clear();
            }
        }
        System.out.println(list.get(4).value);
    }
}
