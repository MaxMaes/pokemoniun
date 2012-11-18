<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class frmAuth
    Inherits System.Windows.Forms.Form

    'Form overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()> _
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()> _
    Private Sub InitializeComponent()
        Me.components = New System.ComponentModel.Container()
        Dim resources As System.ComponentModel.ComponentResourceManager = New System.ComponentModel.ComponentResourceManager(GetType(frmAuth))
        Me.GhostTheme1 = New PokeCalc.GhostTheme()
        Me.Label1 = New System.Windows.Forms.Label()
        Me.GhostButton1 = New PokeCalc.GhostButton()
        Me.GhostTextBox4 = New PokeCalc.GhostTextBox()
        Me.GhostTextBox3 = New PokeCalc.GhostTextBox()
        Me.GhostTextBox2 = New PokeCalc.GhostTextBox()
        Me.GhostTextBox1 = New PokeCalc.GhostTextBox()
        Me.GhostControlBox1 = New PokeCalc.GhostControlBox()
        Me.ToolTip1 = New System.Windows.Forms.ToolTip(Me.components)
        Me.GhostTheme1.SuspendLayout()
        Me.SuspendLayout()
        '
        'GhostTheme1
        '
        Me.GhostTheme1.BorderStyle = System.Windows.Forms.FormBorderStyle.None
        Me.GhostTheme1.Colors = New PokeCalc.Bloom(-1) {}
        Me.GhostTheme1.Controls.Add(Me.Label1)
        Me.GhostTheme1.Controls.Add(Me.GhostButton1)
        Me.GhostTheme1.Controls.Add(Me.GhostTextBox4)
        Me.GhostTheme1.Controls.Add(Me.GhostTextBox3)
        Me.GhostTheme1.Controls.Add(Me.GhostTextBox2)
        Me.GhostTheme1.Controls.Add(Me.GhostTextBox1)
        Me.GhostTheme1.Controls.Add(Me.GhostControlBox1)
        Me.GhostTheme1.Customization = ""
        Me.GhostTheme1.Dock = System.Windows.Forms.DockStyle.Fill
        Me.GhostTheme1.Font = New System.Drawing.Font("Verdana", 8.0!)
        Me.GhostTheme1.Image = Nothing
        Me.GhostTheme1.Location = New System.Drawing.Point(0, 0)
        Me.GhostTheme1.Movable = True
        Me.GhostTheme1.Name = "GhostTheme1"
        Me.GhostTheme1.NoRounding = False
        Me.GhostTheme1.ShowIcon = False
        Me.GhostTheme1.Sizable = False
        Me.GhostTheme1.Size = New System.Drawing.Size(198, 199)
        Me.GhostTheme1.SmartBounds = True
        Me.GhostTheme1.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.GhostTheme1.TabIndex = 0
        Me.GhostTheme1.Text = "PokeCalc - Authentication"
        Me.GhostTheme1.TransparencyKey = System.Drawing.Color.Fuchsia
        Me.GhostTheme1.Transparent = False
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.BackColor = System.Drawing.Color.Transparent
        Me.Label1.Font = New System.Drawing.Font("Calibri", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label1.ForeColor = System.Drawing.Color.White
        Me.Label1.Location = New System.Drawing.Point(57, 175)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(81, 13)
        Me.Label1.TabIndex = 6
        Me.Label1.Text = "[ What do i do? ]"
        Me.ToolTip1.SetToolTip(Me.Label1, resources.GetString("Label1.ToolTip"))
        '
        'GhostButton1
        '
        Me.GhostButton1.Color = System.Drawing.Color.Blue
        Me.GhostButton1.Colors = New PokeCalc.Bloom(-1) {}
        Me.GhostButton1.Customization = ""
        Me.GhostButton1.EnableGlass = True
        Me.GhostButton1.Font = New System.Drawing.Font("Verdana", 8.0!)
        Me.GhostButton1.Image = Nothing
        Me.GhostButton1.Location = New System.Drawing.Point(12, 149)
        Me.GhostButton1.Name = "GhostButton1"
        Me.GhostButton1.NoRounding = False
        Me.GhostButton1.Size = New System.Drawing.Size(175, 23)
        Me.GhostButton1.TabIndex = 5
        Me.GhostButton1.Text = "Authenticate"
        Me.GhostButton1.Transparent = False
        '
        'GhostTextBox4
        '
        Me.GhostTextBox4.Customization = "/////wAAAP8AAAD/Wlpa/w=="
        Me.GhostTextBox4.Font = New System.Drawing.Font("Verdana", 8.0!)
        Me.GhostTextBox4.Image = Nothing
        Me.GhostTextBox4.Location = New System.Drawing.Point(12, 119)
        Me.GhostTextBox4.MaxLength = 32767
        Me.GhostTextBox4.Multiline = False
        Me.GhostTextBox4.Name = "GhostTextBox4"
        Me.GhostTextBox4.NoRounding = False
        Me.GhostTextBox4.ReadOnly = False
        Me.GhostTextBox4.Size = New System.Drawing.Size(175, 24)
        Me.GhostTextBox4.TabIndex = 4
        Me.GhostTextBox4.Text = "Email"
        Me.GhostTextBox4.TextAlign = System.Windows.Forms.HorizontalAlignment.Center
        Me.GhostTextBox4.Transparent = False
        Me.GhostTextBox4.UseSystemPasswordChar = False
        '
        'GhostTextBox3
        '
        Me.GhostTextBox3.Customization = "/////wAAAP8AAAD/Wlpa/w=="
        Me.GhostTextBox3.Font = New System.Drawing.Font("Verdana", 8.0!)
        Me.GhostTextBox3.Image = Nothing
        Me.GhostTextBox3.Location = New System.Drawing.Point(12, 89)
        Me.GhostTextBox3.MaxLength = 32767
        Me.GhostTextBox3.Multiline = False
        Me.GhostTextBox3.Name = "GhostTextBox3"
        Me.GhostTextBox3.NoRounding = False
        Me.GhostTextBox3.ReadOnly = False
        Me.GhostTextBox3.Size = New System.Drawing.Size(175, 24)
        Me.GhostTextBox3.TabIndex = 3
        Me.GhostTextBox3.Text = "Password"
        Me.GhostTextBox3.TextAlign = System.Windows.Forms.HorizontalAlignment.Center
        Me.GhostTextBox3.Transparent = False
        Me.GhostTextBox3.UseSystemPasswordChar = True
        '
        'GhostTextBox2
        '
        Me.GhostTextBox2.Customization = "/////wAAAP8AAAD/Wlpa/w=="
        Me.GhostTextBox2.Font = New System.Drawing.Font("Verdana", 8.0!)
        Me.GhostTextBox2.Image = Nothing
        Me.GhostTextBox2.Location = New System.Drawing.Point(12, 59)
        Me.GhostTextBox2.MaxLength = 32767
        Me.GhostTextBox2.Multiline = False
        Me.GhostTextBox2.Name = "GhostTextBox2"
        Me.GhostTextBox2.NoRounding = False
        Me.GhostTextBox2.ReadOnly = False
        Me.GhostTextBox2.Size = New System.Drawing.Size(175, 24)
        Me.GhostTextBox2.TabIndex = 2
        Me.GhostTextBox2.Text = "Password"
        Me.GhostTextBox2.TextAlign = System.Windows.Forms.HorizontalAlignment.Center
        Me.GhostTextBox2.Transparent = False
        Me.GhostTextBox2.UseSystemPasswordChar = True
        '
        'GhostTextBox1
        '
        Me.GhostTextBox1.Customization = "/////wAAAP8AAAD/Wlpa/w=="
        Me.GhostTextBox1.Font = New System.Drawing.Font("Verdana", 8.0!)
        Me.GhostTextBox1.Image = Nothing
        Me.GhostTextBox1.Location = New System.Drawing.Point(12, 29)
        Me.GhostTextBox1.MaxLength = 32767
        Me.GhostTextBox1.Multiline = False
        Me.GhostTextBox1.Name = "GhostTextBox1"
        Me.GhostTextBox1.NoRounding = False
        Me.GhostTextBox1.ReadOnly = False
        Me.GhostTextBox1.Size = New System.Drawing.Size(175, 24)
        Me.GhostTextBox1.TabIndex = 1
        Me.GhostTextBox1.Text = "Username"
        Me.GhostTextBox1.TextAlign = System.Windows.Forms.HorizontalAlignment.Center
        Me.GhostTextBox1.Transparent = False
        Me.GhostTextBox1.UseSystemPasswordChar = False
        '
        'GhostControlBox1
        '
        Me.GhostControlBox1.Anchor = CType((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.GhostControlBox1.Customization = "QEBA/wAAAP9aWlr/"
        Me.GhostControlBox1.Font = New System.Drawing.Font("Verdana", 8.0!)
        Me.GhostControlBox1.Image = Nothing
        Me.GhostControlBox1.Location = New System.Drawing.Point(115, 3)
        Me.GhostControlBox1.Name = "GhostControlBox1"
        Me.GhostControlBox1.NoRounding = False
        Me.GhostControlBox1.Size = New System.Drawing.Size(71, 19)
        Me.GhostControlBox1.TabIndex = 0
        Me.GhostControlBox1.Text = "GhostControlBox1"
        Me.GhostControlBox1.Transparent = False
        '
        'ToolTip1
        '
        Me.ToolTip1.ToolTipIcon = System.Windows.Forms.ToolTipIcon.Info
        Me.ToolTip1.ToolTipTitle = "PokeCalc Help:"
        '
        'frmAuth
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(198, 199)
        Me.Controls.Add(Me.GhostTheme1)
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None
        Me.Icon = CType(resources.GetObject("$this.Icon"), System.Drawing.Icon)
        Me.Name = "frmAuth"
        Me.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.Text = "PokeCalc - Authentication"
        Me.TransparencyKey = System.Drawing.Color.Fuchsia
        Me.GhostTheme1.ResumeLayout(False)
        Me.GhostTheme1.PerformLayout()
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents GhostTheme1 As PokeCalc.GhostTheme
    Friend WithEvents GhostControlBox1 As PokeCalc.GhostControlBox
    Friend WithEvents GhostButton1 As PokeCalc.GhostButton
    Friend WithEvents GhostTextBox4 As PokeCalc.GhostTextBox
    Friend WithEvents GhostTextBox3 As PokeCalc.GhostTextBox
    Friend WithEvents GhostTextBox2 As PokeCalc.GhostTextBox
    Friend WithEvents GhostTextBox1 As PokeCalc.GhostTextBox
    Friend WithEvents Label1 As System.Windows.Forms.Label
    Friend WithEvents ToolTip1 As System.Windows.Forms.ToolTip
End Class
