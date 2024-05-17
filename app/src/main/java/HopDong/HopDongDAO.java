package HopDong;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Database.DbHelper;
import Model.HopDong;

public class HopDongDAO {
    private SQLiteDatabase db;
//    private Button btnthemHopDong;
//    EditText edtNgayBatDau;
//    EditText edtNgayKetThuc;
//    EditText edtSoLuongXe;
//    EditText edtTienCoc;
//    EditText edtSoNguoi;

    public HopDongDAO(Context mContext) {
        DbHelper dbHelper = new DbHelper(mContext);
        db= dbHelper.getWritableDatabase();
    }
    //insert
    public long insertHopDong(HopDong obj){
        ContentValues values = new ContentValues();
        values.put("NgayBatDau",obj.getNgayBatDau());
        values.put("NgayKetThuc",obj.getNgayKetThuc());
        values.put("SoNguoi",obj.getSoNguoi());
        values.put("SoLuongXe",obj.getSoLuongXe());
        values.put("TienCoc",obj.getTiecCoc());
        values.put("TrangThaiHD",obj.getTrangThaiHD());
        values.put("IdKhachThue",obj.getIdKhachThue());
        values.put("IdPhong",obj.getIdPhong());
        return db.insert("HopDong",null,values);
    }


    //update
    public int updateHopDong(HopDong obj){
        ContentValues values = new ContentValues();
        values.put("NgayBatDau",obj.getNgayBatDau());
        values.put("NgayKetThuc",obj.getNgayKetThuc());
        values.put("SoNguoi",obj.getSoNguoi());
        values.put("SoLuongXe",obj.getSoLuongXe());
        values.put("TienCoc",obj.getTiecCoc());
        values.put("TrangThaiHD",obj.getTrangThaiHD());
        values.put("IdKhachThue",obj.getIdKhachThue());
        values.put("IdPhong",obj.getIdPhong());
        String Id = String.valueOf(obj.getIdHopDong());
        return db.update("HopDong",values,"IdHopDong=?",new String[]{Id});
    }
    //getAll
    public List<HopDong> getAll(){
        String sql="SELECT * FROM HopDong ORDER BY TrangThaiHD ASC ";
        return getData(sql);
    }
    //getAllbýoPhong
    public List<HopDong> getAllTimKiem(String Id){
        String sql="SELECT * FROM HopDong JOIN Phong ON HopDong.IdPhong = Phong.IdPhong WHERE Phong.SoPhong = ? ORDER BY TrangThaiHD ASC ";
        List<HopDong> list = getData(sql,Id);
        if(list.size()>0){
            return list;
        }
        return null;
    }
    //get hợp đồng by id
    public HopDong getHopDongByIdPhong(String Id, String trangthai){
        String sql="SELECT * FROM HopDong WHERE IdPhong=? AND TrangThaiHD = ?";
        List<HopDong> list = getData(sql,Id,trangthai);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }
    //get hợp đồng by id
    public HopDong getHopDongByIdKT(String Id){
        String sql="SELECT * FROM HopDong WHERE IdKhachThue=? ";
        List<HopDong> list = getData(sql,Id);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }
    @SuppressLint("Range")
    public List<HopDong>getData(String sql, String...SelectArgs){
        List<HopDong> list= new ArrayList<>();
        Cursor cursor= db.rawQuery(sql,SelectArgs);
        cursor.moveToFirst ();
        while (!cursor.isAfterLast ()){
            int idhopdong = Integer.parseInt (cursor.getString (cursor.getColumnIndex ("IdHopDong")));
            int idphong = Integer.parseInt (cursor.getString (cursor.getColumnIndex ("IdPhong")));
            int idkhachthue = Integer.parseInt (cursor.getString (cursor.getColumnIndex ("IdKhachThue")));
            String ngaybatdau = cursor.getString (cursor.getColumnIndex ("NgayBatDau"));
            String ngayketthuc = cursor.getString (cursor.getColumnIndex ("NgayKetThuc"));
            int songuoi = Integer.parseInt (cursor.getString (cursor.getColumnIndex ("SoNguoi")));
            int soluongxe = Integer.parseInt (cursor.getString (cursor.getColumnIndex ("SoLuongXe")));
            int tiencoc = Integer.parseInt (cursor.getString (cursor.getColumnIndex ("TienCoc")));
            int trangthai = Integer.parseInt (cursor.getString (cursor.getColumnIndex ("TrangThaiHD")));
            list.add(new HopDong(idhopdong,ngaybatdau,ngayketthuc,songuoi,soluongxe,tiencoc,trangthai,idphong,idkhachthue));
            cursor.moveToNext ();
        }
        if(list!=null||list.size()!=0){
            return list;
        }
        return null;
    }
}

