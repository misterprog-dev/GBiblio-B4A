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
	
	Dim cpteExist As Boolean = False
	
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private ButtonSignIn As Button
	Private ButtonSignUp As Button
	Private CheckBoxRemember As CheckBox
	Private EditTextMail As EditText
	Private EditTextMdp As EditText
	Private LabelMdpForget As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	
	Activity.LoadLayout("Connexion")
	
	SQL1.Initialize(File.DirInternal, "gbiblio.db", False)
	
	bmpBack.Initialize(File.DirAssets, "background.jpg")
	Activity.SetBackgroundImage(bmpBack)


End Sub

'email TEXT PRIMARY KEY, nom TEXT, codeT TEXT, tel TEXT, mdp TEXT
Sub CheckConnexion
	Dim Cursor1 As Cursor
	Dim Query As String
	
	Query = "SELECT email, mdp FROM bibliothecaire WHERE email = ? AND mdp = ?"
	Cursor1 = SQL1.ExecQuery2(Query, Array As String (EditTextMail.Text, EditTextMdp.Text))
	
	If Cursor1.RowCount > 0 Then
		cpteExist = True
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


Sub LabelMdpForget_Click
	StartActivity(forgetmdp)
End Sub

Sub CheckBoxRemember_CheckedChange(Checked As Boolean)
	
End Sub

Sub ButtonSignUp_Click
	StartActivity(Inscription)
	ToastMessageShow("Veuillez remplir tous les champs svp !!!!", True)
End Sub

Sub ButtonSignIn_Click
	CheckConnexion
	If cpteExist Then
		StartActivity(Dashboard)
	Else
		ToastMessageShow("Email ou mot de passe incorrecte, veuillez vérifier svp !!!!", True)
	End If
End Sub