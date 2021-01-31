package com.example.bounce.database;


public class constraint {


    public static final String DB_NAME="DATABASE";
    public static final int DB_VERSION=1;

    public static final String TBL_NAME="TBL_REG";

    public static final String COL_ID="id";
    public static final String COL_LAT="lat";
    public static final String COL_LANG="lang";
    public static final String COL_TIME="time";




    public static final String TBL_QUERY="create table "+TBL_NAME+"("+COL_ID+"" +" integer primary key autoincrement,"+COL_LAT+" text,"+COL_LANG+" text,"+COL_TIME+" text)";




}
