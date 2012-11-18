Public Class frmLogin

    Private Sub frmLogin_FormClosing(sender As Object, e As FormClosingEventArgs) Handles Me.FormClosing
        Closeing()
        frmAuth.Close()
    End Sub

    Private Sub frmLogin_Load(sender As Object, e As EventArgs) Handles Me.Load
        If My.Settings.Tick = "True" Then
            GhostTextBox1.Text = My.Settings.Username
            GhostTextBox2.Text = My.Settings.Password
            GhostCheckbox1.Checked = True
        End If

        LoadCon()

        AcceptButton = GhostButton1
    End Sub

    Private Sub GhostButton1_Click(sender As Object, e As EventArgs) Handles GhostButton1.Click
        Login()
    End Sub

    Private Sub GhostCheckbox1_CheckedChanged(sender As Object) Handles GhostCheckbox1.CheckedChanged
        If GhostCheckbox1.Checked = True Then
            My.Settings.Username = GhostTextBox1.Text
            My.Settings.Password = GhostTextBox2.Text
            My.Settings.Tick = "True"
            My.Settings.Save()
        Else
            My.Settings.Tick = "False"
            My.Settings.Save()
        End If
    End Sub

    Private Sub GhostButton2_Click(sender As Object, e As EventArgs) Handles GhostButton2.Click
        frmAuth.Show()
        Me.Hide()
    End Sub
End Class
