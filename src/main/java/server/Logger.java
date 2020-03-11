package server;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alfred Jones
 *
 * A simple logger class which formats any info we want to display
 * in the console
 *
 */

public class Logger {


    /**
     * @author Alfred Jones
     * @param text the text to be logged
     */
    public static void log(String text) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm:ss.SSS dd/MM/yyyy");
        Date d = new Date();
        System.out.println("[" + simpleDate.format(d) + "] " + text);
    }

}
