package kz.edu.sdu.galix.phonebookadvanced;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    Button btn;
    DBHelper dbh;
    SQLiteDatabase db;
    SimpleAdapter sa;
    ArrayList<String> data;
    Long changableId;
    final int MENU_UPDATE = 1;
    final int MENU_DELETE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        btn = (Button) findViewById(R.id.btn_create);
        dbh = new DBHelper(this);
        db = dbh.getWritableDatabase();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) findViewById(R.id.tv1);
                Log.d("id",id+"");
            }
        });
        registerForContextMenu(lv);


    }



    @Override
    protected void onResume() {
        super.onResume();
        refresh();
        sa.notifyDataSetChanged();
    }

    public void refresh(){
        ArrayList<Map<String, String>> dataSA =  new ArrayList<>();
        Cursor c = db.query("contacts", null, null, null, null, null, "name");
        if(c.moveToFirst()){
            do{
                long id = c.getLong(c.getColumnIndex("_id"));
                String name = c.getString(c.getColumnIndex("name"));
                String phone = c.getString(c.getColumnIndex("number"));
                Map<String, String> map = new HashMap<>();
                map.put("name",id+". "+name);
                map.put("number",phone);
                dataSA.add(map);
                Log.d("name",name+" "+phone);
            }while(c.moveToNext());
        }
        c.close();
        sa = new SimpleAdapter(this, dataSA, R.layout.llview,
                new String[]{"name", "number"}, new int[]{R.id.tv1, R.id.tv2});
        lv.setAdapter(sa);
    }
    public void create(View v){
        Intent i = new Intent(this,CreateActivity.class);
        i.putExtra("action","insert");
        startActivity(i);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ListView ll = (ListView)v;

        String contactStr = ((TextView)ll.findViewById(R.id.tv1)).getText().toString();
        changableId = Long.parseLong(contactStr.split("\\.")[0]);
        menu.add(0, MENU_UPDATE, 0, "Update");
        menu.add(0, MENU_DELETE, 0, "Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        TextView tv1 = (TextView)info.targetView.findViewById(R.id.tv1);
        switch (item.getItemId()){
            case MENU_UPDATE:
                Intent i = new Intent(this, CreateActivity.class);
                i.putExtra("action", "update");
                i.putExtra("id", changableId);
                startActivity(i);
                break;
            case MENU_DELETE:
                db.delete("contacts", "_id=?", new String[]{changableId+""});
                Log.d("id_info",""+changableId);
                refresh();
                sa.notifyDataSetChanged();
                break;
        }

        return super.onContextItemSelected(item);
    }

}
