Imports System.IO
Public Class frmBuyDP

    Private Sub PictureBox1_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox1.Click
        frmWindow.WebBrowser1.Navigate("http://www.xtremetop100.com/in.php?site=1132338103")
        XtremeVote()
    End Sub

    Private Sub buyBtn_Click(sender As System.Object, e As System.EventArgs) Handles buyBtn.Click
        '5
        frmWindow.WebBrowser1.Navigate("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=T39FP797LR7ZC")
        BuyDP5()
        frmWindow.Show()
    End Sub

    Private Sub PictureBox2_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox2.Click
        '10
        frmWindow.WebBrowser1.Navigate("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GR53Q2YCYUZTA")
        BuyDP10()
        frmWindow.Show()
    End Sub

    Private Sub PictureBox3_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox3.Click
        '20
        frmWindow.WebBrowser1.Navigate("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=AMNKJW7E9YB22")
        BuyDP20()
        frmWindow.Show()
    End Sub

    Private Sub PictureBox4_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox4.Click
        '50
        frmWindow.WebBrowser1.Navigate("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=QUGRLY3EJDG9L")
        BuyDP50()
        frmWindow.Show()
    End Sub

    Private Sub PictureBox5_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox5.Click
        '100
        frmWindow.WebBrowser1.Navigate("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=X7HZ5L3WDHXLY")
        BuyDP100()
        frmWindow.Show()
    End Sub

    Private Sub PictureBox6_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox6.Click
        frmWindow.WebBrowser1.Navigate("http://www.gamesites100.net/in.php?site=24440")
        GameSitesVote()
    End Sub

    Private Sub PictureBox7_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox7.Click
        frmWindow.WebBrowser1.Navigate("http://www.gtop100.com/in.php?site=74631")
        gTop100Vote()
    End Sub

    Private Sub PictureBox8_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox8.Click
        frmWindow.WebBrowser1.Navigate("http://www.mmorpgtoplist.com/in.php?site=57303")
        mmoVote()
    End Sub

    Private Sub PictureBox9_Click(sender As System.Object, e As System.EventArgs) Handles PictureBox9.Click
        frmWindow.WebBrowser1.Navigate("http://www.top100arena.com/in.asp?id=79274 ")
        Top100Vote()
    End Sub
End Class