' 从模型类生成实体bean 

Option explicit

'rootPath -- 代码包根目录的存放路径
'rootPage -- 基础包路径
'tempPath -- 代码生成的临时目录
Dim rootPath,rootPage,tempPath,defaultDbRootPath
defaultDbRootPath = "..\db"
'本实体下的包名
Dim classPage
'fso -- FileSystemObject
'wshNetwork -- WScript.Network
'entityPath -- 生成实体的路径
'author -- 注释上的作者名字
Dim fso,wshNetwork,entityPath
Dim table_top,ref_table_top
table_top = ActivePackage.Comment + "_"
ref_table_top = table_top
Set fso = CreateObject("Scripting.FileSystemObject")
'作者名称的获取
Set wshNetwork = CreateObject("WScript.Network")
'author = InputBox("输入名字","注释上的作者名字",wshNetwork.UserName)
If author="" Then
	author = wshNetwork.UserName
End If


'临时目录的生成
'Dim nowDate
'nowDate = Date
'tempPath = CStr(nowDate) + "-temp"
tempPath = "_temp"
If fso.FolderExists(tempPath) then
	fso.DeleteFolder (tempPath)
End If
fso.CreateFolder(tempPath)

If init() Then
  '目录的创建
  'entityPath = CreateEntityPageFolder(fso, rootPath, classPage)
   entityPath = defaultRootPath
   Output "创建Mysql建表语句文件..."
   CreateSelectEntityToSql()
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
		'Output "源代码根路径:" + rootPath
		CreateRootPahtFolder fso,rootPath
		CreateRootPahtFolder fso,defaultDbRootPath
		rootPage = defaultRootPackage+FirstCharToL(ActiveModel.code)+".entity"
		'Output "实体包路径:" + rootPage
		classPage =  getClassPage(ActiveDiagram.GetPackage())
		'output "classPage:" + classPage
   Else
		rootPath = iniArray(0)
		'Output "源代码根路径:" + rootPath
		CreateRootPahtFolder fso,rootPath
		rootPage = Mid(iniArray(2),2)+".entity"
		'Output "实体包路径:" + rootPage
		classPage =  getClassPage(ActiveDiagram.GetPackage())
		'output "classPage:" + classPage

   End If
   init = True
End Function


Function getClassPage(pPage)
	Dim currentPage
   getClassPage = ""
   Set currentPage = pPage
  ' Do While currentPage.ClassName <> "Conceptual Data Model" '去掉循环，只取上级的包信息
  	  getClassPage = LCase(currentPage.code) + "." + getClassPage 
      
  	  Set currentPage = currentPage.GetPackage()
  ' Loop
   If getClassPage <> "" Then
	 getClassPage =  rootPage + "." + Left(getClassPage,Len(getClassPage)-1)
   Else
     getClassPage = rootPage
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
	  Output "实体目录：" + CreateEntityPageFolder
    Next
    
End Function

'创建文件夹
Function CreateFolder (vfso, vpath)
   Set CreateFolder = nothing
   If vfso.FolderExists(vpath) then
     Set CreateFolder = vfso.GetFolder(vpath)
	 'output "已存在文件夹: " + vpath
	 Exit Function
   Else 
     output "创建文件夹: " + vpath
     Set CreateFolder = vfso.CreateFolder(vpath)
	 'Output "创建文件夹成功"
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
		backupDir = "backup\"+Replace(CStr(Now()),":","-")
		CreateFolder fso, backupDir
		output pOldFileName
		Set oldFile = fso.GetFile(pOldFileName)
		oldFile.Move (backupDir + "\" + fileName)
		BackUpFile = true
    End If
	BackUpFile = true
End Function



'对选择的对象生成java代码
Function CreateSelectEntityToSql()
   dim entity
   For Each entity In ActiveSelection 'ActiveModel.Entities
	If entity.ClassName = "Entity" Then
		EntityToSql entity
    End If
  Next
End Function

'生成实体类SQL代码
Function EntityToSql(pEntity)
   Dim filepath,filepathUTF8
   filepath = tempPath + "\mysql-" + table_top + GetColumnStr(FirstCharToL(pEntity.Code)) + ".sql.gbk"
   filepathUTF8 = defaultDbRootPath + "\mysql-" + table_top + GetColumnStr(FirstCharToL(pEntity.Code)) + ".sql"

   Dim file
   Set file = fso.OpenTextFile(filepath, 2, true)

   'priAttr 主键
   'baseAttStr 基本属性
   'rsAttStr关联属性
   Dim priAttr,rsCol
   Dim baseAttStr
   Dim rsAttStr
   Dim parentEntity
   Dim entityAnnotateStr
   
   '写入代码信息
   'file.Write  "-- 请另存为UTF-8编码格式后再用mysql工具导入运行该文件（建议用EditPlus打开另存为UTF-8编码）"  + vbCrLf
   file.Write  "-- Date:" + CStr(Date) + " "  + CStr(Time) + vbCrLf
   file.Write  "-- author:" + author + vbCrLf + vbCrLf
   file.Write  "SET FOREIGN_KEY_CHECKS=0; " +vbCrLf
   file.Write vbCrLf
   
   getBaseStr pEntity,priAttr,baseAttStr 
   getRelationshipStr pEntity,rsCol,rsAttStr
  
   file.Write "-- DROP TABLE IF EXISTS `"+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+"`;" + vbCrLf
   file.Write "CREATE TABLE `"+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+"` ("+vbCrLf

   '写属性
   file.Write  baseAttStr
   file.Write  rsAttStr

   file.Write priAttr
   file.Write ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='"+pEntity.Name+"';"
   'UTF-8转换
   ConvertToUTF8 filepath,filepathUTF8
  ' WriteToFile filepathUTF8, ReadFile(filepath, "utf-8"), "utf-8"
End Function

'生成实体的属性
Function getBaseStr(pEntity,priAttr,baseAttStr)
	Dim attr,iden
    baseAttStr = ""
	For Each attr In pEntity.Attributes
		If Not attr.IsShortcut Then 
			If attr.PrimaryIdentifier Then
				priAttr = "  PRIMARY KEY (`" + FirstCharToL(attr.Code) + "`)" + vbCrLf 
				
			End If
			baseAttStr = baseAttStr + AddBaseAttrib(attr)
		End If	 
	Next  
End Function


'生成属性方法
Function AddBaseAttrib(attr) 
    Dim isnull
	isnull = " DEFAULT NULL "
	If attr.Mandatory Then
		isnull = " NOT NULL "
	End If
	'Output attr.name + ":"+attr.DataType +"--"+ cstr(attr.length) +"--"+ cstr(attr.Precision)
	AddBaseAttrib = AddBaseAttrib + "  `"+GetColumnStr(FirstCharToL(attr.Code))+"` " + GetType(attr.DataType,attr.Length,attr.Precision) + isnull + " COMMENT '" + attr.Name + attr.Comment+ "'," + vbCrLf 
End Function


Function getRelationshipStr(pEntity,rsCol,rsAttStr)
	Dim rs,doneRsCodes
	doneRsCodes = ";"
    For Each rs In pEntity.relationships
		If InStr(doneRsCodes,";"+rs.code+";")=0 Then 
			Dim rsType,otherEntity,roleName,otheRroleName,toCardinality
			'rs.RelationshipType
			'(0) One-to-one <1..1> 
			'(1) One-to-many <1..n> 
			'(2) Many-to-one
			'(3) Many-to-many
			rsType = rs.RelationshipType
			If pEntity = rs.Entity1 Or (rs.Entity1.IsShortcut() And pEntity.code = rs.Entity1.code) Then
				Set otherEntity = rs.Entity2
				If rs.Entity2.IsShortcut() Then
					Set otherEntity = rs.Entity2.TargetObject
				End If 
				roleName = rs.Entity1ToEntity2Role
				otheRroleName = rs.Entity2ToEntity1Role
				toCardinality = rs.Entity1ToEntity2RoleCardinality	
			End If
			If pEntity = rs.Entity2 Or (rs.Entity2.IsShortcut() And pEntity.code = rs.Entity2.code) Then
				 Set otherEntity = rs.Entity1
				 If rs.Entity1.IsShortcut() Then
					Set otherEntity = rs.Entity1.TargetObject
				 End If 
				 roleName = rs.Entity2ToEntity1Role
				 otheRroleName = rs.Entity1ToEntity2Role
				 toCardinality = rs.Entity2ToEntity1RoleCardinality		
			End If
			
			'多对多的情况下
			If rsType = 3 Then
				If Trim(roleName)<>"" Then 
					'生成多对多的属性和方法
					getManyToMany rs,roleName,pEntity,otherEntity
				End If 
			'非多对多
			Else 
				Dim objectNum,otherObjectNum
				objectNum = GetVaule(toCardinality,",",1)
				otherObjectNum = GetVaule(toCardinality,",",0)
				
				'配置的是一对多的对应关系 roleName为空不生成
				If objectNum = "n" And roleName <> "" Then
					'生成一对多的属性和方法
					getOneToMany rs,roleName,otherEntity,rsCol,rsAttStr
				End If 
				'配置的是多对一的对应关系
				If objectNum = "1"   Then
					'生成多对一的属性和方法
					getManyToOne rs,roleName,otherEntity,rsCol,rsAttStr 
				End If
			End If
			
			If Trim(roleName)<> ""  And pEntity.GetPackage() <> otherEntity.GetPackage() Then 
					Dim otherImport
					otherImport = getClassPage(otherEntity.GetPackage()) + "." + FirstCharToU(otherEntity.Code)
			End If
			doneRsCodes = doneRsCodes + rs.code+";"
		End If 
	Next
End Function

'多对多关系
Function getManyToMany(rs,roleName,pEntity,otherEntity)
   Dim filepath,filepathUTF8,rsAttStr,attr
   filepath = tempPath + "\mysql-" + rs.code + ".sql.gbk"
   filepathUTF8 = defaultDbRootPath + "\mysql-" + rs.code + ".sql"

   Dim file
   Set file = fso.OpenTextFile(filepath, 2, true)
   
   '写入代码信息
   file.Write  "-- 请另存为UTF-8编码格式后再用mysql工具导入运行该文件（建议用EditPlus打开另存为UTF-8编码）"  + vbCrLf
   file.Write  "-- Date:" + CStr(Date) + " "  + CStr(Time) + vbCrLf
   file.Write  "-- author:" + author + vbCrLf + vbCrLf
   file.Write  "SET FOREIGN_KEY_CHECKS=0; " +vbCrLf
   file.Write vbCrLf
  
   file.Write "DROP TABLE IF EXISTS "+rs.code+";" + vbCrLf+ vbCrLf
   file.Write "CREATE TABLE "+rs.code+" ("+vbCrLf

   '写属性
   For Each attr In pEntity.Attributes
		If Not attr.IsShortcut Then 
			If attr.PrimaryIdentifier Then
				rsAttStr = rsAttStr + "  `"+ GetColumnStr(FirstCharToL(pEntity.Code)) +"_id` "+ GetType(attr.DataType,attr.Length,attr.Precision) +" NOT NULL COMMENT '"+pEntity.Name+"',"+ vbCrLf
				rsAttStr = rsAttStr + "  KEY `idx_"+GetColumnStr(FirstCharToL(pEntity.Code))+"_"+ rs.code +"` (`"+ GetColumnStr(FirstCharToL(pEntity.Code))+"_id`) USING BTREE,"+ vbCrLf
				rsAttStr = rsAttStr + "  CONSTRAINT `fk_"+ GetColumnStr(FirstCharToL(pEntity.Code))+"_"+ rs.code +"` FOREIGN KEY (`"+ GetColumnStr(FirstCharToL(pEntity.Code)) +"_id`) REFERENCES `"+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+"` (`"+attr.Code+"`),"+ vbCrLf
			End If
		End If	 
	Next

   For Each attr In otherEntity.Attributes
   ref_table_top = otherEntity.GetPackage().Comment + "_"
		If Not attr.IsShortcut Then 
			If attr.PrimaryIdentifier Then
				rsAttStr = rsAttStr + "  `"+ GetColumnStr(FirstCharToL(otherEntity.Code)) +"_id` "+ GetType(attr.DataType,attr.Length,attr.Precision) +" NOT NULL COMMENT '"+otherEntity.Name+"',"+ vbCrLf
				rsAttStr = rsAttStr + "  KEY `idx_"+GetColumnStr(FirstCharToL(otherEntity.Code))+"_"+ rs.code +"` (`"+ GetColumnStr(FirstCharToL(otherEntity.Code)) +"_id`) USING BTREE,"+ vbCrLf
				rsAttStr = rsAttStr + "  CONSTRAINT `fk_"+ GetColumnStr(FirstCharToL(otherEntity.Code))+"_"+ rs.code +"` FOREIGN KEY (`"+ GetColumnStr(FirstCharToL(otherEntity.Code)) +"_id`) REFERENCES `"+ref_table_top + GetColumnStr(FirstCharToL(otherEntity.Code))+"` (`"+attr.Code+"`),"+ vbCrLf
			End If
		End If	 
	Next
	rsAttStr = rsAttStr + "  PRIMARY KEY (`"+ GetColumnStr(FirstCharToL(pEntity.Code)) +"_id`,`"+ GetColumnStr(FirstCharToL(otherEntity.Code)) +"_id`)"
   file.Write rsAttStr+vbCrLf
   file.Write ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='"+rs.Comment+"';"
   'UTF-8转换
   ConvertToUTF8 filepath,filepathUTF8
End Function 

'一对多关系
Function getOneToMany(rs,roleName,otherEntity,rsCol,rsAttStr)
   Dim attr
   ref_table_top = otherEntity.GetPackage().Comment + "_"
   For Each attr In otherEntity.Attributes
		If Not attr.IsShortcut Then 
			If attr.PrimaryIdentifier Then
				rsAttStr = rsAttStr + "  `"+ GetColumnStr(rs.name) +"` "+ GetType(attr.DataType,attr.Length,attr.Precision) +" DEFAULT NULL COMMENT '"+otherEntity.Name+"',"+ vbCrLf
				rsAttStr = rsAttStr + "  KEY `"+ GetColumnStr(rs.code) +"` (`"+ GetColumnStr(rs.name) +"`) USING BTREE,"+ vbCrLf
				rsAttStr = rsAttStr + "  CONSTRAINT `fk_"+ GetColumnStr(rs.code) +"` FOREIGN KEY (`"+ GetColumnStr(rs.name) +"`) REFERENCES `"+ref_table_top + GetColumnStr(FirstCharToL(otherEntity.Code))+"` (`"+attr.Code+"`),"+ vbCrLf
			End If
		End If	 
	Next 
End Function 

'多对一关系
Function getManyToOne(rs,roleName,otherEntity,rsCol,rsAttStr)
   Dim attr
'  If otherEntity.IsShortcut() = true Then
	ref_table_top = otherEntity.GetPackage().Comment + "_"
'   End If
   For Each attr In otherEntity.Attributes
		If Not attr.IsShortcut Then 
			If attr.PrimaryIdentifier Then
				rsAttStr = rsAttStr + "  `"+ GetColumnStr(rs.name) +"` "+ GetType(attr.DataType,attr.Length,attr.Precision) +" DEFAULT NULL COMMENT '"+otherEntity.Name+"',"+ vbCrLf
				rsAttStr = rsAttStr + "  KEY `"+ GetColumnStr(rs.code) +"` (`"+ GetColumnStr(rs.name) +"`) USING BTREE,"+ vbCrLf
				rsAttStr = rsAttStr + "  CONSTRAINT `fk_"+ GetColumnStr(rs.code) +"` FOREIGN KEY (`"+ GetColumnStr(rs.name) +"`) REFERENCES `"+ref_table_top + GetColumnStr(FirstCharToL(otherEntity.Code))+"` (`"+attr.Code+"`),"+ vbCrLf
			End If
		End If	 
	Next 
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


'获得类型
Function GetType(vtype,vLength,vPrecision)

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
           Case "I"   GetType = "INT(20)"
           Case "LI"  GetType = "INT(20)"
           Case "SI"  GetType = "INT(20)"

		   Case "BT"  GetType = "VARCHAR("+vLength+")"
           
		   Case "N"  
			If vPrecision = 0 Then
				GetType = "BIGINT(20)"
			Else 
				GetType = "DOUBLE("+cstr(vLength)+","+vPrecision+")"
			End If 
		   Case "DC"  GetType = "DECIMAL("+cstr(vLength)+","+cstr(vPrecision)+")"

		   Case "F"   GetType = "FLOAT("+cstr(vLength)+","+cstr(vPrecision)+")"
		   Case "SF"  GetType = "FLOAT("+cstr(vLength)+","+cstr(vPrecision)+")"
		   Case "LF"  GetType = "FLOAT("+cstr(vLength)+","+cstr(vPrecision)+")"

		   Case "MN"  GetType = "DECIMAL("+cstr(vLength)+","+cstr(vPrecision)+")"
		   Case "NO"  GetType = "BIGINT(20)"

		   Case "BL"  GetType = "BOOLEAN"

		   Case "A"   GetType = "VARCHAR("+cstr(vLength)+")"
		   Case "VA"  GetType = "VARCHAR("+cstr(vLength)+")"
		   Case "LA"  GetType = "VARCHAR("+cstr(vLength)+")"
		   Case "LVA" GetType = "VARCHAR("+cstr(vLength)+")"
		   Case "TXT" GetType = "VARCHAR("+cstr(vLength)+")"

		   Case "MBT"  GetType = "BINARY"
		   Case "VMBT" GetType = "BINARY"

		   Case "D"   GetType = "DATE"
		   Case "T"   GetType = "TIME"
		   Case "DT"  GetType = "DATETIME"
		   Case "TS"  GetType = "TIMESTAMP"

		   Case "BIN"  GetType = "BINARY"
		   Case "LBIN" GetType = "BINARY"

		   Case "BMP"  GetType = "BLOB"
		   Case "PIC"  GetType = "BLOB"
		   Case "OLE"  GetType = "CLOB"

           Case Else GetType = vtype
    End Select
End Function


'取得分割符分解的数组的第N个值
Function GetVaule(vstr,vsign,vnum)
	Dim tempArray,length
    tempArray = Split(vstr, vsign)
	length = UBound(tempArray)
	if vnum > length Then 
	 GetVaule = null
	Else
	 GetVaule = tempArray(vnum)
	End If
End Function



'获取驼峰命名
Function GetColumnStr(vstr)
	Dim vLeftChar
	Dim strLen
	Dim k
	Dim newStr
	Dim bigLen
	strLen = Len(vstr)
	newStr = Left(vstr,1)
	bigLen = 0
	If newStr >=  "A"  and newStr <=  "Z"  Then
		bigLen = 1
	End If
	For k = 2 To strLen
       vLeftChar = Mid(vstr, k,1)
	   If vLeftChar >=  "A"  and vLeftChar <=  "Z"  Then
			bigLen = bigLen + 1
			If  Mid(vstr, k-1,1)<>"_"   Then
				newStr = newStr + "_" 
				
			End If
	   End If
	   newStr = newStr + LCase(vLeftChar)
    Next

	If bigLen = strLen Then
		GetColumnStr = vstr
	Else
		GetColumnStr = newStr
	End If
	
End Function

'-----------------------------------------------------------------------------------------
Function toUTF8(szInput)
    Dim wch, uch, szRet
    Dim x
    Dim nAsc, nAsc2, nAsc3
    '如果输入参数为空，则退出函数
    If szInput = "" Then
        toUTF8 = szInput
        Exit Function
    End If
    '开始转换
     For x = 1 To Len(szInput)
        '利用mid函数分拆GB编码文字
        wch = Mid(szInput, x, 1)
        '利用ascW函数返回每一个GB编码文字的Unicode字符代码
        '注：asc函数返回的是ANSI 字符代码，注意区别
        nAsc = AscW(wch)
        If nAsc < 0 Then nAsc = nAsc + 65536
    
        If (nAsc And &HFF80) = 0 Then
            szRet = szRet & wch
        Else
            If (nAsc And &HF000) = 0 Then
                uch = "%" & Hex(((nAsc / 2 ^ 6)) Or &HC0) & Hex(nAsc And &H3F Or &H80)
                szRet = szRet & uch
            Else
               'GB编码文字的Unicode字符代码在0800 - FFFF之间采用三字节模版
                uch = "%" & Hex((nAsc / 2 ^ 12) Or &HE0) & "%" & _
                            Hex((nAsc / 2 ^ 6) And &H3F Or &H80) & "%" & _
                            Hex(nAsc And &H3F Or &H80)
                szRet = szRet & uch
            End If
        End If
    Next
        
    toUTF8 = szRet
End Function

Dim SrcCode,DestCode,stm
SrcCode="gb2312"
DestCode="utf-8"
'-------------------------------------------------
'函数名称:ConvertFile
'作用:将一个文件进行编码转换
'-------------------------------------------------
Function ConvertFile(FileUrl)
    Call WriteToFile(FileUrl, ReadFile(FileUrl, SrcCode), DestCode)
End Function
'-------------------------------------------------
'函数名称:ReadFile
'作用:利用AdoDb.Stream对象来读取各种格式的文本文件
'-------------------------------------------------
Function ReadFile(FileUrl, CharSet)
    Dim Str
    Set stm = CreateObject("Adodb.Stream")
    stm.Type = 2
    stm.mode = 3
    stm.charset = CharSet
    stm.Open
    stm.loadfromfile FileUrl
    Str = stm.readtext
    stm.Close
    Set stm = Nothing
    ReadFile = Str
End Function
'-------------------------------------------------
'函数名称:WriteToFile
'作用:利用AdoDb.Stream对象来写入各种格式的文本文件
'-------------------------------------------------
Function WriteToFile (FileUrl, Str, CharSet)
    Set stm = CreateObject("Adodb.Stream")
	stm.Position = 0
    stm.Type = 2
    stm.mode = 3
    stm.charset = CharSet
    stm.Open
    stm.WriteText Str
    stm.SaveToFile FileUrl, 2
    stm.flush
    stm.Close
    Set stm = Nothing
End Function