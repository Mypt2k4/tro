package Adapter;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

import HopDong.HopDongDAO;
import HopDong.KhachThueDAO;
import HopDong.PhongDAO;
import Model.HopDong;
import Model.KhachThue;
import Model.Phong;
import longvtph16016.poly.appquanlyphongtro.R;

public class KhachThueAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<KhachThue> list;
    private List<KhachThue> listold;
    private CardView ln_item_dv;

    public KhachThueAdapter(Context context, List<KhachThue> list  ) {
        this.context = context;
        this.list = list;
        this.listold=list;
    }

    public void setData(List<KhachThue> lists){
        this.list= lists;

        notifyDataSetChanged();// có tác dụng refresh lại data
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
        KhachThueAdapter.MyViewHolder myViewHolder = null;
        if (view == null) {

            myViewHolder = new MyViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.item_khachthue, null);
            myViewHolder.tv_KhachThue = view.findViewById(R.id.txt_item_khachThue);
            view.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) view.getTag();
        }
        myViewHolder.tv_KhachThue.setText((i+1)+" .Khách Thuê"+": "+list.get(i).getHoTen());
        PhongDAO phongDAO=new PhongDAO(context);
        Phong phong=phongDAO.getPhongById(String.valueOf(list.get(i).getIdPhong()));
        ln_item_dv=view.findViewById(R.id.ln_menu_khachthue);
        TextView txtCCCD=view.findViewById(R.id.txtCCCD);
        TextView txtPhongTro=view.findViewById(R.id.txtPhongTro);
        TextView txtsdt=view.findViewById(R.id.txtSDT);
        ImageView imgHd=view.findViewById(R.id.HDKhachThue);
        HopDongDAO hopDongDAO=new HopDongDAO(context);

        KhachThue khachThue=list.get(i);
        HopDong hopDong=hopDongDAO.getHopDongByIdKT(khachThue.getIdKhachThue()+"");
        if(hopDong==null){
            ln_item_dv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94F589")));
            txtPhongTro.setText("Phòng trọ: "+phong.getSoPhong());
            txtCCCD.setText("CCCD: "+khachThue.getCccd());
            txtsdt.setText("SĐT: "+khachThue.getSdt());
            imgHd.setVisibility(View.INVISIBLE);
        }
        else {
            if(hopDong.getTrangThaiHD()==3)
            {
                ln_item_dv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E8A5A7")));
                txtPhongTro.setText("Đã từng thuê Phòng : "+ phongDAO.getPhongById(String.valueOf(hopDong.getIdPhong())).getSoPhong());
                txtCCCD.setText("CCCD: "+khachThue.getCccd());
                txtsdt.setText("SĐT: "+khachThue.getSdt());
                imgHd.setVisibility(View.VISIBLE);
                imgHd.setImageResource(R.drawable.unhd);
            }
            else if(hopDong.getTrangThaiHD()==2){
                ln_item_dv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94F589")));
                txtPhongTro.setText("Phòng Trọ: "+phong.getSoPhong());
                txtCCCD.setText("CCCD: "+khachThue.getCccd());
                txtsdt.setText("SĐT: "+khachThue.getSdt());
                imgHd.setVisibility(View.VISIBLE);
                imgHd.setImageResource(R.drawable.ic_baseline_announcement_24);
            }
            else {
                ln_item_dv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94F589")));
                txtPhongTro.setText("Phòng Trọ: "+phong.getSoPhong());
                txtCCCD.setText("CCCD: "+khachThue.getCccd());
                txtsdt.setText("SĐT: "+khachThue.getSdt());
                imgHd.setVisibility(View.VISIBLE);
                imgHd.setImageResource(R.drawable.hopdong);

            }
        }



        ln_item_dv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.bottom_layout_khachthue);
                LinearLayout editLayout = dialog.findViewById(R.id.edit_layout_khachthue);

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);


                editLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editKhachTHue(i);
                    }
                });
            }
        });
        return view;
    }

    private void editKhachTHue(int i) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View vieww = inflater.inflate(R.layout.diglog_sua_khach_thue, null);

        builder.setView(vieww);
        Dialog dialog = builder.create();
        dialog.show();

        TextView tv_SoPhong_ThemKhachThue_update=dialog.findViewById(R.id.tv_SoPhong_ThemKhachThue_update);
        TextView edTenK_update=dialog.findViewById(R.id.edTenK_update);
        EditText edtsdt=dialog.findViewById(R.id.edSde_update);
        EditText edtcccd=dialog.findViewById(R.id.edCcce_update);
        Button btnsua=dialog.findViewById(R.id.btnSave_update);
        Button btnhuy=dialog.findViewById(R.id.btnCancee_update);

        PhongDAO phongDAO=new PhongDAO(context);
        Phong phong=phongDAO.getPhongById(list.get(i).getIdPhong()+"");
        if(phong==null){
            tv_SoPhong_ThemKhachThue_update.setText("Khách đã ngừng thuê");
        }
        else {
            tv_SoPhong_ThemKhachThue_update.setText("Phòng: "+phong.getSoPhong()+"");
        }

        edTenK_update.setText(list.get(i).getHoTen()+ "");
        edtsdt.setText(list.get(i).getSdt()+ "");
        edtcccd.setText(list.get(i).getCccd()+ "");


        KhachThueDAO khachThueDAO =new KhachThueDAO(context);
        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate
                Boolean check = true;
                String edTenK =edTenK_update.getText().toString();
                String sdt=edtsdt.getText().toString();
                String cccd=edtcccd.getText().toString();
                //validate
                if(edTenK.length()==0){
                    edTenK_update.requestFocus();
                    Toast.makeText(context, "Hãy nhập tên chủ phòng", Toast.LENGTH_SHORT).show();
                    check = false;
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

                list.get(i).setHoTen(edTenK);
                list.get(i).setSdt(sdt);
                list.get(i).setCccd(cccd);
                long err=khachThueDAO.updateKhachThue(list.get(i));
                if(err>0){

                    Toast.makeText(context,"Sua thanh cong",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    notifyDataSetChanged();


                }
                else {
                    Toast.makeText(context,"Sua khong thanh cong",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public  class MyViewHolder {
        //khai báo các thành phần view có trong layoutitem
        private TextView tv_KhachThue,giaphong,giadien,gianuoc,giawifi,trangthai;
    }
    public void detail(int i) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.detail_khachthue);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        Button button = dialog.findViewById(R.id.btn_dissmiss_khachthue);
        TextView tvname=dialog.findViewById(R.id.thongtinname);
        TextView tvsdt=dialog.findViewById(R.id.thongtinsdt);
        TextView tvcccd=dialog.findViewById(R.id.thongtincccd);
        TextView tvophong=dialog.findViewById(R.id.thongtinophong);
        tvname.setText("Họ và Tên: "+list.get(i).getHoTen());
        tvcccd.setText("Số Điện Thoại: 0"+list.get(i).getSdt());
        tvsdt.setText("Số CCCD: "+list.get(i).getCccd());
        PhongDAO phongDAO=new PhongDAO(context);
        Log.d("cccc", "detail: "+list.get(i).getIdKhachThue());
        Phong phong=phongDAO.getPhongById(String.valueOf(list.get(i).getIdPhong()));

        if(phong==null)
        {
            tvophong.setText("Khách Chưa Thuê Phòng nào");

        }
        else
        {
            tvophong.setText("Phòng Ở: "+phong.getSoPhong());

        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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


                    List<KhachThue> khachThueList=new ArrayList<>();
                    for(KhachThue khachThue: listold){
                        if(khachThue.getHoTen().toLowerCase().contains(strSrearch.toLowerCase())){
                            khachThueList.add(khachThue);
                        }
                    }
                    list=khachThueList;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                try {
                    list= (List<KhachThue>) filterResults;
                    notifyDataSetChanged();
                }catch (Exception e){

                }

            }
        };
    }
}
