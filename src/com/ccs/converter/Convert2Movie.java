package com.ccs.converter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewDatabase;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;
import com.googlecode.javacv.FFmpegFrameRecorder;
import com.googlecode.javacv.FrameRecorder;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_imgproc;
import java.io.File;
import java.util.Arrays;

public class Convert2Movie extends Activity
  implements View.OnClickListener, AdListener
{
  static final String CAMERA_PIC_DIR = "/DCIM/CCFiles/Project01/";
  static final String SD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
  private static final String TAG = "Convert to Movie";
  static final String VID_DIR = "/DCIM/CCVideos/";
  static final String VID_DIR2 = "/DCIM/myVideos/";
  boolean adBased = false;
  AdView adView;
  opencv_core.IplImage colorImage;
  ProgressDialog dialog;
  File directory;
  File[] files;
  int fps;
  int height;
  opencv_core.IplImage image_small;
  String imgDir;
  int increment;
  private LayoutInflater mInflater = null;
  int max;
  String movieFileNamePath;
  String movieName;
  View overlayAds;
  int position;
  View progressBar;
  Handler progressHandler;
  AdRequest request;
  TextView statusText;
  boolean useTimeStampName = true;
  String vidDir;
  String vidDir2;
  int width;

  public void getList(File paramFile)
  {
    this.progressBar.setVisibility(0);
    this.imgDir = paramFile.toString();
    this.vidDir = (SD_DIR + "/DCIM/CCVideos/");
    this.vidDir2 = (SD_DIR + "/DCIM/myVideos/");
    File localFile = new File(this.imgDir);
    String[] arrayOfString2;
    String str;
    if (localFile.isDirectory())
    {
      this.files = localFile.listFiles();
      Arrays.sort(this.files);
      this.max = this.files.length;
      String[] arrayOfString1 = this.files[0].toString().split("/");
      arrayOfString2 = arrayOfString1[(arrayOfString1.length - 1)].toString().split(".jpg")[0].toString().split("_");
      str = arrayOfString1[5];
      if (!this.useTimeStampName)
        break label338;
    }
    label338: for (this.movieFileNamePath = (this.vidDir + str + "/" + "vid_" + arrayOfString2[0] + arrayOfString2[1] + "_" + arrayOfString2[2] + arrayOfString2[3] + arrayOfString2[4] + ".3gp"); ; this.movieFileNamePath = (this.vidDir2 + this.movieName + ".3gp"))
    {
      this.dialog = new ProgressDialog(this);
      this.dialog.setCancelable(false);
      this.dialog.setMessage("Creating Movie...");
      this.dialog.setProgressStyle(1);
      this.dialog.setProgress(0);
      this.dialog.setMax(this.max);
      this.dialog.show();
      this.progressHandler = new Convert2Movie.2(this);
      this.progressBar.setVisibility(4);
      new Convert2Movie.3(this).start();
      return;
    }
  }

  public void onBackPressed()
  {
  }

  public void onClick(View paramView)
  {
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFormat(-3);
    getWindow().setFlags(1024, 1024);
    setContentView(2130903044);
    this.statusText = ((TextView)findViewById(2131165192));
    this.statusText.setText(2131099666);
    this.mInflater = LayoutInflater.from(this);
    this.progressBar = this.mInflater.inflate(2130903057, null);
    addContentView(this.progressBar, new ViewGroup.LayoutParams(-1, -1));
    this.progressBar.setVisibility(0);
    this.progressBar.bringToFront();
    ((ImageView)findViewById(2131165206)).setOnClickListener(new Convert2Movie.1(this));
    Intent localIntent = getIntent();
    this.directory = new File(localIntent.getStringExtra("directory"));
    this.useTimeStampName = localIntent.getBooleanExtra("useTimeStampName", true);
    this.movieName = localIntent.getStringExtra("moviename");
    this.width = localIntent.getIntExtra("width", 640);
    this.height = localIntent.getIntExtra("height", 480);
    this.fps = localIntent.getIntExtra("fps", 10);
    this.adBased = localIntent.getBooleanExtra("adbased", false);
    this.mInflater = LayoutInflater.from(this);
    this.overlayAds = this.mInflater.inflate(2130903042, null);
    addContentView(this.overlayAds, new ViewGroup.LayoutParams(-1, -1));
    if (this.adBased)
    {
      new WebView(this).clearCache(true);
      if (WebViewDatabase.getInstance(this) != null)
      {
        this.overlayAds.setVisibility(0);
        this.adView = ((AdView)findViewById(2131165191));
        this.adView.setAdListener(this);
      }
    }
    while (true)
    {
      try
      {
        this.adView.loadAd(new AdRequest());
        getList(this.directory);
        return;
      }
      catch (NullPointerException localNullPointerException)
      {
        this.adView.setBackgroundResource(2130837509);
        this.adBased = false;
        continue;
      }
      this.overlayAds.setVisibility(4);
    }
  }

  public void onDismissScreen(Ad paramAd)
  {
  }

  public void onFailedToReceiveAd(Ad paramAd, AdRequest.ErrorCode paramErrorCode)
  {
    if (this.adView != null)
      this.adView.setBackgroundResource(2130837509);
  }

  public void onLeaveApplication(Ad paramAd)
  {
  }

  public void onPause()
  {
    super.onPause();
  }

  public void onPresentScreen(Ad paramAd)
  {
  }

  public void onReceiveAd(Ad paramAd)
  {
  }

  public void onResume()
  {
    super.onResume();
  }

  public void writeMovie(File[] paramArrayOfFile, String paramString)
  {
    FFmpegFrameRecorder localFFmpegFrameRecorder = new FFmpegFrameRecorder(paramString, this.width, this.height);
    localFFmpegFrameRecorder.setFrameRate(this.fps);
    localFFmpegFrameRecorder.setCodecID(13);
    localFFmpegFrameRecorder.setFormat("3gp");
    localFFmpegFrameRecorder.setPixelFormat(0);
    try
    {
      localFFmpegFrameRecorder.start();
      this.increment = 0;
      int i = paramArrayOfFile.length;
      j = 0;
      if (j < i);
    }
    catch (Exception localException1)
    {
      try
      {
        int j;
        while (true)
        {
          localFFmpegFrameRecorder.stop();
          localFFmpegFrameRecorder.release();
          if (this.adBased)
            this.adView.stopLoading();
          Intent localIntent = getIntent();
          localIntent.putExtra("moviefilename", this.movieFileNamePath);
          setResult(52, localIntent);
          System.gc();
          finish();
          return;
          localException1 = localException1;
          Log.e("Convert to Movie", localException1.toString());
        }
        this.colorImage = opencv_highgui.cvLoadImage(paramArrayOfFile[j].toString(), 1);
        if (this.colorImage == null)
          Log.e("Convert to Movie", "Color Image is NULL!");
        this.image_small = opencv_core.cvCreateImage(opencv_core.cvSize(640, 480), this.colorImage.depth(), this.colorImage.nChannels());
        if (this.image_small == null)
          Log.e("Convert to Movie", "Image Small is NULL!");
        opencv_imgproc.cvResize(this.colorImage, this.image_small, 1);
        if (localFFmpegFrameRecorder != null);
        while (true)
        {
          try
          {
            localFFmpegFrameRecorder.record(this.image_small);
            this.increment = (1 + this.increment);
            this.progressHandler.post(new Convert2Movie.4(this));
            opencv_core.cvReleaseImage(this.colorImage);
            opencv_core.cvReleaseImage(this.image_small);
            j++;
          }
          catch (Exception localException2)
          {
            Log.e("Convert to Movie", localException2.toString());
            continue;
          }
          Log.e("Convert to Movie", "Can not write frame, No writer!");
        }
      }
      catch (Exception localException3)
      {
        while (true)
          Log.e("Convert to Movie", localException3.toString());
      }
    }
  }
}