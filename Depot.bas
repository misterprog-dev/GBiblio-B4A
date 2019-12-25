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
	
	Dim EltsExiste As Boolean = False
	
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private ButtonValidDepot As Button
	
	Private SpinnerCodeL As Spinner
	Private SpinnerEmailAdh As Spinner

End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("Depot")
	SQL1.Initialize(File.DirInternal, "gbiblio.db", False)
	
	bmpBack.Initialize(File.DirAssets, "background.jpg")
	Activity.SetBackgroundImage(bmpBack)
	
	ReadTable
	
	SpinnerCodeL.SelectedIndex = 0
	SpinnerEmailAdh.SelectedIndex = 0
	
End Sub
'transactionLivre (id INT PRIMARY KEY, codeLivre TEXT, emailAdh TEXT, dateEmprunt DATE, dateDepot DATE DEFAULT NULL)

Sub ReadTable
	Dim Row As Int
	Dim Cursor1 As Cursor
	Dim RowNumber = 0 As Int
	SpinnerCodeL.Clear
	SpinnerEmailAdh.Clear
	'On remplit avec les emails
	Cursor1 = SQL1.ExecQuery("SELECT codeLivre, emailAdh FROM transactionLivre WHERE dateDepot IS NULL ")
	If Cursor1.RowCount > 0 Then						
		RowNumber = Cursor1.RowCount					
		For Row = 0 To RowNumber - 1
			Cursor1.Position = Row
			SpinnerCodeL.Add(Cursor1.GetString("codeLivre"))
			SpinnerEmailAdh.Add(Cursor1.GetString("emailAdh"))
			
			EltsExiste = True
		Next										
	End If
	Cursor1.Close
End Sub

Sub SaveDepot
	Dim Query, Query2 As String
	Dim nbre As Int = 0
	Dim Cursor1 As Cursor

	Dim exist As Boolean = False
	
	Cursor1 = SQL1.ExecQuery2("SELECT * FROM transactionLivre WHERE codeLivre = ? AND  emailAdh = ?", Array As String(SpinnerCodeL.SelectedItem, SpinnerEmailAdh.SelectedItem))
	Cursor1.Position = 0
	If Cursor1.RowCount > 0 Then
		exist = True
	End If
	
	If exist Then
		
		Cursor1 = SQL1.ExecQuery2("SELECT nbreExple FROM livre WHERE codeLivre = ?",  Array As String(SpinnerCodeL.SelectedItem))
		Cursor1.Position = 0
		nbre = Cursor1.GetInt("nbreExple")
		
		Query = "UPDATE transactionLivre SET dateDepot = date('now') WHERE codeLivre = ? AND  emailAdh = ?"
		SQL1.ExecNonQuery2(Query, Array As String(SpinnerCodeL.SelectedItem, SpinnerEmailAdh.SelectedItem))
	
		'livre (codeLivre TEXT PRIMARY KEY, nomAuteur TEXT, titreLivre TEXT, datePub DATE, nbreExple INT, resume TEXT)
		Query2 = "UPDATE livre SET nbreExple = "& (nbre + 1) &" WHERE codeLivre = ? "
		SQL1.ExecNonQuery2(Query2, Array As String(SpinnerCodeL.SelectedItem))

		ToastMessageShow("Dépôt validé avec succès !", True)
	Else
		ToastMessageShow("Aucun emprunt de ce type !", True)
	End If
	
End Sub


Sub Activity_Resume
	EltsExiste = False
	ReadTable
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		SQL1.Close		'if the user closes the program we close the database
	End If
End Sub


Sub ButtonValidDepot_Click
	
	If EltsExiste Then
		SaveDepot
		StartActivity(Dashboard)
	End If
	
End Sub