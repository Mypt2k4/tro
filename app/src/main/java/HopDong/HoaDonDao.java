package HopDong;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Database.DbHelper;
import Model.HoaDon;

public class HoaDonDao {
    private static SQLiteDatabase db;

    public HoaDonDao(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    //insert

    public boolean insertHoaDon(HoaDon HoaDon){
        ContentValues values = new ContentValues();
        values.put("IdPhong",HoaDon.getIdPhong());
        values.put("TenHoaDon",HoaDon.getTenHoaDOn());
        values.put("Ngay",HoaDon.getNgay());
        values.put("SoDien",HoaDon.getSoDien());
        values.put("SoNuoc",HoaDon.getSoNuoc());
        values.put("Tong",HoaDon.getTong());
        values.put("ChiPhiKhac",HoaDon.getChiPhiKhac());
        values.put("TrangThai",HoaDon.getTrangThai());
        values.put("GhiChu",HoaDon.getGhiChu());
        long row = db.insert("HoaDon",null,values);
        return(row>0);
    }

    //update
    public static int updateHoaDon(HoaDon HoaDon){
        ContentValues values = new ContentValues();

        values.put("TrangThai",HoaDon.getTrangThai());

        String Id = String.valueOf(HoaDon.getIdHoaDon());
        return db.update("HoaDon",values,"IdHoaDon=?",new String[]{Id});
    }

    //getAll
    public List<HoaDon> getAll() {
        String sql = "SELECT * FROM HoaDon";
        return getData(sql);
    }

    //delete by object
    public int deleteHoaDon(HoaDon obj){
        String Id = String.valueOf(obj.getIdHoaDon());
        return db.delete("HoaDon","IdHoaDon=?",new String[]{Id});
    }

    //get user by id
    public HoaDon getHoaDonById(String IdHoaDon){
        String sql="SELECT * FROM HoaDon WHERE IdHoaDon=?";
        List<HoaDon> list = getData(sql,IdHoaDon);
        if(list != null){
            return list.get(0);
        }
        return null;
    }
//    public List<HoaDon> gethoadonByNgay(String a, String b){
//        String sql="SELECT * FROM HoaDon WHERE Ngay BETWEEN "+a+" AND "+b;
//        List<HoaDon> list = getData(sql);
//        if(list != null){
//            return list;
//        }
//        return null;
//    }

    public List<HoaDon> gethoadonByNgay(String a, String b){
        List<HoaDon> arrayList = new ArrayList<>();
        String sql="SELECT * FROM HoaDon WHERE Ngay BETWEEN ? AND ?";
        Cursor cursor = db.rawQuery (sql,new String[]{a,b});
        cursor.moveToFirst ();
        while (!cursor.isAfterLast ()){
            HoaDon hoaDon= new HoaDon();
            hoaDon.setIdHoaDon(Integer.parseInt(cursor.getString(0)));
            hoaDon.setTenHoaDOn(cursor.getString(1));
            hoaDon.setNgay(cursor.getString(2));
            hoaDon.setSoDien(Integer.parseInt(cursor.getString(3)));
            hoaDon.setSoNuoc(Integer.parseInt(cursor.getString(4)));
            hoaDon.setChiPhiKhac(Integer.parseInt(cursor.getString(5)));
            hoaDon.setTong(Integer.parseInt(cursor.getString(6)));
            hoaDon.setTrangThai(Integer.parseInt(cursor.getString(7)));
            hoaDon.setGhiChu(cursor.getString(8));
            hoaDon.setIdPhong(Integer.parseInt(cursor.getString(9)));
            hoaDon.setIdHopDong(cursor.getColumnIndex("IdHopDong"));
            arrayList.add(hoaDon);
            cursor.moveToNext ();
        }
        cursor.close ();
        return arrayList;
    }
    @SuppressLint("Range")
    public List<HoaDon>getData(String sql, String...SelectArgs){
        List<HoaDon> list= new ArrayList<>();
        Cursor cursor= db.rawQuery(sql,SelectArgs);
        while (cursor.moveToNext()){
            HoaDon hoaDon= new HoaDon();
            hoaDon.setIdPhong(cursor.getInt(cursor.getColumnIndex("IdPhong")));
            hoaDon.setIdHoaDon(cursor.getInt(cursor.getColumnIndex("IdHoaDon")));
            hoaDon.setNgay(cursor.getString (cursor.getColumnIndex ("Ngay")));
            hoaDon.setNgay(cursor.getString(2));
            hoaDon.setTenHoaDOn(cursor.getString(cursor.getColumnIndex("TenHoaDon")));
            hoaDon.setSoDien(cursor.getInt(cursor.getColumnIndex("SoDien")));
            hoaDon.setSoNuoc(cursor.getInt(cursor.getColumnIndex("SoNuoc")));
            hoaDon.setTong(cursor.getInt(cursor.getColumnIndex("Tong")));
            hoaDon.setTrangThai(cursor.getInt(cursor.getColumnIndex("TrangThai")));
            hoaDon.setGhiChu(String.valueOf(cursor.getInt(cursor.getColumnIndex("GhiChu"))));
            hoaDon.setChiPhiKhac(cursor.getInt(cursor.getColumnIndex("ChiPhiKhac")));
            list.add(hoaDon);
        }
        if(list!=null||list.size()!=0){
            return list;
        }
        return null;
    }
}
