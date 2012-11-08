Public Class frmMall
    Dim appData As String = System.Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData)
    Private Sub frmMain_FormClosing(sender As Object, e As System.Windows.Forms.FormClosingEventArgs) Handles Me.FormClosing
        Closeing()
        frmLogin.Close()
        frmAuth.Close()
    End Sub

    Private Sub frmMain_Load(sender As System.Object, e As System.EventArgs) Handles MyBase.Load
        'frmLists.Show()
        Timer1.Start()
        LoadCon()
        LoadUser()
        GetHotItems()
        LoadHotItems()
        GetPotions()
        GetMedicine()
        GetPokeball()
        GetField()
        GetEvolution()
        GetTM()
        GetFood()
        LoadPotions()
        frmLists.CATEGORYToolStripMenuItem.Text = "Potions"

        ' Download current time
        ' We download time so users cant cheat by changing the time on thier clock systems.
        Try
            ' Format
            ' HH
            ' MM
            If My.Computer.FileSystem.FileExists(appData & "/malltime.php") = True Then
                My.Computer.FileSystem.DeleteFile(appData & "/malltime.php")
                My.Computer.Network.DownloadFile _
                              ("http://pokemonium.com/functions/MallTime.php", _
                           appData & "/malltime.php")
            ElseIf My.Computer.FileSystem.FileExists(appData & "/malltime.php") = False Then
                My.Computer.Network.DownloadFile _
                               ("http://pokemonium.com/functions/MallTime.php", _
                            appData & "/malltime.php")
            End If
        Catch ex As Exception
            MsgBox(ex.Message)
        End Try
    End Sub

    Private Sub PictureBox18_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox18.Click
        frmBuyDP.Show()
    End Sub

    Private Sub PictureBox18_MouseHover(sender As Object, e As System.EventArgs) Handles PictureBox18.MouseHover
        PictureBox18.Image = My.Resources.getdp2
    End Sub

    Private Sub PictureBox18_MouseLeave(sender As Object, e As System.EventArgs) Handles PictureBox18.MouseLeave
        PictureBox18.Image = My.Resources.getdp1
    End Sub

    Private Sub PictureBox11_Click(sender As System.Object, e As System.EventArgs)

    End Sub

    Private Sub PictureBox2_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox2.Click
        frmLists.CATEGORYToolStripMenuItem.Text = "Hot"

        frmLists.ToolStripMenuItem2.Text = "0"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub PictureBox11_Click_1(sender As System.Object, e As System.EventArgs) Handles PictureBox11.Click
        ComboBox1.Text = "1"
        LoadPotions()
        frmLists.CATEGORYToolStripMenuItem.Text = "Potions"
        If Panel2.Visible = True Then Panel2.Visible = False
    End Sub

    Private Sub PictureBox12_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox12.Click
        ComboBox1.Text = "1"
        LoadMedicine()
        frmLists.CATEGORYToolStripMenuItem.Text = "Medicine"
        If Panel2.Visible = True Then Panel2.Visible = False
    End Sub

    Private Sub PictureBox13_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox13.Click
        ComboBox1.Text = "1"
        LoadPokeball()
        frmLists.CATEGORYToolStripMenuItem.Text = "Pokeball"
        If Panel2.Visible = True Then Panel2.Visible = False
    End Sub

    Private Sub PictureBox14_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox14.Click
        ComboBox1.Text = "1"
        LoadField()
        frmLists.CATEGORYToolStripMenuItem.Text = "Field"
        If Panel2.Visible = True Then Panel2.Visible = False
    End Sub

    Private Sub PictureBox15_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox15.Click
        ComboBox1.Text = "1"
        LoadEvolution()
        frmLists.CATEGORYToolStripMenuItem.Text = "Evolution"
        If Panel2.Visible = True Then Panel2.Visible = False
    End Sub

    Private Sub PictureBox16_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox16.Click
        ComboBox1.Text = "1"
        LoadTM()
        frmLists.CATEGORYToolStripMenuItem.Text = "TM"
        If Panel2.Visible = True Then Panel2.Visible = False
    End Sub

    Private Sub PictureBox19_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox19.Click
        ComboBox1.Text = "1"
        LoadFood()
        frmLists.CATEGORYToolStripMenuItem.Text = "Food"
        If Panel2.Visible = True Then Panel2.Visible = False
    End Sub

    Private Sub PictureBox21_Click(sender As System.Object, e As System.EventArgs) Handles buyBtn.Click
        frmBuy.Show()
    End Sub

    Private Sub PictureBox21_MouseHover(sender As Object, e As System.EventArgs) Handles buyBtn.MouseHover
        buyBtn.Image = My.Resources.buy2
    End Sub

    Private Sub PictureBox21_MouseLeave(sender As Object, e As System.EventArgs) Handles buyBtn.MouseLeave
        buyBtn.Image = My.Resources.buy1
    End Sub

    Private Sub LinkLabel1_LinkClicked(sender As System.Object, e As System.Windows.Forms.LinkLabelLinkClickedEventArgs) Handles LinkLabel1.LinkClicked
        ' Log out
        frmLogin.Show()
        Me.Close()
    End Sub

    Private Sub pb1_Click(sender As System.Object, e As System.EventArgs) Handles pb1.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "0"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub ComboBox1_SelectedIndexChanged(sender As System.Object, e As System.EventArgs) Handles ComboBox1.SelectedIndexChanged
        ' Maths
        Try
            totalValDP.Text = dpValue.Text * ComboBox1.Text
            totalValP.Text = pValue.Text * ComboBox1.Text
        Catch ex As Exception
            ' We dont need to see this error :P
        End Try

    End Sub

    Private Sub pb2_Click(sender As System.Object, e As System.EventArgs) Handles pb2.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "1"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb3_Click(sender As System.Object, e As System.EventArgs) Handles pb3.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "2"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb4_Click(sender As System.Object, e As System.EventArgs) Handles pb4.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "3"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb5_Click(sender As System.Object, e As System.EventArgs) Handles pb5.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "4"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub PictureBox20_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox20.Click
        ' Back Button
        ComboBox1.Text = "1"
        Panel2.Visible = False
    End Sub

    Private Sub pb6_Click(sender As System.Object, e As System.EventArgs) Handles pb6.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "5"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb7_Click(sender As System.Object, e As System.EventArgs) Handles pb7.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "6"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb8_Click(sender As System.Object, e As System.EventArgs) Handles pb8.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "7"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb9_Click(sender As System.Object, e As System.EventArgs) Handles pb9.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "8"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb10_Click(sender As System.Object, e As System.EventArgs) Handles pb10.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "9"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb11_Click(sender As System.Object, e As System.EventArgs) Handles pb11.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "10"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub PictureBox3_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox3.Click
        frmLists.CATEGORYToolStripMenuItem.Text = "Hot"

        frmLists.ToolStripMenuItem2.Text = "1"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub PictureBox4_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox4.Click
        frmLists.CATEGORYToolStripMenuItem.Text = "Hot"

        frmLists.ToolStripMenuItem2.Text = "2"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub PictureBox5_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox5.Click
        frmLists.CATEGORYToolStripMenuItem.Text = "Hot"

        frmLists.ToolStripMenuItem2.Text = "3"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub PictureBox6_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox6.Click
        frmLists.CATEGORYToolStripMenuItem.Text = "Hot"

        frmLists.ToolStripMenuItem2.Text = "4"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub PictureBox7_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox7.Click
        frmLists.CATEGORYToolStripMenuItem.Text = "Hot"

        frmLists.ToolStripMenuItem2.Text = "5"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub PictureBox8_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox8.Click
        frmLists.CATEGORYToolStripMenuItem.Text = "Hot"

        frmLists.ToolStripMenuItem2.Text = "6"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub PictureBox9_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox9.Click
        frmLists.CATEGORYToolStripMenuItem.Text = "Hot"

        frmLists.ToolStripMenuItem2.Text = "7"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub PictureBox10_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox10.Click
        frmLists.CATEGORYToolStripMenuItem.Text = "Hot"

        frmLists.ToolStripMenuItem2.Text = "8"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb12_Click(sender As System.Object, e As System.EventArgs) Handles pb12.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "11"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb13_Click(sender As System.Object, e As System.EventArgs) Handles pb13.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "12"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb14_Click(sender As System.Object, e As System.EventArgs) Handles pb14.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "13"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb15_Click(sender As System.Object, e As System.EventArgs) Handles pb15.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "14"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb16_Click(sender As System.Object, e As System.EventArgs) Handles pb16.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "15"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb17_Click(sender As System.Object, e As System.EventArgs) Handles pb17.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "16"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb18_Click(sender As System.Object, e As System.EventArgs) Handles pb18.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "17"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb19_Click(sender As System.Object, e As System.EventArgs) Handles pb19.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "18"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb20_Click(sender As System.Object, e As System.EventArgs) Handles pb20.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "19"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb21_Click(sender As System.Object, e As System.EventArgs) Handles pb21.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "20"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb22_Click(sender As System.Object, e As System.EventArgs) Handles pb22.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "21"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb23_Click(sender As System.Object, e As System.EventArgs) Handles pb23.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "22"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb24_Click(sender As System.Object, e As System.EventArgs) Handles pb24.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "23"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb25_Click(sender As System.Object, e As System.EventArgs) Handles pb25.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "24"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub

    Private Sub pb26_Click(sender As System.Object, e As System.EventArgs) Handles pb26.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "25"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb27_Click(sender As System.Object, e As System.EventArgs) Handles pb27.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "26"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb28_Click(sender As System.Object, e As System.EventArgs) Handles pb28.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "27"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb29_Click(sender As System.Object, e As System.EventArgs) Handles pb29.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "28"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb30_Click(sender As System.Object, e As System.EventArgs) Handles pb30.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "29"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb31_Click(sender As System.Object, e As System.EventArgs) Handles pb31.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "30"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb32_Click(sender As System.Object, e As System.EventArgs) Handles pb32.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "31"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb33_Click(sender As System.Object, e As System.EventArgs) Handles pb33.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "32"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb34_Click(sender As System.Object, e As System.EventArgs) Handles pb34.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "33"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb35_Click(sender As System.Object, e As System.EventArgs) Handles pb35.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "34"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb36_Click(sender As System.Object, e As System.EventArgs) Handles pb36.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "35"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb37_Click(sender As System.Object, e As System.EventArgs) Handles pb37.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "36"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb38_Click(sender As System.Object, e As System.EventArgs) Handles pb38.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "37"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub p39_Click(sender As System.Object, e As System.EventArgs) Handles pb39.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "38"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb40_Click(sender As System.Object, e As System.EventArgs) Handles pb40.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "39"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb41_Click(sender As System.Object, e As System.EventArgs) Handles pb41.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "40"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb42_Click(sender As System.Object, e As System.EventArgs) Handles pb42.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "41"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb43_Click(sender As System.Object, e As System.EventArgs) Handles pb43.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "42"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb44_Click(sender As System.Object, e As System.EventArgs) Handles pb44.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "43"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb45_Click(sender As System.Object, e As System.EventArgs) Handles pb45.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "44"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb46_Click(sender As System.Object, e As System.EventArgs) Handles pb46.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "45"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb47_Click(sender As System.Object, e As System.EventArgs) Handles pb47.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "46"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb48_Click(sender As System.Object, e As System.EventArgs) Handles pb48.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "47"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub p49_Click(sender As System.Object, e As System.EventArgs) Handles pb49.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "48"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb50_Click(sender As System.Object, e As System.EventArgs) Handles pb50.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "49"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb51_Click(sender As System.Object, e As System.EventArgs) Handles pb51.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "50"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb52_Click(sender As System.Object, e As System.EventArgs) Handles pb52.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "51"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb53_Click(sender As System.Object, e As System.EventArgs) Handles pb53.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "52"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb54_Click(sender As System.Object, e As System.EventArgs) Handles pb54.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "53"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb55_Click(sender As System.Object, e As System.EventArgs) Handles pb55.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "54"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb56_Click(sender As System.Object, e As System.EventArgs) Handles pb56.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "55"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb57_Click(sender As System.Object, e As System.EventArgs) Handles pb57.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "56"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb58_Click(sender As System.Object, e As System.EventArgs) Handles pb58.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "57"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub p59_Click(sender As System.Object, e As System.EventArgs) Handles pb59.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "58"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb60_Click(sender As System.Object, e As System.EventArgs) Handles pb60.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "59"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb61_Click(sender As System.Object, e As System.EventArgs) Handles pb61.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "60"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb62_Click(sender As System.Object, e As System.EventArgs) Handles pb62.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "61"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb63_Click(sender As System.Object, e As System.EventArgs) Handles pb63.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "62"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb64_Click(sender As System.Object, e As System.EventArgs) Handles pb64.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "63"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb65_Click(sender As System.Object, e As System.EventArgs) Handles pb65.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "64"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb66_Click(sender As System.Object, e As System.EventArgs) Handles pb66.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "65"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb67_Click(sender As System.Object, e As System.EventArgs) Handles pb67.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "66"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb68_Click(sender As System.Object, e As System.EventArgs) Handles pb68.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "67"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub p69_Click(sender As System.Object, e As System.EventArgs) Handles pb69.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "68"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb70_Click(sender As System.Object, e As System.EventArgs) Handles pb70.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "69"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb71_Click(sender As System.Object, e As System.EventArgs) Handles pb71.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "70"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb72_Click(sender As System.Object, e As System.EventArgs) Handles pb72.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "71"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb73_Click(sender As System.Object, e As System.EventArgs) Handles pb73.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "72"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb74_Click(sender As System.Object, e As System.EventArgs) Handles pb74.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "73"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb75_Click(sender As System.Object, e As System.EventArgs) Handles pb75.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "74"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb76_Click(sender As System.Object, e As System.EventArgs) Handles pb76.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "75"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb77_Click(sender As System.Object, e As System.EventArgs) Handles pb77.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "76"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb78_Click(sender As System.Object, e As System.EventArgs) Handles pb78.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "77"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub p79_Click(sender As System.Object, e As System.EventArgs) Handles pb79.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "78"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb80_Click(sender As System.Object, e As System.EventArgs) Handles pb80.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "79"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb81_Click(sender As System.Object, e As System.EventArgs) Handles pb81.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "80"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb82_Click(sender As System.Object, e As System.EventArgs) Handles pb82.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "81"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb83_Click(sender As System.Object, e As System.EventArgs) Handles pb83.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "82"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb84_Click(sender As System.Object, e As System.EventArgs) Handles pb84.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "83"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb85_Click(sender As System.Object, e As System.EventArgs) Handles pb85.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "84"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb86_Click(sender As System.Object, e As System.EventArgs) Handles pb86.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "85"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb87_Click(sender As System.Object, e As System.EventArgs) Handles pb87.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "86"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb88_Click(sender As System.Object, e As System.EventArgs) Handles pb88.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "87"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub p89_Click(sender As System.Object, e As System.EventArgs) Handles pb89.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "88"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb90_Click(sender As System.Object, e As System.EventArgs) Handles pb90.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "89"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb91_Click(sender As System.Object, e As System.EventArgs) Handles pb91.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "90"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb92_Click(sender As System.Object, e As System.EventArgs) Handles pb92.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "91"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb93_Click(sender As System.Object, e As System.EventArgs) Handles pb93.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "92"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb94_Click(sender As System.Object, e As System.EventArgs) Handles pb94.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "93"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb95_Click(sender As System.Object, e As System.EventArgs) Handles pb95.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "94"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb96_Click(sender As System.Object, e As System.EventArgs) Handles pb96.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "95"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb97_Click(sender As System.Object, e As System.EventArgs) Handles pb97.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "96"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub pb98_Click(sender As System.Object, e As System.EventArgs) Handles pb98.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "97"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    Private Sub p99_Click(sender As System.Object, e As System.EventArgs) Handles pb99.Click
        ' This is for the loading of the items information on the purchase screen
        ' The number represents the listbox index # that represents it
        ' The category is so we can figure out exactly which list boxes to pull from
        frmLists.ToolStripMenuItem2.Text = "98"

        buyItemPB.BringToFront()
        buyItemPB.Visible = True

        ItemSelect()
    End Sub
    
    Private Sub Timer1_Tick(sender As System.Object, e As System.EventArgs) Handles Timer1.Tick
        If ComboBox1.Text <> "1" Then
            ' Maths
            Try
                totalValDP.Text = dpValue.Text * ComboBox1.Text
                totalValP.Text = pValue.Text * ComboBox1.Text
            Catch ex As Exception
                ' We dont need to see this error :P
            End Try

        End If
    End Sub
End Class