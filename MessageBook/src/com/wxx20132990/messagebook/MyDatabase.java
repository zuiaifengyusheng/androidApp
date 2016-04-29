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
	
	
	//函数
	public MyDatabase(Context context)
	{
		super(context, strDataBaseName, null, Version);
		db=getWritableDatabase();
	}
	
	//执行SQL语句，建议语句类型create　delete　update　insert select	
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
			System.out.println("没有查到任何数据");
		}
		catch(Exception e)
		{
			System.out.println("有查到任何数据");
		}
		
		db.close();
		return result;
	}
	
	
	//刷新界面，获得数据库中所有的用户，将每一个用户放到一个map中去，然后再将map放到list里面去返回
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
	
	//查找联系人
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
	
	//删除单个联系人
	public void deletedMarked(int id)
	{
		db.delete("person", "id=?", new String[]{String.valueOf(id)});
	}
	
	//批量删除联系人
	public void deleteMarked(ArrayList<Integer> deleteId)
	{
		String sql;
		
		for(int i=0;i<deleteId.size();i++)
		{
			sql="delete from person where id="+deleteId.get(i);
			db.execSQL(sql);
		}
	}
	
	
	
	
	
	//构造
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
