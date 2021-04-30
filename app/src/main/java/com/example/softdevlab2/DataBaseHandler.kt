package com.example.softdevlab2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.nfc.Tag
import android.util.Log
import kotlin.math.cos

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "InterbrandRating"

        private val TABLE_BRANDS = "Brands"

        private val COL_ID      = "_id"
        private val COL_NAME    = "name"
        private val COL_RANK    = "rank"
        private val COL_COST    = "cost"
        private val COL_CITY    = "city"
        private val COL_CHANGE  = "change"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BRANDS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT," +
                COL_RANK + " INTEGER," +
                COL_COST + " REAL," +
                COL_CITY + " TEXT," +
                COL_CHANGE + " INTEGER)";
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_BRANDS")
        onCreate(db)
    }
    /**
     * Function to insert data
     */
    fun addBrand(brand: InterbrandRating): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(COL_NAME, brand.name) // InterbrandRatingClass Name
        contentValues.put(COL_RANK, brand.rank) // InterbrandRatingClass Rank
        contentValues.put(COL_COST, brand.cost) // InterbrandRatingClass Cost
        contentValues.put(COL_CITY, brand.city) // InterbrandRatingClass City
        contentValues.put(COL_CHANGE, brand.change) // InterbrandRatingClass Change

        // Inserting employee details using insert query.
        val success = db.insert(TABLE_BRANDS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    //Method to read the records from database in form of ArrayList
    fun viewBrands(): ArrayList<InterbrandRating> {

        val brandList: ArrayList<InterbrandRating> = ArrayList<InterbrandRating>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_BRANDS"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var rank:Int
        var cost:Double
        var city:String
        var change:Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                name = cursor.getString(cursor.getColumnIndex(COL_NAME))
                rank = cursor.getInt(cursor.getColumnIndex(COL_RANK))
                cost = cursor.getDouble(cursor.getColumnIndex(COL_COST))
                city = cursor.getString(cursor.getColumnIndex(COL_CITY))
                change = cursor.getInt(cursor.getColumnIndex(COL_CHANGE))


                val brand = InterbrandRating(id = id, name = name,
                        rank = rank, cost = cost, city = city, change = change)
                brandList.add(brand)

            } while (cursor.moveToNext())
        }
        return brandList
    }

    /**
     * Function to update record
     */
    fun updateBrand(brand: InterbrandRating): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, brand.name) // InterbrandRating Name
        contentValues.put(COL_RANK, brand.rank) // InterbrandRating Rank
        contentValues.put(COL_COST, brand.cost) // InterbrandRating Cost
        contentValues.put(COL_CITY, brand.city) // InterbrandRating City
        contentValues.put(COL_CHANGE, brand.change) // InterbrandRating Change


        // Updating Row
        val success = db.update(TABLE_BRANDS, contentValues, COL_ID + "=" + brand.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }

    /**
     * Function to delete record
     */
    fun deleteBrand(brand: InterbrandRating): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID, brand.id) // InterbrandRating id
        // Deleting Row
        val success = db.delete(TABLE_BRANDS, COL_ID + "=" + brand.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }


    //Method to read the records with change more than 50 from database in form of ArrayList
    fun viewBrandsChangeMore50(): ArrayList<InterbrandRating> {

        val brandList: ArrayList<InterbrandRating> = ArrayList<InterbrandRating>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_BRANDS WHERE $COL_CHANGE >= 50"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var rank:Int
        var cost:Double
        var city:String
        var change:Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                name = cursor.getString(cursor.getColumnIndex(COL_NAME))
                rank = cursor.getInt(cursor.getColumnIndex(COL_RANK))
                cost = cursor.getDouble(cursor.getColumnIndex(COL_COST))
                city = cursor.getString(cursor.getColumnIndex(COL_CITY))
                change = cursor.getInt(cursor.getColumnIndex(COL_CHANGE))


                val brand = InterbrandRating(id = id, name = name,
                        rank = rank, cost = cost, city = city, change = change)
                brandList.add(brand)

            } while (cursor.moveToNext())
        }
        return brandList
    }

    //Method to read the records with change more than 50 from database in form of ArrayList
    fun viewBrandsValueMore20(): Int {

        val brandList: ArrayList<InterbrandRating> = ArrayList<InterbrandRating>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT COUNT(*) FROM $TABLE_BRANDS WHERE $COL_COST > 20"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return -1
        }
        var res = -1

        if (cursor.moveToFirst()) {
            res = cursor.getInt(cursor.getColumnIndex("COUNT(*)"))
        }

        return res
    }


}