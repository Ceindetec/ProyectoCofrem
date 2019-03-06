package com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cofrem.transacciones.MainScreenActivity_;
import com.cofrem.transacciones.R;
import com.cofrem.transacciones.TransactionScreenActivity_;
import com.cofrem.transacciones.global.InfoGlobalSettingsBlockButtons;
import com.cofrem.transacciones.lib.KeyBoard;
import com.cofrem.transacciones.lib.MagneticHandler;
import com.cofrem.transacciones.models.InfoHeaderApp;
import com.cofrem.transacciones.models.PrintRow;
import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen.AnulacionScreenPresenter;
import com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen.AnulacionScreenPresenterImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import static android.view.KeyEvent.KEYCODE_ENTER;

@EActivity(R.layout.activity_transaction_anulacion_screen)
public class AnulacionScreenActivity extends Activity implements AnulacionScreenView {

    /*
      #############################################################################################
      Declaracion de componentes y variables
      #############################################################################################
     */

    final static int PASO_CLAVE_ADMINISTRADOR = 0;
    final static int PASO_NUMERO_CARGO = 1;
    final static int PASO_NUMERO_DOCUMENTO = 2;
    final static int PASO_VERIFICACION_VALOR = 3;
    final static int PASO_DESLIZAR_TARJETA = 4;
    final static int PASO_CLAVE_USUARIO = 5;
    final static int PASO_TRANSACCION_EXITOSA = 6;
    /**
     * Declaracion de los Contoles
     */

    //Controles del header
    @ViewById
    TextView txvHeaderIdDispositivo;
    @ViewById
    TextView txvHeaderIdPunto;
    @ViewById
    TextView txvHeaderEstablecimiento;
    @ViewById
    TextView txvHeaderPunto;
    //Controles del modulo
    @ViewById
    RelativeLayout bodyContentAnulacionClaveAdministrador;
    @ViewById
    RelativeLayout bodyContentAnulacionNumeroCargo;
    @ViewById
    RelativeLayout bodyContentAnulacionNumeroDocumento;
    @ViewById
    RelativeLayout bodyContentAnulacionVerificacionValor;
    @ViewById
    RelativeLayout bodyContentAnulacionDesliceTarjeta;
    @ViewById
    RelativeLayout bodyContentAnulacionLecturaIncorrecta;

    //Paso transaction_anulacion_paso_verificacion_valor
    @ViewById
    RelativeLayout bodyContentAnulacionPassUsuario;
    @ViewById
    RelativeLayout bodyContentAnulacionTransaccionExitosa;
    @ViewById
    RelativeLayout bodyContentAnulacionTransaccionErronea;
    @ViewById
    FrameLayout frlPgbHldTransactionAnulacion;
    //Paso transaction_anulacion_paso_clave_administrador
    @ViewById
    EditText edtAnulacionTransactionClaveAdministradorContenidoClave;
    //Paso transaction_anulacion_paso_numero_documento
    @ViewById
    EditText edtAnulacionTransactionNumeroDocumentoValor;
    //Paso transaction_anulacion_paso_numero_cargo
    @ViewById
    EditText edtAnulacionTransactionNumeroCargoContenidoValor;
    @ViewById
    TextView txvAnulacionTransactionVerificacionDatosNumeroCargo;
    @ViewById
    TextView txvAnulacionTransactionVerificacionDatosNumeroDocumento;
    @ViewById
    TextView txvAnulacionTransactionVerificacionDatosValorCantidad;
    //Paso transaction_anulacion_paso_deslice_tarjeta
    @ViewById
    TextView txvAnulacionTransactionDesliceTarjetaDatosNumeroCargo;
    @ViewById
    TextView txvAnulacionTransactionDesliceTarjetaDatosNumeroDocumento;
    @ViewById
    TextView txvAnulacionTransactionDesliceTarjetaDatosValorCantidad;
    //Paso content_transaction_anulacion_paso_pass_usuario
    @ViewById
    EditText edtAnulacionTransactionClaveUsuarioContenidoClave;
    //Paso content_transaction_anulacion_paso_transaccion_error
    @ViewById
    TextView txvAnulacionTransactionErrorDetalleTexto;
    /**
     * Model que almacena la transaccion actual
     */
    Transaccion modelTransaccion = new Transaccion();
    String passwordAdmin = "";
    /**
     * Pasos definidos
     */
    int pasoTransaction = 0; //Define el paso actual
    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    //Instanciamiento de la interface AnulacionScreenPresenter
    private AnulacionScreenPresenter anulacionScreenPresenter;

    /**
     * #############################################################################################
     * Constructor  de  la clase
     * #############################################################################################
     */
    @AfterViews
    void MainInit() {

        //Instanciamiento e inicializacion del presentador
        anulacionScreenPresenter = new AnulacionScreenPresenterImpl(this);

        //Llamada al metodo onCreate del presentador para el registro del bus de datos
        anulacionScreenPresenter.onCreate();

        //Metodo que oculta por defecto los include de la vista
        inicializarOcultamientoVistas();

        // Metodo para colocar la orientacion de la app
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Coloca la informacion del encabezado
        setInfoHeader();

        //Inicializa el paso de la contraseña de administrador
        pasoTransaction = PASO_CLAVE_ADMINISTRADOR;

        //Primera ventana visible
        bodyContentAnulacionClaveAdministrador.setVisibility(View.VISIBLE);

    }

    /*
      #############################################################################################
      Metodos sobrecargados del sistema
      #############################################################################################
     */

    /**
     * Metodo sobrecargado de la vista para la destruccion de la activity
     */
    @Override
    public void onDestroy() {
        anulacionScreenPresenter.onDestroy();
        super.onDestroy();
    }

    /**
     * Metodo que intercepta las pulsaciones de las teclas del teclado fisico
     *
     * @param keyCode Tecla del evento
     * @param event   Evento
     * @return retorno de Boolean
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*
          Keycodes disponibles

          4: Back
          66: Enter
          67: Delete
         */
        switch (keyCode) {

            case KEYCODE_ENTER:

                //Ocula el soft keyboard al presionar la tecla enter
                hideKeyBoard();

                switch (pasoTransaction) {

                    case PASO_CLAVE_ADMINISTRADOR:
                        //Metodo para la validacion de la contraseña de administracion
                        validarPassAdministrador();
                        break;

                    case PASO_NUMERO_DOCUMENTO:
                        //Metodo para registrar el numero de documento
                        registrarNumeroDocumento();
                        break;

                    case PASO_NUMERO_CARGO:
                        //Metodo para registrar el numero del cargo
                        registrarNumeroCargo();
                        break;

                    case PASO_VERIFICACION_VALOR:
                        //Metodo para mostrar la información registrada
                        verificarValor();
                        break;

                    case PASO_DESLIZAR_TARJETA:
                        //Metodo para mostrar la orden de deslizar la tarjeta
                        deslizarTarjeta();
                        break;

                    case PASO_CLAVE_USUARIO:
                        //Metodo para registrar la contraseña del usuario
                        registrarPassUsuario();
                        break;

                    case PASO_TRANSACCION_EXITOSA:
                        //Metodo para ir a la ventana principal
                        navigateToMainScreen();
                        break;
                }
                break;

            default:
                Log.i("Key Pressed", String.valueOf(keyCode));
                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Metodo que interfiere la presion del boton "Back"
     */
    @Override
    public void onBackPressed() {

        switch (pasoTransaction) {

            case PASO_CLAVE_ADMINISTRADOR:
                //Vacia la caja de la contraseña de administracion
                edtAnulacionTransactionClaveAdministradorContenidoClave.setText("");
                break;

            case PASO_NUMERO_DOCUMENTO:
                //Vacia la caja del numero de documento
                edtAnulacionTransactionNumeroDocumentoValor.setText("");
                break;

            case PASO_NUMERO_CARGO:
                //Vacia la caja del numero de cargo
                edtAnulacionTransactionNumeroCargoContenidoValor.setText("");
                break;

            case PASO_VERIFICACION_VALOR:

                break;

            case PASO_DESLIZAR_TARJETA:

                break;

            case PASO_CLAVE_USUARIO:
                //Vacia la caja del valor de la contraseña de usuario
                edtAnulacionTransactionClaveUsuarioContenidoClave.setText("");
                break;

            case PASO_TRANSACCION_EXITOSA:

                break;
        }
    }

    /**
     * Metodo que interfiere en la presion del boton Task
     */
    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    /**
     * Metodo sobrecargado de la vista para la presion de las teclas de volumen
     *
     * @param event evento de la presion de una tecla
     * @return regresa el rechazo de la presion
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (InfoGlobalSettingsBlockButtons.blockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
    /*
      #############################################################################################
      Metodos sobrecargados de la interface
      #############################################################################################
     */

    /**
     * Metodo para manejar la verificacion exitosa de la clave de administracion
     */
    public void handleClaveAdministracionValida() {

        //Oculta la barra de progreso
        hideProgress();

        //Oculta la vista de la contraseña de administracion
        bodyContentAnulacionClaveAdministrador.setVisibility(View.GONE);

        //Muestra la vista del registro de numero de cargo
        bodyContentAnulacionNumeroCargo.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoTransaction++;
    }

    /**
     * Metodo para manejar la verificacion erronea de la clave de administracion
     */
    public void handleClaveAdministracionNoValida() {

        //Oculta la barra de progreso
        hideProgress();

        //Muestra el mensaje de error en la contraseña de administracion
        Toast.makeText(this, R.string.configuration_text_clave_no_valido, Toast.LENGTH_SHORT).show();
    }

    /**
     * Metodo para manejar error en la verificacion de la clave de administracion
     */
    public void handleClaveAdministracionError() {

        //Oculta la barra de progreso
        hideProgress();

        //Muestra el mensaje de error en la configuracion de la contraseña de administracion tecnica
        Toast.makeText(this, R.string.configuration_text_clave_error, Toast.LENGTH_SHORT).show();

    }

    /**
     * Metodo para manejar el valor no valido en la transaccion
     */
    @Override
    public void handleNumeroCargoNoRelacionado() {

        //Oculta la barra de progreso
        hideProgress();

        //Muestra el mensaje de error en el  monto de transaccion registrado
        Toast.makeText(this, R.string.transaction_error_numero_Cargo_no_relacionado, Toast.LENGTH_SHORT).show();
    }

    /**
     * Metodo para manejar el valor valido en la transaccion
     */
    @Override
    public void handleNumeroCargoRelacionado(int valorTransaccion) {

        //Se oculta la barra de progreso
        hideProgress();

        //Se registra el valor de la transaccion
        modelTransaccion.setValor(valorTransaccion);

        //Oculta la vista del numero de cargo
        bodyContentAnulacionNumeroCargo.setVisibility(View.GONE);

        //Muestra la vista de verificacion del valor
        bodyContentAnulacionNumeroDocumento.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoTransaction++;

    }

    /**
     * Metodo para manejar la transaccion exitosa
     */
    @Override
    public void handleTransaccionSuccess() {

        //Oculta la barra de progreso
        hideProgress();

        //Oculta la vista de clave de usuario
        bodyContentAnulacionPassUsuario.setVisibility(View.GONE);

        //Muestra la vista de transaccion exitosa
        bodyContentAnulacionTransaccionExitosa.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoTransaction++;
    }

    /**
     * Metodo para manejar la conexion del Web Service Erronea
     */
    @Override
    public void handleTransaccionWSConexionError() {

        //Oculta la barra de progreso
        hideProgress();

        //Oculta la vista de clave de usuario
        bodyContentAnulacionPassUsuario.setVisibility(View.GONE);

        //Muestra la vista de transaccion erronea
        bodyContentAnulacionTransaccionErronea.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoTransaction++;
    }

    /**
     * Metodo para manejar la transaccion erronea desde el Web Service
     *
     * @param errorMessage mensaje de error
     */
    @Override
    public void handleTransaccionWSRegisterError(String errorMessage) {

        //Oculta la barra de progreso
        hideProgress();

        //Muestra el mensaje de error del registro de informacion del dispositivo incorrecto
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

        //Regresa a la vista de transacciones
        navigateToTransactionScreen();
    }

    /**
     * Metodo para manejar la transaccion erronea desde la base de datos
     */
    @Override
    public void handleTransaccionDBRegisterError() {

        //Oculta la barra de progreso
        hideProgress();

        //Oculta la vista de clave de usuario
        bodyContentAnulacionPassUsuario.setVisibility(View.GONE);

        //Muestra la vista de transaccion erronea
        bodyContentAnulacionTransaccionErronea.setVisibility(View.VISIBLE);
    }


    /*
      #############################################################################################
      Metodo propios de la clase
      #############################################################################################
     */

    /**
     * Metodo para mostrar la barra de progreso
     */
    private void showProgress() {
        //TODO: VERIFICAR QUE ESTA MOSTRANDO LA BARRA DE PROGRESO
        //Muestra la barra  de progreso
        frlPgbHldTransactionAnulacion.setVisibility(View.VISIBLE);
        frlPgbHldTransactionAnulacion.bringToFront();
        frlPgbHldTransactionAnulacion.invalidate();
    }

    /**
     * Metodo para ocultar la barra de progreso
     */
    private void hideProgress() {
        //Oculta la barra de progreso
        frlPgbHldTransactionAnulacion.setVisibility(View.GONE);
    }

    /**
     * Metodo que oculta por defecto los include de la vista
     */
    private void inicializarOcultamientoVistas() {

        bodyContentAnulacionClaveAdministrador.setVisibility(View.GONE);
        bodyContentAnulacionNumeroCargo.setVisibility(View.GONE);
        bodyContentAnulacionNumeroDocumento.setVisibility(View.GONE);
        bodyContentAnulacionVerificacionValor.setVisibility(View.GONE);
        bodyContentAnulacionDesliceTarjeta.setVisibility(View.GONE);
        bodyContentAnulacionLecturaIncorrecta.setVisibility(View.GONE);
        bodyContentAnulacionPassUsuario.setVisibility(View.GONE);
        bodyContentAnulacionTransaccionExitosa.setVisibility(View.GONE);
        bodyContentAnulacionTransaccionErronea.setVisibility(View.GONE);

    }

    /**
     * Metodo que llena el header de la App
     */
    private void setInfoHeader() {

        txvHeaderIdDispositivo.setText(
                String.format(
                        getString(R.string.header_text_id_dispositivo_registrado)
                        , InfoHeaderApp.getInstance().getIdDispositivo()
                )
        );

        txvHeaderIdPunto.setText(
                String.format(
                        getString(R.string.header_text_id_punto_registrado)
                        , InfoHeaderApp.getInstance().getIdPunto()
                )
        );

        txvHeaderEstablecimiento.setText(
                String.format(
                        getString(R.string.header_text_nombre_establecimiento_registrado)
                        , InfoHeaderApp.getInstance().getNombreEstablecimiento()
                )
        );

        txvHeaderPunto.setText(
                String.format(
                        getString(R.string.header_text_nombre_punto_registrado)
                        , InfoHeaderApp.getInstance().getNombrePunto()
                )
        );

    }

    /**
     * Metodo que oculta el teclado al presionar el EditText
     */
    @LongClick({R.id.edtAnulacionTransactionClaveAdministradorContenidoClave,
            R.id.edtAnulacionTransactionNumeroDocumentoValor,
            R.id.edtAnulacionTransactionNumeroCargoContenidoValor,
            R.id.edtAnulacionTransactionClaveUsuarioContenidoClave
    })
    @Click({R.id.edtAnulacionTransactionClaveAdministradorContenidoClave,
            R.id.edtAnulacionTransactionNumeroDocumentoValor,
            R.id.edtAnulacionTransactionNumeroCargoContenidoValor,
            R.id.edtAnulacionTransactionClaveUsuarioContenidoClave
    })
    @Touch({R.id.edtAnulacionTransactionClaveAdministradorContenidoClave,
            R.id.edtAnulacionTransactionNumeroDocumentoValor,
            R.id.edtAnulacionTransactionNumeroCargoContenidoValor,
            R.id.edtAnulacionTransactionClaveUsuarioContenidoClave
    })
    public void hideKeyBoard() {

        //TODO:VERIFICAR QUE EL TECLADO SE ESTA OCULTANDO
        //Oculta el teclado
        KeyBoard.hide(this);

    }

    /**
     * Metodo para regresar a la ventana de transaccion
     */
    @Click({R.id.btnAnulacionTransactionClaveAdministradorBotonCancelar,
            R.id.btnAnulacionTransactionNumeroDocumentoBotonCancelar,
            R.id.btnAnulacionTransactionNumeroCargoBotonCancelar,
            R.id.btnAnulacionTransactionVerificacionDatosBotonCancelar,
            R.id.btnAnulacionTransactionLecturaIncorrectaBotonSalir,
            R.id.btnAnulacionTransactionClaveUsuarioBotonCancelar,
            R.id.btnAnulacionTransactionErrorBotonSalir,
    })
    public void navigateToTransactionScreen() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(AnulacionScreenActivity.this, TransactionScreenActivity_.class);
                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 100);
    }

    /**
     * Metodo para regresar a la ventana inicial
     */
    @Click({R.id.btnAnulacionTransactionExitosaBotonSalir})
    public void navigateToMainScreen() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(AnulacionScreenActivity.this, MainScreenActivity_.class);

                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 100);

    }

    /**
     * Metodo que valida la contraseña de administracion
     */
    @Click(R.id.btnAnulacionTransactionClaveAdministradorBotonAceptar)
    public void validarPassAdministrador() {

        //Se obtiene el texto de la contraseña
        passwordAdmin = edtAnulacionTransactionClaveAdministradorContenidoClave.getText().toString();

        //Vacia la caja de contraseña
        edtAnulacionTransactionClaveAdministradorContenidoClave.setText("");

        //La contraseña debe ser de exactamente 4 caracteres
        if (passwordAdmin.length() == 4) {

            //Muestra la barra de progreso
            showProgress();

            //Valida la contraseña
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    anulacionScreenPresenter.validarPasswordAdministrador(AnulacionScreenActivity.this, passwordAdmin);
                }
            }, 100);

        } else {

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_format_clave_administrador, Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Metodo para registrar el numero de cargo
     */
    @Click(R.id.btnAnulacionTransactionNumeroCargoBotonAceptar)
    public void registrarNumeroCargo() {

        //Se obtiene el numero de cargo
        String numeroCargo = edtAnulacionTransactionNumeroCargoContenidoValor.getText().toString();

        //La longitud del numero de cargo debe ser mayor a 0 y diferente del valor 0
        if (numeroCargo.length() > 0 && !numeroCargo.equals("0")) {

            //Registra el numero de cargo
            modelTransaccion.setNumero_cargo(numeroCargo);

            //Muestra la barra de progreso
            showProgress();

            //Obtiene el valor de la transaccion
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    anulacionScreenPresenter.obtenerValorTransaccion(AnulacionScreenActivity.this, modelTransaccion.getNumero_cargo());
                }
            }, 100);


        } else {

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_format_numero_cargo, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Metodo para registrar el numero de documento
     */
    @Click(R.id.btnAnulacionTransactionNumeroDocumentoBotonAceptar)
    public void registrarNumeroDocumento() {

        //Se obtiene el numero de documento
        String numeroDocumento = edtAnulacionTransactionNumeroDocumentoValor.getText().toString();

        //Vacia la caja del numero de documento
        edtAnulacionTransactionNumeroDocumentoValor.setText("");

        //La longitud del numero de documento debe ser mayor a 0
        if (numeroDocumento.length() > 0) {

            //Registra el valor del numero de documento en el modelo de transaccion
            modelTransaccion.setNumero_documento(numeroDocumento);

            //Registra el numero de cargo en la vista de verificacion
            txvAnulacionTransactionVerificacionDatosNumeroCargo.setText(
                    modelTransaccion.getNumero_cargo()
            );

            //Registra el numero de documento en la vista de verificacion
            txvAnulacionTransactionVerificacionDatosNumeroDocumento.setText(
                    String.valueOf(modelTransaccion.getNumero_documento())
            );

            //Registra el valor del consumo en la vista de verificacion
            txvAnulacionTransactionVerificacionDatosValorCantidad.setText(
                    PrintRow.numberFormat(modelTransaccion.getValor())
            );

            //Registra el numero de cargo en la vista de verificacion
            txvAnulacionTransactionDesliceTarjetaDatosNumeroCargo.setText(
                    modelTransaccion.getNumero_cargo()
            );

            //Registra el numero de documento en la vista de verificacion
            txvAnulacionTransactionDesliceTarjetaDatosNumeroDocumento.setText(
                    String.valueOf(modelTransaccion.getNumero_documento())
            );

            //Registra el valor del consumo en la vista de verificacion
            txvAnulacionTransactionDesliceTarjetaDatosValorCantidad.setText(
                    PrintRow.numberFormat(modelTransaccion.getValor())
            );

            //Oculta la vista del numero de documento
            bodyContentAnulacionNumeroDocumento.setVisibility(View.GONE);

            //Muestra la vista de verificacion del valor
            bodyContentAnulacionVerificacionValor.setVisibility(View.VISIBLE);

            //Actualiza el paso actual
            pasoTransaction++;

        } else {

            Toast.makeText(this, R.string.transaction_error_numero_documento, Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * Metodo para mostrar la información registrada
     */
    @Click(R.id.btnAnulacionTransactionVerificacionDatosBotonAceptar)
    public void verificarValor() {

        //Oculta la vista de verificacion de valor
        bodyContentAnulacionVerificacionValor.setVisibility(View.GONE);

        //Muestra la vista de deslizar la tarjeta
        bodyContentAnulacionDesliceTarjeta.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoTransaction++;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                deslizarTarjeta();
            }
        }, 100);


    }


    /**
     * Metodo que imprime la copia para el cliente
     */
    @Click(R.id.btnAnulacionTransactionExitosaBotonImprimir)
    public void imprimirRecibo() {

        //Imprime el recibo
        anulacionScreenPresenter.imprimirRecibo(this, this.getResources().getString(
                R.string.recibo_copia_cliente));

    }


    /**
     * Metodo para mostrar la orden de deslizar la tarjeta
     */
    public void deslizarTarjeta() {


        //Obtiene la lectura de la banda magnetica
        String[] magneticHandler = new MagneticHandler().readMagnetic();

        //Determina si la lectura fue correcta
        if (magneticHandler != null) {

            //Limpia el formato de la lectura
            String numeroTarjeta = magneticHandler[1]
                    .replace(";", "")
                    .replace("!", "")
                    .replace("#", "")
                    .replace("$", "")
                    .replace("&", "")
                    .replace("/", "")
                    .replace("|", "")
                    .replace("(", "")
                    .replace(")", "")
                    .replace("=", "")
                    .replace("?", "")
                    .replace("¿", "")
                    .replace("¿", "")
                    .replace("¡", "")
                    .replace("*", "")
                    .replace("{", "")
                    .replace("}", "")
                    .replace("[", "")
                    .replace("]", "")
                    .replace(",", "")
                    .replace(".", "")
                    .replace("-", "")
                    .replace("_", "")
                    .replace("%", "");

            //Registra el valor del numero de tarjeta en el modelo de la transaccion
            modelTransaccion.setNumero_tarjeta(numeroTarjeta);

            //En caso de la lectura correcta se continua el proceso
            lecturaTarjetaCorrecta();

        } else {

            //En caso de la lectura erronea se muestra la pantalla de error
            lecturaTarjetaErronea();

        }

    }

    /**
     * Metodo para mostrar la lectura correcta de tarjeta
     */
    private void lecturaTarjetaCorrecta() {

        //Oculta la vista de deslizar la tarjeta
        bodyContentAnulacionDesliceTarjeta.setVisibility(View.GONE);

        //Muestra la vista de contraseña de usuario
        bodyContentAnulacionPassUsuario.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoTransaction++;
    }

    /**
     * Metodo para mostrar la lectura erronea de tarjeta
     */
    private void lecturaTarjetaErronea() {

        //Oculta la vista de deslizar tarjeta
        bodyContentAnulacionDesliceTarjeta.setVisibility(View.GONE);

        //Muestra la vista de contraseña de usuario
        bodyContentAnulacionLecturaIncorrecta.setVisibility(View.VISIBLE);

    }

    /**
     * Metodo para registrar la contraseña del usuario
     */
    @Click(R.id.btnAnulacionTransactionClaveUsuarioBotonAceptar)
    public void registrarPassUsuario() {

        //Se obtiene el texto de la contraseña
        String passwordUser = edtAnulacionTransactionClaveUsuarioContenidoClave.getText().toString();

        //Vacia la caja de contraseña
        edtAnulacionTransactionClaveUsuarioContenidoClave.setText("");

        //La contraseña debe tener 4 caracteres exactos
        if (passwordUser.length() == 4) {

            //Se registra la contraseña en el modelo
            modelTransaccion.setClave(passwordUser);

            //TODO: agregar las diferentes encriptaciones
            //Se registra el tipo de encriptacion en el modelo
            modelTransaccion.setTipo_encriptacion(Transaccion.CODIGO_ENCR_NO_ENCRIPTADO);

            //TODO: agregar los diferentes tipos de productos
            //Se registra el tipo de producto en el modelo
            modelTransaccion.setTipo_servicio(Transaccion.CODIGO_PRODUCTO_CUPO_ROTATIVO);

            //Actualiza el paso actual
            pasoTransaction++;

            //Mostrar la barra de progreso
            showProgress();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    anulacionScreenPresenter.registrarTransaccion(AnulacionScreenActivity.this, modelTransaccion);
                }
            }, 100);

        } else {

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_format_clave_usuario, Toast.LENGTH_SHORT).show();

        }

    }

}
