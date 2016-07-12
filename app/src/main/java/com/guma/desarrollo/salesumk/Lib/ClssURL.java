package com.guma.desarrollo.salesumk.Lib;

/**
 * Created by marangelo.php on 25/05/2016.
 */
public class ClssURL {
    private static String SERVER = "192.168.1.78:8448";
    //private static String SERVER = "165.98.75.219:8448";

    private static String URL_INVE  = "http://"+ SERVER +"/APIREST/Inventario.php";
    private static String URL_login = "http://"+ SERVER +"/APIREST/Login.php";
    private static String URL_liq6  = "http://"+ SERVER +"/APIREST/InventarioLiq6.php";
    private static String URL_liq12 = "http://"+ SERVER +"/APIREST/InventarioLiq12.php";
    private static String URL_mtlc = "http://"+ SERVER +"/APIREST/MTCL.php";
    private static String URL_ExistenciaLotes = "http://"+ SERVER +"/APIREST/ExistenciaLotes.php";

    private static String URL_Factura = "http://"+ SERVER +"/APIREST/FACTURAS.php";

    public ClssURL() {
    }

    public static String getSERVER() {
        return SERVER;
    }

    public static void setSERVER(String SERVER) {
        ClssURL.SERVER = SERVER;
    }

    public static String getUrlInve() {
        return URL_INVE;
    }

    public static void setUrlInve(String urlInve) {
        URL_INVE = urlInve;
    }

    public static String getURL_login() {
        return URL_login;
    }

    public static void setURL_login(String URL_login) {
        ClssURL.URL_login = URL_login;
    }

    public static String getURL_liq6() {
        return URL_liq6;
    }

    public static void setURL_liq6(String URL_liq6) {
        ClssURL.URL_liq6 = URL_liq6;
    }

    public static String getURL_liq12() {
        return URL_liq12;
    }

    public static void setURL_liq12(String URL_liq12) {
        ClssURL.URL_liq12 = URL_liq12;
    }

    public static String getURL_mtlc() {
        return URL_mtlc;
    }

    public static void setURL_mtlc(String URL_mtlc) {
        ClssURL.URL_mtlc = URL_mtlc;
    }

    public static String getURL_ExistenciaLotes() {
        return URL_ExistenciaLotes;
    }

    public static void setURL_ExistenciaLotes(String URL_ExistenciaLotes) {
        ClssURL.URL_ExistenciaLotes = URL_ExistenciaLotes;
    }

    public static String getURL_Factura() {
        return URL_Factura;
    }

    public static void setURL_Factura(String URL_Factura) {
        ClssURL.URL_Factura = URL_Factura;
    }
}