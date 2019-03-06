package com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen;

import android.content.Context;

import com.cofrem.transacciones.database.AppDatabase;
import com.cofrem.transacciones.global.InfoGlobalTransaccionSOAP;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.lib.KsoapAsync;
import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.models.modelsWS.MessageWS;
import com.cofrem.transacciones.models.modelsWS.TransactionWS;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionSaldo;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionTransaccion;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.ResultadoTransaccion;
import com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen.events.UltimaTransaccionScreenEvent;

import org.ksoap2.serialization.SoapObject;

import java.util.concurrent.ExecutionException;

public class UltimaTransaccionScreenRepositoryImpl implements UltimaTransaccionScreenRepository {
    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */


    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public UltimaTransaccionScreenRepositoryImpl() {
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    @Override
    public void validarUltimaTransaccion(Context context) {

        //Se crea una variable de estado de la transaccion
        ResultadoTransaccion resultadoTransaccionValidar = null;

        //Inicializacion y declaracion de parametros para la peticion web service
        String[][] params = {
                {InfoGlobalTransaccionSOAP.PARAM_NAME_TRANSACCION_CODIGO_TERMINAL, AppDatabase.getInstance(context).obtenerCodigoTerminal()}
        };

        //Creacion del modelo TransactionWS para ser usado dentro del webservice
        TransactionWS transactionWS = new TransactionWS(
                InfoGlobalTransaccionSOAP.HTTP + AppDatabase.getInstance(context).obtenerURLConfiguracionConexion() + InfoGlobalTransaccionSOAP.WEB_SERVICE_URI,
                InfoGlobalTransaccionSOAP.HTTP + InfoGlobalTransaccionSOAP.NAME_SPACE,
                InfoGlobalTransaccionSOAP.METHOD_NAME_VALIDACION,
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
            MessageWS messageWS = new MessageWS((SoapObject) soapTransaction.getProperty(MessageWS.PROPERTY_MESSAGE));

            switch (messageWS.getCodigoMensaje()) {

                //Transaccion exitosa
                case MessageWS.statusTransaccionExitosa:

                    InformacionTransaccion informacionTransaccion = new InformacionTransaccion((SoapObject) soapTransaction.getProperty(InformacionTransaccion.PROPERTY_TRANSAC_RESULT));

                    resultadoTransaccionValidar = new ResultadoTransaccion(informacionTransaccion, messageWS);

                    break;

                default:

                    resultadoTransaccionValidar = new ResultadoTransaccion(messageWS);

                    break;
            }

        }

        try {

            Transaccion transaccion = AppDatabase.getInstance(context).obtenerUltimaTransaccion().get(0);

            if (resultadoTransaccionValidar.getInformacionTransaccion() != null) {
                if (resultadoTransaccionValidar.getInformacionTransaccion().getCedulaUsuario() == transaccion.getNumero_documento() &&
                        resultadoTransaccionValidar.getInformacionTransaccion().getNumeroTarjeta() == transaccion.getNumero_tarjeta() &&
                        Integer.valueOf(resultadoTransaccionValidar.getInformacionTransaccion().getValor()) == transaccion.getValor() &&
                        Integer.valueOf(resultadoTransaccionValidar.getInformacionTransaccion().getTipoTransaccion()) == transaccion.getTipo_transaccion() &&
                        Integer.valueOf(resultadoTransaccionValidar.getInformacionTransaccion().getTipoServicio()) == transaccion.getTipo_servicio()
                        )

                    postEvent(UltimaTransaccionScreenEvent.onValidarUltimaTransaccionCorrecta);
                else

                    postEvent(UltimaTransaccionScreenEvent.onValidarUltimaTransaccionErronea);
            }
        } catch (NullPointerException e) {

            postEvent(UltimaTransaccionScreenEvent.onValidarUltimaTransaccionError);

        } catch (IndexOutOfBoundsException e) {

            postEvent(UltimaTransaccionScreenEvent.onValidarUltimaTransaccionError);

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
     * @param context contexto desde la cual se realiza la transaccion
     * @return regreso del resultado de la transaccion
     */
    private ResultadoTransaccion registrarTransaccionConsumoWS(Context context) {

        //Se crea una variable de estado de la transaccion
        ResultadoTransaccion resultadoTransaccion = null;

        //Inicializacion y declaracion de parametros para la peticion web service
        String[][] params = {
                {InfoGlobalTransaccionSOAP.PARAM_NAME_SALDO_CODIGO_TERMINAL, AppDatabase.getInstance(context).obtenerCodigoTerminal()}
        };

        //Creacion del modelo TransactionWS para ser usado dentro del webservice
        TransactionWS transactionWS = new TransactionWS(
                InfoGlobalTransaccionSOAP.HTTP + AppDatabase.getInstance(context).obtenerURLConfiguracionConexion() + InfoGlobalTransaccionSOAP.WEB_SERVICE_URI,
                InfoGlobalTransaccionSOAP.HTTP + InfoGlobalTransaccionSOAP.NAME_SPACE,
                InfoGlobalTransaccionSOAP.METHOD_NAME_TEST,
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

                    InformacionSaldo informacionSaldo = new InformacionSaldo(
                            (SoapObject) soapTransaction.getProperty(InformacionSaldo.PROPERTY_SALDO_RESULT)
                    );

                    resultadoTransaccion = new ResultadoTransaccion(
                            informacionSaldo,
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
     * Metodo que registra los eventos
     *
     * @param type
     * @param errorMessage
     */
    private void postEvent(int type, String errorMessage) {
        UltimaTransaccionScreenEvent ultimaTransaccionScreenEvent = new UltimaTransaccionScreenEvent();
        ultimaTransaccionScreenEvent.setEventType(type);
        if (errorMessage != null) {
            ultimaTransaccionScreenEvent.setErrorMessage(errorMessage);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();

        eventBus.post(ultimaTransaccionScreenEvent);
    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type) {

        postEvent(type, null);

    }
}
