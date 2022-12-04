package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "QLPT";
    private static final int DB_VERSION =19;
    //---------------------------------
    static final String CREATE_TABLE_PHONG =
            "create table Phong (IdPhong INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "SoPhong INTEGER NOT NULL ," +
                    "GiaPhong INTEGER  NOT NULL," +
                    "GiaDien INTEGER  NOT NULL," +
                    "GiaNuoc INTEGER  NOT NULL," +
                    "GiaWifi INTEGER NOT NULL," +
                    "TrangThai INTEGER NOT NULL)" ;
    //---------------------------------
    String createTableKhachThue = " create table KhachThue (" +
            "IdKhachThue INTEGER PRIMARY KEY autoincrement," +
            "HoTen TEXT NOT NULL," +
            "SoDienThoai TEXT NOT NULL," +
            "Cccd TEXT NOT NULL," +
            "IdPhong INTEGER REFERENCES Phong(IdPhong))";

    //---------------------------------

    static final String CREATE_TABLE_HOP_DONG =
            "create table HopDong (" +
                    "IdHopDong INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NgayBatDau TEXT NOT NULL, " +
                    "NgayKetThuc TEXT NOT NULL," +
                    "SoNguoi INTEGER NOT NULL, " +
                    "SoLuongXe INTEGER NOT NULL, " +
                    "TienCoc INTEGER NOT NULL," +
                    "TrangThaiHD INTEGER NOT NULL," +
                    "IdKhachThue INTEGER REFERENCES KhachThue(IdKhachThue)," +
                    "IdPhong INTEGER REFERENCES Phong(IdPhong))";
    //---------------------------------
    static final String CREATE_TABLE_HOA_DON =
            "create table HoaDon (" +
                    "IdHoaDon INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "TenHoaDon TEXT NOT NULL, " +
                    "Ngay DATE NOT NULL, " +
                    "SoDien INTEGER NOT NULL, " +
                    "SoNuoc INTEGER NOT NULL," +
                    "ChiPhiKhac INTERGER NOT NULL, " +
                    "Tong INTEGER NOT NULL," +
                    "TrangThai INTEGER NOT NULL," +
                    "GhiChu TEXT NOT NULL," +
                    "IdPhong INTEGER REFERENCES Phong(IdPhong)," +
                    "IdHopDong INTEGER REFERENCES HopDong(IdHopDong))";

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //tao bang Phong
        db.execSQL(CREATE_TABLE_PHONG);
        //Tao bang Khach Thue
        db.execSQL(createTableKhachThue);
        //Tao bang HopDong
        db.execSQL(CREATE_TABLE_HOP_DONG);
        //Tao bang Hoa Don
        db.execSQL(CREATE_TABLE_HOA_DON);
//        db.execSQL(INSERT_khach);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTablePhong = "drop table if exists Phong";
        db.execSQL(dropTablePhong);
        String dropTableKhachThue = "drop table if exists KhachThue";
        db.execSQL(dropTableKhachThue);
        String dropTableHopDong = "drop table if exists HopDong";
        db.execSQL(dropTableHopDong);
        String dropTableHoaDon = "drop table if exists HoaDon";
        db.execSQL(dropTableHoaDon);
        onCreate(db);
    }
}
