package com.ccs.pics2flix;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.ControlledCapture.FilePlayer;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBrowser extends ListActivity
{
  private static final int DIALOG_DELETE = 2;
  private static final int FILE_DIALOG = 1;
  private static final int NON_JPEG = 4;
  private static final int NO_FILES = 3;
  private static final int NUM_IMGS = 5;
  protected static final int SUB_ACTIVITY_REQUEST_CODE = 1337;
  File aDirectory;
  boolean adBased = false;
  File clickedFile;
  private File currentDirectory = new File("/mnt/sdcard/DCIM");
  File currentFile;
  private List<IconifiedText> directoryEntries = new ArrayList();
  private final DISPLAYMODE displayMode = DISPLAYMODE.RELATIVE;
  File[] fileList;
  IconifiedTextListAdapter iconedList;
  boolean isFreeVersion = false;
  ListView list;
  int numberJpegs;
  int position;
  File rootFile;
  boolean useTimeStampName = false;

  private void browseTo(File paramFile)
  {
    if (this.displayMode == DISPLAYMODE.RELATIVE)
      setTitle("Long Press Folder to Select");
    if (paramFile.isDirectory())
    {
      this.currentDirectory = paramFile;
      fill(paramFile.listFiles());
    }
  }

  private void browseToRoot()
  {
    this.rootFile = new File("/mnt/sdcard/DCIM/");
    browseTo(this.rootFile);
  }

  private boolean checkEndsWithInStringArray(String paramString, String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; ; j++)
    {
      if (j >= i);
      for (int k = 0; ; k = 1)
      {
        return k;
        if (!paramString.endsWith(paramArrayOfString[j]))
          break;
      }
    }
  }

  public static boolean deleteDir(File paramFile)
  {
    File[] arrayOfFile;
    int i;
    if (paramFile.isDirectory())
    {
      arrayOfFile = paramFile.listFiles();
      i = 0;
      if (i < arrayOfFile.length);
    }
    for (boolean bool = paramFile.delete(); ; bool = false)
    {
      return bool;
      if (arrayOfFile[i].isDirectory())
        deleteDir(arrayOfFile[i]);
      while (true)
      {
        i++;
        break;
        arrayOfFile[i].delete();
      }
    }
  }

  private void fill(File[] paramArrayOfFile)
  {
    this.directoryEntries.clear();
    if (this.currentDirectory.compareTo(this.rootFile) != 0)
    {
      Log.e("File Browser", "Image Directory is: " + this.rootFile.toString());
      this.directoryEntries.add(new IconifiedText(getString(2131099699), getResources().getDrawable(2130837537)));
    }
    Drawable localDrawable = null;
    int i = paramArrayOfFile.length;
    int j = 0;
    if (j >= i)
    {
      Collections.sort(this.directoryEntries);
      this.list = getListView();
      this.list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
      {
        public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
        {
          FileBrowser.this.position = paramInt;
          FileBrowser.this.onLongListItemClick(paramView, FileBrowser.this.position, paramLong);
          return false;
        }
      });
      IconifiedTextListAdapter localIconifiedTextListAdapter = new IconifiedTextListAdapter(this);
      localIconifiedTextListAdapter.setListItems(this.directoryEntries);
      setListAdapter(localIconifiedTextListAdapter);
      return;
    }
    File localFile = paramArrayOfFile[j];
    if (localFile.isDirectory())
    {
      localDrawable = getResources().getDrawable(2130837513);
      label177: switch ($SWITCH_TABLE$com$ccs$pics2flix$FileBrowser$DISPLAYMODE()[this.displayMode.ordinal()])
      {
      default:
      case 1:
      case 2:
      }
    }
    while (true)
    {
      j++;
      break;
      if (!checkEndsWithInStringArray(localFile.getName(), getResources().getStringArray(2131296256)))
        break label177;
      localDrawable = getResources().getDrawable(2130837519);
      break label177;
      this.directoryEntries.add(new IconifiedText(localFile.getPath(), localDrawable));
      continue;
      int k = this.currentDirectory.getAbsolutePath().length();
      this.directoryEntries.add(new IconifiedText(localFile.getAbsolutePath().substring(k), localDrawable));
    }
  }

  private void upOneLevel()
  {
    if (this.currentDirectory.getParent() != null)
      browseTo(this.currentDirectory.getParentFile());
  }

  public void onBackPressed()
  {
    if (this.currentDirectory.compareTo(this.rootFile) != 0)
      upOneLevel();
    while (true)
    {
      return;
      finish();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = getIntent();
    this.adBased = localIntent.getBooleanExtra("adbased", false);
    this.isFreeVersion = localIntent.getBooleanExtra("isFreeVersion", this.isFreeVersion);
    this.useTimeStampName = localIntent.getBooleanExtra("useTimeStampName", this.useTimeStampName);
    browseToRoot();
    setSelection(0);
  }

  public Dialog onCreateDialog(int paramInt)
  {
    AlertDialog localAlertDialog;
    switch (paramInt)
    {
    default:
      localAlertDialog = null;
    case 1:
    case 5:
    case 3:
    case 4:
    case 2:
    }
    while (true)
    {
      return localAlertDialog;
      CharSequence[] arrayOfCharSequence = new CharSequence[3];
      arrayOfCharSequence[0] = "Yes, use this folder";
      arrayOfCharSequence[1] = "No, choose a different folder";
      arrayOfCharSequence[2] = "Review Images with Image Player";
      localAlertDialog = new AlertDialog.Builder(this).setTitle("Use this folder?").setItems(arrayOfCharSequence, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          if (paramInt == 0)
          {
            Intent localIntent2 = FileBrowser.this.getIntent();
            localIntent2.putExtra("selectedFolder", FileBrowser.this.clickedFile.toString());
            localIntent2.putExtra("numberJpegs", FileBrowser.this.numberJpegs);
            FileBrowser.this.setResult(-1, localIntent2);
            paramDialogInterface.dismiss();
            FileBrowser.this.showDialog(5);
          }
          if (paramInt == 1)
            paramDialogInterface.dismiss();
          if (paramInt == 2)
          {
            Intent localIntent1 = new Intent(FileBrowser.this, FilePlayer.class);
            localIntent1.putExtra("directory", FileBrowser.this.clickedFile.toString());
            localIntent1.putExtra("adbased", FileBrowser.this.adBased);
            localIntent1.putExtra("isFreeVersion", FileBrowser.this.isFreeVersion);
            localIntent1.putExtra("useTimeStampName", FileBrowser.this.useTimeStampName);
            FileBrowser.this.startActivityForResult(localIntent1, 0);
          }
        }
      }).create();
      continue;
      String str = "The Folder selected, contains " + this.numberJpegs + " image files which will be used for your new movie.";
      localAlertDialog = new AlertDialog.Builder(this).setTitle("Selected Folder Information").setIcon(17301659).setMessage(str).setNegativeButton("OK", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
          FileBrowser.this.finish();
        }
      }).create();
      continue;
      localAlertDialog = new AlertDialog.Builder(this).setTitle("Can not use this folder!").setIcon(17301543).setMessage("Movie maker can not process an empty folder.").setNegativeButton("OK", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
        }
      }).create();
      continue;
      localAlertDialog = new AlertDialog.Builder(this).setTitle("Can not use this folder!").setIcon(17301543).setMessage("Movie maker can only process folders that contain  .jpg files.").setNegativeButton("OK", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
        }
      }).create();
      continue;
      localAlertDialog = new AlertDialog.Builder(this).setTitle(getString(2131099669) + " Project named " + this.clickedFile + "?").setIcon(17301543).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
        }
      }).setPositiveButton("OK", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          String str = FileBrowser.this.clickedFile.toString().split("\t")[0];
          File localFile = new File(FileBrowser.this.currentDirectory + File.separator + str);
          if ((localFile.isDirectory()) && (Boolean.valueOf(FileBrowser.deleteDir(localFile)).booleanValue()))
            Toast.makeText(FileBrowser.this, "Project has been deleted", 0).show();
          FileBrowser.this.iconedList.removeItem(FileBrowser.this.position);
          FileBrowser.this.iconedList.notifyDataSetChanged();
          paramDialogInterface.dismiss();
          FileBrowser.this.fill(FileBrowser.this.aDirectory.listFiles());
        }
      }).create();
    }
  }

  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    super.onListItemClick(paramListView, paramView, paramInt, paramLong);
    if (((IconifiedText)this.directoryEntries.get(paramInt)).getText().equals(getString(2131099699)))
      upOneLevel();
    label171: 
    while (true)
    {
      return;
      File localFile = null;
      switch ($SWITCH_TABLE$com$ccs$pics2flix$FileBrowser$DISPLAYMODE()[this.displayMode.ordinal()])
      {
      default:
      case 2:
      case 1:
      }
      while (true)
      {
        if (localFile == null)
          break label171;
        browseTo(localFile);
        break;
        localFile = new File(this.currentDirectory.getAbsolutePath() + ((IconifiedText)this.directoryEntries.get(paramInt)).getText());
        continue;
        localFile = new File(((IconifiedText)this.directoryEntries.get(paramInt)).getText());
      }
    }
  }

  protected void onLongListItemClick(View paramView, int paramInt, long paramLong)
  {
    this.clickedFile = new File(this.currentDirectory.getAbsolutePath() + ((IconifiedText)this.directoryEntries.get(this.position)).getText());
    Log.e("Browser", "Item longListItemClick: " + this.clickedFile);
    int i;
    if (this.clickedFile.isDirectory())
    {
      this.numberJpegs = 0;
      this.fileList = this.clickedFile.listFiles();
      i = 0;
      if (i < this.fileList.length)
        break label150;
      if (this.numberJpegs != this.fileList.length)
        break label193;
      if (this.fileList.length != 0)
        break label185;
      showDialog(3);
    }
    while (true)
    {
      return;
      label150: if (this.fileList[i].toString().contains(".jpg"))
        this.numberJpegs = (1 + this.numberJpegs);
      i++;
      break;
      label185: showDialog(1);
      continue;
      label193: showDialog(4);
    }
  }

  private static enum DISPLAYMODE
  {
    static
    {
      DISPLAYMODE[] arrayOfDISPLAYMODE = new DISPLAYMODE[2];
      arrayOfDISPLAYMODE[0] = ABSOLUTE;
      arrayOfDISPLAYMODE[1] = RELATIVE;
      ENUM$VALUES = arrayOfDISPLAYMODE;
    }
  }
}