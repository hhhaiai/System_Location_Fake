package com.leyuwei.tfdsfake;

import java.io.File;
import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.leyuwei.tfdsfake.MapDialog.onMapClickData;

public class MainActivity extends Activity implements OnClickListener,onMapClickData{
	private Button btn;
	private Button btn2;
	private Button btn3,btn4;
	private TextView tv,tv3;
	public boolean isEnabledFake,isBoss;
	private double lat = 32.0263185088f;
	private double lon = 118.7876922371f;
	public String picPath2 = "";
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private static final int PHOTO_REQUEST_GALLERY = 1;// �������ѡ��
	//��¼�û��״ε�����ؼ���ʱ��
    private long firstTime=0;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		setTitle("����ͣ");
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		
		
		btn = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		btn3 = (Button) findViewById(R.id.button3);
		btn4 = (Button) findViewById(R.id.button4);
		btn.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		tv = (TextView) findViewById(R.id.textView2);
		tv3 = (TextView) findViewById(R.id.textView3);
		tv3.setOnClickListener(this);
		isEnabledFake = true;
		isBoss = false;
		
		sp = getSharedPreferences("Global", Activity.MODE_WORLD_READABLE);
		editor = sp.edit();
		editor.putBoolean("isON", true);
		editor.putBoolean("isBoss", false);
		editor.putString("ll", "32.0263185088+118.7876922371");
		editor.commit();
		FileUtils.makeRootDirectory(FileUtils.SDPATH); //1.1�汾�޸�����
		if(sp.getBoolean("isFirst3.0", true)){
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle("�°汾˵��")
				   .setMessage("V3.0�汾�������ݣ�\n1.�����ҵ�ܼ��°汾���������˴����Ż�\n2.�Ż���GPS���ݽٳ��ȶ���")
				   .setPositiveButton("����Ӧ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							editor.putBoolean("isFirst3.0", false);
							editor.commit();
						}
				   })
				   .show();
		}

		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		addLog("�豸imei:"+imei);
		addLog("��ʼע������λ��...");
	}
	
	public static void T(Activity a, String x){
		Toast.makeText(a, x, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button1:
			isEnabledFake = !isEnabledFake;
			editor.putBoolean("isON", isEnabledFake);
			editor.commit();
			if(isEnabledFake){
				btn.setText("��ͣ����");
				addLog("�Ѿ���ʼע������λ��...");
			}else{
				btn.setText("��������");
				addLog("�Ѿ�ֹͣע������λ��...");
			}
			break;
		case R.id.button2:
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle("��ʾ")
				   .setMessage("ͼƬѡȡ����ܻ���ֶ�ʱ���APP�����������ĵȴ�����Ҫǿ���˳���̨������������ͼƬ�����ϸ������ĳ�ʱ��ѹ����ʱ�������½⣡")
				   .setPositiveButton("�õ�", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							openGallery();
						}
				   })
				   .setNegativeButton("ȡ������", null)
				   .show();
			break;
		case R.id.button3:
			FileUtils.deleteDir();
			addLog("ȫ��������ɾ���������ۼ���");
			break;
		case R.id.textView3:
			MapDialog dialog = new MapDialog(); 
			dialog.setCancelable(true);
	        dialog.show(getFragmentManager(), "mapDialog"); 
			break;
		case R.id.button4:
			isBoss = !isBoss;
			editor.putBoolean("isBoss", isBoss);
			editor.commit();
			if(isBoss){
				btn4.setText("�����ϰ�");
				addLog("�Ѿ������ϰ�ģʽ...");
			}else{
				btn4.setText("����Ա��");
				addLog("�Ѿ�����Ա��ģʽ...");
			}
			addLog("���������������Ӧ������Ӧ���ģ�");
			break;
		}
		
	}
	
	@Override
	protected void onDestroy() {
		isEnabledFake = false;
		editor.putBoolean("isON", isEnabledFake);
		editor.commit();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                Toast.makeText(MainActivity.this,"�ٰ�һ���˳�����",Toast.LENGTH_SHORT).show();
                firstTime=System.currentTimeMillis();
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	private void openGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// ����һ�����з���ֵ��Activity��������ΪPHOTO_REQUEST_GALLERY
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	public void addLog(String x){
		tv.append(x+"\n");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			// ����᷵�ص�����
			if (data != null) {
				Uri originalUri = data.getData();
				String[] proj = { MediaStore.Images.Media.DATA };
	            // ������android��ý�����ݿ�ķ�װ�ӿڣ�����Ŀ�Android�ĵ�  
	            Cursor cursor = managedQuery(originalUri, proj, null, null, null);  
				// ���Ҹ������ ����ǻ���û�ѡ���ͼƬ������ֵ  
	            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
	            // �����������ͷ ���������Ҫ����С�ĺ���������Խ��  
	            cursor.moveToFirst();  
	            // ����������ֵ��ȡͼƬ·��  
	            picPath2 = cursor.getString(column_index);
	            addLog("�û�ѡ��ͼƬ��"+picPath2);
	            BitmapUtil.saveImgToDisk("tempfile"+File.separator+"tfdsTEMP.jpg", BitmapCompressor.compressBitmap(BitmapUtil.getDiskBitmap(picPath2), 600));
	            picPath2 = BitmapUtil.getSdCardPath() + File.separator + "tempfile" + File.separator + "tfdsTEMP.jpg";
	            addLog("��ѹ����Ƭ����"+picPath2);
	            addLog("�ѳɹ�ע�룬���л�Ӧ�ò�����");
			}
		}
	}

	@Override
	public void onMapClickComplete(LatLng ll) {
		editor.putBoolean("isON", false); //1.1�汾����
		editor.commit();
		lat = ll.latitude;
		lon = ll.longitude;
		DecimalFormat decimalFormat=new DecimalFormat(".0000");//���췽�����ַ���ʽ�������С������2λ,����0����.
		String s = "��ѡ�������꣺"+decimalFormat.format(lat)+", "+decimalFormat.format(lon);
		tv3.setText(s);
		editor.putString("ll", String.valueOf(lat)+"+"+String.valueOf(lon));
		editor.commit();
		addLog(s);
		addLog("���������Ѹ������");
		editor.putBoolean("isON", true);
		editor.commit();
		
		//1.1�汾�Ż�
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("����λ��˵��")
			   .setMessage("���ոճɹ��������������꣬Ϊ�˱�֤������APP���ܼ�ʱ��Ӧ��������Ҫ������������н��������к�̨֮�����������������䲻����ȷ��Ӧ��λ��")
			   .setPositiveButton("OK", null)
			   .show();
	}
}
