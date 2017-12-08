package com.example.pokemon.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Wirasinee on 08-Dec-17.
 */

public class PokemonDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pokemon.db";
    private static final int DATABASE_VERSION = 1;

    //ชื่อฟิล
    public static final String TABLE_NAME = "pokemon";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";//ชื่อโปเกมอน
    public static final String COL_TITLE = "title";//รายละเอียด
    public static final String COL_PICTURE = "picture";


    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT,"
            + COL_TITLE + " TEXT,"
            + COL_PICTURE + " TEXT)";

    //ALT+INS
    public PokemonDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //ถ้าฐานข้อมูลยังไม่มีก็จะมาทำตรงนี้
        db.execSQL(CREATE_TABLE); //ไปสร้างตาราง
        insertInitialData(db);//ใส่ข้อมูลลงตาราง
    }

    private void insertInitialData(SQLiteDatabase db) {//idไม่ต้องใส่เพราะเดียวandroidทำให้เอง
//ข้อมูล1
        ContentValues cv = new ContentValues();
        //putค่าต่างๆใส่ลงcv
        cv.put(COL_NAME,"ปิกาจู");
        cv.put(COL_TITLE,"สายฟ้า");//ใส่ฟิลไหน,ค่าที่ใส่
        cv.put(COL_PICTURE,"Pikachu.png");
        //app->New->Foder->Asset จะได้Fodel asset ไปหารูปมาใส่
        //เอาcv ใส่ลงฐานข้อมูล
        db.insert(TABLE_NAME,null,cv);//ชื่อตาราง,null,cv

//ข้อมูล2
        cv = new ContentValues();
        cv.put(COL_NAME,"ฟุจิกิดาเนะ");
        cv.put(COL_TITLE,"พืช");//ใส่ฟิลไหน,ค่าที่ใส่
        cv.put(COL_PICTURE,"Fushigidane.png");
        db.insert(TABLE_NAME,null,cv);

        //ข้อมูล2
        cv = new ContentValues();
        cv.put(COL_NAME,"พูริน");
        cv.put(COL_TITLE,"หลังจิต");//ใส่ฟิลไหน,ค่าที่ใส่
        cv.put(COL_PICTURE,"Purin.png");
        db.insert(TABLE_NAME,null,cv);
    }
//คลิก แพคเกจย่อย-> สร้างแพคเกจ model-> สร้าง javaclass PhoneItem [2]

    //ไม่ออกข้อสอบ  สมมุติออกแอพไปแล้ว ให้ผู้ใช้ใช้ไปแล้ว พอเราจะทำเวอชันถัดไปแต่เทเบิลไม่พอในเวอชันถัดไป จะมาใช้ตรงนี้
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);//ลบตารางออก
        onCreate(db);
        //แล้วไปปรับ เวอชัน
        //หรือลบแอพทิ้งแล้วรันใหม่ก็ได้
    }
}
