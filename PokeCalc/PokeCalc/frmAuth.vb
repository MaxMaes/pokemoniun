Public Class frmAuth


    Private Sub GhostButton1_Click(sender As Object, e As EventArgs) Handles GhostButton1.Click
        Authenticate()
    End Sub

    Private Sub frmAuth_FormClosing(sender As Object, e As FormClosingEventArgs) Handles Me.FormClosing
        Closeing()
        frmLogin.Show()
    End Sub

    Private Sub frmAuth_Load(sender As Object, e As EventArgs) Handles Me.Load
        LoadCon()
    End Sub

    Private Sub Label1_MouseHover(sender As Object, e As EventArgs)

    End Sub

    Private Sub Label1_Click(sender As Object, e As EventArgs)

    End Sub
End Class