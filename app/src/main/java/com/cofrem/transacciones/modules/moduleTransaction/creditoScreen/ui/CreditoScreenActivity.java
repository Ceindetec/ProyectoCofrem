package com.cofrem.transacciones.modules.moduleTransaction.creditoScreen.ui;

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
import com.cofrem.transacciones.modules.moduleTransaction.creditoScreen.CreditoScreenPresenter;
import com.cofrem.transacciones.modules.moduleTransaction.creditoScreen.CreditoScreenPresenterImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import static android.view.KeyEvent.KEYCODE_ENTER;

@EActivity(R.layout.activity_transaction_credito_screen)
public class CreditoScreenActivity extends Activity implements CreditoScreenView {

    /*
      #############################################################################################
      Declaracion de componentes y variables
      #############################################################################################
     */

    final static int PASO_VALOR_CONSUMO = 0;
    final static int PASO_NUMERO_DOCUMENTO = 1;
    final static int PASO_VERIFICACION_VALOR = 2;
    final static int PASO_DESLICE_TARJETA = 3;
    final static int PASO_CLAVE_USUARIO = 4;
    final static int PASO_TRANSACCION_EXITOSA = 5;
    /**
     * Declaracion de los Contoles
     */

    //Elementos del header
    @ViewById
    TextView txvHeaderIdDispositivo;
    @ViewById
    TextView txvHeaderIdPunto;
    @ViewById
    TextView txvHeaderEstablecimiento;
    @ViewById
    TextView txvHeaderPunto;
    //Elementos del modulo
    @ViewById
    RelativeLayout bodyContentCreditoValorCompra;
    @ViewById
    RelativeLayout bodyContentCreditoNumeroDocumento;
    @ViewById
    RelativeLayout bodyContentCreditoVerificacionValor;
    @ViewById
    RelativeLayout bodyContentCreditoDesliceTarjeta;
    @ViewById
    RelativeLayout bodyContentCreditoLecturaIncorrecta;
    @ViewById
    RelativeLayout bodyContentCreditoPassUsuario;
    @ViewById
    RelativeLayout bodyContentCreditoTransaccionExitosa;
    @ViewById
    RelativeLayout bodyContentCreditoTransaccionErronea;
    @ViewById
    FrameLayout frlPgbHldTransactionCredito;
    //Paso transaction_credito_paso_valor_compra
    @ViewById
    EditText edtCreditoTransactionValorCompraValor;
    //Paso transaction_credito_paso_numero_documento
    @ViewById
    EditText edtCreditoTransactionNumeroDocumentoValor;
    //Paso transaction_credito_paso_verificacion_valor
    @ViewById
    TextView txvCreditoTransactionVerificacionDatosValorCantidad;
    @ViewById
    TextView txvCreditoTransactionVerificacionDatosNumeroDocumento;
    //Paso transaction_credito_paso_deslice_tarjeta
    @ViewById
    TextView txvCreditoTransactionDesliceTarjetaValorCantidad;
    @ViewById
    TextView txvCreditoTransactionDesliceTarjetaNumeroDocumento;
    //Paso transaction_credito_paso_clave_usuario
    @ViewById
    EditText edtCreditoTransactionClaveUsuarioContenidoClave;
    //Paso content_transaction_credito_paso_transaccion_error
    @ViewById
    TextView txvCreditoTransactionErrorDetalleTexto;
    //Model que almacena la transaccion actual
    Transaccion modelTransaccion = new Transaccion();
    //Pasos definidos
    int pasoTransaccion = 0; //Define el paso actual
    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    //Instanciamiento de la interface SaldoScreenPresenter
    private CreditoScreenPresenter creditoScreenPresenter;


    /**
     * #############################################################################################
     * Constructor  de  la clase
     * #############################################################################################
     */
    @AfterViews
    void MainInit() {

        //Instanciamiento e inicializacion del presentador
        creditoScreenPresenter = new CreditoScreenPresenterImpl(this);

        //Llamada al metodo onCreate del presentador para el registro del bus de datos
        creditoScreenPresenter.onCreate();

        //Metodo que oculta por defecto los include de la vista
        inicializarOcultamientoVistas();

        // Metodo para colocar la orientacion de la app
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Coloca la informacion del encabezado
        setInfoHeader();

        //Inicializa el paso del valor del consumo
        pasoTransaccion = PASO_VALOR_CONSUMO;

        //Se registra consumo como el tipo de transaccion
        modelTransaccion.setTipo_transaccion(Transaccion.CODIGO_TIPO_TRANSACCION_CONSUMO);

        //Primera ventana visible
        bodyContentCreditoValorCompra.setVisibility(View.VISIBLE);
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
        creditoScreenPresenter.onDestroy();
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

                switch (pasoTransaccion) {

                    case PASO_VALOR_CONSUMO:
                        //Metodo para registrar el valor del consumo
                        registrarValorConsumo();
                        break;

                    case PASO_NUMERO_DOCUMENTO:
                        //Metodo para registrar el numero de documento
                        registrarNumeroDocumento();
                        break;

                    case PASO_VERIFICACION_VALOR:
                        //Metodo para mostrar la información registrada
                        verificarValor();
                        break;

                    case PASO_DESLICE_TARJETA:
                        //Metodo para mostrar la orden de deslizar la tarjeta
                        deslizarTarjeta();
                        break;

                    case PASO_CLAVE_USUARIO:
                        //Metodo para registrar la contraseña del usuario
                        registrarClaveUsuario();
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

        switch (pasoTransaccion) {

            case PASO_VALOR_CONSUMO:
                //Vacia la caja del valor del consumo
                edtCreditoTransactionValorCompraValor.setText("");
                break;

            case PASO_NUMERO_DOCUMENTO:
                //Vacia la caja del valor del consumo
                edtCreditoTransactionNumeroDocumentoValor.setText("");
                break;

            case PASO_VERIFICACION_VALOR:

                break;

            case PASO_DESLICE_TARJETA:

                break;

            case PASO_CLAVE_USUARIO:
                //Vacia la caja del valor de la clave de usuario
                edtCreditoTransactionClaveUsuarioContenidoClave.setText("");
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
     * Metodo para manejar la transaccion exitosa
     */
    public void handleTransaccionSuccess() {

        //Oculta la barra de progreso
        hideProgress();

        //Oculta la vista del Host de conexion
        bodyContentCreditoPassUsuario.setVisibility(View.GONE);

        //Muestra la vista del Port de conexion
        bodyContentCreditoTransaccionExitosa.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoTransaccion++;

    }

    /**
     * Metodo para manejar la conexion del Web Service Erronea
     */
    @Override
    public void handleTransaccionWSConexionError() {

        //Oculta la barra de progreso
        hideProgress();

        //Oculta la vista del Host de conexion
        bodyContentCreditoPassUsuario.setVisibility(View.GONE);

        //Muestra la vista del Port de conexion
        bodyContentCreditoTransaccionErronea.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoTransaccion++;

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

        //Oculta la vista del Host de conexion
        bodyContentCreditoPassUsuario.setVisibility(View.GONE);

        //Muestra la vista del Port de conexion
        bodyContentCreditoTransaccionErronea.setVisibility(View.VISIBLE);

    }

    /**
     * Metodo para manejar la orden de imprimir recibo exitosa
     */
    @Override
    public void handleImprimirReciboSuccess() {

        //???
        regresarDesdeImpimirRecibo(1000);

    }

    /**
     * Metodo para manejar la orden de imprimir recibo Error
     */
    @Override
    public void handleImprimirReciboError(String errorMessage) {

        //Muestra el mensaje de error de formato de la contraseña
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

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
        frlPgbHldTransactionCredito.setVisibility(View.VISIBLE);
        frlPgbHldTransactionCredito.bringToFront();
        frlPgbHldTransactionCredito.invalidate();
    }

    /**
     * Metodo para ocultar la barra de progreso
     */
    private void hideProgress() {
        //Oculta la barra de progreso
        frlPgbHldTransactionCredito.setVisibility(View.GONE);
    }

    /**
     * Metodo que oculta por defecto los include de la vista
     */
    private void inicializarOcultamientoVistas() {

        bodyContentCreditoValorCompra.setVisibility(View.GONE);
        bodyContentCreditoNumeroDocumento.setVisibility(View.GONE);
        bodyContentCreditoVerificacionValor.setVisibility(View.GONE);
        bodyContentCreditoDesliceTarjeta.setVisibility(View.GONE);
        bodyContentCreditoLecturaIncorrecta.setVisibility(View.GONE);
        bodyContentCreditoPassUsuario.setVisibility(View.GONE);
        bodyContentCreditoTransaccionExitosa.setVisibility(View.GONE);
        bodyContentCreditoTransaccionErronea.setVisibility(View.GONE);
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
    @LongClick({R.id.edtCreditoTransactionValorCompraValor,
            R.id.edtCreditoTransactionNumeroDocumentoValor,
            R.id.edtCreditoTransactionClaveUsuarioContenidoClave
    })
    @Click({R.id.edtCreditoTransactionValorCompraValor,
            R.id.edtCreditoTransactionNumeroDocumentoValor,
            R.id.edtCreditoTransactionClaveUsuarioContenidoClave
    })
    @Touch({R.id.edtCreditoTransactionValorCompraValor,
            R.id.edtCreditoTransactionNumeroDocumentoValor,
            R.id.edtCreditoTransactionClaveUsuarioContenidoClave
    })
    public void hideKeyBoard() {

        //TODO:VERIFICAR QUE EL TECLADO SE ESTA OCULTANDO
        //Oculta el teclado
        KeyBoard.hide(this);

    }

    /**
     * Metodo para regresar a la ventana de transaccion
     */
    @Click({R.id.btnCreditoTransactionValorCompraBotonCancelar,
            R.id.btnCreditoTransactionNumeroDocumentoBotonCancelar,
            R.id.btnCreditoTransactionVerificacionDatosBotonCancelar,
            R.id.btnCreditoTransactionLecturaIncorrectaBotonSalir,
            R.id.btnCreditoTransactionClaveUsuarioBotonCancelar,
            R.id.btnCreditoTransactionErrorBotonSalir
    })
    public void navigateToTransactionScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(CreditoScreenActivity.this, TransactionScreenActivity_.class);
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
    @Click({R.id.btnCreditoTransactionExitosaBotonSalir})
    public void navigateToMainScreen() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(CreditoScreenActivity.this, MainScreenActivity_.class);

                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 100);

    }

    /**
     * Metodo para registrar el valor del consumo
     */
    @Click(R.id.btnCreditoTransactionValorCompraBotonAceptar)
    public void registrarValorConsumo() {

        //Se obtiene el texto del valor del consumo
        String valorCompra = edtCreditoTransactionValorCompraValor.getText().toString();

        //Vacia la caja del valor del consumo
        edtCreditoTransactionValorCompraValor.setText("");

        //El valor del consumo debe estar entre 1 y 3'000.000
        if (valorCompra.length() > 0 && Integer.parseInt(valorCompra) >= 1 && Integer.parseInt(valorCompra) <= 3000000) {

            //Registra el valor del consumo en el modelo de la transaccion
            modelTransaccion.setValor(Integer.parseInt(valorCompra));

            //Oculta la vista del Valor del consumo
            bodyContentCreditoValorCompra.setVisibility(View.GONE);

            //Muestra la vista del Numero de documento
            bodyContentCreditoNumeroDocumento.setVisibility(View.VISIBLE);

            //Actualiza el paso actual
            pasoTransaccion++;

        } else if (valorCompra.length() == 0) {

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_valor, Toast.LENGTH_SHORT).show();

        } else if (Integer.parseInt(valorCompra) < 1) {

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_valor_monto_minimo, Toast.LENGTH_SHORT).show();

        } else if (Integer.parseInt(valorCompra) > 3000000) {

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_valor_monto_maximo, Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * Metodo para registrar el numero de documento
     */
    @Click(R.id.btnCreditoTransactionNumeroDocumentoBotonAceptar)
    public void registrarNumeroDocumento() {

        //Se obtiene el texto de la contraseña
        String numeroDocumento = edtCreditoTransactionNumeroDocumentoValor.getText().toString();

        //Vacia la caja de numero de documento
        edtCreditoTransactionNumeroDocumentoValor.setText("");

        //La longitud del numero de documento debe ser mayor a cero
        if (numeroDocumento.length() > 0) {

            //Registra el valor del numero de documento en el modelo de transaccion
            modelTransaccion.setNumero_documento(numeroDocumento);

            //Registra el valor del consumo en la vista de verificacion
            txvCreditoTransactionVerificacionDatosValorCantidad.setText(
                    PrintRow.numberFormat(modelTransaccion.getValor())
            );

            //Registra el numero de documento en la vista de verificacion
            txvCreditoTransactionVerificacionDatosNumeroDocumento.setText(
                    String.valueOf(modelTransaccion.getNumero_documento())
            );

            //Registra el valor del consumo en la vista de deslizar tarjeta
            txvCreditoTransactionDesliceTarjetaValorCantidad.setText(
                    PrintRow.numberFormat(modelTransaccion.getValor())
            );

            //Registra el numero de documento en la vista de deslizar tarjeta
            txvCreditoTransactionDesliceTarjetaNumeroDocumento.setText(
                    String.valueOf(modelTransaccion.getNumero_documento())
            );

            //Oculta la vista del numero de documento
            bodyContentCreditoNumeroDocumento.setVisibility(View.GONE);

            //Muestra la vista de verificacion del valor
            bodyContentCreditoVerificacionValor.setVisibility(View.VISIBLE);

            //Actualiza el paso actual
            pasoTransaccion++;

        } else {

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_numero_documento, Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * Metodo para mostrar la información registrada
     */
    @Click(R.id.btnCreditoTransactionVerificacionDatosBotonAceptar)
    public void verificarValor() {

        //Oculta la vista de verificacion de valor
        bodyContentCreditoVerificacionValor.setVisibility(View.GONE);

        //Muestra la vista de deslizar tarjeta
        bodyContentCreditoDesliceTarjeta.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoTransaccion++;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                deslizarTarjeta();
            }
        }, 100);

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

        //Oculta la vista de deslizar tarjeta
        bodyContentCreditoDesliceTarjeta.setVisibility(View.GONE);

        //Muestra la vista de contraseña de usuario
        bodyContentCreditoPassUsuario.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoTransaccion++;
    }

    /**
     * Metodo para mostrar la lectura erronea de tarjeta
     */
    private void lecturaTarjetaErronea() {

        //Oculta la vista de deslizar tarjeta
        bodyContentCreditoDesliceTarjeta.setVisibility(View.GONE);

        //Muestra la vista de lectura de tarjeta incorrecta
        bodyContentCreditoLecturaIncorrecta.setVisibility(View.VISIBLE);

    }

    /**
     * Metodo para registrar la contraseña del usuario
     */
    @Click(R.id.btnCreditoTransactionClaveUsuarioBotonAceptar)
    public void registrarClaveUsuario() {

        //Se obtiene el texto de la contraseña
        String passwordUser = edtCreditoTransactionClaveUsuarioContenidoClave.getText().toString();

        //Vacia la caja de contraseña
        edtCreditoTransactionClaveUsuarioContenidoClave.setText("");

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
            pasoTransaccion++;

            //Mostrar la barra de progreso
            showProgress();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    creditoScreenPresenter.registrarTransaccion(CreditoScreenActivity.this, modelTransaccion);
                }
            }, 100);

        } else {

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_format_clave_usuario, Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * Metodo que imprime la copia para el cliente
     */
    @Click(R.id.btnCreditoTransactionExitosaBotonImprimir)
    public void imprimirRecibo() {

        //Imprime el recibo
        creditoScreenPresenter.imprimirRecibo(this, this.getResources().getString(
                R.string.recibo_copia_cliente));

    }

    /**
     * ??? Pineda
     *
     * @param timer temporizador
     */
    public void regresarDesdeImpimirRecibo(int timer) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToTransactionScreen();
            }
        }, timer);
    }

}
