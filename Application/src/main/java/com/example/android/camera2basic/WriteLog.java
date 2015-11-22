package com.example.android.camera2basic;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by XiaowenJiang on 11/19/15.
 */
public class WriteLog {
    private static final String TAG = "Writelog";
    File file;
    Boolean stop = false;
    RandomAccessFile randomAccessFile;

    WriteLog() throws IOException {
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator+"loghehe.txt");
        if(!file.exists())
        {
            Log.d(TAG,"not exist");
            file.createNewFile();
        }
        randomAccessFile = new RandomAccessFile(file,"rw");
        randomAccessFile.seek(file.length());
        randomAccessFile.writeChars("\n");
        randomAccessFile.close();

    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }

    public Boolean getStop() {
        return stop;
    }

    public void writefile(String msg) throws IOException {
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator+"loghehe.txt");
      if(!file.exists())
      {
          Log.d(TAG,"not exist");
          file.createNewFile();
      }
      randomAccessFile = new RandomAccessFile(file,"rw");
      randomAccessFile.seek(file.length());
      randomAccessFile.writeChars(msg);
      randomAccessFile.close();
  }
    public boolean isFileExist()
    {
        return (file.exists());
    }

}
