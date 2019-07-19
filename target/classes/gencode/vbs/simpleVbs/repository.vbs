' 从模型类生成实体bean

Option explicit

'rootPath -- 代码包根目录的存放路径
'respRootPage -- 基础包路径
'tempPath -- 代码生成的临时目录
Dim rootPath,rootPage,respRootPage,tempPath,entityPage
'本实体下的包名
Dim classPage,respPack
'fso -- FileSystemObject
'wshNetwork -- WScript.Network
'entityPath -- 生成实体的路径
'author -- 注释上的作者名字
respPack = "repository"
Dim fso,wshNetwork,entityPath


Set fso = CreateObject("Scripting.FileSystemObject")
''作者名称的获取
Set wshNetwork = CreateObject("WScript.Network")
'author = InputBox("输入名字","注释上的作者名字",wshNetwork.UserName)
If author="" Then
	author = wshNetwork.UserName
End If


'临时目录的生成
tempPath = "_temp"
If fso.FolderExists(tempPath) then
	fso.DeleteFolder (tempPath)
End If
fso.CreateFolder(tempPath)



If init() Then
  '目录的创建
   entityPath = CreateEntityPageFolder(fso, rootPath, classPage)
   CreateSelectEntityToJava()
End If
fso.DeleteFolder (tempPath)

'初始化基本参数
Function init()

   Dim iniArray,signLen
   iniArray = Split(ActiveModel.Comment,vbCr)
   signLen = UBound(iniArray)
   If signLen < 1 Then
		'Output "自定义生成路径请参考以下配置（如果没配置默认生成到该vbs文件相同的文件夹里）:"
		'Output "请在ConceptualDataModel的Comment属性下注明:"
		'Output "源代码根路径"
		'Output "生成的页面路径"
		'Output "基础包名"
		'Output "例如:"
		'Output "D:\projectPath\src\main\java"
		'Output "D:\projectPath\src\main\resources\templates\project"
		'Output "com.xmrbi.project"
		'Output ""
		'Output "<注：第1行为生成的源代码路径，第2行为生成的页面路径，第3行为基础包名>"

		rootPath = defaultRootPath
		Output "源代码根路径:" + rootPath
		CreateRootPahtFolder fso,rootPath
		rootPage = defaultRootPackage+FirstCharToL(ActiveModel.code)+"."+respPack
		respRootPage = defaultRootPackage+FirstCharToL(ActiveModel.code)+"."+respPack
	    Output "repository包路径:" + respRootPage
		classPage =  getClassPage(ActiveDiagram.GetPackage())
		output "classPage:" + classPage
		entityPage = defaultRootPackage+FirstCharToL(ActiveModel.code) +".entity." +  FirstCharToL(ActivePackage.Code)
   Else
	   rootPath = iniArray(0)
	   Output "源代码根路径:" + rootPath
	   CreateRootPahtFolder fso,rootPath
	   rootPage = Mid(iniArray(1),2)
	   respRootPage = Mid(iniArray(2),2)+"."+respPack
	   Output "repository包路径:" + respRootPage
	   classPage =  getClassPage(ActiveDiagram.GetPackage())
	   output "classPage:" + classPage
	   entityPage = Mid(iniArray(2),2) +".entity." +  FirstCharToL(ActivePackage.Code)
   End If
   init = True
End Function


Function getClassPage(pPage)
	Dim currentPage
   getClassPage = ""
   Set currentPage = pPage
  ' Do While currentPage.ClassName <> "Conceptual Data Model"'去掉循环，只取上级的包信息
      If currentPage.code <>"" Then 
	  getClassPage = LCase(currentPage.code) + "." + getClassPage 
      End If
	  Set currentPage = currentPage.GetPackage()
	  
   'Loop
   If getClassPage <> "" Then
	 getClassPage =  respRootPage + "." + Left(getClassPage,Len(getClassPage)-1)
   Else
     getClassPage = respRootPage
   End If
   
End Function 

'BEGIN -------------------目录的创建
'创建根目录
Function CreateRootPahtFolder(vfso, vpath)
	Dim tempArray,temp,rootPath
	tempArray = Split(vpath, "\")

	For Each temp in tempArray
	  If rootPath <> "" Then
		 rootPath = rootPath +"\"
	  End If
      rootPath = rootPath  + temp
	  CreateFolder vfso, rootPath
    Next    
End Function

'创建包根目录
Function CreateEntityPageFolder(vfso, vpath, vpage)
	Dim tempArray,temp
	CreateEntityPageFolder = vpath
	tempArray = Split(vpage, ".")
	
	For Each temp in tempArray
      CreateEntityPageFolder = CreateEntityPageFolder +"\" + temp
	  CreateFolder vfso, CreateEntityPageFolder
	  Output "repository目录：" + CreateEntityPageFolder
    Next
    
End Function

'创建文件夹
Function CreateFolder (vfso, vpath)
   Set CreateFolder = nothing
   If vfso.FolderExists(vpath) then
     Set CreateFolder = vfso.GetFolder(vpath)
'	 output "已存在文件夹: " + vpath
	 Exit Function
   Else
     Set CreateFolder = vfso.CreateFolder(vpath)
	 output "创建文件夹: " + vpath
	 Output ""
   End If 
End Function
'END-------------------目录的创建
Function BackUpFile(pOldFileName)
	If fso.FileExists(pOldFileName) then
	   If MsgBox(pOldFileName + " 文件已经存在,你确定要覆盖吗？",vbYesNo)=vbNo Then
	     BackUpFile = false
	     Exit Function
	   End If
	   Dim oldFile
		Dim backupDir,fileName
		fileName = Mid(pOldFileName,InStrRev(pOldFileName,"\"))
		backupDir = "backup-"+Replace(CStr(Now()),":","-")
		CreateFolder fso, backupDir
		output pOldFileName
		Set oldFile = fso.GetFile(pOldFileName)
		oldFile.Move (backupDir + "\" + fileName)
		BackUpFile = true
    End If
	BackUpFile = true
End Function


'对选择的对象生成java代码
Function CreateSelectEntityToJava()
   dim entity
   For Each entity In ActiveSelection 'ActiveModel.Entities
	If entity.ClassName = "Entity" Then
		EntityToJava entity
    End If
  Next
End Function

'对选择的对象生成java Service代码
Function EntityToJava(pEntity)
   
   Dim entity,tempResp
   Dim respName
   Dim UTF8resp
   
   
   '创建接口文件，创建抽象文件，创建实现文件
   respName = FirstCharToU(pEntity.Code)+"Repository"
   tempResp = tempPath + "\" + respName + ".java.gbk"
   UTF8resp = entityPath + "\" + respName + ".java"

   Dim fileResp
   Set fileResp = fso.OpenTextFile(tempResp, 2, true)

   WriteJavaTop fileResp,pEntity
    
   WritePageInfo fileResp,getClassPage(ActivePackage)
 
   insertRespImportPage fileResp,pEntity

   Dim respStr
   respStr = respStr + AddRespMethod(pEntity)
   fileResp.Write "/**" + vbCrLf
   fileResp.Write " * " +pEntity.Name+ vbCrLf
   fileResp.Write " */" + vbCrLf
   fileResp.Write "@Repository" + vbCrLf
   fileResp.Write "public interface "+FirstCharToU(pEntity.Code)+"Repository extends JpaRepository<"+FirstCharToU(pEntity.Code)+",String>, JpaSpecificationExecutor<"+FirstCharToU(pEntity.Code)+">{" + vbCrLf
   fileResp.Write respStr + vbCrLf
   fileResp.Write "}" + vbCrLf
   
   ConvertToUTF8 tempResp,UTF8resp
End Function


'写文件头注释
Function WriteJavaTop(file,pEntity)
	file.Write "/**" + vbCrLf
 	file.Write "* <p>Description: "+pEntity.name+"管理</p>" + vbCrLf
 	file.Write "* <p>Copyright: Copyright (c) "+ CStr(DatePart("yyyy",Date)) +"</p>" + vbCrLf
 	file.Write "* <p>Company: 厦门路桥信息股份有限公司</p>" + vbCrLf
 	file.Write "* @author :" + author + vbCrLf
 	file.Write "* 创建日期 " + CStr(Date) + " "  + CStr(Time) + vbCrLf
 	file.Write "* @version V1.0" + vbCrLf
 	file.Write "*/" + vbCrLf
End Function



'写文件头注释
Function WritePageInfo(file,pageinfo)
   file.Write  vbCrLf
   file.Write  "package " + pageinfo + ";"+vbCrLf
   file.Write vbCrLf
End Function



'接口类的引入包
Function insertRespImportPage(file,entity)
	file.Write   vbCrLf
	file.Write   "import org.springframework.data.domain.Page;"+vbCrLf
	file.Write   "import org.springframework.data.domain.Pageable;"+vbCrLf
	file.Write   "import org.springframework.data.jpa.repository.Query;"+vbCrLf
	file.Write   "import org.springframework.data.jpa.repository.JpaRepository;"+vbCrLf
	file.Write   "import org.springframework.data.jpa.repository.JpaSpecificationExecutor;"+vbCrLf
	file.Write   "import org.springframework.stereotype.Repository;"+vbCrLf
    file.Write vbCrLf
	file.Write   "import "+entityPage + "."+FirstCharToU(entity.Code)+";"+vbCrLf
    file.Write vbCrLf
End Function


'生成接口方法
Function AddRespMethod(entity)
     Dim attr,countNum
	 countNum = 0
	 AddRespMethod =                      "	/**" + vbCrLf
	 AddRespMethod = AddRespMethod + "	 * 分页查询" + vbCrLf
	 AddRespMethod = AddRespMethod + "	 * @param searchText" + vbCrLf
	 AddRespMethod = AddRespMethod + "	 * @param pageable" + vbCrLf
	 AddRespMethod = AddRespMethod + "	 * @return Page" + vbCrLf
	 AddRespMethod = AddRespMethod + "	 */" + vbCrLf
	 AddRespMethod = AddRespMethod + "	@Query(""select t from "+FirstCharToU(entity.Code)+" t "
	 For Each attr In entity.Attributes
		If Not attr.IsShortcut Then
			If Not attr.PrimaryIdentifier  Then
				If GetType(attr.DataType) = "java.lang.String" Then
					If countNum =0 Then
						AddRespMethod = AddRespMethod + "where t."+attr.Code +" like ?1 "
                    Else
                        AddRespMethod = AddRespMethod + "or t."+attr.Code +" like ?1 "
					End If
					countNum = countNum + 1
				End If
			End If
		End If
     Next
	 AddRespMethod = AddRespMethod + """)" + vbCrLf
	 AddRespMethod = AddRespMethod + "    public Page<"+FirstCharToU(entity.Code)+"> queryPage(String searchText,Pageable pageable);" + vbCrLf
	 AddRespMethod = AddRespMethod + vbCrLf

End Function



'获得类型
Function GetType(vtype)
  Dim regEx, patrn, typeStr,Match, Matches
  patrn = "\D*"
  Set regEx = New RegExp
  regEx.Pattern = patrn
  regEx.IgnoreCase = True
  Set Matches = regEx.Execute(vtype)
  
  For Each Match in Matches   ' 循环遍历Matches集合。
      typeStr = Match.Value
  Next
  
	Select Case typeStr
           Case "I"   GetType = "java.lang.Integer"
           Case "LI"  GetType = "java.lang.Long"
           Case "SI"  GetType = "java.lang.Short"

		   Case "BT"  GetType = "java.lang.Byte"

		   Case "N"  GetType = "java.lang.Double"
		   Case "DC"  GetType = "java.math.BigDecimal"

		   Case "F"   GetType = "java.lang.Float"
		   Case "SF"  GetType = "java.lang.Float"
		   Case "LF"  GetType = "java.lang.Float"


		   Case "MN"  GetType = "java.math.BigDecimal"
		   Case "NO"  GetType = "java.lang.Long"

		   Case "BL"  GetType = "java.lang.Boolean"

		   Case "A"   GetType = "java.lang.String"
		   Case "VA"  GetType = "java.lang.String"
		   Case "LA"  GetType = "java.lang.String"
		   Case "LVA" GetType = "java.lang.String"
		   Case "TXT" GetType = "java.lang.String"

		   Case "MBT"  GetType = "byte[]"
		   Case "VMBT" GetType = "byte[]"

		   Case "D"   GetType = "java.util.Date"
		   Case "T"   GetType = "java.util.Date"
		   Case "DT"  GetType = "java.util.Date"
		   Case "TS"  GetType = "java.util.Calendar"

		   Case "BIN"  GetType = "byte[]"
		   Case "LBIN" GetType = "byte[]"

		   Case "BMP"  GetType = "java.sql.Blob"
		   Case "PIC"  GetType = "java.sql.Blob"
		   Case "OLE"  GetType = "java.sql.Clob"

           Case Else GetType = vtype
    End Select
End Function

'首字母变大写
Function FirstCharToU(vstr)
	Dim vLeftChar,vBackStr
	vLeftChar = Left(vstr, 1)
	vLeftChar = UCase(vLeftChar)
	vBackStr = Mid(vstr,2)
    FirstCharToU = vLeftChar + vBackStr
End Function


'首字母变小写
Function FirstCharToL(vstr)
	Dim vLeftChar,vBackStr
	vLeftChar = Left(vstr, 1)
	vLeftChar = LCase(vLeftChar)
	vBackStr = Mid(vstr,2)
    FirstCharToL = vLeftChar + vBackStr
End Function