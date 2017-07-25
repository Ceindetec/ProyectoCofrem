package com.cofrem.transacciones.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.cofrem.transacciones.global.InfoGlobalSettingsPrint;
import com.cofrem.transacciones.models.PrintRow;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.printer.ThermalPrinter;
//import com.telpo.tps550.api.printer.Printer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

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
                    Printer.printText(justificarTexto(row.getMsg1(), row.getMsg2()), row.getStyleConfig());
                } else if (row.getMsg1() != null) {
                    Printer.printText(row.getMsg1(), row.getStyleConfig());
                } else if (row.getLogo() != null) {
                    Printer.printImage(row.getLogo(), row.getAlign());
                    Printer.printText("", new StyleConfig(StyleConfig.Align.LEFT, true));
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
    private static String justificarTexto(String msg1, String msg2) {

        //calculado el tamaño de cada unos de los String
        int lengthMsg1 = msg1.length();
        int lengthMsg2 = msg2.length();

        //con el tipo de letra actual que es el tamaño 2 cada renglon tiene 31 caracteres
        //espero no los String que se envien a justificar no suoperen los 31 caracteres para no tener problemas
        int lengthTotal = 31 - (lengthMsg1 + lengthMsg2);

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

}
