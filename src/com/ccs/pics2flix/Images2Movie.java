package com.ccs.pics2flix;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.ControlledCapture.FilePlayer;
import com.ControlledCapture.ListFiles;
import com.ControlledCapture.VideoPlayer;
import com.ccs.converter.Convert2Movie;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

public class Images2Movie extends Activity
  implements View.OnTouchListener
{
  private static final int BAD_CHARS = 1;
  static final String CAMERA_VID_DIR = "/DCIM/myVideos/";
  private static final int FOLDER_NOT_SET = 6;
  private static final int FORM_NOT_COMPLETE = 2;
  private static int MAXIMUM = 0;
  private static int MINIMUM = 0;
  private static final int MOVIE_NOT_MADE = 5;
  private static final int MOVIE_SHORT = 4;
  private static final int PAID_AP_FPS = 8;
  private static final int PAID_AP_MOVIE_NAME = 7;
  private static final int PAID_AP_RES = 9;
  private static final int PLAY_MOVIE = 3;
  static final String SD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
  Button FPSNumHolder;
  TextView FPSsetting;
  RadioButton HiRes;
  TextView ImageFolderText;
  Button MakeMovieButton;
  TextView MovieFilenameText;
  RadioButton NormRes;
  Button SetFPSButton;
  Button SetImgFolderButton;
  Button SetMovieNameButton;
  TextView TextFPSsetting;
  boolean adBased = false;
  private boolean autoDecrement = false;
  private boolean autoIncrement = false;
  Button decrementFPS;
  boolean folderIsSet = false;
  int fps = 10;
  boolean fpsIsSet = false;
  View fpsView;
  int height = 480;
  String imageFiles;
  Button incrementFPS;
  boolean isFreeVersion = false;
  double length;
  private LayoutInflater mInflater = null;
  String movieFileName;
  String movieLength;
  String movieName = "NewMovie";
  Editable movieNameStg;
  EditText movieNameTxt;
  View movieNameView;
  boolean movieWasMade = false;
  boolean nameIsSet = false;
  int numberJpegs;
  private Handler repeatUpdateHandler = new Handler();
  RadioGroup resolution;
  boolean settingFPS = false;
  boolean settingName = false;
  boolean useTimeStampName = false;
  int width = 640;

  static
  {
    MINIMUM = 1;
    MAXIMUM = 30;
  }

  private void convert2movie()
  {
    switch (this.resolution.getCheckedRadioButtonId())
    {
    default:
    case 2131165222:
    case 2131165223:
    }
    while (true)
    {
      Intent localIntent = new Intent(this, Convert2Movie.class);
      localIntent.putExtra("directory", this.imageFiles);
      localIntent.putExtra("useTimeStampName", this.useTimeStampName);
      localIntent.putExtra("moviename", this.movieName);
      localIntent.putExtra("width", this.width);
      localIntent.putExtra("height", this.height);
      localIntent.putExtra("fps", this.fps);
      localIntent.putExtra("adbased", this.adBased);
      startActivityForResult(localIntent, 0);
      return;
      this.width = 640;
      this.height = 480;
      continue;
      this.width = 720;
      this.height = 480;
    }
  }

  private String getNewProjectString()
  {
    this.movieNameStg = this.movieNameTxt.getText();
    if (!Boolean.valueOf(this.movieNameStg.toString().matches("^[a-zA-Z0-9]+$")).booleanValue())
    {
      showDialog(1);
      this.movieNameTxt.setText("");
      this.movieNameTxt.requestFocus();
      this.movieNameTxt.setSelectAllOnFocus(true);
    }
    while (true)
    {
      return this.movieName;
      this.nameIsSet = true;
      this.settingName = false;
      this.movieNameTxt.setVisibility(8);
      this.SetImgFolderButton.setVisibility(0);
      this.SetMovieNameButton.setVisibility(0);
      this.SetFPSButton.setVisibility(0);
      this.FPSsetting.setVisibility(0);
      this.ImageFolderText.setVisibility(0);
      this.MovieFilenameText.setVisibility(0);
      this.NormRes.setVisibility(0);
      this.HiRes.setVisibility(0);
      this.MovieFilenameText.setText(this.movieNameStg);
      this.movieName = this.movieNameStg.toString();
      this.MakeMovieButton.setText("Make Movie");
      ((InputMethodManager)getSystemService("input_method")).hideSoftInputFromWindow(this.movieNameTxt.getWindowToken(), 0);
    }
  }

  private void startFileBrowser()
  {
    Intent localIntent = new Intent(this, FileBrowser.class);
    localIntent.putExtra("adbased", this.adBased);
    localIntent.putExtra("isFreeVersion", this.isFreeVersion);
    localIntent.putExtra("useTimeStampName", this.useTimeStampName);
    startActivityForResult(localIntent, 0);
  }

  public void decrement()
  {
    int i = Integer.parseInt((String)this.FPSNumHolder.getText());
    if (i > MINIMUM)
    {
      int j = i - 1;
      this.FPSNumHolder.setText(String.valueOf(j));
      this.TextFPSsetting.setText("Image will be shown for ~1/" + String.valueOf(j) + " sec(s) in movie");
    }
  }

  public void increment()
  {
    int i = Integer.parseInt((String)this.FPSNumHolder.getText());
    if (i < MAXIMUM)
    {
      int j = i + 1;
      this.FPSNumHolder.setText(String.valueOf(j));
      this.TextFPSsetting.setText("Image will be shown for ~1/" + String.valueOf(j) + " sec(s) in movie");
    }
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 0) && (paramInt2 == -1))
    {
      this.folderIsSet = true;
      String str = paramIntent.getExtras().getString("selectedFolder");
      this.numberJpegs = paramIntent.getIntExtra("numberJpegs", 10);
      this.movieFileName = paramIntent.getExtras().getString("clickedProject");
      if (this.movieFileName != null)
      {
        String[] arrayOfString = paramIntent.getExtras().getString("clickedProject").split("\t");
        this.movieFileName = (SD_DIR + "/DCIM/myVideos/" + arrayOfString[0]);
        this.movieWasMade = true;
      }
      if (str != null)
      {
        this.imageFiles = str;
        this.ImageFolderText.setText(this.imageFiles);
      }
    }
    while (true)
    {
      return;
      if (paramInt2 == 52)
      {
        this.movieFileName = paramIntent.getExtras().getString("moviefilename");
        this.movieWasMade = true;
        showDialog(3);
        continue;
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFormat(-3);
    getWindow().setFlags(1024, 1024);
    setContentView(2130903053);
    this.SetImgFolderButton = ((Button)findViewById(2131165207));
    this.SetMovieNameButton = ((Button)findViewById(2131165208));
    this.SetFPSButton = ((Button)findViewById(2131165225));
    this.FPSsetting = ((TextView)findViewById(2131165226));
    this.MakeMovieButton = ((Button)findViewById(2131165228));
    this.ImageFolderText = ((TextView)findViewById(2131165192));
    this.MovieFilenameText = ((TextView)findViewById(2131165219));
    this.resolution = ((RadioGroup)findViewById(2131165221));
    this.NormRes = ((RadioButton)findViewById(2131165222));
    this.HiRes = ((RadioButton)findViewById(2131165223));
    this.mInflater = LayoutInflater.from(this);
    this.movieNameView = this.mInflater.inflate(2130903055, null);
    addContentView(this.movieNameView, new ViewGroup.LayoutParams(-1, -1));
    this.movieNameView.setOnTouchListener(this);
    this.movieNameTxt = ((EditText)findViewById(2131165202));
    this.movieNameTxt.setVisibility(8);
    this.mInflater = LayoutInflater.from(this);
    this.fpsView = this.mInflater.inflate(2130903052, null);
    addContentView(this.fpsView, new ViewGroup.LayoutParams(-1, -1));
    this.fpsView.setOnTouchListener(this);
    this.decrementFPS = ((Button)findViewById(2131165195));
    this.decrementFPS.setVisibility(8);
    this.FPSNumHolder = ((Button)findViewById(2131165196));
    this.FPSNumHolder.setVisibility(8);
    this.incrementFPS = ((Button)findViewById(2131165197));
    this.incrementFPS.setVisibility(8);
    this.TextFPSsetting = ((TextView)findViewById(2131165215));
    int i = Integer.parseInt((String)this.FPSNumHolder.getText());
    this.TextFPSsetting.setText("Image will be shown for ~1/" + String.valueOf(i) + " sec(s) in movie");
    this.TextFPSsetting.setVisibility(8);
    Intent localIntent = getIntent();
    this.adBased = localIntent.getBooleanExtra("adbased", false);
    this.isFreeVersion = localIntent.getBooleanExtra("isFreeVersion", this.isFreeVersion);
    if (this.isFreeVersion)
    {
      this.MovieFilenameText.setText(this.movieName);
      this.nameIsSet = true;
      this.fps = 5;
      this.FPSsetting.setText("5");
      this.fpsIsSet = true;
    }
    File localFile = new File(SD_DIR + "/DCIM/myVideos/");
    if (!localFile.isDirectory())
      localFile.mkdirs();
    this.SetImgFolderButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        Images2Movie.this.startFileBrowser();
      }
    });
    this.NormRes.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (Images2Movie.this.isFreeVersion)
        {
          Images2Movie.this.NormRes.setChecked(true);
          Images2Movie.this.HiRes.setChecked(false);
          Images2Movie.this.showDialog(9);
        }
      }
    });
    this.HiRes.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (Images2Movie.this.isFreeVersion)
        {
          Images2Movie.this.NormRes.setChecked(true);
          Images2Movie.this.HiRes.setChecked(false);
          Images2Movie.this.showDialog(9);
        }
      }
    });
    this.SetMovieNameButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        boolean bool;
        if (!Images2Movie.this.isFreeVersion)
        {
          Images2Movie localImages2Movie = Images2Movie.this;
          if (Images2Movie.this.settingName)
          {
            bool = false;
            localImages2Movie.settingName = bool;
            Images2Movie.this.movieNameTxt.setVisibility(0);
            Images2Movie.this.SetImgFolderButton.setVisibility(4);
            Images2Movie.this.SetMovieNameButton.setVisibility(4);
            Images2Movie.this.SetFPSButton.setVisibility(4);
            Images2Movie.this.FPSsetting.setVisibility(4);
            Images2Movie.this.ImageFolderText.setVisibility(4);
            Images2Movie.this.MovieFilenameText.setVisibility(4);
            Images2Movie.this.NormRes.setVisibility(4);
            Images2Movie.this.HiRes.setVisibility(4);
            Images2Movie.this.movieNameTxt.setSelectAllOnFocus(true);
            Images2Movie.this.MakeMovieButton.setText("      OK       ");
          }
        }
        while (true)
        {
          return;
          bool = true;
          break;
          Images2Movie.this.showDialog(7);
        }
      }
    });
    this.SetFPSButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        boolean bool;
        if (!Images2Movie.this.isFreeVersion)
        {
          Images2Movie localImages2Movie = Images2Movie.this;
          if (Images2Movie.this.settingFPS)
          {
            bool = false;
            localImages2Movie.settingFPS = bool;
            Images2Movie.this.decrementFPS.setVisibility(0);
            Images2Movie.this.FPSNumHolder.setVisibility(0);
            Images2Movie.this.incrementFPS.setVisibility(0);
            Images2Movie.this.TextFPSsetting.setVisibility(0);
            Images2Movie.this.SetImgFolderButton.setVisibility(4);
            Images2Movie.this.SetMovieNameButton.setVisibility(4);
            Images2Movie.this.SetFPSButton.setVisibility(4);
            Images2Movie.this.FPSsetting.setVisibility(4);
            Images2Movie.this.ImageFolderText.setVisibility(4);
            Images2Movie.this.MovieFilenameText.setVisibility(4);
            Images2Movie.this.NormRes.setVisibility(4);
            Images2Movie.this.HiRes.setVisibility(4);
            Images2Movie.this.movieNameTxt.setSelectAllOnFocus(true);
            Images2Movie.this.MakeMovieButton.setText("      OK       ");
          }
        }
        while (true)
        {
          return;
          bool = true;
          break;
          Images2Movie.this.showDialog(8);
        }
      }
    });
    this.decrementFPS.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        Images2Movie.this.decrement();
      }
    });
    this.decrementFPS.setOnLongClickListener(new View.OnLongClickListener()
    {
      public boolean onLongClick(View paramView)
      {
        Images2Movie.this.autoDecrement = true;
        Images2Movie.this.repeatUpdateHandler.post(new Images2Movie.RepetitiveUpdater(Images2Movie.this, null));
        return false;
      }
    });
    this.decrementFPS.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if ((paramMotionEvent.getAction() == 1) && (Images2Movie.this.autoDecrement))
          Images2Movie.this.autoDecrement = false;
        return false;
      }
    });
    this.incrementFPS.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        Images2Movie.this.increment();
      }
    });
    this.incrementFPS.setOnLongClickListener(new View.OnLongClickListener()
    {
      public boolean onLongClick(View paramView)
      {
        Images2Movie.this.autoIncrement = true;
        Images2Movie.this.repeatUpdateHandler.post(new Images2Movie.RepetitiveUpdater(Images2Movie.this, null));
        return false;
      }
    });
    this.incrementFPS.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if ((paramMotionEvent.getAction() == 1) && (Images2Movie.this.autoIncrement))
          Images2Movie.this.autoIncrement = false;
        return false;
      }
    });
    this.MakeMovieButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if ((Images2Movie.this.settingName) && (!Images2Movie.this.settingFPS))
          Images2Movie.this.getNewProjectString();
        while (true)
        {
          return;
          if ((Images2Movie.this.settingFPS) && (!Images2Movie.this.settingName))
          {
            Images2Movie.this.settingFPS = false;
            Images2Movie.this.fpsIsSet = true;
            Images2Movie.this.decrementFPS.setVisibility(8);
            Images2Movie.this.FPSNumHolder.setVisibility(8);
            Images2Movie.this.incrementFPS.setVisibility(8);
            Images2Movie.this.TextFPSsetting.setVisibility(8);
            Images2Movie.this.SetImgFolderButton.setVisibility(0);
            Images2Movie.this.SetMovieNameButton.setVisibility(0);
            Images2Movie.this.SetFPSButton.setVisibility(0);
            Images2Movie.this.FPSsetting.setVisibility(0);
            Images2Movie.this.ImageFolderText.setVisibility(0);
            Images2Movie.this.MovieFilenameText.setVisibility(0);
            Images2Movie.this.NormRes.setVisibility(0);
            Images2Movie.this.HiRes.setVisibility(0);
            Images2Movie.this.MakeMovieButton.setText("Make Movie");
            String str = (String)Images2Movie.this.FPSNumHolder.getText();
            Images2Movie.this.fps = Integer.parseInt(str);
            Images2Movie.this.FPSsetting.setText(str);
            continue;
          }
          if ((Images2Movie.this.folderIsSet) && (Images2Movie.this.nameIsSet) && (Images2Movie.this.fpsIsSet))
          {
            Images2Movie.this.length = (Images2Movie.this.numberJpegs / Images2Movie.this.fps);
            DecimalFormat localDecimalFormat = new DecimalFormat("#0.00");
            Images2Movie.this.movieLength = localDecimalFormat.format(Images2Movie.this.length);
            if (Images2Movie.this.length < 1.0D)
            {
              Images2Movie.this.showDialog(4);
              continue;
            }
            new Images2Movie.12.1(this).start();
            continue;
          }
          Images2Movie.this.showDialog(2);
        }
      }
    });
    if (this.movieNameTxt.isShown())
    {
      this.movieNameTxt.setSelectAllOnFocus(true);
      this.movieNameTxt.setOnKeyListener(new View.OnKeyListener()
      {
        public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
        {
          switch (paramInt)
          {
          default:
          case 66:
          }
          for (int i = 0; ; i = 1)
          {
            return i;
            Images2Movie.this.getNewProjectString();
          }
        }
      });
      this.movieNameTxt.setOnEditorActionListener(new TextView.OnEditorActionListener()
      {
        public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
        {
          if (paramInt == 6)
            Images2Movie.this.getNewProjectString();
          for (int i = 1; ; i = 0)
            return i;
        }
      });
    }
    ((ImageView)findViewById(2131165193)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        Intent localIntent = new Intent();
        localIntent.setAction("android.intent.action.VIEW");
        localIntent.addCategory("android.intent.category.BROWSABLE");
        if (Images2Movie.this.isFreeVersion)
          localIntent.setData(Uri.parse("market://details?id=com.ccs.pics2flix.Pro"));
        while (true)
        {
          Images2Movie.this.startActivity(localIntent);
          return;
          localIntent.setData(Uri.parse("market://search?q=pub:Controlled Capture Systems, LLC"));
        }
      }
    });
  }

  public Dialog onCreateDialog(int paramInt)
  {
    AlertDialog localAlertDialog;
    switch (paramInt)
    {
    default:
      localAlertDialog = null;
    case 7:
    case 8:
    case 9:
    case 1:
    case 2:
    case 5:
    case 6:
    case 3:
    case 4:
    }
    while (true)
    {
      return localAlertDialog;
      TextView localTextView3 = new TextView(this);
      SpannableString localSpannableString3 = new SpannableString("This feature is only available on our Pro applications which are available\n\n on the Controlled Capture Systems, LLC Android Market page \n\nFor your testing of this application, the Free version automatically names your movie 'NewMovie'.");
      Linkify.addLinks(localSpannableString3, Pattern.compile("Controlled Capture Systems, LLC"), "https://market.android.com/search?q=pub:");
      localTextView3.setText(localSpannableString3);
      localTextView3.setMovementMethod(LinkMovementMethod.getInstance());
      localTextView3.setLinkTextColor(-16711681);
      localAlertDialog = new AlertDialog.Builder(this).setTitle("Feature Disabled").setCancelable(true).setIcon(17301659).setPositiveButton(2131099689, null).setView(localTextView3).create();
      continue;
      TextView localTextView2 = new TextView(this);
      SpannableString localSpannableString2 = new SpannableString("This feature is only available on our Pro applications which are available\n\n on the Controlled Capture Systems, LLC Android Market page \n\nFor your testing of this application, the Free version automatically sets the FPS to 5 frames per second.");
      Linkify.addLinks(localSpannableString2, Pattern.compile("Controlled Capture Systems, LLC"), "https://market.android.com/search?q=pub:");
      localTextView2.setText(localSpannableString2);
      localTextView2.setMovementMethod(LinkMovementMethod.getInstance());
      localTextView2.setLinkTextColor(-16711681);
      localAlertDialog = new AlertDialog.Builder(this).setTitle("Feature Disabled").setCancelable(true).setIcon(17301659).setPositiveButton(2131099689, null).setView(localTextView2).create();
      continue;
      TextView localTextView1 = new TextView(this);
      SpannableString localSpannableString1 = new SpannableString("This feature is only available on our Pro applications which are available\n\n on the Controlled Capture Systems, LLC Android Market page \n\nFor your testing of this application, the Free version automatically sets the resolution at 640 x 480");
      Linkify.addLinks(localSpannableString1, Pattern.compile("Controlled Capture Systems, LLC"), "https://market.android.com/search?q=pub:");
      localTextView1.setText(localSpannableString1);
      localTextView1.setMovementMethod(LinkMovementMethod.getInstance());
      localTextView1.setLinkTextColor(-16711681);
      localAlertDialog = new AlertDialog.Builder(this).setTitle("Feature Disabled").setCancelable(true).setIcon(17301659).setPositiveButton(2131099689, null).setView(localTextView1).create();
      continue;
      localAlertDialog = new AlertDialog.Builder(this).setTitle("Please use only Letters or Numbers for your Movie names!").setIcon(17301543).setNegativeButton("OK", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
        }
      }).create();
      continue;
      localAlertDialog = new AlertDialog.Builder(this).setTitle("Error, one of the necessary items is not set!").setIcon(17301543).setMessage("Please ensure Image Folder, Movie Filename, and Frames Per Sec (FPS) are set before trying to make movie.").setNegativeButton("OK", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
        }
      }).create();
      continue;
      localAlertDialog = new AlertDialog.Builder(this).setTitle("No Movie to Play!").setIcon(17301543).setMessage("You must create a movie to play one!").setNegativeButton("OK", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
        }
      }).create();
      continue;
      localAlertDialog = new AlertDialog.Builder(this).setTitle("Image Folder Not Set!").setIcon(17301543).setMessage("You must Set Image Folder before you can play one!").setNegativeButton("OK", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
        }
      }).create();
      continue;
      localAlertDialog = new AlertDialog.Builder(this).setTitle("Would you like to watch your new movie?").setIcon(17301543).setNegativeButton("No", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
        }
      }).setPositiveButton("Yes", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
          Intent localIntent = new Intent(Images2Movie.this, VideoPlayer.class);
          localIntent.putExtra("directory", Images2Movie.this.movieFileName);
          localIntent.putExtra("fromCCS", false);
          localIntent.putExtra("adbased", false);
          Images2Movie.this.startActivityForResult(localIntent, 0);
        }
      }).create();
      continue;
      localAlertDialog = new AlertDialog.Builder(this).setTitle("Your New Movie will be too short to be playable!").setIcon(17301543).setMessage("Your movie is only " + this.movieLength + " secs long. Please take more images or set Frames Per Sec (FPS) to a lower number!").setNegativeButton("OK", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.cancel();
        }
      }).create();
    }
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2130903050, paramMenu);
    return true;
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default:
    case 2131165211:
    case 2131165212:
    case 2131165213:
    }
    while (true)
    {
      return true;
      if (this.folderIsSet)
      {
        Intent localIntent3 = new Intent(this, FilePlayer.class);
        localIntent3.putExtra("directory", this.imageFiles);
        localIntent3.putExtra("adbased", false);
        localIntent3.putExtra("isFreeVersion", this.isFreeVersion);
        localIntent3.putExtra("useTimeStampName", this.useTimeStampName);
        startActivityForResult(localIntent3, 0);
        continue;
      }
      showDialog(6);
      continue;
      if (this.movieWasMade)
      {
        Intent localIntent2 = new Intent(this, VideoPlayer.class);
        localIntent2.putExtra("directory", this.movieFileName);
        localIntent2.putExtra("fromCCS", false);
        localIntent2.putExtra("adbased", false);
        startActivityForResult(localIntent2, 0);
        continue;
      }
      showDialog(5);
      continue;
      String str = SD_DIR + "/DCIM/myVideos/";
      Intent localIntent1 = new Intent(this, ListFiles.class);
      localIntent1.putExtra("directory", str);
      localIntent1.putExtra("videoenabled", true);
      localIntent1.putExtra("fromImgs2Movie", true);
      startActivityForResult(localIntent1, 0);
    }
  }

  public void onPrepareDialog(int paramInt, Dialog paramDialog)
  {
    switch (paramInt)
    {
    default:
    case 4:
    }
    while (true)
    {
      return;
      this.length = (this.numberJpegs / this.fps);
      this.movieLength = new DecimalFormat("#0.00").format(this.length);
      ((AlertDialog)paramDialog).setMessage("Your movie is only " + this.movieLength + " secs long. Please take more images or set Frames Per Sec (FPS) to a lower number!");
    }
  }

  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    return false;
  }

  private class RepetitiveUpdater
    implements Runnable
  {
    private static final long REPEAT_DELAY;

    private RepetitiveUpdater()
    {
    }

    public void run()
    {
      if (Images2Movie.this.autoIncrement)
      {
        Images2Movie.this.increment();
        Images2Movie.this.repeatUpdateHandler.postDelayed(new RepetitiveUpdater(Images2Movie.this), 0L);
      }
      while (true)
      {
        return;
        if (Images2Movie.this.autoDecrement)
        {
          Images2Movie.this.decrement();
          Images2Movie.this.repeatUpdateHandler.postDelayed(new RepetitiveUpdater(Images2Movie.this), 0L);
          continue;
        }
      }
    }
  }
}