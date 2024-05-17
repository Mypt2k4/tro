package Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


import Adapter.HoaDonAdapter;
import HopDong.HoaDonDao;
import Model.HoaDon;
import longvtph16016.poly.appquanlyphongtro.R;
import longvtph16016.poly.appquanlyphongtro.interfaceDeleteClickdistioner;


public class HoaDonFragment extends Fragment implements interfaceDeleteClickdistioner {

    EditText  Ed_NgayTao_HDon, Ed_NhapSoDien_HDon, Ed_NhapSoNuoc_HDon, Ed_ChiPhiKhac_HDon,Ed_TongTien_HDon,Ed_GhiChu_HDon,ed_tenhoaDon;
    Spinner  Ed_ChonSoPhong;
    Button Btn_huy_HDon, Btn_them_HDon;
    FloatingActionButton fab;
    ListView rcv_hoadon;
    ImageView image_ngay;

    HoaDonAdapter HoaDonAdapter;
    private HoaDonDao hoaDonDao;
    private List<HoaDon> list = new ArrayList<>();
    Context context;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcv_hoadon = view.findViewById(R.id.rec_HoaDon);
        context = this.getActivity();
        hoaDonDao = new HoaDonDao(context);
        list = hoaDonDao.getAll();
        HoaDonAdapter = new HoaDonAdapter(context);
        HoaDonAdapter.setData(list);
        rcv_hoadon.setAdapter(HoaDonAdapter);
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
                HoaDonAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                HoaDonAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hoa_don, container, false);
    }


    @Override
    public void OnClickDelete(int index) {
        deletedialog(index);
    }

    public void deletedialog(int index) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("bạn có chắc chắn muốn xóa không?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (hoaDonDao.deleteHoaDon(list.get(index)) > 0) {
                    list.remove(index);
                    HoaDonAdapter.setData(list);
                    Toast.makeText(context, "xóa thành công",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "xóa không thành công",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}
