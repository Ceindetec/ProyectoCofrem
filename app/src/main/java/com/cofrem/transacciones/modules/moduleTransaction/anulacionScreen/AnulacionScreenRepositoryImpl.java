package com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen;

import android.content.Context;
import android.util.Log;

import com.cofrem.transacciones.R;
import com.cofrem.transacciones.database.AppDatabase;
import com.cofrem.transacciones.global.InfoGlobalSettingsPrint;
import com.cofrem.transacciones.global.InfoGlobalTransaccionSOAP;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.lib.KsoapAsync;
import com.cofrem.transacciones.lib.MD5;
import com.cofrem.transacciones.lib.PrinterHandler;
import com.cofrem.transacciones.lib.StyleConfig;
import com.cofrem.transacciones.models.ConfigurationPrinter;
import com.cofrem.transacciones.models.PrintRow;
import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.models.modelsWS.MessageWS;
import com.cofrem.transacciones.models.modelsWS.TransactionWS;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionTransaccion;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.ResultadoTransaccion;
import com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen.events.AnulacionScreenEvent;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AnulacionScreenRepositoryImpl implements AnulacionScreenRepository {
    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */
    Transaccion modelTransaccion;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public AnulacionScreenRepositoryImpl() {
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     * Metodo quevalida la existencia de la clave de administracion
     *
     * @param context
     * @param passAdmin
     */
    @Override
    public void validarPasswordAdministrador(Context context, String passAdmin) {

        int validateExistValorAcceso = AppDatabase.getInstance(context).validarAccesoByClaveAdministracion(MD5.crypt(passAdmin));

        switch (validateExistValorAcceso) {
            case 0:
                postEvent(AnulacionScreenEvent.onClaveAdministracionNoValida);
                break;
            case 1:
                postEvent(AnulacionScreenEvent.onClaveAdministracionValida);
                break;
            default:
                postEvent(AnulacionScreenEvent.onClaveAdministracionError);
                break;
        }

    }

    /**
     * @param context
     * @param numeroCargo
     */
    @Override
    public void obtenerValorTransaccion(Context context, String numeroCargo) {

        Log.i("Repositorio cargo", numeroCargo);

        int valorTransaccion = AppDatabase.getInstance(context).obtenerValorTransaccion(numeroCargo);

        switch (valorTransaccion) {
            case AnulacionScreenEvent.VALOR_TRANSACCION_NO_VALIDO:
                postEvent(AnulacionScreenEvent.onNumeroCargoNoRelacionado);
                break;
            default:
                postEvent(AnulacionScreenEvent.onNumeroCargoRelacionado, valorTransaccion);
                break;
        }
    }

    /**
     * @param context
     * @param transaccion
     */
    @Override
    public void registrarTransaccion(Context context, Transaccion transaccion) {

        ResultadoTransaccion resultadoTransaccion = registrarTransaccionConsumoWS(context, transaccion);

        //Registra mediante el WS la transaccion
        if (resultadoTransaccion != null) {

            MessageWS messageWS = resultadoTransaccion.getMessageWS();

            if (messageWS.getCodigoMensaje() == MessageWS.statusTransaccionExitosa) {

                //Registro en la base de datos de la transaccion
                if (registrarTransaccionConsumoDB(context, resultadoTransaccion.getInformacionTransaccion())) {

                    postEvent(AnulacionScreenEvent.onTransaccionSuccess);

                    modelTransaccion = transaccion;

                    //Imprime el recibo
                    imprimirRecibo(context, context.getResources().getString(
                            R.string.recibo_copia_comercio));

                } else {

                    //Error en el registro en la Base de Datos la transaccion
                    postEvent(AnulacionScreenEvent.onTransaccionDBRegisterError);

                }
            } else {
                //Error en el registro de la transaccion del web service
                postEvent(AnulacionScreenEvent.onTransaccionWSRegisterError, messageWS.getDetalleMensaje());
            }
        } else

        {
            //Error en la conexion con el Web Service
            postEvent(AnulacionScreenEvent.onTransaccionWSConexionError);
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
     * @param context
     * @param transaccion
     * @return
     */

    private ResultadoTransaccion registrarTransaccionConsumoWS(Context context, Transaccion transaccion) {

        //Se crea una variable de estado de la transaccion
        ResultadoTransaccion resultadoTransaccion = null;

        //Inicializacion y declaracion de parametros para la peticion web service
        String[][] params = {
                {InfoGlobalTransaccionSOAP.PARAM_NAME_ANULACION_CODIGO_TERMINAL, AppDatabase.getInstance(context).obtenerCodigoTerminal()},
                {InfoGlobalTransaccionSOAP.PARAM_NAME_ANULACION_NUMERO_APROBACION, transaccion.getNumero_cargo()},
                {InfoGlobalTransaccionSOAP.PARAM_NAME_ANULACION_CEDULA_USUARIO, transaccion.getNumero_documento()},
                {InfoGlobalTransaccionSOAP.PARAM_NAME_ANULACION_NUMERO_TARJETA, transaccion.getNumero_tarjeta()},
                {InfoGlobalTransaccionSOAP.PARAM_NAME_ANULACION_CLAVE_USUARIO, String.valueOf(transaccion.getClave())},
                {InfoGlobalTransaccionSOAP.PARAM_NAME_ANULACION_TIPO_ENCRIPTACION, String.valueOf(transaccion.getTipo_encriptacion())},
                {InfoGlobalTransaccionSOAP.PARAM_NAME_ANULACION_VALOR_APROBADO, String.valueOf(transaccion.getValor())},
        };

        //Creacion del modelo TransactionWS para ser usado dentro del webservice
        TransactionWS transactionWS = new TransactionWS(
                InfoGlobalTransaccionSOAP.HTTP + AppDatabase.getInstance(context).obtenerURLConfiguracionConexion() + InfoGlobalTransaccionSOAP.WEB_SERVICE_URI,
                InfoGlobalTransaccionSOAP.HTTP + InfoGlobalTransaccionSOAP.NAME_SPACE,
                InfoGlobalTransaccionSOAP.METHOD_NAME_ANULACION,
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

                    InformacionTransaccion informacionTransaccion = new InformacionTransaccion(
                            (SoapObject) soapTransaction.getProperty(InformacionTransaccion.PROPERTY_TRANSAC_RESULT)
                    );

                    resultadoTransaccion = new ResultadoTransaccion(
                            informacionTransaccion,
                            messageWS
                    );
                    break;

                default:
                    resultadoTransaccion = new ResultadoTransaccion(
                            messageWS
                    );
                    break;
            }

        }

        //Retorno de estado de transaccion
        return resultadoTransaccion;
    }

    /**
     * Metodo que registra en la base de datos interna la transaccion
     *
     * @param context
     * @return
     */
    private boolean registrarTransaccionConsumoDB(Context context, InformacionTransaccion informacionTransaccion) {

        //Se crea una variable de estado de la transaccion
        boolean statusTransaction = false;

        //Se registra la transaccion
        if (AppDatabase.getInstance(context).insertRegistroTransaction(informacionTransaccion, Transaccion.TIPO_TRANSACCION_ANULACION)) {
            statusTransaction = true;
        }

        return statusTransaction;
    }


    /**
     * Metodo que imprime el recibo de la transaccion
     *
     * @param context
     * @param stringCopia
     */
    @Override
    public void imprimirRecibo(Context context, String stringCopia) {

        ConfigurationPrinter configurationPrinter = AppDatabase.getInstance(context).getConfigurationPrinter();

        int gray = configurationPrinter.getGray_level();

        Transaccion modelTransaccionAnulada = AppDatabase.getInstance(context).obtenerUltimaTransaccionAnulada();

//        String fecha_transaccion = AppDatabase.getInstance(context).obtenerFechaTransaccionNumCargo(modelTransaccion.getNumero_cargo());

        // creamos el ArrayList se que encarga de almacenar los rows del recibo
        ArrayList<PrintRow> printRows = new ArrayList<PrintRow>();

        //Se agrega el logo al primer renglon del recibo y se coloca en el centro
        printRows.add(PrintRow.printLogo(context, gray));

        PrintRow.printCofrem(context, printRows, gray, 10);

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_title_anulacion), new StyleConfig(StyleConfig.Align.CENTER, gray, 20)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_operador), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));
        PrintRow.printOperador(context, printRows, gray, 10);

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_numero_transaccion), modelTransaccion.getNumero_cargo(), new StyleConfig(StyleConfig.Align.LEFT, gray)));
//        printRows.add(new PrintRow(context.getResources().getString(
//                R.string.recibo_fecha),fecha_transaccion, new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_fecha_anulacion), modelTransaccionAnulada.getFullFechaServer(), new StyleConfig(StyleConfig.Align.LEFT, gray, 20)));

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
                R.string.recibo_valor_anulado), PrintRow.numberFormat(modelTransaccionAnulada.getValor()), new StyleConfig(StyleConfig.Align.LEFT, gray)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_entidad), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_vigilado), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3, 20)));
        printRows.add(new PrintRow(stringCopia, new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3, 60)));


        printRows.add(new PrintRow(".", new StyleConfig(StyleConfig.Align.LEFT, 1)));

        int status = new PrinterHandler().imprimerTexto(printRows);

        if (status == InfoGlobalSettingsPrint.PRINTER_OK) {
            int i = 0;
        } else {
            int i = 1;
        }


    }

    /**
     * Metodo que registra los eventos
     *
     * @param type
     * @param errorMessage
     */
    private void postEvent(int type, String errorMessage, int valorInt) {

        AnulacionScreenEvent anulacionScreenEvent = new AnulacionScreenEvent();

        anulacionScreenEvent.setEventType(type);

        if (errorMessage != null) {
            anulacionScreenEvent.setErrorMessage(errorMessage);
        }

        if (valorInt != AnulacionScreenEvent.VALOR_TRANSACCION_NO_VALIDO) {
            anulacionScreenEvent.setValorInt(valorInt);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();

        eventBus.post(anulacionScreenEvent);
    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type) {

        postEvent(type, null, AnulacionScreenEvent.VALOR_TRANSACCION_NO_VALIDO);

    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type, int valorInt) {

        postEvent(type, null, valorInt);

    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type, String errorMessage) {

        postEvent(type, errorMessage, AnulacionScreenEvent.VALOR_TRANSACCION_NO_VALIDO);

    }

}
