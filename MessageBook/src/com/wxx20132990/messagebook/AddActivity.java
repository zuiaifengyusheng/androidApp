package com.wxx20132990.messagebook;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends Activity {
	
	//变量***********************************************************************
	private MyDatabase db=null;
	private String insert=null;
	private EditText Name;
	private EditText Phone;
	private EditText Address;
	private EditText Email;
	private ContentValues contentValues;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_people);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
		Name=(EditText)findViewById(R.id.eName);
		Phone=(EditText)findViewById(R.id.ePhone);
		Address=(EditText)findViewById(R.id.eAddress);
		Email=(EditText)findViewById(R.id.eEmail);
		contentValues = new ContentValues();
	}

	
	public void ImageButto2_OnClick(View v)
	{
		db=new MyDatabase(this);
    	
    	//添加新的联系人
    	
    	String N=Name.getText().toString();
    	String P=Phone.getText().toString();
    	String A=Address.getText().toString();
    	String E=Email.getText().toString();
    	
    	boolean b;
    	contentValues.put("Name", N);
		contentValues.put("Phone", P);
		contentValues.put("Address", A);
		contentValues.put("Email", E);
		b=db.insert("person", null, contentValues);
		contentValues.clear();
    	if(b==false)
    	{
    		Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
    	}
    	else
    	{
    		Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
    		//返回到上一个Activity
    		Intent intent=new Intent(this,MainActivity.class);
    		startActivity(intent);
    		//销毁当前视图
    		finish();
    	}
    	db.close();
	}
	public void ImageButto1_OnClick(View v)
	{
		this.finish();
	}
}
