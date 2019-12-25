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
	Dim SQL1 As SQL
	
	Dim EmailExist As Boolean = False
	Dim codeTExist As Boolean = True
	
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private ButtonSignIn As Button
	Private ButtonSignUp As Button
	Private EditTextCodeTuteur As EditText
	Private EditTextConfMDP As EditText
	Private EditTextEmail As EditText
	Private EditTextMdp As EditText
	Private EditTextNomPrenom As EditText
	Private EditTextTel As EditText
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("Inscription")
	
	SQL1.Initialize(File.DirInternal, "gbiblio.db", False)
	
	EditTextCodeTuteur.Text = ""
	EditTextConfMDP.Text = ""
	EditTextEmail.Text = ""
	EditTextMdp.Text = ""
	EditTextNomPrenom.Text = ""
	EditTextTel.Text = ""

End Sub

Sub CheckEmail
	Dim Cursor1 As Cursor
	Dim Query As String
	
	Query = "SELECT email FROM bibliothecaire WHERE email = ?"
	Cursor1 = SQL1.ExecQuery2(Query, Array As String (EditTextEmail.Text))
	
	If Cursor1.RowCount > 0 Then						
		EmailExist = True
	End If
	Cursor1.Close
End Sub


Sub CheckCodeT
	Dim Cursor1 As Cursor
	Dim Query As String
	
	Query = "SELECT codeT FROM bibliothecaire WHERE codeT = ?"
	Cursor1 = SQL1.ExecQuery2(Query, Array As String (EditTextCodeTuteur.Text))

	If Cursor1.RowCount == 0 Then						'check if entries exist
		codeTExist = False
	End If
	
	Cursor1.Close														'close the cursor, we don't need it anymore
End Sub

Sub SaveInfoPerson
	Dim Query As String
	'email TEXT PRIMARY KEY, nom TEXT, codeT TEXT, tel TEXT, mdp TEXT
	Query = "INSERT INTO bibliothecaire VALUES (?, ?, ?, ?, ?)"
	SQL1.ExecNonQuery2(Query, Array As String(EditTextEmail.Text, EditTextNomPrenom.Text, EditTextCodeTuteur.Text, EditTextTel.Text, EditTextMdp.Text))
		
	ToastMessageShow("Gestionnaire ajouté avec succès", True)
	
End Sub

Sub Activity_Resume
	EditTextCodeTuteur.Text = ""
	EditTextConfMDP.Text = ""
	EditTextEmail.Text = ""
	EditTextMdp.Text = ""
	EditTextNomPrenom.Text = ""
	EditTextTel.Text = ""
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		SQL1.Close		'if the user closes the program we close the database
	End If
End Sub


Sub ButtonSignUp_Click
	If EditTextNomPrenom.Text.Trim.Length  == 0 Or EditTextEmail.Text.Trim.Length  == 0 Or EditTextCodeTuteur.Text.Trim.Length  == 0 Or EditTextTel.Text.Trim.Length  == 0 Or EditTextMdp.Text.Trim.Length  == 0 Then
		Msgbox("Entrez toutes les valeurs svp !!!","Inscription agent")
	Else
		If EditTextMdp.Text <> EditTextConfMDP.Text Then
			ToastMessageShow("Mots de passe différents !!!", True)
		Else
			CheckEmail
			If EmailExist Then
				ToastMessageShow("Ce email existe déjà, veuillez utilisez un autre mail pour votre inscription !!!",True)
				EditTextEmail.Text = ""
			Else
				CheckCodeT
				If codeTExist == False And EditTextCodeTuteur.Text<> "<@>12345<@/>" Then
					ToastMessageShow("Ce code tuteur n'existe pas, veuillez utilisez un code tuteur valide pour votre inscription !!!", True)
					EditTextCodeTuteur.Text = ""
				Else
					SaveInfoPerson
					StartActivity(Connexion)
				End If
			End If
		End If
		
	End If
End Sub

Sub ButtonSignIn_Click
	StartActivity(Connexion)
End Sub