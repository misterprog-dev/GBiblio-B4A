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

	Private ButtonValidEmprunt As Button

	Private SpinnerCodeL As Spinner
	Private SpinnerEmailAdh As Spinner
	
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("Emprunt")
	SQL1.Initialize(File.DirInternal, "gbiblio.db", False)
	
	bmpBack.Initialize(File.DirAssets, "background.jpg")
	Activity.SetBackgroundImage(bmpBack)
	
	ReadTable
	
	SpinnerCodeL.SelectedIndex = 0
	SpinnerEmailAdh.SelectedIndex = 0
	
End Sub

Sub ReadTable
	Dim Row As Int
	Dim Cursor1 As Cursor
	Dim RowNumber = 0 As Int
	
	'On remplit avec les emails
	Cursor1 = SQL1.ExecQuery("SELECT email FROM adherant")
	If Cursor1.RowCount > 0 Then						'check if entries exist
		RowNumber = Cursor1.RowCount					'set the row count variable				
		For Row = 0 To RowNumber - 1
			Cursor1.Position = Row
			SpinnerEmailAdh.Add(Cursor1.GetString("email"))		
		Next										'set the current index to 0
	End If
	Cursor1.Close
	
	'On remplit avec les codes livre
	Cursor1 = SQL1.ExecQuery("SELECT codeLivre FROM livre")
	If Cursor1.RowCount > 0 Then						'check if entries exist
		RowNumber = Cursor1.RowCount					'set the row count variable
		For Row = 0 To RowNumber - 1
			Cursor1.Position = Row
			SpinnerCodeL.Add(Cursor1.GetString("codeLivre"))
		Next										'set the current index to 0
	End If
	Cursor1.Close
														'close the cursor, we don't need it anymore
End Sub

Sub CheckExistEmprunt
	Dim Cursor1 As Cursor
	Dim Query As String
	codeExist = False
	
	Query = "SELECT emailAdh FROM transactionLivre WHERE emailAdh = ? AND dateDepot IS NULL"
	Cursor1 = SQL1.ExecQuery2(Query, Array As String (SpinnerEmailAdh.SelectedItem))
	Cursor1.Position = 0
	If Cursor1.RowCount > 0 Then
		codeExist = True
	End If
	Cursor1.Close
	
End Sub

Sub SaveEmprunt
	Dim Query, Query2 As String
	Dim nbre As Int = 0
	Dim Cursor1 As Cursor

	Cursor1 = SQL1.ExecQuery("SELECT nbreExple FROM livre WHERE codeLivre = '" & SpinnerCodeL.SelectedItem &"'")
	Cursor1.Position = 0
	nbre = Cursor1.GetInt("nbreExple")
	
	If nbre == 0 Then
		ToastMessageShow("Quantité de livre insuffisante !", True)
	Else  'transactionLivre (id INT PRIMARY KEY, codeLivre TEXT, emailAdh TEXT, dateEmprunt DATETIME, dateDepot DATETIME DEFAULT NULL)
		Query = "INSERT INTO transactionLivre VALUES (NULL, ?, ?, date('now'), NULL)"
		'Log(SpinnerCodeL.SelectedItem & " - " &SpinnerEmailAdh.SelectedItem)
		SQL1.ExecNonQuery2(Query, Array As String(SpinnerCodeL.SelectedItem, SpinnerEmailAdh.SelectedItem))
	
		'livre (codeLivre TEXT PRIMARY KEY, nomAuteur TEXT, titreLivre TEXT, datePub DATE, nbreExple INT, resume TEXT)
		Query2 = "UPDATE livre SET nbreExple = " & (nbre - 1) & " WHERE codeLivre = ? "
		SQL1.ExecNonQuery2(Query2, Array As String(SpinnerCodeL.SelectedItem))
		
		ToastMessageShow("Emprunt validé avec succès !", True)
	End If
	
End Sub

Sub Activity_Resume

End Sub

'transactionLivre (id INT PRIMARY KEY, codeLivre TEXT, emailAdh TEXT, dateEmprunt DATE, dateDepot DATE DEFAULT NULL)

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		SQL1.Close		'if the user closes the program we close the database
	End If
End Sub


Sub ButtonValidEmprunt_Click
	CheckExistEmprunt
	If codeExist Then
		ToastMessageShow("Vous avez emprunté un livre que vous n'avez pas encore déposé !!!",True)
	Else
		SaveEmprunt
		StartActivity(Dashboard)
	End If
End Sub