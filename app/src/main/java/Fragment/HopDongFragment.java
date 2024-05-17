package Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Adapter.HopDongAdapter;
import HopDong.HopDongDAO;
import Model.HopDong;
import longvtph16016.poly.appquanlyphongtro.R;

public class HopDongFragment extends Fragment {
    HopDongAdapter hopDong_adapter;
    private HopDongDAO hopDongDAO;
    ListView listViewHopDong;
//    private Button btnthemHopDong;
//    EditText edtNgayBatDau;
//    EditText edtNgayKetThuc;
//    EditText edtSoLuongXe;
//    EditText edtTienCoc;


    FloatingActionButton fab;
    private List<HopDong> list = new ArrayList<>();

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
                hopDong_adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                hopDong_adapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hop_dong, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        checkData();
        hopDongDAO=new HopDongDAO(getContext());
          list=hopDongDAO.getAll();

        listViewHopDong=view.findViewById(R.id.rec_HopDong);
        hopDong_adapter= new HopDongAdapter(getContext(),list);
        listViewHopDong.setAdapter(hopDong_adapter);

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


}
