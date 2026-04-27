package com.apps.ayurvedcareproject;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "AyurvedaDB";
    private static final int DB_VERSION = 2; // Incremented version to add timestamp column

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users table
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "password TEXT)");

        // Health data table with timestamp
        db.execSQL("CREATE TABLE health_data (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "problem TEXT," +
                "solution TEXT," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // SQLite does not allow adding a column with CURRENT_TIMESTAMP default via ALTER TABLE.
            // We'll add it without a default or with a constant default, and then let new entries use the default from onCreate.
            db.execSQL("ALTER TABLE health_data ADD COLUMN timestamp DATETIME");
            // Optionally update existing rows to current time
            db.execSQL("UPDATE health_data SET timestamp = CURRENT_TIMESTAMP WHERE timestamp IS NULL");
        }
    }

    // Register User
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("INSERT INTO users (username, password) VALUES (?, ?)",
                    new Object[]{username, password});
            return true;
        } catch (Exception e) {
            return false; // username already exists
        }
    }

    // Login Check
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE username=? AND password=?",
                new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Save Health Data
    public void saveHealthData(String username, String problem, String solution) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO health_data (username, problem, solution) VALUES (?, ?, ?)",
                new Object[]{username, problem, solution});
    }

    // Get health data with timestamp
    public Cursor getHistory(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT problem, solution, timestamp FROM health_data WHERE username=? ORDER BY timestamp DESC",
                new String[]{username});
    }
}
