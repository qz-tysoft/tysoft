' 从模型类生成实体bean

Option explicit
Do 
Dim fso,rootPath,webPage,tempPath,batFilePath,defaultDbRootPath
defaultDbRootPath = "..\db"
 
Set fso = CreateObject("Scripting.FileSystemObject")  

If init() Then
   Output "创建utf8转化命令的临时文件..."
   createBat()
   Output "将文件转换为无Bom的UTF8格式... ..."
   executeBat()
   Output "转换完成。"
   Output "代码生成成功！"
   delBat()
End If

Exit Do  
Loop 

'初始化基本参数
Function init()
   Dim iniArray,signLen
   iniArray = Split(ActiveModel.Comment,vbCr)
   signLen = UBound(iniArray)
   If signLen < 1 Then
		rootPath = defaultRootPath
		Output "源代码根路径:" + rootPath
		webPage = defaultWebPage
		output "网页路径= " + webPage		
   Else
		rootPath = iniArray(0)
		Output "源代码根路径=" + rootPath
		webPage = Mid(iniArray(1),2)
		output "网页路径= " + webPage
   End If
   init = True
End Function

'创建bat文件
Function createBat()
   Dim file
   batFilePath = rootPath + "\Utf8ToNoBom.bat"
   Set file = fso.OpenTextFile(batFilePath, 2, true)
   file.Write  "@echo off"+vbCrLf
   file.Write  "powershell ^"+vbCrLf
   file.Write  "    Get-ChildItem "+rootPath+"\ -recurse *.java^|%%{^"+vbCrLf
   file.Write  "        $txt = [IO.File]::ReadAllText($_.FullName, [Text.Encoding]::UTF8);^"+vbCrLf
   file.Write  "        $Utf8NoBomEncoding = New-Object System.Text.UTF8Encoding $False;^"+vbCrLf
   file.Write  "        [System.IO.File]::WriteAllText($_.FullName, $txt, $Utf8NoBomEncoding);^"+vbCrLf
   file.Write  "    }"+vbCrLf
   'file.Write  "pause"+vbCrLf

   file.Write  "@echo off"+vbCrLf
   file.Write  "powershell ^"+vbCrLf
   file.Write  "    Get-ChildItem "+webPage+"\ -recurse *.html^|%%{^"+vbCrLf
   file.Write  "        $txt = [IO.File]::ReadAllText($_.FullName, [Text.Encoding]::UTF8);^"+vbCrLf
   file.Write  "        $Utf8NoBomEncoding = New-Object System.Text.UTF8Encoding $False;^"+vbCrLf
   file.Write  "        [System.IO.File]::WriteAllText($_.FullName, $txt, $Utf8NoBomEncoding);^"+vbCrLf
   file.Write  "    }"+vbCrLf

   file.Write  "@echo off"+vbCrLf
   file.Write  "powershell ^"+vbCrLf
   file.Write  "    Get-ChildItem "+defaultDbRootPath+"\ -recurse *.sql^|%%{^"+vbCrLf
   file.Write  "        $txt = [IO.File]::ReadAllText($_.FullName, [Text.Encoding]::UTF8);^"+vbCrLf
   file.Write  "        $Utf8NoBomEncoding = New-Object System.Text.UTF8Encoding $False;^"+vbCrLf
   file.Write  "        [System.IO.File]::WriteAllText($_.FullName, $txt, $Utf8NoBomEncoding);^"+vbCrLf
   file.Write  "    }"+vbCrLf
End Function

'执行bat文件
Function executeBat()
Dim wshshell
set wshshell=CreateObject("wscript.shell")
wshshell.run batFilePath,0,true
End Function

'删除bat文件
Function delBat()
    ''WScript.Sleep 2000 '这种方式的延时在这里不可用
	'Dim ws  '可以用这种方式延时
	'set ws=CreateObject("wscript.shell")
    'ws.run "cmd.exe /c choice /t 5 /d y /n >nul" ,0,true
    'MsgBox "代码生成成功"
fso.deleteFile  batFilePath
End Function
'-----------------------------------------------------------------------------------------
