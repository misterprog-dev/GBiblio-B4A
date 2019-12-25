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

public class depot extends Activity implements B4AActivity{
	public static depot mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.sqlitelight1", "b4a.sqlitelight1.depot");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (depot).");
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
		activityBA = new BA(this, layout, processBA, "b4a.sqlitelight1", "b4a.sqlitelight1.depot");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.sqlitelight1.depot", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (depot) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (depot) Resume **");
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
		return depot.class;
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
        BA.LogInfo("** Activity (depot) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            depot mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (depot) Resume **");
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
public static boolean _eltsexiste = false;
public anywheresoftware.b4a.objects.ButtonWrapper _buttonvaliddepot = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinnercodel = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinneremailadh = null;
public b4a.sqlitelight1.main _main = null;
public b4a.sqlitelight1.addbook _addbook = null;
public b4a.sqlitelight1.addadhe _addadhe = null;
public b4a.sqlitelight1.emprunt _emprunt = null;
public b4a.sqlitelight1.forgetmdp _forgetmdp = null;
public b4a.sqlitelight1.gereradh _gereradh = null;
public b4a.sqlitelight1.gererbook _gererbook = null;
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
 //BA.debugLineNum = 28;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 30;BA.debugLine="Activity.LoadLayout(\"Depot\")";
mostCurrent._activity.LoadLayout("Depot",mostCurrent.activityBA);
 //BA.debugLineNum = 31;BA.debugLine="SQL1.Initialize(File.DirInternal, \"gbiblio.db\", F";
_sql1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"gbiblio.db",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 33;BA.debugLine="bmpBack.Initialize(File.DirAssets, \"background.jp";
_bmpback.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"background.jpg");
 //BA.debugLineNum = 34;BA.debugLine="Activity.SetBackgroundImage(bmpBack)";
mostCurrent._activity.SetBackgroundImageNew((android.graphics.Bitmap)(_bmpback.getObject()));
 //BA.debugLineNum = 36;BA.debugLine="ReadTable";
_readtable();
 //BA.debugLineNum = 38;BA.debugLine="SpinnerCodeL.SelectedIndex = 0";
mostCurrent._spinnercodel.setSelectedIndex((int) (0));
 //BA.debugLineNum = 39;BA.debugLine="SpinnerEmailAdh.SelectedIndex = 0";
mostCurrent._spinneremailadh.setSelectedIndex((int) (0));
 //BA.debugLineNum = 41;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 104;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 105;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 106;BA.debugLine="SQL1.Close		'if the user closes the program we c";
_sql1.Close();
 };
 //BA.debugLineNum = 108;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 100;BA.debugLine="EltsExiste = False";
_eltsexiste = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 101;BA.debugLine="ReadTable";
_readtable();
 //BA.debugLineNum = 102;BA.debugLine="End Sub";
return "";
}
public static String  _buttonvaliddepot_click() throws Exception{
 //BA.debugLineNum = 111;BA.debugLine="Sub ButtonValidDepot_Click";
 //BA.debugLineNum = 113;BA.debugLine="If EltsExiste Then";
if (_eltsexiste) { 
 //BA.debugLineNum = 114;BA.debugLine="SaveDepot";
_savedepot();
 //BA.debugLineNum = 115;BA.debugLine="StartActivity(Dashboard)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._dashboard.getObject()));
 };
 //BA.debugLineNum = 118;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 21;BA.debugLine="Private ButtonValidDepot As Button";
mostCurrent._buttonvaliddepot = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private SpinnerCodeL As Spinner";
mostCurrent._spinnercodel = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private SpinnerEmailAdh As Spinner";
mostCurrent._spinneremailadh = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim bmpBack  As Bitmap";
_bmpback = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 11;BA.debugLine="Dim SQL1 As SQL";
_sql1 = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 13;BA.debugLine="Dim EltsExiste As Boolean = False";
_eltsexiste = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return "";
}
public static String  _readtable() throws Exception{
int _row = 0;
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
int _rownumber = 0;
 //BA.debugLineNum = 44;BA.debugLine="Sub ReadTable";
 //BA.debugLineNum = 45;BA.debugLine="Dim Row As Int";
_row = 0;
 //BA.debugLineNum = 46;BA.debugLine="Dim Cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Dim RowNumber = 0 As Int";
_rownumber = (int) (0);
 //BA.debugLineNum = 48;BA.debugLine="SpinnerCodeL.Clear";
mostCurrent._spinnercodel.Clear();
 //BA.debugLineNum = 49;BA.debugLine="SpinnerEmailAdh.Clear";
mostCurrent._spinneremailadh.Clear();
 //BA.debugLineNum = 51;BA.debugLine="Cursor1 = SQL1.ExecQuery(\"SELECT codeLivre, email";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery("SELECT codeLivre, emailAdh FROM transactionLivre WHERE dateDepot IS NULL ")));
 //BA.debugLineNum = 52;BA.debugLine="If Cursor1.RowCount > 0 Then";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 53;BA.debugLine="RowNumber = Cursor1.RowCount";
_rownumber = _cursor1.getRowCount();
 //BA.debugLineNum = 54;BA.debugLine="For Row = 0 To RowNumber - 1";
{
final int step9 = 1;
final int limit9 = (int) (_rownumber-1);
_row = (int) (0) ;
for (;_row <= limit9 ;_row = _row + step9 ) {
 //BA.debugLineNum = 55;BA.debugLine="Cursor1.Position = Row";
_cursor1.setPosition(_row);
 //BA.debugLineNum = 56;BA.debugLine="SpinnerCodeL.Add(Cursor1.GetString(\"codeLivre\")";
mostCurrent._spinnercodel.Add(_cursor1.GetString("codeLivre"));
 //BA.debugLineNum = 57;BA.debugLine="SpinnerEmailAdh.Add(Cursor1.GetString(\"emailAdh";
mostCurrent._spinneremailadh.Add(_cursor1.GetString("emailAdh"));
 //BA.debugLineNum = 59;BA.debugLine="EltsExiste = True";
_eltsexiste = anywheresoftware.b4a.keywords.Common.True;
 }
};
 };
 //BA.debugLineNum = 62;BA.debugLine="Cursor1.Close";
_cursor1.Close();
 //BA.debugLineNum = 63;BA.debugLine="End Sub";
return "";
}
public static String  _savedepot() throws Exception{
String _query = "";
String _query2 = "";
int _nbre = 0;
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
boolean _exist = false;
 //BA.debugLineNum = 65;BA.debugLine="Sub SaveDepot";
 //BA.debugLineNum = 66;BA.debugLine="Dim Query, Query2 As String";
_query = "";
_query2 = "";
 //BA.debugLineNum = 67;BA.debugLine="Dim nbre As Int = 0";
_nbre = (int) (0);
 //BA.debugLineNum = 68;BA.debugLine="Dim Cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 70;BA.debugLine="Dim exist As Boolean = False";
_exist = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 72;BA.debugLine="Cursor1 = SQL1.ExecQuery2(\"SELECT * FROM transact";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery2("SELECT * FROM transactionLivre WHERE codeLivre = ? AND  emailAdh = ?",new String[]{mostCurrent._spinnercodel.getSelectedItem(),mostCurrent._spinneremailadh.getSelectedItem()})));
 //BA.debugLineNum = 73;BA.debugLine="Cursor1.Position = 0";
_cursor1.setPosition((int) (0));
 //BA.debugLineNum = 74;BA.debugLine="If Cursor1.RowCount > 0 Then";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 75;BA.debugLine="exist = True";
_exist = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 78;BA.debugLine="If exist Then";
if (_exist) { 
 //BA.debugLineNum = 80;BA.debugLine="Cursor1 = SQL1.ExecQuery2(\"SELECT nbreExple FROM";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery2("SELECT nbreExple FROM livre WHERE codeLivre = ?",new String[]{mostCurrent._spinnercodel.getSelectedItem()})));
 //BA.debugLineNum = 81;BA.debugLine="Cursor1.Position = 0";
_cursor1.setPosition((int) (0));
 //BA.debugLineNum = 82;BA.debugLine="nbre = Cursor1.GetInt(\"nbreExple\")";
_nbre = _cursor1.GetInt("nbreExple");
 //BA.debugLineNum = 84;BA.debugLine="Query = \"UPDATE transactionLivre SET dateDepot =";
_query = "UPDATE transactionLivre SET dateDepot = date('now') WHERE codeLivre = ? AND  emailAdh = ?";
 //BA.debugLineNum = 85;BA.debugLine="SQL1.ExecNonQuery2(Query, Array As String(Spinne";
_sql1.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{mostCurrent._spinnercodel.getSelectedItem(),mostCurrent._spinneremailadh.getSelectedItem()}));
 //BA.debugLineNum = 88;BA.debugLine="Query2 = \"UPDATE livre SET nbreExple = \"& (nbre";
_query2 = "UPDATE livre SET nbreExple = "+BA.NumberToString((_nbre+1))+" WHERE codeLivre = ? ";
 //BA.debugLineNum = 89;BA.debugLine="SQL1.ExecNonQuery2(Query2, Array As String(Spinn";
_sql1.ExecNonQuery2(_query2,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{mostCurrent._spinnercodel.getSelectedItem()}));
 //BA.debugLineNum = 91;BA.debugLine="ToastMessageShow(\"Dépôt validé avec succès !\", T";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Dépôt validé avec succès !"),anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 93;BA.debugLine="ToastMessageShow(\"Aucun emprunt de ce type !\", T";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Aucun emprunt de ce type !"),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return "";
}
}
