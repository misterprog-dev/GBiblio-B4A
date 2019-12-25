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

public class emprunt extends Activity implements B4AActivity{
	public static emprunt mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.sqlitelight1", "b4a.sqlitelight1.emprunt");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (emprunt).");
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
		activityBA = new BA(this, layout, processBA, "b4a.sqlitelight1", "b4a.sqlitelight1.emprunt");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.sqlitelight1.emprunt", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (emprunt) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (emprunt) Resume **");
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
		return emprunt.class;
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
        BA.LogInfo("** Activity (emprunt) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            emprunt mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (emprunt) Resume **");
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
public static boolean _codeexist = false;
public anywheresoftware.b4a.objects.ButtonWrapper _buttonvalidemprunt = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinnercodel = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinneremailadh = null;
public b4a.sqlitelight1.main _main = null;
public b4a.sqlitelight1.addbook _addbook = null;
public b4a.sqlitelight1.addadhe _addadhe = null;
public b4a.sqlitelight1.depot _depot = null;
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
 //BA.debugLineNum = 27;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 29;BA.debugLine="Activity.LoadLayout(\"Emprunt\")";
mostCurrent._activity.LoadLayout("Emprunt",mostCurrent.activityBA);
 //BA.debugLineNum = 30;BA.debugLine="SQL1.Initialize(File.DirInternal, \"gbiblio.db\", F";
_sql1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"gbiblio.db",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 32;BA.debugLine="bmpBack.Initialize(File.DirAssets, \"background.jp";
_bmpback.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"background.jpg");
 //BA.debugLineNum = 33;BA.debugLine="Activity.SetBackgroundImage(bmpBack)";
mostCurrent._activity.SetBackgroundImageNew((android.graphics.Bitmap)(_bmpback.getObject()));
 //BA.debugLineNum = 35;BA.debugLine="ReadTable";
_readtable();
 //BA.debugLineNum = 37;BA.debugLine="SpinnerCodeL.SelectedIndex = 0";
mostCurrent._spinnercodel.setSelectedIndex((int) (0));
 //BA.debugLineNum = 38;BA.debugLine="SpinnerEmailAdh.SelectedIndex = 0";
mostCurrent._spinneremailadh.setSelectedIndex((int) (0));
 //BA.debugLineNum = 40;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 117;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 118;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 119;BA.debugLine="SQL1.Close		'if the user closes the program we c";
_sql1.Close();
 };
 //BA.debugLineNum = 121;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 111;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 113;BA.debugLine="End Sub";
return "";
}
public static String  _buttonvalidemprunt_click() throws Exception{
 //BA.debugLineNum = 124;BA.debugLine="Sub ButtonValidEmprunt_Click";
 //BA.debugLineNum = 125;BA.debugLine="CheckExistEmprunt";
_checkexistemprunt();
 //BA.debugLineNum = 126;BA.debugLine="If codeExist Then";
if (_codeexist) { 
 //BA.debugLineNum = 127;BA.debugLine="ToastMessageShow(\"Vous avez emprunté un livre qu";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Vous avez emprunté un livre que vous n'avez pas encore déposé !!!"),anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 129;BA.debugLine="SaveEmprunt";
_saveemprunt();
 //BA.debugLineNum = 130;BA.debugLine="StartActivity(Dashboard)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._dashboard.getObject()));
 };
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return "";
}
public static String  _checkexistemprunt() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
String _query = "";
 //BA.debugLineNum = 71;BA.debugLine="Sub CheckExistEmprunt";
 //BA.debugLineNum = 72;BA.debugLine="Dim Cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Dim Query As String";
_query = "";
 //BA.debugLineNum = 74;BA.debugLine="codeExist = False";
_codeexist = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 76;BA.debugLine="Query = \"SELECT emailAdh FROM transactionLivre WH";
_query = "SELECT emailAdh FROM transactionLivre WHERE emailAdh = ? AND dateDepot IS NULL";
 //BA.debugLineNum = 77;BA.debugLine="Cursor1 = SQL1.ExecQuery2(Query, Array As String";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery2(_query,new String[]{mostCurrent._spinneremailadh.getSelectedItem()})));
 //BA.debugLineNum = 78;BA.debugLine="Cursor1.Position = 0";
_cursor1.setPosition((int) (0));
 //BA.debugLineNum = 79;BA.debugLine="If Cursor1.RowCount > 0 Then";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 80;BA.debugLine="codeExist = True";
_codeexist = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 82;BA.debugLine="Cursor1.Close";
_cursor1.Close();
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 20;BA.debugLine="Private ButtonValidEmprunt As Button";
mostCurrent._buttonvalidemprunt = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private SpinnerCodeL As Spinner";
mostCurrent._spinnercodel = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private SpinnerEmailAdh As Spinner";
mostCurrent._spinneremailadh = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim bmpBack  As Bitmap";
_bmpback = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 11;BA.debugLine="Dim SQL1 As SQL";
_sql1 = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 12;BA.debugLine="Dim codeExist As Boolean = False";
_codeexist = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public static String  _readtable() throws Exception{
int _row = 0;
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
int _rownumber = 0;
 //BA.debugLineNum = 42;BA.debugLine="Sub ReadTable";
 //BA.debugLineNum = 43;BA.debugLine="Dim Row As Int";
_row = 0;
 //BA.debugLineNum = 44;BA.debugLine="Dim Cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Dim RowNumber = 0 As Int";
_rownumber = (int) (0);
 //BA.debugLineNum = 48;BA.debugLine="Cursor1 = SQL1.ExecQuery(\"SELECT email FROM adher";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery("SELECT email FROM adherant")));
 //BA.debugLineNum = 49;BA.debugLine="If Cursor1.RowCount > 0 Then						'check if entri";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 50;BA.debugLine="RowNumber = Cursor1.RowCount					'set the row co";
_rownumber = _cursor1.getRowCount();
 //BA.debugLineNum = 51;BA.debugLine="For Row = 0 To RowNumber - 1";
{
final int step7 = 1;
final int limit7 = (int) (_rownumber-1);
_row = (int) (0) ;
for (;_row <= limit7 ;_row = _row + step7 ) {
 //BA.debugLineNum = 52;BA.debugLine="Cursor1.Position = Row";
_cursor1.setPosition(_row);
 //BA.debugLineNum = 53;BA.debugLine="SpinnerEmailAdh.Add(Cursor1.GetString(\"email\"))";
mostCurrent._spinneremailadh.Add(_cursor1.GetString("email"));
 }
};
 };
 //BA.debugLineNum = 56;BA.debugLine="Cursor1.Close";
_cursor1.Close();
 //BA.debugLineNum = 59;BA.debugLine="Cursor1 = SQL1.ExecQuery(\"SELECT codeLivre FROM l";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery("SELECT codeLivre FROM livre")));
 //BA.debugLineNum = 60;BA.debugLine="If Cursor1.RowCount > 0 Then						'check if entri";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 61;BA.debugLine="RowNumber = Cursor1.RowCount					'set the row co";
_rownumber = _cursor1.getRowCount();
 //BA.debugLineNum = 62;BA.debugLine="For Row = 0 To RowNumber - 1";
{
final int step16 = 1;
final int limit16 = (int) (_rownumber-1);
_row = (int) (0) ;
for (;_row <= limit16 ;_row = _row + step16 ) {
 //BA.debugLineNum = 63;BA.debugLine="Cursor1.Position = Row";
_cursor1.setPosition(_row);
 //BA.debugLineNum = 64;BA.debugLine="SpinnerCodeL.Add(Cursor1.GetString(\"codeLivre\")";
mostCurrent._spinnercodel.Add(_cursor1.GetString("codeLivre"));
 }
};
 };
 //BA.debugLineNum = 67;BA.debugLine="Cursor1.Close";
_cursor1.Close();
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
return "";
}
public static String  _saveemprunt() throws Exception{
String _query = "";
String _query2 = "";
int _nbre = 0;
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
 //BA.debugLineNum = 86;BA.debugLine="Sub SaveEmprunt";
 //BA.debugLineNum = 87;BA.debugLine="Dim Query, Query2 As String";
_query = "";
_query2 = "";
 //BA.debugLineNum = 88;BA.debugLine="Dim nbre As Int = 0";
_nbre = (int) (0);
 //BA.debugLineNum = 89;BA.debugLine="Dim Cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 91;BA.debugLine="Cursor1 = SQL1.ExecQuery(\"SELECT nbreExple FROM l";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery("SELECT nbreExple FROM livre WHERE codeLivre = '"+mostCurrent._spinnercodel.getSelectedItem()+"'")));
 //BA.debugLineNum = 92;BA.debugLine="Cursor1.Position = 0";
_cursor1.setPosition((int) (0));
 //BA.debugLineNum = 93;BA.debugLine="nbre = Cursor1.GetInt(\"nbreExple\")";
_nbre = _cursor1.GetInt("nbreExple");
 //BA.debugLineNum = 95;BA.debugLine="If nbre == 0 Then";
if (_nbre==0) { 
 //BA.debugLineNum = 96;BA.debugLine="ToastMessageShow(\"Quantité de livre insuffisante";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Quantité de livre insuffisante !"),anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 98;BA.debugLine="Query = \"INSERT INTO transactionLivre VALUES (NU";
_query = "INSERT INTO transactionLivre VALUES (NULL, ?, ?, date('now'), NULL)";
 //BA.debugLineNum = 100;BA.debugLine="SQL1.ExecNonQuery2(Query, Array As String(Spinne";
_sql1.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{mostCurrent._spinnercodel.getSelectedItem(),mostCurrent._spinneremailadh.getSelectedItem()}));
 //BA.debugLineNum = 103;BA.debugLine="Query2 = \"UPDATE livre SET nbreExple = \" & (nbre";
_query2 = "UPDATE livre SET nbreExple = "+BA.NumberToString((_nbre-1))+" WHERE codeLivre = ? ";
 //BA.debugLineNum = 104;BA.debugLine="SQL1.ExecNonQuery2(Query2, Array As String(Spinn";
_sql1.ExecNonQuery2(_query2,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{mostCurrent._spinnercodel.getSelectedItem()}));
 //BA.debugLineNum = 106;BA.debugLine="ToastMessageShow(\"Emprunt validé avec succès !\",";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Emprunt validé avec succès !"),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 109;BA.debugLine="End Sub";
return "";
}
}
