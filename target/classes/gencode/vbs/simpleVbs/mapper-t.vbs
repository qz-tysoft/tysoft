' 从模型类生成实体bean

Option explicit

'rootPath -- 代码包根目录的存放路径
'mapperRootPage -- 基础包路径
'tempPath -- 代码生成的临时目录
Dim rootPath,rootPage,mapperRootPage,tempPath,entityPage
'本实体下的包名
Dim classPage,mapperPack
'fso -- FileSystemObject
'wshNetwork -- WScript.Network
'entityPath -- 生成实体的路径
'author -- 注释上的作者名字
mapperPack = "mapper"
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
		rootPage = defaultRootPackage+FirstCharToL(ActiveModel.code)+"."+mapperPack
		mapperRootPage = defaultRootPackage+FirstCharToL(ActiveModel.code)+"."+mapperPack
	    Output "mapper包路径:" + mapperRootPage
		classPage =  getClassPage(ActiveDiagram.GetPackage())
		output "mapper子包路径:" + classPage
		entityPage = defaultRootPackage+FirstCharToL(ActiveModel.code) +".entity." +  FirstCharToL(ActivePackage.Code)
   Else
		rootPath = iniArray(0)
		Output "源代码根路径:" + rootPath
		CreateRootPahtFolder fso,rootPath
		rootPage = Mid(iniArray(1),2)
		mapperRootPage = Mid(iniArray(2),2)+"."+mapperPack
		Output "mapper包路径:" + mapperRootPage
		classPage =  getClassPage(ActiveDiagram.GetPackage())
		output "mapper子包路径:" + classPage
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
	 getClassPage =  mapperRootPage + "." + Left(getClassPage,Len(getClassPage)-1)
   Else
     getClassPage = mapperRootPage
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
	  Output "mapper目录：" + CreateEntityPageFolder
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
		EntityToMapperXml entity
    End If
  Next
End Function

'对选择的对象生成java Mapper代码
Function EntityToJava(pEntity)
   
   Dim entity,tempMapper
   Dim mapperName
   Dim UTF8Mapper
   
   '创建接口文件，创建Mapper xml文件
   mapperName = FirstCharToU(pEntity.Code)+"Mapper"
   tempMapper = tempPath + "\" + mapperName + ".java.gbk"
   UTF8Mapper = entityPath + "\" + mapperName + ".java"

   Dim fileMapper
   Set fileMapper = fso.OpenTextFile(tempMapper, 2, true)

   WriteJavaTop fileMapper,pEntity
    
   WritePageInfo fileMapper,getClassPage(ActivePackage)
 
   insertMapperImportPage fileMapper,pEntity

   Dim mapperStr
'   mapperStr = mapperStr + AddMapperMethod(pEntity)
   fileMapper.Write "/**" + vbCrLf
   fileMapper.Write " * " +pEntity.Name+ vbCrLf
   fileMapper.Write " */" + vbCrLf
   fileMapper.Write "public interface "+FirstCharToU(pEntity.Code)+"Mapper extends Mapper<"+FirstCharToU(pEntity.Code)+"> {" + vbCrLf
'   fileMapper.Write mapperStr + vbCrLf
   fileMapper.Write "    " + vbCrLf
   fileMapper.Write "}" + vbCrLf
   
  ConvertToUTF8 tempMapper,UTF8Mapper
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
Function insertMapperImportPage(file,entity)
	file.Write   vbCrLf
	file.Write   "import java.util.List;"+vbCrLf
	file.Write   "import tk.mybatis.mapper.common.Mapper;"+vbCrLf
    file.Write vbCrLf
	file.Write   "import "+entityPage + "."+FirstCharToU(entity.Code)+";"+vbCrLf
    file.Write vbCrLf
End Function

'生成接口方法
Function AddMapperMethod(entity)
    
	 AddMapperMethod =  "    " + vbCrLf

End Function


'对选择的对象生成Mapper xml代码
Function EntityToMapperXml(pEntity)
   
   Dim entity,tempMapperXml
   Dim mapperXmlName
   Dim UTF8MapperXml
   
   '创建接口文件，创建Mapper xml文件
   mapperXmlName = FirstCharToU(pEntity.Code)+"Mapper"
   tempMapperXml = tempPath + "\" + mapperXmlName + ".xml.gbk"
   UTF8MapperXml = entityPath + "\" + mapperXmlName + ".xml"

   Dim fileMapperXml
   Set fileMapperXml = fso.OpenTextFile(tempMapperXml, 2, true)

   Dim mapperStrXml
   mapperStrXml = mapperStrXml + AddMapperXmlMethod(pEntity)
   fileMapperXml.Write "<?xml version=""1.0"" encoding=""UTF-8"" ?>" + vbCrLf
   fileMapperXml.Write "<!DOCTYPE mapper PUBLIC ""-//mybatis.org//DTD Mapper 3.0//EN"" ""http://mybatis.org/dtd/mybatis-3-mapper.dtd"" >" + vbCrLf
   fileMapperXml.Write "<mapper namespace="""+classPage+"."+mapperXmlName+""" >" + vbCrLf
   fileMapperXml.Write mapperStrXml + vbCrLf
   fileMapperXml.Write "</mapper>" + vbCrLf
   
  ConvertToUTF8 tempMapperXml,UTF8MapperXml
End Function

'生成xml串
Function AddMapperXmlMethod(pEntity)   
	 AddMapperXmlMethod = "  <resultMap id=""BaseResultMap"" type="""+entityPage+"."+FirstCharToU(pEntity.Code)+""" >" + vbCrLf
	 Dim attr,resultMapStr,BaseColumnStr
	 For Each attr In pEntity.Attributes
	   If Not attr.IsShortcut Then 
			If attr.PrimaryIdentifier Then
				resultMapStr = resultMapStr + "    <id column="""+FirstCharToL(attr.Code)+""" property="""+FirstCharToL(attr.Code)+""" jdbcType="""+GetType(attr.DataType,attr.Length,attr.Precision)+""" />" + vbCrLf
			Else
				resultMapStr = resultMapStr + "    <result column="""+FirstCharToL(attr.Code)+""" property="""+FirstCharToL(attr.Code)+""" jdbcType="""+GetType(attr.DataType,attr.Length,attr.Precision)+""" />" + vbCrLf
			End If
			BaseColumnStr = BaseColumnStr+","+GetTableColumName(FirstCharToL(attr.Code))
	   End If 
	 Next
	 AddMapperXmlMethod = AddMapperXmlMethod+resultMapStr+"  </resultMap>" + vbCrLf

     AddMapperXmlMethod = AddMapperXmlMethod + "  <sql id=""Base_Column_List"" >" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + Mid(BaseColumnStr,2) + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "  </sql>" + vbCrLf

     AddMapperXmlMethod = AddMapperXmlMethod + "  <sql id="""+FirstCharToU(pEntity.Code)+"_Where_Clause"" >" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "    <where >" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "      <foreach collection=""oredCriteria"" item=""criteria"" separator=""or"" >" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "        <if test=""criteria.valid"" >" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "          <trim prefix=""("" suffix="")"" prefixOverrides=""and"" >" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "            <foreach collection=""criteria.criteria"" item=""criterion"" >" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "              <choose >" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                <when test=""criterion.noValue"" >" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                  and ${criterion.condition}" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                </when>" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                <when test=""criterion.singleValue"" >" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                  and ${criterion.condition} #{criterion.value}" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                </when>" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                <when test=""criterion.betweenValue"" >" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                  and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                </when>" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                <when test=""criterion.listValue"" >" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                  and ${criterion.condition}" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                  <foreach collection=""criterion.value"" item=""listItem"" open=""("" close="")"" separator="","" >" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                    #{listItem}" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                  </foreach>" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "                </when>" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "              </choose>" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "            </foreach>" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "          </trim>" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "        </if>" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "      </foreach>" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "    </where>" + vbCrLf
     AddMapperXmlMethod = AddMapperXmlMethod + "  </sql>" + vbCrLf

End Function

'驼峰命名改成下划线
Function GetTableColumName(vstr)
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
		newStr = LCase(newStr)
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
		GetTableColumName = vstr
	Else
		GetTableColumName = newStr
	End If
	
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
           Case "I"   GetType = "BIT"
           Case "LI"  GetType = "BIGINT"
           Case "SI"  GetType = "BIT"

		   Case "BT"  GetType = "VARCHAR"
           
		   Case "N"  
			If vPrecision = 0 Then
				GetType = "BIGINT"
			Else 
				GetType = "DOUBLE"
			End If 
		   Case "DC"  GetType = "DECIMAL"

		   Case "F"   GetType = "FLOAT"
		   Case "SF"  GetType = "FLOAT"
		   Case "LF"  GetType = "FLOAT"

		   Case "MN"  GetType = "DECIMAL"
		   Case "NO"  GetType = "BIGINT"

		   Case "BL"  GetType = "BOOLEAN"

		   Case "A"   GetType = "VARCHAR"
		   Case "VA"  GetType = "VARCHAR"
		   Case "LA"  GetType = "VARCHAR"
		   Case "LVA" GetType = "VARCHAR"
		   Case "TXT" GetType = "VARCHAR"

		   Case "MBT"  GetType = "BINARY"
		   Case "VMBT" GetType = "BINARY"

		   Case "D"   GetType = "TIMESTAMP"
		   Case "T"   GetType = "TIMESTAMP"
		   Case "DT"  GetType = "TIMESTAMP"
		   Case "TS"  GetType = "TIMESTAMP"

		   Case "BIN"  GetType = "BINARY"
		   Case "LBIN" GetType = "BINARY"

		   Case "BMP"  GetType = "BLOB"
		   Case "PIC"  GetType = "BLOB"
		   Case "OLE"  GetType = "CLOB"

           Case Else GetType = vtype
    End Select
End Function