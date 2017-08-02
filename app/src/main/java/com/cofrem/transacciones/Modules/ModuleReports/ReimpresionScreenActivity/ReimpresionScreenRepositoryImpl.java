package com.cofrem.transacciones.Modules.ModuleReports.ReimpresionScreenActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cofrem.transacciones.Modules.ModuleReports.ReimpresionScreenActivity.events.ReimpresionScreenEvent;
import com.cofrem.transacciones.R;
import com.cofrem.transacciones.database.AppDatabase;
import com.cofrem.transacciones.global.InfoGlobalSettingsPrint;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.lib.PrinterHandler;
import com.cofrem.transacciones.lib.StyleConfig;
import com.cofrem.transacciones.models.PrintRow;
import com.cofrem.transacciones.models.Transaccion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReimpresionScreenRepositoryImpl implements ReimpresionScreenRepository {
    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */
    // variable que almacenara una tansaccion para imprimirla
    private Transaccion modelTransaccion;
    // lista de las transacciones que estan para imprimir en el reporte de Detalles
    private ArrayList<Transaccion> listaDetalle;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public ReimpresionScreenRepositoryImpl() {
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     *
     */
    @Override
    public void validateAcces() {

        postEvent(ReimpresionScreenEvent.onVerifySuccess);

    }

    /**
     * Metodo que verifica:
     * - La existencia de una ultima transaccion
     *
     * @param context
     */
    @Override
    public void validarExistenciaUltimoRecibo(Context context) {
        //Consulta la existencia del registro de la ultima transaccion
        modelTransaccion = AppDatabase.getInstance(context).obtenerUltimaTransaccion();
        /**
         * En caso de que no exista un registro de una transaccion no se mostrara la vista con el boton
         * de imprimir sino que se no tificara que no existen transacciones para imprimir
         */
        if (modelTransaccion.getNumero_tarjeta() != null) {
            // Registra el evento de existencia de una transaccion para imprimir
            postEvent(ReimpresionScreenEvent.onVerifyExistenceUltimoReciboSuccess, modelTransaccion);
        } else {
            // Registra el evento de la NO existencia de una transaccion para imprimir
            postEvent(ReimpresionScreenEvent.onVerifyExistenceUltimoReciboError);
        }
    }

    /**
     * Metodo que verifica:
     * - La existencia de transacciones para imprimir el reporte detallado
     *
     * @param context
     */
    @Override
    public void validarExistenciaDetalleRecibos(Context context) {
        //Consulta la existencia del registros de transacciones
        listaDetalle = AppDatabase.getInstance(context).obtenerDetallesTransaccion();

        if (!listaDetalle.isEmpty()) {
            // Registra el evento de existencia de transacciones para imprimir el reporte
            postEvent(ReimpresionScreenEvent.onVerifyExistenceReporteDetalleSuccess, listaDetalle);
        } else {
            // Registra el evento de la NO existencia de transacciones para imprimir el reporte
            postEvent(ReimpresionScreenEvent.onVerifyExistenceReporteDetalleError);
        }

    }

    @Override
    public void imprimirReporteDetalle(Context context) {
        //logo de COFREM que se imprime al inicio del recibo
        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);

        // creamos el ArrayList se que encarga de almacenar los rows del recibo
        ArrayList<PrintRow> printRows = new ArrayList<PrintRow>();

        //Se agrega el logo al primer renglon del recibo y se coloca en el centro
        printRows.add(new PrintRow(logo, StyleConfig.Align.CENTER));

        //se siguen agregando cado auno de los String a los renglones (Rows) del recibo para imprimir
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.report_text_button_detalle), new StyleConfig(StyleConfig.Align.CENTER, StyleConfig.FontStyle.BOLD)));
        printRows.add(new PrintRow(getDateTime(), new StyleConfig(StyleConfig.Align.CENTER, 20)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_num_transacciones), String.valueOf(listaDetalle.size()), new StyleConfig(StyleConfig.Align.LEFT, true)));


        for (Transaccion modelTransaccion : listaDetalle) {
            printRows.add(new PrintRow(context.getResources().getString(
                    R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, 10)));
            printRows.add(new PrintRow(context.getResources().getString(
                    R.string.recibo_numero_transaccion), String.valueOf(modelTransaccion.getNumero_cargo()), new StyleConfig(StyleConfig.Align.LEFT, true)));
            printRows.add(new PrintRow(context.getResources().getString(
                    R.string.recibo_valor), String.valueOf(modelTransaccion.getValor()), new StyleConfig(StyleConfig.Align.LEFT, true)));

        }

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, 50)));
        printRows.add(new PrintRow(".", new StyleConfig(StyleConfig.Align.LEFT, true)));

        int status = new PrinterHandler().imprimerTexto(printRows);

        if (status == InfoGlobalSettingsPrint.PRINTER_OK) {
            postEvent(ReimpresionScreenEvent.onImprimirReporteDetalleSuccess);
        } else {
            postEvent(ReimpresionScreenEvent.onImprimirReporteDetalleError, stringErrorPrinter(status, context), null, null);
        }
    }

    @Override
    public void imprimirReporteGeneral(Context context) {

        ArrayList<Transaccion> listaDetalle = AppDatabase.getInstance(context).obtenerDetallesTransaccion();

        int valor = 0;

        //logo de COFREM que se imprime al inicio del recibo
        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);

        // creamos el ArrayList se que encarga de almacenar los rows del recibo
        ArrayList<PrintRow> printRows = new ArrayList<PrintRow>();

        //Se agrega el logo al primer renglon del recibo y se coloca en el centro
        printRows.add(new PrintRow(logo, StyleConfig.Align.CENTER));

        //se siguen agregando cado auno de los String a los renglones (Rows) del recibo para imprimir
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.report_text_button_detalle), new StyleConfig(StyleConfig.Align.CENTER, StyleConfig.FontStyle.BOLD)));
        printRows.add(new PrintRow(getDateTime(), new StyleConfig(StyleConfig.Align.CENTER, 20)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_num_transacciones), String.valueOf(listaDetalle.size()), new StyleConfig(StyleConfig.Align.LEFT, true)));


        for (Transaccion modelTransaccion : listaDetalle) {
//            printRows.add(new PrintRow(context.getResources().getString(
//                    R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, 10)));
//            printRows.add(new PrintRow(context.getResources().getString(
//                    R.string.recibo_numero_transaccion), String.valueOf(modelTransaccion.getNumero_cargo()), new StyleConfig(StyleConfig.Align.LEFT, true)));
//            printRows.add(new PrintRow(context.getResources().getString(
//                    R.string.recibo_valor), String.valueOf(modelTransaccion.getValor()), new StyleConfig(StyleConfig.Align.LEFT, true)));

            valor += modelTransaccion.getValor();
        }
            printRows.add(new PrintRow(context.getResources().getString(
                    R.string.recibo_valor), String.valueOf(valor), new StyleConfig(StyleConfig.Align.LEFT, true)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, 50)));
        printRows.add(new PrintRow(".", new StyleConfig(StyleConfig.Align.LEFT, true)));

        int status = new PrinterHandler().imprimerTexto(printRows);

        if (status == InfoGlobalSettingsPrint.PRINTER_OK) {
            postEvent(ReimpresionScreenEvent.onImprimirReporteGeneralSuccess);
        } else {
            postEvent(ReimpresionScreenEvent.onImprimirReporteGeneralError, stringErrorPrinter(status, context), null, null);
        }
    }


    /**
     * Metodo que valida la contraseña del administrador para reimprimir los recibos:
     *
     * @param context
     * @param clave
     */
    @Override
    public void validarClaveAdministrador(Context context, String clave) {

        //Consulta la clave del administrador para compararla con la ingresada en la vista


        //conparar la clave del administrador
        if (clave.equals("123")) {
            // Registra el evento de que la clave es correcta
            postEvent(ReimpresionScreenEvent.onVerifyClaveAdministradorSuccess);
        } else {
            // Registra el evento de que la clave es Incorrecta
            postEvent(ReimpresionScreenEvent.onVerifyClaveAdministradorError);
        }
    }

    /**
     * Metodo que verifica:
     * - La existencia de una transaccion por número de Cargo
     *
     * @param context
     */
    @Override
    public void validarExistenciaReciboConNumCargo(Context context, String numCargo) {

        //Consulta la existencia del registro de una transaccion por numero de Cargo
        modelTransaccion = AppDatabase.getInstance(context).obtenerTransaccion(numCargo);

        if (modelTransaccion.getNumero_tarjeta() != null) {
            // Registra el evento de existencia de la transaccion para imprimir
            postEvent(ReimpresionScreenEvent.onVerifyExistenceReciboPorNumCargoSuccess, modelTransaccion);
        } else {
            // Registra el evento de la NO existencia de la transaccion para imprimir
            postEvent(ReimpresionScreenEvent.onVerifyExistenceReciboPorNumCargoError);
        }

    }


    /**
     * Metodo que se encartga:
     * - hacer el llamado para imprimir la ultima transaccion
     * - notificar los diferentes estados de la impresora, por si no se pudio imprimir
     *
     * @param context
     */
    @Override
    public void imprimirUltimoRecibo(Context context) {

        int status = imprimirRecibo(context);

        if (status == InfoGlobalSettingsPrint.PRINTER_OK) {
            postEvent(ReimpresionScreenEvent.onImprimirUltimoReciboSuccess);
        } else {
            postEvent(ReimpresionScreenEvent.onImprimirUltimoReciboError, stringErrorPrinter(status, context), null, null);
        }

    }

    /**
     * Metodo que se encartga:
     * - hacer el llamado para imprimir una transaccion por numero de cargo
     * - notificar los diferentes estados de la impresora, por si no se pudio imprimir
     *
     * @param context
     */
    @Override
    public void imprimirReciboConNumCargo(Context context) {


        int status = imprimirRecibo(context);

        if (status == InfoGlobalSettingsPrint.PRINTER_OK) {
            postEvent(ReimpresionScreenEvent.onImprimirReciboPorNumCargoSuccess);
        } else {
            postEvent(ReimpresionScreenEvent.onImprimirReciboPorNumCargoError, stringErrorPrinter(status, context), null, null);
        }
    }

    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo que se encartga de imprimir :
     *
     * @param context
     */
    private int imprimirRecibo(Context context) {

        //logo de COFREM que se imprime al inicio del recibo
        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);

        // creamos el ArrayList se que encarga de almacenar los rows del recibo
        ArrayList<PrintRow> printRows = new ArrayList<PrintRow>();

        //Se agrega el logo al primer renglon del recibo y se coloca en el centro
        printRows.add(new PrintRow(logo, StyleConfig.Align.CENTER));

        //se siguen agregando cado auno de los String a los renglones (Rows) del recibo para imprimir
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_reimpresion), new StyleConfig(StyleConfig.Align.CENTER, StyleConfig.FontStyle.BOLD)));
        printRows.add(new PrintRow(getDateTime(), new StyleConfig(StyleConfig.Align.CENTER, 20)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_numero_transaccion), String.valueOf(modelTransaccion.getNumero_cargo()), new StyleConfig(StyleConfig.Align.LEFT, true)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_valor), String.valueOf(modelTransaccion.getValor()), new StyleConfig(StyleConfig.Align.LEFT, true)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_numero_tarjeta), modelTransaccion.getNumero_tarjeta(), new StyleConfig(StyleConfig.Align.LEFT, 20)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_ingresa_firma), new StyleConfig(StyleConfig.Align.LEFT, true)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_ingresa_cc), new StyleConfig(StyleConfig.Align.LEFT, true)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_ingresa_tel), new StyleConfig(StyleConfig.Align.LEFT, 50)));
        printRows.add(new PrintRow(".", new StyleConfig(StyleConfig.Align.LEFT, true)));

        //retornamos el estado de la impresora tras enviar los rows para imprimir
        return new PrinterHandler().imprimerTexto(printRows);
    }


    /**
     * Metodo auxiliar que se encarga de obtener la fecha y hora del sistema :
     *
     * @return date
     */
    private String getDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());

        Date date = new Date();

        return dateFormat.format(date);
    }


    /**
     * Metodo auxiliar que se encarga de retornar el String correspondiente al estado de la impresora
     *
     * @return String
     */
    private String stringErrorPrinter(int status, Context context) {
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
     * Metodo que registra los eventos
     *
     * @param type
     * @param errorMessage
     */
    private void postEvent(int type, String errorMessage, Transaccion modalTransaccion, ArrayList<Transaccion> lista) {
        ReimpresionScreenEvent reimpresionScreenEvent = new ReimpresionScreenEvent();
        reimpresionScreenEvent.setEventType(type);
        if (errorMessage != null) {
            reimpresionScreenEvent.setErrorMessage(errorMessage);
        }

        if (modalTransaccion != null) {
            reimpresionScreenEvent.setModelTransaccion(modalTransaccion);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();

        eventBus.post(reimpresionScreenEvent);
    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type, ArrayList<Transaccion> lista) {

        postEvent(type, null, null, lista);

    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type, Transaccion modelTransaccion) {

        postEvent(type, null, modelTransaccion, null);

    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type) {

        postEvent(type, null, null, null);

    }
}
