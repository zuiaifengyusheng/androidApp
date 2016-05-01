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

	//����***********************************************************************
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
		
		//����person��,�������		
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
		
		
		//��ѯȫ����ϵ��
		list=db.getAlluser();
		if(list.size()==0)
		{
			Toast.makeText(this,"û�в鵽�κ�����", Toast.LENGTH_SHORT).show();
		}
		adapter=new SimpleAdapter(this, list, R.layout.listitem, new String[]{"id","Name","Phone","Address","Email"},
				new int[]{R.id.li_id,R.id.li_name,R.id.li_phone});
		show.setAdapter(adapter);
		
		
		//��ʾ��ϵ����ϸ��Ϣ
		show.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//�õ�Itemλ��
				HashMap item=(HashMap)arg0.getItemAtPosition(arg2);
				String number=String.valueOf(item.get("id"));

				Intent intent=new Intent(MainActivity.this,informationActivity.class);

				Bundle bundle=new Bundle();				
				bundle.putString("str",number);

				intent.putExtras(bundle);
				startActivityForResult(intent, 0);  	
			}
			
		});
		
		//����ʱ�䴦�����ѡ��
		show.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				if(deleteId==null)
				{
					deleteId=new ArrayList<Integer>();
				}
				
				//�õ�item��λ��id
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
	
	
	//��дBack��
	public boolean onKeyDown(int keyCode,KeyEvent event) {

		 if (keyCode == KeyEvent.KEYCODE_BACK&&lean==true)
		 {
			
		    //������д���ؼ�
			db=new MyDatabase(this);
		    db.Execsql(strsql);
						
			//��ѯȫ����ϵ��
			list=db.getAlluser();
			if(list.size()==0)
			{
				Toast.makeText(this,"û�в鵽�κ�����", Toast.LENGTH_SHORT).show();
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
				
		//��ѯȫ����ϵ��
		list=db.getAlluser();
		if(list.size()==0)
		{
			Toast.makeText(this,"û�в鵽�κ�����", Toast.LENGTH_SHORT).show();
		}
		adapter=new SimpleAdapter(this, list, R.layout.listitem, new String[]{"id","Name","Phone","Address","Email"},
				new int[]{R.id.li_id,R.id.li_name,R.id.li_phone});
		show.setAdapter(adapter);
		db.close();
	}
	
	//�����ϵ��
	public void ImageButton_OnClick1(View v)
	{
		Intent intent=new Intent(this,AddActivity.class);
		startActivityForResult(intent, 1);
		this.finish();
		
	}

	//������ϵ��
	public void ImageButton_OnClick2(View v)
	{
		layout=(LinearLayout)findViewById(R.id.layout);
		search=(EditText)findViewById(R.id.search);
		ibutton=(ImageButton)findViewById(R.id.OK);
		
		
		//�������ѿ��Ƿ�ɼ���layout������ĸ������
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
						//��ʾ�Ի���
						AlertDialog alert=new AlertDialog.Builder(MainActivity.this).create();
						alert.setTitle("��ʾ��Ϣ��");
						alert.setMessage("����ѯ�����������ڣ�");
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
	
	//����ɾ����ϵ��
	public void ImageButton_OnClick3(View v)
	{
		if(deleteId==null||deleteId.size()==0)
		{
			Toast.makeText(MainActivity.this, "û��ѡ���κμ�¼\n����һ����¼���Ա��", Toast.LENGTH_SHORT).show();
		}
		else
		{
			//�½�һ������ĶԻ���
			new AlertDialog.Builder(this).setTitle("ȷ��Ҫɾ����ǵ�"+deleteId.size()+"����¼��")
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					lean=false;
					db=new MyDatabase(MainActivity.this);
				    //�������ݿ��ɾ����������ɾ����¼
					db.deleteMarked(deleteId);
					list=db.getAlluser();
					adapter=new SimpleAdapter(MainActivity.this, list, R.layout.listitem, new String[]{"id","Name","Phone","Address","Email"},
							new int[]{R.id.li_id,R.id.li_name,R.id.li_phone});
					show.setAdapter(adapter);
					deleteId.clear();
				}
			}).setNegativeButton("ȡ��", null).create().show();
			
			
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
