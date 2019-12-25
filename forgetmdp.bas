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
	Dim SQL1 As SQL
	
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private Buttonvalider As Button
	Private EditTextEmail As EditText
	Private LabelResultat As Label
	Private Label1 As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("forgetmdp")
	
	SQL1.Initialize(File.DirInternal, "gbiblio.db", False)
	
	bmpBack.Initialize(File.DirAssets, "background.jpg")
	Activity.SetBackgroundImage(bmpBack)

End Sub

'bibliothecaire (email TEXT PRIMARY KEY, nom TEXT, codeT TEXT, tel TEXT, mdp TEXT)
Sub CheckEmailExist
	Dim Cursor1 As Cursor
	Dim Query As String
	
	Query = "SELECT mdp FROM bibliothecaire WHERE email = ?"
	Cursor1 = SQL1.ExecQuery2(Query, Array As String (EditTextEmail.Text))
	
	If Cursor1.RowCount > 0 Then
		Label1.Visible = True
		LabelResultat.Visible = True
		'LabelResultat.Text = Cursor1.GetString("mdp")
	Else
		ToastMessageShow("Ce compte n'existe pas !!!!", True)
	End If
	Cursor1.Close
	
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		SQL1.Close		'if the user closes the program we close the database
	End If
End Sub


Sub Buttonvalider_Click
	CheckEmailExist
End Sub