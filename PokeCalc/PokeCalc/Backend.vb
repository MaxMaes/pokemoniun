Imports mysql.data.mysqlclient
Imports System.IO
Imports Nini.Config

Module Backend
    Dim ds As DataSet
    Dim sqlcon As MySqlConnection
    Dim da As MySqlDataAdapter
    Dim host As String = My.Settings.Host
    Dim user As String = My.Settings.User
    Dim pass As String = My.Settings.Pass
    Dim db As String = My.Settings.DB

    Dim source As New IniConfigSource("pokemon.ini")

    'Load Connection
    Public Sub LoadCon()
        sqlcon = New MySqlConnection("Server= " & host & " ; database= " & db & " ; uid= " & user & " ;pwd= " & pass)
        sqlcon.Open()
    End Sub

    'Close Connection
    Public Sub Closeing()
        If ConnectionState.Open Then
            sqlcon.Dispose()
        End If
    End Sub

    Public Sub Login()
        'MySQL Addapters
        Dim userCommand As New MySqlCommand
        Dim passCommand As New MySqlCommand
        Dim userDReader As MySqlDataReader
        Dim passDReader As MySqlDataReader

        ' Static Variable
        Dim sUser As String
        Dim sPass As String

        ' Connection
        sqlcon = New MySqlConnection("Server= " & host & " ; database= " & db & " ; uid= " & user & " ;pwd= " & pass)

        ' SQL
        Dim GetUsername As String = "SELECT username FROM pn_mallMembers WHERE username='" & frmLogin.GhostTextBox1.Text & "'"
        Dim GetPassword As String = "SELECT password FROM pn_mallMembers WHERE username='" & frmLogin.GhostTextBox1.Text & "'"

        Try
            ' Open connection
            sqlcon.Open()

            ' Execute login
            Try
                ' Set commands
                userCommand.Connection = sqlcon
                passCommand.Connection = sqlcon
                userCommand.CommandText = GetUsername
                passCommand.CommandText = GetPassword

                ' Set user reader
                userDReader = userCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While userDReader.Read
                    sUser = userDReader("username")
                Loop

                ' Close once we find value
                userDReader.Close()

                ' Set pass reader
                passDReader = passCommand.ExecuteReader

                Do While passDReader.Read
                    sPass = passDReader("password")
                Loop

                ' Close once we find value
                passDReader.Close()

                ' Dispose the connection
                sqlcon.Dispose()

                ' Now check the login
                If frmLogin.GhostTextBox1.Text = "" Then
                    ' No username
                    MsgBox("Error: Please input a username.")
                ElseIf frmLogin.GhostTextBox2.Text = "" Then
                    ' No password
                    MsgBox("Error: Please input a password.")
                ElseIf frmLogin.GhostTextBox1.Text <> sUser Then
                    ' Username isn't in the database
                    MsgBox("Error: No such username.")
                ElseIf frmLogin.GhostTextBox2.Text <> sPass Then
                    ' Password doesn't match with the username
                    MsgBox("Error: Incorrect password.")
                Else
                    ' Finally, lets log them in.
                    frmLogin.Hide()
                    frmSelection.Show()
                End If
            Catch myerror As MySqlException
                ' There was an error
                MsgBox("There was an error reading from the database: " & myerror.Message)

                ' Dispose the connection
                sqlcon.Dispose()
            End Try
        Catch myerror As MySqlException
            ' There was an error
            MessageBox.Show("Error connecting to the database: " & myerror.Message)

            ' Dispose the connection
            sqlcon.Dispose()
        Finally
            ' Dispose the connection
            If sqlcon.State <> ConnectionState.Closed Then sqlcon.Close()
        End Try
    End Sub


    Public Sub Authenticate()
        ' MySQL Addapters
        Dim userCommand As New MySqlCommand
        Dim emailCommand As New MySqlCommand
        Dim userInfoCommand As New MySqlCommand
        Dim uCheckCommand As New MySqlCommand
        Dim eCheckCommand As New MySqlCommand
        Dim userDReader As MySqlDataReader
        Dim emailDReader As MySqlDataReader
        Dim uCheckDReader As MySqlDataReader
        Dim eCheckDReader As MySqlDataReader

        ' Static Variable
        Dim sUser As String
        Dim sEmail As String
        Dim stUser As String
        Dim stEmail As String

        ' Connection
        sqlcon = New MySqlConnection("Server= " & Host & " ; database= " & db & " ; uid= " & user & " ;pwd= " & pass)

        ' SQL
        Dim GetUsername As String = "SELECT username FROM pn_members WHERE username='" & frmAuth.GhostTextBox1.Text & "'"
        Dim GetEmail As String = "SELECT email FROM pn_members WHERE username='" & frmAuth.GhostTextBox1.Text & "'"
        Dim UserCheck As String = "SELECT username FROM pn_mallmembers WHERE username='" & frmAuth.GhostTextBox1.Text & "'"
        Dim EmailCheck As String = "SELECT email FROM pn_mallmembers WHERE email='" & frmAuth.GhostTextBox4.Text & "'"
        Dim SetUserInfo As String = "INSERT INTO pn_mallMembers (username,password,email) VALUES ('" & frmAuth.GhostTextBox1.Text & "', '" & frmAuth.GhostTextBox2.Text & "', '" & frmAuth.GhostTextBox4.Text & "')"


        Try
            ' Open connection
            sqlcon.Open()

            ' Execute authentification
            Try
                ' Set commands
                userCommand.Connection = sqlcon
                emailCommand.Connection = sqlcon
                userInfoCommand.Connection = sqlcon
                uCheckCommand.Connection = sqlcon
                eCheckCommand.Connection = sqlcon
                userCommand.CommandText = GetUsername
                emailCommand.CommandText = GetEmail
                userInfoCommand.CommandText = SetUserInfo
                eCheckCommand.CommandText = EmailCheck
                uCheckCommand.CommandText = UserCheck

                ' Set user reader
                userDReader = userCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While userDReader.Read
                    sUser = userDReader("username")
                Loop

                ' Close once we find value
                userDReader.Close()

                ' Set email reader
                emailDReader = emailCommand.ExecuteReader

                Do While emailDReader.Read
                    sEmail = emailDReader("email")
                Loop

                ' Close once we find value
                emailDReader.Close()

                ' Set username check reader
                uCheckDReader = uCheckCommand.ExecuteReader

                Do While uCheckDReader.Read
                    stUser = uCheckDReader("username")
                Loop

                ' Close once we find value
                uCheckDReader.Close()

                ' Set email check reader
                eCheckDReader = eCheckCommand.ExecuteReader

                Do While eCheckDReader.Read
                    stEmail = eCheckDReader("email")
                Loop

                ' Close once we find value
                eCheckDReader.Close()

                ' Now check the authentification
                If frmAuth.GhostTextBox2.Text <> frmAuth.GhostTextBox3.Text Then
                    MsgBox("Error: Passwords do not match.")
                ElseIf frmAuth.GhostTextBox1.Text = "" Then
                    MsgBox("Error: Please type a username.")
                ElseIf frmAuth.GhostTextBox2.Text = "" Then
                    MsgBox("Error: Please type a password.")
                ElseIf frmAuth.GhostTextBox3.Text = "" Then
                    MsgBox("Error: Please re-type the password.")
                ElseIf frmAuth.GhostTextBox4.Text = "" Then
                    MsgBox("Error: Please type an email.")
                ElseIf frmAuth.GhostTextBox1.Text <> sUser Then
                    MsgBox("Error: No such user found.")
                ElseIf frmAuth.GhostTextBox4.Text <> sEmail Then
                    MsgBox("Error: No such email found.")
                ElseIf frmAuth.GhostTextBox1.Text = stUser Then
                    MsgBox("Error: This username is already authenticated.")
                ElseIf frmAuth.GhostTextBox4.Text = stEmail Then
                    MsgBox("Error: This email has already been used.")
                ElseIf frmAuth.GhostTextBox1.Text = sUser And frmAuth.GhostTextBox4.Text = sEmail Then
                    ' Execute the command
                    userInfoCommand.ExecuteNonQuery()
                    ' Dispose the connection
                    sqlcon.Dispose()

                    ' Show success message
                    MsgBox("Authentification Successful. You may now login.")
                    frmAuth.Close()
                    frmLogin.Show()
                End If

            Catch myerror As MySqlException
                ' There was an error
                MsgBox("There was an error reading from the database: " & myerror.Message)

                ' Dispose the connection
                sqlcon.Dispose()
            End Try
        Catch myerror As MySqlException
            ' There was an error
            MessageBox.Show("Error connecting to the database: " & myerror.Message)

            ' Dispose the connection
            sqlcon.Dispose()
        Finally
            ' Dispose the connection
            If sqlcon.State <> ConnectionState.Closed Then sqlcon.Close()
        End Try

    End Sub

    Public Sub LoadPoke()
        ' MySQL Addapters
        Dim GetItemsCommand As New MySqlCommand
        Dim PokeNameReader As MySqlDataReader
        Dim PokeLevelReader As MySqlDataReader
        Dim PokeNatureReader As MySqlDataReader
        Dim evHpReader As MySqlDataReader
        Dim evAtkReader As MySqlDataReader
        Dim evDefReader As MySqlDataReader
        Dim evSpdReader As MySqlDataReader
        Dim evSpAtkReader As MySqlDataReader
        Dim evSpDefReader As MySqlDataReader
        Dim ivHpReader As MySqlDataReader
        Dim ivAtkReader As MySqlDataReader
        Dim ivDefReader As MySqlDataReader
        Dim ivSpdReader As MySqlDataReader
        Dim ivSpAtkReader As MySqlDataReader
        Dim ivSpDefReader As MySqlDataReader

        ' Connection
        sqlcon = New MySqlConnection("Server= " & host & " ; database= " & db & " ; uid= " & user & " ;pwd= " & pass)

        ' SQL
        Dim GetItems As String = "SELECT * FROM pn_pokemon WHERE currentTrainerName='" & frmLogin.GhostTextBox1.Text & "' ORDER BY id ASC LIMIT 40"

        Try
            ' Open connection
            sqlcon.Open()

            ' Execute authentification
            Try
                ' Set commands
                GetItemsCommand.Connection = sqlcon
                GetItemsCommand.CommandText = GetItems

                ' Set itemName reader
                PokeNameReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While PokeNameReader.Read
                    frmLists.ListBox1.Items.Add(PokeNameReader("Name"))
                    frmSelection.GhostComboBox2.Items.Add(PokeNameReader("Name"))
                Loop

                ' Close once we find value
                PokeNameReader.Close()

                ' Set itemIDs reader
                PokeLevelReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While PokeLevelReader.Read
                    frmLists.ListBox2.Items.Add(PokeLevelReader("level"))
                Loop

                ' Close once we find value
                PokeLevelReader.Close()

                ' Set itemDescription reader
                PokeNatureReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While PokeNatureReader.Read
                    frmLists.ListBox3.Items.Add(PokeNatureReader("nature"))
                Loop

                ' Close once we find value
                PokeNatureReader.Close()

                ' Set itemCategory reader
                evHpReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While evHpReader.Read
                    frmLists.ListBox4.Items.Add(evHpReader("evHP"))
                Loop

                ' Close once we find value
                evHpReader.Close()

                ' Set itemPrice reader
                evAtkReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While evAtkReader.Read
                    frmLists.ListBox5.Items.Add(evAtkReader("evATK"))
                Loop

                ' Close once we find value
                evAtkReader.Close()

                ' Set itemDP reader
                evDefReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While evDefReader.Read
                    frmLists.ListBox6.Items.Add(evDefReader("evDEF"))
                Loop

                ' Close once we find value
                evDefReader.Close()

                ' Set Enabled reader
                evSpdReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While evSpdReader.Read
                    frmLists.ListBox7.Items.Add(evSpdReader("evSPD"))
                Loop

                ' Close once we find value
                evSpdReader.Close()

                ' Set itemCount reader
                evSpAtkReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While evSpAtkReader.Read
                    frmLists.ListBox8.Items.Add(evSpAtkReader("evSPATK"))
                Loop

                ' Close once we find value
                evSpAtkReader.Close()

                ' Set itemCount reader
                evSpDefReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While evSpDefReader.Read
                    frmLists.ListBox9.Items.Add(evSpDefReader("evSPDEF"))
                Loop

                ' Close once we find value
                evSpDefReader.Close()

                ' Set itemCount reader
                ivHpReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While ivHpReader.Read
                    frmLists.ListBox10.Items.Add(ivHpReader("ivHP"))
                Loop

                ' Close once we find value
                ivHpReader.Close()

                ' Set itemCount reader
                ivAtkReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While ivAtkReader.Read
                    frmLists.ListBox11.Items.Add(ivAtkReader("ivATK"))
                Loop

                ' Close once we find value
                ivAtkReader.Close()

                ' Set itemCount reader
                ivDefReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While ivDefReader.Read
                    frmLists.ListBox12.Items.Add(ivDefReader("ivDEF"))
                Loop

                ' Close once we find value
                ivDefReader.Close()

                ' Set itemCount reader
                ivSpdReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While ivSpdReader.Read
                    frmLists.ListBox13.Items.Add(ivSpdReader("ivSPD"))
                Loop

                ' Close once we find value
                ivSpdReader.Close()

                ' Set itemCount reader
                ivSpAtkReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While ivSpAtkReader.Read
                    frmLists.ListBox14.Items.Add(ivSpAtkReader("ivSPATK"))
                Loop

                ' Close once we find value
                ivSpAtkReader.Close()

                ' Set itemCount reader
                ivSpDefReader = GetItemsCommand.ExecuteReader

                ' Execute readers and loop until we get a value
                Do While ivSpDefReader.Read
                    frmLists.ListBox15.Items.Add(ivSpDefReader("ivSPDEF"))
                Loop

                ' Close once we find value
                ivSpDefReader.Close()

                ' Dispose the connection
                sqlcon.Dispose()

            Catch myerror As MySqlException
                ' There was an error
                MsgBox("There was an error reading from the database: " & myerror.Message)

                ' Dispose the connection
                sqlcon.Dispose()
            End Try
        Catch myerror As MySqlException
            ' There was an error
            MessageBox.Show("Error connecting to the database: " & myerror.Message)

            ' Dispose the connection
            sqlcon.Dispose()
        Finally
            ' Dispose the connection
            If sqlcon.State <> ConnectionState.Closed Then sqlcon.Close()
        End Try
    End Sub

    Public Sub SelectPoke()
        frmLists.ComboBox1.SelectedItem = frmSelection.GhostComboBox2.Text

        'ID Formula
        Dim subid As String
        subid = 1
        Dim getid As String
        getid = subid + frmLists.ComboBox1.SelectedIndex

        Dim picid As String
        picid = getid

        If getid < 10 Then
            picid = "00" & getid
        ElseIf getid < 100 Then
            picid = "0" & getid
        End If

        Dim basestats As String = source.Configs(getid).Get("BaseStats")

        'Set the sprite
        Try
            If My.Computer.FileSystem.FileExists(Application.StartupPath & "/img/" & picid & ".gif") = True Then
                frmSelection.PokeSprite.Image = Image.FromFile(Application.StartupPath & "/img/" & picid & ".gif")
            Else
                MsgBox("Error; Missing image file")
            End If
        Catch ex As Exception
            MsgBox(ex.Message)
        End Try

        ' Split the string on the comma character
        Dim parts As String() = basestats.Split(New Char() {","c})

        ' Loop through result strings with For Each
        Dim part As String
        frmLists.ListBox16.Items.Clear()
        For Each part In parts
            frmLists.ListBox16.Items.Add(part)
        Next

        'Base Stats structure
        'hp
        'atk
        'def
        'speed
        'sp.atk
        'sp.def

        'Set the progressbag values to the pokemons base stats
        frmSelection.BaseHpBar.Value = frmLists.ListBox16.Items(0)
        frmSelection.BaseAtkBar.Value = frmLists.ListBox16.Items(1)
        frmSelection.BaseDefBar.Value = frmLists.ListBox16.Items(2)
        frmSelection.BaseSpdBar.Value = frmLists.ListBox16.Items(3)
        frmSelection.BaseSpatkBar.Value = frmLists.ListBox16.Items(4)
        frmSelection.BaseSpdefBar.Value = frmLists.ListBox16.Items(5)

        'Set the values in the labels
        frmSelection.BaseHp.Text = frmSelection.BaseHpBar.Value
        frmSelection.BaseAtk.Text = frmSelection.BaseAtkBar.Value
        frmSelection.BaseDef.Text = frmSelection.BaseDefBar.Value
        frmSelection.BaseSpd.Text = frmSelection.BaseSpdBar.Value
        frmSelection.BaseSpatk.Text = frmSelection.BaseSpatkBar.Value
        frmSelection.BaseSpdef.Text = frmSelection.BaseSpdefBar.Value

        'Set the name label
        frmSelection.pkmnName.Text = frmSelection.GhostComboBox2.Text

        'Set the other information
        frmLists.ListBox1.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox2.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox3.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox4.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox5.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox6.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox7.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox8.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox9.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox10.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox11.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox12.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox13.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox14.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex
        frmLists.ListBox15.SelectedIndex = frmSelection.GhostComboBox2.SelectedIndex

        'Set level then nature
        frmSelection.pkmnLevel.Text = frmLists.ListBox2.SelectedItem
        frmSelection.pkmnNature.SelectedItem = frmLists.ListBox3.SelectedItem

        'Set EVs already achived
        frmSelection.evHpTxtBox.Text = frmLists.ListBox4.SelectedItem
        frmSelection.evAtkTxtBox.Text = frmLists.ListBox5.SelectedItem
        frmSelection.evDefTxtBox.Text = frmLists.ListBox6.SelectedItem
        frmSelection.evSpdTxtBox.Text = frmLists.ListBox7.SelectedItem
        frmSelection.evSpatkTxtBox.Text = frmLists.ListBox8.SelectedItem
        frmSelection.evSpdefTxtBox.Text = frmLists.ListBox9.SelectedItem

        Dim evHptxt As Integer = frmSelection.evHpTxtBox.Text
        Dim evAtktxt As Integer = frmSelection.evAtkTxtBox.Text
        Dim evDeftxt As Integer = frmSelection.evDefTxtBox.Text
        Dim evSpdtxt As Integer = frmSelection.evSpdTxtBox.Text
        Dim evSpatktxt As Integer = frmSelection.evSpatkTxtBox.Text
        Dim evSpdeftxt As Integer = frmSelection.evSpdefTxtBox.Text

        frmSelection.evUsage.Text = evHptxt + evAtktxt + evDeftxt + evSpdtxt + evSpatktxt + evSpdeftxt
        frmSelection.evUsageBar.Value = frmSelection.evUsage.Text
    End Sub

    Public Sub ChangeLevel()
        If frmSelection.pkmnChangeLvlTxt.Text > 100 Then
            MsgBox("Error: Level can not exceed 100.")
        ElseIf frmSelection.pkmnChangeLvlTxt.Text < 1 Then
            MsgBox("Error: Level can not be 0 or below.")
        Else
            frmSelection.pkmnLevel.Text = frmSelection.pkmnChangeLvlTxt.Text
        End If
    End Sub

    Public Sub CalcHP()
        'Formula GEN V
        'HP = [IV + (2 x Base) + EV/4 + 100] x Level / 100 +10

        Dim HpIv As Integer = frmLists.ListBox10.SelectedItem
        Dim BaseHP As Integer = frmSelection.BaseHp.Text
        Dim HpEv As Integer = frmSelection.evHpTxtBox.Text
        Dim Level As Integer = frmSelection.pkmnLevel.Text
        Dim Sum As Integer

        Sum = (HpIv + (2 * BaseHP) + HpEv / 4 + 100) * Level / 100 + 10

        frmSelection.IvHp.Text = Sum
        frmSelection.IvHpBar.Value = HpIv
        frmSelection.LvHp.Text = HpIv
        frmSelection.HpMinMax.Text = HpIv - 1 & " - " & HpIv + 1

    End Sub

    Public Sub CalcAtk()
        'Formula GEN V
        'Other Stats = ([IV + (2 x Base) + EV/4] x Level / 100 + 5) x Nature

        '0 Hardy = 0
        '1 Lonely = atk / def
        '2 Brave = atk / spd
        '3 Adament = atk / sp. atk
        '4 Naughty = atk / sp. def
        '5 Bold = def / atk
        '6 Docile = 0
        '7 Relaxed = def / spd
        '8 Impish = def / sp. atk
        '9 Lax = def / sp. def
        '10 Timid = spd / atk
        '11 Hasty = spd / def
        '12 Serious = 0
        '13 Jolly = spd / sp.atk
        '14 Naive = spd / sp. def
        '15 Modest = sp. atk / atk
        '16 Mild = sp. atk / def
        '17 Quiet = sp. atk / spd
        '18 Bashful = 0
        '19 Rash = sp. atk / sp.def
        '20 Calm = sp. def / atk
        '21 Gentle = sp. def / def
        '22 Sassy = sp. def / spd
        '23 Careful = sp. def / sp. atk
        '24 Quirky = 0

        Dim AtkIv As Integer = frmLists.ListBox11.SelectedItem
        Dim BaseAtk As Integer = frmSelection.BaseAtk.Text
        Dim AtkEv As Integer = frmSelection.evAtkTxtBox.Text
        Dim Level As Integer = frmSelection.pkmnLevel.Text
        Dim Sum As Integer

        Dim nature As String = frmSelection.pkmnNature.SelectedIndex

        frmSelection.AtkMinMax.Text = AtkIv - 1 & " - " & AtkIv + 1

        If nature = 1 Or 2 Or 3 Or 4 Then
            Sum = ((AtkIv + (2 * BaseAtk) + AtkEv / 4) * Level / 100 + 5) * 1%

            frmSelection.IvAtk.Text = Sum
            frmSelection.IvAtkBar.Value = AtkIv
            frmSelection.LvAtk.Text = AtkIv
        ElseIf nature = 5 Or 10 Or 15 Or 20 Then
            Sum = ((AtkIv + (2 * BaseAtk) + AtkEv / 4) * Level / 100 + 5) * -1%

            frmSelection.IvAtk.Text = Sum
            frmSelection.IvAtkBar.Value = AtkIv
            frmSelection.LvAtk.Text = AtkIv
        Else
            Sum = ((AtkIv + (2 * BaseAtk) + AtkEv / 4) * Level / 100 + 5)

            frmSelection.IvAtk.Text = Sum
            frmSelection.IvAtkBar.Value = AtkIv
            frmSelection.LvAtk.Text = AtkIv
        End If
    End Sub

    Public Sub CalcDef()
        'Formula GEN V
        'Other Stats = ([IV + (2 x Base) + EV/4] x Level / 100 + 5) x Nature

        '0 Hardy = 0
        '1 Lonely = atk / def
        '2 Brave = atk / spd
        '3 Adament = atk / sp. atk
        '4 Naughty = atk / sp. def
        '5 Bold = def / atk
        '6 Docile = 0
        '7 Relaxed = def / spd
        '8 Impish = def / sp. atk
        '9 Lax = def / sp. def
        '10 Timid = spd / atk
        '11 Hasty = spd / def
        '12 Serious = 0
        '13 Jolly = spd / sp.atk
        '14 Naive = spd / sp. def
        '15 Modest = sp. atk / atk
        '16 Mild = sp. atk / def
        '17 Quiet = sp. atk / spd
        '18 Bashful = 0
        '19 Rash = sp. atk / sp.def
        '20 Calm = sp. def / atk
        '21 Gentle = sp. def / def
        '22 Sassy = sp. def / spd
        '23 Careful = sp. def / sp. atk
        '24 Quirky = 0

        Dim DefIv As Integer = frmLists.ListBox12.SelectedItem
        Dim BaseDef As Integer = frmSelection.BaseDef.Text
        Dim DefEv As Integer = frmSelection.evDefTxtBox.Text
        Dim Level As Integer = frmSelection.pkmnLevel.Text
        Dim Sum As Integer

        Dim nature As String = frmSelection.pkmnNature.SelectedIndex

        frmSelection.DefMinMax.Text = DefIv - 1 & " - " & DefIv + 1

        If nature = 6 Or 7 Or 8 Or 9 Then
            Sum = ((DefIv + (2 * BaseDef) + DefEv / 4) * Level / 100 + 5) * 1%

            frmSelection.IvDef.Text = Sum
            frmSelection.IvDefBar.Value = DefIv
            frmSelection.LvDef.Text = DefIv
        ElseIf nature = 1 Or 11 Or 16 Or 21 Then
            Sum = ((DefIv + (2 * BaseDef) + DefEv / 4) * Level / 100 + 5) * -1%

            frmSelection.IvDef.Text = Sum
            frmSelection.IvDefBar.Value = DefIv
            frmSelection.LvDef.Text = DefIv
        Else
            Sum = ((DefIv + (2 * BaseDef) + DefEv / 4) * Level / 100 + 5)

            frmSelection.IvDef.Text = Sum
            frmSelection.IvDefBar.Value = DefIv
            frmSelection.LvAtk.Text = DefIv
        End If
    End Sub

    Public Sub CalcSpd()
        'Formula GEN V
        'Other Stats = ([IV + (2 x Base) + EV/4] x Level / 100 + 5) x Nature

        '0 Hardy = 0
        '1 Lonely = atk / def
        '2 Brave = atk / spd
        '3 Adament = atk / sp. atk
        '4 Naughty = atk / sp. def
        '5 Bold = def / atk
        '6 Docile = 0
        '7 Relaxed = def / spd
        '8 Impish = def / sp. atk
        '9 Lax = def / sp. def
        '10 Timid = spd / atk
        '11 Hasty = spd / def
        '12 Serious = 0
        '13 Jolly = spd / sp.atk
        '14 Naive = spd / sp. def
        '15 Modest = sp. atk / atk
        '16 Mild = sp. atk / def
        '17 Quiet = sp. atk / spd
        '18 Bashful = 0
        '19 Rash = sp. atk / sp.def
        '20 Calm = sp. def / atk
        '21 Gentle = sp. def / def
        '22 Sassy = sp. def / spd
        '23 Careful = sp. def / sp. atk
        '24 Quirky = 0

        Dim spdIv As Integer = frmLists.ListBox13.SelectedItem
        Dim Basespd As Integer = frmSelection.BaseSpd.Text
        Dim spdEv As Integer = frmSelection.evSpdTxtBox.Text
        Dim Level As Integer = frmSelection.pkmnLevel.Text
        Dim Sum As Integer

        Dim nature As String = frmSelection.pkmnNature.SelectedIndex

        frmSelection.SpdMinMax.Text = spdIv - 1 & " - " & spdIv + 1

        If nature = 10 Or 11 Or 13 Or 14 Then
            Sum = ((spdIv + (2 * Basespd) + spdEv / 4) * Level / 100 + 5) * 1%

            frmSelection.IvSpd.Text = Sum
            frmSelection.IvSpdBar.Value = spdIv
            frmSelection.LvSpd.Text = spdIv
        ElseIf nature = 2 Or 7 Or 17 Or 22 Then
            Sum = ((spdIv + (2 * Basespd) + spdEv / 4) * Level / 100 + 5) * -1%

            frmSelection.IvSpd.Text = Sum
            frmSelection.IvSpdBar.Value = spdIv
            frmSelection.LvSpd.Text = spdIv
        Else
            Sum = ((spdIv + (2 * Basespd) + spdEv / 4) * Level / 100 + 5)

            frmSelection.IvSpd.Text = Sum
            frmSelection.IvSpdBar.Value = spdIv
            frmSelection.LvAtk.Text = spdIv
        End If
    End Sub

    Public Sub CalcSpAtk()
        'Formula GEN V
        'Other Stats = ([IV + (2 x Base) + EV/4] x Level / 100 + 5) x Nature

        '0 Hardy = 0
        '1 Lonely = atk / def
        '2 Brave = atk / spd
        '3 Adament = atk / sp. atk
        '4 Naughty = atk / sp. def
        '5 Bold = def / atk
        '6 Docile = 0
        '7 Relaxed = def / spd
        '8 Impish = def / sp. atk
        '9 Lax = def / sp. def
        '10 Timid = spd / atk
        '11 Hasty = spd / def
        '12 Serious = 0
        '13 Jolly = spd / sp.atk
        '14 Naive = spd / sp. def
        '15 Modest = sp. atk / atk
        '16 Mild = sp. atk / def
        '17 Quiet = sp. atk / spd
        '18 Bashful = 0
        '19 Rash = sp. atk / sp.def
        '20 Calm = sp. def / atk
        '21 Gentle = sp. def / def
        '22 Sassy = sp. def / spd
        '23 Careful = sp. def / sp. atk
        '24 Quirky = 0

        Dim spatkIv As Integer = frmLists.ListBox14.SelectedItem
        Dim Basespatk As Integer = frmSelection.BaseSpatk.Text
        Dim spatkEv As Integer = frmSelection.evSpatkTxtBox.Text
        Dim Level As Integer = frmSelection.pkmnLevel.Text
        Dim Sum As Integer

        Dim nature As String = frmSelection.pkmnNature.SelectedIndex

        frmSelection.SpatkMinMax.Text = spatkIv - 1 & " - " & spatkIv + 1

        If nature = 15 Or 14 Or 17 Or 19 Then
            Sum = ((spatkIv + (2 * Basespatk) + spatkEv / 4) * Level / 100 + 5) * 1%

            frmSelection.IvSpatk.Text = Sum
            frmSelection.IvSpatkBar.Value = spatkIv
            frmSelection.LvSpatk.Text = spatkIv
        ElseIf nature = 3 Or 8 Or 13 Or 23 Then
            Sum = ((spatkIv + (2 * Basespatk) + spatkEv / 4) * Level / 100 + 5) * -1%

            frmSelection.IvSpatk.Text = Sum
            frmSelection.IvSpatkBar.Value = spatkIv
            frmSelection.LvSpatk.Text = spatkIv
        Else
            Sum = ((spatkIv + (2 * Basespatk) + spatkEv / 4) * Level / 100 + 5)

            frmSelection.IvSpatk.Text = Sum
            frmSelection.IvSpatkBar.Value = spatkIv
            frmSelection.LvAtk.Text = spatkIv
        End If
    End Sub

    Public Sub CalcSpDef()
        'Formula GEN V
        'Other Stats = ([IV + (2 x Base) + EV/4] x Level / 100 + 5) x Nature

        '0 Hardy = 0
        '1 Lonely = atk / def
        '2 Brave = atk / spd
        '3 Adament = atk / sp. atk
        '4 Naughty = atk / sp. def
        '5 Bold = def / atk
        '6 Docile = 0
        '7 Relaxed = def / spd
        '8 Impish = def / sp. atk
        '9 Lax = def / sp. def
        '10 Timid = spd / atk
        '11 Hasty = spd / def
        '12 Serious = 0
        '13 Jolly = spd / sp.atk
        '14 Naive = spd / sp. def
        '15 Modest = sp. atk / atk
        '16 Mild = sp. atk / def
        '17 Quiet = sp. atk / spd
        '18 Bashful = 0
        '19 Rash = sp. atk / sp.def
        '20 Calm = sp. def / atk
        '21 Gentle = sp. def / def
        '22 Sassy = sp. def / spd
        '23 Careful = sp. def / sp. atk
        '24 Quirky = 0

        Dim spdefIv As Integer = frmLists.ListBox15.SelectedItem
        Dim Basespdef As Integer = frmSelection.BaseSpdef.Text
        Dim spdefEv As Integer = frmSelection.evSpdefTxtBox.Text
        Dim Level As Integer = frmSelection.pkmnLevel.Text
        Dim Sum As Integer

        Dim nature As String = frmSelection.pkmnNature.SelectedIndex

        frmSelection.SpdefMinMax.Text = spdefIv - 1 & " - " & spdefIv + 1

        If nature = 20 Or 21 Or 22 Or 23 Then
            Sum = ((spdefIv + (2 * Basespdef) + spdefEv / 4) * Level / 100 + 5) * 1%

            frmSelection.IvSpdef.Text = Sum
            frmSelection.IvSpdefBar.Value = spdefIv
            frmSelection.LvSpdef.Text = spdefIv
        ElseIf nature = 4 Or 9 Or 14 Or 19 Then
            Sum = ((spdefIv + (2 * Basespdef) + spdefEv / 4) * Level / 100 + 5) * -1%

            frmSelection.IvSpdef.Text = Sum
            frmSelection.IvSpdefBar.Value = spdefIv
            frmSelection.LvSpdef.Text = spdefIv
        Else
            Sum = ((spdefIv + (2 * Basespdef) + spdefEv / 4) * Level / 100 + 5)

            frmSelection.IvSpdef.Text = Sum
            frmSelection.IvSpdefBar.Value = spdefIv
            frmSelection.LvAtk.Text = spdefIv
        End If
    End Sub

    Public Sub CalcAvg()
        Dim average As Integer
        Dim sum As Integer
        Dim hp As Integer = frmSelection.LvHp.Text
        Dim atk As Integer = frmSelection.LvAtk.Text
        Dim def As Integer = frmSelection.LvDef.Text
        Dim spd As Integer = frmSelection.LvSpd.Text
        Dim spatk As Integer = frmSelection.LvSpatk.Text
        Dim spdef As Integer = frmSelection.LvDef.Text

        sum = hp + atk + def + spd + spatk + spdef
        average = sum / 6

        frmSelection.TotalIv.Text = average
        frmSelection.IvTotalBar.Value = average
    End Sub
End Module
