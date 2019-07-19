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
'author -- 注释上的作者名字
Dim fso,wshNetwork,entityPath
Dim table_top
table_top = ActivePackage.Comment + "_"
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
   entityPath = defaultRootPath
   Output "创建Oracle建表语句文件..."
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
		output "classPage:" + classPage
   Else
		rootPath = iniArray(0)
		'Output "源代码根路径:" + rootPath
		CreateRootPahtFolder fso,rootPath
		rootPage = Mid(iniArray(2),2)+".entity"
		'Output "实体包路径:" + rootPage
		classPage =  getClassPage(ActiveDiagram.GetPackage())
		output "classPage:" + classPage
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
   filepath = tempPath + "\oracle-" + table_top + GetColumnStr(FirstCharToL(pEntity.Code)) + ".sql.gbk"
   filepathUTF8 = defaultDbRootPath + "\oracle-" + table_top + GetColumnStr(FirstCharToL(pEntity.Code)) + ".sql"

   Dim file
   Set file = fso.OpenTextFile(filepath, 2, true)

   'priAttr 主键
   'baseAttStr 基本属性
   'rsAttStr关联属性
   Dim priAttr
   Dim baseAttStr,commentStr
   Dim rsAttStr,rsCol
   Dim parentEntity
   Dim entityAnnotateStr
   
   '' On   Error   Resume   Next
   '写入代码信息
   'file.Write  "-- 请另存为UTF-8编码格式后再用oracle工具导入运行该文件（建议用EditPlus打开另存为UTF-8编码）"  + vbCrLf
   file.Write  "-- Date:" + CStr(Date) + " "  + CStr(Time) + vbCrLf
   file.Write  "-- author:" + author + vbCrLf + vbCrLf

   getBaseStr pEntity,priAttr,baseAttStr,commentStr 
   getRelationshipStr pEntity,rsAttStr,commentStr
  
   file.Write "--drop table "+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+" cascade constraints;" + vbCrLf
   file.Write "create table "+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+" ("+vbCrLf

   '写属性
   file.Write  baseAttStr
   file.Write  rsAttStr

   file.Write vbCrLf+") ;"+vbCrLf

   '写备注
   file.Write commentStr
   file.Write priAttr

   'UTF-8转换
   ConvertToUTF8 filepath,filepathUTF8
End Function

'生成实体的属性
Function getBaseStr(pEntity,priAttr,baseAttStr,commentStr)
	Dim attr,iden
    baseAttStr = ""
	commentStr = commentStr + "comment on table "+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+"  is '"+ pEntity.Name + "';" + vbCrLf 
	For Each attr In pEntity.Attributes
		If Not attr.IsShortcut Then 
			If attr.PrimaryIdentifier Then
				priAttr = "alter table "+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+ "  add constraint pk_"+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+" primary key (" + FirstCharToL(attr.Code) + ");" + vbCrLf 
			End If
			If baseAttStr <> "" Then
			baseAttStr = baseAttStr+","+ vbCrLf 
			End If 
			baseAttStr = baseAttStr + AddBaseAttrib(attr)
			commentStr = commentStr + "comment on column "+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+"."+GetColumnStr(FirstCharToL(attr.Code))+ " is '"+ attr.Name + attr.Comment+ "';" + vbCrLf 
		End If	 
	Next  
End Function


'生成属性方法
Function AddBaseAttrib(attr) 
    Dim isnull
	isnull = ""
	If attr.Mandatory Then
		isnull = " not null"
	End If
	'Output attr.name + ":"+attr.DataType +"--"+ cstr(attr.length) +"--"+ cstr(attr.Precision)
	AddBaseAttrib = AddBaseAttrib + "  "+GetColumnStr(FirstCharToL(attr.Code))+" " + GetType(attr.DataType,attr.Length,attr.Precision) + isnull 
End Function


Function getRelationshipStr(pEntity,rsAttStr,commentStr)
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
					getOneToMany rs,roleName,pEntity,otherEntity,rsAttStr,commentStr 
				End If 
				'配置的是多对一的对应关系
				If objectNum = "1"   Then
					'生成多对一的属性和方法
					getManyToOne rs,roleName,pEntity,otherEntity,rsAttStr,commentStr 
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
   Dim filepath,filepathUTF8,rsAttStr,attr,comStr
   filepath = tempPath + "\oracle-" + rs.code + ".sql.gbk"
   filepathUTF8 = defaultDbRootPath + "\oracle-" + rs.code + ".sql"

   Dim file
   Set file = fso.OpenTextFile(filepath, 2, true)
   
   On   Error   Resume   Next
   '写入代码信息
   'file.Write  "-- 请另存为UTF-8编码格式后再用oracle工具导入运行该文件（建议用EditPlus打开另存为UTF-8编码）"  + vbCrLf
   file.Write  "-- Date:" + CStr(Date) + " "  + CStr(Time) + vbCrLf
   file.Write  "-- author:" + author + vbCrLf + vbCrLf
  
   file.Write "--drop table "+rs.code+" cascade constraints;" + vbCrLf+ vbCrLf
   file.Write "create table "+rs.code+" ("+vbCrLf

   '写属性
   For Each attr In pEntity.Attributes
		If Not attr.IsShortcut Then 
			If attr.PrimaryIdentifier Then
				rsAttStr = rsAttStr + "  "+ GetColumnStr(FirstCharToL(pEntity.Code)) +"_id "+ GetType(attr.DataType,attr.Length,attr.Precision) +" not null,"+ vbCrLf
				comStr = comStr + "comment on column "+rs.code+"."+ GetColumnStr(FirstCharToL(pEntity.Code)) +"_id is '"+pEntity.Name+"';"+ vbCrLf
				comStr = comStr + "create index "+ GetColumnStr(FirstCharToL(pEntity.Code))+"_"+ rs.code +"_fk on "+rs.code+" ("+ GetColumnStr(FirstCharToL(pEntity.Code)) +"_id);"+ vbCrLf
				comStr = comStr + "alter table "+rs.code+" add constraint fk_"+ GetColumnStr(FirstCharToL(pEntity.Code))+"_"+ rs.code +" foreign key ("+ GetColumnStr(FirstCharToL(pEntity.Code))+"_id) references "+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+" ("+attr.Code+");"+ vbCrLf
			End If
		End If	 
	Next
   For Each attr In otherEntity.Attributes
		If Not attr.IsShortcut Then 
			If attr.PrimaryIdentifier Then
				rsAttStr = rsAttStr + "  "+ GetColumnStr(FirstCharToL(otherEntity.Code)) +"_id "+ GetType(attr.DataType,attr.Length,attr.Precision) +" not null"+ vbCrLf
				comStr = comStr + "comment on column "+rs.code+"."+ GetColumnStr(FirstCharToL(otherEntity.Code)) +"_id is '"+otherEntity.Name+"';"+ vbCrLf
				comStr = comStr + "create index "+ GetColumnStr(FirstCharToL(otherEntity.Code))+"_"+ rs.code +"_fk on "+rs.code+" ("+ GetColumnStr(FirstCharToL(otherEntity.Code)) +"_id);"+ vbCrLf
				comStr = comStr + "alter table "+rs.code+" add constraint fk_"+ GetColumnStr(FirstCharToL(otherEntity.Code))+"_"+ rs.code +" foreign key ("+ GetColumnStr(FirstCharToL(otherEntity.Code))+"_id) references "+table_top + GetColumnStr(FirstCharToL(otherEntity.Code))+" ("+attr.Code+");"+ vbCrLf
			End If
		End If	 
	Next

   file.Write rsAttStr
   file.Write ");"+vbCrLf
   file.Write comStr
   file.Write "comment on table "+rs.code+" is '"+rs.Comment+"';"+vbCrLf
   file.Write "alter table "+rs.code+" add constraint pk_"+ rs.code +" primary key ("+ GetColumnStr(FirstCharToL(pEntity.Code)) +"_id,"+ GetColumnStr(FirstCharToL(otherEntity.Code)) +"_id);"

   'UTF-8转换
   ConvertToUTF8 filepath,filepathUTF8
End Function 

'一对多关系
Function getOneToMany(rs,roleName,pEntity,otherEntity,rsAttStr,commentStr)
   Dim attr
   For Each attr In otherEntity.Attributes
		If Not attr.IsShortcut Then 
			If attr.PrimaryIdentifier Then
				rsAttStr = rsAttStr + "  "+ GetColumnStr(rs.name) +" "+ GetType(attr.DataType,attr.Length,attr.Precision) +","+ vbCrLf
				commentStr = commentStr + "comment on column "+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+"."+GetColumnStr(rs.name)+"  is '"+otherEntity.Name+"ID';"+ vbCrLf
				commentStr = commentStr + "create index idx_"+ GetColumnStr(rs.code) +" on "+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+" ("+ GetColumnStr(rs.code) +");"+ vbCrLf
				commentStr = commentStr + "alter table "+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+" add constraint fk_"+ GetColumnStr(rs.code) +" foreign key ("+ GetColumnStr(rs.name) +") references "+table_top + GetColumnStr(FirstCharToL(otherEntity.Code))+" ("+attr.Code+");"+ vbCrLf
			End If
		End If	 
	Next
End Function 

'多对一关系
Function getManyToOne(rs,roleName,pEntity,otherEntity,rsAttStr,commentStr)
   Dim attr
   For Each attr In otherEntity.Attributes
		If Not attr.IsShortcut Then 
			If attr.PrimaryIdentifier Then
				rsAttStr = rsAttStr +","+ vbCrLf+ "  "+ GetColumnStr(rs.name) +" "+ GetType(attr.DataType,attr.Length,attr.Precision) 
				commentStr = commentStr + "comment on column "+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+"."+GetColumnStr(rs.name)+"  is '"+otherEntity.Name+"ID';"+ vbCrLf
				commentStr = commentStr + "create index "+ GetColumnStr(rs.code) +"_fk on "+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+" ("+ GetColumnStr(rs.name) +");"+ vbCrLf
				commentStr = commentStr + "alter table "+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+" add constraint fk_"+ GetColumnStr(rs.code) +" foreign key ("+ GetColumnStr(rs.name) +") references "+table_top + GetColumnStr(FirstCharToL(otherEntity.Code))+" ("+attr.Code+");"+ vbCrLf
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
           Case "I"   GetType = "NUMBER"
           Case "LI"  GetType = "NUMBER"
           Case "SI"  GetType = "NUMBER"

		   Case "BT"  GetType = "VARCHAR2("+vLength+")"
           
		   Case "N"  
			If vPrecision = 0 Then
				GetType = "NUMBER"
			Else 
				GetType = "NUMBER("+cstr(vLength)+","+vPrecision+")"
			End If 
		   Case "DC"  GetType = "NUMBER("+cstr(vLength)+","+cstr(vPrecision)+")"

		   Case "F"   GetType = "NUMBER("+cstr(vLength)+","+cstr(vPrecision)+")"
		   Case "SF"  GetType = "NUMBER("+cstr(vLength)+","+cstr(vPrecision)+")"
		   Case "LF"  GetType = "NUMBER("+cstr(vLength)+","+cstr(vPrecision)+")"

		   Case "MN"  GetType = "NUMBER("+cstr(vLength)+","+cstr(vPrecision)+")"
		   Case "NO"  GetType = "NUMBER"

		   Case "BL"  GetType = "NUMBER"

		   Case "A"   GetType = "VARCHAR2("+cstr(vLength)+")"
		   Case "VA"  GetType = "VARCHAR2("+cstr(vLength)+")"
		   Case "LA"  GetType = "VARCHAR2("+cstr(vLength)+")"
		   Case "LVA" GetType = "VARCHAR2("+cstr(vLength)+")"
		   Case "TXT" GetType = "VARCHAR2("+cstr(vLength)+")"

		   Case "MBT"  GetType = "BINARY"
		   Case "VMBT" GetType = "BINARY"

		   Case "D"   GetType = "DATE"
		   Case "T"   GetType = "DATE"
		   Case "DT"  GetType = "DATE"
		   Case "TS"  GetType = "DATE"

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
	'Output "bigLen=" + CStr(bigLen)
	If bigLen = strLen Then
		GetColumnStr = vstr
	Else
		GetColumnStr = newStr
	End If
	
End Function

'-----------------------------------------------------------------------------------------
