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

	Private ButtonValiderEnregist As Button
	Private EditTextCodeLivre As EditText
	Private EditTextDatePub As EditText
	Private EditTextNbre As EditText
	Private EditTextNomPrenomAut As EditText
	Private EditTextTitre As EditText
	Private EditTextResume As EditText

End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("addBook")
	SQL1.Initialize(File.DirInternal, "gbiblio.db", False)
	
	bmpBack.Initialize(File.DirAssets, "background.jpg")
	Activity.SetBackgroundImage(bmpBack)
	
	EditTextCodeLivre.Text = ""
	EditTextDatePub.Text = ""
	EditTextNbre.Text = ""
	EditTextNomPrenomAut.Text = ""
	EditTextTitre.Text = ""
	EditTextResume.Text = ""

End Sub

Sub CheckCodeExist
	Dim Cursor1 As Cursor
	Dim Query As String
	
	Query = "SELECT codeLivre FROM livre WHERE codeLivre = ?"
	Cursor1 = SQL1.ExecQuery2(Query, Array As String (EditTextCodeLivre.Text))
	
	If Cursor1.RowCount > 0 Then
		codeExist = True
	End If
	Cursor1.Close
	
End Sub

Sub SaveBook
	Dim Query As String
	Dim nbreExple As Int
	nbreExple = EditTextNbre.Text
	Query = "INSERT INTO livre VALUES (?, ?, ?, ?, "& nbreExple &",?)"
	SQL1.ExecNonQuery2(Query, Array As String(EditTextCodeLivre.Text, EditTextNomPrenomAut.Text, EditTextTitre.Text, EditTextDatePub.Text, EditTextResume.Text))
		
	ToastMessageShow("Livre ajouté avec succès", True)
	
End Sub


 'codeLivre TEXT PRIMARY KEY, nomAuteur TEXT, titreLivre TEXT, datePub DATE, nbreExple INT, resume TEXT
Sub Activity_Resume
	EditTextCodeLivre.Text = ""
	EditTextDatePub.Text = ""
	EditTextNbre.Text = ""
	EditTextNomPrenomAut.Text = ""
	EditTextTitre.Text = ""
	EditTextResume.Text = ""
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		SQL1.Close		'if the user closes the program we close the database
	End If
End Sub


Sub ButtonValiderEnregist_Click
	If EditTextNomPrenomAut.Text.Trim.Length  == 0 Or EditTextTitre.Text.Trim.Length  == 0 Or EditTextNbre.Text.Trim.Length  == 0 Or EditTextCodeLivre.Text.Trim.Length  == 0 Then
		Msgbox("Entrez toutes les valeurs svp !!!","Enregistrement livre")
	Else
		CheckCodeExist
		If codeExist Then
			ToastMessageShow("Ce code existe déjà, veuillez utilisez un autre code pour ce livre !!!",True)
			EditTextCodeLivre.Text = ""
		Else
			If IsNumber(EditTextNbre.Text) Then
				SaveBook
				StartActivity(Dashboard)
			Else
				ToastMessageShow("Le nombre de livre est incorrect !!!",True)
			End If
		End If
	End If

End Sub
