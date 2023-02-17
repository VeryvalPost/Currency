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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Currency {

    public String valuteID;
    public int numCode;
    public String charCode;
    public int nominal;
    public String name;
    public double value;

    public Currency() {
    }

    public Currency(/*String valuteID,*/ int numCode, String charCode, int nominal, String name, double value){
        //this.valuteID = valuteID;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }


    @Override
    public String toString() {
        return "Valute{" +//+"ID='" + valuteID + '\'' +
                ", numCode='" + numCode + '\'' +", " +
                "charCode='" + charCode + '\'' +
                ", nominal='" + nominal + '\'' +
                ", name='" + name+ '\'' +
                ", value='" + value+ '\'' +
                '}';
    }

    public static class Main {

        private static final String REMOTE_SERVICE_URI = "https://cbr.ru/scripts/XML_daily.asp?date_req=";  //
        public static Map<String, Double> currencyTable = new TreeMap<>();


        // парсинг XML с курсами валют. На выходе значение конкретно курса доллара на дату, которую указали на входе.
        public static double getCurr(String date) throws IOException, SAXException, ParserConfigurationException, ParseException {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL(REMOTE_SERVICE_URI+date).openStream());

            List<String> elements = new ArrayList<>();
            List<Currency> list = new ArrayList<>();

            Node root = (doc).getDocumentElement();
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
            return list.get(4).value;
        }


        // преобразование даты из формата LocalDate, в тот который можно парсить
        public static String convertDate(String unconvertedDate) throws ParseException {
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat1.parse(unconvertedDate);

            SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
            String newDateString = dateFormat2.format(date);
            return newDateString;
        }


        // выбор начального и конечного периодов. Основной код программы. На выходе таблица со значениями даты и курса.
        public static void main(String[] args) throws ParseException, IOException, ParserConfigurationException, SAXException {
            // установка начальной и конечной даты
            LocalDate start = LocalDate.parse("2022-01-01"),
                    end   = LocalDate.parse("2023-01-01");
            // выборка всех значений даты
            List<LocalDate> localDateList= Stream.iterate(start, date -> date.plusDays(1))
                    .limit(ChronoUnit.DAYS.between(start, end) + 1)
                    .collect(Collectors.toList());

            // заполнение таблицы значениями даты
            for (int i=0; i<localDateList.size();i++){
                currencyTable.put(convertDate(String.valueOf(localDateList.get(i))),null);
            }

            //заполнение таблицы курсами

            for (Map.Entry<String, Double> entry : currencyTable.entrySet()) {
            entry.setValue(getCurr(entry.getKey()));
            System.out.println(entry);
            }
        }
    }
}
