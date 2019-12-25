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

public class addadhe extends Activity implements B4AActivity{
	public static addadhe mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.sqlitelight1", "b4a.sqlitelight1.addadhe");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (addadhe).");
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
		activityBA = new BA(this, layout, processBA, "b4a.sqlitelight1", "b4a.sqlitelight1.addadhe");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.sqlitelight1.addadhe", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (addadhe) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (addadhe) Resume **");
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
		return addadhe.class;
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
        BA.LogInfo("** Activity (addadhe) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            addadhe mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (addadhe) Resume **");
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
public anywheresoftware.b4a.objects.ButtonWrapper _buttonvaliderinsc = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextadresse = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextdatenai = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextemailadh = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextnomadh = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextprenomadh = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextteladh = null;
public b4a.sqlitelight1.main _main = null;
public b4a.sqlitelight1.addbook _addbook = null;
public b4a.sqlitelight1.emprunt _emprunt = null;
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
 //BA.debugLineNum = 33;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 35;BA.debugLine="Activity.LoadLayout(\"addAdhe\")";
mostCurrent._activity.LoadLayout("addAdhe",mostCurrent.activityBA);
 //BA.debugLineNum = 37;BA.debugLine="SQL1.Initialize(File.DirInternal, \"gbiblio.db\", F";
_sql1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"gbiblio.db",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 39;BA.debugLine="bmpBack.Initialize(File.DirAssets, \"background.jp";
_bmpback.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"background.jpg");
 //BA.debugLineNum = 40;BA.debugLine="Activity.SetBackgroundImage(bmpBack)";
mostCurrent._activity.SetBackgroundImageNew((android.graphics.Bitmap)(_bmpback.getObject()));
 //BA.debugLineNum = 42;BA.debugLine="EditTextAdresse.Text = \"\"";
mostCurrent._edittextadresse.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 43;BA.debugLine="EditTextDateNai.Text = \"\"";
mostCurrent._edittextdatenai.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 44;BA.debugLine="EditTextEmailAdh.Text = \"\"";
mostCurrent._edittextemailadh.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 45;BA.debugLine="EditTextNomAdh.Text = \"\"";
mostCurrent._edittextnomadh.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 46;BA.debugLine="EditTextPrenomAdh.Text = \"\"";
mostCurrent._edittextprenomadh.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 47;BA.debugLine="EditTextTelAdh.Text = \"\"";
mostCurrent._edittextteladh.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 49;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 83;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 84;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 85;BA.debugLine="SQL1.Close		'if the user closes the program we c";
_sql1.Close();
 };
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 74;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 75;BA.debugLine="EditTextAdresse.Text = \"\"";
mostCurrent._edittextadresse.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 76;BA.debugLine="EditTextDateNai.Text = \"\"";
mostCurrent._edittextdatenai.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 77;BA.debugLine="EditTextEmailAdh.Text = \"\"";
mostCurrent._edittextemailadh.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 78;BA.debugLine="EditTextNomAdh.Text = \"\"";
mostCurrent._edittextnomadh.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 79;BA.debugLine="EditTextPrenomAdh.Text = \"\"";
mostCurrent._edittextprenomadh.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 80;BA.debugLine="EditTextTelAdh.Text = \"\"";
mostCurrent._edittextteladh.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
public static String  _buttonvaliderinsc_click() throws Exception{
 //BA.debugLineNum = 90;BA.debugLine="Sub ButtonValiderInsc_Click";
 //BA.debugLineNum = 91;BA.debugLine="If EditTextEmailAdh.Text.Trim.Length  == 0 Or Edi";
if (mostCurrent._edittextemailadh.getText().trim().length()==0 || mostCurrent._edittextadresse.getText().trim().length()==0 || mostCurrent._edittextteladh.getText().trim().length()==0) { 
 //BA.debugLineNum = 92;BA.debugLine="Msgbox(\"Entrez toutes les valeurs svp !!!\",\"Enre";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Entrez toutes les valeurs svp !!!"),BA.ObjectToCharSequence("Enregistrement livre"),mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 94;BA.debugLine="CheckCodeExist";
_checkcodeexist();
 //BA.debugLineNum = 95;BA.debugLine="If codeExist Then";
if (_codeexist) { 
 //BA.debugLineNum = 96;BA.debugLine="ToastMessageShow(\"Cet email existe déjà, veuill";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Cet email existe déjà, veuillez utilisez un autre email pour l'inscription !!!"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 97;BA.debugLine="EditTextEmailAdh.Text = \"\"";
mostCurrent._edittextemailadh.setText(BA.ObjectToCharSequence(""));
 }else {
 //BA.debugLineNum = 99;BA.debugLine="SaveAdh";
_saveadh();
 //BA.debugLineNum = 100;BA.debugLine="StartActivity(Dashboard)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._dashboard.getObject()));
 };
 };
 //BA.debugLineNum = 103;BA.debugLine="End Sub";
return "";
}
public static String  _checkcodeexist() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
String _query = "";
 //BA.debugLineNum = 51;BA.debugLine="Sub CheckCodeExist";
 //BA.debugLineNum = 52;BA.debugLine="Dim Cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Dim Query As String";
_query = "";
 //BA.debugLineNum = 55;BA.debugLine="Query = \"SELECT email FROM adherant WHERE email =";
_query = "SELECT email FROM adherant WHERE email = ?";
 //BA.debugLineNum = 56;BA.debugLine="Cursor1 = SQL1.ExecQuery2(Query, Array As String";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery2(_query,new String[]{mostCurrent._edittextemailadh.getText()})));
 //BA.debugLineNum = 58;BA.debugLine="If Cursor1.RowCount > 0 Then";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 59;BA.debugLine="codeExist = True";
_codeexist = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 61;BA.debugLine="Cursor1.Close";
_cursor1.Close();
 //BA.debugLineNum = 63;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 19;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 23;BA.debugLine="Private ButtonValiderInsc As Button";
mostCurrent._buttonvaliderinsc = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private EditTextAdresse As EditText";
mostCurrent._edittextadresse = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private EditTextDateNai As EditText";
mostCurrent._edittextdatenai = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private EditTextEmailAdh As EditText";
mostCurrent._edittextemailadh = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private EditTextNomAdh As EditText";
mostCurrent._edittextnomadh = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private EditTextPrenomAdh As EditText";
mostCurrent._edittextprenomadh = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private EditTextTelAdh As EditText";
mostCurrent._edittextteladh = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 31;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="Dim bmpBack  As Bitmap";
_bmpback = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Dim SQL1 As SQL";
_sql1 = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 14;BA.debugLine="Dim codeExist As Boolean = False";
_codeexist = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 17;BA.debugLine="End Sub";
return "";
}
public static String  _saveadh() throws Exception{
String _query = "";
 //BA.debugLineNum = 65;BA.debugLine="Sub SaveAdh";
 //BA.debugLineNum = 66;BA.debugLine="Dim Query As String";
_query = "";
 //BA.debugLineNum = 67;BA.debugLine="Query = \"INSERT INTO adherant VALUES (?, ?, ?, ?,";
_query = "INSERT INTO adherant VALUES (?, ?, ?, ?, ?,?)";
 //BA.debugLineNum = 68;BA.debugLine="SQL1.ExecNonQuery2(Query, Array As String(EditTex";
_sql1.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{mostCurrent._edittextemailadh.getText(),mostCurrent._edittextnomadh.getText(),mostCurrent._edittextprenomadh.getText(),mostCurrent._edittextdatenai.getText(),mostCurrent._edittextadresse.getText(),mostCurrent._edittextteladh.getText()}));
 //BA.debugLineNum = 70;BA.debugLine="ToastMessageShow(\"Adhérant ajouté avec succès !\",";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Adhérant ajouté avec succès !"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 72;BA.debugLine="End Sub";
return "";
}
}
