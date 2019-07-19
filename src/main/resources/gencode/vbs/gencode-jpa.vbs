Sub Include(sInstFile) 
Dim oFSO, f, s 
Set oFSO = CreateObject("Scripting.FileSystemObject") 
Set f = oFSO.OpenTextFile(sInstFile) 
s = f.ReadAll 
f.Close 
ExecuteGlobal s 
End Sub

'defaultRootPath --源代码默认根路径
'defaultWebPage --页面默认路径
'defaultRootPackage --默认包前缀
'author --作者
Dim defaultRootPath,defaultWebPage,defaultRootPackage,author
defaultRootPath = "..\code\src\main\java"
defaultWebPage = "..\code\src\main\resources\templates"
defaultRootPackage = "com.xmrbi."

'作者名称的获取
Set wNetwork = CreateObject("WScript.Network")
author = InputBox("输入名字","注释上的作者名字",wNetwork.UserName)

'执行各个文件脚本
Include "simpleVbs/entity.vbs"
Include "simpleVbs/repository.vbs"
Include "simpleVbs/db-mysql.vbs"
'Include "simpleVbs/db-oracle.vbs"
Include "simpleVbs/service-jpa-criteria.vbs"
Include "simpleVbs/controller_page-jpa.vbs"
Include "simpleVbs/Utf8ToNoBom.vbs"
MsgBox "代码生成成功"