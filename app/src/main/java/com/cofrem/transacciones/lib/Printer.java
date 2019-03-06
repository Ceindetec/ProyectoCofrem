package com.cofrem.transacciones.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.telpo.tps550.api.DeviceAlreadyOpenException;
import com.telpo.tps550.api.DeviceNotOpenException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.iccard.NotEnoughBufferException;
import com.telpo.tps550.api.printer.ICommitCallback;
import com.telpo.tps550.api.printer.NoPaperException;
import com.telpo.tps550.api.printer.OverHeatException;
import com.telpo.tps550.api.printer.ThermalPrinter;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by luispineda on 13/07/17.
 */

public class Printer {
    public static final int STATUS_OK = 0;
    public static final int STATUS_NO_PAPER = -1001;
    public static final int STATUS_OVER_HEAT = -1002;
    public static final int STATUS_OVER_FLOW = -1003;
    public static final int STATUS_DISCONNECT = -1004;
    public static final int STATUS_UNKNOWN = -9999;
    private static final String TAG = "Printer";
    private static final int MODE_TEXT = 0;
    private static final int MODE_PIC = 1;
    private static final int MODE_BAR = 2;
    private static final int ACTION_PRINT = 0;
    private static final int ACTION_STATUS = 1;
    private static List<PrintItem> printList = null;
    private static HandlerThread handlerThread = null;
    private static Handler handler = null;
    private static int barcode_mode = -1;
    private static int mStatus = -9999;
    private static Object lock = new Object();

    public Printer() {
    }

    public static synchronized void printText(String txt, StyleConfig style) {
        if (txt != null && printList != null) {
            StyleConfig styleConfig = new StyleConfig();
            if (style != null) {
                styleConfig.fontFamily = style.fontFamily;
                styleConfig.fontSize = style.fontSize;
                styleConfig.fontStyle = style.fontStyle;
                styleConfig.align = style.align;
                styleConfig.gray = style.gray;
                styleConfig.lineSpace = style.lineSpace;
                styleConfig.newLine = style.newLine;
            }

            printList.add(new Printer.PrintItem(txt, styleConfig));
        }
    }

    public static synchronized void printBarCode(String barcode, StyleConfig.Align align) {
        if (barcode != null && printList != null) {
            if (barcode_mode < 0) {
                barcode_mode = 0;

                try {
                    String bitmap = ThermalPrinter.getVersion().trim();
                    if (bitmap.substring(bitmap.length() - 8).compareTo("20151106") >= 0) {
                        barcode_mode = 1;
                    }
                } catch (TelpoException var5) {
                    var5.printStackTrace();
                }
            }

            if (barcode_mode == 1) {
                StyleConfig bitmap1 = new StyleConfig();
                bitmap1.align = align;
                Printer.PrintItem e = new Printer.PrintItem(barcode, bitmap1);
                e.mode = 2;
                printList.add(e);
            } else {
                try {
                    Bitmap bitmap2 = CreateCode(barcode, BarcodeFormat.CODE_128, 360, 64);
                    Bitmap e1 = adjustBitmap(bitmap2, align);
                    printList.add(new Printer.PrintItem(e1, new StyleConfig()));
                } catch (WriterException var4) {
                    var4.printStackTrace();
                }
            }

        }
    }

    public static synchronized void printQRCode(String QRCode, StyleConfig.Align align) {
        if (QRCode != null && printList != null) {
            try {
                Bitmap bitmap = CreateCode(QRCode, BarcodeFormat.QR_CODE, 256, 256);
                Bitmap e = Bitmap.createBitmap(bitmap, 40, 40, bitmap.getWidth() - 80, bitmap.getHeight() - 80);
                Bitmap newBitmap = adjustBitmap(e, align);
                printList.add(new Printer.PrintItem(newBitmap, new StyleConfig()));
            } catch (WriterException var5) {
                var5.printStackTrace();
            }

        }
    }

    public static synchronized void printImage(String path, StyleConfig.Align align) {
        if (path != null && printList != null) {
            File file = new File(path);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                Bitmap cutBitmap = Bitmap.createBitmap(bitmap, 0, 16, bitmap.getWidth(), bitmap.getHeight() - 32);
                Bitmap newBitmap = adjustBitmap(cutBitmap, align);
                printList.add(new Printer.PrintItem(newBitmap, new StyleConfig()));
            }

        }
    }

    public static synchronized void printImage(Bitmap cutBitmap, StyleConfig.Align align, int gray, int lineSpace) {
        if (cutBitmap != null && printList != null) {
            Bitmap newBitmap = adjustBitmap(cutBitmap, align);
            printList.add(new Printer.PrintItem(newBitmap, new StyleConfig(align, gray, lineSpace)));

        }
    }

    public static synchronized void printSalto() {
        try {
            ThermalPrinter.walkPaper(100);
        } catch (TelpoException e) {
            e.printStackTrace();
        }
    }


    private static void commitOperation(List<Printer.PrintItem> contentList, ICommitCallback commitCallback) {
        boolean printFlag = false;
        Iterator printIterator = contentList.iterator();

        try {
            int e = ThermalPrinter.checkStatus();
            if (e != 0) {
                String msg;
                short e1;
                switch (e) {
                    case 1:
                        e1 = -1001;
                        msg = "Printer out of paper";
                        break;
                    case 2:
                        e1 = -1002;
                        msg = "Printer over heat";
                        break;
                    case 3:
                        e1 = -1003;
                        msg = "Printer over flow";
                        break;
                    case 4:
                    default:
                        e1 = -9999;
                        msg = "Printer error";
                }

                ThermalPrinter.reset();
                if (commitCallback != null) {
                    commitCallback.printerStatus(e1, msg);
                }

                return;
            }
        } catch (DeviceNotOpenException var7) {
            if (commitCallback != null) {
                commitCallback.printerStatus(-1004, "Printer disconnect");
            }

            return;
        } catch (TelpoException var8) {
            var8.printStackTrace();
        }

        while (printIterator.hasNext()) {
            Printer.PrintItem printItem = (Printer.PrintItem) printIterator.next();
            if (printItem.mode == 1) {
                try {
                    if (printFlag) {
                        ThermalPrinter.printString();
                        printFlag = false;
                        Thread.sleep(200L);
                    }

                    ThermalPrinter.setAlgin(0);
                    ThermalPrinter.setGray(printItem.styleConfig.gray);
                    ThermalPrinter.printLogo(printItem.bitmap);
                    ThermalPrinter.walkPaper(printItem.styleConfig.lineSpace);
                } catch (DeviceNotOpenException var19) {
                    var19.printStackTrace();
                    if (commitCallback != null) {
                        commitCallback.printerStatus(-1004, "Printer disconnect");
                    }

                    return;
                } catch (NoPaperException var20) {
                    var20.printStackTrace();
                    if (commitCallback != null) {
                        commitCallback.printerStatus(-1001, "Printer out of paper");
                    }

                    return;
                } catch (NotEnoughBufferException var21) {
                    var21.printStackTrace();
                    if (commitCallback != null) {
                        commitCallback.printerStatus(-1003, "Printer over flow");
                    }

                    return;
                } catch (OverHeatException var22) {
                    var22.printStackTrace();
                    if (commitCallback != null) {
                        commitCallback.printerStatus(-1002, "Printer over heat");
                    }

                    return;
                } catch (Exception var23) {
                    var23.printStackTrace();
                    if (commitCallback != null) {
                        commitCallback.printerStatus(-9999, "Printer error");
                    }

                    return;
                }
            } else {
                try {
                    if (printItem.styleConfig.fontSize == StyleConfig.FontSize.F1) {
                        ThermalPrinter.setFontSize(1);
                        ThermalPrinter.enlargeFontSize(1, 1);
                    } else if (printItem.styleConfig.fontSize == StyleConfig.FontSize.F3) {
                        ThermalPrinter.setFontSize(2);
                        ThermalPrinter.enlargeFontSize(1, 1);
                    } else if (printItem.styleConfig.fontSize == StyleConfig.FontSize.F4) {
                        ThermalPrinter.setFontSize(1);
                        ThermalPrinter.enlargeFontSize(2, 2);
                    } else {
                        ThermalPrinter.setFontSize(1);
                        ThermalPrinter.enlargeFontSize(1, 2);
                    }

                    if (printItem.styleConfig.align == StyleConfig.Align.CENTER) {
                        ThermalPrinter.setAlgin(1);
                    } else if (printItem.styleConfig.align == StyleConfig.Align.RIGHT) {
                        ThermalPrinter.setAlgin(2);
                    } else {
                        ThermalPrinter.setAlgin(0);
                    }

                    ThermalPrinter.setGray(printItem.styleConfig.gray);
                    ThermalPrinter.setLineSpace(printItem.styleConfig.lineSpace);
                    if (printItem.string.length() > 0) {
                        if (printItem.mode == 0) {
                            ThermalPrinter.addString(printItem.string);
                            if (printItem.styleConfig.newLine) {
                                ThermalPrinter.addString("\n");
                            }
                        } else if (printItem.mode == 2) {
                            ThermalPrinter.addBarcode(printItem.string);
                        }
                    }

                    if (printItem.feed > 0) {
                        ThermalPrinter.printStringAndWalk(0, 0, printItem.feed);
                        printFlag = false;
                    } else {
                        printFlag = true;
                    }


                } catch (DeviceNotOpenException var14) {
                    var14.printStackTrace();
                    if (commitCallback != null) {
                        commitCallback.printerStatus(-1004, "Printer disconnect");
                    }

                    return;
                } catch (NoPaperException var15) {
                    var15.printStackTrace();
                    if (commitCallback != null) {
                        commitCallback.printerStatus(-1001, "Printer out of paper");
                    }

                    return;
                } catch (NotEnoughBufferException var16) {
                    var16.printStackTrace();
                    if (commitCallback != null) {
                        commitCallback.printerStatus(-1003, "Printer over flow");
                    }

                    return;
                } catch (OverHeatException var17) {
                    var17.printStackTrace();
                    if (commitCallback != null) {
                        commitCallback.printerStatus(-1002, "Printer over heat");
                    }

                    return;
                } catch (Exception var18) {
                    var18.printStackTrace();
                    if (commitCallback != null) {
                        commitCallback.printerStatus(-9999, "Printer error");
                    }

                    return;
                }
            }
        }

        if (printFlag) {
            try {
                ThermalPrinter.printString();
            } catch (DeviceNotOpenException var9) {
                var9.printStackTrace();
                if (commitCallback != null) {
                    commitCallback.printerStatus(-1004, "Printer disconnect");
                }

                return;
            } catch (NoPaperException var10) {
                var10.printStackTrace();
                if (commitCallback != null) {
                    commitCallback.printerStatus(-1001, "Printer out of paper");
                }

                return;
            } catch (NotEnoughBufferException var11) {
                var11.printStackTrace();
                if (commitCallback != null) {
                    commitCallback.printerStatus(-1003, "Printer over flow");
                }

                return;
            } catch (OverHeatException var12) {
                var12.printStackTrace();
                if (commitCallback != null) {
                    commitCallback.printerStatus(-1002, "Printer over heat");
                }

                return;
            } catch (Exception var13) {
                var13.printStackTrace();
                if (commitCallback != null) {
                    commitCallback.printerStatus(-9999, "Printer error");
                }

                return;
            }
        }

        if (commitCallback != null) {
            commitCallback.printerStatus(0, "Printer OK");
        }

    }

    public static synchronized void commitOperation() {
        if (printList != null && handler != null) {
            ArrayList list = new ArrayList(printList.size());
            Iterator iterator = printList.iterator();

            while (iterator.hasNext()) {
                list.add((Printer.PrintItem) iterator.next());
            }

            Printer.CommitData commitData = new Printer.CommitData();
            commitData.printList = list;
            commitData.callback = null;
            Message message = handler.obtainMessage(0, commitData);
            handler.sendMessage(message);
            printList.clear();
        }
    }

    public static synchronized void commitOperation(ICommitCallback callback) {
        if (printList != null && handler != null) {
            ArrayList list = new ArrayList(printList.size());
            Iterator iterator = printList.iterator();

            while (iterator.hasNext()) {
                list.add((Printer.PrintItem) iterator.next());
            }

            Printer.CommitData commitData = new Printer.CommitData();
            commitData.printList = list;
            commitData.callback = callback;
            Message message = handler.obtainMessage(0, commitData);
            handler.sendMessage(message);
            printList.clear();
        }
    }

    public static synchronized int connect() {
        byte ret = 0;

        try {
            if (printList == null) {
                printList = new ArrayList();
            }

            if (handlerThread == null) {
                handlerThread = new HandlerThread("Printer");
                handlerThread.start();
                handler = new Printer.MyHandler(handlerThread.getLooper());
            }

            ThermalPrinter.start();
        } catch (DeviceAlreadyOpenException var2) {
            var2.printStackTrace();
            ret = -1;
        } catch (TelpoException var3) {
            var3.printStackTrace();
            ret = -1;
        } catch (Error e) {
            ret = -1;
        }

        return ret;
    }


    public static synchronized boolean testPrinterDevice() {

        boolean resultTestPrinterDevice = false;

        try {

            ThermalPrinter.start();

            ThermalPrinter.stop();

            resultTestPrinterDevice = true;

        } catch (TelpoException e) {

            e.printStackTrace();

        } catch (Error e) {

            e.printStackTrace();

        }

        return resultTestPrinterDevice;

    }


    public static synchronized void disconnect() {
        ThermalPrinter.stop();
        if (printList != null) {
            printList.clear();
            printList = null;
        }

        if (handlerThread != null) {
            handlerThread.quit();
            handlerThread = null;
            handler = null;
        }

        barcode_mode = -1;
    }

    public static synchronized void reset() {
        try {
            ThermalPrinter.reset();
        } catch (TelpoException var1) {
            var1.printStackTrace();
        }

        if (printList != null) {
            printList.clear();
        }

    }

    public static synchronized void feedPaper(int lines) {
        if (lines > 0 && printList != null) {
            Printer.PrintItem printItem = new Printer.PrintItem("", new StyleConfig());
            printItem.feed = lines;
            printList.add(printItem);
        }
    }


    public static synchronized int getStatus() {
        Object var0 = lock;
        synchronized (lock) {
            mStatus = -9999;
            handler.sendEmptyMessage(1);

            try {
                lock.wait(30000L);
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }
        }

        return mStatus;
    }

    public static synchronized boolean isConnected() {
        return printList != null && handlerThread != null;
    }

    private static Bitmap adjustBitmap(Bitmap bitmap, StyleConfig.Align align) {
        if (bitmap == null) {
            return null;
        } else {
            int adjustWidth = bitmap.getWidth();
            int adjustHeight = bitmap.getHeight();
            int offset = 0;
            boolean temp = false;
            int temp1;
            if (align == StyleConfig.Align.CENTER) {
                offset = (384 - adjustWidth) / 2;
                adjustWidth += offset;
                temp1 = adjustWidth % 8;
                if (temp1 != 0) {
                    adjustWidth += 8 - temp1;
                }
            } else if (align == StyleConfig.Align.RIGHT) {
                offset = 384 - adjustWidth;
                adjustWidth = 384;
            } else {
                temp1 = adjustWidth % 8;
                if (temp1 != 0) {
                    adjustWidth += 8 - temp1;
                }
            }

            Bitmap newBitmap = Bitmap.createBitmap(adjustWidth, adjustHeight, bitmap.getConfig());
            Paint paint = new Paint();
            paint.setColor(-1);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawRect(0.0F, 0.0F, (float) adjustWidth, (float) adjustHeight, paint);
            canvas.drawBitmap(bitmap, (float) offset, 0.0F, (Paint) null);
            return newBitmap;
        }
    }

    private static Bitmap CreateCode(String str, BarcodeFormat type, int bmpWidth, int bmpHeight) throws WriterException {
        BitMatrix matrix = (new MultiFormatWriter()).encode(str, type, bmpWidth, bmpHeight);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int bitmap = 0; bitmap < height; ++bitmap) {
            for (int x = 0; x < width; ++x) {
                if (matrix.get(x, bitmap)) {
                    pixels[bitmap * width + x] = -16777216;
                } else {
                    pixels[bitmap * width + x] = -1;
                }
            }
        }

        Bitmap var10 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        var10.setPixels(pixels, 0, width, 0, 0, width, height);
        return var10;
    }

    private static class CommitData {
        public List<Printer.PrintItem> printList;
        public ICommitCallback callback;

        private CommitData() {
            this.printList = null;
            this.callback = null;
        }
    }

    private static class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Printer.CommitData data = (Printer.CommitData) msg.obj;
                Printer.commitOperation(data.printList, data.callback);

                try {
                    ThermalPrinter.clearString();
                } catch (TelpoException var7) {
                    var7.printStackTrace();
                }
            } else if (msg.what == 1) {
                synchronized (Printer.lock) {
                    try {
                        int e = ThermalPrinter.checkStatus();
                        switch (e) {
                            case 0:
                                Printer.mStatus = 0;
                                break;
                            case 1:
                                Printer.mStatus = -1001;
                                break;
                            case 2:
                                Printer.mStatus = -1002;
                                break;
                            case 3:
                                Printer.mStatus = -1003;
                                break;
                            case 4:
                            default:
                                Printer.mStatus = -9999;
                        }
                    } catch (DeviceNotOpenException var4) {
                        Printer.mStatus = -1004;
                    } catch (TelpoException var5) {
                        var5.printStackTrace();
                        Printer.mStatus = -9999;
                    }

                    Printer.lock.notify();
                }
            }

        }
    }

    private static class PrintItem {
        public StyleConfig styleConfig = null;
        public String string = null;
        public Bitmap bitmap = null;
        public int feed = 0;
        public int mode = 0;

        public PrintItem(String string, StyleConfig styleConfig) {
            this.string = string;
            this.styleConfig = styleConfig;
        }

        public PrintItem(Bitmap bitmap, StyleConfig styleConfig) {
            this.styleConfig = styleConfig;
            this.bitmap = bitmap;
            this.mode = 1;
        }
    }
}

