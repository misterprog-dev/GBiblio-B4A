package b4a.sqlitelight1;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class gererbook extends Activity implements B4AActivity{
	public static gererbook mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.sqlitelight1", "b4a.sqlitelight1.gererbook");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (gererbook).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.sqlitelight1", "b4a.sqlitelight1.gererbook");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.sqlitelight1.gererbook", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (gererbook) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (gererbook) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return gererbook.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (gererbook) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            gererbook mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (gererbook) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmpback = null;
public static anywheresoftware.b4a.sql.SQL _sql1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _buttonmodifier = null;
public anywheresoftware.b4a.objects.ButtonWrapper _buttonsupprimer = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextnom = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextnombre = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextpublication = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextresume = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittexttitre = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinnercode = null;
public b4a.sqlitelight1.main _main = null;
public b4a.sqlitelight1.addbook _addbook = null;
public b4a.sqlitelight1.addadhe _addadhe = null;
public b4a.sqlitelight1.emprunt _emprunt = null;
public b4a.sqlitelight1.depot _depot = null;
public b4a.sqlitelight1.forgetmdp _forgetmdp = null;
public b4a.sqlitelight1.gereradh _gereradh = null;
public b4a.sqlitelight1.inscription _inscription = null;
public b4a.sqlitelight1.connexion _connexion = null;
public b4a.sqlitelight1.dashboard _dashboard = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 27;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 29;BA.debugLine="Activity.LoadLayout(\"gererBook\")";
mostCurrent._activity.LoadLayout("gererBook",mostCurrent.activityBA);
 //BA.debugLineNum = 31;BA.debugLine="SQL1.Initialize(File.DirInternal, \"gbiblio.db\", F";
_sql1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"gbiblio.db",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 33;BA.debugLine="bmpBack.Initialize(File.DirAssets, \"background.jp";
_bmpback.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"background.jpg");
 //BA.debugLineNum = 34;BA.debugLine="Activity.SetBackgroundImage(bmpBack)";
mostCurrent._activity.SetBackgroundImageNew((android.graphics.Bitmap)(_bmpback.getObject()));
 //BA.debugLineNum = 36;BA.debugLine="ReadTable";
_readtable();
 //BA.debugLineNum = 38;BA.debugLine="SpinnerCode.SelectedIndex = 0";
mostCurrent._spinnercode.setSelectedIndex((int) (0));
 //BA.debugLineNum = 40;BA.debugLine="ReadTableForView";
_readtableforview();
 //BA.debugLineNum = 42;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 119;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 120;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 121;BA.debugLine="SQL1.Close		'if the user closes the program we c";
_sql1.Close();
 };
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 114;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 115;BA.debugLine="SpinnerCode.SelectedIndex = 0";
mostCurrent._spinnercode.setSelectedIndex((int) (0));
 //BA.debugLineNum = 116;BA.debugLine="ClearChamp";
_clearchamp();
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return "";
}
public static String  _buttonmodifier_click() throws Exception{
 //BA.debugLineNum = 140;BA.debugLine="Sub ButtonModifier_Click";
 //BA.debugLineNum = 141;BA.debugLine="If SpinnerCode.SelectedIndex <> 0 Then";
if (mostCurrent._spinnercode.getSelectedIndex()!=0) { 
 //BA.debugLineNum = 142;BA.debugLine="UpdateInfo";
_updateinfo();
 };
 //BA.debugLineNum = 144;BA.debugLine="End Sub";
return "";
}
public static String  _buttonsupprimer_click() throws Exception{
 //BA.debugLineNum = 134;BA.debugLine="Sub ButtonSupprimer_Click";
 //BA.debugLineNum = 135;BA.debugLine="If SpinnerCode.SelectedIndex <> 0 Then";
if (mostCurrent._spinnercode.getSelectedIndex()!=0) { 
 //BA.debugLineNum = 136;BA.debugLine="deleteBook";
_deletebook();
 };
 //BA.debugLineNum = 138;BA.debugLine="End Sub";
return "";
}
public static String  _clearchamp() throws Exception{
 //BA.debugLineNum = 44;BA.debugLine="Sub ClearChamp";
 //BA.debugLineNum = 45;BA.debugLine="EditTextNom.Text = \"\"";
mostCurrent._edittextnom.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 46;BA.debugLine="EditTextTitre.Text = \"\"";
mostCurrent._edittexttitre.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 47;BA.debugLine="EditTextPublication.Text = \"\"";
mostCurrent._edittextpublication.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 48;BA.debugLine="EditTextNombre.Text = \"\"";
mostCurrent._edittextnombre.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 49;BA.debugLine="EditTextResume.Text = \"\"";
mostCurrent._edittextresume.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public static String  _deletebook() throws Exception{
String _query = "";
 //BA.debugLineNum = 100;BA.debugLine="Sub deleteBook";
 //BA.debugLineNum = 101;BA.debugLine="Dim Query As String";
_query = "";
 //BA.debugLineNum = 102;BA.debugLine="Query = \"DELETE FROM livre WHERE codeLivre = ? \"";
_query = "DELETE FROM livre WHERE codeLivre = ? ";
 //BA.debugLineNum = 103;BA.debugLine="SQL1.ExecNonQuery2(Query, Array As String(Spinner";
_sql1.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{mostCurrent._spinnercode.getSelectedItem()}));
 //BA.debugLineNum = 105;BA.debugLine="ReadTable";
_readtable();
 //BA.debugLineNum = 106;BA.debugLine="SpinnerCode.SelectedIndex = 0";
mostCurrent._spinnercode.setSelectedIndex((int) (0));
 //BA.debugLineNum = 107;BA.debugLine="ClearChamp";
_clearchamp();
 //BA.debugLineNum = 109;BA.debugLine="ToastMessageShow(\"Suppression effectuée avec succ";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Suppression effectuée avec succès"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 110;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 13;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 17;BA.debugLine="Private ButtonModifier As Button";
mostCurrent._buttonmodifier = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private ButtonSupprimer As Button";
mostCurrent._buttonsupprimer = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private EditTextNom As EditText";
mostCurrent._edittextnom = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private EditTextNombre As EditText";
mostCurrent._edittextnombre = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private EditTextPublication As EditText";
mostCurrent._edittextpublication = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private EditTextResume As EditText";
mostCurrent._edittextresume = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private EditTextTitre As EditText";
mostCurrent._edittexttitre = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private SpinnerCode As Spinner";
mostCurrent._spinnercode = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim bmpBack  As Bitmap";
_bmpback = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 10;BA.debugLine="Dim SQL1 As SQL";
_sql1 = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
public static String  _readtable() throws Exception{
int _row = 0;
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
int _rownumber = 0;
 //BA.debugLineNum = 52;BA.debugLine="Sub ReadTable";
 //BA.debugLineNum = 53;BA.debugLine="Dim Row As Int";
_row = 0;
 //BA.debugLineNum = 54;BA.debugLine="Dim Cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Dim RowNumber = 0 As Int";
_rownumber = (int) (0);
 //BA.debugLineNum = 58;BA.debugLine="Cursor1 = SQL1.ExecQuery(\"SELECT codeLivre FROM l";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery("SELECT codeLivre FROM livre")));
 //BA.debugLineNum = 59;BA.debugLine="SpinnerCode.Clear";
mostCurrent._spinnercode.Clear();
 //BA.debugLineNum = 60;BA.debugLine="SpinnerCode.Add(\"\")";
mostCurrent._spinnercode.Add("");
 //BA.debugLineNum = 61;BA.debugLine="If Cursor1.RowCount > 0 Then";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 62;BA.debugLine="RowNumber = Cursor1.RowCount";
_rownumber = _cursor1.getRowCount();
 //BA.debugLineNum = 63;BA.debugLine="For Row = 0 To RowNumber - 1";
{
final int step9 = 1;
final int limit9 = (int) (_rownumber-1);
_row = (int) (0) ;
for (;_row <= limit9 ;_row = _row + step9 ) {
 //BA.debugLineNum = 64;BA.debugLine="Cursor1.Position = Row";
_cursor1.setPosition(_row);
 //BA.debugLineNum = 65;BA.debugLine="SpinnerCode.Add(Cursor1.GetString(\"codeLivre\"))";
mostCurrent._spinnercode.Add(_cursor1.GetString("codeLivre"));
 }
};
 };
 //BA.debugLineNum = 68;BA.debugLine="Cursor1.Close";
_cursor1.Close();
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return "";
}
public static String  _readtableforview() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
 //BA.debugLineNum = 72;BA.debugLine="Sub ReadTableForView";
 //BA.debugLineNum = 73;BA.debugLine="Dim Cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Cursor1 = SQL1.ExecQuery2(\"SELECT * FROM livre WH";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery2("SELECT * FROM livre WHERE codeLivre = ?",new String[]{mostCurrent._spinnercode.getSelectedItem()})));
 //BA.debugLineNum = 75;BA.debugLine="Cursor1.Position = 0";
_cursor1.setPosition((int) (0));
 //BA.debugLineNum = 76;BA.debugLine="If Cursor1.RowCount > 0 Then";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 77;BA.debugLine="EditTextNom.Text = Cursor1.GetString(\"nomAuteur\"";
mostCurrent._edittextnom.setText(BA.ObjectToCharSequence(_cursor1.GetString("nomAuteur")));
 //BA.debugLineNum = 78;BA.debugLine="EditTextTitre.Text = Cursor1.GetString(\"titreLiv";
mostCurrent._edittexttitre.setText(BA.ObjectToCharSequence(_cursor1.GetString("titreLivre")));
 //BA.debugLineNum = 79;BA.debugLine="EditTextPublication.Text = Cursor1.GetString(\"da";
mostCurrent._edittextpublication.setText(BA.ObjectToCharSequence(_cursor1.GetString("datePub")));
 //BA.debugLineNum = 80;BA.debugLine="EditTextNombre.Text = Cursor1.Getint(\"nbreExple\"";
mostCurrent._edittextnombre.setText(BA.ObjectToCharSequence(_cursor1.GetInt("nbreExple")));
 //BA.debugLineNum = 81;BA.debugLine="EditTextResume.Text = Cursor1.GetString(\"resume\"";
mostCurrent._edittextresume.setText(BA.ObjectToCharSequence(_cursor1.GetString("resume")));
 };
 //BA.debugLineNum = 83;BA.debugLine="Cursor1.Close";
_cursor1.Close();
 //BA.debugLineNum = 85;BA.debugLine="End Sub";
return "";
}
public static String  _spinnercode_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 126;BA.debugLine="Sub SpinnerCode_ItemClick (Position As Int, Value";
 //BA.debugLineNum = 127;BA.debugLine="If SpinnerCode.SelectedIndex <> 0 Then";
if (mostCurrent._spinnercode.getSelectedIndex()!=0) { 
 //BA.debugLineNum = 128;BA.debugLine="ReadTableForView";
_readtableforview();
 }else {
 //BA.debugLineNum = 130;BA.debugLine="ClearChamp";
_clearchamp();
 };
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return "";
}
public static String  _updateinfo() throws Exception{
String _query = "";
int _nbre = 0;
 //BA.debugLineNum = 88;BA.debugLine="Sub UpdateInfo";
 //BA.debugLineNum = 90;BA.debugLine="Dim Query As String";
_query = "";
 //BA.debugLineNum = 91;BA.debugLine="Dim nbre As Int";
_nbre = 0;
 //BA.debugLineNum = 92;BA.debugLine="nbre = EditTextNombre.Text";
_nbre = (int)(Double.parseDouble(mostCurrent._edittextnombre.getText()));
 //BA.debugLineNum = 94;BA.debugLine="Query = \"UPDATE livre SET nomAuteur = ?, titreLiv";
_query = "UPDATE livre SET nomAuteur = ?, titreLivre = ? , datePub = ? , nbreExple = "+BA.NumberToString(_nbre)+"  , resume = ? WHERE codeLivre = ? ";
 //BA.debugLineNum = 95;BA.debugLine="SQL1.ExecNonQuery2(Query, Array As String(EditTex";
_sql1.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{mostCurrent._edittextnom.getText(),mostCurrent._edittexttitre.getText(),mostCurrent._edittextpublication.getText(),mostCurrent._edittextresume.getText(),mostCurrent._spinnercode.getSelectedItem()}));
 //BA.debugLineNum = 96;BA.debugLine="ToastMessageShow(\"Mise à jour effectuée avec succ";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Mise à jour effectuée avec succès"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
return "";
}
}
