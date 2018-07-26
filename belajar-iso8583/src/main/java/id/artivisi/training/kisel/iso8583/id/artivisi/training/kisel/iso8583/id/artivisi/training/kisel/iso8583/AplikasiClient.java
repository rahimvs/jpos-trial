package id.artivisi.training.kisel.iso8583;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.channel.XMLChannel;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.iso.packager.XMLPackager;

import java.net.Socket;
import java.util.Date;


public class AplikasiClient {
    public static void main(String[] args) throws ISOException {
//        DateFormat formatterBit7 = new SimpleDateFormat("MMddHHmmss");
//
//        Map<Integer, String> logonRequest = new LinkedHashMap<Integer, String>();
//        logonRequest.put(7, formatterBit7.format(new Date()));
//        logonRequest.put(11, "834624");
//        logonRequest.put(70, "001");
//
//        AplikasiClient client = new AplikasiClient();
//        BigInteger bitmapRequest = Iso8583Helper.hitungBitmap(logonRequest);
//
//        String strBitmap = bitmapRequest.toString(2);
////        System.out.println("Bitmap Binary : ["+strBitmap+"]");
//
//        String strBitmapHex = bitmapRequest.toString(16);
////        System.out.println("Bitmap Hexa : ["+strBitmapHex+"]");
//
//        String strLogonRequest = Iso8583Helper.messageString("0800", logonRequest);
////        System.out.println("Logon Request : ["+strLogonRequest+"]");
//
//        short messageLength = (short) (strLogonRequest.length() + 2);
////        System.out.println("Message Length : "+ messageLength);
//
//        byte[] baLength = new byte[2];
//        baLength[0] = (byte) ((messageLength >> 8) & 0xff);
//        baLength[1] = (byte) (messageLength  & 0xff);
//        System.out.println("Message Length Byte Order : "+new String(baLength));



//        System.out.println("Bitmap Binary Logon Response ["+bitmapResponse.toString(2));
//        System.out.println("Bitmap Hex Logon Response ["+bitmapResponse.toString(16));

        ISOMsg msg = new ISOMsg("0200");
        msg.set(2, "001");
        msg.set(3, "341000");
        msg.set(7, "1115112500");
        msg.set(11, "123456");
        msg.set(12, "112500");
        msg.set(13, "1115");
        msg.set(15, "1115");
        msg.set(18, "6012");
        msg.set(29, "C00000000");
        msg.set(31, "C00000000");
        msg.set(103, "001");

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0200"); //request financial transaction
        isoMsg.set(3, "123456");
        isoMsg.set(4, "300000");
        String formattedDate = new java.text.SimpleDateFormat("MMddhhmmss").format(new Date());
        isoMsg.set(7, formattedDate);
        isoMsg.set(11, "654321"); //bebas, can be sequencial
        isoMsg.set(22, "123"); //special specification from channel
        isoMsg.set(24, "123"); //special specification from channel
        isoMsg.set(35, "AAAAAAAAAAAAAAA"); //special specification from channel
        isoMsg.set(37, "1234567890AB"); //bebas, can be sequencial
        isoMsg.set(41, "CAT12345"); //special specification from channel
        isoMsg.set(42, "MERCHANT1234567"); //special specification from channel
        isoMsg.set(48, "000"); //special specification from host
        isoMsg.set(63, "020010023110030001"); //special specification from host
        isoMsg.setPackager(new GenericPackager("cfg/tian-packager.xml"));
        String result = new String(isoMsg.pack());

        msg.setPackager(new GenericPackager("cfg/tian-packager.xml"));
        String msgString = new String(msg.pack());
        System.out.println("Account Inquiry : "+msgString);

        AplikasiClient client = new AplikasiClient();
        client.kirim("080020200000008000000000000000013239313130303031");

        client.kirim(msgString);

        client.kirim(result);

    }



    public void kirim(String message){
        short messageLength = (short) (message.length() + 2);
        System.out.println("Panjang message : "+ messageLength);

        try {
            // mengirim data
            Socket koneksi = new Socket("localhost", 8000);
            DataOutputStream out = new DataOutputStream(koneksi.getOutputStream());
            out.writeShort(messageLength);
            out.writeBytes(message);
            out.flush();
            System.out.println("Data terkirim");

            // menerima response
            DataInputStream in = new DataInputStream(koneksi.getInputStream());
            short respLength = in.readShort();
            System.out.println("Panjang response = "+respLength);
            byte[] responseData = new byte[respLength - 2];
            in.readFully(responseData);
            System.out.println("Response = ["+new String(responseData)+"]");

            koneksi.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
