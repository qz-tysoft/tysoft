' 从模型类生成实体bean 

Option explicit

'rootPath -- 代码包根目录的存放路径
'rootPage -- 基础包路径
'tempPath -- 代码生成的临时目录
Dim rootPath,rootPage,tempPath
'本实体下的包名
Dim classPage
'fso -- FileSystemObject
'wshNetwork -- WScript.Network
'entityPath -- 生成实体的路径
'author -- 注释上的作者名字
Dim fso,wshNetwork,entityPath
Dim table_top
table_top = ActivePackage.Comment + "_"
Set fso = CreateObject("Scripting.FileSystemObject")
'作者名称的获取
Set wshNetwork = CreateObject("WScript.Network")
'author = InputBox("输入名字","注释上的作者名字",wshNetwork.UserName)
''On   Error   Resume   Next
If author="" Then
	author = wshNetwork.UserName
End If
''If   Err   <>   0   Then
''Dim author
''author = InputBox("输入名字","注释上的作者名字",wshNetwork.UserName)
''End IF

'临时目录的生成
Dim nowDate
nowDate = Date
'tempPath = CStr(nowDate) + "-temp"
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
		Output "改项目没有定义生成路径，将按默认的相对路径生成。"
		Output "自定义生成路径请参考以下配置（如果没配置默认生成到该vbs文件相同的文件夹里）:"
		Output "请在ConceptualDataModel的Comment属性下注明:"
		Output "源代码根路径"
		Output "生成的页面路径"
		Output "基础包名"
		Output "例如:"
		Output "D:\projectPath\src\main\java"
		Output "D:\projectPath\src\main\resources\templates\project"
		Output "com.xmrbi.project"
		Output ""
		Output "<注：第1行为生成的源代码路径，第2行为生成的页面路径，第3行为基础包名>"
		'init = false
		'Exit Function
		rootPath = defaultRootPath
		Output "源代码根路径:" + rootPath
		CreateRootPahtFolder fso,rootPath
		rootPage = defaultRootPackage+FirstCharToL(ActiveModel.code)+".entity"
		Output "实体包路径:" + rootPage
		classPage =  getClassPage(ActiveDiagram.GetPackage())
		output "实体子包路径:" + classPage
   Else
		rootPath = iniArray(0)
		Output "源代码根路径:" + rootPath
		CreateRootPahtFolder fso,rootPath
		rootPage = Mid(iniArray(2),2)+".entity"
		Output "实体包路径:" + rootPage
		classPage =  getClassPage(ActiveDiagram.GetPackage())
		output "实体子包路径:" + classPage
		'init = True
   End If
   init = True
End Function


Function getClassPage(pPage)
	Dim currentPage
   getClassPage = ""
   Set currentPage = pPage
  ' Do While currentPage.ClassName <> "Conceptual Data Model" '去掉循环，只取上级的包信息 2013.7.22
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
Function CreateSelectEntityToJava()
   dim entity
   For Each entity In ActiveSelection 'ActiveModel.Entities
	If entity.ClassName = "Entity" Then
		EntityToJava entity
    End If
  Next
End Function

'生成实体类代码
Function EntityToJava(pEntity)
   Dim filepath,filepathUTF8
   filepath = tempPath + "\" + FirstCharToU(pEntity.Code) + ".java.gbk"
   filepathUTF8 = entityPath + "\" + FirstCharToU(pEntity.Code) + ".java"
   
   'If BackUpFile(filepathUTF8)=false then
   '   Exit Function
   'End If
   Dim file
   Set file = fso.OpenTextFile(filepath, 2, true)


   'importStr imports包
   'baseAttStr 基本属性
   'baseMethodStr基本方法
   'rsAttStr关联属性
   'rsMethodStr关联方法
   Dim importStr
   Dim baseAttStr,baseMethodStr
   Dim rsAttStr,rsMethodStr
   Dim parentEntity
   Dim entityAnnotateStr
   importStr = ""

   
   '写入代码信息
   WriteJavaTop(file)
   file.Write  vbCrLf
   file.Write  "package " + getClassPage(pEntity.GetPackage()) + ";"+vbCrLf
   file.Write vbCrLf
   
   parentEntity = getExtendEntity(pEntity,importStr)
   getBaseStr pEntity,importStr,baseAttStr,baseMethodStr 
   getRelationshipStr pEntity,importStr,rsAttStr,rsMethodStr
   
    '写imports
   
   
   '写实体注释
   entityAnnotateStr = getEntityAnnotate(pEntity,importStr)
   file.Write importStr + vbCrLf+ vbCrLf

   file.Write entityAnnotateStr
   file.Write "public class " + FirstCharToU(pEntity.code) + " implements "+parentEntity+"{" + vbCrLf
   file.Write  vbCrLf
   '写实体构造函数
   WriteEntityConstruct file,pEntity 
   file.Write  vbCrLf
   '写属性
   file.Write  baseAttStr
   file.Write  rsAttStr
   '写方法
   file.Write  baseMethodStr
   file.Write  rsMethodStr

   '写vopo
   WritePoToVo file,pEntity
   '写json
   WritePoToJson file,pEntity
   '写jsonMap
   WritePoToMap file,pEntity

   file.Write "}"
   'UTF-8转换
   ConvertToUTF8 filepath,filepathUTF8
End Function


'写文件头注释
Function WriteJavaTop(file)
	file.Write "/**" + vbCrLf
 	file.Write "* <p>Description: "+ActiveModel.name+" "+ ActiveModel.code +"</p>" + vbCrLf
 	file.Write "*" + vbCrLf
 	file.Write "* <p>Copyright: Copyright (c) "+ CStr(DatePart("yyyy",Date)) +"</p>" + vbCrLf
 	file.Write "*" + vbCrLf
 	file.Write "* <p>Company: 厦门路桥信息股份有限公司</p>" + vbCrLf
 	file.Write "*" + vbCrLf
 	file.Write "* @author :" + author + vbCrLf
 	file.Write "* @version 1.0" + vbCrLf
 	file.Write "*/" + vbCrLf
End Function


'写实体注释
Function getEntityAnnotate(pEntity,importStr)
   insertImportPage importStr,"java.util.HashMap" 
   insertImportPage importStr,"java.util.Map" 
   insertImportPage importStr,"java.text.SimpleDateFormat" 
   insertImportPage importStr,"javax.persistence.Entity" 
   insertImportPage importStr,"org.hibernate.annotations.Cache" 
   insertImportPage importStr,"org.hibernate.annotations.CacheConcurrencyStrategy"
   insertImportPage importStr,"org.hibernate.annotations.GenericGenerator"
   insertImportPage importStr,"org.springframework.format.annotation.DateTimeFormat"

   getEntityAnnotate =    "/**" + vbCrLf
   getEntityAnnotate = getEntityAnnotate +" * "+ pEntity.Name + " " + pEntity.Comment + vbCrLf
   getEntityAnnotate = getEntityAnnotate + " * 创建日期 " + CStr(Date) + " "  + CStr(Time) + vbCrLf
   getEntityAnnotate = getEntityAnnotate + " */" + vbCrLf
   getEntityAnnotate = getEntityAnnotate + "@Entity" + vbCrLf
   getEntityAnnotate = getEntityAnnotate + "@Table(name="""+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+""")"  + vbCrLf
   'getEntityAnnotate = getEntityAnnotate + "@Table(appliesTo="""+table_top + GetColumnStr(FirstCharToL(pEntity.Code))+""",comment="""+pEntity.name+""")"  + vbCrLf
   getEntityAnnotate = getEntityAnnotate + "@Inheritance(strategy = InheritanceType.JOINED)"  + vbCrLf
   getEntityAnnotate = getEntityAnnotate + "@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)"  + vbCrLf
End Function
'写实体构造函数
Function WriteEntityConstruct(file,pEntity)
   file.Write "    private static final long serialVersionUID = "+CStr(Int(Rnd*1000000*Rnd*1000000))+"L;" + vbCrLf+ vbCrLf
   file.Write "    public  " + FirstCharToU(pEntity.code) + "(){" + vbCrLf + "    }" + vbCrLf
   file.Write "    public  " + FirstCharToU(pEntity.code) + "(String id){" + vbCrLf 
   file.Write "      this.id = id;" + vbCrLf
   file.Write "    }" + vbCrLf
End Function

'写实体父类
Function getExtendEntity(pEntity,importStr)
   Dim parentObjs,p
   Set parentObjs = pEntity.InheritsFrom
   For Each p In parentObjs
		Dim parent,parentImport
		Set parent = p.ParentEntity
		getExtendEntity = FirstCharToU(parent.Code)
		Output FirstCharToU(parent.Code) + "'s IsShortcut = "+CStr(parent. IsShortcut())
		If parent. IsShortcut()=True Then 
			parentImport = getClassPage(parent.TargetPackage) + "." + FirstCharToU(parent.Code)
		    insertImportPage importStr,parentImport 
		End If
		Exit Function
   Next
   importStr = importStr + "import java.io.Serializable;"
   getExtendEntity = "Serializable"
End Function

Function insertImportPage(importStr,parentImport)
	
	Dim currentImport
    currentImport = "import "+parentImport  + ";"

	If InStr(importStr,currentImport)=0 Then 
		importStr = importStr + vbCrLf + currentImport 
		
	End If
End Function



'生成实体的属性
Function getBaseStr(pEntity,importStr,baseAttStr,baseMethodStr)
	Dim attr
    baseAttStr = ""
    baseMethodStr = ""
	For Each attr In pEntity.Attributes
			 If Not attr.IsShortcut Then 
				   baseAttStr = baseAttStr + AddBaseAttrib(attr)
				   baseMethodStr = baseMethodStr + AddBaseMethod(pEntity.Code,attr,importStr)
			 End If
			 
	Next
   
	
End Function


'生成属性方法
Function AddBaseAttrib(attr)
	Dim tempComment 
	tempComment = attr.Name
	If Trim(attr.comment) <> "" then
		tempComment = tempComment + ":" + attr.Comment
	End If
	AddBaseAttrib = "    /**" + vbCrLf 
	AddBaseAttrib = AddBaseAttrib + "     * "+ tempComment + vbCrLf  
	AddBaseAttrib = AddBaseAttrib + "     */" + vbCrLf 
'	If attr.PrimaryIdentifier Then
'		AddBaseAttrib = AddBaseAttrib + "    private String " + FirstCharToL(attr.Code) + ";" + vbCrLf 
'	Else
	Dim td
	td = attr.DataType
'	Output attr.name + ":"+td +"--"+ cstr(attr.length) +"--"+ cstr(attr.Precision)
		AddBaseAttrib = AddBaseAttrib + "    private " + GetType(attr.DataType,attr.Length,attr.Precision) + " " + FirstCharToL(attr.Code) + ";" + vbCrLf 
'	End If 
	AddBaseAttrib = AddBaseAttrib + vbCrLf
End Function



'生成方法
Function AddBaseMethod(entityCode,attr,importStr)
     Dim hiberComment
	 hiberComment = ""
	 If attr.PrimaryIdentifier Then
		insertImportPage importStr,"javax.persistence.Id" 
		insertImportPage importStr,"javax.persistence.GeneratedValue" 
		insertImportPage importStr,"org.hibernate.annotations.GenericGenerator" 
		'insertImportPage importStr,"org.hibernate.annotations.Table" 
		insertImportPage importStr,"org.springframework.format.annotation.DateTimeFormat" 
	    insertImportPage importStr,"javax.persistence.Table" 
	    insertImportPage importStr,"javax.persistence.Inheritance" 
	    insertImportPage importStr,"javax.persistence.InheritanceType" 
	    insertImportPage importStr,"javax.persistence.Column" 

		 hiberComment = vbCrLf+"    @Id" + vbCrLf
		 hiberComment = hiberComment + "    @GenericGenerator(name=""idGenerator"", strategy=""uuid"")"+ vbCrLf
		 hiberComment = hiberComment + "    @GeneratedValue(generator=""idGenerator"")"+ vbCrLf
		 AddBaseMethod = hiberComment
		 AddBaseMethod = AddBaseMethod  + "    /**" + vbCrLf
		 AddBaseMethod = AddBaseMethod  + "     *@return:"+ GetType(attr.DataType,attr.Length,attr.Precision) +" " + attr.Name + vbCrLf
		 AddBaseMethod = AddBaseMethod  + "     */" + vbCrLf
		 AddBaseMethod = AddBaseMethod  + "    @Column(length="+CStr(attr.Length)+")" + vbCrLf
		 AddBaseMethod = AddBaseMethod  + "    public "+ GetType(attr.DataType,attr.Length,attr.Precision) +" get" + FirstCharToU(attr.Code) + "(){"
		 AddBaseMethod = AddBaseMethod + vbCrLf + "      return this." + FirstCharToL(attr.Code)+";"
		 AddBaseMethod = AddBaseMethod + vbCrLf + "    }"

		 AddBaseMethod = AddBaseMethod + vbCrLf
		 AddBaseMethod = AddBaseMethod  + "    /**" + vbCrLf
		 AddBaseMethod = AddBaseMethod  + "     *@param:"+ GetType(attr.DataType,attr.Length,attr.Precision) +" " + attr.Name + vbCrLf
		 AddBaseMethod = AddBaseMethod  + "     */" + vbCrLf         
		 AddBaseMethod = AddBaseMethod  + "    public void set" + FirstCharToU(attr.Code) + "("+ GetType(attr.DataType,attr.Length,attr.Precision) +" " + FirstCharToL(attr.Code) + "){ "
		 AddBaseMethod = AddBaseMethod + vbCrLf + "      this." + FirstCharToL(attr.Code) + "=" + FirstCharToL(attr.Code) +";"
		 AddBaseMethod = AddBaseMethod + vbCrLf + "    }"
		 AddBaseMethod = AddBaseMethod + vbCrLf
		 AddBaseMethod = AddBaseMethod + vbCrLf
	 Else 
		 AddBaseMethod = hiberComment
		 AddBaseMethod = AddBaseMethod  + "    /**" + vbCrLf
		 AddBaseMethod = AddBaseMethod  + "     *@return:"+ GetType(attr.DataType,attr.Length,attr.Precision) +" " + attr.Name + vbCrLf
		 AddBaseMethod = AddBaseMethod  + "     */" + vbCrLf
	     If GetType(attr.DataType,attr.Length,attr.Precision) = "java.util.Date" Then
		 AddBaseMethod = AddBaseMethod  + "    @DateTimeFormat(pattern = ""yyyy-MM-dd HH:mm:ss"")" + vbCrLf
		 ElseIf GetType(attr.DataType,attr.Length,attr.Precision) = "java.lang.String" Then
		 AddBaseMethod = AddBaseMethod  + "    @Column(length="+CStr(attr.Length)+")" + vbCrLf
	     End If
		 AddBaseMethod = AddBaseMethod  + "    public "+ GetType(attr.DataType,attr.Length,attr.Precision) +" get" + FirstCharToU(attr.Code) + "(){"
		 AddBaseMethod = AddBaseMethod + vbCrLf + "      return this." + FirstCharToL(attr.Code)+";"
		 AddBaseMethod = AddBaseMethod + vbCrLf + "    }"

		 AddBaseMethod = AddBaseMethod + vbCrLf
         AddBaseMethod = AddBaseMethod  + "    /**" + vbCrLf
		 AddBaseMethod = AddBaseMethod  + "     *@param:"+ GetType(attr.DataType,attr.Length,attr.Precision) +" " + attr.Name + vbCrLf
		 AddBaseMethod = AddBaseMethod  + "     */" + vbCrLf
		 AddBaseMethod = AddBaseMethod  + "    public void set" + FirstCharToU(attr.Code) + "(" + GetType(attr.DataType,attr.Length,attr.Precision) + " " + FirstCharToL(attr.Code) + "){ "
		 AddBaseMethod = AddBaseMethod + vbCrLf + "      this." + FirstCharToL(attr.Code) + "=" + FirstCharToL(attr.Code) +";"
		 AddBaseMethod = AddBaseMethod + vbCrLf + "    }"
		 AddBaseMethod = AddBaseMethod + vbCrLf
		 AddBaseMethod = AddBaseMethod + vbCrLf
	 End If
End Function




Function getRelationshipStr(pEntity,importStr,rsAttStr,rsMethodStr)
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
					getManyToMany rs,roleName,pEntity,otherEntity,importStr,rsAttStr,rsMethodStr
				End If 
			'非多对多
			Else 
				Dim objectNum,otherObjectNum
				objectNum = GetVaule(toCardinality,",",1)
				otherObjectNum = GetVaule(toCardinality,",",0)
				
				'配置的是一对多的对应关系 roleName为空不生成
				If objectNum = "n" And roleName <> "" Then
					'生成一对多的属性和方法
					getOneToMany rs,roleName,otherEntity,importStr,rsAttStr,rsMethodStr
				End If 
				'配置的是多对一的对应关系
				If objectNum = "1"   Then
					'生成多对一的属性和方法
					getManyToOne rs,roleName,otherEntity,importStr,rsAttStr,rsMethodStr 
				End If
			End If
			
			If Trim(roleName)<> ""  And pEntity.GetPackage() <> otherEntity.GetPackage() Then 
					Dim otherImport
					otherImport = getClassPage(otherEntity.GetPackage()) + "." + FirstCharToU(otherEntity.Code)
					insertImportPage importStr,otherImport 
			End If
			doneRsCodes = doneRsCodes + rs.code+";"
		End If 
	Next
End Function

'多对多关系
Function getManyToMany(rs,roleName,pEntity,otherEntity,importStr,rsAttStr,rsMethodStr)
		rsAttStr = rsAttStr + "    /**"+ vbCrLf
		rsAttStr = rsAttStr + "     * "  + rs.Comment +" "+ otherEntity.name + vbCrLf
		rsAttStr = rsAttStr + "     */"+ vbCrLf
		insertImportPage importStr,"java.util.List" 
		insertImportPage importStr,"java.util.ArrayList" 
		insertImportPage importStr,getClassPage(otherEntity.GetPackage())+"."+FirstCharToU(otherEntity.code)

		rsAttStr = rsAttStr + "    private List<"+FirstCharToU(otherEntity.Code)+"> "+FirstCharToL(roleName) +" = new ArrayList<"+FirstCharToU(otherEntity.Code)+">();"+ vbCrLf
	    insertImportPage importStr,"javax.persistence.FetchType"
		insertImportPage importStr,"javax.persistence.CascadeType" 
		insertImportPage importStr,"javax.persistence.ManyToMany" 
		insertImportPage importStr,"javax.persistence.JoinTable" 
		insertImportPage importStr,"javax.persistence.JoinColumn" 
		'insertImportPage importStr,"javax.persistence.Table" 
	    insertImportPage importStr,"javax.persistence.Inheritance" 
	    insertImportPage importStr,"javax.persistence.InheritanceType" 
		insertImportPage importStr,"org.hibernate.annotations.Fetch"
		insertImportPage importStr,"org.hibernate.annotations.FetchMode"
		insertImportPage importStr,"org.hibernate.annotations.Cache"
		insertImportPage importStr,"org.hibernate.annotations.CacheConcurrencyStrategy"
		rsMethodStr = rsMethodStr + "    @ManyToMany "+ vbCrLf
		rsMethodStr = rsMethodStr + "    @JoinTable(name = """+GetColumnStr(rs.code)+""", "
		rsMethodStr = rsMethodStr + " joinColumns = { @JoinColumn(name = """+GetColumnStr(FirstCharToL(pEntity.code)) + "_id"") }, "
		rsMethodStr = rsMethodStr + " inverseJoinColumns = { @JoinColumn(name = """+GetColumnStr(FirstCharToL(otherEntity.code))+"_id"") })"+ vbCrLf
		rsMethodStr = rsMethodStr + "    @Fetch(FetchMode.SUBSELECT)"+ vbCrLf
		'rsMethodStr = rsMethodStr + "@OrderBy("""+id+""")"+ vbCrLf
		rsMethodStr = rsMethodStr + "    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)"+ vbCrLf
		rsMethodStr = rsMethodStr + "    public List<"+FirstCharToU(otherEntity.code)+"> get"+FirstCharToU(roleName)+"() {"+ vbCrLf
		rsMethodStr = rsMethodStr + "       return "+FirstCharToL(roleName)+";"+ vbCrLf
		rsMethodStr = rsMethodStr + "    }"+ vbCrLf

		rsMethodStr = rsMethodStr + "    public void set"+FirstCharToU(roleName)+"(List<"+FirstCharToU(otherEntity.code)+"> "+FirstCharToL(roleName)+") {"+ vbCrLf
		rsMethodStr = rsMethodStr + "       this."+FirstCharToL(roleName)+" = "+FirstCharToL(roleName)+";"+ vbCrLf
		rsMethodStr = rsMethodStr + "    }"+ vbCrLf
End Function 
'一对多关系
Function getOneToMany(rs,roleName,otherEntity,importStr,rsAttStr,rsMethodStr)
	rsAttStr = rsAttStr + "    /**"+ vbCrLf
	rsAttStr = rsAttStr + "     * "  + rs.Comment +" "+ otherEntity.name + vbCrLf
	rsAttStr = rsAttStr + "     */"+ vbCrLf

	
	insertImportPage importStr,"java.util.List" 
	insertImportPage importStr,"java.util.ArrayList" 
	insertImportPage importStr,"javax.persistence.FetchType"
	insertImportPage importStr,"javax.persistence.OneToMany" 
	insertImportPage importStr,"javax.persistence.JoinColumn" 
	'insertImportPage importStr,"javax.persistence.Table" 
	insertImportPage importStr,"javax.persistence.Inheritance" 
	insertImportPage importStr,"javax.persistence.InheritanceType" 
	insertImportPage importStr,"org.hibernate.annotations.Fetch"
	insertImportPage importStr,"org.hibernate.annotations.FetchMode"
	insertImportPage importStr,"org.hibernate.annotations.Cache"
	insertImportPage importStr,"org.hibernate.annotations.CacheConcurrencyStrategy"
	insertImportPage importStr,"javax.persistence.OrderBy"
	insertImportPage importStr,"javax.persistence.CascadeType" 
	insertImportPage importStr,getClassPage(otherEntity.GetPackage())+"."+FirstCharToU(otherEntity.code)


	rsAttStr = rsAttStr + "    private List<"+FirstCharToU(otherEntity.code)+"> "+FirstCharToL(roleName)+" = new ArrayList<"+FirstCharToU(otherEntity.code)+">();"+ vbCrLf
	rsMethodStr = rsMethodStr + "    @OneToMany(cascade={CascadeType.ALL},fetch=FetchType.LAZY) "+ vbCrLf
    rsMethodStr = rsMethodStr + "    @JoinColumn(name="""+GetColumnStr(rs.name)+""")"+ vbCrLf 'GetColumnStr(rs.name)
    rsMethodStr = rsMethodStr + "    @Fetch(FetchMode.SUBSELECT)"+ vbCrLf
    rsMethodStr = rsMethodStr + "    @OrderBy(""id"")"+ vbCrLf
    rsMethodStr = rsMethodStr + "    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)"+ vbCrLf
	rsMethodStr = rsMethodStr + "    public List<"+FirstCharToU(otherEntity.code)+"> get"+FirstCharToU(roleName)+"() {"+ vbCrLf
    rsMethodStr = rsMethodStr + "      return "+FirstCharToL(roleName)+";"+ vbCrLf
    rsMethodStr = rsMethodStr + "    }"+ vbCrLf
	rsMethodStr = rsMethodStr + vbCrLf
	rsMethodStr = rsMethodStr + "    public void set"+FirstCharToU(roleName)+"(List<"+FirstCharToU(otherEntity.code)+"> "+FirstCharToL(roleName)+") {"+ vbCrLf
    rsMethodStr = rsMethodStr + "      this."+FirstCharToL(roleName)+" = "+FirstCharToL(roleName)+";"+ vbCrLf
    rsMethodStr = rsMethodStr + "    }"+ vbCrLf
End Function 
'多对一关系
Function getManyToOne(rs,roleName,otherEntity,importStr,rsAttStr,rsMethodStr)
	rsAttStr = rsAttStr + "    /**"+ vbCrLf
	rsAttStr = rsAttStr + "     * " + rs.Comment +" "+ otherEntity.name + vbCrLf
	rsAttStr = rsAttStr + "     */"+ vbCrLf

	If Trim(roleName)="" Then 
		rsAttStr = rsAttStr + "    private Long " + FirstCharToL(rs.name) + ";" + vbCrLf 
		rsAttStr = rsAttStr + vbCrLf
		rsMethodStr = rsMethodStr  + "    public Long get" + FirstCharToU(rs.name) + "(){"+ vbCrLf
		rsMethodStr = rsMethodStr + vbCrLf + "      return this." + FirstCharToL(rs.name)+";"+ vbCrLf
		rsMethodStr = rsMethodStr + vbCrLf + "    }"+ vbCrLf

		rsMethodStr = rsMethodStr + vbCrLf + "    public void set" + FirstCharToU(rs.name) + "(Long " + FirstCharToL(rs.name) + "){ "+ vbCrLf
		rsMethodStr = rsMethodStr + vbCrLf + "      this." + FirstCharToL(rs.name) + "=" + FirstCharToL(rs.name) +";"+ vbCrLf
		rsMethodStr = rsMethodStr + vbCrLf + "    }"+ vbCrLf
		rsMethodStr = rsMethodStr + vbCrLf
		rsMethodStr = rsMethodStr + vbCrLf
	Else 
	    insertImportPage importStr,"javax.persistence.FetchType"
		insertImportPage importStr,"javax.persistence.ManyToOne" 
		insertImportPage importStr,"javax.persistence.ForeignKey" 
		insertImportPage importStr,"javax.persistence.JoinColumn" 
		insertImportPage importStr,"javax.persistence.CascadeType" 
		insertImportPage importStr,getClassPage(otherEntity.GetPackage())+"."+FirstCharToU(otherEntity.code)

		rsAttStr = rsAttStr + "    private " + FirstCharToU(otherEntity.code) + " " + FirstCharToL(roleName) + ";" + vbCrLf 
		rsAttStr = rsAttStr + vbCrLf
		
		rsMethodStr = rsMethodStr + "    @ManyToOne( cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY )" + vbCrLf 
		rsMethodStr = rsMethodStr + "    @JoinColumn(name="""+GetColumnStr(rs.name)+""",nullable = true,foreignKey=@ForeignKey(name=""fk_"+ GetColumnStr(rs.code) +"""))" + vbCrLf  'GetColumnStr(rs.name)
		rsMethodStr = rsMethodStr + "    public " + FirstCharToU(otherEntity.code) + " get" + FirstCharToU(roleName) + "() {" + vbCrLf 
		rsMethodStr = rsMethodStr + "       return " + FirstCharToL(roleName) + ";"+ vbCrLf 
		rsMethodStr = rsMethodStr + "    }" + vbCrLf 

		rsMethodStr = rsMethodStr + "    public void set" + FirstCharToU(roleName) + "(" + FirstCharToU(otherEntity.code) + " " + FirstCharToL(roleName) + ") {" + vbCrLf 
		rsMethodStr = rsMethodStr + "       this." + FirstCharToL(roleName) + " = " + FirstCharToL(roleName) + ";" + vbCrLf 
		rsMethodStr = rsMethodStr + "    }" + vbCrLf 
	End If 
	
	

End Function 

'写实体PoToVo
Function WritePoToVo(file,pEntity)
    file.Write "" + vbCrLf
    file.Write "    /**PoToVo*/" + vbCrLf
	file.Write "    public "++FirstCharToU(pEntity.Code)+" poToVo() {" + vbCrLf
	file.Write "        "+FirstCharToU(pEntity.Code)+ " vo = new "+FirstCharToU(pEntity.Code)+ "();"+ vbCrLf
	Dim attr
	For Each attr In pEntity.Attributes
			 If Not attr.IsShortcut Then 
					If attr.PrimaryIdentifier Then
						file.Write "        vo.setId(this.id);"+ vbCrLf
				   Else
						file.Write "        vo.set"+FirstCharToU(attr.Code)+"(this."+FirstCharToL(attr.code)+");"+ vbCrLf
				   End If
			 End If		 
	Next
	file.Write "       return vo;"+ vbCrLf
	file.Write "    }"+ vbCrLf
	
	
End Function

'写实体PoToJson
Function WritePoToJson(file,pEntity)
    file.Write "" + vbCrLf
    file.Write "    /**PoToJson*/" + vbCrLf
	file.Write "    public String poToJson() {" + vbCrLf
	file.Write "    	SimpleDateFormat sdf = new SimpleDateFormat(""yyyy-MM-dd HH:mm:ss"");"+ vbCrLf
	file.Write "    	StringBuilder sb = new StringBuilder(""{"");"+ vbCrLf
	Dim attr,num
	num = 0
	For Each attr In pEntity.Attributes
		 If Not attr.IsShortcut Then 
		       If num > 0 Then
			      file.Write "        sb.append("","");"+ vbCrLf
			   End If
			   If GetType(attr.DataType,attr.Length,attr.Precision)<> "java.util.Date" Then
			      file.Write "        sb.append(""\"""+FirstCharToL(attr.Code)+"\"":\"""").append(this.get"+FirstCharToU(attr.code)+"()).append(""\"""");"+ vbCrLf
			   Else
			      file.Write "        sb.append(""\"""+FirstCharToL(attr.Code)+"\"":\"""").append(this.get"+FirstCharToU(attr.code)+"() == null ? null : sdf.format(this.get"+FirstCharToU(attr.code)+"())).append(""\"""");"+ vbCrLf
			   End If
			   num = num + 1
		 End If
	Next
	file.Write "        sb.append(""}"");"+ vbCrLf
	file.Write "        return sb.toString();"+ vbCrLf
	file.Write "    }"+ vbCrLf
End Function

'写实体PoToMap
Function WritePoToMap(file,pEntity)
    file.Write "" + vbCrLf
    file.Write "    /**PoToMap*/" + vbCrLf
	file.Write "    public Map<String, Object> poToMap() {" + vbCrLf
	file.Write "    	SimpleDateFormat sdf = new SimpleDateFormat(""yyyy-MM-dd HH:mm:ss"");"+ vbCrLf
	file.Write "    	Map<String, Object> jsonMap = new HashMap<String, Object>();"+ vbCrLf
	Dim attr
	For Each attr In pEntity.Attributes
		 If Not attr.IsShortcut Then 
			   If GetType(attr.DataType,attr.Length,attr.Precision) = "java.util.Date" Then
			      file.Write "        jsonMap.put("""+FirstCharToL(attr.Code)+""",this."+FirstCharToL(attr.code)+" == null ? null : sdf.format(this."+FirstCharToL(attr.code)+"));"+ vbCrLf
			   Else
			      file.Write "        jsonMap.put("""+FirstCharToL(attr.Code)+""",this."+FirstCharToL(attr.code)+");"+ vbCrLf
			   End If
		 End If
	Next

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
			
			'非多对多
			If rsType <> 3 Then
				Dim objectNum,otherObjectNum
				objectNum = GetVaule(toCardinality,",",1)
				otherObjectNum = GetVaule(toCardinality,",",0)
				
				'配置的是多对一的对应关系
				If objectNum = "1"   Then
					'生成多对一的属性和方法
					file.Write "        jsonMap.put("""+FirstCharToL(roleName)+""", this."+FirstCharToL(roleName)+"==null?null:this."+FirstCharToL(roleName)+".poToMap());"+ vbCrLf
				End If
			End If
			doneRsCodes = doneRsCodes + rs.code+";"
		End If 
	Next

	file.Write "        return jsonMap;"+ vbCrLf
	file.Write "    }"+ vbCrLf
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
           Case "I"   GetType = "java.lang.Integer"
           Case "LI"  GetType = "java.lang.Long"
           Case "SI"  GetType = "java.lang.Short"

		   Case "BT"  GetType = "java.lang.Byte"
           
		   Case "N"  
			If vPrecision = 0 Then
				GetType = "java.lang.Long"
			Else 
				GetType = "java.lang.Double"
			End If 
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

'获取字段注解
Function GetColumnComment(attr)
    Dim colComment
    If GetType(attr.DataType,attr.Length,attr.Precision) = "java.lang.String" Then
		 GetColumnComment = "@Column(name = """+GetColumnStr(FirstCharToL(attr.Code))+""",length="+CStr(attr.Length)+",columnDefinition=""COMMENT '"+attr.name+"'"")"
	Else
		 GetColumnComment = "@Column(name = """+GetColumnStr(FirstCharToL(attr.Code))+""",columnDefinition=""COMMENT '"+attr.name+"'"")"
	End If
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

	If bigLen = strLen Then
		GetColumnStr = vstr
	Else
		GetColumnStr = newStr
	End If
	
End Function

'-----------------------------------------------------------------------------------------
