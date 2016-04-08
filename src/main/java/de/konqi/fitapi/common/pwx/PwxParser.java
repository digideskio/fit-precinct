package de.konqi.fitapi.common.pwx;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by konqi on 08.04.2016.
 */
public class PwxParser {
    public PwxFile parse(InputStream is) throws XMLStreamException, NoSuchFieldException, IllegalAccessException {
        PwxFile pwxFile = new PwxFile();
        Summary summary = new Summary();

        List<Sample> samples = new ArrayList<>();

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(is);
        try {
            while (xmlStreamReader.hasNext()) {
                switch (xmlStreamReader.getEventType()) {
                    case XMLStreamConstants.END_DOCUMENT:
                        xmlStreamReader.close();
                        break;

                    case XMLStreamConstants.START_ELEMENT:
                        if (xmlStreamReader.getLocalName().equalsIgnoreCase("sample")) {
                            Sample sample = parseSample(xmlStreamReader);
                            samples.add(sample);
                        } else if (xmlStreamReader.getLocalName().equalsIgnoreCase("segment")) {
                            while (xmlStreamReader.getEventType() != XMLStreamConstants.END_ELEMENT || !xmlStreamReader.getLocalName().equalsIgnoreCase("segment")) {
                                xmlStreamReader.next();
                            }
                        } else if (xmlStreamReader.getLocalName().equalsIgnoreCase("summarydata")) {
                            parseSummary(xmlStreamReader, summary);
                        } else if (xmlStreamReader.getLocalName().equalsIgnoreCase("sportType")) {
                            parseSingle(xmlStreamReader, summary);
                        } else if (xmlStreamReader.getLocalName().equalsIgnoreCase("time")) {
                            parseSingle(xmlStreamReader, summary);
                        }
                        break;

                    default:
                        break;
                }

                xmlStreamReader.next();
            }
        } finally {
            xmlStreamReader.close();
        }

        pwxFile.setSummary(summary);
        pwxFile.setSamples(samples);

        return pwxFile;
    }

    private void parseSingle(XMLStreamReader xmlStreamReader, Summary summary) throws XMLStreamException, NoSuchFieldException, IllegalAccessException {
        int i = 0;
        String type = xmlStreamReader.getLocalName();
        while (xmlStreamReader.hasNext()) {
            switch (xmlStreamReader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    i++;
                    break;

                case XMLStreamConstants.CHARACTERS:
                    summary.set(type, xmlStreamReader.getText());
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    if (--i <= 0) {
                        return;
                    }
                default:
                    break;
            }

            xmlStreamReader.next();
        }
    }

    List<String> minMaxFields = Arrays.asList("hr", "spd");

    private Summary parseSummary(XMLStreamReader xmlStreamReader, Summary summary) throws XMLStreamException, NoSuchFieldException, IllegalAccessException {
        int i = 0;
        String attributeName = null;

        while (xmlStreamReader.hasNext()) {
            switch (xmlStreamReader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    i++;
                    attributeName = xmlStreamReader.getLocalName();
                    if (minMaxFields.contains(attributeName)) {
                        parseMinMaxField(xmlStreamReader, summary);
                        i--;
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    if (!attributeName.equalsIgnoreCase("summarydata")) {
                        summary.set(attributeName, xmlStreamReader.getText());
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    if (--i <= 0) {
                        return summary;
                    }
                default:
                    break;
            }

            xmlStreamReader.next();
        }

        return summary;
    }

    private void parseMinMaxField(XMLStreamReader xmlStreamReader, Summary summary) throws XMLStreamException, NoSuchFieldException, IllegalAccessException {
        String prefix = xmlStreamReader.getLocalName();
        for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {
            String attribName = xmlStreamReader.getAttributeName(i).getLocalPart();
            String attribValue = xmlStreamReader.getAttributeValue(i);

            summary.set(prefix + Character.toUpperCase(attribName.charAt(0)) + attribName.substring(1), attribValue);
        }

        while (xmlStreamReader.hasNext()) {
            if (xmlStreamReader.getEventType() == XMLStreamConstants.END_ELEMENT)
                break;
            xmlStreamReader.next();
        }
    }

    private Sample parseSample(XMLStreamReader xmlStreamReader) throws XMLStreamException, NoSuchFieldException, IllegalAccessException {
        int i = 0;
        Sample sample = new Sample();
        String attributeName = null;

        while (xmlStreamReader.hasNext()) {
            switch (xmlStreamReader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    i++;
                    attributeName = xmlStreamReader.getLocalName();
                    break;

                case XMLStreamConstants.CHARACTERS:
                    if (!attributeName.equalsIgnoreCase("sample")) {
                        sample.set(attributeName, xmlStreamReader.getText());
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    if (--i <= 0) {
                        return sample;
                    }
                default:
                    break;
            }

            xmlStreamReader.next();
        }

        return sample;
    }
}
