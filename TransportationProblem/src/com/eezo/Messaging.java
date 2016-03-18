package com.eezo;

import javax.swing.*;

/**
 *
 * Created by Eezo on 17.03.2016.
 */
public final class Messaging {
    public static void showMessageDialog(String message){
        showMessageDialog(message, "info");
    }

    public static void showMessageDialog(String message, String type){
        int t;
        if (type.equalsIgnoreCase("warn")){
            t = JOptionPane.WARNING_MESSAGE;
        } else if (type.equalsIgnoreCase("err")){
            t = JOptionPane.ERROR_MESSAGE;
        } else if (type.equalsIgnoreCase("?")){
            t = JOptionPane.QUESTION_MESSAGE;
        } else {
            t = JOptionPane.INFORMATION_MESSAGE;
        }
        JOptionPane.showMessageDialog(null, message,"Program message", t);
    }

    public static String showInputDialog(String message){
        return JOptionPane.showInputDialog(null, message);
    }

    public static void log(String message){
        log(message, "info");
    }

    public static void log(String message, String type){
        if (type.equalsIgnoreCase("err")){
            System.err.println("ERROR: "+message);
        } else if (type.equalsIgnoreCase("warn")){
            System.out.println("WARN: "+message);
        } else {
            System.out.println("INFO: "+message);
        }
    }


}
