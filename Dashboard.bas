B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=8.8
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

	Dim bmpBack  As Bitmap
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private ImageView1 As ImageView
	Private ImageView2 As ImageView
	Private ImageView3 As ImageView
	Private ImageView4 As ImageView
	Private ImageView5 As ImageView
	Private ImageView6 As ImageView
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("Dashboard")

		
	bmpBack.Initialize(File.DirAssets, "background.jpg")
	Activity.SetBackgroundImage(bmpBack)
	
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub


Sub ImageView6_Click
	StartActivity(Depot)
End Sub

Sub ImageView5_Click
	StartActivity(Emprunt)
End Sub

Sub ImageView4_Click
	StartActivity(gererBook)
End Sub

Sub ImageView3_Click
	StartActivity(addBook)
End Sub

Sub ImageView2_Click
	StartActivity(GererAdh)
End Sub

Sub ImageView1_Click
	StartActivity(addAdhe)
End Sub