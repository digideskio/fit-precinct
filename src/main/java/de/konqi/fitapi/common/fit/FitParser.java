package de.konqi.fitapi.common.fit;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by konqi on 22.04.2016.
 */
@Slf4j
public class FitParser {
    Map<Integer, FitDefinition> definitions = new HashMap<>();

    public FitHeader parseHeader(InputStream is) throws IOException {
        FitHeader header = new FitHeader();

        byte[] buffer;
        int read;
        // read header size
        buffer = new byte[1];
        read = is.read(buffer);
        if (read < buffer.length) throw new IOException("Unexpected end of file.");
        header.setSize(buffer[0]);
        // read protocol version
        read = is.read(buffer);
        if (read < buffer.length) throw new IOException("Unexpected end of file.");
        header.setProtocolVersion(buffer[0]);
        // read profile version
        buffer = new byte[2];
        read = is.read(buffer);
        if (read < buffer.length) throw new IOException("Unexpected end of file.");
        header.setProfileVersion(ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getChar());
        // read data size
        buffer = new byte[4];
        read = is.read(buffer);
        if (read < buffer.length) throw new IOException("Unexpected end of file.");
        header.setDataSize(ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt());
        // read data type
        read = is.read(buffer);
        if (read < buffer.length) throw new IOException("Unexpected end of file.");
        header.setDataType(new String(buffer));
        // read crc
        buffer = new byte[2];
        read = is.read(buffer);
        if (read < buffer.length) throw new IOException("Unexpected end of file.");
        header.setCrc(ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getShort());

        return header;
    }

    private long bytesRead = 0;

    public FitData readRecords(InputStream is, long bytes) throws IOException {
        FitData fitData = new FitData();

        while (bytesRead < bytes) {
            byte[] buffer = new byte[1];
            int read = is.read(buffer);
            if (read < buffer.length) throw new IOException("Unexpected end of file.");
            bytesRead += read;

            boolean compressedTimestamp = (buffer[0] & 0x80) > 0;
            if (compressedTimestamp) {
                System.out.println("compressed timestamp.");
            } else {
                int localMessageType = buffer[0] & 0xF;

                if ((buffer[0] & 0x40) > 0) {
                    // definition message
                    FitDefinition definition = readDefinition(is);
                    // System.out.println("localMessageType " + localMessageType + " -> " + definition.toString());
                    definitions.put(localMessageType, definition);
                } else {
                    // normal message
                    // System.out.println("data message, type: " + localMessageType + ".");

                    // get definition
                    FitDefinition fitDefinition = definitions.get(localMessageType);
                    Map<Enum, Object> values = new LinkedHashMap<>(fitDefinition.getFieldDefinitions().size());
                    Object timestamp = null;

                    for (FitDefinition.FieldDefinition fieldDefinition : fitDefinition.getFieldDefinitions()) {
                        buffer = new byte[fieldDefinition.getSize()];
                        read = is.read(buffer);
                        if (read < buffer.length) throw new IOException("Unexpected end of file.");
                        bytesRead += read;

                        Object value;
                        switch (fieldDefinition.getBaseType()) {
                            case UINT32:
                                value = fitDefinition.toUInt(buffer);
                                break;
                            case SINT32:
                                value = fitDefinition.toInt(buffer);
                                break;
                            case UINT16:
                                value = fitDefinition.toUShort(buffer);
                                break;
                            case SINT16:
                                value = fitDefinition.toShort(buffer);
                                break;
                            case ENUM:
                            case UINT8:
                                value = buffer[0] & 0xFF;
                                break;
                            case STRING:
                                value = new String(buffer);
                                break;
                            default:
                                value = null;
                        }

                        if (value != null) {
                            switch (fitDefinition.getGlobalMessageNumber()) {
                                case FILE_ID:
                                    // file header information?
                                    fitData.getHeader().put(fieldDefinition.getFieldDefinition(), value);
                                    break;
                                case EVENT:
                                case RECORD:
                                    // data
                                    if (fieldDefinition.getFieldDefinition().name().equalsIgnoreCase("TIMESTAMP")) {
                                        timestamp = value;
                                    } else {
                                        values.put(fieldDefinition.getFieldDefinition(), value);
                                    }
                                    break;
                                case SESSION:
                                default:
                                    values.put(fieldDefinition.getFieldDefinition(), value);
                                    break;
                            }
                        }
                    }

                    if (values.size() > 0) {
                        if (timestamp != null) {
                            if (!fitData.getData().containsKey(fitDefinition.getGlobalMessageNumber())) {
                                fitData.getData().put(fitDefinition.getGlobalMessageNumber(), new LinkedHashMap<Object, Map<Enum, Object>>());
                            }

                            fitData.getData().get(fitDefinition.getGlobalMessageNumber()).put(timestamp, values);
                        } else {
                            switch (fitDefinition.getGlobalMessageNumber()){
                                case LAP:
                                    fitData.getLaps().add(values);
                                    break;
                                default:
                                    fitData.getMeta().put(fitDefinition.getGlobalMessageNumber(), values);
                            }
                        }
                    }
                }
            }
        }

        return fitData;
    }

    private FitDefinition readDefinition(InputStream is) throws IOException {
        byte[] buffer;
        int read;
        FitDefinition fitDefinition = new FitDefinition();

        // read reserved
        buffer = new byte[1];
        read = is.read(buffer);
        if (read < buffer.length) throw new IOException("Unexpected end of file.");
        bytesRead += read;
        fitDefinition.setReserved(buffer[0]);
        // read architecture
        read = is.read(buffer);
        if (read < buffer.length) throw new IOException("Unexpected end of file.");
        bytesRead += read;
        fitDefinition.setByteOrder(buffer[0] == 0 ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
        // read global message number
        buffer = new byte[2];
        read = is.read(buffer);
        if (read < buffer.length) throw new IOException("Unexpected end of file.");
        bytesRead += read;
        fitDefinition.setGlobalMessageNumber(FitGlobalMessageNum.get(fitDefinition.toUShort(buffer)));
        // read field count
        buffer = new byte[1];
        read = is.read(buffer);
        if (read < buffer.length) throw new IOException("Unexpected end of file.");
        bytesRead += read;

        int fieldCount = buffer[0];
        for (int i = 0; i < fieldCount; i++) {
            buffer = new byte[3];
            read = is.read(buffer);
            if (read < buffer.length) throw new IOException("Unexpected end of file.");
            bytesRead += read;

            FitDefinition.FieldDefinition fieldDefinition = new FitDefinition.FieldDefinition();
            fieldDefinition.setFieldDefinition(fitDefinition.getGlobalMessageNumber().getSub(buffer[0] & 0xFF));

            fieldDefinition.setSize(buffer[1]);
            fieldDefinition.setMultibyte((buffer[2] & 0x80) > 0);
            fieldDefinition.setBaseType(FitBaseType.get(buffer[2] & 0xF));

            fitDefinition.getFieldDefinitions().add(fieldDefinition);
        }

        return fitDefinition;
    }

}
