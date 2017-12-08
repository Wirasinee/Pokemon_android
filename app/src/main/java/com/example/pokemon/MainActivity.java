package com.example.pokemon;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pokemon.DB.PokemonDbHelper;
import com.example.pokemon.adapter.PokemonListAdapter;
import com.example.pokemon.model.PokemonItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //<1>
    private PokemonDbHelper mHelper;//เข้าถึงdb
    private SQLiteDatabase mDb;//ตัวอ้างอิงdb
    private ArrayList<PokemonItem> mPokemonItemsList = new ArrayList<>();
    private PokemonListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //<2>อ้างอิงdb
        mHelper = new PokemonDbHelper(this);
        mDb = mHelper.getReadableDatabase();

        //จะคิวรีข้อมูล ทุกแถว ทุกคอลัม
        loadDataFromDb();
//<6>
        mAdapter = new PokemonListAdapter(
                this,
                R.layout.item,//<4>layout->new->layout reso->จะได้item.xml  <5>สร้างแพคเกจ adapter -> สร้างPhoneListAdapter.class
                mPokemonItemsList
        );
//end <6>
        //ไปเพิ่มเมธอท toString ในPhoneItem
        //อ้างอิงถึงลิสวิว
        ListView iv = findViewById(R.id.list_View);
        iv.setAdapter(mAdapter);

        iv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PokemonItem item = mPokemonItemsList.get(position);
                int pokemonId=item.id;
                Intent intent = new Intent(MainActivity.this,ShowPokemonActivity.class);
                intent.putExtra("pokemonName", item.getName());
                intent.putExtra("pokemonTitle", item.getTitle());
                intent.putExtra("pokemonPicture", item.getPicture());
                startActivity(intent);


            }
        });

//(11)มีการลบได้เมือกดค้างที่ไอเทม
        iv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int position, long l) {//positionคือตัวที่เราเลือกเช่นเลือกแจ่งเหตุด่วนเหตุร้ายคือ0
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                String[] items = new String[]{"แก้ไขข้อมูล","ลบข้อมูล"};//มีลิสมาโชว์ให้ผู้ใช้เลือกได้
                dialog.setItems(items, new DialogInterface.OnClickListener() {//โดยจะทำงานเมือผู้ใช้เลือกitemsในนั้น
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){//กรณีแก้ไขข้อมูล
                            //อ้างอิงตัวmodelว่าแถวไหนจะแก้ไข
                            PokemonItem item = mPokemonItemsList.get(position);
                            Intent intent = new Intent(MainActivity.this,EditPokemonActivity.class);
                            intent.putExtra("pokemonID", item.getId());
                            intent.putExtra("pokemonName", item.getName());
                            intent.putExtra("pokemonTitle", item.getTitle());
                            intent.putExtra("pokemonPicture", item.getPicture());
                            startActivityForResult(intent, 002);

                            /*int phoneId=item.id;
                            ContentValues cv = new ContentValues();
                            cv.put(PokemonDbHelper.COL_TITLE,"12345");//สมมุติจะแก้ไขแค่เบอร์โดยเลือกitemไหนก็จะเปนเบอร?1234
                            mDb.update(
                                    PokemonDbHelper.TABLE_NAME,//ชื่อตาราง
                                    cv,//ข้อมูลที่จะแก้
                                    PokemonDbHelper.COL_ID+"=?",
                                    new String[]{String.valueOf(phoneId)}
                            );

                            loadDataFromDb();//โหลดDB
                            mAdapter.notifyDataSetChanged();//บอกให้อแดปเตอรู้*/
                        }else if(i==1){//กรณีเลือกลบข้อมูล
                            PokemonItem item = mPokemonItemsList.get(position);
                            int pokemonId=item.id;
                            //String[] args = new String[]{String.valueOf(phoneId)};
                            mDb.delete(
                                    PokemonDbHelper.TABLE_NAME,
                                    PokemonDbHelper.COL_ID+"=?",//คอลัมเงือนไขตัวที่จะลบ    _id=? AND picture=?"
                                    new String[]{String.valueOf(pokemonId)}//?คือphoneId ก็คือargsนั้นหละ  (phoneID,"number0001.jpg")
                            );
                            loadDataFromDb();//โหลดDB
                            mAdapter.notifyDataSetChanged();//บอกให้อแดปเตอรู้
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });//end (11)มีการลบได้เมือกดค้างที่ไอเทม

        //(2)เมือกดปุ่ม+จะไปอีกหน้าจอนึง
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddPokemonActivity.class);

                startActivityForResult(intent, 001);
            }
        });
        //end (2)

    }
    //(7)เข้ามาเมือหน้าปลายทางมรการส่งค่ากลับมา
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==001){
            if(resultCode==RESULT_OK){
                loadDataFromDb();//โหลดข้อมูลDB(คิวรีข้อมูลจากDBมา)
                mAdapter.notifyDataSetChanged();//บอกให้อแดปเตอรู้ว่าข้อมูลเปลียนไปแล้วนะ
            }
            //ไป(8)ที่ปลายทาง
        }
        if(requestCode==002){
            if(resultCode==RESULT_OK){
                loadDataFromDb();//โหลดข้อมูลDB(คิวรีข้อมูลจากDBมา)
                mAdapter.notifyDataSetChanged();//บอกให้อแดปเตอรู้ว่าข้อมูลเปลียนไปแล้วนะ
            }
            //ไป(8)ที่ปลายทาง
        }
    }

    private void loadDataFromDb() {
        Cursor cursor =mDb.query(
                //CTRT+P เอาแบบ3
                PokemonDbHelper.TABLE_NAME,
                null,//เอามาทุกคอลัม
                null,//"category=1"  คิวรีเแพาะที่มีค่าแคททากอรีเป็น1เท่ารั้ร
                null,
                null,
                null,
                null
        );

        mPokemonItemsList.clear();//เคลียข้อมูลเก่าทิ้ง เผือไว้กรณีผู้ใช้แอดข้อมูลเพิ่มมา
//วนลูปเอาข้อมูลออกมา
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(PokemonDbHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(PokemonDbHelper.COL_TITLE));  //getมาแต่ละคอลัมของแถวนั้นๆ หรือcursor.getString(1); ช่อง1ตือtitle
            String name = cursor.getString(cursor.getColumnIndex(PokemonDbHelper.COL_NAME));  //getมาแต่ละคอลัมของแถวนั้นๆ หรือcursor.getString(1); ช่อง1ตือtitle
            String picture = cursor.getString(cursor.getColumnIndex(PokemonDbHelper.COL_PICTURE));  //getมาแต่ละคอลัมของแถวนั้นๆ หรือcursor.getString(1); ช่อง1ตือtitle

            //สร้างโมเดลobj โดยผ่านคอนสตักจอPhoneItem ที่สร้างไว้
            PokemonItem item = new PokemonItem(id,name,title,picture);
            mPokemonItemsList.add(item);//ข้อมูลขากdbมาอยู่ในนี้หมดแล้ว
        }




    }


}
