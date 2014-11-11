package com.himumsaiddad.niggle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class DataBaseHelper extends SQLiteOpenHelper{
 
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.himumsaiddad.niggle/databases/";
 
    private static String DB_NAME = "NiggleDB.sqlite";
    
    private static String DB_TABLE_NIGGLE = "ZNIGGLE";
    
    private static String DB_TABLE_QUESTION = "ZQUESTION";
    private static String DB_TABLE_ANSWER = "ZANSWER";
    private static String DB_TABLE_QUOTE = "ZQUOTE"; 
    public SQLiteDatabase myDataBase; 
 
    private final Context myContext;
    
    
    static private DataBaseHelper _instance = null;
    
    static public DataBaseHelper instance(Context context)
    {
    	if(_instance == null)
    	{
    		return new DataBaseHelper(context); 
    	}
    	
    	return _instance; 
    }
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
	
	public Cursor ReadAnswersByNiggle(String sId)
	{
		String sql_stm = "SELECT A.ZTEXT AS ANSWER, Q.ZTEXT " +
						 "AS QUESTION FROM ZANSWER AS A, " +
						 "ZNIGGLE AS N, ZQUESTION AS Q	" +
						 "WHERE A.ZNIGGLE = N._id AND " +
						 "A.ZQUESTION = Q.ZQUESTIONID AND " +
						 "N._id = "+sId +" ORDER BY A.ZANSWERID ASC";
		
		return myDataBase.rawQuery(sql_stm, null);
	}
	
	public Cursor ReadNiggles()
	{
		return myDataBase.query(DB_TABLE_NIGGLE,
				
				new String[] { "_id", "ZWHATNIGGLEDME", "ZCREATIONDATE", "ZWHATINOWKNOW", "ZWHATIWILLNOWDO" }, 
				null, null, null, null, "_id desc", null);
	
	}
	
	public Cursor ReadNiggleLastId()
	{	
		/*return myDataBase.query(DB_TABLE_NIGGLE, 
				new String[] {"_id"}, "_id" + "=" + "MAX(_id)", null, null, null, null, null);
*/
		return myDataBase.rawQuery( "SELECT MAX(_id) FROM " + DB_TABLE_NIGGLE
						, null);
		
		
	}
	
	public long CountTableEntries(String table_name)
	{
		
		return DatabaseUtils.queryNumEntries(myDataBase,table_name);     
		
	}
	
	public Cursor ReadQuestionsLinked(int qid)
	{
		return myDataBase.query(DB_TABLE_QUESTION,
				
				new String[] { "ZQUESTIONID", "ZLINKEDTO", "ZTEXT"}, 
				"ZLINKEDTO="+qid, null, null, null, null, null);
	
	}
	
	public Cursor ReadQuestion(long qid)
	{
		return myDataBase.query(DB_TABLE_QUESTION,
				
				new String[] { "ZQUESTIONID", "ZLINKEDTO", "ZTEXT"}, 
				"ZQUESTIONID="+qid, null, null, null, null, null);
	
	}
	
	public Cursor ReadQuote(long qid)
	{
		return myDataBase.query(DB_TABLE_QUOTE,
				
				new String[] { "ZTEXT"}, 
				"ZQUOTEID="+qid, null, null, null, null, null);
	
	}

	public void DeleteNiggles(String sId)
	{
		String dString = "DELETE FROM "+DB_TABLE_NIGGLE+" WHERE _id="+sId;
		myDataBase.execSQL(dString);
	
	}
	
	public void InsertNiggle(String[] v)
	{
//		String[] s = Utils.escapequotes(v);
		
		//this.myDataBase.insert(DB_TABLE_NIGGLE, 1, v[0]);
		String query = "INSERT INTO "+DB_TABLE_NIGGLE+"(ZWHATNIGGLEDME, ZCREATIONDATE, ZWHATINOWKNOW, ZWHATIWILLNOWDO) VALUES(?,?,?,?)";
	
		SQLiteStatement stmt= myDataBase.compileStatement(query);
		stmt.bindString(1, v[0]);
		stmt.bindString(2, v[1]);
		stmt.bindString(3, v[2]);
		stmt.bindString(4, v[3]);
		stmt.executeInsert();

	}
	
	public void InsertAnswer(String[] v)
	{
//		String[] s = Utils.escapequotes(v);
		//this.myDataBase.insert(DB_TABLE_NIGGLE, 1, v[0]);
		
		String query = "INSERT INTO "+DB_TABLE_ANSWER+"(ZNIGGLE, ZQUESTION, ZCREATIONDATE, ZTEXT) VALUES(?,?,?,?)";
		
		SQLiteStatement stmt= myDataBase.compileStatement(query);
		stmt.bindLong(1, (Long.parseLong(v[0])));
		stmt.bindLong(2, (Long.parseLong(v[1])));
		stmt.bindString(3, v[2]);
		stmt.bindString(4, v[3]);
		stmt.executeInsert();

	
	}

 
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.

}