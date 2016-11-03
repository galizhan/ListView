package kz.edu.sdu.galix.phonebookadvanced;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CreateActivity extends AppCompatActivity {
    DBHelper dbh;
    SQLiteDatabase db;
    EditText ed,ed2;
    String action;
    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        dbh = new DBHelper(this);
        db = dbh.getWritableDatabase();
        ed = (EditText) findViewById(R.id.ed1);
        ed2 = (EditText) findViewById(R.id.ed2);
        Intent intent = getIntent();
        action = intent.getStringExtra("action");

        if(action.equals("update")){
            id = intent.getLongExtra("id",0);
            Log.d("id2",""+id);
            Cursor c = db.query("contacts", null, "_id=?",
                    new String[]{id+""}, null, null, null);
            if(c.moveToFirst()){
                ed.setText(c.getString(c.getColumnIndex("name")));
                ed2.setText(c.getString(c.getColumnIndex("number")));
            }

        }
    }
    public void save(View v){
        ContentValues cv = new ContentValues();
                cv.put("name",ed.getText().toString());
                cv.put("number",ed2.getText().toString());
                if(action.equals("update")){
                    db.update("contacts", cv, "_id="+id, null);
                }
                else{
                    db.insert("contacts",null,cv);
                }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbh.close();
    }
}
