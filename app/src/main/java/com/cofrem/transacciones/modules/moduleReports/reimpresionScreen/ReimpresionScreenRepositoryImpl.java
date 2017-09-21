package com.cofrem.transacciones.modules.moduleReports.reimpresionScreen;

import android.content.Context;

import com.cofrem.transacciones.global.InfoGlobalTransaccionSOAP;
import com.cofrem.transacciones.lib.KsoapAsync;
import com.cofrem.transacciones.lib.MD5;
import com.cofrem.transacciones.models.ConfigurationPrinter;
import com.cofrem.transacciones.models.modelsWS.MessageWS;
import com.cofrem.transacciones.models.modelsWS.TransactionWS;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.ResultadoTransaccion;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.TransacList;
import com.cofrem.transacciones.modules.moduleConfiguration.testCommunicationScreen.events.TestCommunicationScreenEvent;
import com.cofrem.transacciones.modules.moduleReports.reimpresionScreen.events.ReimpresionScreenEvent;
import com.cofrem.transacciones.R;
import com.cofrem.transacciones.database.AppDatabase;
import com.cofrem.transacciones.global.InfoGlobalSettingsPrint;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.lib.PrinterHandler;
import com.cofrem.transacciones.lib.StyleConfig;
import com.cofrem.transacciones.models.PrintRow;
import com.cofrem.transacciones.models.Transaccion;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ReimpresionScreenRepositoryImpl implements ReimpresionScreenRepository {
    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */
    // variable que almacenara una tansaccion para imprimirla
    private ArrayList<Transaccion> modelsTransaccion;
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


    @Override
    public void cierreDeLote(Context context) {

        String codigoTermial =  AppDatabase.getInstance(context).obtenerCodigoTerminal();

        ArrayList<Transaccion> listaTransacciones = AppDatabase.getInstance(context).obtenerTransaccionesCierreLote();


        ArrayList<TransacList> transacLists = new ArrayList<>();

        for (Transaccion modelTransaccion : listaTransacciones) {
            if (modelTransaccion.getTipo_transaccion() == Transaccion.TIPO_TRANSACCION_CONSUMO) {
                transacLists.add(new TransacList(codigoTermial,modelTransaccion.getNumero_cargo(),modelTransaccion.getNumero_documento(),Integer.toString(modelTransaccion.getValor()),TransacList.ESTADO_ACTIVO_SIN_CIERRE));
            } else {
                transacLists.add(new TransacList(codigoTermial,modelTransaccion.getNumero_cargo(),modelTransaccion.getNumero_documento(),Integer.toString(modelTransaccion.getValor()),TransacList.ESTADO_DEVOLUCION_SIN_CIERRE));
            }
        }


        ResultadoTransaccion resultadoTransaccion = registrarTransaccionConsumoWS(context ,transacLists);

        //Registra mediante el WS la transaccion
        if (resultadoTransaccion != null) {

            MessageWS messageWS = resultadoTransaccion.getMessageWS();

            if (messageWS.getCodigoMensaje() == MessageWS.statusConsultaExitosa) {

                postEvent(ReimpresionScreenEvent.onCierreLoteSuccess);

                //Imprime el recibo
                //imprimirRecibo(context);

            } else {
                //Error en el registro de la transaccion del web service
                postEvent(ReimpresionScreenEvent.onCierreLoteError, messageWS.getDetalleMensaje(),null,null);
            }
        } else {
            //Error en la conexion con el Web Service
            postEvent(ReimpresionScreenEvent.onTransaccionWSConexionError);
        }
    }

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
        modelsTransaccion = AppDatabase.getInstance(context).obtenerUltimaTransaccion();
        /**
         * En caso de que no exista un registro de una transaccion no se mostrara la vista con el boton
         * de imprimir sino que se no tificara que no existen transacciones para imprimir
         */
        if (!modelsTransaccion.isEmpty()) {
            // Registra el evento de existencia de una transaccion para imprimir
            postEvent(ReimpresionScreenEvent.onVerifyExistenceUltimoReciboSuccess, modelsTransaccion.get(0));
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

        int totalConsumo = 0, totalAnulacion = 0;

        ArrayList<Transaccion> listaAnulaciones = new ArrayList<Transaccion>();

        ConfigurationPrinter configurationPrinter = AppDatabase.getInstance(context).getConfigurationPrinter();

        int gray = configurationPrinter.getGray_level();

        // creamos el ArrayList se que encarga de almacenar los rows del recibo
        ArrayList<PrintRow> printRows = new ArrayList<PrintRow>();

        //Se agrega el logo al primer renglon del recibo y se coloca en el centro
        printRows.add(PrintRow.printLogo(context, gray));

        PrintRow.printCofrem(context, printRows, gray, 10);

        //se siguen agregando cado auno de los String a los renglones (Rows) del recibo para imprimir
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_title_informe_diario), new StyleConfig(StyleConfig.Align.CENTER, gray,20)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_operador), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));
        PrintRow.printOperador(context, printRows, gray, 10);

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_fecha), getDateTime(), new StyleConfig(StyleConfig.Align.LEFT, gray, 20)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_detalle), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_title_transaccion_aprobadas), new StyleConfig(StyleConfig.Align.LEFT, gray, 10)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_text_transaccion), context.getResources().getString(
                R.string.recibo_text_valor), new StyleConfig(StyleConfig.Align.LEFT, gray, 10)));

        for (Transaccion modelTransaccion : listaDetalle) {
            if (modelTransaccion.getTipo_transaccion() == Transaccion.TIPO_TRANSACCION_CONSUMO) {
                totalConsumo += modelTransaccion.getValor();
//                printRows.add(new PrintRow(context.getResources().getString(
//                        R.string.recibo_separador_fecha, modelTransaccion.getRegistro()), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));
                printRows.add(new PrintRow(modelTransaccion.getNumero_cargo(), PrintRow.numberFormat(modelTransaccion.getValor()), new StyleConfig(StyleConfig.Align.LEFT, gray, 4)));
            } else {
                listaAnulaciones.add(modelTransaccion);
            }
        }
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 10)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_total), PrintRow.numberFormat(totalConsumo), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 20)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_title_transaccion_anuladas), new StyleConfig(StyleConfig.Align.LEFT, gray, 10)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_text_transaccion), context.getResources().getString(
                R.string.recibo_text_valor), new StyleConfig(StyleConfig.Align.LEFT, gray, 10)));

        for (Transaccion modelTransaccion : listaAnulaciones) {
            totalAnulacion += modelTransaccion.getValor();
//                printRows.add(new PrintRow(context.getResources().getString(
//                        R.string.recibo_separador_fecha, modelTransaccion.getRegistro()), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));
            printRows.add(new PrintRow(modelTransaccion.getNumero_cargo(), PrintRow.numberFormat(modelTransaccion.getValor()), new StyleConfig(StyleConfig.Align.LEFT, gray, 4)));

        }

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 10)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_total), PrintRow.numberFormat(totalAnulacion), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 20)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_num_transacciones), String.valueOf(listaDetalle.size() - listaAnulaciones.size()), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_num_anulaciones), "" + listaAnulaciones.size(), new StyleConfig(StyleConfig.Align.LEFT, gray, 30)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_entidad), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_vigilado), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3, 60)));

        printRows.add(new PrintRow(".", new StyleConfig(StyleConfig.Align.LEFT, 1)));

        int status = new PrinterHandler().imprimerTexto(printRows);

        if (status == InfoGlobalSettingsPrint.PRINTER_OK) {
            postEvent(ReimpresionScreenEvent.onImprimirReporteDetalleSuccess);
        } else {
            postEvent(ReimpresionScreenEvent.onImprimirReporteDetalleError, PrinterHandler.stringErrorPrinter(status, context), null, null);
        }
    }

    @Override
    public void imprimirReporteGeneral(Context context) {

        ArrayList<Transaccion> listaDetalle = AppDatabase.getInstance(context).obtenerGeneralTransaccion();

        int totalConsumo = 0, totalAnulacion = 0;

        ArrayList<Transaccion> listaAnulaciones = new ArrayList<Transaccion>();

        ConfigurationPrinter configurationPrinter = AppDatabase.getInstance(context).getConfigurationPrinter();

        int gray = configurationPrinter.getGray_level();

        // creamos el ArrayList se que encarga de almacenar los rows del recibo
        ArrayList<PrintRow> printRows = new ArrayList<PrintRow>();

        //Se agrega el logo al primer renglon del recibo y se coloca en el centro
        printRows.add(PrintRow.printLogo(context, gray));

        PrintRow.printCofrem(context, printRows, gray, 10);

        //se siguen agregando cado auno de los String a los renglones (Rows) del recibo para imprimir
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_title_informe_general), new StyleConfig(StyleConfig.Align.CENTER, gray,20)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_operador), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));
        PrintRow.printOperador(context, printRows, gray, 10);

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_fecha), getDateTime(), new StyleConfig(StyleConfig.Align.LEFT, gray, 20)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_detalle), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_title_transaccion_aprobadas), new StyleConfig(StyleConfig.Align.LEFT, gray, 10)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_text_transa)+ "  " +context.getResources().getString(
                R.string.recibo_text_fecha), context.getResources().getString(
                R.string.recibo_text_valor), new StyleConfig(StyleConfig.Align.LEFT, gray, 10)));

        for (Transaccion modelTransaccion : listaDetalle) {
            if (modelTransaccion.getTipo_transaccion() == Transaccion.TIPO_TRANSACCION_CONSUMO) {
                totalConsumo += modelTransaccion.getValor();
//                printRows.add(new PrintRow(context.getResources().getString(
//                        R.string.recibo_separador_fecha, modelTransaccion.getRegistro()), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));
                printRows.add(new PrintRow(modelTransaccion.getNumero_cargo() + "  " + modelTransaccion.getFecha_server(), PrintRow.numberFormat(modelTransaccion.getValor()), new StyleConfig(StyleConfig.Align.LEFT, gray, 4)));
            } else {
                listaAnulaciones.add(modelTransaccion);
            }
        }
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 10)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_total), PrintRow.numberFormat(totalConsumo), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 20)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_title_transaccion_anuladas), new StyleConfig(StyleConfig.Align.LEFT, gray, 10)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_text_transa)+ "  " +context.getResources().getString(
                R.string.recibo_text_fecha), context.getResources().getString(
                R.string.recibo_text_valor), new StyleConfig(StyleConfig.Align.LEFT, gray, 10)));

        for (Transaccion modelTransaccion : listaAnulaciones) {
            totalAnulacion += modelTransaccion.getValor();
//                printRows.add(new PrintRow(context.getResources().getString(
//                        R.string.recibo_separador_fecha, modelTransaccion.getRegistro()), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));
            printRows.add(new PrintRow(modelTransaccion.getNumero_cargo() + "  " + modelTransaccion.getFecha_server(), PrintRow.numberFormat(modelTransaccion.getValor()), new StyleConfig(StyleConfig.Align.LEFT, gray, 4)));

        }
//        printRows.add(new PrintRow("0000070" + "  " + "2017-08-17", PrintRow.numberFormat(3000000), new StyleConfig(StyleConfig.Align.LEFT, gray, 4)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 10)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_total), PrintRow.numberFormat(totalAnulacion), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 20)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_num_transacciones), String.valueOf(listaDetalle.size() - listaAnulaciones.size()), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_num_anulaciones), "" + listaAnulaciones.size(), new StyleConfig(StyleConfig.Align.LEFT, gray, 30)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_entidad), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_vigilado), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3, 60)));

        printRows.add(new PrintRow(".", new StyleConfig(StyleConfig.Align.LEFT, 1)));

        int status = new PrinterHandler().imprimerTexto(printRows);

        if (status == InfoGlobalSettingsPrint.PRINTER_OK) {
            postEvent(ReimpresionScreenEvent.onImprimirReporteGeneralSuccess);
        } else {
            postEvent(ReimpresionScreenEvent.onImprimirReporteGeneralError, PrinterHandler.stringErrorPrinter(status, context), null, null);
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
        String claveadmin = AppDatabase.getInstance(context).getClaveAdmin();

        //conparar la clave del administrador
        if (claveadmin.equals(MD5.crypt(clave))) {
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
        modelsTransaccion = AppDatabase.getInstance(context).obtenerTransaccionByNumeroCargo(numCargo);

        if (!modelsTransaccion.isEmpty()) {
            // Registra el evento de existencia de la transaccion para imprimir
            postEvent(ReimpresionScreenEvent.onVerifyExistenceReciboPorNumCargoSuccess, modelsTransaccion.get(0));
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
            postEvent(ReimpresionScreenEvent.onImprimirUltimoReciboError, PrinterHandler.stringErrorPrinter(status, context), null, null);
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
            postEvent(ReimpresionScreenEvent.onImprimirReciboPorNumCargoError, PrinterHandler.stringErrorPrinter(status, context), null, null);
        }
    }

    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */


    /**
     * Metodo que:
     * - registra mediante el WS la transaccion
     * - Extrae el estado de la transaccion
     *
     * @param context     contexto desde la cual se realiza la transaccion
     * @return regreso del resultado de la transaccion
     */
    private ResultadoTransaccion registrarTransaccionConsumoWS(Context context,ArrayList<TransacList> transacLists) {

        //Se crea una variable de estado de la transaccion
        ResultadoTransaccion resultadoTransaccion = null;

        //TODO: nose como enviar ese array que esta llegando al metodo ya que params es un array de String 

        //dejo este error aqui para que ud sepa en donde voy


        aqui

        //Inicializacion y declaracion de parametros para la peticion web service
        String[][] params = {
                {}
        };

        //Creacion del modelo TransactionWS para ser usado dentro del webservice
        TransactionWS transactionWS = new TransactionWS(
                InfoGlobalTransaccionSOAP.HTTP + AppDatabase.getInstance(context).obtenerURLConfiguracionConexion() + InfoGlobalTransaccionSOAP.WEB_SERVICE_URI,
                InfoGlobalTransaccionSOAP.HTTP + InfoGlobalTransaccionSOAP.NAME_SPACE,
                InfoGlobalTransaccionSOAP.METHOD_NAME_CIERRE_LOTE,
                params);

        //Inicializacion del objeto que sera devuelto por la transaccion del webservice
        SoapObject soapTransaction = null;

        try {

            //Transaccion solicitada al web service
            soapTransaction = new KsoapAsync(new KsoapAsync.ResponseKsoapAsync() {

                /**
                 * Metodo sobrecargado que maneja el callback de los datos
                 *
                 * @param soapResponse
                 * @return
                 */
                @Override
                public SoapObject processFinish(SoapObject soapResponse) {
                    return soapResponse;
                }

            }).execute(transactionWS).get();

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

        }

        //Si la transaccion no genero resultado regresa un establecimiento vacio
        if (soapTransaction != null) {

            //Inicializacion del modelo MessageWS
            MessageWS messageWS = new MessageWS(
                    (SoapObject) soapTransaction.getProperty(MessageWS.PROPERTY_MESSAGE)
            );

            switch (messageWS.getCodigoMensaje()) {

                //Transaccion exitosa
                case MessageWS.statusTransaccionExitosa:


                    break;

                default:

                    break;
            }

        }

        //Retorno de estado de transaccion
        return resultadoTransaccion;
    }



    /**
     * Metodo que se encartga de imprimir :
     *
     * @param context
     */
    private int imprimirRecibo(Context context) {

        //se obtiene la configuracion de la impresora para su uso
        ConfigurationPrinter configurationPrinter = AppDatabase.getInstance(context).getConfigurationPrinter();

        //obtenemos la intensidad con que trabajara la impresora
        int gray = configurationPrinter.getGray_level();

        // creamos el ArrayList se que encarga de almacenar los rows del recibo
        ArrayList<PrintRow> printRows = new ArrayList<PrintRow>();

        //Se agrega el logo al primer renglon del recibo y se coloca en el centro
        printRows.add(PrintRow.printLogo(context, gray));

        Transaccion modelTransaccion = modelsTransaccion.get(0);

        Transaccion modelTransaccionAnulada = null;
        if (modelsTransaccion.size() > 1)
            modelTransaccionAnulada = modelsTransaccion.get(1);

        PrintRow.printCofrem(context, printRows, gray, 10);

        //se siguen agregando cado auno de los String a los renglones (Rows) del recibo para imprimir
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_title_reimpresion), new StyleConfig(StyleConfig.Align.CENTER, gray, 20)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_operador), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));
        PrintRow.printOperador(context, printRows, gray, 10);


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_numero_transaccion), modelTransaccion.getNumero_cargo(), new StyleConfig(StyleConfig.Align.LEFT, gray)));

        if (modelTransaccionAnulada == null)
            printRows.add(new PrintRow(context.getResources().getString(
                    R.string.recibo_fecha), modelTransaccion.getRegistro(), new StyleConfig(StyleConfig.Align.LEFT, gray, 20)));
        else {
            printRows.add(new PrintRow(context.getResources().getString(
                    R.string.recibo_fecha), modelTransaccion.getRegistro(), new StyleConfig(StyleConfig.Align.LEFT, gray)));
            printRows.add(new PrintRow(context.getResources().getString(
                    R.string.recibo_fecha_anulacion), modelTransaccionAnulada.getFullFechaServer(), new StyleConfig(StyleConfig.Align.LEFT, gray, 10)));
            printRows.add(new PrintRow(context.getResources().getString(
                    R.string.recibo_title_transaccion_anulada), new StyleConfig(StyleConfig.Align.CENTER, gray, 20)));

        }


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_afiliado), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_numero_documento), modelTransaccion.getNumero_documento(), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(modelTransaccion.getNombre_usuario(), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_numero_tarjeta), PrinterHandler.getFormatNumTarjeta(modelTransaccion.getNumero_tarjeta()), new StyleConfig(StyleConfig.Align.LEFT, gray, 20)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_detalle), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_valor), PrintRow.numberFormat(modelTransaccion.getValor()), new StyleConfig(StyleConfig.Align.LEFT, gray)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 10)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_total), PrintRow.numberFormat(modelTransaccion.getValor()), new StyleConfig(StyleConfig.Align.LEFT, gray)));

        if (modelTransaccionAnulada == null)
            printRows.add(new PrintRow(context.getResources().getString(
                    R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 30)));
        else {
            printRows.add(new PrintRow(context.getResources().getString(
                    R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 10)));
            printRows.add(new PrintRow(context.getResources().getString(
                    R.string.recibo_valor_anulado), PrintRow.numberFormat(modelTransaccionAnulada.getValor()), new StyleConfig(StyleConfig.Align.LEFT, gray, 30)));

        }

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_mensaje_final), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3, 30)));


        PrintRow.printFirma(context, printRows, gray);

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_entidad), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_vigilado), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3, 20)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_copia), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3, 60)));


        printRows.add(new PrintRow(".", new StyleConfig(StyleConfig.Align.LEFT, 1)));

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
