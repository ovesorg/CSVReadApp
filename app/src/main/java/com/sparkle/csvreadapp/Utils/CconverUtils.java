package com.sparkle.csvreadapp.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
 * ת��������
 *
 * @author HFH
 */
public class CconverUtils {

    public final static char[] BCD_2_ASC = "0123456789abcdef".toCharArray();

    private CconverUtils() {

    }

    /**
     * @param args
     */

    /**
     * ���ַ�ת��Ϊ�ֽڣ���ʮ������ַ�תΪ�ֽ�����ʱ��
     *
     * @param achar
     * @return
     * @author hfh, 2007-10-25
     */
    private static byte char2byte(char achar) {
        byte b = (byte) "0123456789ABCDEF".indexOf(Character.toUpperCase(achar));
        return b;
    }

    /**
     * ��16�����ַ�ת�����ֽ�����
     *
     * @param hexStr
     * @return
     * @author hfh, 2007-10-25
     */
    public static final byte[] hexStr2Bytes(String hexStr) {
        hexStr = hexStr.replace(" ", ""); //tang
        int len = (hexStr.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hexStr.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (char2byte(achar[pos]) << 4 | char2byte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * ���ֽ�����ת����16�����ַ�
     *
     * @param bArray
     * @return
     * @author hfh, 2007-10-25
     */
    public static final String bytes2HexStr(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * ���ֽ�����ת����16�����ַ�
     * <p>
     * ���ֽڼ���ָ���ָ���ָ�,���ڲ鿴
     *
     * @param bArray
     * @param delimiter
     * @return
     * @author hfh, 2007-10-25
     */
    public static final String bytes2HexStr(byte[] bArray, String delimiter) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);

            sb.append(sTemp.toUpperCase());

            // Ƕ��ָ���    
            if (i < bArray.length - 1)
                sb.append(delimiter);
        }
        return sb.toString();
    }

    /**
     * ���ֽ�����ת��Ϊ����
     *
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @author hfh, 2007-10-25
     */
    public static final Object bytes2Object(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(in);
        Object o = oi.readObject();
        oi.close();
        return o;
    }

    /**
     * �������л�����ת�����ֽ�����
     *
     * @param s
     * @return
     * @throws IOException
     * @author hfh, 2007-10-25
     */
    public static final byte[] object2Bytes(Serializable s) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream ot = new ObjectOutputStream(out);
        ot.writeObject(s);
        ot.flush();
        ot.close();
        return out.toByteArray();
    }

    /**
     * �������л�����ת����16�����ַ�
     *
     * @param s
     * @return
     * @throws IOException
     * @author hfh, 2007-10-25
     */
    public static final String object2HexStr(Serializable s) throws IOException {
        return bytes2HexStr(object2Bytes(s));
    }

    /**
     * ��16�����ַ�ת���ɶ���
     *
     * @param hexStr
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @author hfh, 2007-10-25
     */
    public static final Object hexStr2Object(String hexStr) throws IOException, ClassNotFoundException {
        return bytes2Object(CconverUtils.hexStr2Bytes(hexStr));
    }

    /**
     * BCD��תΪ10���ƴ�(���������ִ�)
     *
     * @param bytes
     * @return
     * @author hfh, 2007-10-25
     */
    public static String bcd2DigitStr(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp
                .toString();
    }

    /**
     * 10���ƴ�תΪBCD��
     *
     * @param asc
     * @return
     * @author hfh, 2007-10-25
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;

        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }

        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }

        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;

        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }

            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }

            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /**
     * BCD��תASC��
     *
     * @param bytes
     * @return
     * @author hfh, 2007-10-25
     */
    public static String bcd2Asc(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++) {
            int h = ((bytes[i] & 0xf0) >>> 4);
            int l = (bytes[i] & 0x0f);
            temp.append(BCD_2_ASC[h]).append(BCD_2_ASC[l]);
        }
        return temp.toString();
    }

    /**
     * ���ַ�ת��Ϊ����ֵ
     *
     * @param value
     * @return
     * @author hfh, 2007-10-26
     */
    public static boolean str2boolean(String value) {
        return value.equalsIgnoreCase("true") || value.equals("1");
    }

}   
