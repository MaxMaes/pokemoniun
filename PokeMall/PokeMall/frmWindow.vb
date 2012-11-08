Public Class frmWindow
    Dim URL As String

    Private Sub WebBrowser1_DocumentCompleted(sender As System.Object, e As System.Windows.Forms.WebBrowserDocumentCompletedEventArgs) Handles WebBrowser1.DocumentCompleted
        URL = (WebBrowser1.Url.ToString())
    End Sub

    Private Sub Timer1_Tick(sender As System.Object, e As System.EventArgs) Handles Timer1.Tick
        If URL = "http://www.xtremetop100.com/mmorpg-&-mpog" Then
            Timer1.Stop()
            Me.Hide()
            AddVoteDP()
        ElseIf URL = "http://www.gtop100.com/mmorpgmpog" Then
            Timer1.Stop()
            Me.Hide()
            AddVoteDP()
        ElseIf URL = "http://www.gamesites100.net/mmorpgmpog" Then
            Timer1.Stop()
            Me.Hide()
            AddVoteDP()
        ElseIf URL = "http://www.mmorpgtoplist.com/mmorpg-mpog" Then
            Timer1.Stop()
            Me.Hide()
            AddVoteDP()
        ElseIf URL = "http://www.top100arena.com/mmorpg/" Then
            Timer1.Stop()
            Me.Hide()
            AddVoteDP()
        End If
    End Sub

    Private Sub frmWindow_Load(sender As System.Object, e As System.EventArgs) Handles MyBase.Load
        Timer1.Start()
    End Sub
End Class