package com.example.myplayer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.socks.library.KLog;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * class name：SIMCardNetInfo<BR>
 * class description：读取Sim卡信息<BR> 获取网络类型
 * PS： 必须在加入各种权限 <BR>
 * Date:2012-3-12<BR>
 *
 * @author CODYY)peijiangping
 * @version 1.00
 */
public class SIMCardNetInfo {
    //=========================== 获取网络信息=========================================
    public static final String NETWORN_NONE = "没有网络";
    public static final String NETWORN_WIFI = "WiFi";
    public static final String NETWORN_2G = "2G网络";
    public static final String NETWORN_3G = "3G网络";
    public static final String NETWORN_4G = "4G网络";
    public static final String NETWORN_MOBILE = "Web网络";
    private static Context mContext;
    /**
     * TelephonyManager提供设备上获取通讯服务信息的入口。 应用程序可以使用这个类方法确定的电信服务商和国家 以及某些类型的用户访问信息。
     * 应用程序也可以注册一个监听器到电话收状态的变化。不需要直接实例化这个类
     * 使用Context.getSystemService(Context.TELEPHONY_SERVICE)来获取这个类的实例。
     */
    private TelephonyManager telephonyManager;
    /**
     * 国际移动用户识别码
     */
    private String IMSI;
    public SIMCardNetInfo(Context context) {
        mContext = context;
        telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取当前网络连接类型
     *
     * @param
     * @return
     */
    public static String getNetworkState() {
        //获取系统的网络服务
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //如果当前没有网络
        if (null == connManager)
            return NETWORN_NONE;
        //获取当前网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORN_NONE;
        }
        // 判断是不是连接的是不是wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORN_WIFI;
                }
        }
        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        //如果是2g类型
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NETWORN_2G;
                        //如果是3g类型
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NETWORN_3G;
                        //如果是4g类型
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NETWORN_4G;
                        default:
                            //中国移动 联通 电信 三种3G制式
                            if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                return NETWORN_3G;
                            } else {
                                return NETWORN_MOBILE;
                            }
                    }
                }
        }
        return NETWORN_NONE;
    }

    /**
     * Role:获取当前设置的电话号码
     * <BR>Date:2012-3-12
     * <BR>@author CODYY)peijiangping
     */
    public String getNativePhoneNumber() {
        String NativePhoneNumber = null;
        NativePhoneNumber = telephonyManager.getLine1Number();
//        KLog.e(telephonyManager.getPhoneCount());
//        KLog.e(telephonyManager.getPhoneType());
//        KLog.e(telephonyManager.getAllCellInfo());
//        KLog.e(telephonyManager.getCellLocation());
//        KLog.e(telephonyManager.getSimCountryIso());
//        KLog.e(telephonyManager.getSimOperator());
//        KLog.e(telephonyManager.getSimOperatorName());
//        KLog.e(telephonyManager.getSimSerialNumber());
//        KLog.e(telephonyManager.getSimState());
        KLog.e(telephonyManager.getSubscriberId());
        return NativePhoneNumber;
    }

    public String getPhone() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nDeviceId(IMEI) = " + telephonyManager.getDeviceId());
        sb.append("\nDeviceSoftwareVersion = " + telephonyManager.getDeviceSoftwareVersion());
        sb.append("\nLine1Number = " + telephonyManager.getLine1Number());
        sb.append("\nNetworkCountryIso = " + telephonyManager.getNetworkCountryIso());
        sb.append("\nNetworkOperator = " + telephonyManager.getNetworkOperator());
        sb.append("\nNetworkOperatorName = " + telephonyManager.getNetworkOperatorName());
        sb.append("\nNetworkType = " + telephonyManager.getNetworkType());
        sb.append("\nPhoneType = " + telephonyManager.getPhoneType());
        sb.append("\nSimCountryIso = " + telephonyManager.getSimCountryIso());
        sb.append("\nSimOperator = " + telephonyManager.getSimOperator());
        sb.append("\nSimOperatorName = " + telephonyManager.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + telephonyManager.getSimSerialNumber());
        sb.append("\nSimState = " + telephonyManager.getSimState());
        sb.append("\nSubscriberId(IMSI) = " + telephonyManager.getSubscriberId());
        sb.append("\nVoiceMailNumber = " + telephonyManager.getVoiceMailNumber());
        return sb.toString();
    }

    /**
     * Role:Telecom service providers获取手机服务商信息 <BR>
     * 需要加入权限<uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/> <BR>
     * Date:2012-3-12 <BR>
     *
     * @author CODYY)peijiangping
     */
    public String getProvidersName() {
        String ProvidersName = null;
        // 返回唯一的用户ID;就是这张卡的编号神马的
        IMSI = telephonyManager.getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        System.out.println(IMSI);
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;
    }

    /**
     * 获取网络IP 地址  WiFi 和 移动网络 通用
     *
     * @return
     */
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        //if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取WiFi 网络的IP地址  包括打开WiFi
     *
     * @return
     */
    public String getWiFiLocalIpAddress() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();

        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }

}