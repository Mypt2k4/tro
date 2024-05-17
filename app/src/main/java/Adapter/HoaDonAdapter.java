package Adapter;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import HopDong.HoaDonDao;
import HopDong.PhongDAO;
import Model.HoaDon;
import Model.Phong;
import longvtph16016.poly.appquanlyphongtro.R;

public class HoaDonAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<HoaDon> list;
    private List<HoaDon> listold;


    // biến tạo hóa đơn
    String tenhoadon,ngaytao,ghiChu;
    int sodien,sonuoc,chiphikhac,tongtien;


    public HoaDonAdapter(Context context  ) {
        this.context = context;

    }

    public void setData(List<HoaDon> arrayList){
        this.list= arrayList;
        this.listold=list;
        notifyDataSetChanged();// có tác dụng refresh lại data
    }

    // tim kiem
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSrearch=charSequence.toString();
                if(strSrearch.isEmpty()){
                    list=listold;
                }
                else {
                    List<HoaDon> hoaDonList=new ArrayList<>();
                    for(HoaDon hoaDon: listold){
                        if(hoaDon.getTenHoaDOn().toLowerCase().contains(strSrearch.toLowerCase())){
                            hoaDonList.add(hoaDon);
                        }
                    }
                    list=hoaDonList;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                try {
                    list= (List<HoaDon>) filterResults;
                    notifyDataSetChanged();
                }catch (Exception e){

                }
            }
        };
    }

    public final class MyViewHolder {
        //khai báo các thành phần view có trong layoutitem
        private TextView tv_sohoadon, Ngay,SoDien,SoNuoc,Tong,ChiPhiKhac, GhiChu;

    }
    public int getCount() {
        if(list!=null)
            return list.size();
        return 0;
    }
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder myViewHolder = null;
        if (view == null) {
            myViewHolder = new MyViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.iteam_hoadon, null);
            myViewHolder.tv_sohoadon = view.findViewById(R.id.txtnamehd);
            view.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) view.getTag();
        }
        HoaDon hoaDonhientai=list.get(i);
        PhongDAO phongDAO=new PhongDAO(context);
        Phong phong=phongDAO.getPhongById(String.valueOf(hoaDonhientai.getIdPhong()));
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        CardView ln_item_dv = view.findViewById(R.id.ln_menu_hoadon);
        TextView txtIDhoaDon=view.findViewById(R.id.txtIDhoaDon);
        TextView txtTongTien=view.findViewById(R.id.txtTongTien);
        TextView txttienphong=view.findViewById(R.id.txttienphong);
        TextView txtDichVU=view.findViewById(R.id.txtDichVU);
        TextView txtTrangThai=view.findViewById(R.id.txtTrangThai);
        TextView txtGiChu=view.findViewById(R.id.txtGiChu);
        //hien thong tin
        txtIDhoaDon.setText("#"+(i+1));
        txtTongTien.setText(decimalFormat.format(hoaDonhientai.getTong())+" Đ");
        txttienphong.setText(decimalFormat.format(phong.getGiaPhong())+" Đ");
        txtGiChu.setText("Ghi chú: "+hoaDonhientai.getGhiChu());
        myViewHolder.tv_sohoadon.setText(hoaDonhientai.getTenHoaDOn());

        int tiendichvu=hoaDonhientai.getChiPhiKhac()+(hoaDonhientai.getSoDien()*phong.getGiaDien())+(hoaDonhientai.getSoNuoc()+phong.getGiaDien())+phong.getGiaWifi();
        txtDichVU.setText(decimalFormat.format(tiendichvu)+" Đ");
        if(hoaDonhientai.getTrangThai()==2) {
            ln_item_dv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#66FFB2")));
            txtTrangThai.setText("Trạng Thái: đã thanh toán");
        }
        else if(hoaDonhientai.getTrangThai()==1){
            ln_item_dv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E1DDDD")));
            txtTrangThai.setText("Trạng Thái: Chưa thanh toán");
        }
        txtDichVU.setText(decimalFormat.format(tiendichvu)+" Đ");
        ln_item_dv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: "+hoaDonhientai.getTrangThai());
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_bottom_hoadon);
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);

                LinearLayout edt_xem_hd = dialog.findViewById(R.id.edt_xem_hd);

                edt_xem_hd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       editHoaDon(hoaDonhientai);
                    }
                });
            }
        });
        return view;
    }

    private void editHoaDon(HoaDon hoaDon) {
        final  Dialog dialog=new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.requestWindowFeature((Window.FEATURE_NO_TITLE));
        dialog.setContentView(R.layout.dialog_sua_hoa_don);
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.show();
        TextView  Ed_NgayTao_HDon, Ed_NhapSoDien_HDon, Ed_NhapSoNuoc_HDon, Ed_ChiPhiKhac_HDon,Ed_TongTien_HDon,Ed_GhiChu_HDon,ed_tenhoaDon;
        TextView tvsophong;
        ImageView image_ngay;
        TextView tvtongtien;
        Button Btn_huy_HDon, Btn_sua_HDon,btn_tongtien;
        //ánh xạ
        Ed_NgayTao_HDon = dialog.findViewById(R.id.ed_NgayTao_HDon);
        ed_tenhoaDon = dialog.findViewById(R.id.ed_tenHoaDon);
        Ed_NhapSoDien_HDon = dialog.findViewById(R.id.ed_NhapSoDien_HDon);
        Ed_NhapSoNuoc_HDon = dialog.findViewById(R.id.ed_NhapSoNuoc_HDon);
        Ed_ChiPhiKhac_HDon = dialog.findViewById(R.id.ed_ChiPhiKhac_HDon);
        Ed_GhiChu_HDon = dialog.findViewById(R.id.ed_GhiChu_HDon);
        image_ngay= dialog.findViewById(R.id.image_NgayTao_HDon);
        Btn_sua_HDon= dialog.findViewById(R.id.btn_sua_HDon);
        Btn_huy_HDon= dialog.findViewById(R.id.btn_huy_HDon);
        tvtongtien=dialog.findViewById(R.id.tvtongtien);
        CheckBox checkBox=dialog.findViewById(R.id.checkboxTrangthai);

        ed_tenhoaDon.setText(hoaDon.getTenHoaDOn());
        Ed_NgayTao_HDon.setText(hoaDon.getNgay());
        Ed_NhapSoDien_HDon.setText(hoaDon.getSoDien()+"");
        Ed_NhapSoNuoc_HDon.setText(hoaDon.getSoNuoc()+"");
        Ed_ChiPhiKhac_HDon.setText(hoaDon.getChiPhiKhac()+"");
        Ed_GhiChu_HDon.setText(hoaDon.getGhiChu());
        tongtien=hoaDon.getTong();
        tvtongtien.setText("Tổng Hóa Đơn: "+tongtien);
        if(hoaDon.getTrangThai()==1){
            checkBox.setChecked(false);
        }
        else {
            checkBox.setChecked(true);
        }

        //---------------- an voa anh chon ngay
        Calendar calendar = Calendar.getInstance();//Lay time
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(calendar.DAY_OF_MONTH);
        image_ngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int d) {
                        Ed_NgayTao_HDon.setText(y + "/" + (m + 1) + "/" + d);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        HoaDonDao hoaDonDao = new HoaDonDao(context);

        //suahd
        Btn_sua_HDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean trangthai=checkBox.isChecked();
                int trangTHai;
                if (trangthai){
                    //đã thanh toán
                    trangTHai=2;
                }
                else {
//                    chưa thanh toan
                    trangTHai=1;
                }
                hoaDon.setTrangThai(trangTHai);
                long err=hoaDonDao.updateHoaDon(hoaDon);
                if(err>0){
                    Toast.makeText(context,"Cập nhập thành công",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    notifyDataSetChanged();
                }
                else {
                    Toast.makeText(context,"Cập nhập không thành công",Toast.LENGTH_LONG).show();
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


}

