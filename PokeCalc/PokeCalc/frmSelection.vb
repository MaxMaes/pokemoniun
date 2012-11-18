Public Class frmSelection

    Private Sub frmSelection_FormClosing(sender As Object, e As FormClosingEventArgs) Handles Me.FormClosing
        Closeing()
        frmLogin.Close()
        frmAuth.Close()
    End Sub

    Private Sub frmSelection_Load(sender As Object, e As EventArgs) Handles Me.Load
        LoadPoke()
        Timer1.Start()
    End Sub

    Private Sub GhostButton1_Click(sender As Object, e As EventArgs) Handles GhostButton1.Click
        SelectPoke()
        GhostTabControl1.SelectedIndex = 1
    End Sub

    Private Sub btnChangeLvl_Click(sender As Object, e As EventArgs) Handles btnChangeLvl.Click
        ChangeLevel()
    End Sub

    Private Sub ClearEvHp_Click(sender As Object, e As EventArgs) Handles ClearEvHp.Click
        evHpTxtBox.Text = "0"
    End Sub

    Private Sub ClearEvAtk_Click(sender As Object, e As EventArgs) Handles ClearEvAtk.Click
        evAtkTxtBox.Text = "0"
    End Sub

    Private Sub ClearEvDef_Click(sender As Object, e As EventArgs) Handles ClearEvDef.Click
        evDefTxtBox.Text = "0"
    End Sub

    Private Sub ClearEvSpd_Click(sender As Object, e As EventArgs) Handles ClearEvSpd.Click
        evSpdTxtBox.Text = "0"
    End Sub

    Private Sub ClearEvSpatk_Click(sender As Object, e As EventArgs) Handles ClearEvSpatk.Click
        evSpatkTxtBox.Text = "0"
    End Sub

    Private Sub ClearEvSpdef_Click(sender As Object, e As EventArgs) Handles ClearEvSpdef.Click
        evSpdefTxtBox.Text = "0"
    End Sub

    Private Sub Timer1_Tick(sender As Object, e As EventArgs) Handles Timer1.Tick
        Dim sum As Integer
        Dim hp As Integer
        Dim atk As Integer
        Dim def As Integer
        Dim spd As Integer
        Dim spatk As Integer
        Dim spdef As Integer

        hp = evHpTxtBox.Text
        atk = evAtkTxtBox.Text
        def = evDefTxtBox.Text
        spd = evSpdTxtBox.Text
        spatk = evSpatkTxtBox.Text
        spdef = evSpdefTxtBox.Text

        sum = hp + atk + def + spd + spatk + spdef

        If hp > 255 Then
            MsgBox("Error: EVs can not go above 255.")
            evHpTxtBox.Text = 0
        ElseIf atk > 255 Then
            MsgBox("Error: EVs can not go above 255.")
            evAtkTxtBox.Text = 0
        ElseIf def > 255 Then
            MsgBox("Error: EVs can not go above 255.")
            evDefTxtBox.Text = 0
        ElseIf spd > 255 Then
            MsgBox("Error: EVs can not go above 255.")
            evSpdTxtBox.Text = 0
        ElseIf spatk > 255 Then
            MsgBox("Error: EVs can not go above 255.")
            evSpatkTxtBox.Text = 0
        ElseIf spdef > 255 Then
            MsgBox("Error: EVs can not go above 255.")
            evSpdefTxtBox.Text = 0
        ElseIf hp < 0 Then
            MsgBox("Error: EVs can not go below 0.")
            evHpTxtBox.Text = 0
        ElseIf atk < 0 Then
            MsgBox("Error: EVs can not go below 0.")
            evAtkTxtBox.Text = 0
        ElseIf def < 0 Then
            MsgBox("Error: EVs can not go below 0.")
            evDefTxtBox.Text = 0
        ElseIf spd < 0 Then
            MsgBox("Error: EVs can not go below 0.")
            evSpdTxtBox.Text = 0
        ElseIf spatk < 0 Then
            MsgBox("Error: EVs can not go below 0.")
            evSpatkTxtBox.Text = 0
        ElseIf spdef < 0 Then
            MsgBox("Error: EVs can not go below 0.")
            evSpdefTxtBox.Text = 0
        ElseIf sum < 0 Then
            MsgBox("Error: EVs can not go below 0.")
            evHpTxtBox.Text = 0
            evAtkTxtBox.Text = 0
            evDefTxtBox.Text = 0
            evSpdTxtBox.Text = 0
            evSpatkTxtBox.Text = 0
            evSpdefTxtBox.Text = 0
            evUsage.Text = 0
        ElseIf sum > 510 Then
            MsgBox("Error: Total EVs sum can not exceed 510")
            evHpTxtBox.Text = 0
            evAtkTxtBox.Text = 0
            evDefTxtBox.Text = 0
            evSpdTxtBox.Text = 0
            evSpatkTxtBox.Text = 0
            evSpdefTxtBox.Text = 0
            evUsage.Text = 0
        End If

        evUsage.Text = sum
        evUsageBar.Value = sum

        CalcHP()
        CalcAtk()
        CalcDef()
        CalcSpd()
        CalcSpAtk()
        CalcSpDef()
        CalcAvg()
    End Sub

    Private Sub FillEvHp_Click(sender As Object, e As EventArgs) Handles FillEvHp.Click
        Dim sum As Integer
        Dim hp As Integer
        Dim atk As Integer
        Dim def As Integer
        Dim spd As Integer
        Dim spatk As Integer
        Dim spdef As Integer

        hp = evHpTxtBox.Text
        atk = evAtkTxtBox.Text
        def = evDefTxtBox.Text
        spd = evSpdTxtBox.Text
        spatk = evSpatkTxtBox.Text
        spdef = evSpdefTxtBox.Text

        sum = hp + atk + def + spd + spatk + spdef

        Dim dif As Integer
        dif = 510 - sum

        If dif >= 252 Then
            evHpTxtBox.Text = 252
        Else
            evHpTxtBox.Text = dif
        End If
    End Sub

    Private Sub ClearAll_Click(sender As Object, e As EventArgs) Handles ClearAll.Click
        evHpTxtBox.Text = 0
        evAtkTxtBox.Text = 0
        evDefTxtBox.Text = 0
        evSpdTxtBox.Text = 0
        evSpatkTxtBox.Text = 0
        evSpdefTxtBox.Text = 0
        evUsage.Text = 0
        evUsageBar.value = 0
    End Sub

    Private Sub FillEvAtk_Click(sender As Object, e As EventArgs)
        Dim sum As Integer
        Dim hp As Integer
        Dim atk As Integer
        Dim def As Integer
        Dim spd As Integer
        Dim spatk As Integer
        Dim spdef As Integer

        hp = evHpTxtBox.Text
        atk = evAtkTxtBox.Text
        def = evDefTxtBox.Text
        spd = evSpdTxtBox.Text
        spatk = evSpatkTxtBox.Text
        spdef = evSpdefTxtBox.Text

        sum = hp + atk + def + spd + spatk + spdef

        Dim dif As Integer
        dif = 510 - sum

        If dif >= 252 Then
            evAtkTxtBox.Text = 252
        Else
            evAtkTxtBox.Text = dif
        End If
    End Sub

    Private Sub FillEvDef_Click(sender As Object, e As EventArgs) Handles FillEvDef.Click
        Dim sum As Integer
        Dim hp As Integer
        Dim atk As Integer
        Dim def As Integer
        Dim spd As Integer
        Dim spatk As Integer
        Dim spdef As Integer

        hp = evHpTxtBox.Text
        atk = evAtkTxtBox.Text
        def = evDefTxtBox.Text
        spd = evSpdTxtBox.Text
        spatk = evSpatkTxtBox.Text
        spdef = evSpdefTxtBox.Text

        sum = hp + atk + def + spd + spatk + spdef

        Dim dif As Integer
        dif = 510 - sum

        If dif >= 252 Then
            evDefTxtBox.Text = 252
        Else
            evDefTxtBox.Text = dif
        End If
    End Sub

    Private Sub FillEvSpd_Click(sender As Object, e As EventArgs) Handles FillEvSpd.Click
        Dim sum As Integer
        Dim hp As Integer
        Dim atk As Integer
        Dim def As Integer
        Dim spd As Integer
        Dim spatk As Integer
        Dim spdef As Integer

        hp = evHpTxtBox.Text
        atk = evAtkTxtBox.Text
        def = evDefTxtBox.Text
        spd = evSpdTxtBox.Text
        spatk = evSpatkTxtBox.Text
        spdef = evSpdefTxtBox.Text

        sum = hp + atk + def + spd + spatk + spdef

        Dim dif As Integer
        dif = 510 - sum

        If dif >= 252 Then
            evSpdTxtBox.Text = 252
        Else
            evSpdTxtBox.Text = dif
        End If
    End Sub

    Private Sub FillEvSpatk_Click(sender As Object, e As EventArgs) Handles FillEvSpatk.Click
        Dim sum As Integer
        Dim hp As Integer
        Dim atk As Integer
        Dim def As Integer
        Dim spd As Integer
        Dim spatk As Integer
        Dim spdef As Integer

        hp = evHpTxtBox.Text
        atk = evAtkTxtBox.Text
        def = evDefTxtBox.Text
        spd = evSpdTxtBox.Text
        spatk = evSpatkTxtBox.Text
        spdef = evSpdefTxtBox.Text

        sum = hp + atk + def + spd + spatk + spdef

        Dim dif As Integer
        dif = 510 - sum

        If dif >= 252 Then
            evSpatkTxtBox.Text = 252
        Else
            evSpatkTxtBox.Text = dif
        End If
    End Sub

    Private Sub FillEvSpdef_Click(sender As Object, e As EventArgs) Handles FillEvSpdef.Click
        Dim sum As Integer
        Dim hp As Integer
        Dim atk As Integer
        Dim def As Integer
        Dim spd As Integer
        Dim spatk As Integer
        Dim spdef As Integer

        hp = evHpTxtBox.Text
        atk = evAtkTxtBox.Text
        def = evDefTxtBox.Text
        spd = evSpdTxtBox.Text
        spatk = evSpatkTxtBox.Text
        spdef = evSpdefTxtBox.Text

        sum = hp + atk + def + spd + spatk + spdef

        Dim dif As Integer
        dif = 510 - sum

        If dif >= 252 Then
            evSpdefTxtBox.Text = 252
        Else
            evSpdefTxtBox.Text = dif
        End If
    End Sub

    Private Sub GhostComboBox1_SelectedIndexChanged(sender As Object, e As EventArgs) Handles GhostComboBox1.SelectedIndexChanged
        'Preset EV Spreads
        'Physical Sweeper
        'Special Sweeper
        'Physical Wall
        'Special Wall
        'Mixed Wall(HP)
        'Mixed Wall(Def)
        'Completely Even

        evHpTxtBox.Text = 0
        evAtkTxtBox.Text = 0
        evDefTxtBox.Text = 0
        evSpdTxtBox.Text = 0
        evSpatkTxtBox.Text = 0
        evSpdefTxtBox.Text = 0
        evUsage.Text = 0
        evUsageBar.Value = 0

        If GhostComboBox1.Text = "Physical Sweeper" Then
            evHpTxtBox.Text = 6
            evAtkTxtBox.Text = 252
            evSpdTxtBox.Text = 252
        ElseIf GhostComboBox1.Text = "Special Sweeper" Then
            evHpTxtBox.Text = 6
            evSpatkTxtBox.Text = 252
            evSpdTxtBox.Text = 252
        ElseIf GhostComboBox1.Text = "Physical Wall" Then
            evHpTxtBox.Text = 252
            evDefTxtBox.Text = 252
            evSpdefTxtBox.Text = 6
        ElseIf GhostComboBox1.Text = "Special Wall" Then
            evHpTxtBox.Text = 252
            evDefTxtBox.Text = 6
            evSpdefTxtBox.Text = 252
        ElseIf GhostComboBox1.Text = "Mixed Wall (HP)" Then
            evHpTxtBox.Text = 252
            evDefTxtBox.Text = 129
            evSpdefTxtBox.Text = 129
        ElseIf GhostComboBox2.Text = "Mixed Wall (Def)" Then
            evHpTxtBox.Text = 6
            evDefTxtBox.Text = 252
            evSpdefTxtBox.Text = 252
        ElseIf GhostComboBox1.Text = "Completely Even" Then
            evHpTxtBox.Text = 85
            evAtkTxtBox.Text = 85
            evDefTxtBox.Text = 85
            evSpdTxtBox.Text = 85
            evSpatkTxtBox.Text = 85
            evSpdefTxtBox.Text = 85
        End If
    End Sub

    Private Sub GhostButton2_Click(sender As Object, e As EventArgs) Handles GhostButton2.Click
        'Preset EV Spreads
        'Physical Sweeper
        'Special Sweeper
        'Physical Wall
        'Special Wall
        'Mixed Wall(HP)
        'Mixed Wall(Def)
        'Completely Even

        evHpTxtBox.Text = 0
        evAtkTxtBox.Text = 0
        evDefTxtBox.Text = 0
        evSpdTxtBox.Text = 0
        evSpatkTxtBox.Text = 0
        evSpdefTxtBox.Text = 0
        evUsage.Text = 0
        evUsageBar.Value = 0

        If GhostComboBox1.Text = "Physical Sweeper" Then
            evHpTxtBox.Text = 6
            evAtkTxtBox.Text = 252
            evSpdTxtBox.Text = 252
        ElseIf GhostComboBox1.Text = "Special Sweeper" Then
            evHpTxtBox.Text = 6
            evSpatkTxtBox.Text = 252
            evSpdTxtBox.Text = 252
        ElseIf GhostComboBox1.Text = "Physical Wall" Then
            evHpTxtBox.Text = 252
            evDefTxtBox.Text = 252
            evSpdefTxtBox.Text = 6
        ElseIf GhostComboBox1.Text = "Special Wall" Then
            evHpTxtBox.Text = 252
            evDefTxtBox.Text = 6
            evSpdefTxtBox.Text = 252
        ElseIf GhostComboBox1.Text = "Mixed Wall (HP)" Then
            evHpTxtBox.Text = 252
            evDefTxtBox.Text = 129
            evSpdefTxtBox.Text = 129
        ElseIf GhostComboBox2.Text = "Mixed Wall (Def)" Then
            evHpTxtBox.Text = 6
            evDefTxtBox.Text = 252
            evSpdefTxtBox.Text = 252
        ElseIf GhostComboBox1.Text = "Completely Even" Then
            evHpTxtBox.Text = 85
            evAtkTxtBox.Text = 85
            evDefTxtBox.Text = 85
            evSpdTxtBox.Text = 85
            evSpatkTxtBox.Text = 85
            evSpdefTxtBox.Text = 85
        End If
    End Sub

End Class