package com.moregood.kit.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.moregood.kit.livedatas.NetWorkData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/11/18.
 */
public class NetWorkUtils {

    public static int getWifiStrength(Context context, int defaultValue) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getBSSID() != null) {
            return WifiManager.calculateSignalLevel(info.getRssi(), defaultValue);
        }
        return defaultValue;
    }

    public static int getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        int mNetWorkType = NetWorkData.NETWORKTYPE_INVALID;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("ETHERNET")) {
                mNetWorkType = NetWorkData.NETWORKTYPE_ETHERNET;
            }else if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NetWorkData.NETWORKTYPE_WIFI;
            }else if(type.equalsIgnoreCase("MOBILE")){
                mNetWorkType = NetWorkData.NETWORKTYPE_MOBILE;
            }

        }
        return mNetWorkType;
    }

    //判断有线网络是否连接上
    public static boolean isEthernetConnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission")
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    //判断wifi是否连接上
    public static boolean isWifiConnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission")
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测是否有网络(包含所有的网络)
     */
    public static boolean isNetworkOpen(Context ctx) {
        ConnectivityManager connectivity = (ConnectivityManager) ctx.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            @SuppressLint("MissingPermission")
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取有线网络的网关
     *
     * @return
     */
    public String getGateWay() {
        String line = "";
        String ret = "";
        String cmd = "getprop";
        String result = null;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(cmd);
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if ((line.contains("eth0") || line.contains("eth1") || line.contains("eth2"))
                        && (line.contains("gateway"))) {
                    ret = line;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (ret.length() > 0 && ret.contains("gateway")) {
            List<String> list = new ArrayList<String>();
            Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
            Matcher m = p.matcher(ret);
            while (m.find()) {
                list.add(m.group().substring(1, m.group().length() - 1));
            }
            result = list.get(1);
        }

        return result;

    }

    /**
     * 获取有线网络的IP地址
     *
     * @return
     */
    public String getIpAddress() {
        String line = "";
        String ret = "";
        String cmd = "getprop";
        String result = null;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(cmd);
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if ((line.contains("eth0") || line.contains("eth1") || line.contains("eth2"))
                        && (line.contains("ipaddress"))) {
                    ret = line;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (ret.length() > 0 && ret.contains("ipaddress")) {
            List<String> list = new ArrayList<String>();
            Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
            Matcher m = p.matcher(ret);
            while (m.find()) {
                list.add(m.group().substring(1, m.group().length() - 1));
            }
            result = list.get(1);
        }

        return result;

    }

    /**
     * 获取有线网络的子网掩码
     *
     * @return
     */
    public String getIpMask() {
        String line = "";
        String ret = "";
        String cmd = "getprop";
        String result = null;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(cmd);
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if ((line.contains("eth0") || line.contains("eth1") || line.contains("eth2"))
                        && (line.contains("mask"))) {
                    ret = line;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (ret.length() > 0 && ret.contains("mask")) {
            List<String> list = new ArrayList<String>();
            Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
            Matcher m = p.matcher(ret);
            while (m.find()) {
                list.add(m.group().substring(1, m.group().length() - 1));
            }
            result = list.get(1);
        }

        return result;

    }

    /**
     * 获取有线网络的dns服务器
     *
     * @return
     */
    public String getIpDns() {
        String line = "";
        String ret = "";
        String cmd = "getprop";
        String result = null;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(cmd);
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if ((line.contains("eth0") || line.contains("eth1") || line.contains("eth2"))
                        && (line.contains("dns1"))) {
                    ret = line;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (ret.length() > 0 && ret.contains("dns1")) {
            List<String> list = new ArrayList<String>();
            Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
            Matcher m = p.matcher(ret);
            while (m.find()) {
                list.add(m.group().substring(1, m.group().length() - 1));
            }
            result = list.get(1);
        }

        return result;
    }

    /**
     * 得到有限线网关的IP地址
     *
     * @return
     */
    public String getLocalIp() {

        try {
            // 获取本地设备的所有网络接口
            Enumeration<NetworkInterface> enumerationNi = NetworkInterface.getNetworkInterfaces();
            while (enumerationNi.hasMoreElements()) {
                NetworkInterface networkInterface = enumerationNi.nextElement();
                String interfaceName = networkInterface.getDisplayName();
                Log.i("tag", "网络名字" + interfaceName);

                // 如果是有线网卡
                if (interfaceName.equals("eth0")) {
                    Enumeration<InetAddress> enumIpAddr = networkInterface
                            .getInetAddresses();

                    while (enumIpAddr.hasMoreElements()) {
                        // 返回枚举集合中的下一个IP地址信息
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        // 不是回环地址，并且是ipv4的
                        if (!inetAddress.isLoopbackAddress()
                                && inetAddress instanceof Inet4Address) {
                            Log.i("tag", inetAddress.getHostAddress() + "   ");

                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 取得 Mac-Address
     * 1. 先取得 eth0 Mac-Address
     * 2. 再取得 wlan0 Mac-Address
     *
     * @return
     */
    public static String getDeviceMacAddress() {
        String mac = !getEth0Mac().equals("Didn\'t get eth0 MAC address") ? getEth0Mac() : getWlan0Mac();
        Log.d("MAC Address", "Mac eth0:" + getEth0Mac());
        Log.d("MAC Address", "Mac wlan0:" + getWlan0Mac());
        return mac;
    }

    /**
     * wlan0 MAC地址获取，适用api9 - api24
     */
    public static String getWlan0Mac() {

        String Mac = "";
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "Didn\'t get Wlan0 MAC address";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                    Mac = res1.toString();
                }
                return Mac;
            }
        } catch (Exception ex) {
        }
        return "Didn\'t get Wlan0 address";
    }

    /**
     * eth0 MAC地址获取，适用api9 - api24
     */
    public static String getEth0Mac() {

        String Mac = "";
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("eth0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "Didn\'t get eth0 MAC address";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                    Mac = res1.toString();
                }
                return Mac;
            }
        } catch (Exception ex) {
        }
        return "Didn\'t get eth0 MAC address";
    }

}
