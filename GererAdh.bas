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

	Private ButtonModifier As Button
	Private ButtonSupprimer As Button
	Private EditTextAdresse As EditText
	Private EditTextDateN As EditText
	Private EditTextNom As EditText
	Private EditTextprenom As EditText
	Private EditTextTel As EditText
	Private SpinnerEmail As Spinner
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("GererAdh")
	
	SQL1.Initialize(File.DirInternal, "gbiblio.db", False)
	
	bmpBack.Initialize(File.DirAssets, "background.jpg")
	Activity.SetBackgroundImage(bmpBack)
	
	ReadTable
	
	SpinnerEmail.SelectedIndex = 0
	ReadTableForView
	
End Sub

Sub ClearChamp
	EditTextNom.Text = ""
	EditTextprenom.Text = ""
	EditTextDateN.Text = ""
	EditTextAdresse.Text = ""
	EditTextTel.Text = ""
End Sub

'adherant (email TEXT PRIMARY KEY, nomAdh TEXT, prenomAdh TEXT,datenaiss DATE, adresse TEXT, tel TEXT)

Sub ReadTable
	Dim Row As Int
	Dim Cursor1 As Cursor
	Dim RowNumber = 0 As Int

	SpinnerEmail.Clear
	SpinnerEmail.Add("")
	'On remplit avec les emails
	Cursor1 = SQL1.ExecQuery("SELECT email FROM adherant")
	If Cursor1.RowCount > 0 Then						
		RowNumber = Cursor1.RowCount					
		For Row = 0 To RowNumber - 1
			Cursor1.Position = Row
			SpinnerEmail.Add(Cursor1.GetString("email"))
		Next										
	End If
	Cursor1.Close
	
End Sub

Sub ReadTableForView
	Dim Cursor1 As Cursor
	'On remplit avec les emails
	Cursor1 = SQL1.ExecQuery2("SELECT * FROM adherant WHERE email = ?", Array As String(SpinnerEmail.SelectedItem))
	Cursor1.Position = 0
	If Cursor1.RowCount > 0 Then						
		EditTextNom.Text = Cursor1.GetString("nomAdh")
		EditTextprenom.Text = Cursor1.GetString("prenomAdh")
		EditTextDateN.Text = Cursor1.GetString("datenaiss")
		EditTextAdresse.Text = Cursor1.GetString("adresse")
		EditTextTel.Text = Cursor1.GetString("tel")	
	End If
	Cursor1.Close
	
End Sub

'adherant (email TEXT PRIMARY KEY, nomAdh TEXT, prenomAdh TEXT,datenaiss DATE, adresse TEXT, tel TEXT)
Sub UpdateInfo
	
	Dim Query As String
	Query = "UPDATE adherant SET nomAdh = ?, prenomAdh = ? , datenaiss = ? , adresse = ? , tel = ? WHERE email = ? "
	SQL1.ExecNonQuery2(Query, Array As String(EditTextNom.Text, EditTextprenom.Text, EditTextDateN.Text, EditTextAdresse.Text, EditTextTel.Text, SpinnerEmail.SelectedItem))
	ToastMessageShow("Mise à jour effectuée avec succès", True)
	
End Sub

Sub deleteAdh
	Dim Query As String
	Query = "DELETE FROM adherant WHERE email = ? "
	
	SQL1.ExecNonQuery2(Query, Array As String(SpinnerEmail.SelectedItem))
	
	ReadTable
	SpinnerEmail.SelectedIndex = 0
	ClearChamp
	
	ToastMessageShow("Suppression effectuée avec succès", True)
End Sub

Sub Activity_Resume
	SpinnerEmail.SelectedIndex = 0
	ClearChamp
	
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		SQL1.Close		'if the user closes the program we close the database
	End If
End Sub


Sub SpinnerEmail_ItemClick (Position As Int, Value As Object)
	If SpinnerEmail.SelectedIndex <> 0 Then
		ReadTableForView
	Else
		ClearChamp
	End If
End Sub

Sub ButtonSupprimer_Click
	If SpinnerEmail.SelectedIndex <> 0 Then
		deleteAdh
	End If
End Sub

Sub ButtonModifier_Click
	If SpinnerEmail.SelectedIndex <> 0 Then
		UpdateInfo
	End If
End Sub