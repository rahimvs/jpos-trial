package id.artivisi.training.kisel.jpos;

import org.jpos.iso.ISOMsg;
import org.jpos.q2.Q2;
import org.jpos.q2.iso.QMUX;
import org.jpos.iso.ISOException;
import org.jpos.util.NameRegistrar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class AplikasiClient {
    public static void main(String[] args) throws Exception {
        Q2 q2 = new Q2();
        q2.start();

        Thread.sleep(5 * 1000);

        QMUX sender = (QMUX) NameRegistrar.get("mux.kisel");

        DateFormat formatterBit7 = new SimpleDateFormat("MMddHHmmss");
        ISOMsg logonRequest = new ISOMsg("0200");
//        logonRequest.set(7, formatterBit7.format(new Date()));
//        logonRequest.set(11, "123456");
//        logonRequest.set(70, "001");

        logonRequest.set(3, "201234");
        logonRequest.set(4, "10000");
        logonRequest.set(7, formatterBit7.format(new Date()));
        logonRequest.set(11, "123456");
        logonRequest.set(44, "A5DFGR");
        logonRequest.set(105, "SVMIHARISO8583 1234567890");



        Scanner input = new Scanner(System.in);
        ISOMsg response = sender.request(logonRequest, 20 * 1000);

//        printISOMessage(logonRequest);
        String msg = new String(logonRequest.pack());
        System.out.println("Kirim request \t\t: ["+msg+"]");
        printISOMessage(logonRequest);

        if (response == null) {
            System.out.println("Tidak dapat response sampai timeout");
            //SEND REVERSAL HERE
            return;
        }

        String data = newW String(response.pack());
        System.out.println("Terima response \t: ["+data+"]");
        printISOMessage(response);

    }
    private static void printISOMessage(ISOMsg isoMsg) {
        try {
            System.out.println("MTI: " + isoMsg.getMTI());
            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.printf("Field (%s) = %s%n", i, isoMsg.getString(i));
                }
            }
            System.out.println();
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }
}
