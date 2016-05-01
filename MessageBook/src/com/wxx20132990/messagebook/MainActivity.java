package com.wxx20132990.messagebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	//变量***********************************************************************
	private String strsql=null;
	private String select=null;
	private MyDatabase db=null;
	private ListView show=null;
	private ArrayList list=null;
	private SimpleAdapter adapter;
	private EditText search=null;
	private LinearLayout layout;
	private ImageButton ibutton=null;
	ArrayList<Integer> deleteId;
	private boolean lean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
		
		show=(ListView)findViewById(R.id.datashow);
		
		//创建person表,建表语句		
		db=new MyDatabase(this);
		strsql="create table person";
		strsql+=" (";
		strsql+="id integer primary key autoincrement,";
		strsql+=" Name varchar(40) null,";
		strsql+=" Phone varchar(20) null,";
		strsql+=" Address varchar(1000) null,";
		strsql+=" Email varchar(100) null";
		strsql+=" )";
		    	
		boolean b;
		b=db.Execsql(strsql);
		
		
		//查询全部联系人
		list=db.getAlluser();
		if(list.size()==0)
		{
			Toast.makeText(this,"没有查到任何数据", Toast.LENGTH_SHORT).show();
		}
		adapter=new SimpleAdapter(this, list, R.layout.listitem, new String[]{"id","Name","Phone","Address","Email"},
				new int[]{R.id.li_id,R.id.li_name,R.id.li_phone});
		show.setAdapter(adapter);
		
		
		//显示联系人详细信息
		show.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//得到Item位置
				HashMap item=(HashMap)arg0.getItemAtPosition(arg2);
				String number=String.valueOf(item.get("id"));

				Intent intent=new Intent(MainActivity.this,informationActivity.class);

				Bundle bundle=new Bundle();				
				bundle.putString("str",number);

				intent.putExtras(bundle);
				startActivityForResult(intent, 0);  	
			}
			
		});
		
		//长按时间处理可以选中
		show.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				if(deleteId==null)
				{
					deleteId=new ArrayList<Integer>();
				}
				
				//得到item的位置id
				HashMap item=(HashMap)arg0.getItemAtPosition(arg2);
				final Integer id=Integer.parseInt(String.valueOf(item.get("id")));
				RelativeLayout r=(RelativeLayout)arg1;
				CheckBox markedView=(CheckBox)r.getChildAt(1);
				if(markedView.getVisibility()==View.VISIBLE)
				{
					markedView.setVisibility(View.GONE);
					deleteId.remove(id);
				}
				else
				{
					lean=true;
					markedView.setVisibility(View.VISIBLE);
                    markedView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
							// TODO Auto-generated method stub
							if(isChecked)
							{
								deleteId.add(id);							
							}
							if(!isChecked)
							{
								deleteId.remove(id);
							}
						}
					});
				}
				return true;
			}
			
		});
		
		db.close();
	}
	
	
	//重写Back键
	public boolean onKeyDown(int keyCode,KeyEvent event) {

		 if (keyCode == KeyEvent.KEYCODE_BACK&&lean==true)
		 {
			
		    //这里重写返回键
			db=new MyDatabase(this);
		    db.Execsql(strsql);
						
			//查询全部联系人
			list=db.getAlluser();
			if(list.size()==0)
			{
				Toast.makeText(this,"没有查到任何数据", Toast.LENGTH_SHORT).show();
			}
			adapter=new SimpleAdapter(this, list, R.layout.listitem, new String[]{"id","Name","Phone","Address","Email"},
					new int[]{R.id.li_id,R.id.li_name,R.id.li_phone});
			show.setAdapter(adapter);
			db.close();
			lean=false;
		    return true;
		 }
		 else if (keyCode == KeyEvent.KEYCODE_BACK&&lean==false)
		 {
			 finish();
			 return true;
		 }
		 return false;

    }
	
	@Override
	public void onRestart()
	{
		db=new MyDatabase(this);
        db.Execsql(strsql);
				
		//查询全部联系人
		list=db.getAlluser();
		if(list.size()==0)
		{
			Toast.makeText(this,"没有查到任何数据", Toast.LENGTH_SHORT).show();
		}
		adapter=new SimpleAdapter(this, list, R.layout.listitem, new String[]{"id","Name","Phone","Address","Email"},
				new int[]{R.id.li_id,R.id.li_name,R.id.li_phone});
		show.setAdapter(adapter);
		db.close();
	}
	
	//添加联系人
	public void ImageButton_OnClick1(View v)
	{
		Intent intent=new Intent(this,AddActivity.class);
		startActivityForResult(intent, 1);
		this.finish();
		
	}

	//查找联系人
	public void ImageButton_OnClick2(View v)
	{
		layout=(LinearLayout)findViewById(R.id.layout);
		search=(EditText)findViewById(R.id.search);
		ibutton=(ImageButton)findViewById(R.id.OK);
		
		
		//设置所搜框是否可见（layout搜索框的父组件）
		if(layout.getVisibility()==View.VISIBLE)
		{
			layout.setVisibility(View.GONE);
			search.setText("");
		}
		else
		{
			layout.setVisibility(View.VISIBLE);
			search.requestFocus();
			search.selectAll();
			
			ibutton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String name=search.getText().toString();
					String[] str={name};
					db=new MyDatabase(MainActivity.this);
					list=db.Query(str);
					
					if(list.size()==0)
					{
						//提示对话框
						AlertDialog alert=new AlertDialog.Builder(MainActivity.this).create();
						alert.setTitle("提示信息：");
						alert.setMessage("所查询的姓名不存在！");
						alert.show();
						
					}
					else
					{
						adapter=new SimpleAdapter(MainActivity.this, list, R.layout.listitem, new String[]{"id","Name","Phone","Address","Email"},
							new int[]{R.id.li_id,R.id.li_name,R.id.li_phone});
					    show.setAdapter(adapter);
					}
					
					db.close();
				}
				
			});
			
		}
	}
	
	//批量删除联系人
	public void ImageButton_OnClick3(View v)
	{
		if(deleteId==null||deleteId.size()==0)
		{
			Toast.makeText(MainActivity.this, "没有选中任何记录\n长按一条记录可以标记", Toast.LENGTH_SHORT).show();
		}
		else
		{
			//新建一个警告的对话框
			new AlertDialog.Builder(this).setTitle("确定要删除标记的"+deleteId.size()+"条记录吗？")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					lean=false;
					db=new MyDatabase(MainActivity.this);
				    //调用数据库的删除函数进行删除记录
					db.deleteMarked(deleteId);
					list=db.getAlluser();
					adapter=new SimpleAdapter(MainActivity.this, list, R.layout.listitem, new String[]{"id","Name","Phone","Address","Email"},
							new int[]{R.id.li_id,R.id.li_name,R.id.li_phone});
					show.setAdapter(adapter);
					deleteId.clear();
				}
			}).setNegativeButton("取消", null).create().show();
			
			
		}
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
