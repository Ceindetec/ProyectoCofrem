package com.cofrem.transacciones.lib;

import android.os.AsyncTask;

import com.cofrem.transacciones.models.modelsWS.TransactionWS;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;


public class KsoapAsync extends AsyncTask<TransactionWS, Integer, SoapObject> {

    //Call back interface
    public ResponseKsoapAsync delegate = null;

    /**
     * Metodo de la interface de devolucion
     *
     * @param response
     */
    public KsoapAsync(ResponseKsoapAsync response) {
        delegate = response;//Assigning call back interfacethrough constructor
    }

    /**
     * Ejecuta la accion de llamar el Web Service
     *
     * @param params
     * @return
     */
    @Override
    protected SoapObject doInBackground(TransactionWS... params) {

        //Se recibe el primer parametro de la peticion tipo TransactionWS
        TransactionWS transactionWs = params[0];

        //Se obtienen los parametros de la peticion
        String[][] mapParams = transactionWs.getParamsTransaction();

        //se crea un nuevo Soap Request
        SoapObject soapRequest = new SoapObject(transactionWs.getNameSpaceTransaction(), transactionWs.getMethodNameTransaction());

        String[] parameters;

        //Se recorren los parametros y se agregan a la peticion
        for (int i = 0; i < mapParams.length; i++) {

            //Se inicializa una propiedad
            PropertyInfo propertyInfo = new PropertyInfo();

            parameters = mapParams[i];

            propertyInfo.setName(parameters[0].trim());
            propertyInfo.setValue(parameters[1].trim());
            propertyInfo.setType(String.class);

            //Se agrega propiedad a la peticion
            soapRequest.addProperty(propertyInfo);

        }

        //request to server and get Soap Primitive response
        SoapObject soapResponse = KsoapTransaction.getData(transactionWs.getUrlTransaction(), transactionWs.getNameSpaceTransaction() + transactionWs.getMethodNameTransaction(), soapRequest);

        return soapResponse;
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la clase
     * #############################################################################################
     */

    /**
     * Metodo que se ejecuta despues de la ejecucion retorna la respuesta
     *
     * @param soapResponse
     */
    @Override
    protected void onPostExecute(SoapObject soapResponse) {

        //Delegado que retorna el objeto soap
        delegate.processFinish(soapResponse);
    }

    /**
     * Interface para el callback de datos
     */
    public interface ResponseKsoapAsync {
        SoapObject processFinish(SoapObject soapResponse);
    }
}
