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

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.sqlitelight1", "b4a.sqlitelight1.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
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
		activityBA = new BA(this, layout, processBA, "b4a.sqlitelight1", "b4a.sqlitelight1.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.sqlitelight1.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
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
		return main.class;
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
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
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
public static anywheresoftware.b4a.objects.Timer _timer = null;
public b4a.sqlitelight1.addbook _addbook = null;
public b4a.sqlitelight1.addadhe _addadhe = null;
public b4a.sqlitelight1.emprunt _emprunt = null;
public b4a.sqlitelight1.depot _depot = null;
public b4a.sqlitelight1.forgetmdp _forgetmdp = null;
public b4a.sqlitelight1.gereradh _gereradh = null;
public b4a.sqlitelight1.gererbook _gererbook = null;
public b4a.sqlitelight1.inscription _inscription = null;
public b4a.sqlitelight1.connexion _connexion = null;
public b4a.sqlitelight1.dashboard _dashboard = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (addbook.mostCurrent != null);
vis = vis | (addadhe.mostCurrent != null);
vis = vis | (emprunt.mostCurrent != null);
vis = vis | (depot.mostCurrent != null);
vis = vis | (forgetmdp.mostCurrent != null);
vis = vis | (gereradh.mostCurrent != null);
vis = vis | (gererbook.mostCurrent != null);
vis = vis | (inscription.mostCurrent != null);
vis = vis | (connexion.mostCurrent != null);
vis = vis | (dashboard.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 25;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 26;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 28;BA.debugLine="If File.Exists(File.DirInternal, \"gbiblio.db\") =";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"gbiblio.db")==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 30;BA.debugLine="SQL1.Initialize(File.DirInternal, \"gbiblio.db\",";
_sql1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"gbiblio.db",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 32;BA.debugLine="CreateTable";
_createtable();
 }else {
 //BA.debugLineNum = 35;BA.debugLine="SQL1.Initialize(File.DirInternal, \"gbiblio.db\",";
_sql1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"gbiblio.db",anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 39;BA.debugLine="Activity.LoadLayout(\"Main\")";
mostCurrent._activity.LoadLayout("Main",mostCurrent.activityBA);
 //BA.debugLineNum = 41;BA.debugLine="timer.Initialize(\"timer\", 2000)";
_timer.Initialize(processBA,"timer",(long) (2000));
 //BA.debugLineNum = 42;BA.debugLine="timer.Enabled = True";
_timer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 50;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 51;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 52;BA.debugLine="SQL1.Close";
_sql1.Close();
 };
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 46;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static String  _createtable() throws Exception{
String _query = "";
String _query2 = "";
String _query3 = "";
String _query4 = "";
String _query5 = "";
String _q = "";
 //BA.debugLineNum = 56;BA.debugLine="Sub CreateTable";
 //BA.debugLineNum = 57;BA.debugLine="Dim Query, Query2, Query3 , Query4 , Query5, Q As";
_query = "";
_query2 = "";
_query3 = "";
_query4 = "";
_query5 = "";
_q = "";
 //BA.debugLineNum = 59;BA.debugLine="Query = \"CREATE TABLE bibliothecaire (email TEXT";
_query = "CREATE TABLE bibliothecaire (email TEXT PRIMARY KEY, nom TEXT, codeT TEXT, tel TEXT, mdp TEXT)";
 //BA.debugLineNum = 60;BA.debugLine="SQL1.ExecNonQuery(Query)";
_sql1.ExecNonQuery(_query);
 //BA.debugLineNum = 62;BA.debugLine="Query2 = \"INSERT INTO bibliothecaire VALUES ('sou";
_query2 = "INSERT INTO bibliothecaire VALUES ('soumdiakite182@gmail.com', 'DIAKITE SOUMAILA', 'mister_prog@', '67988610/56058826','mister_prog@')";
 //BA.debugLineNum = 63;BA.debugLine="SQL1.ExecNonQuery(Query2)";
_sql1.ExecNonQuery(_query2);
 //BA.debugLineNum = 65;BA.debugLine="Query3 = \"CREATE TABLE livre (codeLivre TEXT PRIM";
_query3 = "CREATE TABLE livre (codeLivre TEXT PRIMARY KEY, nomAuteur TEXT, titreLivre TEXT, datePub DATE, nbreExple INT, resume TEXT)";
 //BA.debugLineNum = 66;BA.debugLine="SQL1.ExecNonQuery(Query3)";
_sql1.ExecNonQuery(_query3);
 //BA.debugLineNum = 68;BA.debugLine="Query4 = \"CREATE TABLE adherant (email TEXT PRIMA";
_query4 = "CREATE TABLE adherant (email TEXT PRIMARY KEY, nomAdh TEXT, prenomAdh TEXT,datenaiss DATE, adresse TEXT, tel TEXT)";
 //BA.debugLineNum = 69;BA.debugLine="SQL1.ExecNonQuery(Query4)";
_sql1.ExecNonQuery(_query4);
 //BA.debugLineNum = 71;BA.debugLine="Query5 = \"CREATE TABLE transactionLivre (id INT P";
_query5 = "CREATE TABLE transactionLivre (id INT PRIMARY KEY, codeLivre TEXT, emailAdh TEXT, dateEmprunt DATETIME, dateDepot DATETIME DEFAULT NULL)";
 //BA.debugLineNum = 72;BA.debugLine="SQL1.ExecNonQuery(Query5)";
_sql1.ExecNonQuery(_query5);
 //BA.debugLineNum = 75;BA.debugLine="Q = \"INSERT INTO adherant VALUES('jaredbruce@gmai";
_q = "INSERT INTO adherant VALUES('jaredbruce@gmail.com','JARED','BRUCE','2012-05-05','1866 John Prairie Kimfort, CO 71609','213.720.6303')";
 //BA.debugLineNum = 76;BA.debugLine="SQL1.ExecNonQuery(Q)";
_sql1.ExecNonQuery(_q);
 //BA.debugLineNum = 77;BA.debugLine="Q = \"INSERT INTO adherant VALUES('samantha68@gmai";
_q = "INSERT INTO adherant VALUES('samantha68@gmail.com','SAMANTHA','ALEHOU','1999-12-05','49441 Powell Mountain Suite 495 Richardsonshire, HI 61302','(018)768-8493')";
 //BA.debugLineNum = 78;BA.debugLine="SQL1.ExecNonQuery(Q)";
_sql1.ExecNonQuery(_q);
 //BA.debugLineNum = 79;BA.debugLine="Q = \"INSERT INTO adherant VALUES('irivera@foster.";
_q = "INSERT INTO adherant VALUES('irivera@foster.org','KIMBERLY','PETERS','2003-03-13','5770 Lori Lock Timfort, LA 60831','+1-550-322-6595')";
 //BA.debugLineNum = 80;BA.debugLine="SQL1.ExecNonQuery(Q)";
_sql1.ExecNonQuery(_q);
 //BA.debugLineNum = 81;BA.debugLine="Q = \"INSERT INTO adherant VALUES('ryan46@yahoo.co";
_q = "INSERT INTO adherant VALUES('ryan46@yahoo.com','JENNIFER','GONZALEZ','1997-12-14','919 Henderson Loop Smithborough, WA 58397','(557)823-6715')";
 //BA.debugLineNum = 82;BA.debugLine="SQL1.ExecNonQuery(Q)";
_sql1.ExecNonQuery(_q);
 //BA.debugLineNum = 83;BA.debugLine="Q = \"INSERT INTO adherant VALUES('micheal94@hotma";
_q = "INSERT INTO adherant VALUES('micheal94@hotmail.com','JOANNA','STONE','1998-12-01','788 Perez Groves Smithfort, KS 45891','150-288-9842x48354')";
 //BA.debugLineNum = 84;BA.debugLine="SQL1.ExecNonQuery(Q)";
_sql1.ExecNonQuery(_q);
 //BA.debugLineNum = 85;BA.debugLine="Q = \"INSERT INTO adherant VALUES('lewistiffany@gm";
_q = "INSERT INTO adherant VALUES('lewistiffany@gmail.com','ROBERT','JOSEPH','2000-01-01','82921 Hughes Station West Melanieberg, MO 06736','814.422.2485')";
 //BA.debugLineNum = 86;BA.debugLine="SQL1.ExecNonQuery(Q)";
_sql1.ExecNonQuery(_q);
 //BA.debugLineNum = 87;BA.debugLine="Q = \"INSERT INTO adherant VALUES('nicholas36@gmai";
_q = "INSERT INTO adherant VALUES('nicholas36@gmail.com','BROOKE','HOOPER','1999-11-26','86390 Jennifer Loaf Wendytown, CO 98757','105.279.1298')";
 //BA.debugLineNum = 88;BA.debugLine="SQL1.ExecNonQuery(Q)";
_sql1.ExecNonQuery(_q);
 //BA.debugLineNum = 90;BA.debugLine="Q = \"INSERT INTO livre VALUES('1','Everina','CRIS";
_q = "INSERT INTO livre VALUES('1','Everina','CRIS, LAURENT GAUDE',' 2001-08-12', 3,'Cris est un roman polyphonique publié en 2001. Il se déroule pendant la Première Guerre Mondiale et fait entendre les voix des hommes du front. I- La relève de la vieille garde. Jules quitte le front')";
 //BA.debugLineNum = 91;BA.debugLine="SQL1.ExecNonQuery(Q)";
_sql1.ExecNonQuery(_q);
 //BA.debugLineNum = 92;BA.debugLine="Q = \"INSERT INTO livre VALUES('2','Molière','Dom";
_q = "INSERT INTO livre VALUES('2','Molière','Dom Juan','2007-03-23',4,'Comme Tartuffe, Dom Juan est une pièce on ne peut comprendre en dehors du contexte politique et religieux du début du règne de Louis XIV.')";
 //BA.debugLineNum = 93;BA.debugLine="SQL1.ExecNonQuery(Q)";
_sql1.ExecNonQuery(_q);
 //BA.debugLineNum = 94;BA.debugLine="Q = \"INSERT INTO livre VALUES('3','Rousseau','Les";
_q = "INSERT INTO livre VALUES('3','Rousseau','Les Confessions','2007-05-09',2,'A la fin de année 1761, éditeur hollandais de Rousseau fait savoir à écrivain son désir de mettre en tête de ses œuvres complètes une vie de auteur. Rousseau lui répond en janvier 1762 qu une telle entreprise compromettrait trop de monde.')";
 //BA.debugLineNum = 95;BA.debugLine="SQL1.ExecNonQuery(Q)";
_sql1.ExecNonQuery(_q);
 //BA.debugLineNum = 96;BA.debugLine="Q = \"INSERT INTO livre VALUES('4','Voltaire','Can";
_q = "INSERT INTO livre VALUES('4','Voltaire','Candide','2007-03-21',5,'Candide est un conte en trente chapitres numérotés et titrés, publié à Genève et diffusé en février 1759, simultanément à Genève, Paris et Amsterdam. En 1761, une édition augmentée paraît à Paris.')";
 //BA.debugLineNum = 97;BA.debugLine="SQL1.ExecNonQuery(Q)";
_sql1.ExecNonQuery(_q);
 //BA.debugLineNum = 101;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
addbook._process_globals();
addadhe._process_globals();
emprunt._process_globals();
depot._process_globals();
forgetmdp._process_globals();
gereradh._process_globals();
gererbook._process_globals();
inscription._process_globals();
connexion._process_globals();
dashboard._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 17;BA.debugLine="Dim SQL1 As SQL";
_sql1 = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 18;BA.debugLine="Dim timer As Timer";
_timer = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _timer_tick() throws Exception{
 //BA.debugLineNum = 103;BA.debugLine="Sub timer_Tick";
 //BA.debugLineNum = 105;BA.debugLine="timer.Enabled = False";
_timer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 106;BA.debugLine="StartActivity(Connexion)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._connexion.getObject()));
 //BA.debugLineNum = 108;BA.debugLine="End Sub";
return "";
}
}
