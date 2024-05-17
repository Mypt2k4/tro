package Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Adapter.PhongAdapter;
import HopDong.HopDongDAO;
import HopDong.PhongDAO;
import Model.HopDong;
import Model.Phong;
import longvtph16016.poly.appquanlyphongtro.R;


public class PhongFragment extends Fragment  {

    EditText edt_themsophong,edt_themgiaphong,edt_themgiadien,edt_themgianuoc,edt_themgiawifi,edt_trangthai;
    Button btn_themphong,btn_huyphong;
    FloatingActionButton fab;
    RecyclerView rcv_phong;
    HopDongDAO hopDongDAO;
    PhongAdapter adapterPhong;
    private PhongDAO phongDAO;
    List<Phong> lists;
    Context context;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = view.findViewById(R.id.fab_addPhong);
        rcv_phong = view.findViewById(R.id.rec_Phong);

        context = this.getActivity();
        lists=new ArrayList<>();
        checkData();
        phongDAO = new PhongDAO(context);
        lists=phongDAO.getAll();

        adapterPhong=new PhongAdapter(lists,context);
        rcv_phong.setAdapter(adapterPhong);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(context,2);
        rcv_phong.setLayoutManager(layoutManager);
        rcv_phong.setHasFixedSize(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(getContext());
            }
        });

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_seach);
        SearchView searchView = (SearchView)  searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterPhong.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterPhong.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void openDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogthemphong, null);

        edt_themsophong = view.findViewById(R.id.edt_themsophong);
        edt_themgiaphong = view.findViewById(R.id.edt_themgiaphong);
        edt_themgiadien = view.findViewById(R.id.edt_themgiadien);
        edt_themgianuoc = view.findViewById(R.id.edt_themgianuoc);
        edt_themgiawifi = view.findViewById(R.id.edt_themgiawifi);

        btn_themphong = view.findViewById(R.id.btn_them_phong);
        btn_huyphong = view.findViewById(R.id.btn_huy_phong);

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        btn_themphong.setOnClickListener(new View.OnClickListener() {
            Phong phong;

            @Override
            public void onClick(View view) {
                boolean check = true;

                //---------- check so phong
                String sop=edt_themsophong.getText().toString();
                int soPhong=0;

                if(sop.length()==0){
                    Toast.makeText(context,"Số phòng không được để trống",Toast.LENGTH_SHORT).show();
                    check=false;
                }
                else {
                    try {
                        soPhong = Integer.parseInt(sop);
                        if(soPhong<0){
                            Toast.makeText(context,"Số phòng phải lớn hơn 0",Toast.LENGTH_SHORT).show();
                            check=false;
                        }
                    }catch (Exception e){
                        Toast.makeText(context,"Số phòng phải là số nguyên",Toast.LENGTH_SHORT).show();
                        check=false;
                    }

                }

                try {
                    for (int i = 0;i<phongDAO.getAll().size();i++)
                    {
                        Phong phong_ = phongDAO.getAll().get(i);
                        if (soPhong == phong_.getSoPhong())
                        {
                            Toast.makeText(context, "Phòng "+soPhong+" đã có", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                }catch (Exception e){

                }

                //-------------------check gia phong
                String giaphongS=edt_themgiaphong.getText().toString();
                int giaphong=0;

                if(giaphongS.length()==0){
                    Toast.makeText(context,"Giá phòng không được để trống",Toast.LENGTH_SHORT).show();
                    check=false;
                }
                else {
                    try {
                        giaphong = Integer.parseInt(giaphongS);
                        if(giaphong<0){
                            Toast.makeText(context,"Giá phòng phải lớn hơn 0",Toast.LENGTH_SHORT).show();
                            check=false;
                        }
                    }catch (Exception e){
                        Toast.makeText(context,"Giá phòng phải là số nguyên",Toast.LENGTH_SHORT).show();
                        check=false;
                    }

                }

                //-------------------check gia dien
                String giadienS=edt_themgiadien.getText().toString();
                int giadien=0;

                if(giadienS.length()==0){
                    Toast.makeText(context,"Giá điện không được để trống",Toast.LENGTH_SHORT).show();
                    check=false;
                }
                else {
                    try {
                        giadien = Integer.parseInt(giadienS);
                        if(giadien<0){
                            Toast.makeText(context,"Giá điện phải lớn hơn 0",Toast.LENGTH_SHORT).show();
                            check=false;
                        }
                    }catch (Exception e){
                        Toast.makeText(context,"Giá điện phải là số nguyên",Toast.LENGTH_SHORT).show();
                        check=false;
                    }

                }

                //-------------------check gia dien
                String gianuocS=edt_themgianuoc.getText().toString();
                int gianuoc=0;

                if(gianuocS.length()==0){
                    Toast.makeText(context,"Giá nước không được để trống",Toast.LENGTH_SHORT).show();
                    check=false;
                }
                else {
                    try {
                        gianuoc = Integer.parseInt(gianuocS);
                        if(gianuoc<0){
                            Toast.makeText(context,"Giá nước phải lớn hơn 0",Toast.LENGTH_SHORT).show();
                            check=false;
                        }
                    }catch (Exception e){
                        Toast.makeText(context,"Giá nước phải là số nguyên",Toast.LENGTH_SHORT).show();
                        check=false;
                    }

                }

                //-------------------check gia dien
                String giawifiS=edt_themgianuoc.getText().toString();
                int giawifi=0;

                if(giawifiS.length()==0){
                    Toast.makeText(context,"Giá wifi không được để trống",Toast.LENGTH_SHORT).show();
                    check=false;
                }
                else {
                    try {
                        giawifi = Integer.parseInt(giawifiS);
                        if(gianuoc<0){
                            Toast.makeText(context,"Giá wifi phải lớn hơn 0",Toast.LENGTH_SHORT).show();
                            check=false;
                        }
                    }catch (Exception e){
                        Toast.makeText(context,"Giá wifi phải là số nguyên",Toast.LENGTH_SHORT).show();
                        check=false;
                    }

                }

                //--------
//                String trangthai= "chưa thuê";
//                edt_themgianuoc.setText(trangthai);

                if (check == true) {

                    if (phongDAO.insertPhong(soPhong,giaphong,giadien,gianuoc,giawifi,1)){
                        Toast.makeText(context, "thêm mới thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        lists.clear();
                        lists.addAll(phongDAO.getAll());
                        adapterPhong.notifyDataSetChanged();
                    }else {
                        Toast.makeText(context, "thêm mới k thành công", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        btn_huyphong
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }
    private void checkData(){
        hopDongDAO=new HopDongDAO(getContext());
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phong, container, false);
    }

}