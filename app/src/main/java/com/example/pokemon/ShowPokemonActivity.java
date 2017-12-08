package com.example.pokemon;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokemon.DB.PokemonDbHelper;
import com.example.pokemon.model.PokemonItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ShowPokemonActivity extends AppCompatActivity {
    TextView mNameTextView,mTitleTextView,mDetailTextView;
    ImageView mImageView;
    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pokemon);
        mNameTextView = findViewById(R.id.name_textView);
        mTitleTextView = findViewById(R.id.title_textView);
        mDetailTextView = findViewById(R.id.detail_textView);
        mImageView = findViewById(R.id.imageView);

        Intent intent = getIntent();//getอินเทนที่ส่งมาจากหน้าก่อนหน้านี

        String pokemonName = intent.getStringExtra("pokemonName");
        String pokemonTitle = intent.getStringExtra("pokemonTitle");
        String pokemonPicture = intent.getStringExtra("pokemonPicture");

        String pictureFileName = pokemonPicture;
        AssetManager am = getAssets();
        try {
            InputStream stream = am.open(pictureFileName); //เปิดไฟล์โดยใช้AssetManager
            Drawable drawable = Drawable.createFromStream(stream, null);//แปลงInputStreamเป็น Drawable
            //สามารถเอาไปsetให้กับรูปภาพได้แล้ว
            mImageView.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
            //(10)ถ้าเปิดไฟล์ไม่เจอมันอ้างเป็นไฟล์ที่ผู้ใช้แอดมาจึง
            File pictureFile = new File(getFilesDir(), pictureFileName);
            Drawable drawable = Drawable.createFromPath(pictureFile.getAbsolutePath());
            mImageView.setImageDrawable(drawable);
        }


        //setลงในวิวต่างๆ(3) getString(R.string.details_cat)
        mNameTextView.setText(pokemonName);
        mTitleTextView.setText(pokemonTitle);




    }
}
