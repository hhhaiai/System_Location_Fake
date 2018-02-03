package com.leyuwei.tfdsfake;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Random;

import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookLoadPackage{
	final static public String telPac = "android.telephony.TelephonyManager";
	final static public String wifiPac = "android.net.wifi.WifiManager";
	final static public String locPac = "android.location.Location";
	final static public int flagSelf = 0;
	final static public int flagWY = 1;
	
	//®ɽ·��31.9955200394,118.7272030762
	//��ɽ�֣�32.0263185088, 118.7876922371
	public double latitude = (double) 32.0263185088;
	public double lontitude = (double) 118.7876922371;
	
	public static boolean isEnabledFake = true;
	private final static String picPathFinal="tfdsTEMP.jpg";
	private static int whichPac=-1;
	private XSharedPreferences pre;
	
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		pre = new XSharedPreferences("com.leyuwei.tfdsfake", "Global");
		isEnabledFake = pre.getBoolean("isON", true);
		Random r = new Random();
		if (!(lpparam.packageName.equals("com.baidu.BaiduMap")||lpparam.packageName.equals("com.autonavi.minimap")||lpparam.packageName.equals("com.ebeitech.pn")||lpparam.packageName.equals("com.leyuwei.tfdsfake")||lpparam.packageName.equals("com.leyuwei.pmtestbt")))  
            return;
		if(!isEnabledFake)
			return;
		XposedBridge.log("Loaded app: " + lpparam.packageName);
		
		/*String rec = pre.getString("ll", "32.0263185088+118.7876922371");
		latitude = Float.valueOf(rec.split("\\+")[0]);
		lontitude = Float.valueOf(rec.split("\\+")[1]);*/
		
		if(lpparam.packageName.equals("com.ebeitech.pn")){
			whichPac = flagWY;
		}else if(lpparam.packageName.equals("com.leyuwei.tfdsfake")){
			whichPac = flagSelf;
		}else{
			whichPac = -1;
		}
		
		hook_gps_set_null(telPac,lpparam,"getCellLocation");
    	hook_gps_set_null(telPac,lpparam,"getNeighboringCellInfo");
    	fake_all_cell();
    	fake_all_location(lpparam);
    	fake_gps_data(lpparam);
		final LoadPackageParam lpfinal = lpparam;
		fake_photo_and_id(lpparam);
	}

	public void addLog(TextView tv, String x){
		tv.append("\n"+x);
	}
	
	public void fake_all_cell(){
		XposedHelpers.findAndHookMethod(TelephonyManager.class, "getAllCellInfo", new XC_MethodHook() {  
            @Override  
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {  
                param.setResult(null);  
            }  
        });
	}
	
	public void fake_all_location(LoadPackageParam lpparam){
		//Multidex����������
		if(whichPac == flagSelf){
			/*XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
	            @Override
	            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
	                ClassLoader cl = ((Context)param.args[0]).getClassLoader();
	                Class<?> hookclass = null;
	                try {
	                    hookclass = cl.loadClass("com.leyuwei.tfdsfake.GlobalVar");
	                    XposedBridge.log("�ҵ���Multidex����������");
	                } catch (Exception e) {
	                	XposedBridge.log("û���ҵ� Multidex����������");
	                    return;
	                }
	                XposedHelpers.findAndHookMethod(hookclass,  "notifyXpos", String.class, new XC_MethodHook() {  
	                    @Override  
	                    protected void beforeHookedMethod(MethodHookParam param)  
	                            throws Throwable {  
	                    	if(((String) param.args[0]).trim().equals("")||param.args[0]==null) return;
	                        picPathFinal = (String) param.args[0];
	                        XposedBridge.log("���Ѿ���picPath��ȡ�����ˣ���"+picPathFinal);
	                    }  
	                }); 
	            }
	        });*/
		}else if(whichPac == flagWY){
			
		}
		
		// 2018.02.02 ���漸���кܶ��ǵ��õ�ϵͳ��������Ҫ���л����޶���ֻ�޶����θ��»����ķ�������
		XposedHelpers.findAndHookMethod("android.location.Location", lpparam.classLoader, "getLatitude", new XC_MethodHook() {  
            @Override  
            protected void beforeHookedMethod(MethodHookParam param)  
                    throws Throwable {  
                // TODO Auto-generated method stub  
                super.beforeHookedMethod(param); 
                if(!pre.getBoolean("isON", true)) return;
                String rec = pre.getString("ll", "32.0263185088+118.7876922371");
        		latitude = Double.valueOf(rec.split("\\+")[0]);
                param.setResult(latitude);  
            }  
        });  
        // ����  
        XposedHelpers.findAndHookMethod("android.location.Location", lpparam.classLoader, "getLongitude", new XC_MethodHook() {  
            @Override  
            protected void beforeHookedMethod(MethodHookParam param)  
                    throws Throwable {  
                // TODO Auto-generated method stub  
                super.beforeHookedMethod(param);
                if(!pre.getBoolean("isON", true)) return;
                String rec = pre.getString("ll", "32.0263185088+118.7876922371");
        		lontitude = Double.valueOf(rec.split("\\+")[1]);
                param.setResult(lontitude);  
            }  
        });
         XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo", lpparam.classLoader, "getBSSID", new XC_MethodHook() {  
             @Override  
             protected void afterHookedMethod(MethodHookParam param) throws Throwable {  
            	 if(!pre.getBoolean("isON", true)) return;
            	 param.setResult("00-00-00-00-00-00-00-00");  
             }  
         });  
         XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo", lpparam.classLoader, "getMacAddress", new XC_MethodHook() {  
             @Override  
             protected void afterHookedMethod(MethodHookParam param) throws Throwable {  
            	 if(!pre.getBoolean("isON", true)) return;
            	 param.setResult("00-00-00-00-00-00-00-00");  
             }  
         }); 
	   XposedHelpers.findAndHookMethod("android.net.wifi.WifiManager", lpparam.classLoader, "isWifiEnabled", new XC_MethodHook() {  
	       @Override  
	       protected void afterHookedMethod(MethodHookParam param) throws Throwable {  
	    	   if(!pre.getBoolean("isON", true)) return;
	    	   param.setResult(false);  
	       }  
	   });
	   //�жϸ�LocationProvider�Ƿ���Ҫ���������վ  
	   XposedHelpers.findAndHookMethod(LocationProvider.class, "requiresCell",  new XC_MethodHook() {  
	       @Override  
	       protected void afterHookedMethod(MethodHookParam param) throws Throwable {  
	    	   if(!pre.getBoolean("isON", true)) return;
	    	   param.setResult(false);  
	       }  
	   });  
	   //�жϸ�LocationProvider�Ƿ���Ҫ��������  
	   XposedHelpers.findAndHookMethod(LocationProvider.class, "requiresNetwork",  new XC_MethodHook() {  
	       @Override  
	       protected void afterHookedMethod(MethodHookParam param) throws Throwable {  
	    	   if(!pre.getBoolean("isON", true)) return;
	    	   param.setResult(false);  
	       }  
	   });  
	   XposedHelpers.findAndHookMethod(LocationManager.class, "getLastLocation", new XC_MethodHook() {  
	       @Override  
	       protected void afterHookedMethod(MethodHookParam param) throws Throwable {  
	    	   if(!pre.getBoolean("isON", true)) return;
	    	   Location l = new Location(LocationManager.GPS_PROVIDER);  
	           String rec = pre.getString("ll", "32.0263185088+118.7876922371");
	   			latitude = Double.valueOf(rec.split("\\+")[0]);
	   			lontitude = Double.valueOf(rec.split("\\+")[1]);
	           l.setLatitude(latitude);  
	           l.setLongitude(lontitude);  
	           l.setAccuracy(100f);  
	           l.setTime(0);
	           param.setResult(l);  
	       }  
	   }); 
	   XposedHelpers.findAndHookMethod(LocationManager.class, "getLastKnownLocation", String.class, new XC_MethodHook() {  
	       @Override  
	       protected void afterHookedMethod(MethodHookParam param) throws Throwable {  
	    	   if(!pre.getBoolean("isON", true)) return;
	    	   Location l = new Location(LocationManager.GPS_PROVIDER);
	           String rec = pre.getString("ll", "32.0263185088+118.7876922371");
	   			latitude = Double.valueOf(rec.split("\\+")[0]);
	   			lontitude = Double.valueOf(rec.split("\\+")[1]);
	           l.setLatitude(latitude);  
	           l.setLongitude(lontitude);  
	           l.setAccuracy(100f);  
	           l.setTime(0);
	           param.setResult(l);  
	       }  
	   });
	   XposedBridge.hookAllMethods(LocationManager.class, "getProviders", new XC_MethodHook() {  
	       @Override  
	       protected void afterHookedMethod(MethodHookParam param) throws Throwable {  
	    	   if(!pre.getBoolean("isON", true)) return;
	    	   ArrayList<String> arrayList = new ArrayList<String>();  
	           arrayList.add("gps");  
	           param.setResult(arrayList);  
	       }  
	   });
	   XposedHelpers.findAndHookMethod(LocationManager.class, "getBestProvider", Criteria.class, Boolean.TYPE, new XC_MethodHook() {  
	       @Override  
	       protected void afterHookedMethod(MethodHookParam param) throws Throwable {  
	    	   if(!pre.getBoolean("isON", true)) return;
	    	   param.setResult("gps");  
	       }  
	   });
	   XposedHelpers.findAndHookMethod(LocationManager.class, "addGpsStatusListener", GpsStatus.Listener.class, new XC_MethodHook() {  
	       @Override  
	       protected void afterHookedMethod(MethodHookParam param) throws Throwable {  
	    	   if(!pre.getBoolean("isON", true)) return;
	    	   if (param.args[0] != null) {  
	               XposedHelpers.callMethod(param.args[0], "onGpsStatusChanged", 1);  
	               XposedHelpers.callMethod(param.args[0], "onGpsStatusChanged", 3);  
	           }  
	       }  
	   });  
	}
	
	// 2018.02.02 ���ڴ�������Դ˴����񷽷��������޶�Ϊe
	public void fake_gps_data(LoadPackageParam lpparam){
		XposedHelpers.findAndHookMethod("com.ebeitech.application.QPIApplication", lpparam.classLoader, "e", new XC_MethodHook() {  
            @Override  
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {}  

            @Override  
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            	if(pre.getBoolean("isON", true)){
            		String rec = pre.getString("ll", "32.0263185088+118.7876922371");
            		latitude = Double.valueOf(rec.split("\\+")[0]);
            		String latitudes = String.valueOf(latitude);
            		param.setResult(latitudes);
            	}
            }  
        }); 
		
		// 2018.02.02 ���ڴ�������Դ˴����񷽷��������޶�Ϊd
		XposedHelpers.findAndHookMethod("com.ebeitech.application.QPIApplication", lpparam.classLoader, "d", new XC_MethodHook() {  
            @Override  
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {}  

            @Override  
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            	if(pre.getBoolean("isON", true)){	
            		String rec = pre.getString("ll", "32.0263185088+118.7876922371");
            		lontitude = Double.valueOf(rec.split("\\+")[1]);
            		String lontitudes = String.valueOf(lontitude);
            		param.setResult(lontitudes);
            	}
            }  
        }); 
		
		// 2018.02.02 �ٶȵ�SDK��û����ӻ���
		XposedHelpers.findAndHookMethod("com.baidu.location.BDLocation", lpparam.classLoader, "getLatitude", new XC_MethodHook() {  
            @Override  
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {}  

            @Override  
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            	if(pre.getBoolean("isON", true)){
            		String rec = pre.getString("ll", "32.0263185088+118.7876922371");
            		latitude = Double.valueOf(rec.split("\\+")[0]);
            		param.setResult(latitude);
            		XposedBridge.log("��Ӧ�ٶȵ�ͼ��ȡ��γ�Ȳ�����"+latitude);
            	}
            }  
        }); 
		
		// 2018.02.02 �ٶȵ�SDK��û����ӻ���
        XposedHelpers.findAndHookMethod("com.baidu.location.BDLocation", lpparam.classLoader, "getLongitude", new XC_MethodHook() {  
            @Override  
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {}  

            @Override  
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            	if(pre.getBoolean("isON", true)){
            		String rec = pre.getString("ll", "32.0263185088+118.7876922371");
        			lontitude = Double.valueOf(rec.split("\\+")[1]);
            		param.setResult(lontitude);
            	}
            }  
        });
				
		XposedHelpers.findAndHookMethod("android.location.LocationManager", lpparam.classLoader,  
                "getGpsStatus", GpsStatus.class, new XC_MethodHook() {  
                    @Override  
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {  
                    	if(!pre.getBoolean("isON", true)) return;
                    	GpsStatus gss = (GpsStatus) param.getResult();  
                        if (gss == null)  
                            return;  
                        Class<?> clazz = GpsStatus.class;  
                        Method m = null;  
                        for (Method method : clazz.getDeclaredMethods()) {  
                            if (method.getName().equals("setStatus")) {  
                                if (method.getParameterTypes().length > 1) {  
                                    m = method;  
                                    break;  
                                }  
                            }  
                        }  
                        if (m == null)  
                            return;  
  
                        //access the private setStatus function of GpsStatus  
                        m.setAccessible(true);  
  
                        //make the apps belive GPS works fine now  
                        int svCount = 5;  
                        int[] prns = {1, 2, 3, 4, 5};  
                        float[] snrs = {0, 0, 0, 0, 0};  
                        float[] elevations = {0, 0, 0, 0, 0};  
                        float[] azimuths = {0, 0, 0, 0, 0};  //�޸ģ�ԭ����float
                        int ephemerisMask = 0x1f;  
                        int almanacMask = 0x1f;  
  
                        //5 satellites are fixed  
                        int usedInFixMask = 0x1f;  
  
                        /*XposedHelpers.callMethod(gss, "setStatus", svCount, prns, snrs, elevations, azimuths, ephemerisMask, almanacMask, usedInFixMask);  
                        param.args[0] = gss;  
                        param.setResult(gss);*/
                        try {  
                            m.invoke(gss, svCount, prns, snrs, elevations, azimuths, ephemerisMask, almanacMask, usedInFixMask);  
                        } catch (Exception e) {  
                            //XposedBridge.log(e);  
                        }  
                        param.args[0] = gss;  
                        param.setResult(gss);
                    }  
                });  
  
        for (Method method : LocationManager.class.getDeclaredMethods()) {  
        	if(!pre.getBoolean("isON", true)) return;
            if (method.getName().equals("requestLocationUpdates")  
                    && !Modifier.isAbstract(method.getModifiers())  
                    && Modifier.isPublic(method.getModifiers())) {  
                XposedBridge.hookMethod(method, new XC_MethodHook() {  
                    @Override  
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {  
                        if (param.args.length >= 4 && (param.args[3] instanceof LocationListener)) {  
  
                            LocationListener ll = (LocationListener) param.args[3];  
  
                            Class<?> clazz = LocationListener.class;  
                            Method m = null;  
                            for (Method method : clazz.getDeclaredMethods()) {  
                                if (method.getName().equals("onLocationChanged") && !Modifier.isAbstract(method.getModifiers())) {  
                                    m = method;  
                                    break;  
                                }  
                            }  
                            Location l = new Location(LocationManager.GPS_PROVIDER);  
                            l.setLatitude(latitude);  
                            l.setLongitude(lontitude);  
                            l.setAccuracy(10.00f);  
                            l.setTime(0);  
                        /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { 
                                l.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos()); 
                            }*/  
                            XposedHelpers.callMethod(ll, "onLocationChanged", l);  
                            try {  
                                if (m != null) {  
                                    m.invoke(ll, l);  
                                }  
                            } catch (Exception e) {  
                                XposedBridge.log(e);  
                            }  
                        }  
                    }  
                });  
            }  
  
            if (method.getName().equals("requestSingleUpdate ")  
                    && !Modifier.isAbstract(method.getModifiers())  
                    && Modifier.isPublic(method.getModifiers())) {  
                XposedBridge.hookMethod(method, new XC_MethodHook() {  
                    @Override  
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {  
                        if (param.args.length >= 3 && (param.args[1] instanceof LocationListener)) {  
  
                            LocationListener ll = (LocationListener) param.args[3];  
  
                            Class<?> clazz = LocationListener.class;  
                            Method m = null;  
                            for (Method method : clazz.getDeclaredMethods()) {  
                                if (method.getName().equals("onLocationChanged") && !Modifier.isAbstract(method.getModifiers())) {  
                                    m = method;  
                                    break;  
                                }  
                            }  
  
                            try {  
                                if (m != null) {  
                                    Location l = new Location(LocationManager.GPS_PROVIDER);  
                                    l.setLatitude(latitude);  
                                    l.setLongitude(lontitude);                                  
                                    l.setAccuracy(100f);  
                                    l.setTime(0);  
                                /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { 
                                        l.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos()); 
                                    }*/  
                                    m.invoke(ll, l);  
                                }  
                            } catch (Exception e) {  
                                XposedBridge.log(e);  
                            }  
                        }  
                    }  
                });  
            }  
        }  
	}
	
	
	public void hook_gps_set_null(String activityName, LoadPackageParam lpparam, String methodName){
		XposedHelpers.findAndHookMethod(activityName, lpparam.classLoader, methodName, new XC_MethodHook() {  
  
                    @Override  
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {}  
  
                    @Override  
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {  
                    	if(!pre.getBoolean("isON", true)) return;
                    	param.setResult(null);
                    }  
                });  
	}
	
	public void fake_photo_and_id(LoadPackageParam lpparam){
		XposedHelpers.findAndHookMethod("android.content.Intent", lpparam.classLoader, "getStringExtra", String.class , new XC_MethodHook() {  
                    @Override  
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {}  
                    @Override  
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    	if(!pre.getBoolean("isON", true)) return;
                    	if(((String)param.args[0]).equals("bitmap path extra")){
                    		String orgPath = (String) param.getResult();
                    		String finalPath = orgPath.substring(0, orgPath.lastIndexOf(File.separator))+File.separator+picPathFinal;
                    		//XposedBridge.log(finalPath);
                    		param.setResult(finalPath);
                    		XposedBridge.log("α����ɣ�");
                    	}
                    }  
                });  
		XposedHelpers.findAndHookMethod("com.ebeitech.application.QPIApplication", lpparam.classLoader, "getString", String.class, String.class , new XC_MethodHook() {  
            @Override  
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            }  
            @Override  
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            	if(!pre.getBoolean("isON", true)) return;
            	if(!pre.getBoolean("isBoss", false)) return;
            	
            	if(((String)param.args[0]).trim().equals("userRole")){
            		param.setResult("qitazhuguan");
            		XposedBridge.log("α�������ɣ�");
            	}
            	if(((String)param.args[0]).trim().equals("permission")){
            		param.setResult("jiashicang|shenyue|weixiu|xiangmu|gongsi|wentigenzong|renwu|shebeixunjian|shebeiweibao|anfang|baoshi|cangku|qingliao|xiujia|fangke|shebeichakan");
            		XposedBridge.log("�����й���ģ����ɣ�_P1");
            	}
            	if(((String)param.args[0]).trim().equals("isManager")){
            		param.setResult("1");
            		XposedBridge.log("�����й���ģ����ɣ�_P2");
            	}
            }  
        });
		
	}

}
