package HopDong;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Database.DbHelper;
import Model.KhachThue;

public class KhachThueDAO {
    private SQLiteDatabase db;

    public KhachThueDAO(Context mContext) {
        DbHelper dbHelper = new DbHelper(mContext);
        db= dbHelper.getWritableDatabase();
    }
    //insert
    public long insertKhachThue(KhachThue obj){
        ContentValues values = new ContentValues();
        values.put("HoTen",obj.getHoTen());
        values.put("SoDienThoai",obj.getSdt());
        values.put("Cccd",obj.getCccd());
        values.put("IdPhong",obj.getIdPhong());
        return db.insert("KhachThue",null,values);
    }

    //delete by object
    public int deleteKhachThue(KhachThue obj){
        String Id = String.valueOf(obj.getIdKhachThue());
        return db.delete("KhachThue","IdKhachThue=?",new String[]{Id});
    }

    //update
    public int updateKhachThue(KhachThue obj){
        ContentValues values = new ContentValues();
        values.put("IdKhachThue",obj.getIdKhachThue());
        values.put("HoTen",obj.getHoTen());
        values.put("SoDienThoai",obj.getSdt());
        values.put("Cccd",obj.getCccd());
        values.put("IdPhong",obj.getIdPhong());
        return db.update("KhachThue",values,"IdKhachThue=?",new String[]{String.valueOf(obj.getIdKhachThue())});
    }
    //getAll
    public List<KhachThue> getAll(){
        String sql="SELECT * FROM KhachThue";
        return getData(sql);
    }
    //get user by id
    public KhachThue getUserByIdPhong(String Id){
        String sql="SELECT * FROM KhachThue WHERE IdPhong=?";
        List<KhachThue> list = getData(sql,Id);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }    //get user by id
    public KhachThue getUserById(String Id){
        String sql="SELECT * FROM KhachThue WHERE IdKhachThue=?";
        List<KhachThue> list = getData(sql,Id);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @SuppressLint("Range")
    public List<KhachThue>getData(String sql, String...SelectArgs){
        List<KhachThue> list= new ArrayList<>();
        Cursor cursor= db.rawQuery(sql,SelectArgs);
//        while (cursor.moveToNext()){
//           KhachThue user= new KhachThue();
//            user.setIdPhong(cursor.getInt(cursor.getColumnIndex("IdPhong")));
//            user.setIdKhachThue(cursor.getInt(cursor.getColumnIndex("IdKhachThue")));
//            user.setHoTen(String.valueOf(cursor.getInt(cursor.getColumnIndex("HoTen"))));
//            user.setCccd(cursor.getInt(cursor.getColumnIndex("Cccd")));
//            list.add(user);
//        }
        cursor.moveToFirst ();
        while (!cursor.isAfterLast ()){
           KhachThue user= new KhachThue();
            int idkhach = Integer.parseInt (cursor.getString (cursor.getColumnIndex ("IdKhachThue")));
            String cccd = cursor.getString (cursor.getColumnIndex ("Cccd"));
            String sdt = cursor.getString (cursor.getColumnIndex ("SoDienThoai"));
            int idphong = Integer.parseInt (cursor.getString (cursor.getColumnIndex ("IdPhong")));
            String tenkhach = cursor.getString (cursor.getColumnIndex ("HoTen"));
            list.add(new KhachThue(idkhach,tenkhach,sdt,cccd,idphong));
            cursor.moveToNext ();
        }
        cursor.close ();
        if(list!=null||list.size()!=0){
            return list;
        }
        return null;
    }
}
