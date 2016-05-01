package com.wxx20132990.messagebook;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class informationActivity extends Activity {

	private MyDatabase db=null;
	private ArrayList list=null;
	private TextView id=null;
	private TextView name=null;
	private EditText phone=null;
	private EditText address=null;
	private EditText email=null;
	private Button save=null;
	private Button back=null;
	private Button edit=null;
	private Button close=null;
	private Button delete=null;
	private Button blank=null;
	private SimpleAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
		
		Bundle bundle=getIntent().getExtras();
		String str=bundle.getString("str");		
		int number=Integer.parseInt(str);
		
		id=(TextView)findViewById(R.id.id);
		name=(TextView)findViewById(R.id.name);
		phone=(EditText)findViewById(R.id.phone);
		address=(EditText)findViewById(R.id.address);
		email=(EditText)findViewById(R.id.email);
		save=(Button)findViewById(R.id.save);
		back=(Button)findViewById(R.id.back);
        edit=(Button)findViewById(R.id.edit);
        close=(Button)findViewById(R.id.close);
        delete=(Button)findViewById(R.id.delete);
        blank=(Button)findViewById(R.id.blank);
		
		db=new MyDatabase(this);
		
		list=db.rawQuery(number);
		
		id.setText(list.get(0).toString());   
		name.setText(list.get(1).toString());
		phone.setText(list.get(2).toString());
		address.setText(list.get(3).toString());
		email.setText(list.get(4).toString());
		db.close();
		
	}
	
	//重写Back键
	public boolean onKeyDown(int keyCode,KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent=new Intent(this,MainActivity.class);
			startActivityForResult(intent, 1);
			finish();
			return true;
		}
				 
		return false;

    }
	
	public void ButtonEdit_Onclick(View v)
	{
		name.setFocusableInTouchMode(true);
		phone.setFocusableInTouchMode(true);
		address.setFocusableInTouchMode(true);
		email.setFocusableInTouchMode(true);
		
		save.setVisibility(View.VISIBLE);
		back.setVisibility(View.VISIBLE);
		edit.setVisibility(View.GONE);
		close.setVisibility(View.GONE);
		delete.setVisibility(View.GONE);
		blank.setVisibility(View.VISIBLE);
	}
	
	public void ButtonDelete_Onclick(View v)
	{
		
		//db.deletedMarked(Integer.parseInt(id.getText().toString()));
		final int n=Integer.parseInt(id.getText().toString());
		
		new AlertDialog.Builder(this).setTitle("确定要删除记录吗？")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				db=new MyDatabase(informationActivity.this);
				db.deleted(n);
				db.close();
				Intent intent=new Intent(informationActivity.this,MainActivity.class);
				startActivityForResult(intent, 0);	
				finish();
			}
		}).setNegativeButton("取消", null).create().show();
		
	}
	public void ButtonSave_Onclick(View v)
	{
		String strName=name.getText().toString();
		String strPhone=phone.getText().toString();
		String strAddress=address.getText().toString();
		String strEmail=email.getText().toString();
		final int n=Integer.parseInt(id.getText().toString());
		String sql="Update person set Name='"+strName+"',Phone='"+strPhone+"',Address='"+strAddress+"',Email='"+strEmail+"' where id="+n;
		db=new MyDatabase(this);
		db.Execsql(sql);
		db.close();
		Intent intent=new Intent(this,MainActivity.class);
		startActivityForResult(intent, 0);
		this.finish();
	}
	public void ButtonBack_Onclick(View v)
	{
		Intent intent=new Intent(this,MainActivity.class);
		startActivityForResult(intent, 0);
		this.finish();
	}
	public void ButtonClose_Onclick(View v)
	{
		Intent intent=new Intent(this,MainActivity.class);
		startActivityForResult(intent, 0);
		this.finish();
	}
}
