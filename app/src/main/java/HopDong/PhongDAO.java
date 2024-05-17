package HopDong;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Database.DbHelper;
import Model.Phong;

public class PhongDAO {
    private SQLiteDatabase db;

    public PhongDAO(Context mContext) {
        DbHelper dbHelper = new DbHelper(mContext);
        db= dbHelper.getWritableDatabase();
    }
    //insert
    public boolean insertPhong(int SoPhong, int GiaPhong, int GiaDien, int GiaNuoc, int GiaWifi, int TrangThai){
        ContentValues values = new ContentValues();
        values.put("SoPhong",SoPhong);
        values.put("GiaPhong",GiaPhong);
        values.put("GiaDien",GiaDien);
        values.put("GiaNuoc",GiaNuoc);
        values.put("GiaWifi",GiaWifi);
        values.put("TrangThai",TrangThai);
        long row = db.insert("Phong",null,values);
        return(row>0);
    }

    //delete by object
    public int deletePhong(Phong obj){
        String Id = String.valueOf(obj.getIdPhong());
        return db.delete("Phong","IdPhong=?",new String[]{Id});
    }

    //update
    public int updatePhong(Phong obj){
        ContentValues values = new ContentValues();
        values.put("SoPhong",obj.getSoPhong());
        values.put("GiaPhong",obj.getGiaPhong());
        values.put("GiaDien",obj.getGiaDien());
        values.put("GiaNuoc",obj.getGiaNuoc());
        values.put("GiaWifi",obj.getGiaWifi());
        values.put("TrangThai",obj.getTrangThai());
        String Id = String.valueOf(obj.getIdPhong());
        return db.update("Phong",values,"IdPhong=?",new String[]{Id});
    }
    //getAll
    public List<Phong> getAll(){
        String sql="SELECT * FROM Phong";
        return getData(sql);
    }
    //get user by id
    public Phong getPhongById(String Id){
        String sql="SELECT * FROM Phong WHERE IdPhong=?";
        List<Phong> list = getData(sql,Id);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }
    @SuppressLint("Range")
    public List<Phong>getData(String sql, String...SelectArgs){
        List<Phong> list= new ArrayList<>();
        Cursor cursor= db.rawQuery(sql,SelectArgs);
        while (cursor.moveToNext()){
            Phong user= new Phong();
            user.setIdPhong(cursor.getInt(cursor.getColumnIndex("IdPhong")));
            user.setSoPhong(cursor.getInt(cursor.getColumnIndex("SoPhong")));
            user.setGiaPhong(cursor.getInt(cursor.getColumnIndex("GiaPhong")));
            user.setGiaDien(cursor.getInt(cursor.getColumnIndex("GiaDien")));
            user.setGiaNuoc(cursor.getInt(cursor.getColumnIndex("GiaNuoc")));
            user.setGiaWifi(cursor.getInt(cursor.getColumnIndex("GiaWifi")));
            user.setTrangThai(cursor.getInt(cursor.getColumnIndex("TrangThai")));
            list.add(user);
        }
        if(list!=null||list.size()!=0){
            return list;
        }
        return null;
    }
}
