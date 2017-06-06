package br.ufc.topresente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Thiago on 20/05/2017.
 */
public class PresencaDAO extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "ToPresente.db";
    public static final int DATABASE_VERSION = 1;


    public PresencaDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public PresencaDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table presencas (");
        sql.append("id integer primary key autoincrement,");
        sql.append("id_usuario integer,");
        sql.append("codaula text unique,");
        sql.append("date text,");
        sql.append("online boolean)");
        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists presencas");
        onCreate(db);
    }

    public void create(Presenca presenca) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id_usuario", presenca.getIdUsuario());
        contentValues.put("codaula", presenca.getCodAula());
        contentValues.put("date", presenca.getHora());
        contentValues.put("online", 0);

        long id = db.insert("presencas", null, contentValues);
        Log.v("SQLite", "create id = " + id);
    }

    /*
    public Presenca retrieve(Integer id) {
        String[] fieldValues = new String[1];
        fieldValues[0] = Integer.toString(id);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from notes where id = ?", fieldValues);
        Note note = null;
        if (result != null && result.getCount() > 0) {
            note = new Note();
            note.setId(result.getInt(0));
            note.setTitle(result.getString(1));
            note.setContent(result.getString(2));

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(result.getLong(3));

            note.setDate(gc);
        }
        return note;
    }
    */

    public void updateOnline() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("online", 1);

        String[] fieldValues = new String[1];
        fieldValues[0] = Integer.toString(0);

        db.update("presencas", contentValues, " online = ? ", fieldValues);
    }

    public List<Presenca> listOffline() {
        List<Presenca> presencas = new ArrayList<Presenca>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from presencas where online == 0 order by id", null);
        if (result != null && result.getCount() > 0) {
            result.moveToFirst();
            while (result.isAfterLast() == false) {
                Presenca presenca = new Presenca();
                presenca.setIdUsuario(result.getInt(1));
                presenca.setCodAula(result.getString(2));
                presenca.setHora(result.getString(3));
                presencas.add(presenca);
                result.moveToNext();
            }
        }
        return presencas;
    }

}
