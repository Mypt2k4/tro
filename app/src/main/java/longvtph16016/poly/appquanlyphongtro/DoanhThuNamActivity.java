package longvtph16016.poly.appquanlyphongtro;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Adapter.HoaDonAdapter;
import HopDong.HoaDonDao;
import Model.HoaDon;

public class DoanhThuNamActivity extends AppCompatActivity {
    private List<String> listthang,listnam;
    Button btn_xemDTN;
    Spinner namDoanThu;
    String nams;
    TextView textViewDoanhThu,txtslHD;
    ListView listView;
    List<HoaDon> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doanh_thu_nam);
        namDoanThu=findViewById(R.id.id_spinner_namDt);
        btn_xemDTN = findViewById(R.id.btn_xemDTN);
        txtslHD=findViewById(R.id.txtsl);
        listnam = new ArrayList<>();
        for (int i=2015;i<2100;i++){
            listnam.add(i+"");
        }
        textViewDoanhThu=findViewById(R.id.tvdoanhthu);

        listView=findViewById(R.id.listDoanhThunam);
        namDoanThu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nams=listnam.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter spinnerAdapter2 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listnam);
        namDoanThu.setAdapter(spinnerAdapter2);


        btn_xemDTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                String daunam=nams+"-01/01";
                String cuoinam=nams+"12/31";
                int doanhthu=0;
                HoaDonDao donDao=new HoaDonDao(getApplicationContext());
                list =donDao.gethoadonByNgay(daunam,cuoinam);
                if (list.size()==0){
                    textViewDoanhThu.setText("Doanh Thu Năm "+nams+": "+doanhthu+" đ");
                    txtslHD.setVisibility(View.VISIBLE);
                }
                else {
                    for(int i=0; i<list.size();i++){
                        doanhthu=doanhthu+list.get(i).getTong();

                    }
                    textViewDoanhThu.setText("Doanh Thu Năm "+nams+": "+doanhthu+" đ");
                    txtslHD.setVisibility(View.GONE);
                }


                HoaDonAdapter adapter=new HoaDonAdapter(DoanhThuNamActivity.this);
                adapter.setData(list);
                listView.setAdapter(adapter);
            }
        });

    }
}