Public Class frmAuth

    Private Sub PictureBox9_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox9.Click
        Authenticate()
    End Sub

    Private Sub PictureBox9_MouseHover(sender As Object, e As System.EventArgs) Handles PictureBox9.MouseHover
        PictureBox9.Image = My.Resources.authenticate2
    End Sub

    Private Sub PictureBox9_MouseLeave(sender As Object, e As System.EventArgs) Handles PictureBox9.MouseLeave
        PictureBox9.Image = My.Resources.authenticate1
    End Sub

    Private Sub frmAuth_FormClosing(sender As Object, e As System.Windows.Forms.FormClosingEventArgs) Handles Me.FormClosing
        Closeing()
        frmLogin.Show()
    End Sub

    Private Sub Form3_Load(sender As System.Object, e As System.EventArgs) Handles MyBase.Load
        LoadCon()
        TextBox1.BringToFront()
        TextBox2.BringToFront()
        TextBox3.BringToFront()
        TextBox4.BringToFront()
    End Sub
End Class