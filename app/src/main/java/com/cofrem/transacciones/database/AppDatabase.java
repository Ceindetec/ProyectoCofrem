package com.cofrem.transacciones.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.cofrem.transacciones.lib.MD5;
import com.cofrem.transacciones.models.ConfigurationPrinter;
import com.cofrem.transacciones.models.Configurations;
import com.cofrem.transacciones.models.Establishment;
import com.cofrem.transacciones.models.InfoHeaderApp;
import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.ConexionEstablecimiento;
import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.Establecimiento;
import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.InformacionEstablecimiento;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionTransaccion;
import com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen.events.AnulacionScreenEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Clase que maneja:
 * - DML de la base de datos
 * - instancia general de acceso a datos
 */
public final class AppDatabase extends SQLiteOpenHelper {

    //Instancia singleton
    private static AppDatabase singleton;

    /**
     * Constructor de la clase
     * Crea la base de datos si no existe
     *
     * @param context instancia desde la que se llaman los metodos
     */
    private AppDatabase(Context context) {
        super(context,
                DatabaseManager.DatabaseApp.DATABASE_NAME,
                null,
                DatabaseManager.DatabaseApp.DATABASE_VERSION);
    }

    /**
     * Retorna la instancia unica del singleton
     *
     * @param context contexto donde se ejecutarán las peticiones
     * @return Instancia
     */
    public static synchronized AppDatabase getInstance(Context context) {

        if (singleton == null) {
            singleton = new AppDatabase(context.getApplicationContext());
        }
        return singleton;
    }

    /**
     * Metodo ejecutado en el evento de la instalacion de la aplicacion
     * Crea las tablas necesarias para el funcionamiento de la aplicacion
     *
     * @param db Database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DatabaseManager.TableProducto.CREATE_TABLE_PRODUCTO);
        db.execSQL(DatabaseManager.TableTransacciones.CREATE_TABLE_TRANSACCIONES);
        db.execSQL(DatabaseManager.TableEstablecimiento.CREATE_TABLE_ESTABLECIMIENTO);
        db.execSQL(DatabaseManager.TableConfiguracionConexion.CREATE_TABLE_CONFIGURACION_CONEXION);
        db.execSQL(DatabaseManager.TableConfiguracionAcceso.CREATE_TABLE_CONFIGURACION_ACCESO);
        db.execSQL(DatabaseManager.TableConfigurationPrinter.CREATE_TABLE_CONFIGURACION_PRINTER);

    }

    /**
     * Metodo ejecutado en la actualizacion de la aplicacion
     *
     * @param db         Database
     * @param oldVersion Version Anterior
     * @param newVersion Version Actual
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        // Drop todas las tablas 
        db.execSQL(DatabaseManager.TableProducto.DROP_TABLE_PRODUCTO);
        db.execSQL(DatabaseManager.TableTransacciones.DROP_TABLE_TRANSACCIONES);
        db.execSQL(DatabaseManager.TableEstablecimiento.DROP_TABLE_ESTABLECIMIENTO);
        db.execSQL(DatabaseManager.TableConfiguracionConexion.DROP_TABLE_CONFIGURACION_CONEXION);
        db.execSQL(DatabaseManager.TableConfiguracionAcceso.DROP_TABLE_CONFIGURACION_ACCESO);
        db.execSQL(DatabaseManager.TableConfigurationPrinter.DROP_TABLE_CONFIGURACION_PRINTER);

        onCreate(db);
    }

    /*
      #############################################################################################
      AREA CONSULTAS GENERALES
      #############################################################################################
     */

    /**
     * Metodo para Obtener el codigo de la terminal
     *
     * @return String codigo de la terminal
     */
    public String obtenerCodigoTerminal() {

        String codigoTerminal = "";

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT " + DatabaseManager.TableConfiguracionConexion.COLUMN_CONFIGURACION_CONEXION_DISPOSITIVO +
                        " FROM " + DatabaseManager.TableConfiguracionConexion.TABLE_NAME_CONFIGURACION_CONEXION +
                        " WHERE " + DatabaseManager.TableConfiguracionConexion.COLUMN_CONFIGURACION_CONEXION_ESTADO + " = 1 " +
                        " LIMIT 1", null
        );

        if (cursorQuery.moveToFirst()) {
            codigoTerminal = cursorQuery.getString(0);
        }

        cursorQuery.close();

        return codigoTerminal;
    }

    /**
     * Metodo para Obtener ultima  transaccion
     *
     * @return boolean informacion del header
     */
    public boolean obtenerInfoHeader() {

        boolean transaction = false;

        String codigoTerminal = obtenerCodigoTerminal();

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT " + DatabaseManager.TableEstablecimiento.COLUMN_ESTABLECIMIENTO_CODIGO + " , " +
                        DatabaseManager.TableEstablecimiento.COLUMN_ESTABLECIMIENTO_RAZON_SOCIAL + " , " +
                        DatabaseManager.TableEstablecimiento.COLUMN_ESTABLECIMIENTO_NOMBRE +
                        " FROM " + DatabaseManager.TableEstablecimiento.TABLE_NAME_ESTABLECIMIENTO +
                        " ORDER BY " + DatabaseManager.TableEstablecimiento.COLUMN_ESTABLECIMIENTO_REGISTRO + " ASC " +
                        " LIMIT 1", null
        );

        if (cursorQuery.moveToFirst()) {
            InfoHeaderApp.getInstance().setIdDispositivo(codigoTerminal);
            InfoHeaderApp.getInstance().setIdPunto(cursorQuery.getString(0));
            InfoHeaderApp.getInstance().setNombreEstablecimiento(cursorQuery.getString(1));
            InfoHeaderApp.getInstance().setNombrePunto(cursorQuery.getString(2));

            transaction = true;
        }

        cursorQuery.close();

        return transaction;
    }

    /*
      #############################################################################################
      AREA REGISTROS INICIALES
      #############################################################################################
     */

    /**
     * Metodo para manejar la informacion que se insertara en el registro inicial de productos en la Base de Datos
     *
     * @return Boolean estado del registro de los productos iniciales
     */
    public boolean handlerRegistroInicialProductos() {

        // Eliminacion de registros anteriores en la base de datos
        getWritableDatabase().delete(
                DatabaseManager.TableProducto.TABLE_NAME_PRODUCTO,
                "",
                null
        );

        // Almacena los valores a insertar
        insertRegistroInicialProductos(1, "CUPO ROTATIVO", "Producto cupo rotativo Cofrem");
        insertRegistroInicialProductos(2, "BONO DE BIENESTAR", "Producto bono de bienestar Cofrem");
        insertRegistroInicialProductos(3, "TARJETA REGALO", "Producto tarjeta regalo Cofrem");
        insertRegistroInicialProductos(4, "BONO ALIMENTARIO FOSFEC", "Producto bono alimentario fosfec Cofrem");
        insertRegistroInicialProductos(5, "CUOTA MONETARIA FOSFEC", "Producto cuota monetaria fosfec Cofrem");
        insertRegistroInicialProductos(6, "CUOTA MONETARIA", "Producto cuota monetaria Cofrem");

        return true;

    }

    /**
     * Metodo para insertar registro inicial de productos en la Base de Datos
     *
     * @param producto_id          int ID del producto
     * @param producto_nombre      String Nombre del producto
     * @param producto_descripcion String Descripcion del producto
     * @return Boolean estado de la transaccion del registro inicial de los productos
     */
    private boolean insertRegistroInicialProductos(int producto_id,
                                                   String producto_nombre,
                                                   String producto_descripcion) {

        boolean transaction = false;

        long conteo = 0;

        // Inicializacion de la variable de contenidos del registro
        ContentValues contentValues = new ContentValues();

        // Almacena los valores a insertar
        contentValues.put(DatabaseManager.TableProducto.COLUMN_PRODUCTO_ID, producto_id);
        contentValues.put(DatabaseManager.TableProducto.COLUMN_PRODUCTO_NOMBRE, producto_nombre);
        contentValues.put(DatabaseManager.TableProducto.COLUMN_PRODUCTO_DESCRIPCION, producto_descripcion);
        contentValues.put(DatabaseManager.TableProducto.COLUMN_PRODUCTO_REGISTRO, getDateTime());
        contentValues.put(DatabaseManager.TableProducto.COLUMN_PRODUCTO_ESTADO, 1);

        try {

            getWritableDatabase().insert(
                    DatabaseManager.TableProducto.TABLE_NAME_PRODUCTO,
                    null,
                    contentValues
            );

            conteo = obtenerConteoCambios();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        if (conteo == 1) {
            transaction = true;
        }

        return transaction;
    }

    /**
     * Metodo para insertar registro inicial en la Base de Datos de la clave de acceso a dispositivo
     * Usado en:
     * Inicio por primera vez de la APP
     *
     * @return Boolean estado de la transaccion del registro inicial de la configuracion de acceso
     */
    public boolean insertRegistroInicialConfiguracionAcceso() {

        boolean transaction = false;

        long conteo = 0;

        // Inicializacion de la variable de contenidos del registro
        ContentValues contentValues = new ContentValues();

        // Almacena los valores a insertar
        contentValues.put(DatabaseManager.TableConfiguracionAcceso.COLUMN_CONFIGURACION_ACCESO_CLAVE_TECNICA, MD5.crypt("8717"));
        contentValues.put(DatabaseManager.TableConfiguracionAcceso.COLUMN_CONFIGURACION_ACCESO_REGISTRO, getDateTime());
        contentValues.put(DatabaseManager.TableConfiguracionAcceso.COLUMN_CONFIGURACION_ACCESO_ESTADO, 1);

        try {

            getWritableDatabase().insert(
                    DatabaseManager.TableConfiguracionAcceso.TABLE_NAME_CONFIGURACION_ACCESO,
                    null,
                    contentValues
            );

            conteo = obtenerConteoCambios();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        if (conteo == 1) {

            transaction = true;

        }

        return transaction;

    }

    /**
     * Metodo para insertar registro de transacciones en la Base de Datos
     *
     * @param informacionTransaccion Informacion de la transaccion realizada
     * @return Boolean estado del registro de la transaccion
     */
    public boolean insertRegistroTransaction(InformacionTransaccion informacionTransaccion, int tipoTransaccion) {

        boolean transaction = false;

        long conteo = 0;

        // Inicializacion de la variable de contenidos del registro
        ContentValues contentValues = new ContentValues();

        // Almacena los valores a insertar
        contentValues.put(
                DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_PRODUCTO_ID,
                obtenerProductoIdByNombre(informacionTransaccion.getDetalleTipoServicio())
        );

        contentValues.put(
                DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_NUMERO_CARGO,
                informacionTransaccion.getNumeroAprobacion()
        );

        contentValues.put(
                DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_NUMERO_TARJETA,
                informacionTransaccion.getNumeroTarjeta()
        );

        contentValues.put(DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_VALOR,
                informacionTransaccion.getValor()
        );

        contentValues.put(DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_TIPO_TRANSACCION,
                tipoTransaccion
        );

        contentValues.put(DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_CEDULA_USUARIO,
                informacionTransaccion.getCedulaUsuario()
        );

        contentValues.put(DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_NOMBRE_USUARIO,
                informacionTransaccion.getNombreAfiliado()
        );

        contentValues.put(DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_FECHA_SERVER,
                informacionTransaccion.getFecha()
        );

        contentValues.put(DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_HORA_SERVER,
                informacionTransaccion.getHora()
        );

        contentValues.put(
                DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_REGISTRO,
                getDateTime());

        contentValues.put(
                DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_ESTADO,
                1);

        try {

            getWritableDatabase().insert(
                    DatabaseManager.TableTransacciones.TABLE_NAME_TRANSACCIONES,
                    null,
                    contentValues
            );

            conteo = obtenerConteoCambios();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        if (conteo == 1) {
            transaction = true;
        }

        return transaction;

    }

    /**
     * Metodo para Obtener el id del producto segun su nombre extraido del web service
     *
     * @param nombreProducto String nombre del producto a consultar
     * @return int id del producto consultado por nombre
     */
    private int obtenerProductoIdByNombre(String nombreProducto) {

        int producto_id;

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT " + DatabaseManager.TableProducto.COLUMN_PRODUCTO_ID + " AS ID " +
                        " FROM " + DatabaseManager.TableProducto.TABLE_NAME_PRODUCTO +
                        " WHERE " + DatabaseManager.TableProducto.COLUMN_PRODUCTO_NOMBRE + " = '" + nombreProducto + "'" +
                        " LIMIT 1", null
        );

        cursorQuery.moveToFirst();

        producto_id = cursorQuery.getInt(0);

        cursorQuery.close();

        return producto_id;
    }


    /**
     * Metodo para Obtener valor de la transaccion por el numero de cargo
     *
     * @param numeroCargo
     * @return
     */
    public int obtenerValorTransaccion(String numeroCargo) {

        Log.i("AppDatabase cargo", numeroCargo);

        int valorTransaccion = AnulacionScreenEvent.VALOR_TRANSACCION_NO_VALIDO;

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT  " +
                        DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_VALOR +
                        " FROM " + DatabaseManager.TableTransacciones.TABLE_NAME_TRANSACCIONES +
                        " WHERE " +
                        DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_NUMERO_CARGO + " = '" + numeroCargo + "' AND " +
                        DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_TIPO_TRANSACCION + " = " + Transaccion.TIPO_TRANSACCION_CONSUMO
                , null
        );
        if (cursorQuery.moveToFirst()) {

            valorTransaccion = cursorQuery.getInt(0);

        }

        cursorQuery.close();

        Log.i("AppDatabase valor", String.valueOf(valorTransaccion));

        return valorTransaccion;
    }

    /**
     * Metodo para insertar registro inicial en la Base de Datos de la configuracion para la impresora
     * Usado en:
     * Inicio por primera vez de la APP
     *
     * @return Boolean estado de la transaccion para el registro inicial de la configuracion de la impresora
     */
    public boolean insertRegistroInicialConfiguracionPrinter() {

        boolean transaction = false;

        long conteo = 0;

        // Inicializacion de la variable de contenidos del registro
        ContentValues contentValues = new ContentValues();

        // Almacena los valores a insertar
        contentValues.put(DatabaseManager.TableConfigurationPrinter.COLUMN_CONFIGURACION_PRINTER_FONT_SIZE, 2);
        contentValues.put(DatabaseManager.TableConfigurationPrinter.COLUMN_CONFIGURACION_PRINTER_ESTADO, 1);
        contentValues.put(DatabaseManager.TableConfigurationPrinter.COLUMN_CONFIGURACION_PRINTER_GRAY_LEVEL, 11);

        try {

            getWritableDatabase().insert(
                    DatabaseManager.TableConfigurationPrinter.TABLE_NAME_CONFIGURACION_PRINTER,
                    null,
                    contentValues
            );

            conteo = obtenerConteoCambios();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        if (conteo == 1) {

            transaction = true;

        }

        return transaction;

    }


    /*
      #############################################################################################
      AREA MODULE CONFIGURACION
      #############################################################################################
     */

    /**
     * Metodo para validar si existe registro inicial en la Base de Datos de la clave de acceso a dispositivo
     * Usado en modulos:
     * - Configuracion
     *
     * @return int conteo de los registro de configuracion de acceso
     */
    public int conteoConfiguracionAcceso() {

        int count;

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT COUNT(1) FROM " +
                        DatabaseManager.TableConfiguracionAcceso.TABLE_NAME_CONFIGURACION_ACCESO +
                        " WHERE " + DatabaseManager.TableConfiguracionAcceso.COLUMN_CONFIGURACION_ACCESO_ESTADO + " = '1'",
                null
        );

        cursorQuery.moveToFirst();

        count = cursorQuery.getInt(0);

        cursorQuery.close();

        return count;

    }

    /**
     * Metodo para validar si existe registro inicial en la Base de Datos de la clave de acceso a dispositivo
     * Usado en modulos:
     * - Configuracion
     *
     * @param claveTecnica String clave para realizar
     * @return int conteo de registros de configuracion de acceso por clave tecnica
     */
    public int validarAccesoByClaveTecnica(String claveTecnica) {

        int count;

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT COUNT(1) FROM " +
                        DatabaseManager.TableConfiguracionAcceso.TABLE_NAME_CONFIGURACION_ACCESO +
                        " WHERE " + DatabaseManager.TableConfiguracionAcceso.COLUMN_CONFIGURACION_ACCESO_CLAVE_TECNICA + " = '" + claveTecnica + "' " +
                        " AND " + DatabaseManager.TableConfiguracionAcceso.COLUMN_CONFIGURACION_ACCESO_ESTADO + " = '1'",
                null
        );

        cursorQuery.moveToFirst();

        count = cursorQuery.getInt(0);

        cursorQuery.close();

        return count;

    }

    /**
     * Metodo para validar si existe registro inicial en la Base de Datos de la clave de acceso a dispositivo
     * Usado en modulos:
     * - Configuracion
     *
     * @param claveAdministracion String clave para realizar
     * @return int conteo de registros de configuracion de acceso por clave tecnica
     */
    public int validarAccesoByClaveAdministracion(String claveAdministracion) {

        int count;

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT COUNT(1) FROM " +
                        DatabaseManager.TableConfiguracionAcceso.TABLE_NAME_CONFIGURACION_ACCESO +
                        " WHERE " + DatabaseManager.TableConfiguracionAcceso.COLUMN_CONFIGURACION_ACCESO_CLAVE_ADMIN + " = '" + claveAdministracion + "' " +
                        " AND " + DatabaseManager.TableConfiguracionAcceso.COLUMN_CONFIGURACION_ACCESO_ESTADO + " = '1'",
                null
        );

        cursorQuery.moveToFirst();

        count = cursorQuery.getInt(0);

        cursorQuery.close();

        return count;

    }

    /**
     * Metodo para Obtener el conteo de los registros de la tabla ConfiguracionConexion
     * Usado en modulos:
     * - Configuracion
     *
     * @return int conteo de los registros
     */
    public int conteoConfiguracionConexion() {

        int count;

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT COUNT(1) FROM " +
                        DatabaseManager.TableConfiguracionConexion.TABLE_NAME_CONFIGURACION_CONEXION +
                        " WHERE " + DatabaseManager.TableConfiguracionConexion.COLUMN_CONFIGURACION_CONEXION_ESTADO + " = '1'",
                null
        );

        cursorQuery.moveToFirst();

        count = cursorQuery.getInt(0);

        cursorQuery.close();

        return count;
    }

    /**
     * Metodo para validar si existe registro inicial en la Base de Datos para la configuracion inical de la impresora
     * Usado en modulos:
     * - Configuracion
     *
     * @return int conteo de los registros de la configuracion de la impresora
     */
    public int conteoConfigurationPrinter() {

        int count;

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT COUNT(" + DatabaseManager.TableConfigurationPrinter.COLUMN_CONFIGURACION_PRINTER_FONT_SIZE + ") FROM " +
                        DatabaseManager.TableConfigurationPrinter.TABLE_NAME_CONFIGURACION_PRINTER,
                null
        );

        cursorQuery.moveToFirst();

        count = cursorQuery.getInt(0);

        cursorQuery.close();

        return count;

    }

    /**
     * Metodo para Obtener la configuracion de la impresora
     *
     * @return ConfigurationPrinter configuracion de la impresora registrada
     */
    public ConfigurationPrinter getConfigurationPrinter() {

        ConfigurationPrinter modelConfigurationPrinter = new ConfigurationPrinter();

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseManager.TableConfigurationPrinter.TABLE_NAME_CONFIGURACION_PRINTER +
                        " WHERE " + DatabaseManager.TableConfigurationPrinter.COLUMN_CONFIGURACION_PRINTER_ESTADO + " = '1'", null
        );

        if (cursorQuery.moveToFirst()) {
            modelConfigurationPrinter.setFont_size(cursorQuery.getInt(0));
            modelConfigurationPrinter.setGray_level(cursorQuery.getInt(2));

        }

        cursorQuery.close();

        return modelConfigurationPrinter;
    }


    /**
     * Metodo para insertar registro de la configuracion de la Impresora
     *
     * @param configurationPrinter ConfigurationPrinter informacion de la configuracion
     * @return Boolean estado de la transaccion
     */
    public boolean insertConfigurationPrinter(ConfigurationPrinter configurationPrinter) {

        boolean transaction = false;

        long conteo = 0;

        try {
            getWritableDatabase().beginTransaction();

            // Eliminacion de registros anteriores en la base de datos
            getWritableDatabase().delete(
                    DatabaseManager.TableConfigurationPrinter.TABLE_NAME_CONFIGURACION_PRINTER,
                    "",
                    null
            );

            // Inicializacion de la variable de contenidos del registro
            ContentValues contentValuesInsert = new ContentValues();

            // Almacena los valores a insertar
            contentValuesInsert.put(DatabaseManager.TableConfigurationPrinter.COLUMN_CONFIGURACION_PRINTER_FONT_SIZE, configurationPrinter.getFont_size());
            contentValuesInsert.put(DatabaseManager.TableConfigurationPrinter.COLUMN_CONFIGURACION_PRINTER_GRAY_LEVEL, configurationPrinter.getGray_level());
            contentValuesInsert.put(DatabaseManager.TableConfigurationPrinter.COLUMN_CONFIGURACION_PRINTER_ESTADO, 1);

            getWritableDatabase().insert(
                    DatabaseManager.TableConfigurationPrinter.TABLE_NAME_CONFIGURACION_PRINTER,
                    null,
                    contentValuesInsert
            );

            conteo = obtenerConteoCambios();

            getWritableDatabase().setTransactionSuccessful();

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            getWritableDatabase().endTransaction();

        }
        if (conteo == 1) {
            transaction = true;
        }

        return transaction;
    }

    /**
     * Metodo para insertar registro de la configuracion de la conexion
     *
     * @param configurations Configurations información de la configuración
     * @return Boolean estado de la transaccion
     */
    public boolean insertConfiguracionConexion(Configurations configurations) {

        boolean transaction = false;

        long conteo = 0;

        try {
            getWritableDatabase().beginTransaction();

            // Eliminacion de registros anteriores en la base de datos
            getWritableDatabase().delete(
                    DatabaseManager.TableConfiguracionConexion.TABLE_NAME_CONFIGURACION_CONEXION,
                    "",
                    null
            );

            // Inicializacion de la variable de contenidos del registro
            ContentValues contentValuesInsert = new ContentValues();

            // Almacena los valores a insertar
            contentValuesInsert.put(DatabaseManager.TableConfiguracionConexion.COLUMN_CONFIGURACION_CONEXION_HOST, configurations.getHost());
            contentValuesInsert.put(DatabaseManager.TableConfiguracionConexion.COLUMN_CONFIGURACION_CONEXION_PORT, configurations.getPort());
            contentValuesInsert.put(DatabaseManager.TableConfiguracionConexion.COLUMN_CONFIGURACION_CONEXION_DISPOSITIVO, configurations.getCodigoDispositivo());
            contentValuesInsert.put(DatabaseManager.TableConfiguracionConexion.COLUMN_CONFIGURACION_CONEXION_REGISTRO, getDateTime());
            contentValuesInsert.put(DatabaseManager.TableConfiguracionConexion.COLUMN_CONFIGURACION_CONEXION_ESTADO, 1);

            getWritableDatabase().insert(
                    DatabaseManager.TableConfiguracionConexion.TABLE_NAME_CONFIGURACION_CONEXION,
                    null,
                    contentValuesInsert
            );

            transaction = true;

            getWritableDatabase().setTransactionSuccessful();

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            getWritableDatabase().endTransaction();

        }
        return transaction;
    }

    /**
     * Metodo para borrar la configuracion de conexion en cas de error
     */
    public void deleteConfiguracionConexion() {

        // Eliminacion de registros anteriores en la base de datos
        getWritableDatabase().delete(
                DatabaseManager.TableConfiguracionConexion.TABLE_NAME_CONFIGURACION_CONEXION,
                "",
                null
        );
    }

    /**
     * Metodo para obtener el registro de la configuracion de la conexion
     *
     * @return String URL de la configuracion de la conexion
     */
    public String obtenerURLConfiguracionConexion() {

        String urlTransacciones = "";

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT " + DatabaseManager.TableConfiguracionConexion.COLUMN_CONFIGURACION_CONEXION_HOST +
                        " , " + DatabaseManager.TableConfiguracionConexion.COLUMN_CONFIGURACION_CONEXION_PORT +
                        " FROM " + DatabaseManager.TableConfiguracionConexion.TABLE_NAME_CONFIGURACION_CONEXION +
                        " WHERE " + DatabaseManager.TableConfiguracionConexion.COLUMN_CONFIGURACION_CONEXION_ESTADO + " = '1' " +
                        " LIMIT 1", null
        );

        if (cursorQuery.moveToFirst()) {
            urlTransacciones = cursorQuery.getString(0) + ":" + cursorQuery.getString(1);
        }

        cursorQuery.close();

        return urlTransacciones;

    }

    /**
     * Metodo para procesar la informacion del establecimiento traida desde el Web Service
     *
     * @param establecimiento Establishment informacion del establecimiento
     * @return Boolean estado de la transaccion de procesamiento de informacion de establecimiento
     */
    public boolean processInfoEstablecimiento(Establecimiento establecimiento) {

        boolean transaction = false;

        try {

            getWritableDatabase().beginTransaction();

            InformacionEstablecimiento informacionEstablecimiento = establecimiento.getInformacionEstablecimiento();

            ConexionEstablecimiento conexionEstablecimiento = establecimiento.getConexionEstablecimiento();


            // Eliminacion de registros anteriores en la base de datos
            getWritableDatabase().delete(
                    DatabaseManager.TableEstablecimiento.TABLE_NAME_ESTABLECIMIENTO,
                    "",
                    null
            );

            // Inicializacion de la variable de contenidos del registro
            ContentValues contentValuesInsertInformacionDispositivo = new ContentValues();

            // Almacena los valores a insertar
            contentValuesInsertInformacionDispositivo.put(DatabaseManager.TableEstablecimiento.COLUMN_ESTABLECIMIENTO_CODIGO, informacionEstablecimiento.getCodigoPunto());
            contentValuesInsertInformacionDispositivo.put(DatabaseManager.TableEstablecimiento.COLUMN_ESTABLECIMIENTO_NOMBRE, informacionEstablecimiento.getNombrePunto());
            contentValuesInsertInformacionDispositivo.put(DatabaseManager.TableEstablecimiento.COLUMN_ESTABLECIMIENTO_DIRECCION, informacionEstablecimiento.getDireccionPunto());
            contentValuesInsertInformacionDispositivo.put(DatabaseManager.TableEstablecimiento.COLUMN_ESTABLECIMIENTO_CIUDAD, informacionEstablecimiento.getCiudadPunto());
            contentValuesInsertInformacionDispositivo.put(DatabaseManager.TableEstablecimiento.COLUMN_ESTABLECIMIENTO_NIT, informacionEstablecimiento.getNitComercio());
            contentValuesInsertInformacionDispositivo.put(DatabaseManager.TableEstablecimiento.COLUMN_ESTABLECIMIENTO_RAZON_SOCIAL, informacionEstablecimiento.getRazonSocialComercio());
            contentValuesInsertInformacionDispositivo.put(DatabaseManager.TableEstablecimiento.COLUMN_ESTABLECIMIENTO_REGISTRO, getDateTime());
            contentValuesInsertInformacionDispositivo.put(DatabaseManager.TableEstablecimiento.COLUMN_ESTABLECIMIENTO_ESTADO, 1);

            getWritableDatabase().insert(
                    DatabaseManager.TableEstablecimiento.TABLE_NAME_ESTABLECIMIENTO,
                    null,
                    contentValuesInsertInformacionDispositivo
            );

            if (!conexionEstablecimiento.getClaveTecnivo().isEmpty()) {

                // Eliminacion de registros anteriores en la base de datos
                getWritableDatabase().delete(
                        DatabaseManager.TableConfiguracionAcceso.TABLE_NAME_CONFIGURACION_ACCESO,
                        "",
                        null
                );

                // Inicializacion de la variable de contenidos del registro de acceso
                ContentValues contentValuesInformacionAcceso = new ContentValues();

                // Almacena los valores a actualizar
                contentValuesInformacionAcceso.put(DatabaseManager.TableConfiguracionAcceso.COLUMN_CONFIGURACION_ACCESO_CLAVE_TECNICA, conexionEstablecimiento.getClaveTecnivo());
                contentValuesInformacionAcceso.put(DatabaseManager.TableConfiguracionAcceso.COLUMN_CONFIGURACION_ACCESO_CLAVE_ADMIN, conexionEstablecimiento.getClaveComercio());
                contentValuesInformacionAcceso.put(DatabaseManager.TableConfiguracionAcceso.COLUMN_CONFIGURACION_ACCESO_REGISTRO, getDateTime());
                contentValuesInformacionAcceso.put(DatabaseManager.TableConfiguracionAcceso.COLUMN_CONFIGURACION_ACCESO_ESTADO, 1);

                getWritableDatabase().insert(
                        DatabaseManager.TableConfiguracionAcceso.TABLE_NAME_CONFIGURACION_ACCESO,
                        null,
                        contentValuesInformacionAcceso
                );

                getWritableDatabase().setTransactionSuccessful();

                transaction = true;

            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            getWritableDatabase().endTransaction();

        }
        return transaction;
    }

    /*
      #############################################################################################
      AREA MODULE REPORTES
      #############################################################################################
     */

    /**
     * Metodo para Obtener la informacion del establecimineto
     *
     * @return modelEstablishment
     */
    public String getClaveAdmin() {

        Cursor cursorQuery;
        String clave = "";

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseManager.TableConfiguracionAcceso.TABLE_NAME_CONFIGURACION_ACCESO +
                        " WHERE " + DatabaseManager.TableConfiguracionAcceso.COLUMN_CONFIGURACION_ACCESO_ESTADO + " = '1'", null
        );

        if (cursorQuery.moveToFirst()) {
            clave = cursorQuery.getString(1);

        }

        cursorQuery.close();

        return clave;
    }

    /**
     * Metodo para Obtener la informacion del establecimineto
     *
     * @return modelEstablishment
     */
    public Establishment getEstablecimiento() {

        Establishment modelEstablishment = new Establishment();

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseManager.TableEstablecimiento.TABLE_NAME_ESTABLECIMIENTO +
                        " WHERE " + DatabaseManager.TableEstablecimiento.COLUMN_ESTABLECIMIENTO_ESTADO + " = '1'", null
        );

        if (cursorQuery.moveToFirst()) {
            modelEstablishment.setCodigo(cursorQuery.getString(0));
            modelEstablishment.setNombre(cursorQuery.getString(1));
            modelEstablishment.setDireccion(cursorQuery.getString(2));
            modelEstablishment.setCiudad(cursorQuery.getString(3));
            modelEstablishment.setNit(cursorQuery.getString(4));
            modelEstablishment.setRazonsocial(cursorQuery.getString(5));
            modelEstablishment.setRegistro(cursorQuery.getString(6));
            modelEstablishment.setEstado(cursorQuery.getInt(7));

        }

        cursorQuery.close();

        return modelEstablishment;
    }


    public String obtenerFechaTransaccionNumCargo(String cargo) {
        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT " + DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_FECHA_SERVER + " , " +
                        DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_HORA_SERVER +
                        " FROM " + DatabaseManager.TableTransacciones.TABLE_NAME_TRANSACCIONES +
                        " WHERE " + DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_NUMERO_CARGO + " = '" + cargo + "'" +
                        " AND " + DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_TIPO_TRANSACCION + " = " + Transaccion.TIPO_TRANSACCION_CONSUMO +
                        " ORDER BY " + DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_REGISTRO + " ASC " +
                        " LIMIT 1", null
        );

        cursorQuery.moveToFirst();

        return cursorQuery.getString(0) + "  " + cursorQuery.getString(1);
    }


    public Transaccion obtenerUltimaTransaccionAnulada() {
        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT * " +
                        " FROM " + DatabaseManager.TableTransacciones.TABLE_NAME_TRANSACCIONES +
                        " WHERE " + DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_TIPO_TRANSACCION + " = " + Transaccion.TIPO_TRANSACCION_ANULACION +
                        " ORDER BY " + DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_REGISTRO + " DESC " +
                        " LIMIT 1", null
        );

        cursorQuery.moveToFirst();

        Transaccion modelTransaccion = getTransaccionDeCursor(cursorQuery);

        cursorQuery.close();

        return modelTransaccion;

    }


    /**
     * Metodo para Obtener ultima  transaccion
     *
     * @return Transaccion ultima transaccion realizada
     */
    public ArrayList<Transaccion> obtenerUltimaTransaccion() {

        String numero_cargo = "";

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT * " +
                        " FROM " + DatabaseManager.TableTransacciones.TABLE_NAME_TRANSACCIONES +
                        " ORDER BY " + DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_REGISTRO + " DESC " +
                        " LIMIT 1", null
        );

        if (cursorQuery.moveToFirst()) {
            numero_cargo = cursorQuery.getString(2);
        }

        cursorQuery.close();

        return obtenerTransaccionByNumeroCargo(numero_cargo);
    }

    /**
     * Metodo para Obtener una  transaccion segun el numero de cargo
     *
     * @param numCargo String numero de cargo para la consulta de la transaccion
     * @return Transaccion obtiene la transaccion segun el numero de cargo
     */
    public ArrayList<Transaccion> obtenerTransaccionByNumeroCargo(String numCargo) {

        ArrayList<Transaccion> lista = new ArrayList<>();

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT * " +
                        " FROM " + DatabaseManager.TableTransacciones.TABLE_NAME_TRANSACCIONES +
                        " WHERE " + DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_NUMERO_CARGO + " = '" + numCargo + "'"
                , null
        );

        while (cursorQuery.moveToNext()) {

            lista.add(getTransaccionDeCursor(cursorQuery));

        }

        cursorQuery.close();

        return lista;
    }

    /**
     * Metodo para Obtener una  transaccion segun el numero de cargo
     *
     * @return Arraylist destalles de la transaccion
     */
    public ArrayList<Transaccion> obtenerDetallesTransaccion() {

        ArrayList<Transaccion> lista = new ArrayList<>();

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseManager.TableTransacciones.TABLE_NAME_TRANSACCIONES +
                        " WHERE " + DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_FECHA_SERVER + " = '" + getDateTime().split(" ")[0] + "'"
                , null
        );

        while (cursorQuery.moveToNext()) {

            lista.add(getTransaccionDeCursor(cursorQuery));

        }

        cursorQuery.close();

        return lista;
    }

    /**
     * Metodo para Obtener una  transaccion segun el numero de cargo
     *
     * @return Arraylist destalles de la transaccion
     */
    public ArrayList<Transaccion> obtenerTransaccionesCierreLote() {

        ArrayList<Transaccion> lista = new ArrayList<>();

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT " + DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_NUMERO_CARGO + " , " +
                        DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_CEDULA_USUARIO + " , " +
                        DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_VALOR + " , " +
                        DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_TIPO_TRANSACCION
                        + " FROM " + DatabaseManager.TableTransacciones.TABLE_NAME_TRANSACCIONES

                , null
        );

        while (cursorQuery.moveToNext()) {

            Transaccion modelTransaccion = new Transaccion();

            modelTransaccion.setNumero_cargo(cursorQuery.getString(0));
            modelTransaccion.setNumero_documento(cursorQuery.getString(1));
            modelTransaccion.setValor(cursorQuery.getInt(2));
            modelTransaccion.setTipo_transaccion(cursorQuery.getInt(3));

            lista.add(modelTransaccion);

        }

        cursorQuery.close();

        return lista;
    }
    //DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_NUMERO_CARGO + " = " + num

    public void dropTransactions(String num) {
        getWritableDatabase().delete(
                DatabaseManager.TableTransacciones.TABLE_NAME_TRANSACCIONES,
                DatabaseManager.TableTransacciones.COLUMN_TRANSACCIONES_NUMERO_CARGO + " = '" + num + "'",
                null
        );
    }

    /**
     * Metodo para Obtener una  transaccion segun el numero de cargo
     *
     * @return Arraylist destalles de la transaccion
     */
    public ArrayList<Transaccion> obtenerGeneralTransaccion() {

        ArrayList<Transaccion> lista = new ArrayList<>();

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseManager.TableTransacciones.TABLE_NAME_TRANSACCIONES
                , null
        );

        while (cursorQuery.moveToNext()) {

            lista.add(getTransaccionDeCursor(cursorQuery));

        }

        cursorQuery.close();

        return lista;
    }


    /**
     * Metodo para Obtener el conteo de los registros de la tabla base_financiera
     *
     * @return conteo de los registros
     */
    public int obtenerConteoRegistroProductos() {

        int count;

        Cursor cursorQuery;

        cursorQuery = getWritableDatabase().rawQuery(
                "SELECT COUNT(1) FROM " +
                        DatabaseManager.TableProducto.TABLE_NAME_PRODUCTO,
                null
        );

        cursorQuery.moveToFirst();

        count = cursorQuery.getInt(0);

        cursorQuery.close();

        return count;
    }

    /*
      #############################################################################################
      Metodos privados auxiliares
      #############################################################################################
     */

    /**
     * Metodo para Obtener el String de fecha y hora
     *
     * @return String fecha
     */
    private String getDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());

        Date date = new Date();

        return dateFormat.format(date);
    }

    /**
     * Metodo que obtiene los cambios registrados en la ultima sesion de lla base de datos
     *
     * @return long conteo de filas afectadas en la ultima transaccion
     */
    private long obtenerConteoCambios() {
        SQLiteStatement statement = getWritableDatabase().compileStatement("SELECT changes()");
        return statement.simpleQueryForLong();
    }


    private Transaccion getTransaccionDeCursor(Cursor cursorQuery) {

        Transaccion modelTransaccion = new Transaccion();

        modelTransaccion.setId(cursorQuery.getInt(0));
        modelTransaccion.setTipo_servicio(cursorQuery.getInt(1));
        modelTransaccion.setNumero_cargo(cursorQuery.getString(2));
        modelTransaccion.setNumero_tarjeta(cursorQuery.getString(3));
        modelTransaccion.setValor(cursorQuery.getInt(4));
        modelTransaccion.setTipo_transaccion(cursorQuery.getInt(5));
        modelTransaccion.setNumero_documento(cursorQuery.getString(6));
        modelTransaccion.setNombre_usuario(cursorQuery.getString(7));
        modelTransaccion.setFecha_server(cursorQuery.getString(8));
        modelTransaccion.setHora_server(cursorQuery.getString(9));
        modelTransaccion.setRegistro(cursorQuery.getString(10));
        modelTransaccion.setEstado(cursorQuery.getInt(11));

        return modelTransaccion;

    }


}
