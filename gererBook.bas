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
	Private EditTextNom As EditText
	Private EditTextNombre As EditText
	Private EditTextPublication As EditText
	Private EditTextResume As EditText
	Private EditTextTitre As EditText
	Private SpinnerCode As Spinner
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("gererBook")
	
	SQL1.Initialize(File.DirInternal, "gbiblio.db", False)
	
	bmpBack.Initialize(File.DirAssets, "background.jpg")
	Activity.SetBackgroundImage(bmpBack)

	ReadTable
	
	SpinnerCode.SelectedIndex = 0
	
	ReadTableForView
	
End Sub

Sub ClearChamp
	EditTextNom.Text = ""
	EditTextTitre.Text = ""
	EditTextPublication.Text = ""
	EditTextNombre.Text = ""
	EditTextResume.Text = ""
End Sub

Sub ReadTable
	Dim Row As Int
	Dim Cursor1 As Cursor
	Dim RowNumber = 0 As Int
	
	'On remplit avec les emails
	Cursor1 = SQL1.ExecQuery("SELECT codeLivre FROM livre")
	SpinnerCode.Clear
	SpinnerCode.Add("")
	If Cursor1.RowCount > 0 Then
		RowNumber = Cursor1.RowCount
		For Row = 0 To RowNumber - 1
			Cursor1.Position = Row
			SpinnerCode.Add(Cursor1.GetString("codeLivre"))
		Next
	End If
	Cursor1.Close
	
End Sub

Sub ReadTableForView
	Dim Cursor1 As Cursor
	Cursor1 = SQL1.ExecQuery2("SELECT * FROM livre WHERE codeLivre = ?", Array As String(SpinnerCode.SelectedItem))
	Cursor1.Position = 0
	If Cursor1.RowCount > 0 Then
		EditTextNom.Text = Cursor1.GetString("nomAuteur")
		EditTextTitre.Text = Cursor1.GetString("titreLivre")
		EditTextPublication.Text = Cursor1.GetString("datePub")
		EditTextNombre.Text = Cursor1.Getint("nbreExple")
		EditTextResume.Text = Cursor1.GetString("resume")
	End If
	Cursor1.Close
	
End Sub


Sub UpdateInfo
	
	Dim Query As String
	Dim nbre As Int
	nbre = EditTextNombre.Text
	
	Query = "UPDATE livre SET nomAuteur = ?, titreLivre = ? , datePub = ? , nbreExple = " & nbre & "  , resume = ? WHERE codeLivre = ? "
	SQL1.ExecNonQuery2(Query, Array As String(EditTextNom.Text, EditTextTitre.Text, EditTextPublication.Text, EditTextResume.Text,SpinnerCode.SelectedItem))
	ToastMessageShow("Mise à jour effectuée avec succès", True)
	
End Sub

Sub deleteBook
	Dim Query As String
	Query = "DELETE FROM livre WHERE codeLivre = ? "
	SQL1.ExecNonQuery2(Query, Array As String(SpinnerCode.SelectedItem))
	
	ReadTable
	SpinnerCode.SelectedIndex = 0
	ClearChamp
	
	ToastMessageShow("Suppression effectuée avec succès", True)
End Sub

'livre (codeLivre TEXT PRIMARY KEY, nomAuteur TEXT, titreLivre TEXT, datePub DATE, nbreExple INT, resume TEXT)

Sub Activity_Resume
	SpinnerCode.SelectedIndex = 0
	ClearChamp
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		SQL1.Close		'if the user closes the program we close the database
	End If
End Sub


Sub SpinnerCode_ItemClick (Position As Int, Value As Object)
	If SpinnerCode.SelectedIndex <> 0 Then
		ReadTableForView
	Else
		ClearChamp
	End If
End Sub

Sub ButtonSupprimer_Click
	If SpinnerCode.SelectedIndex <> 0 Then
		deleteBook
	End If
End Sub

Sub ButtonModifier_Click
	If SpinnerCode.SelectedIndex <> 0 Then
		UpdateInfo
	End If
End Sub