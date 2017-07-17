package com.cofrem.transacciones.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.cofrem.transacciones.global.InfoGlobalSettingsPrint;
import com.cofrem.transacciones.models.PrintRow;
import com.telpo.tps550.api.printer.ThermalPrinter;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PrintHandler extends Handler {


    // Atributos
    private static PrintHandler singleton;
    private static Context context;

    private Bitmap bitmapOrigen;

    private String messagePrint;

    private boolean nopaper = false;
    private boolean LowBattery = false;
    private boolean stop = false;
    private final boolean isClose = false;

    /**
     * Constructor de la clase que recibe una instancia de contexto
     *
     * @param context
     */
    private PrintHandler(Context context) {

        PrintHandler.context = context;

        IntentFilter pIntentFilter = new IntentFilter();

        pIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        context.registerReceiver(broadcastReceiver, pIntentFilter);

    }

    /**
     * Retorna la instancia unica del singleton
     *
     * @param context contexto donde se ejecutaron las peticiones
     * @return Instancia
     */
    public static synchronized PrintHandler getInstance(Context context) {
        if (singleton == null) {
            singleton = new PrintHandler(context.getApplicationContext());
        }
        return singleton;
    }

    public void printMessage(String messagePrint) {
        this.messagePrint = messagePrint;
        singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_PRINTCONTENT, 1, 0, null)
        );
    }

    public void printPinture(Bitmap origen){
        this.bitmapOrigen = origen;
        singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_PRINTPICTURE, 1, 0, null)
        );
    }

    public void printRecibo(Bitmap origen,String messagePrint){
        this.bitmapOrigen = origen;
        this.messagePrint = messagePrint;
        singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_PRINTCERIBO, 1, 0, null)
        );
    }


    @Override
    public void handleMessage(Message msg) {
        if (stop == true)
            return;
        switch (msg.what) {

            case InfoGlobalSettingsPrint.CODE_NOPAPER:
                noPaperDlg();
                break;

            case InfoGlobalSettingsPrint.CODE_LOWBATTERY:
/*
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle(R.string.operation_result);
                alertDialog.setMessageWS(getString(R.string.LowBattery));
                alertDialog.setPositiveButton(getString(R.string.dlg_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertDialog.show();
                */
                break;

            case InfoGlobalSettingsPrint.CODE_PRINTVERSION:
                // dialog.dismiss();
                if (msg.obj.equals("1")) {
                    // txvPrintVersion.setText(printVersion);
                } else {
                    // Toast.makeText(context, R.string.operation_fail, Toast.LENGTH_LONG).show();
                }
                break;

            case InfoGlobalSettingsPrint.CODE_PRINTPICTURE:
                // progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.bl_dy),getString(R.string.printing_wait));
                // printting = CODE_PRINTPICTURE;
                new printPicture().start();
                break;

            case InfoGlobalSettingsPrint.CODE_PRINTCONTENT:
                // progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.bl_dy),getString(R.string.printing_wait));
                // printting = CODE_PRINTCONTENT;
//                new contentPrintThread().start();
                Boolean respuesta = true;
                try {
                    respuesta = new ImprimirTexto(new ImprimirTexto.ResponseImprimirTexto(){

                        @Override
                        public boolean processFinish(boolean exito) {
                            return exito;
                        }
                    }).execute(messagePrint).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(respuesta){
                   Log.e("impriminso","exito") ;
                }else{
                    Log.e("impriminso","error") ;
                }

                break;

            case InfoGlobalSettingsPrint.CODE_PRINTCERIBO:
                // progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.bl_dy),getString(R.string.printing_wait));
                // printting = CODE_PRINTPICTURE;
                new PrintRecibo().start();
                break;

            case InfoGlobalSettingsPrint.CODE_CANCELPROMPT:
                /*
                if (progressDialog != null && !PrinterActivity.this.isFinishing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }*/
                break;

            case InfoGlobalSettingsPrint.CODE_EXECUTECOMMAND:
                new executeCommand().start();
                break;

            case InfoGlobalSettingsPrint.CODE_OVERHEAT:
               /* AlertDialog.Builder overHeatDialog = new AlertDialog.Builder(context);
                overHeatDialog.setTitle(R.string.operation_result);
                overHeatDialog.setMessageWS(getString(R.string.overTemp));
                overHeatDialog.setPositiveButton(getString(R.string.dlg_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                overHeatDialog.show();*/
                break;

            default:
                Toast.makeText(context, "Print Error!", Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * Metodo extendido de un hilo q hace algo
     * - al parecer imprime contenido
     * - este es el q importa
     */
    private class contentPrintThread extends Thread {
        @Override
        public void run() {
            super.run();

            String Result;
            setName("Content Print Thread");
                try {
                    ThermalPrinter.start();
                    ThermalPrinter.reset();
                    ThermalPrinter.setAlgin(ThermalPrinter.ALGIN_LEFT);
                    ThermalPrinter.setLeftIndent(InfoGlobalSettingsPrint.LEFT_DISTANCE);
                    ThermalPrinter.setLineSpace(InfoGlobalSettingsPrint.LINE_DISTANCE);
                    if (InfoGlobalSettingsPrint.FONT_SIZE == 4) {
                        ThermalPrinter.setFontSize(2);
                        ThermalPrinter.enlargeFontSize(2, 2);
                    } else if (InfoGlobalSettingsPrint.FONT_SIZE == 3) {
                        ThermalPrinter.setFontSize(1);
                        ThermalPrinter.enlargeFontSize(2, 2);
                    } else if (InfoGlobalSettingsPrint.FONT_SIZE == 2) {
                        ThermalPrinter.setFontSize(2);
                    } else if (InfoGlobalSettingsPrint.FONT_SIZE == 1) {
                        ThermalPrinter.setFontSize(1);
                    }
                    ThermalPrinter.setGray(InfoGlobalSettingsPrint.GRAY_LEVEL);
                    ThermalPrinter.addString(messagePrint);
                    ThermalPrinter.printString();
                    ThermalPrinter.clearString();
                    ThermalPrinter.walkPaper(100);
                } catch (Exception e) {
                    e.printStackTrace();
                    Result = e.toString();
                    if (Result.equals("com.telpo.tps550.api.printer.NoPaperException")) {
                        nopaper = true;
                        // return;
                    } else if (Result.equals("com.telpo.tps550.api.printer.OverHeatException")) {
                        singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_OVERHEAT, 1, 0, null));
                    } else {
                        singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_PRINTERR, 1, 0, null));
                    }
                } finally {
                    // lock.release();
                    singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_CANCELPROMPT, 1, 0, null));
                    if (nopaper)
                        singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_NOPAPER, 1, 0, null));
                    ThermalPrinter.stop();
                    nopaper = false;
                }

        }
    }


    protected static class ImprimirTexto extends AsyncTask<String, Integer, Boolean>{

        public interface ResponseImprimirTexto{
            boolean processFinish(boolean exito);
        }

        public ResponseImprimirTexto delegate =null;

        public ImprimirTexto(ResponseImprimirTexto response) {
            this.delegate = response;
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            String Result;
            Boolean nopaper = false;
            try {
                ThermalPrinter.start();
                ThermalPrinter.reset();
                ThermalPrinter.setAlgin(ThermalPrinter.ALGIN_LEFT);
                ThermalPrinter.setLeftIndent(InfoGlobalSettingsPrint.LEFT_DISTANCE);
                ThermalPrinter.setLineSpace(InfoGlobalSettingsPrint.LINE_DISTANCE);
                if (InfoGlobalSettingsPrint.FONT_SIZE == 4) {
                    ThermalPrinter.setFontSize(2);
                    ThermalPrinter.enlargeFontSize(2, 2);
                } else if (InfoGlobalSettingsPrint.FONT_SIZE == 3) {
                    ThermalPrinter.setFontSize(1);
                    ThermalPrinter.enlargeFontSize(2, 2);
                } else if (InfoGlobalSettingsPrint.FONT_SIZE == 2) {
                    ThermalPrinter.setFontSize(2);
                } else if (InfoGlobalSettingsPrint.FONT_SIZE == 1) {
                    ThermalPrinter.setFontSize(1);
                }
                ThermalPrinter.setGray(InfoGlobalSettingsPrint.GRAY_LEVEL);
                ThermalPrinter.addString(strings[0]);
                ThermalPrinter.printString();
                ThermalPrinter.clearString();
                ThermalPrinter.walkPaper(100);
            } catch (Exception e) {
                e.printStackTrace();
                Result = e.toString();
                if (Result.equals("com.telpo.tps550.api.printer.NoPaperException")) {
                    nopaper = true;
                    // return;
                } else if (Result.equals("com.telpo.tps550.api.printer.OverHeatException")) {
                    singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_OVERHEAT, 1, 0, null));
                } else {
                    singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_PRINTERR, 1, 0, null));
                }
            } finally {
                // lock.release();
                singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_CANCELPROMPT, 1, 0, null));
                if (nopaper)
                    singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_NOPAPER, 1, 0, null));
                ThermalPrinter.stop();
                nopaper = false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            delegate.processFinish(aBoolean);
        }

    }



    /**
     * Metodo extendido de un hilo q hace algo
     * - al parecer imprime una figura
     * - este tambn importa
     */
    private class printPicture extends Thread {

        @Override
        public void run() {
            super.run();
            String Result;
            setName("PrintPicture Thread");

                try {
                    ThermalPrinter.start();
                    ThermalPrinter.reset();
                    ThermalPrinter.setGray(InfoGlobalSettingsPrint.GRAY_LEVEL);
                    ThermalPrinter.setAlgin(ThermalPrinter.ALGIN_MIDDLE);

                    if (bitmapOrigen != null) {
                        ThermalPrinter.printLogo(bitmapOrigen);
                        ThermalPrinter.walkPaper(20);
                    } else {
                        Log.e("error", "no se cargo la imagen");
                    }


//                File file = new File(bitmapOrigen);
//                if (file.exists()) {
//                    ThermalPrinter.printLogo(BitmapFactory.decodeFile("/mnt/sdcard/111.bmp"));
//                    ThermalPrinter.walkPaper(100);
//                } else {
//
//                    Log.e("error","no se cargo la imagen");
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            Toast.makeText(ReimpresionScreenActivity.this, getString(R.string.not_find_picture), Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
                } catch (Exception e) {
                    e.printStackTrace();
                    Result = e.toString();
                    if (Result.equals("com.telpo.tps550.api.printer.NoPaperException")) {
                        nopaper = true;
                        // return;
                    } else if (Result.equals("com.telpo.tps550.api.printer.OverHeatException")) {
                        singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_OVERHEAT, 1, 0, null));
                    } else {
                        singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_PRINTERR, 1, 0, null));
                    }
                } finally {
                    singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_CANCELPROMPT, 1, 0, null));
                    if (nopaper)
                        singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_NOPAPER, 1, 0, null));
                    ThermalPrinter.stop();
                    nopaper = false;
                    // PrinterActivity.this.sleep(1500);
                    // if(progressDialog != null &&
                    // !PrinterActivity.this.isFinishing() ){
                    // progressDialog.dismiss();
                    // progressDialog = null;
                    // }
                    Log.v("", "The PrintPicture Progress End !!!");
//                if (isClose) {
//                    finish();
//                }
                }
                // handler.sendMessage(handler
                // .obtainMessage(CODE_ENABLE_BUTTON, 1, 0, null));

        }
    }


    /**
     * Metodo extendido de un hilo q hace algo
     * - al parecer imprime contenido
     * - este es el q importa
     */
    private class PrintRecibo extends Thread {
        @Override
        public void run() {
            super.run();

            String Result;

            setName("Content Print Thread");
            try {
                ThermalPrinter.start();
                ThermalPrinter.reset();
                ThermalPrinter.setGray(InfoGlobalSettingsPrint.GRAY_LEVEL);
                ThermalPrinter.setAlgin(ThermalPrinter.ALGIN_MIDDLE);
                if(bitmapOrigen!=null){
                    ThermalPrinter.printLogo(bitmapOrigen);
                    ThermalPrinter.walkPaper(1);
                }else{
                    Log.e("error","no se cargo la imagen");
                }
                ThermalPrinter.setAlgin(ThermalPrinter.ALGIN_LEFT);
                ThermalPrinter.setLeftIndent(InfoGlobalSettingsPrint.LEFT_DISTANCE);
                ThermalPrinter.setLineSpace(InfoGlobalSettingsPrint.LINE_DISTANCE);
                if (InfoGlobalSettingsPrint.FONT_SIZE == 4) {
                    ThermalPrinter.setFontSize(2);
                    ThermalPrinter.enlargeFontSize(2, 2);
                } else if (InfoGlobalSettingsPrint.FONT_SIZE == 3) {
                    ThermalPrinter.setFontSize(1);
                    ThermalPrinter.enlargeFontSize(2, 2);
                } else if (InfoGlobalSettingsPrint.FONT_SIZE == 2) {
                    ThermalPrinter.setFontSize(2);
                } else if (InfoGlobalSettingsPrint.FONT_SIZE == 1) {
                    ThermalPrinter.setFontSize(1);
                }
                ThermalPrinter.setGray(InfoGlobalSettingsPrint.GRAY_LEVEL);
                ThermalPrinter.addString(messagePrint);
                ThermalPrinter.printString();
                ThermalPrinter.clearString();
                ThermalPrinter.walkPaper(100);
            } catch (Exception e) {
                e.printStackTrace();
                Result = e.toString();
                if (Result.equals("com.telpo.tps550.api.printer.NoPaperException")) {
                    nopaper = true;
                    // return;
                } else if (Result.equals("com.telpo.tps550.api.printer.OverHeatException")) {
                    singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_OVERHEAT, 1, 0, null));
                } else {
                    singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_PRINTERR, 1, 0, null));
                }
            } finally {
                // lock.release();
                singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_CANCELPROMPT, 1, 0, null));
                if (nopaper)
                    singleton.sendMessage(singleton.obtainMessage(InfoGlobalSettingsPrint.CODE_NOPAPER, 1, 0, null));
                ThermalPrinter.stop();
                nopaper = false;
            }
        }
    }


    /**
     * Metodo extendido de un hilo q hace algo
     */
    private class executeCommand extends Thread {
        @Override
        public void run() {
            super.run();
            /*
            setName("ExecuteCommand Thread");
            try {
                ThermalPrinter.start();
                ThermalPrinter.reset();
                ThermalPrinter.sendCommand(edtInputCommand.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
                Result = e.toString();
                if (Result.equals("com.telpo.tps550.api.printer.NoPaperException")) {
                    nopaper = true;
                    // return;
                } else if (Result.equals("com.telpo.tps550.api.printer.OverHeatException")) {
                    handler.sendMessage(handler.obtainMessage(CODE_OVERHEAT, 1, 0, null));
                } else {
                    handler.sendMessage(handler.obtainMessage(CODE_PRINTERR, 1, 0, null));
                }
            } finally {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(handler.obtainMessage(CODE_CANCELPROMPT, 1, 0, null));
                if (nopaper)
                    handler.sendMessage(handler.obtainMessage(CODE_NOPAPER, 1, 0, null));
                ThermalPrinter.stop();
                nopaper = false;
                // PrinterActivity.this.sleep(1500);
                // if(progressDialog != null &&
                // !PrinterActivity.this.isFinishing() ){
                // progressDialog.dismiss();
                // progressDialog = null;
                // }
                Log.v(TAG, "The ExecuteCommand Progress End !!!");
                if (isClose) {
                    finish();
                }
            }
            */
        }

    }

    /**
     * Metodo para dialogo de no papel
     */

    private void noPaperDlg() {
        /*
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle(getString(R.string.noPaper));
        dlg.setMessageWS(getString(R.string.noPaperNotice));
        dlg.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dlg.show();
        */
    }


    /**
     * Inicializacion de una variable broadcastReceiver con una implementacion de un metodo onReceive
     */
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_NOT_CHARGING);
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                if (status != BatteryManager.BATTERY_STATUS_CHARGING) {
                    if (android.os.Build.MODEL.toUpperCase().equals("TPS390")) {
                        if (level * 10 <= scale) {
                            LowBattery = true;
                        } else {
                            LowBattery = false;
                        }
                    } else {
                        if (level * 5 <= scale) {
                            LowBattery = true;
                        } else {
                            LowBattery = false;
                        }
                    }
                } else {
                    LowBattery = false;
                }
            }
        }
    };

}

