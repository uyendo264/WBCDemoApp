package com.singalarity.wbcdemo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_NAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String TABLE_NAME = "mobileUsers";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        String create_mobileUsers_table = String.format("CREATE TABLE %s(%s TEXT PRIMARY KEY, %s TEXT)", new Object[]{TABLE_NAME, KEY_NAME, KEY_PASSWORD});
        Log.d("SQLite", "Create table " + create_mobileUsers_table);
        db.execSQL(create_mobileUsers_table);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", new Object[]{TABLE_NAME}));
        Log.d("SQLite", "Drop table success");
        onCreate(db);
    }

    public void addUsers(UserInfo_extend user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getUid());
        values.put(KEY_PASSWORD, user.getHashPassword());
        db.insert(TABLE_NAME, (String) null, values);
        db.close();
    }

    public UserInfo_extend getUserInfo(String username) {
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, (String[]) null, "username = ?", new String[]{String.valueOf(username)}, (String) null, (String) null, (String) null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return new UserInfo_extend(cursor.getString(0), cursor.getString(1));
    }

    public List<UserInfo_extend> getAllUserInfo() {
        List<UserInfo_extend> userList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM mobileUsers;", (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            userList.add(new UserInfo_extend(cursor.getString(0), cursor.getString(1)));
            cursor.moveToNext();
        }
        Log.d("List", String.valueOf(userList.size()));
        return userList;
    }

    public void updateUsers(UserInfo_extend user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getUid());
        values.put(KEY_PASSWORD, user.getHashPassword());
        db.update(TABLE_NAME, values, "username = ?", new String[]{String.valueOf(user.getUid())});
        db.close();
    }

    public void deleteUsers(String uid) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "username = ?", new String[]{String.valueOf(uid)});
        Log.d("DatabaseHandler", "delete user");
        db.close();
    }

    public int getNumberUsers() {
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM mobileUsers;", (String[]) null);
        int count = cursor.getCount();
        Log.d("Database Handler", "Number of Users");
        cursor.close();
        return count;
    }
}