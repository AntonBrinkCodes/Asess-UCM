package com.example.asessucm.utils;

import android.content.Context;
import android.util.Log;


import com.example.asessucm.Model.ResultList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import kotlin.internal.ProgressionUtilKt;

public class FileHandler {

    static FileOutputStream fos;
    static ObjectOutputStream oos;
    FileInputStream fis; //hihi
    ObjectInputStream ois;
    Context context;
    static String fileName = "results.ser";
    private static final String filehandler_tag = "Filehandler";

    /**
     * Saves results to a private .ser file called "results.ser".
     * @param resultLists List of results to be saved.
     */
    public static void saveResults(List<ResultList> resultLists, Context context){
        try{
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(resultLists);
            oos.close();
            Log.i(filehandler_tag, "saved file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the saved file from saveResults containing a ResultList.
     * @return a ResultList.
     */
    public List<ResultList> loadResults(){
        try {
            fis = context.openFileInput(fileName);
            ois = new ObjectInputStream(fis);
            Log.i(filehandler_tag,"Loaded file");
            List<ResultList> tmp = (List<ResultList>)ois.readObject();
            ois.close();
            return tmp;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        Log.i(filehandler_tag, "Failed to load file uh oh");
        /* If we fail to load from file for some reason, like the first time the app
        is opened after install */
        return null;
    }
}

