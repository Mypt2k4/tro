package Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import HopDong.HoaDonDao;
import HopDong.HopDongDAO;
import HopDong.KhachThueDAO;
import HopDong.PhongDAO;
import Model.HoaDon;
import Model.HopDong;
import Model.KhachThue;
import Model.Phong;
import longvtph16016.poly.appquanlyphongtro.R;

public class PhongAdapter  extends RecyclerView.Adapter<PhongAdapter.MyViewHolder> implements Filterable {
    List<Phong> phongList;
    List<Phong> phongListold;
    Context context;
    HopDongDAO hopDongDAO;
    KhachThueDAO khachThueDAO;
    PhongDAO phongDAO;
    HoaDonDao hoaDonDao;
    String tenhoadon,ngaytao,ghiChu;
    int sodien,sonuoc,chiphikhac,tongtien;
    public PhongAdapter(List<Phong> phongs, Context context) {
        this.phongList = phongs;
        this.context = context;
        this.phongListold=phongList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phong,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        hopDongDAO=new HopDongDAO(context);
        hoaDonDao=new HoaDonDao(context);
        khachThueDAO=new KhachThueDAO(context);
        phongDAO=new PhongDAO(context);
        Phong phonghienTai=phongList.get(position);
        try {
            if(hopDongDAO.getHopDongByIdPhong(String.valueOf(phongList.get(position).getIdPhong()),"1")!=null){
                holder.imageView.setVisibility(View.VISIBLE);
                holder.imageView.setImageResource(R.drawable.hopdong);
            }
            else if(hopDongDAO.getHopDongByIdPhong(String.valueOf(phongList.get(position).getIdPhong()),"2")!=null){
                holder.imageView.setVisibility(View.VISIBLE);
                holder.imageView.setImageResource(R.drawable.ic_baseline_announcement_24);
            }
            else {
                holder.imageView.setVisibility(View.GONE);
            }
        }catch (Exception e){
            holder.imageView.setVisibility(View.GONE);
        }
        holder.tv_sophong.setText("Phòng"+": "+phongList.get(position).getSoPhong());
        if(phongList.get(position).getTrangThai()==1) {
            holder.ln_item_dv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF2B")));

        }
        else if(phongList.get(position).getTrangThai()!=1){
            holder.ln_item_dv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFE5CC")));
        }
        holder.ln_item_dv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_bottom_phong);

                LinearLayout editLayout = dialog.findViewById(R.id.edt_update_dv);
                LinearLayout detail = dialog.findViewById(R.id.edt_detailphong);
                LinearLayout themhopdong = dialog.findViewById(R.id.edt_ThemHopDong);
                LinearLayout themhoadon = dialog.findViewById(R.id.edt_ThemHoaDon);

                LinearLayout ThemKhachThue = dialog.findViewById(R.id.edt_ThemKhachThue);

                TextView textView = dialog.findViewById(R.id.tv_title);
                textView.setText("Phòng "+phonghienTai.getSoPhong());

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ThongTinPhong(phonghienTai);
                    }
                });
                ThemKhachThue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(phonghienTai.getTrangThai()!=1){
                            Toast.makeText(context, "Phòng đã có người thuê", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                        View view = inflater.inflate(R.layout.themkhachthue_dialog, null);
                        TextView edsophong = view.findViewById(R.id.tv_SoPhong_ThemKhachThu);

                        EditText edtenKT = view.findViewById(R.id.edTenK);
                        EditText edSdt = view.findViewById(R.id.edSd);
                        EditText edCccd = view.findViewById(R.id.edCcc);
                        Button btnCancel = view.findViewById(R.id.btnCance);
                        Button btnSave = view.findViewById(R.id.btnSav);
                        edsophong.setText(""+phonghienTai.getSoPhong());
                        builder.setView(view);
                        Dialog dialog = builder.create();
                        dialog.show();

                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String edTenK =edtenKT.getText().toString();
                                String sdt=edSdt.getText().toString();
                                String cccd=edCccd.getText().toString();
                                //validate
                                if(edTenK.length()==0){

                                    Toast.makeText(context, "Hãy nhập tên chủ phòng", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String reg = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";
                                String regcccd="[0-9]{12}";
                                if(!sdt.matches(reg)){
                                    Toast.makeText(context, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(!cccd.matches(regcccd)){
                                    Toast.makeText(context, "Cccd không hợp lệ", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                khachThueDAO = new KhachThueDAO(context);
                                KhachThue khach = new KhachThue();
                                khach.setIdPhong(phonghienTai.getIdPhong());
                                khach.setHoTen(edTenK);
                                khach.setSdt(sdt);
                                khach.setCccd(cccd);
                                if (khachThueDAO.insertKhachThue(khach)>0){
                                    Toast.makeText(context, "thêm mới thành công", Toast.LENGTH_SHORT).show();
                                    phonghienTai.setTrangThai(2);
                                    phongDAO=new PhongDAO(context);
                                    phongDAO.updatePhong(phonghienTai);
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                }else {
                                    Toast.makeText(context, "thêm mới k thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                editLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                        View vieww = inflater.inflate(R.layout.dialog_sua_phong, null);
                        EditText edt_update_sophong = vieww.findViewById(R.id.edt_themsophong_update);
                        EditText edt_update_giaphong = vieww.findViewById(R.id.edt_themgiaphong_update);
                        EditText edt_update_giadien = vieww.findViewById(R.id.edt_themgiadien_update);
                        EditText edt_update_gianuoc = vieww.findViewById(R.id.edt_themgianuoc_update);
                        EditText edt_update_wifi = vieww.findViewById(R.id.edt_themgiawifi_update);
                        Button btn_Cancel = vieww.findViewById(R.id.btn_huy_phong_update);
                        Button btn_update = vieww.findViewById(R.id.btn_them_phong_update);

                        edt_update_sophong.setText(phonghienTai.getSoPhong()+ "");
                        edt_update_giaphong.setText(phonghienTai.getGiaPhong()+ "");
                        edt_update_giadien.setText(phonghienTai.getGiaDien()+ "");
                        edt_update_gianuoc.setText(phonghienTai.getGiaNuoc()+ "");
                        edt_update_wifi.setText(phonghienTai.getGiaWifi()+ "");

                        builder.setView(vieww);
                        Dialog dialog = builder.create();
                        dialog.show();
                        btn_Cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        btn_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PhongDAO phongDAO = new PhongDAO(context);

                                phonghienTai.setSoPhong(Integer.parseInt(edt_update_sophong.getText().toString()));
                                phonghienTai.setGiaPhong(Integer.parseInt(edt_update_giaphong.getText().toString()));
                                phonghienTai.setGiaDien(Integer.parseInt(edt_update_giadien.getText().toString()));
                                phonghienTai.setGiaNuoc(Integer.parseInt(edt_update_gianuoc.getText().toString()));
                                phonghienTai.setGiaWifi(Integer.parseInt(edt_update_wifi.getText().toString()));
                                if (phongDAO.updatePhong(phonghienTai)>0){
                                    Toast.makeText(context,"Sửa thành công",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    phongList.clear();
                                    phongList.addAll(phongDAO.getAll());
                                    notifyDataSetChanged();
                                }else {
                                    Toast.makeText(context,"Sửa ko thành công",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                themhopdong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                        View view = inflater.inflate(R.layout.tao_hop_dong, null);
                        hopDongDAO=new HopDongDAO(context);
                        khachThueDAO=new KhachThueDAO(context);

                        if(phonghienTai.getTrangThai()==1){
                            Toast.makeText(context, "Phòng chưa add có người thuê", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(hopDongDAO.getHopDongByIdPhong(String.valueOf(phonghienTai.getIdPhong()),"1")!=null){
                            Toast.makeText(context, "Phòng đã tạo hợp đồng thành công", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(hopDongDAO.getHopDongByIdPhong(String.valueOf(phonghienTai.getIdPhong()),"2")!=null){
                            Toast.makeText(context, "Phòng đang có hợp đồng quá hạn, hãy gia hạn", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {


                            khachThueDAO = new KhachThueDAO(context);
                            KhachThue khachThue = khachThueDAO.getUserByIdPhong(String.valueOf(phonghienTai.getIdPhong()));

                            TextView edsophong = view.findViewById(R.id.edt_SoPhong_HopDong);
                            TextView edtTenKhachThue = view.findViewById(R.id.edt_TenKhachThue_HopDong);
                            EditText edt_ngaybatdau_hopdong = view.findViewById(R.id.edt_NgayBatDau_HopDong);
                            EditText edt_ngayketthuc_hopdong = view.findViewById(R.id.edt_NgayKetThuc_HopDong);
                            ImageView img_a = view.findViewById(R.id.img_ngay_bat_dau_HopDong);
                            ImageView img_b = view.findViewById(R.id.img_Ngay_Ket_Thuc_HopDong);
                            EditText edt_songuoi = view.findViewById(R.id.edt_SoNguoi_HopDong);
                            EditText edt_soluongxe = view.findViewById(R.id.edt_SoLuongXe_HopDong);
                            EditText edt_tiencoc = view.findViewById(R.id.edt_TienCoc_HopDong);
                            TextView edt_trangthai = view.findViewById(R.id.edt_TrangThai_HopDong);
                            Button btnCancel = view.findViewById(R.id.btn_huy_HopDong);
                            Button btnSavet = view.findViewById(R.id.btn_Tao_HopDong);

                            //----
                            Calendar calendar = Calendar.getInstance();//Lay time
                            final int year = calendar.get(Calendar.YEAR);
                            final int month = calendar.get(Calendar.MONTH);
                            final int day = calendar.get(calendar.DAY_OF_MONTH);

                            //datePicker
                            img_a.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int y, int m, int d) {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            calendar.set(y, m, d);
                                            String dateString = sdf.format(calendar.getTime());
                                            edt_ngaybatdau_hopdong.setText(dateString);
                                        }
                                    }, year, month, day);
                                    datePickerDialog.show();
                                }
                            });
                            //datePicker
                            img_b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int y, int m, int d) {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            calendar.set(y, m, d);
                                            String dateString = sdf.format(calendar.getTime());
                                            edt_ngayketthuc_hopdong.setText(dateString);
                                        }
                                    }, year, month, day);
                                    datePickerDialog.show();
                                }
                            });
                            edsophong.setText(""+phonghienTai.getSoPhong());
                            edtTenKhachThue.setText(khachThue.getHoTen());
                            builder.setView(view);
                            Dialog dialog = builder.create();
                            dialog.show();


                            edt_trangthai.setText("đang thuê");
                            btnSavet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if( edt_ngaybatdau_hopdong.getText().toString().length()==0){
                                        Toast.makeText(context, "Ngày bắt đầu không được để trống", Toast.LENGTH_SHORT).show();
                                        return;
                                    }if( edt_ngayketthuc_hopdong .getText().toString().length()==0){
                                        Toast.makeText(context, "Ngày kết thúc không được để trống và ", Toast.LENGTH_SHORT).show();
                                        return;
                                    }if(edt_songuoi.getText().toString().length()==0){
                                        Toast.makeText(context, "Số người không được để trống", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    try {
                                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                                        Date date1=sdf.parse(edt_ngaybatdau_hopdong.getText().toString());
                                        Date date2=sdf.parse(edt_ngayketthuc_hopdong.getText().toString());
                                        if(date1.compareTo(date2)<0){
                                            HopDong hopDong = new HopDong();
                                            hopDong.setIdPhong(phonghienTai.getIdPhong());
                                            hopDong.setIdKhachThue(khachThue.getIdKhachThue());
                                            hopDong.setNgayBatDau((edt_ngaybatdau_hopdong.getText().toString()));
                                            hopDong.setNgayKetThuc((edt_ngayketthuc_hopdong.getText().toString()));
                                            hopDong.setSoNguoi(Integer.parseInt(edt_songuoi.getText().toString()));
                                            hopDong.setSoLuongXe(Integer.parseInt(edt_soluongxe.getText().toString()));
                                            hopDong.setTiecCoc(Integer.parseInt(edt_tiencoc.getText().toString()));
                                            hopDong.setTrangThaiHD(1);
                                            if (hopDongDAO.insertHopDong(hopDong)>0){

                                                Toast.makeText(context, "thêm mới thành công", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                                checkData();
                                                dialog.dismiss();
                                            }else {
                                                Toast.makeText(context, "thêm mới k thành công", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            Toast.makeText(context, "Ngày kết thúc lớn hơn ngày bắt đầu", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Log.d("ssssssss", "onClick: "+e);
                                    }

                                }
                            });
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }


                });
                themhoadon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HopDongDAO hopDongDAO=new HopDongDAO(context);

                        try {
                            if(hopDongDAO.getHopDongByIdPhong(String.valueOf(phonghienTai.getIdPhong()),"1")!=null){
                                ThemHoadon(phonghienTai);
                                notifyDataSetChanged();
                            }else {
                                Toast.makeText(context, "Hãy tạo hợp đồng cho khách ở phòng này", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }catch (Exception e){
                            Toast.makeText(context, "Hãy tạo hợp đồng cho khách ở phòng này", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });
            }
        });
    }

    private void ThongTinPhong(Phong phong) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thongtinphong);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        TextView tvSophong=dialog.findViewById(R.id.tvsophong);
        TextView tvGiaPhong=dialog.findViewById(R.id.tvGiaPhong);
        TextView tvGiadien=dialog.findViewById(R.id.tvGiaDien);
        TextView tvGianuoc=dialog.findViewById(R.id.tvGiaNuoc);
        TextView tvWifi=dialog.findViewById(R.id.tvGiaWifi);
        TextView tvtrangthai=dialog.findViewById(R.id.tinhTrang);
        TextView tvnguoithue=dialog.findViewById(R.id.tvnguoiThue);
        Button button = dialog.findViewById(R.id.btn_dissmiss_phong);
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        tvSophong.setText("Số phòng: "+phong.getSoPhong());
        tvGiaPhong.setText("Giá Phòng: "+decimalFormat.format(phong.getGiaPhong())+"VndĐ");
        tvGiadien.setText("Giá Điện: "+decimalFormat.format(phong.getGiaDien())+"VndĐ");
        tvGianuoc.setText("Giá Nước: "+decimalFormat.format(phong.getGiaNuoc())+"VndĐ");
        tvWifi.setText("Giá Wifi: "+decimalFormat.format(phong.getGiaWifi())+"VndĐ");
        if(phong.getTrangThai()==1){
            tvtrangthai.setText("Trạng Thái: chưa ai thuê ");
        }
        else {
            tvtrangthai.setText("Trạng Thái: đã có người thuê ");
            KhachThue khachThue=khachThueDAO.getUserByIdPhong(String.valueOf(phong.getIdPhong()));
            tvnguoithue.setText("Người Thuê: "+khachThue.getHoTen());

        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    private void checkData(){
        hopDongDAO=new HopDongDAO(context);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        String date = sdf.format(new Date());



        List<HopDong> hopDongList=hopDongDAO.getAll();
        if(hopDongList.size()>0){
            for(int i=0;i<hopDongList.size();i++){
                String ngayhethan=hopDongList.get(i).getNgayKetThuc();
                try {
                    Date date1=sdf.parse(date);
                    Date date2=sdf.parse(ngayhethan);
                    if(date2.compareTo(date1)<0){
                        if(hopDongList.get(i).getTrangThaiHD()==1){
                            hopDongList.get(i).setTrangThaiHD(2);
                            Log.d("TAG", "checkData: "+"đã hết hạn");
                            hopDongDAO.updateHopDong(hopDongList.get(i));
                        }

                    }
                    else {
                        Log.d("TAG", "checkData: "+hopDongList.get(i).getTrangThaiHD()+hopDongList.get(i).getIdHopDong());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private void ThemHoadon(Phong phong) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tao_hoa_don);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        EditText Ed_NgayTao_HDon, Ed_NhapSoDien_HDon, Ed_NhapSoNuoc_HDon, Ed_ChiPhiKhac_HDon,Ed_TongTien_HDon,Ed_GhiChu_HDon,ed_tenhoaDon;
        TextView tvsophong;
        ImageView image_ngay;
        TextView tvtongtien;
        Button Btn_huy_HDon, Btn_them_HDon,btn_tongtien;
        //ánh xạ
        Ed_NgayTao_HDon = dialog.findViewById(R.id.ed_NgayTao_HDon);
        ed_tenhoaDon = dialog.findViewById(R.id.ed_tenHoaDon);

        Ed_NhapSoDien_HDon = dialog.findViewById(R.id.ed_NhapSoDien_HDon);
        Ed_NhapSoNuoc_HDon = dialog.findViewById(R.id.ed_NhapSoNuoc_HDon);
        Ed_ChiPhiKhac_HDon = dialog.findViewById(R.id.ed_ChiPhiKhac_HDon);
        tvsophong=dialog.findViewById(R.id.tvsophong_hd);
        tvsophong.setText("Phòng: "+phong.getSoPhong());
        Ed_GhiChu_HDon = dialog.findViewById(R.id.ed_GhiChu_HDon);
        image_ngay= dialog.findViewById(R.id.image_NgayTao_HDon);
        Btn_them_HDon= dialog.findViewById(R.id.btn_them_HDon);
        Btn_huy_HDon= dialog.findViewById(R.id.btn_huy_HDon);
        btn_tongtien= dialog.findViewById(R.id.tinhtongtien);
        tvtongtien=dialog.findViewById(R.id.tvtongtien);
        CheckBox checkBox=dialog.findViewById(R.id.checkboxTrangthai);
        //---------------- an voa anh chon ngay
        Calendar calendar = Calendar.getInstance();//Lay time
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(calendar.DAY_OF_MONTH);
        ed_tenhoaDon.setText("Hóa Đơn tháng "+ (month+1)+" phòng " +phong.getSoPhong());
        image_ngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int d) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        calendar.set(y, m, d);
                        String dateString = sdf.format(calendar.getTime());
                        Ed_NgayTao_HDon.setText(dateString);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        btn_tongtien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check vavidate

                ghiChu=Ed_GhiChu_HDon.getText().toString();
                try {
                    sodien= Integer.parseInt(Ed_NhapSoDien_HDon.getText().toString());
                    sonuoc= Integer.parseInt(Ed_NhapSoDien_HDon.getText().toString());
                    chiphikhac= Integer.parseInt(Ed_NhapSoDien_HDon.getText().toString());
                }
                catch (Exception e){
                    Toast.makeText(context, "số điện, nước, chi phí khác phải điền dạng số", Toast.LENGTH_SHORT).show();
                    return;
                }

                int giadien=phong.getGiaDien();
                int giaphong=phong.getGiaPhong();
                int gianuoc=phong.getGiaNuoc();
                int giawifi=phong.getGiaWifi();
                tongtien=(giadien*sodien)+(gianuoc*sonuoc)+giaphong+giawifi+chiphikhac;
                tvtongtien.setText("Tổng Hóa Đơn: "+tongtien);
            }
        });
        Btn_them_HDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tenhoadon=ed_tenhoaDon.getText().toString();
                if(tenhoadon.length()==0){
                    Toast.makeText(context, "Hãy điền tên hóa đơn", Toast.LENGTH_SHORT).show();
                    return;
                }
                ngaytao=Ed_NgayTao_HDon.getText().toString();
                if(ngaytao.length()==0){
                    Toast.makeText(context, "Hãy điền tên hóa đơn", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(tongtien==0){
                    Toast.makeText(context, "hãy tính tổng", Toast.LENGTH_SHORT).show();
                    return;
                }
                //ínert
                hopDongDAO=new HopDongDAO(context);
                HopDong hopDong=hopDongDAO.getHopDongByIdPhong(String.valueOf(phong.getIdPhong()),"1");
                hoaDonDao=new HoaDonDao(context);
                HoaDon hoaDon=new HoaDon();
                hoaDon.setIdPhong(phong.getIdPhong());
                hoaDon.setTenHoaDOn(tenhoadon);
                hoaDon.setGhiChu(ghiChu);
                hoaDon.setChiPhiKhac(chiphikhac);
                hoaDon.setNgay(ngaytao);
                hoaDon.setSoDien(sodien);
                hoaDon.setSoNuoc(sonuoc);
                hoaDon.setIdHopDong(hopDong.getIdHopDong());
                boolean trangthai=checkBox.isChecked();
                int trangTHai;
                Log.d("sssssss", "onClick: "+trangthai);
                if (trangthai){
                    //đã thanh toán
                    trangTHai=2;
                }
                else {
//                    chưa thanh toan
                    trangTHai=1;
                }
                hoaDon.setTrangThai(trangTHai);
                hoaDon.setTong(tongtien);

                if(hoaDonDao.insertHoaDon(hoaDon)){
                    Toast.makeText(context, "thêm mới thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(context, "thêm mới không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Btn_huy_HDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    @Override
    public int getItemCount() {
        return phongList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSrearch=charSequence.toString();
                if(strSrearch.isEmpty()){
                    phongList=phongListold;
                }
                else {
                    List<Phong> phongs=new ArrayList<>();
                    for(Phong phong: phongListold){
                        if((phong.getSoPhong()+"").toLowerCase().contains(strSrearch.toLowerCase())){
                            phongs.add(phong);
                        }
                    }
                    phongList=phongs;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=phongList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                try {
                    phongList= (List<Phong>) filterResults;
                    notifyDataSetChanged();
                }catch (Exception e){

                }
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ln_item_dv;
        TextView tv_sophong;
        ImageView imageView;
        public MyViewHolder(@NonNull View view) {
            super(view);
            ln_item_dv = view.findViewById(R.id.ln_menu_phong);
            imageView=view.findViewById(R.id.thHopDong);
            tv_sophong = view.findViewById(R.id.tv_ssophong);
        }
    }
}
