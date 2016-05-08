package de.konqi.fitapi.common.fit;

import lombok.Data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by konqi on 22.04.2016.
 */
@Data
public class FitDefinition {
    @Data
    public static class FieldDefinition {
        Enum fieldDefinition;
        int size;
        FitBaseType baseType;
        boolean multibyte;
    }

    int reserved;
    ByteOrder byteOrder;
    FitGlobalMessageNum globalMessageNumber;
    List<FieldDefinition> fieldDefinitions = new ArrayList<>();

    /**
     * 32-Bit
     * @param bytes
     * @return
     */
    public int toInt(byte[] bytes){
        return ByteBuffer.wrap(bytes).order(byteOrder).getInt();
    }

    public long toUInt(byte[] bytes){
        return ByteBuffer.wrap(bytes).order(byteOrder).getInt();
    }

    /**
     * 64-Bit
     * @param bytes
     * @return
     */
    public long toLong(byte[] bytes){
        return ByteBuffer.wrap(bytes).order(byteOrder).getLong();
    }

    /**
     * 32-Bit
     * @param bytes
     * @return
     */
    public float toFloat(byte[] bytes){
        return ByteBuffer.wrap(bytes).order(byteOrder).getFloat();
    }

    /**
     * 64-Bit
     * @param bytes
     * @return
     */
    public double toDouble(byte[] bytes){
        return ByteBuffer.wrap(bytes).order(byteOrder).getDouble();
    }

    /**
     * 16-Bit unsigned
     * @param bytes
     * @return
     */
    public char toChar(byte[] bytes){
        return ByteBuffer.wrap(bytes).order(byteOrder).getChar();
    }

    /**
     * 16-Bit signed
     * @param bytes
     * @return
     */
    public short toShort(byte[] bytes){
        return ByteBuffer.wrap(bytes).order(byteOrder).getShort();
    }

    /**
     * 16-Bit unsigned
     * @param bytes
     * @return
     */
    public int toUShort(byte[] bytes){
        return ByteBuffer.wrap(bytes).order(byteOrder).getShort() & 0xFFFF;
    }
}
