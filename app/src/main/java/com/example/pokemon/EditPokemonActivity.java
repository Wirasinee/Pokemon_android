package com.example.pokemon;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokemon.DB.PokemonDbHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class EditPokemonActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mPokemonNameEditText, mPokemonTitleEditText;
    private ImageView mPokemonImegeView;
    private Button mSaveButton;
    private File mSelectedPictureFile;
    private TextView mStatusTextView;
    private static final String TAG = AddPokemonActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pokemon);

        mPokemonNameEditText = findViewById(R.id.pokemon_name_edit_text);
        mPokemonTitleEditText = findViewById(R.id.pokemon_title_edit_text);
        mPokemonImegeView = findViewById(R.id.pokemon_image_view);
        mSaveButton = findViewById(R.id.save_button);
        mStatusTextView = findViewById(R.id.status_textView);
        mStatusTextView.setText("รูปภาพเดิม");
        //เมือกดรูป
        //กำหนดให้ Activity เป็น Listener ของ Image View
        mPokemonImegeView.setOnClickListener(this);//ให้คลาสหลักเป็นลิสเซอเนอ
        //กำหนดให้ Activity เป็น Listener ของ Save Button
        mSaveButton.setOnClickListener(this);//เมือกดปุ่มก็จะวิ่งไปที่เมธอดonClick

        Intent intent = getIntent();//getอินเทนที่ส่งมาจากหน้าก่อนหน้านี
        String pokemonName = intent.getStringExtra("pokemonName");
        String pokemonTitle = intent.getStringExtra("pokemonTitle");
        String pokemonPicture = intent.getStringExtra("pokemonPicture");

        mPokemonNameEditText.setText(pokemonName);
        mPokemonTitleEditText.setText(pokemonTitle);


        AssetManager am = getAssets();
        try {
            InputStream stream = am.open(pokemonPicture); //เปิดไฟล์โดยใช้AssetManager
            Drawable drawable = Drawable.createFromStream(stream, null);//แปลงInputStreamเป็น Drawable
            //สามารถเอาไปsetให้กับรูปภาพได้แล้ว
            mPokemonImegeView.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
            //(10)ถ้าเปิดไฟล์ไม่เจอมันอ้างเป็นไฟล์ที่ผู้ใช้แอดมาจึง
            File pictureFile = new File(getFilesDir(), pokemonPicture);
            Drawable drawable = Drawable.createFromPath(pictureFile.getAbsolutePath());
            mPokemonImegeView.setImageDrawable(drawable);
        }


    }

    @Override
    public void onClick(View view) {//ส่งวิว มาบอกว่าพารามิเตอไหนที่ส่งค่ามา ว่าเป็นidของภาพหรือidของปุ่ม
        int viewId=view.getId();//ประกาศintเพราะidเป็นเลขจำนวนเต็ม

        if(viewId==R.id.pokemon_image_view){//ถ้าตัวที่ผู้ใช้กดคือรูป
//ทำการเพิ่มไลาบารีhttps://github.com/jkwiecien/EasyImage
            //แล้วใช้คำสั่งจากไลบารี
            EasyImage.openChooserWithGallery(EditPokemonActivity.this, "ถ่ายรูปหรือเลือกรูปภาพที่ต้องการ", 0);//เรียกใช้ให้ผู้ใช้เลือกภาพหรือถ่ายภาพ
//ไป(3)
            mStatusTextView.setText("");
        }else if(viewId==R.id.save_button){
            //(4)ย้ายภาพที่ผู้ใช้เอามาลงไปไว้ที่private เผือกรณีผู้ใช้ล้างแคส
            if(mSelectedPictureFile==null){
                /*Toast.makeText(
                        getApplicationContext(),
                        "คุณยังไม่ได้เลือกรูปภาพ",
                        Toast.LENGTH_LONG
                ).show();
                return;*/
            }
            else { 
                File privateDir = getApplicationContext().getFilesDir();
                File datFile = new File(privateDir, mSelectedPictureFile.getName());
                try {
                    copyFile(mSelectedPictureFile, datFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error copying picture file.");
                    return;
                }
            }
            editDataToDb();//(5) ออกข้อสอบ
            setResult(RESULT_OK);//(8)

            //(9)ถ้ามีการส่งข้อมูลอื่นๆจากปลายทางไปต้นทางด้วย
            /*Intent intent = new Intent();
            intent.putExtra(//อะไรสักอย่าง);
            setResult(RESULT_OK,intent);
            */
            finish();
        }


}
    //(5)
    private void editDataToDb() {
        Intent intent = getIntent();//getอินเทนที่ส่งมาจากหน้าก่อนหน้านี
        int id = intent.getIntExtra("pokemonID",0);
        String pokemonName = mPokemonNameEditText.getText().toString();
        String pokemonTitle = mPokemonTitleEditText.getText().toString();
        String pokemonPicture = intent.getStringExtra("pokemonPicture");
        //String pictureFileName=mSelectedPictureFile.getName();//ชื่อไฟล์รูปที่ผู้ใช้ใส่เข้ามา
        String pictureFileName;
        if(mSelectedPictureFile==null){
            pictureFileName=pokemonPicture;
        }else{
            pictureFileName=mSelectedPictureFile.getName();
        }


        //saveลงDBโดย
        ContentValues cv = new ContentValues();
        cv.put(PokemonDbHelper.COL_NAME,pokemonName);
        cv.put(PokemonDbHelper.COL_TITLE,pokemonTitle);
        cv.put(PokemonDbHelper.COL_PICTURE,pictureFileName);
        PokemonDbHelper dbHelper = new PokemonDbHelper(this);
        SQLiteDatabase mDb= dbHelper.getReadableDatabase();//รีเทินตัวฐานข้อมูล เป็นตัวแทนของฐานข้อมูล ทำให้เราสามารถเล่นกับdbได้แล้ว
        mDb.update(
                PokemonDbHelper.TABLE_NAME,//ชื่อตาราง
                cv,//ข้อมูลที่จะแก้
                PokemonDbHelper.COL_ID+"=?",
                new String[]{String.valueOf(id)}
        );
        //อินเซิสเข้าDB
        //สร้างobjของDBHelper

    }

    //(3)อ่านผลลัพคือรูป
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//onActivityResult ใช้ส่งผลลัพจากปลายทางมาต้นทาง อารมกล้องคืิหน้าปลายทาง ส่วนโค้ดเป็นหน้าต้นทาง
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {//ถ้าเกิดเลือกหรือถ่ายภาพ errorจะตกลงตรงนี้
                //Some error handling
                Log.e(TAG,"Error chocsing picture file:"+e.getMessage());
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {//ถ้าไม่เกิดerror imagesFilesจะเห็นว่าเราสามารถเลือกได้หลายภาพด้วย
                //Handle the images
                mSelectedPictureFile=imagesFiles.get(0);//แต่รูปภาพเราสนใจแค่รูปเดียว
                Drawable drawable = Drawable.createFromPath(mSelectedPictureFile.getAbsolutePath());
                mPokemonImegeView.setImageDrawable(drawable);
                Log.i(TAG,mSelectedPictureFile.getAbsolutePath());
            }


        });
    }

public static void copyFile(File src, File dst) throws IOException {
        FileInputStream inputStream = new FileInputStream(src);
        FileOutputStream outputStream = new FileOutputStream(dst);
        byte[] buffer = new byte[1024];

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();
    }

}
