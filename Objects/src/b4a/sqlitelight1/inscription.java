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

public class inscription extends Activity implements B4AActivity{
	public static inscription mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.sqlitelight1", "b4a.sqlitelight1.inscription");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (inscription).");
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
		activityBA = new BA(this, layout, processBA, "b4a.sqlitelight1", "b4a.sqlitelight1.inscription");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.sqlitelight1.inscription", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (inscription) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (inscription) Resume **");
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
		return inscription.class;
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
        BA.LogInfo("** Activity (inscription) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            inscription mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (inscription) Resume **");
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
public static anywheresoftware.b4a.sql.SQL _sql1 = null;
public static boolean _emailexist = false;
public static boolean _codetexist = false;
public anywheresoftware.b4a.objects.ButtonWrapper _buttonsignin = null;
public anywheresoftware.b4a.objects.ButtonWrapper _buttonsignup = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextcodetuteur = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextconfmdp = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextemail = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextmdp = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextnomprenom = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittexttel = null;
public b4a.sqlitelight1.main _main = null;
public b4a.sqlitelight1.addbook _addbook = null;
public b4a.sqlitelight1.addadhe _addadhe = null;
public b4a.sqlitelight1.emprunt _emprunt = null;
public b4a.sqlitelight1.depot _depot = null;
public b4a.sqlitelight1.forgetmdp _forgetmdp = null;
public b4a.sqlitelight1.gereradh _gereradh = null;
public b4a.sqlitelight1.gererbook _gererbook = null;
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
 //BA.debugLineNum = 30;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 32;BA.debugLine="Activity.LoadLayout(\"Inscription\")";
mostCurrent._activity.LoadLayout("Inscription",mostCurrent.activityBA);
 //BA.debugLineNum = 34;BA.debugLine="SQL1.Initialize(File.DirInternal, \"gbiblio.db\", F";
_sql1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"gbiblio.db",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 36;BA.debugLine="EditTextCodeTuteur.Text = \"\"";
mostCurrent._edittextcodetuteur.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 37;BA.debugLine="EditTextConfMDP.Text = \"\"";
mostCurrent._edittextconfmdp.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 38;BA.debugLine="EditTextEmail.Text = \"\"";
mostCurrent._edittextemail.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 39;BA.debugLine="EditTextMdp.Text = \"\"";
mostCurrent._edittextmdp.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 40;BA.debugLine="EditTextNomPrenom.Text = \"\"";
mostCurrent._edittextnomprenom.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 41;BA.debugLine="EditTextTel.Text = \"\"";
mostCurrent._edittexttel.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 43;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 92;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 93;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 94;BA.debugLine="SQL1.Close		'if the user closes the program we c";
_sql1.Close();
 };
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 83;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 84;BA.debugLine="EditTextCodeTuteur.Text = \"\"";
mostCurrent._edittextcodetuteur.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 85;BA.debugLine="EditTextConfMDP.Text = \"\"";
mostCurrent._edittextconfmdp.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 86;BA.debugLine="EditTextEmail.Text = \"\"";
mostCurrent._edittextemail.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 87;BA.debugLine="EditTextMdp.Text = \"\"";
mostCurrent._edittextmdp.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 88;BA.debugLine="EditTextNomPrenom.Text = \"\"";
mostCurrent._edittextnomprenom.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 89;BA.debugLine="EditTextTel.Text = \"\"";
mostCurrent._edittexttel.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return "";
}
public static String  _buttonsignin_click() throws Exception{
 //BA.debugLineNum = 125;BA.debugLine="Sub ButtonSignIn_Click";
 //BA.debugLineNum = 126;BA.debugLine="StartActivity(Connexion)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._connexion.getObject()));
 //BA.debugLineNum = 127;BA.debugLine="End Sub";
return "";
}
public static String  _buttonsignup_click() throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub ButtonSignUp_Click";
 //BA.debugLineNum = 100;BA.debugLine="If EditTextNomPrenom.Text.Trim.Length  == 0 Or Ed";
if (mostCurrent._edittextnomprenom.getText().trim().length()==0 || mostCurrent._edittextemail.getText().trim().length()==0 || mostCurrent._edittextcodetuteur.getText().trim().length()==0 || mostCurrent._edittexttel.getText().trim().length()==0 || mostCurrent._edittextmdp.getText().trim().length()==0) { 
 //BA.debugLineNum = 101;BA.debugLine="Msgbox(\"Entrez toutes les valeurs svp !!!\",\"Insc";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Entrez toutes les valeurs svp !!!"),BA.ObjectToCharSequence("Inscription agent"),mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 103;BA.debugLine="If EditTextMdp.Text <> EditTextConfMDP.Text Then";
if ((mostCurrent._edittextmdp.getText()).equals(mostCurrent._edittextconfmdp.getText()) == false) { 
 //BA.debugLineNum = 104;BA.debugLine="ToastMessageShow(\"Mots de passe différents !!!\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Mots de passe différents !!!"),anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 106;BA.debugLine="CheckEmail";
_checkemail();
 //BA.debugLineNum = 107;BA.debugLine="If EmailExist Then";
if (_emailexist) { 
 //BA.debugLineNum = 108;BA.debugLine="ToastMessageShow(\"Ce email existe déjà, veuill";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Ce email existe déjà, veuillez utilisez un autre mail pour votre inscription !!!"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 109;BA.debugLine="EditTextEmail.Text = \"\"";
mostCurrent._edittextemail.setText(BA.ObjectToCharSequence(""));
 }else {
 //BA.debugLineNum = 111;BA.debugLine="CheckCodeT";
_checkcodet();
 //BA.debugLineNum = 112;BA.debugLine="If codeTExist == False And EditTextCodeTuteur.";
if (_codetexist==anywheresoftware.b4a.keywords.Common.False && (mostCurrent._edittextcodetuteur.getText()).equals("<@>12345<@/>") == false) { 
 //BA.debugLineNum = 113;BA.debugLine="ToastMessageShow(\"Ce code tuteur n'existe pas";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Ce code tuteur n'existe pas, veuillez utilisez un code tuteur valide pour votre inscription !!!"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 114;BA.debugLine="EditTextCodeTuteur.Text = \"\"";
mostCurrent._edittextcodetuteur.setText(BA.ObjectToCharSequence(""));
 }else {
 //BA.debugLineNum = 116;BA.debugLine="SaveInfoPerson";
_saveinfoperson();
 //BA.debugLineNum = 117;BA.debugLine="StartActivity(Connexion)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._connexion.getObject()));
 };
 };
 };
 };
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public static String  _checkcodet() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
String _query = "";
 //BA.debugLineNum = 59;BA.debugLine="Sub CheckCodeT";
 //BA.debugLineNum = 60;BA.debugLine="Dim Cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Dim Query As String";
_query = "";
 //BA.debugLineNum = 63;BA.debugLine="Query = \"SELECT codeT FROM bibliothecaire WHERE c";
_query = "SELECT codeT FROM bibliothecaire WHERE codeT = ?";
 //BA.debugLineNum = 64;BA.debugLine="Cursor1 = SQL1.ExecQuery2(Query, Array As String";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery2(_query,new String[]{mostCurrent._edittextcodetuteur.getText()})));
 //BA.debugLineNum = 66;BA.debugLine="If Cursor1.RowCount == 0 Then						'check if entr";
if (_cursor1.getRowCount()==0) { 
 //BA.debugLineNum = 67;BA.debugLine="codeTExist = False";
_codetexist = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 70;BA.debugLine="Cursor1.Close														'close the cursor, we";
_cursor1.Close();
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
return "";
}
public static String  _checkemail() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
String _query = "";
 //BA.debugLineNum = 45;BA.debugLine="Sub CheckEmail";
 //BA.debugLineNum = 46;BA.debugLine="Dim Cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Dim Query As String";
_query = "";
 //BA.debugLineNum = 49;BA.debugLine="Query = \"SELECT email FROM bibliothecaire WHERE e";
_query = "SELECT email FROM bibliothecaire WHERE email = ?";
 //BA.debugLineNum = 50;BA.debugLine="Cursor1 = SQL1.ExecQuery2(Query, Array As String";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery2(_query,new String[]{mostCurrent._edittextemail.getText()})));
 //BA.debugLineNum = 52;BA.debugLine="If Cursor1.RowCount > 0 Then";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 53;BA.debugLine="EmailExist = True";
_emailexist = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 55;BA.debugLine="Cursor1.Close";
_cursor1.Close();
 //BA.debugLineNum = 56;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 20;BA.debugLine="Private ButtonSignIn As Button";
mostCurrent._buttonsignin = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private ButtonSignUp As Button";
mostCurrent._buttonsignup = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private EditTextCodeTuteur As EditText";
mostCurrent._edittextcodetuteur = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private EditTextConfMDP As EditText";
mostCurrent._edittextconfmdp = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private EditTextEmail As EditText";
mostCurrent._edittextemail = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private EditTextMdp As EditText";
mostCurrent._edittextmdp = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private EditTextNomPrenom As EditText";
mostCurrent._edittextnomprenom = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private EditTextTel As EditText";
mostCurrent._edittexttel = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 28;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim SQL1 As SQL";
_sql1 = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 11;BA.debugLine="Dim EmailExist As Boolean = False";
_emailexist = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 12;BA.debugLine="Dim codeTExist As Boolean = True";
_codetexist = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public static String  _saveinfoperson() throws Exception{
String _query = "";
 //BA.debugLineNum = 73;BA.debugLine="Sub SaveInfoPerson";
 //BA.debugLineNum = 74;BA.debugLine="Dim Query As String";
_query = "";
 //BA.debugLineNum = 76;BA.debugLine="Query = \"INSERT INTO bibliothecaire VALUES (?, ?,";
_query = "INSERT INTO bibliothecaire VALUES (?, ?, ?, ?, ?)";
 //BA.debugLineNum = 77;BA.debugLine="SQL1.ExecNonQuery2(Query, Array As String(EditTex";
_sql1.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{mostCurrent._edittextemail.getText(),mostCurrent._edittextnomprenom.getText(),mostCurrent._edittextcodetuteur.getText(),mostCurrent._edittexttel.getText(),mostCurrent._edittextmdp.getText()}));
 //BA.debugLineNum = 79;BA.debugLine="ToastMessageShow(\"Gestionnaire ajouté avec succès";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Gestionnaire ajouté avec succès"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
}
