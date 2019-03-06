package com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen;

import android.content.Context;

import com.cofrem.transacciones.database.AppDatabase;
import com.cofrem.transacciones.global.InfoGlobalTransaccionSOAP;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.lib.KsoapAsync;
import com.cofrem.transacciones.lib.MD5;
import com.cofrem.transacciones.models.Configurations;
import com.cofrem.transacciones.models.modelsWS.MessageWS;
import com.cofrem.transacciones.models.modelsWS.TransactionWS;
import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.ConexionEstablecimiento;
import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.Establecimiento;
import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.InformacionEstablecimiento;
import com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen.events.RegisterConfigurationScreenEvent;

import org.ksoap2.serialization.SoapObject;

import java.util.concurrent.ExecutionException;

public class RegisterConfigurationScreenRepositoryImpl implements RegisterConfigurationScreenRepository {

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public RegisterConfigurationScreenRepositoryImpl() {
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     * Valida el acceso a la configuracion del dispositivo mediante la contraseña de administrador
     *
     * @param context
     * @param passAdmin
     */
    @Override
    public void validarPasswordTecnico(Context context, String passAdmin) {

        int validateExistValorAcceso = AppDatabase.getInstance(context).validarAccesoByClaveTecnica(MD5.crypt(passAdmin));

        switch (validateExistValorAcceso) {
            case 0:
                postEvent(RegisterConfigurationScreenEvent.onClaveTecnicaNoValida);
                break;
            case 1:
                postEvent(RegisterConfigurationScreenEvent.onClaveTecnicaValida);
                break;
            default:
                postEvent(RegisterConfigurationScreenEvent.onClaveTecnicaError);
                break;
        }

    }

    /**
     * Registra los parametros de conexion del dispositivo
     *
     * @param context
     * @param configurations
     */
    @Override
    public void registrarConfiguracionConexion(Context context, Configurations configurations) {

        //Se crea una nueva instancia del model establecimiento
        Establecimiento establecimiento;

        //Valida por medio del WS si la informacion del establecimiento es correcta
        establecimiento = validarInfoDispositivo(context, configurations);

        //Si la informacion del estableceimiento no es nula, la recoleccion de la informacion es correcta
        if (establecimiento != null) {

            MessageWS messageWS = establecimiento.getMessageWS();

            if (messageWS.getCodigoMensaje() == MessageWS.statusConsultaExitosa ||
                    messageWS.getCodigoMensaje() == MessageWS.statusTerminalExiste) {

                //Se registra la configuracion de la conexion en el dispositivo
                if (AppDatabase.getInstance(context).insertConfiguracionConexion(configurations)) {

                    //Se registra la configuracion de la conexion en el establecimiento
                    if (AppDatabase.getInstance(context).processInfoEstablecimiento(establecimiento)) {

                        AppDatabase.getInstance(context).obtenerInfoHeader();

                        //Evento Correcto de registro de informacion del establecimiento desde el WS
                        // y actualizacion de accesos
                        postEvent(RegisterConfigurationScreenEvent.onProccessInformacionEstablecimientoSuccess);

                    } else {

                        //Evento Erroneo de registro de informacion del establecimiento desde el WS y actualizacion de accesos
                        postEvent(RegisterConfigurationScreenEvent.onProccessInformacionEstablecimientoError);

                    }
                } else {

                    //Evento Erroneo de registro de configuracion de la conexion en el establecimiento
                    postEvent(RegisterConfigurationScreenEvent.onRegistroConfigConexionError);
                }

            } else {

                //Evento Erroneo de registro de informacion del establecimiento desde el WS y actualizacion de accesos
                postEvent(RegisterConfigurationScreenEvent.onInformacionDispositivoErrorInformacion);

            }

        } else {

            AppDatabase.getInstance(context).deleteConfiguracionConexion();

            //Evento Erroneo de recepcion de informacion del establecimiento desde el WS
            postEvent(RegisterConfigurationScreenEvent.onInformacionDispositivoErrorConexion);

        }

    }


    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################     *
     */

    /**
     * Metodo que:
     * - valida mediante el WS la existencia del dispositivo
     * - Extrae la informacion del establecimiento
     *
     * @param context
     * @param configurations
     * @return
     */
    private Establecimiento validarInfoDispositivo(Context context, Configurations configurations) {

        //Se crea una nueva instancia del model establecimiento para ser retornada
        Establecimiento establecimiento = null;

        //Inicializacion y declaracion de parametros para la peticion web service
        String[] params = new String[]{InfoGlobalTransaccionSOAP.PARAM_NAME_TERMINAL_CODIGO_TERMINAL, configurations.getCodigoDispositivo()};

        //Creacion del modelo TransactionWS para ser usado dentro del webservice
        TransactionWS transactionWS = new TransactionWS(
                InfoGlobalTransaccionSOAP.HTTP + configurations.getHost() + ":" + configurations.getPort() + InfoGlobalTransaccionSOAP.WEB_SERVICE_URI,
                InfoGlobalTransaccionSOAP.HTTP + InfoGlobalTransaccionSOAP.NAME_SPACE,
                InfoGlobalTransaccionSOAP.METHOD_NAME_TERMINAL,
                new String[][]{params});

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
            MessageWS messageWS = new MessageWS((SoapObject) soapTransaction.getProperty(0));

            switch (messageWS.getCodigoMensaje()) {

                //Informacion encontrada
                case MessageWS.statusTransaccionExitosa:
                case MessageWS.statusTerminalExiste:
                case MessageWS.statusConsultaExitosa:


                    //Inicializacion del modelo InformacionEstablecimiento
                    InformacionEstablecimiento informacionEstablecimiento = new InformacionEstablecimiento((SoapObject) soapTransaction.getProperty(1));

                    //Inicializacion del modelo ConexionEstablecimiento
                    ConexionEstablecimiento conexionEstablecimiento = new ConexionEstablecimiento((SoapObject) soapTransaction.getProperty(1));

                    //Inicializacion del modelo establecimiento
                    establecimiento = new Establecimiento(
                            informacionEstablecimiento,
                            conexionEstablecimiento,
                            messageWS
                    );
                    break;


                //Terminal no existe
                case MessageWS.statusTerminalNoExiste:
                    //Inicializacion del modelo establecimiento
                    establecimiento = new Establecimiento(
                            messageWS
                    );
                    break;

                //Error general en la transaccion
                case MessageWS.statusTerminalErrorException:
                    //Inicializacion del modelo establecimiento
                    establecimiento = new Establecimiento(
                            messageWS
                    );
                    break;
            }

        }

        //Retorno del establecimiento
        return establecimiento;
    }

    /**
     * Metodo que registra los eventos
     *
     * @param type
     * @param errorMessage
     */
    private void postEvent(int type, String errorMessage) {
        RegisterConfigurationScreenEvent registerConfigurationScreenEvent = new RegisterConfigurationScreenEvent();
        registerConfigurationScreenEvent.setEventType(type);
        if (errorMessage != null) {
            registerConfigurationScreenEvent.setErrorMessage(errorMessage);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();

        eventBus.post(registerConfigurationScreenEvent);
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
