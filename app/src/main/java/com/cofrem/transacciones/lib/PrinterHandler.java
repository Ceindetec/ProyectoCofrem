package com.cofrem.transacciones.lib;


import android.content.Context;

import com.cofrem.transacciones.R;
import com.cofrem.transacciones.global.InfoGlobalSettingsPrint;
import com.cofrem.transacciones.models.PrintRow;

import java.util.ArrayList;


/**
 * Created by luispineda on 12/07/17.
 */

public class PrinterHandler {

    private static ArrayList<PrintRow> Rows;


    /**
     * Constructor de la clase
     */
    public PrinterHandler() {

    }

    /**
     * metodo privado encargado de comunicarse con la clase Printer
     * Retorna el estado de la impresora
     *
     * @return int
     */
    private static int imprimir() {

        //inicializar el arraylist interno en Printer para recibir los string a imprimir
        //inicializar el hilo de TermalPrinter
        Printer.connect();

        //obtener el estasdo de la impresora
        int status = Printer.getStatus();

        //validar que el estado sea igual a Ok para poder seguir con el proceso de imprimir
        if (status == 0) {

            //recorrer el ArrayList de rows a imprimir
            for (PrintRow row : Rows) {
                //validar si el row tiene 2 String. De ser así
                // el primer String se imprime a la izquierda y el segundo a la derecha
                if (row.getMsg1() != null && row.getMsg2() != null) {
                    Printer.printText(justificarTexto(row.getMsg1(), row.getMsg2(), row.getStyleConfig()), row.getStyleConfig());
                } else if (row.getMsg1() != null) {
                    Printer.printText(row.getMsg1(), row.getStyleConfig());
                } else if (row.getLogo() != null) {
                    Printer.printImage(row.getLogo(), row.getAlign(), row.getGray(), row.getLineSpace());

                }
                //dar la orden a Printer de que imprima el renglon
                Printer.commitOperation();
            }
//            Printer.printSalto();
        }
        return status;
    }

    /**
     * metodo privado encargado de justificar el texto recibido en los dos String
     * Retorna un String justificado
     *
     * @param msg1
     * @param msg2
     * @return String resul
     */
    private static String justificarTexto(String msg1, String msg2, StyleConfig style) {

        //calculado el tamaño de cada unos de los String
        int lengthMsg1 = msg1.length();
        int lengthMsg2 = msg2.length();

        //con el tipo de letra actual que es el tamaño 2 cada renglon tiene 31 caracteres
        //espero no los String que se envien a justificar no suoperen los 31 caracteres para no tener problemas
        int lengthTotal = 0;

        if (style.fontSize == StyleConfig.FontSize.F1) {
            lengthTotal = 46 - (lengthMsg1 + lengthMsg2);
        } else if (style.fontSize == StyleConfig.FontSize.F2) {
            lengthTotal = 46 - (lengthMsg1 + lengthMsg2);
        } else if (style.fontSize == StyleConfig.FontSize.F3) {
            lengthTotal = 31 - (lengthMsg1 + lengthMsg2);
        }

        //colocar el primer Strin en la parte Izquierda del renglon
        String resul = msg1;

        //completar con espacion en blanco parta que se vean justificados los dos Strings
        for (int i = 0; i < lengthTotal; i++) {
            resul += " ";
        }
        // Colocar el segundo String en la parte derecha del renglon
        resul += msg2;

        // retornar el String con los dos String recibido mas los espacios entre ellos
        return resul;
    }

    /**
     * Metodo auxiliar que se encarga de retornar el String correspondiente al estado de la impresora
     *
     * @return String
     */
    public static String stringErrorPrinter(int status, Context context) {
        String result = "";
        switch (status) {
            case InfoGlobalSettingsPrint.PRINTER_DISCONNECT:
                result = context.getResources().getString(R.string.printer_disconnect);
                break;
            case InfoGlobalSettingsPrint.PRINTER_OUT_OF_PAPER:
                result = context.getResources().getString(R.string.printer_out_of_paper);
                break;
            case InfoGlobalSettingsPrint.PRINTER_OVER_FLOW:
                result = context.getResources().getString(R.string.printer_over_flow);
                break;
            case InfoGlobalSettingsPrint.PRINTER_OVER_HEAT:
                result = context.getResources().getString(R.string.printer_over_heat);
                break;
            case InfoGlobalSettingsPrint.PRINTER_ERROR:
                result = context.getResources().getString(R.string.printer_error);
                break;
        }
        return result;
    }

    /**
     * Metodo que se encarga de mostrar el numero de las tarjeta
     *
     * @return String
     */
    public static String getFormatNumTarjeta(String num) {

        int length = num.length();
        int punto = length - 3;

        if (punto < 0)
            punto = 0;

        return "* * * " + num.substring(punto, length);
    }

    /**
     * metodo publico que recibe un array de rows para imprimir
     * Retorna el estado de la impresora
     *
     * @param modelRow
     * @return int
     */
    public int imprimerTexto(ArrayList<PrintRow> modelRow) {
        Rows = modelRow;
        return imprimir();
    }

    public boolean testPrinterDevice() {

        boolean testPrinterDevice = false;

        if (Printer.testPrinterDevice()) {
            testPrinterDevice = true;
        }

        return testPrinterDevice;

    }


}
