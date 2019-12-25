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
	
	Dim codeExist As Boolean = False
	

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private ButtonValiderInsc As Button
	Private EditTextAdresse As EditText
	Private EditTextDateNai As EditText
	Private EditTextEmailAdh As EditText
	Private EditTextNomAdh As EditText
	Private EditTextPrenomAdh As EditText
	Private EditTextTelAdh As EditText
	
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("addAdhe")
	
	SQL1.Initialize(File.DirInternal, "gbiblio.db", False)
	
	bmpBack.Initialize(File.DirAssets, "background.jpg")
	Activity.SetBackgroundImage(bmpBack)
	
	EditTextAdresse.Text = ""
	EditTextDateNai.Text = ""
	EditTextEmailAdh.Text = ""
	EditTextNomAdh.Text = ""
	EditTextPrenomAdh.Text = ""
	EditTextTelAdh.Text = ""
	
End Sub
'adherant (email TEXT PRIMARY KEY, nomAdh TEXT, prenomAdh TEXT,datenaiss DATE, adresse TEXT, tel TEXT)
Sub CheckCodeExist
	Dim Cursor1 As Cursor
	Dim Query As String
	
	Query = "SELECT email FROM adherant WHERE email = ?"
	Cursor1 = SQL1.ExecQuery2(Query, Array As String (EditTextEmailAdh.Text))
	
	If Cursor1.RowCount > 0 Then
		codeExist = True
	End If
	Cursor1.Close
	
End Sub

Sub SaveAdh
	Dim Query As String
	Query = "INSERT INTO adherant VALUES (?, ?, ?, ?, ?,?)"
	SQL1.ExecNonQuery2(Query, Array As String(EditTextEmailAdh.Text, EditTextNomAdh.Text, EditTextPrenomAdh.Text, EditTextDateNai.Text, EditTextAdresse.Text, EditTextTelAdh.Text))
		
	ToastMessageShow("Adhérant ajouté avec succès !", True)
	
End Sub

Sub Activity_Resume
	EditTextAdresse.Text = ""
	EditTextDateNai.Text = ""
	EditTextEmailAdh.Text = ""
	EditTextNomAdh.Text = ""
	EditTextPrenomAdh.Text = ""
	EditTextTelAdh.Text = ""
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		SQL1.Close		'if the user closes the program we close the database
	End If
End Sub


Sub ButtonValiderInsc_Click
	If EditTextEmailAdh.Text.Trim.Length  == 0 Or EditTextAdresse.Text.Trim.Length  == 0 Or EditTextTelAdh.Text.Trim.Length  == 0 Then
		Msgbox("Entrez toutes les valeurs svp !!!","Enregistrement livre")
	Else
		CheckCodeExist
		If codeExist Then
			ToastMessageShow("Cet email existe déjà, veuillez utilisez un autre email pour l'inscription !!!",True)
			EditTextEmailAdh.Text = ""
		Else
			SaveAdh
			StartActivity(Dashboard)
		End If
	End If
End Sub