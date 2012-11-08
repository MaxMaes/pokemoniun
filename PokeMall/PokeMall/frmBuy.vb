Public Class frmBuy

    Private Sub Button1_Click(sender As System.Object, e As System.EventArgs) Handles Button1.Click
        ' First make sure the user is logged out of the game.
        MsgBox("Are you logged out of the game?", MsgBoxStyle.Critical + MsgBoxStyle.YesNo, "WARNING")

        If MsgBoxResult.Yes Then
            BuyItemDP()
        Else
            Me.Hide()
        End If
    End Sub

    Private Sub Button2_Click(sender As System.Object, e As System.EventArgs) Handles Button2.Click
        ' First make sure the user is logged out of the game.
        MsgBox("Are you logged out of the game?", MsgBoxStyle.Critical + MsgBoxStyle.YesNo, "WARNING")

        If MsgBoxResult.Yes Then
            BuyItemP()
        Else
            Me.Hide()
        End If
    End Sub
End Class