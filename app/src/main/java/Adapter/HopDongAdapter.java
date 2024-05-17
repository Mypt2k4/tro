package Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import HopDong.HopDongDAO;
import HopDong.KhachThueDAO;
import HopDong.PhongDAO;
import Model.HopDong;
import Model.KhachThue;
import Model.Phong;
import longvtph16016.poly.appquanlyphongtro.R;

public class HopDongAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<HopDong> list;
    private List<HopDong> listold;
    private Button btnthemHopDong;
    EditText edtNgayBatDau;
    EditText edtNgayKetThuc;
    EditText edtSoLuongXe;
    EditText edtTienCoc;
    EditText edtSoNguoi;


    public HopDongAdapter(Context context, List<HopDong> list  ) {
        this.context = context;
        this.list = list;
        this.listold=list;
    }

    public void setData(List<HopDong> list){
        this.list= list;
        notifyDataSetChanged();// có tác dụng refresh lại data
    }


    public final class MyViewHolder {
        //khai báo các thành phần view có trong layoutitem
        private TextView tv_HopDong,giaphong,giadien,gianuoc,giawifi,trangthai;

    }
    @Override
    public int getCount() {
        if(list!=null)
            return list.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HopDongAdapter.MyViewHolder myViewHolder = null;
        if (view == null) {
            myViewHolder = new MyViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_hopdong, null);
            myViewHolder.tv_HopDong = view.findViewById(R.id.txt_item_hopDong);
            view.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) view.getTag();
        }
        CardView ln_item_dv = view.findViewById(R.id.linear_item_hd);
        HopDong hopDong=list.get(i);
        PhongDAO phongDAO = new PhongDAO(context);
        Phong phong = phongDAO.getPhongById(String.valueOf(list.get(i).getIdPhong()));
        KhachThueDAO khachThueDAO=new KhachThueDAO(context);
        KhachThue khachThue=khachThueDAO.getUserById(String.valueOf(hopDong.getIdKhachThue()));

        myViewHolder.tv_HopDong.setText("#"+(i+1)+". Hợp Đồng Phòng: "+phong.getSoPhong());
        TextView txtNguoithue,txtNgayBatdau,txtNgayKetThuc,txtSoNGuoi,txtSoxe;
        ImageView imageView;
        txtNguoithue=view.findViewById(R.id.txtKhachThue);
        txtNgayBatdau=view.findViewById(R.id.txtNgayBatdau);
        txtNgayKetThuc=view.findViewById(R.id.txtNgayketthuc);
        txtSoNGuoi=view.findViewById(R.id.txtcPerson);
        txtSoxe=view.findViewById(R.id.txtCar);
        imageView=view.findViewById(R.id.imghd);
        txtNguoithue.setText("Người Thuê: "+khachThue.getHoTen());
        txtNgayBatdau.setText("Ngày bắt đầu: "+hopDong.getNgayBatDau());
        txtNgayKetThuc.setText("Ngày kết thúc: "+hopDong.getNgayKetThuc());
        txtSoNGuoi.setText("Số người ở: "+hopDong.getSoNguoi());
        txtSoxe.setText("Số Lượng xe: "+hopDong.getSoLuongXe());
        if(list.get(i).getTrangThaiHD()==1) {
            ln_item_dv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94F589")));
            imageView.setImageResource(R.drawable.hopdong);
        }
        else if(list.get(i).getTrangThaiHD()==2){
            ln_item_dv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F7FF00")));
            imageView.setImageResource(R.drawable.ic_baseline_announcement_24);
        }
        else if(list.get(i).getTrangThaiHD()==3){
            ln_item_dv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E8A5A7")));
            imageView.setImageResource(R.drawable.unhd);
        }

        ln_item_dv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HopDong hopDong=list.get(i);
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.bottom_layout_hdong);
                LinearLayout giahanLayuot = dialog.findViewById(R.id.edit_layout_dh);
                LinearLayout chamDutHD = dialog.findViewById(R.id.huyGiaHan);

                LinearLayout detailLayout = dialog.findViewById(R.id.view_layout_hd);

                dialog.show();

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);

                detailLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        detail(list.get(i));
                    }
                });
                giahanLayuot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(hopDong.getTrangThaiHD()==3){
                            Toast.makeText(context,"Hợp đồng đã chấm dứt, không thể gia hạn",Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            GiaHanHD(hopDong);
                        }

                    }
                });
                chamDutHD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(hopDong.getTrangThaiHD()==3){
                            Toast.makeText(context,"Hợp đồng đã chấm dứt",Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            chamDutHd(hopDong);
                        }
                    }
                });
            }
        });


        return view;
    }
    public void detail(HopDong hopDong) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.detail_hop_dong);
        TextView tvNgayTaoHd,tvTenKhachThue,tvCCCd,tvSdt,tvSoPhong,tvGiaPhong,tvGiaDien,tvGianuoc,
                tvGiawifi,tvtienCoc,tvThoiHan,tvchuky,vtnameChuky;
        tvNgayTaoHd=dialog.findViewById(R.id.thoigianTaoHD);
        tvTenKhachThue=dialog.findViewById(R.id.nameKhachThue);
        tvCCCd=dialog.findViewById(R.id.cccdKhachThue);
        tvSdt=dialog.findViewById(R.id.sdtKhachThue);
        tvSoPhong=dialog.findViewById(R.id.soPhongHopDong);
        tvGiaPhong=dialog.findViewById(R.id.HopDongGIaPhong);
        tvGiaDien=dialog.findViewById(R.id.TienDienHopDong);
        tvGianuoc=dialog.findViewById(R.id.TienNuocHopDong);
        tvGiawifi=dialog.findViewById(R.id.TienWifiHopDong);
        tvtienCoc=dialog.findViewById(R.id.TienCocHopDong);
        tvThoiHan=dialog.findViewById(R.id.ThoiHanHopDong);
        tvchuky=dialog.findViewById(R.id.chuKyKhachThue);
        vtnameChuky=dialog.findViewById(R.id.kytenKhachThue);
        KhachThueDAO khachThueDAO=new KhachThueDAO(context);
        KhachThue khachThue=khachThueDAO.getUserById(hopDong.getIdPhong()+"");
        PhongDAO phongDAO=new PhongDAO(context);
        Phong phong=phongDAO.getPhongById(hopDong.getIdPhong()+"");
        tvNgayTaoHd.setText("Ngày: "+hopDong.getNgayBatDau());

        tvTenKhachThue.setText("Ông/Bà: "+khachThue.getHoTen());
        tvSdt.setText("Số CCCD: "+khachThue.getSdt());
        tvSdt.setText("Số điện thoại :"+khachThue.getSdt());
        tvSoPhong.setText(phong.getSoPhong()+"");
        tvGiaPhong.setText("Giá Thuê: " +phong.getGiaPhong());
        tvGiaDien.setText("Tiền điện: "+phong.getGiaDien() +" Vnđ/kwh");
        tvGianuoc.setText("Tiền Nước: "+phong.getGiaNuoc() +" Vnđ/khối");
        tvGiawifi.setText("Tiền Wifi: "+phong.getGiaWifi() +" Vnđ/Tháng");
        tvtienCoc.setText("Tiền Cọc: "+hopDong.getTiecCoc() +" Vnđ");
        tvThoiHan.setText("Hợp đồng có giá trị kể từ ngày " +hopDong.getNgayBatDau()+ " đến ngày "+ hopDong.getNgayKetThuc());
        tvchuky.setText(khachThue.getHoTen());
        vtnameChuky.setText(khachThue.getHoTen());
        dialog.show();
    }
    public void GiaHanHD(HopDong hopDong){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.giahanhopdong);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        TextView tvNgayhethan=dialog.findViewById(R.id.tvngayHetHan);
        tvNgayhethan.setText("Hạn Hợp Đồng: "+hopDong.getNgayKetThuc());
        ImageView imageView=dialog.findViewById(R.id.image_NgayTao_GiaHan);
        EditText editText=dialog.findViewById(R.id.ed_NgayGiaHan);
        Button update=dialog.findViewById(R.id.btn_giaHanHd);
        Button cannel=dialog.findViewById(R.id.btn_HuygiaHanHd);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();//Lay time
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int d) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        calendar.set(y, m, d);
                        String dateString = sdf.format(calendar.getTime());
                        editText.setText(dateString);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.length()<0){
                    Toast.makeText(context,"hãy chọn ngày gia hạn",Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date1=sdf.parse(editText.getText().toString());
                    Date date2=sdf.parse(hopDong.getNgayKetThuc());
                    if(date2.compareTo(date1)<0){
                        hopDong.setNgayKetThuc(editText.getText().toString());
                        hopDong.setTrangThaiHD(1);
                        HopDongDAO hopDongDAO=new HopDongDAO(context);
                        hopDongDAO.updateHopDong(hopDong);
                        Toast.makeText(context,"Gia hạn thành công",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(context,"Chọn ngày xa hơn đi",Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        cannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    public void chamDutHd( HopDong hopDong){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chấm Dứt Hợp Đồng");
        builder.setMessage("Bạn Có Chắc Chắn Muốn Chấm Dứt Hợp Đồng Không?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HopDongDAO hopDongDAO=new HopDongDAO(context);
                int idPhong=hopDong.getIdPhong();
                KhachThueDAO khachThueDAO=new KhachThueDAO(context);
                KhachThue khachThue=khachThueDAO.getUserByIdPhong(String.valueOf(idPhong));
                khachThue.setIdPhong(0);
                khachThueDAO.updateKhachThue(khachThue);
                PhongDAO phongDAO=new PhongDAO(context);
                Phong phong=phongDAO.getPhongById(String.valueOf(idPhong));
                phong.setTrangThai(1);
                phongDAO.updatePhong(phong);
                hopDong.setTrangThaiHD(3);
                hopDongDAO.updateHopDong(hopDong);
                Toast.makeText(context,"Chấm dứt hợp đồng thành công",Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }
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

                    try {
                        HopDongDAO hopDongDAO=new HopDongDAO(context);
                        list=hopDongDAO.getAllTimKiem(strSrearch);
                    }
                    catch (Exception e){
                        list=listold;
                    }
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                try {
                    list= (List<HopDong>) filterResults;
                    notifyDataSetChanged();
                }catch (Exception e){

                }

            }
        };
    }


}