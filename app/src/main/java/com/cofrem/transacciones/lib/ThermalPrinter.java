package com.cofrem.transacciones.lib;

/**
 * Created by luispineda on 16/08/17.
 */

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.telpo.tps550.api.DeviceAlreadyOpenException;
import com.telpo.tps550.api.DeviceNotOpenException;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.iccard.NotEnoughBufferException;
import com.telpo.tps550.api.printer.NoPaperException;
import com.telpo.tps550.api.printer.OverHeatException;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;


public class ThermalPrinter extends com.telpo.tps550.api.printer.ThermalPrinter {
    public static final int ALGIN_LEFT = 0;
    public static final int ALGIN_MIDDLE = 1;
    public static final int ALGIN_RIGHT = 2;
    public static final int DIRECTION_FORWORD = 0;
    public static final int DIRECTION_BACK = 1;
    public static final int WALK_DOTLINE = 0;
    public static final int WALK_LINE = 1;
    public static final int STATUS_OK = 0;
    public static final int STATUS_NO_PAPER = 1;
    public static final int STATUS_OVER_HEAT = 2;
    public static final int STATUS_OVER_FLOW = 3;
    public static final int STATUS_UNKNOWN = 4;
    private static final String TAG = "ThermalPrinter";
    private static final int ARGB_MASK_RED = 16711680;
    private static final int ARGB_MASK_GREEN = 65280;
    private static final int ARGB_MASK_BLUE = 255;
    private static final int RGB565_MASK_RED = 63488;
    private static final int RGB565_MASK_GREEN = 2016;
    private static final int RGB565_MASK_BLUE = 31;
    private static final int color = 128;
    private static boolean openFlag = false;

    static {
        System.loadLibrary("telpo_printer");
    }

    public ThermalPrinter() {
    }

    private static native int device_open();

    private static native int device_close();

    private static native int init();

    private static native int walk_paper(int var0);

    private static native int enlarge(int var0, int var1);

    private static native int highlight(boolean var0);

    private static native int gray(int var0);

    private static native int algin(int var0);

    private static native int line_space(int var0);

    private static native int add_string(byte[] var0, int var1);

    private static native int clear_string();

    private static native int print_and_walk(int var0, int var1, int var2);

    private static native int print_logo(int var0, int var1, char[] var2);

    private static native int print_barcode(int var0, byte[] var1, int var2);

    private static native int check_status();

    private static native int set_font(int var0);

    private static native int get_version(byte[] var0);

    private static native int indent(int var0);

    private static native void sleep_ms(int var0);

    private static native void get_sdk_version(byte[] var0);

    private static native int get_printer_type();

    private static native int search_mark(int var0, int var1, int var2);

    private static native int paper_cut();

    private static native int send_command(byte[] var0, int var1);

    private static native int add_barcode(byte[] var0, int var1);

    private static native int set_bold(int var0);

    private static TelpoException getException(int ret) {
        switch (ret) {
            case 65521:
                return new DeviceNotOpenException();
            case 65522:
            default:
                return new InternalErrorException();
            case 65523:
                return new DeviceAlreadyOpenException();
            case 65524:
                return new NoPaperException();
            case 65525:
                return new NotEnoughBufferException();
            case 65526:
                return new OverHeatException();
            case 65527:
                return new InternalErrorException();
        }
    }

    public static synchronized void start() throws TelpoException {
        if (openFlag) {
            throw new DeviceAlreadyOpenException();
        } else {
            int ret = device_open();
            if (ret == 0) {
                openFlag = true;
            } else {
                throw getException(ret);
            }
        }
    }

    public static synchronized void reset() throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            int ret = init();
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static synchronized void walkPaper(int line) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else if (line <= 0) {
            throw new IllegalArgumentException();
        } else {
            int ret = walk_paper(line);
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static synchronized void stop() {
        if (openFlag) {
            device_close();
            openFlag = false;
        }
    }

    public static synchronized int checkStatus() throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            int ret = check_status();
            switch (ret) {
                case 0:
                    return 0;
                case 65524:
                    return 1;
                case 65525:
                    return 3;
                case 65526:
                    return 2;
                default:
                    return 4;
            }
        }
    }

    public static synchronized void enlargeFontSize(int widthMultiple, int heightMultiple) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            int ret = enlarge(widthMultiple, heightMultiple);
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static synchronized void setFontSize(int type) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            int ret = set_font(type);
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static synchronized void setHighlight(boolean mode) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            int ret = highlight(mode);
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static synchronized void setGray(int level) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            int ret = gray(level);
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static synchronized void setAlgin(int mode) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            int ret = algin(mode);
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static synchronized void addString(String content) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else if (content != null && content.length() != 0) {
            Object text = null;

            byte[] text1;
            try {
                text1 = content.getBytes("GBK");
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                throw new InternalErrorException();
            }

            int ret = add_string(text1, text1.length);
            if (ret != 0) {
                throw getException(ret);
            }
        } else {
            throw new NullPointerException();
        }
    }

    public static synchronized void addBarcode(String barcode) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else if (barcode != null && barcode.length() != 0) {
            byte[] barcodeCmd = new byte[53];
            barcodeCmd[0] = 29;
            barcodeCmd[1] = 104;
            barcodeCmd[2] = 84;
            barcodeCmd[3] = 29;
            barcodeCmd[4] = 108;
            Bitmap bitmap = CreateCode(barcode, BarcodeFormat.CODE_128, 360, 108);
            boolean temp = false;
            boolean pix = false;
            int ss = 5;
            int widthOctet = bitmap.getWidth() / 8;
            int ret = 0;

            for (int column = 0; ret < widthOctet; column += 8) {
                int var10 = 0;

                for (int i = 0; i < 8; ++i) {
                    int var11 = bitmap.getPixel(i + column, 0);
                    if ((var11 & 16711680) >> 16 > 128 && (var11 & '\uff00') >> 8 > 128 && (var11 & 255) > 128) {
                        var10 <<= 1;
                    } else {
                        var10 = (var10 << 1) + 1;
                    }
                }

                barcodeCmd[ss] = (byte) var10;
                ++ss;
                ++ret;
            }

            ret = add_barcode(barcodeCmd, barcodeCmd.length);
            if (ret != 0) {
                throw getException(ret);
            }
        } else {
            throw new NullPointerException();
        }
    }

    public static synchronized void clearString() throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            int ret = clear_string();
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static synchronized void printString() throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            int ret = print_and_walk(0, 0, 0);
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static synchronized void printStringAndWalk(int direction, int mode, int lines) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else if (direction != 1 && direction != 0) {
            throw new IllegalArgumentException();
        } else if (mode != 1 && mode != 0) {
            throw new IllegalArgumentException();
        } else {
            int ret = print_and_walk(direction, mode, lines);
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static synchronized void setLineSpace(int lineSpace) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else if (lineSpace >= 0 && lineSpace <= 255) {
            int ret = line_space(lineSpace);
            if (ret != 0) {
                throw getException(ret);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static synchronized void setLeftIndent(int space) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else if (space >= 0 && space <= 255) {
            int ret = indent(space);
            if (ret != 0) {
                throw getException(ret);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static synchronized void printBarcode(String barcode) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else if (barcode != null && barcode.length() != 0 && barcode.length() <= 12 && barcode.length() >= 11) {
            if (!isNumeric(barcode)) {
                throw new IllegalArgumentException();
            } else {
                Bitmap bitmap = CreateCode(barcode, BarcodeFormat.UPC_A, 360, 108);
                printLogo(bitmap);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static synchronized void printLogo(int width, int height, char[] logo) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException("The printer has not been init!");
        } else {
            int printer_type = get_printer_type();
            if (printer_type != 3 && printer_type != 4) {
                if (width > 384 || height % 8 != 0) {
                    throw new IllegalArgumentException("The width or the height of the image to print is illegal!");
                }
            } else if (width > 576 || width % 8 != 0) {
                throw new IllegalArgumentException("The width of the image to print is illegal!");
            }

            int ret = print_logo(width, height, logo);
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static synchronized void printLogo(Bitmap image) throws TelpoException {
        boolean height = false;
        boolean with = false;
        boolean temp = false;
        boolean row = false;
        boolean pix = false;
        int ss = 0;
        boolean ret = false;
        boolean printHeight = false;
        boolean printWidth = false;
        boolean widthLeft = false;
        boolean widthOctet = false;
        boolean width = false;
        boolean column = false;
        if (!openFlag) {
            throw new DeviceNotOpenException("The printer has not been init!");
        } else if (image == null) {
            throw new NullPointerException();
        } else {
            Log.i("ThermalPrinter", "width:" + image.getWidth() + "height:" + image.getHeight() + "config:" + image.getConfig());
            int printer_type = get_printer_type();
            char[] Imagelogo;
            int var19;
            int var20;
            int var21;
            int var22;
            if (printer_type != 3 && printer_type != 4) {
                if (image.getWidth() > 384 || image.getHeight() < 1) {
                    throw new IllegalArgumentException("The width or the height of the image to print is illegal!");
                }

                int var23;
                if (image.getHeight() % 8 != 0) {
                    var23 = (image.getHeight() / 8 + 1) * 8;
                } else {
                    var23 = image.getHeight();
                }

                Imagelogo = new char[image.getWidth() * var23 / 8];
                int var17;
                int var18;
                if (image.getConfig().equals(Config.ARGB_8888)) {
                    var20 = 0;

                    while (true) {
                        if (var20 >= image.getHeight() / 8) {
                            Log.i("ThermalPrinter", "dealing ARGB_8888 image");
                            break;
                        }

                        for (var18 = 0; var18 < image.getWidth(); ++var18) {
                            var19 = 0;

                            for (var17 = var20 * 8; var17 < var20 * 8 + 8; ++var17) {
                                var21 = image.getPixel(var18, var17);
                                if ((var21 & 16711680) >> 16 > 128 && (var21 & '\uff00') >> 8 > 128 && (var21 & 255) > 128) {
                                    var19 <<= 1;
                                } else {
                                    var19 = (var19 << 1) + 1;
                                }
                            }

                            Imagelogo[ss] = (char) var19;
                            ++ss;
                        }

                        ++var20;
                    }
                } else if (image.getConfig().equals(Config.ALPHA_8)) {
                    var20 = 0;

                    while (true) {
                        if (var20 >= image.getHeight() / 8) {
                            Log.i("ThermalPrinter", "dealing ALPHA_8 image");
                            break;
                        }

                        for (var18 = 0; var18 < image.getWidth(); ++var18) {
                            var19 = 0;

                            for (var17 = var20 * 8; var17 < var20 * 8 + 8; ++var17) {
                                var21 = image.getPixel(var18, var17);
                                if ((var21 & 255) > 128) {
                                    var19 *= 2;
                                    var19 <<= 1;
                                } else {
                                    var19 = (var19 << 1) + 1;
                                }
                            }

                            Imagelogo[ss] = (char) var19;
                            ++ss;
                        }

                        ++var20;
                    }
                } else if (!image.getConfig().equals(Config.RGB_565)) {
                    Log.e("ThermalPrinter", "unsupport image formate!");
                } else {
                    var20 = 0;

                    while (true) {
                        if (var20 >= image.getHeight() / 8) {
                            Log.i("ThermalPrinter", "dealing RGB_565 image");
                            break;
                        }

                        for (var18 = 0; var18 < image.getWidth(); ++var18) {
                            var19 = 0;

                            for (var17 = var20 * 8; var17 < var20 * 8 + 8; ++var17) {
                                var21 = image.getPixel(var18, var17);
                                if ((var21 & '\uf800') >> 11 > 15 && (var21 & 2016) >> 5 > 30 && (var21 & 31) > 15) {
                                    var19 <<= 1;
                                } else {
                                    var19 = (var19 << 1) + 1;
                                }
                            }

                            Imagelogo[ss] = (char) var19;
                            ++ss;
                        }

                        ++var20;
                    }
                }

                var22 = print_logo(image.getWidth(), var23, Imagelogo);
            } else {
                if (image.getWidth() > 576 || image.getHeight() < 1) {
                    throw new IllegalArgumentException("The width or the height of the image to print is illegal!");
                }

                int var25 = image.getWidth() % 8;
                int var24;
                if (var25 != 0) {
                    var24 = image.getWidth() - var25 + 8;
                } else {
                    var24 = image.getWidth();
                }

                Imagelogo = new char[var24 / 8 * image.getHeight()];
                int i;
                int var26;
                int var27;
                int var28;
                if (image.getConfig().equals(Config.ARGB_8888)) {
                    var26 = image.getWidth() / 8;
                    var20 = 0;

                    while (true) {
                        if (var20 >= image.getHeight()) {
                            Log.i("ThermalPrinter", "dealing ARGB_8888 image");
                            break;
                        }

                        var27 = 0;

                        for (var28 = 0; var27 < var26; var28 += 8) {
                            var19 = 0;

                            for (i = 0; i < 8; ++i) {
                                var21 = image.getPixel(i + var28, var20);
                                if ((var21 & 16711680) >> 16 > 128 && (var21 & '\uff00') >> 8 > 128 && (var21 & 255) > 128) {
                                    var19 <<= 1;
                                } else {
                                    var19 = (var19 << 1) + 1;
                                }
                            }

                            Imagelogo[ss] = (char) var19;
                            ++ss;
                            ++var27;
                        }

                        if (var25 != 0) {
                            var19 = 0;
                            i = 0;

                            while (true) {
                                if (i >= var25) {
                                    var19 <<= 8 - var25;
                                    Imagelogo[ss] = (char) var19;
                                    ++ss;
                                    break;
                                }

                                var21 = image.getPixel(i + var28, var20);
                                if ((var21 & 16711680) >> 16 > 128 && (var21 & '\uff00') >> 8 > 128 && (var21 & 255) > 128) {
                                    var19 <<= 1;
                                } else {
                                    var19 = (var19 << 1) + 1;
                                }

                                ++i;
                            }
                        }

                        ++var20;
                    }
                } else if (image.getConfig().equals(Config.ALPHA_8)) {
                    var26 = image.getWidth() / 8;
                    var20 = 0;

                    while (true) {
                        if (var20 >= image.getHeight()) {
                            Log.i("ThermalPrinter", "dealing ALPHA_8 image");
                            break;
                        }

                        var27 = 0;

                        for (var28 = 0; var27 < var26; var28 += 8) {
                            var19 = 0;

                            for (i = 0; i < 8; ++i) {
                                var21 = image.getPixel(i + var28, var20);
                                if ((var21 & 255) > 128) {
                                    var19 <<= 1;
                                } else {
                                    var19 = (var19 << 1) + 1;
                                }
                            }

                            Imagelogo[ss] = (char) var19;
                            ++ss;
                            ++var27;
                        }

                        if (var25 != 0) {
                            var19 = 0;

                            for (i = 0; i < var25; ++i) {
                                var21 = image.getPixel(i + var28, var20);
                                if ((var21 & 255) > 128) {
                                    var19 <<= 1;
                                } else {
                                    var19 = (var19 << 1) + 1;
                                }
                            }

                            var19 <<= 8 - var25;
                            Imagelogo[ss] = (char) var19;
                            ++ss;
                        }

                        ++var20;
                    }
                } else if (!image.getConfig().equals(Config.RGB_565)) {
                    Log.e("ThermalPrinter", "unsupport image formate!");
                } else {
                    var26 = image.getWidth() / 8;
                    var20 = 0;

                    while (true) {
                        if (var20 >= image.getHeight()) {
                            Log.i("ThermalPrinter", "dealing RGB_565 image");
                            break;
                        }

                        var27 = 0;

                        for (var28 = 0; var27 < var26; var28 += 8) {
                            var19 = 0;

                            for (i = 0; i < 8; ++i) {
                                var21 = image.getPixel(i + var28, var20);
                                if ((var21 & '\uf800') >> 11 > 15 && (var21 & 2016) >> 5 > 30 && (var21 & 31) > 15) {
                                    var19 <<= 1;
                                } else {
                                    var19 = (var19 << 1) + 1;
                                }
                            }

                            Imagelogo[ss] = (char) var19;
                            ++ss;
                            ++var27;
                        }

                        if (var25 != 0) {
                            var19 = 0;
                            i = 0;

                            while (true) {
                                if (i >= var25) {
                                    var19 <<= 8 - var25;
                                    Imagelogo[ss] = (char) var19;
                                    ++ss;
                                    break;
                                }

                                var21 = image.getPixel(i + var28, var20);
                                if ((var21 & '\uf800') >> 11 > 15 && (var21 & 2016) >> 5 > 30 && (var21 & 31) > 15) {
                                    var19 <<= 1;
                                } else {
                                    var19 = (var19 << 1) + 1;
                                }

                                ++i;
                            }
                        }

                        ++var20;
                    }
                }

                var22 = print_logo(var24, image.getHeight(), Imagelogo);
            }

            if (var22 != 0) {
                if (var22 == '\ufff2') {
                    throw new IllegalArgumentException();
                } else {
                    throw getException(var22);
                }
            }
        }
    }

    public static synchronized String getVersion() throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            byte[] version = new byte[128];
            int ret = get_version(version);
            if (ret != 0) {
                throw getException(ret);
            } else {
                return new String(version);
            }
        }
    }

    public static synchronized void printLogo(Bitmap image, int mode) throws TelpoException {
        boolean height = false;
        byte width = 0;
        boolean temp = false;
        boolean row = false;
        boolean pix = false;
        boolean ss = false;
        boolean ret = false;
        boolean printHeight = false;
        boolean printWidth = false;
        boolean widthLeft = false;
        boolean widthOctet = false;
        boolean column = false;
        if (!openFlag) {
            throw new DeviceNotOpenException("The printer has not been init!");
        } else if (image == null) {
            throw new NullPointerException();
        } else {
            Log.i("ThermalPrinter", "width:" + image.getWidth() + "height:" + image.getHeight() + "config:" + image.getConfig());
            int printer_type = get_printer_type();
            boolean initWidth;
            char[] Imagelogo;
            int var19;
            int var20;
            int var21;
            int var22;
            int var23;
            int var24;
            int var26;
            int var30;
            if (printer_type != 3 && printer_type != 4) {
                int var25;
                if (image.getHeight() % 8 != 0) {
                    var25 = (image.getHeight() / 8 + 1) * 8;
                } else {
                    var25 = image.getHeight();
                }

                if (image.getWidth() > 384) {
                    throw new IllegalArgumentException("The width or the height of the image to print is illegal!");
                }

                initWidth = false;
                printWidth = false;
                switch (mode) {
                    case 0:
                        var26 = image.getWidth();
                        var30 = 0;
                        break;
                    case 1:
                        var26 = (384 - image.getWidth()) / 2 + image.getWidth();
                        var30 = (384 - image.getWidth()) / 2;
                        break;
                    case 2:
                        var26 = 384;
                        var30 = 384 - image.getWidth();
                        break;
                    default:
                        throw new IllegalArgumentException("The mode algin of the image to print is illegal!");
                }

                Imagelogo = new char[var26 * var25 / 8];
                Log.e("ThermalPrinter", ":" + var30 + ":" + var26 + ":" + var25);
                var23 = var30;
                int var18;
                if (image.getConfig().equals(Config.ARGB_8888)) {
                    var21 = 0;

                    while (true) {
                        if (var21 >= var25 / 8) {
                            Log.i("ThermalPrinter", "dealing ARGB_8888 image");
                            break;
                        }

                        for (var19 = var30; var19 < var30 + image.getWidth(); ++var19) {
                            var20 = 0;

                            for (var18 = var21 * 8; var18 < Math.min(var21 * 8 + 8, image.getHeight()); ++var18) {
                                var22 = image.getPixel(var19 - var30, var18);
                                if ((var22 & 16711680) >> 16 > 128 && (var22 & '\uff00') >> 8 > 128 && (var22 & 255) > 128) {
                                    var20 <<= 1;
                                } else {
                                    var20 = (var20 << 1) + 1;
                                }
                            }

                            Imagelogo[var23] = (char) var20;
                            ++var23;
                        }

                        var23 += var30;
                        ++var21;
                    }
                } else if (image.getConfig().equals(Config.ALPHA_8)) {
                    var21 = 0;

                    while (true) {
                        if (var21 >= var25 / 8) {
                            Log.i("ThermalPrinter", "dealing ALPHA_8 image");
                            break;
                        }

                        for (var19 = var30; var19 < var30 + image.getWidth(); ++var19) {
                            var20 = 0;

                            for (var18 = var21 * 8; var18 < Math.min(var21 * 8 + 8, image.getHeight()); ++var18) {
                                var22 = image.getPixel(var19 - var30, var18);
                                if ((var22 & 255) > 128) {
                                    var20 *= 2;
                                    var20 <<= 1;
                                } else {
                                    var20 = (var20 << 1) + 1;
                                }
                            }

                            Imagelogo[var23] = (char) var20;
                            ++var23;
                        }

                        var23 += var30;
                        ++var21;
                    }
                } else if (!image.getConfig().equals(Config.RGB_565)) {
                    Log.e("ThermalPrinter", "unsupport image formate!");
                } else {
                    var21 = 0;

                    while (true) {
                        if (var21 >= var25 / 8) {
                            Log.i("ThermalPrinter", "dealing RGB_565 image");
                            break;
                        }

                        for (var18 = var21 * 8; var18 < Math.min(var21 * 8 + 8, image.getHeight()); ++var18) {
                            var20 = 0;

                            for (var18 = var21 * 8; var18 < var21 * 8 + 8; ++var18) {
                                var22 = image.getPixel(width - var30, var18);
                                if ((var22 & '\uf800') >> 11 > 15 && (var22 & 2016) >> 5 > 30 && (var22 & 31) > 15) {
                                    var20 <<= 1;
                                } else {
                                    var20 = (var20 << 1) + 1;
                                }
                            }

                            Imagelogo[var23] = (char) var20;
                            ++var23;
                        }

                        var23 += var30;
                        ++var21;
                    }
                }

                var24 = print_logo(var26, var25, Imagelogo);
            } else {
                if (image.getWidth() > 576 || image.getHeight() < 1) {
                    throw new IllegalArgumentException("The width or the height of the image to print is illegal!");
                }

                int var27 = image.getWidth() % 8;
                if (var27 != 0) {
                    var26 = image.getWidth() - var27 + 8;
                } else {
                    var26 = image.getWidth();
                }

                initWidth = false;
                switch (mode) {
                    case 0:
                        var30 = 0;
                        break;
                    case 1:
                        var30 = (576 - var26) / 2;
                        var20 = var30 % 8;
                        if (var20 != 0) {
                            var30 = var30 - var20 + 8;
                        }

                        var26 += var30;
                        break;
                    case 2:
                        var30 = 576 - var26;
                        var26 = 576;
                        break;
                    default:
                        throw new IllegalArgumentException("The mode algin of the image to print is illegal!");
                }

                Log.i("ThermalPrinter", "printWidth: " + var26);
                Imagelogo = new char[var26 / 8 * image.getHeight()];
                var30 /= 8;
                var23 = var30;
                int i;
                int var28;
                int var29;
                if (image.getConfig().equals(Config.ARGB_8888)) {
                    var28 = image.getWidth() / 8;
                    var21 = 0;

                    while (true) {
                        if (var21 >= image.getHeight()) {
                            Log.i("ThermalPrinter", "dealing ARGB_8888 image");
                            break;
                        }

                        var19 = 0;

                        for (var29 = 0; var19 < var28; var29 += 8) {
                            var20 = 0;

                            for (i = 0; i < 8; ++i) {
                                var22 = image.getPixel(i + var29, var21);
                                if ((var22 & 16711680) >> 16 > 128 && (var22 & '\uff00') >> 8 > 128 && (var22 & 255) > 128) {
                                    var20 <<= 1;
                                } else {
                                    var20 = (var20 << 1) + 1;
                                }
                            }

                            Imagelogo[var23] = (char) var20;
                            ++var23;
                            ++var19;
                        }

                        if (var27 != 0) {
                            var20 = 0;
                            i = 0;

                            while (true) {
                                if (i >= var27) {
                                    var20 <<= 8 - var27;
                                    Imagelogo[var23] = (char) var20;
                                    ++var23;
                                    break;
                                }

                                var22 = image.getPixel(i + var29, var21);
                                if ((var22 & 16711680) >> 16 > 128 && (var22 & '\uff00') >> 8 > 128 && (var22 & 255) > 128) {
                                    var20 <<= 1;
                                } else {
                                    var20 = (var20 << 1) + 1;
                                }

                                ++i;
                            }
                        }

                        var23 += var30;
                        ++var21;
                    }
                } else if (image.getConfig().equals(Config.ALPHA_8)) {
                    var28 = image.getWidth() / 8;
                    var21 = 0;

                    while (true) {
                        if (var21 >= image.getHeight()) {
                            Log.i("ThermalPrinter", "dealing ALPHA_8 image");
                            break;
                        }

                        var19 = 0;

                        for (var29 = 0; var19 < var28; var29 += 8) {
                            var20 = 0;

                            for (i = 0; i < 8; ++i) {
                                var22 = image.getPixel(i + var29, var21);
                                if ((var22 & 255) > 128) {
                                    var20 <<= 1;
                                } else {
                                    var20 = (var20 << 1) + 1;
                                }
                            }

                            Imagelogo[var23] = (char) var20;
                            ++var23;
                            ++var19;
                        }

                        if (var27 != 0) {
                            var20 = 0;

                            for (i = 0; i < var27; ++i) {
                                var22 = image.getPixel(i + var29, var21);
                                if ((var22 & 255) > 128) {
                                    var20 <<= 1;
                                } else {
                                    var20 = (var20 << 1) + 1;
                                }
                            }

                            var20 <<= 8 - var27;
                            Imagelogo[var23] = (char) var20;
                            ++var23;
                        }

                        var23 += var30;
                        ++var21;
                    }
                } else if (!image.getConfig().equals(Config.RGB_565)) {
                    Log.e("ThermalPrinter", "unsupport image formate!");
                } else {
                    var28 = image.getWidth() / 8;
                    var21 = 0;

                    while (true) {
                        if (var21 >= image.getHeight()) {
                            Log.i("ThermalPrinter", "dealing RGB_565 image");
                            break;
                        }

                        var19 = 0;

                        for (var29 = 0; var19 < var28; var29 += 8) {
                            var20 = 0;

                            for (i = 0; i < 8; ++i) {
                                var22 = image.getPixel(i + var29, var21);
                                if ((var22 & '\uf800') >> 11 > 15 && (var22 & 2016) >> 5 > 30 && (var22 & 31) > 15) {
                                    var20 <<= 1;
                                } else {
                                    var20 = (var20 << 1) + 1;
                                }
                            }

                            Imagelogo[var23] = (char) var20;
                            ++var23;
                            ++var19;
                        }

                        if (var27 != 0) {
                            var20 = 0;
                            i = 0;

                            while (true) {
                                if (i >= var27) {
                                    var20 <<= 8 - var27;
                                    Imagelogo[var23] = (char) var20;
                                    ++var23;
                                    break;
                                }

                                var22 = image.getPixel(i + var29, var21);
                                if ((var22 & '\uf800') >> 11 > 15 && (var22 & 2016) >> 5 > 30 && (var22 & 31) > 15) {
                                    var20 <<= 1;
                                } else {
                                    var20 = (var20 << 1) + 1;
                                }

                                ++i;
                            }
                        }

                        var23 += var30;
                        ++var21;
                    }
                }

                var24 = print_logo(var26, image.getHeight(), Imagelogo);
            }

            if (var24 != 0) {
                throw getException(var24);
            }
        }
    }

    private static Bitmap CreateCode(String str, BarcodeFormat type, int bmpWidth, int bmpHeight) throws InternalErrorException {
        BitMatrix matrix = null;

        try {
            matrix = (new MultiFormatWriter()).encode(str, type, bmpWidth, bmpHeight);
        } catch (WriterException var10) {
            var10.printStackTrace();
            throw new InternalErrorException("Failed to encode bitmap");
        }

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

        Bitmap var11 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        var11.setPixels(pixels, 0, width, 0, 0, width, height);
        return var11;
    }

    public static String getSDKVersion() {
        byte[] version = new byte[64];
        get_sdk_version(version);
        return new String(version);
    }

    public static void searchMark(int direction, int search_disdance, int walk_disdance) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            int ret = search_mark(direction, search_disdance, walk_disdance);
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static void paperCut() throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            int ret = paper_cut();
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static void sendCommand(String cmdStr) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else if (cmdStr == null) {
            throw new IllegalArgumentException();
        } else {
            byte[] cmd = str2BCD(cmdStr.replace(" ", ""));
            int ret = send_command(cmd, cmd.length);
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static void sendCommand(byte[] cmdStr, int len) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else if (cmdStr == null) {
            throw new IllegalArgumentException();
        } else {
            int ret = send_command(cmdStr, len);
            if (ret != 0) {
                throw getException(ret);
            }
        }
    }

    public static void setBold(boolean isBold) throws DeviceNotOpenException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            if (isBold) {
                set_bold(1);
            } else {
                set_bold(0);
            }

        }
    }

    private static byte[] str2BCD(String string) {
        String hexStr = "0123456789ABCDEF";
        int len = string.length();
        String str;
        if (len % 2 == 1) {
            str = string + "0";
            len = len + 1 >> 1;
        } else {
            str = string;
            len >>= 1;
        }

        byte[] bytes = new byte[len];
        int i = 0;

        for (int j = 0; i < len; j += 2) {
            byte high = (byte) (hexStr.indexOf(str.charAt(j)) << 4);
            byte low = (byte) hexStr.indexOf(str.charAt(j + 1));
            bytes[i] = (byte) (high | low);
            ++i;
        }

        return bytes;
    }
}

