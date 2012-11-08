Public Class frmLogin

    Private Sub frmLogin_FormClosing(sender As Object, e As System.Windows.Forms.FormClosingEventArgs) Handles Me.FormClosing
        Closeing()
    End Sub

    Private Sub Form1_Load(sender As System.Object, e As System.EventArgs) Handles MyBase.Load
        ' Check remember me function
        If My.Settings.enabled = "True" Then
            TextBox1.Text = My.Settings.username
            TextBox2.Text = My.Settings.password
            CheckBox1.Checked = True
        End If

        LoadCon()
        TextBox2.BringToFront()
        TextBox1.BringToFront()
    End Sub

    Private Sub PictureBox5_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox5.Click
        Login()
    End Sub

    ' Rollover
    Private Sub PictureBox5_MouseHover(sender As Object, e As System.EventArgs) Handles PictureBox5.MouseHover
        PictureBox5.Image = My.Resources.login2
    End Sub

    ' Rollover
    Private Sub PictureBox5_MouseLeave(sender As Object, e As System.EventArgs) Handles PictureBox5.MouseLeave
        PictureBox5.Image = My.Resources.login1
    End Sub

    Private Sub PictureBox6_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox6.Click
        frmAuth.Show()
        Me.Hide()
    End Sub

    ' Rollover
    Private Sub PictureBox6_MouseHover(sender As Object, e As System.EventArgs) Handles PictureBox6.MouseHover
        PictureBox6.Image = My.Resources.authenticate2
    End Sub

    ' Rollover
    Private Sub PictureBox6_MouseLeave(sender As Object, e As System.EventArgs) Handles PictureBox6.MouseLeave
        PictureBox6.Image = My.Resources.authenticate1
    End Sub

    Private Sub CheckBox1_CheckedChanged(sender As System.Object, e As System.EventArgs) Handles CheckBox1.CheckedChanged
        If CheckBox1.Checked = True Then
            My.Settings.username = TextBox1.Text
            My.Settings.password = TextBox2.Text
            My.Settings.enabled = "True"
            My.Settings.Save()
        Else
            My.Settings.enabled = "False"
            My.Settings.Save()
        End If
    End Sub
End Class
