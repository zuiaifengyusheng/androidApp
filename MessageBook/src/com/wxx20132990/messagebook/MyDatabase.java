package com.wxx20132990.messagebook;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabase extends SQLiteOpenHelper {

	private static String strDataBaseName="people.do";
	private static int Version=1;
	private SQLiteDatabase db=null;
	
	
	//����
	public MyDatabase(Context context)
	{
		super(context, strDataBaseName, null, Version);
		db=getWritableDatabase();
	}
	
	//ִ��SQL��䣬�����������create��delete��update��insert select	
	public Boolean Execsql(String strsql)
	{
			
		try{
			db.execSQL(strsql);
			db.close();
			return true;
		}catch(Exception e){
		return false;
		}
	}
	public Boolean insert(String string, Object object, ContentValues contentValues) {	
		// TODO Auto-generated method stub
		
		try
		{
			db.insert(string, null, contentValues);
			db.close();
			return true;
		}catch(Exception e)
		{
			return false;
		}
	}
	public Cursor rawQuery(String sql)
	{
		Cursor result=null;
		try
		{
			result=db.rawQuery(sql, null);
			System.out.println("û�в鵽�κ�����");
		}
		catch(Exception e)
		{
			System.out.println("�в鵽�κ�����");
		}
		
		db.close();
		return result;
	}
	
	
	//ˢ�½��棬������ݿ������е��û�����ÿһ���û��ŵ�һ��map��ȥ��Ȼ���ٽ�map�ŵ�list����ȥ����
	public ArrayList getAlluser()
	{
		ArrayList list=new ArrayList();
		Cursor cursor=null;
		
		cursor=db.query("person", new String[]{"id",
				"Name","Phone","Address","Email"}, null, null, null, null, "name asc");
		while(cursor.moveToNext())
		{
			HashMap item=new HashMap();
			item.put("id",cursor.getInt(cursor.getColumnIndex("id")));
			item.put("Name",cursor.getString(cursor.getColumnIndex("Name")));
			item.put("Phone",cursor.getString(cursor.getColumnIndex("Phone")));
			item.put("Address",cursor.getString(cursor.getColumnIndex("Address")));
			item.put("Email",cursor.getInt(cursor.getColumnIndex("Email")));
			list.add(item);
		}
		db.close();
		return list;
		
	}
	
	//������ϵ��
	public ArrayList Query(String[] name)
	{
		ArrayList list=new ArrayList();
		Cursor cursor;
		String[] columns={"id","Name","Phone","Address","Email"};
		cursor=db.query("person", columns, "Name=?", name, null, null, "name asc");
		while(cursor.moveToNext())
		{
			HashMap item=new HashMap();
			item.put("id",cursor.getInt(cursor.getColumnIndex("id")));
			item.put("Name",cursor.getString(cursor.getColumnIndex("Name")));
			item.put("Phone",cursor.getString(cursor.getColumnIndex("Phone")));
			item.put("Address",cursor.getString(cursor.getColumnIndex("Address")));
			item.put("Email",cursor.getInt(cursor.getColumnIndex("Email")));
			list.add(item);
		}
		db.close();
		return list;
	}
	
	//ɾ��������ϵ��
	public void deletedMarked(int id)
	{
		db.delete("person", "id=?", new String[]{String.valueOf(id)});
	}
	
	//����ɾ����ϵ��
	public void deleteMarked(ArrayList<Integer> deleteId)
	{
		String sql;
		
		for(int i=0;i<deleteId.size();i++)
		{
			sql="delete from person where id="+deleteId.get(i);
			db.execSQL(sql);
		}
	}
	
	
	
	
	
	//����
	public MyDatabase(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	public MyDatabase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	
	

}
