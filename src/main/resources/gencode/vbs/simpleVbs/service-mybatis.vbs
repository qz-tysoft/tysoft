' 从模型类生成实体bean

Option explicit

'rootPath -- 代码包根目录的存放路径
'serviceRootPage -- 基础包路径
'tempPath -- 代码生成的临时目录
Dim rootPath,serviceRootPage,rootPage,tempPath,entityPage
'本实体下的包名
Dim classPage,implPage
'fso -- FileSystemObject
'wshNetwork -- WScript.Network
'entityPath -- 生成实体的路径
'author -- 注释上的作者名字
implPage = "impl"
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
   CreateFolder fso,entityPath+"\"+implPage
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
		rootPage = defaultRootPackage+FirstCharToL(ActiveModel.code)
		serviceRootPage = defaultRootPackage+FirstCharToL(ActiveModel.code)+".service"
	    Output "服务包路径:" + serviceRootPage
		classPage =  getClassPage(ActiveDiagram.GetPackage())
		output "classPage:" + classPage
		entityPage = defaultRootPackage+FirstCharToL(ActiveModel.code) +".entity." +  FirstCharToL(ActivePackage.Code)
   Else
		rootPath = iniArray(0)
		Output "源代码根路径:" + rootPath
		rootPage = Mid(iniArray(2),2)
		serviceRootPage = Mid(iniArray(2),2)+".service"
		Output "服务包路径:" + serviceRootPage
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
	 getClassPage =  serviceRootPage + "." + Left(getClassPage,Len(getClassPage)-1)
   Else
     getClassPage = serviceRootPage
   End If
   
End Function 

'BEGIN -------------------目录的创建
'创建包根目录
Function CreateEntityPageFolder(vfso, vpath, vpage)
	Dim tempArray,temp
	CreateEntityPageFolder = vpath
	tempArray = Split(vpage, ".")
	
	For Each temp in tempArray
      CreateEntityPageFolder = CreateEntityPageFolder +"\" + temp
	  CreateFolder vfso, CreateEntityPageFolder
	  Output "创建service目录：" + CreateEntityPageFolder
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
		EntityToService entity
    End If
  Next
End Function

'对选择的对象生成java Service代码
Function EntityToService(pEntity)
   
   Dim entity,tempInterface,tempImpl
   Dim interfaceName,implName
   Dim UTF8Interface,UTF8Impl
   
   
   '创建接口文件，创建抽象文件，创建实现文件
   interfaceName = FirstCharToU(pEntity.Code)+"Service"
   implName = FirstCharToU(pEntity.Code) + "ServiceImpl"
   tempInterface = tempPath + "\" + interfaceName + ".java.gbk"
   tempImpl =  tempPath + "\" + implName + ".java.gbk"
   UTF8Interface = entityPath + "\" + interfaceName + ".java"
   UTF8Impl = entityPath + "\impl\" + implName + ".java"

   Dim fileInter,fileImpl
   Set fileInter = fso.OpenTextFile(tempInterface, 2, true)
   Set fileImpl = fso.OpenTextFile(tempImpl, 2, true)

   WriteJavaTop(fileInter)
   WriteJavaTop(fileImpl)
    
   WritePageInfo fileInter,getClassPage(ActivePackage)
   WritePageInfo fileImpl,getClassPage(ActivePackage)+".impl"
 
   insertInterImportPage fileInter,pEntity
   insertImpImportPage fileImpl,pEntity

   Dim interStr,implStr
   interStr = interStr + AddInterfaceMethod(pEntity)
   implStr = implStr + AddImplMethod(pEntity)

   fileInter.Write "/**" + vbCrLf 
   fileInter.Write " * "+FirstCharToU(pEntity.Name)+"管理服务接口类" + vbCrLf 
   fileInter.Write " */" + vbCrLf 	
   fileInter.Write "public interface "+FirstCharToU(pEntity.Code)+"Service {" + vbCrLf 
   fileInter.Write interStr + vbCrLf
   fileInter.Write "}" + vbCrLf
   
   fileImpl.Write "/**" + vbCrLf 
   fileImpl.Write " * "+FirstCharToU(pEntity.Name)+"管理服务实现类" + vbCrLf 
   fileImpl.Write " */" + vbCrLf 
   fileImpl.Write "@Service("""+FirstCharToL(pEntity.Code)+"Service"")"+ vbCrLf
   fileImpl.Write "@Transactional"+ vbCrLf
   fileImpl.Write "public class " + implName + " implements "+FirstCharToU(pEntity.Code)+"Service {" + vbCrLf 
   fileImpl.Write implStr+ vbCrLf
   fileImpl.Write "}" + vbCrLf

  ConvertToUTF8 tempInterface,UTF8Interface
  ConvertToUTF8 tempImpl,UTF8Impl
End Function


'写文件头注释
Function WriteJavaTop(file)
	file.Write "/**" + vbCrLf
 	file.Write "* <p>Description: "+ActivePackage.name+" "+ ActivePackage.Comment +"</p>" + vbCrLf
 	file.Write "* <p>Copyright: Copyright (c) "+ CStr(DatePart("yyyy",Date)) +"</p>" + vbCrLf
 	file.Write "* <p>Company: 厦门路桥信息股份有限公司</p>" + vbCrLf
 	file.Write "*" + vbCrLf
 	file.Write "* @author :" + author + vbCrLf
 	file.Write "* @version 1.0" + vbCrLf
 	file.Write "*/" + vbCrLf
End Function


'写文件头注释
Function WritePageInfo(file,pageinfo)
   file.Write  vbCrLf
   file.Write  "package " + pageinfo + ";"+vbCrLf
   file.Write vbCrLf
End Function


'接口类的引入包
Function insertInterImportPage(file,entity)
	file.Write   vbCrLf
	file.Write   "import java.util.Map;"+vbCrLf
	file.Write   "import org.springframework.data.domain.Sort;"+vbCrLf
	file.Write   "import com.github.pagehelper.PageInfo;"+vbCrLf
    'file.Write   "import "+serviceRootPage+".BaseService;"+vbCrLf
    file.Write   "import "+entityPage+"."+FirstCharToU(entity.Code)+";"+vbCrLf
    file.Write vbCrLf
End Function

'实现类的引入包
Function insertImpImportPage(file,entity)
	file.Write   vbCrLf
	file.Write   "import java.util.Map;"+vbCrLf
	file.Write   "import javax.transaction.Transactional;"+vbCrLf
	file.Write   "import org.springframework.beans.factory.annotation.Autowired;"+vbCrLf
	file.Write   "import org.springframework.data.domain.Sort;"+vbCrLf
	file.Write   "import org.springframework.data.domain.Pageable;"+vbCrLf
	file.Write   "import org.springframework.stereotype.Service;"+vbCrLf
	file.Write   "import com.github.pagehelper.Page;"+vbCrLf
	file.Write   "import com.github.pagehelper.PageHelper;"+vbCrLf
	file.Write   "import com.github.pagehelper.PageInfo;"+vbCrLf
	file.Write   vbCrLf
    'file.Write   "import "+serviceRootPage+".BaseServiceImpl;"+vbCrLf
    file.Write   "import "+entityPage+"."+FirstCharToU(entity.Code)+";"+vbCrLf
    file.Write   "import "+classPage+"."+FirstCharToU(entity.Code)+"Service;"+vbCrLf
    file.Write   "import "+Replace(classPage,"service","mapper")+"."+FirstCharToU(entity.Code)+"Mapper;"+vbCrLf
    file.Write vbCrLf
End Function


'生成接口方法
Function AddInterfaceMethod(entity)
     AddInterfaceMethod = "" + vbCrLf
	 AddInterfaceMethod =                      "	/**" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * 新增" +entity.name + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @param " +FirstCharToL(entity.Code) + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @return int" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 */" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "    public int save("+FirstCharToU(entity.Code)+" "+FirstCharToL(entity.Code)+");" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	/**" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * 删除" +entity.name + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @param id" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @return " + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 */" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "    public int delete(String id);" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	/**" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * 修改" +entity.name + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @param " +FirstCharToL(entity.Code) + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @return " + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 */" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "    public int update("+FirstCharToU(entity.Code)+" "+FirstCharToL(entity.Code)+");" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	/**" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * 根据id查找" +entity.name + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @param id" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @return " +FirstCharToU(entity.Code) + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 */" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "    public "+FirstCharToU(entity.Code) +" searchById(String id);" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	/**" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * 查询全部" +entity.name + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @param " + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @return List<" +FirstCharToU(entity.Code) +">" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 */" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "    public List<"+FirstCharToU(entity.Code) +"> queryAll();" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	/**" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * 分页查询" +entity.name + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @param " +FirstCharToL(entity.Code) + vbCrLf
	 'AddInterfaceMethod = AddInterfaceMethod + "	 * @param sort" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @param pageNo 页号" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @param pageSize 每页条数" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 * @return PageInfo" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "	 */" + vbCrLf
	 AddInterfaceMethod = AddInterfaceMethod + "    public PageInfo<"+FirstCharToU(entity.Code)+"> queryPages("+FirstCharToU(entity.Code)+" "+FirstCharToL(entity.Code)+",int pageNo,int pageSize);" + vbCrLf	 
     AddInterfaceMethod = AddInterfaceMethod + vbCrLf

End Function


'生成实现类方法
Function AddImplMethod(entity)
     AddImplMethod = "" + vbCrLf
	 AddImplMethod = AddImplMethod + "	@Resource" + vbCrLf
	 AddImplMethod = AddImplMethod + "	private "+FirstCharToU(entity.Code)+"Mapper "+FirstCharToL(entity.Code)+"Mapper;" + vbCrLf
	 AddImplMethod = AddImplMethod + vbCrLf
	 AddImplMethod = AddImplMethod + "	 /**" +entity.name + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * 新增" +entity.name + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @param " +FirstCharToL(entity.Code) + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @return int" + vbCrLf
	 AddImplMethod = AddImplMethod + "	 */" + vbCrLf
	 AddImplMethod = AddImplMethod + "    public int save("+FirstCharToU(entity.Code)+" "+FirstCharToL(entity.Code)+"){" + vbCrLf
	 AddImplMethod = AddImplMethod + "        return "+FirstCharToL(entity.Code)+"Mapper.insert("+FirstCharToL(entity.Code)+");" + vbCrLf
	 AddImplMethod = AddImplMethod + "    }" + vbCrLf
	 AddImplMethod = AddImplMethod + vbCrLf
	 AddImplMethod = AddImplMethod + "	/**" + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * 删除" +entity.name + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @param id" + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @return " + vbCrLf
	 AddImplMethod = AddImplMethod + "	 */" + vbCrLf
	 AddImplMethod = AddImplMethod + "    public int delete(String id){" + vbCrLf
	 AddImplMethod = AddImplMethod + "        return "+FirstCharToL(entity.Code)+"Mapper.deleteByPrimaryKey(id);" + vbCrLf
	 AddImplMethod = AddImplMethod + "    }" + vbCrLf
	 AddImplMethod = AddImplMethod + vbCrLf
	 AddImplMethod = AddImplMethod + "	/**" + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * 修改" +entity.name + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @param " +FirstCharToL(entity.Code) + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @return " + vbCrLf
	 AddImplMethod = AddImplMethod + "	 */" + vbCrLf
	 AddImplMethod = AddImplMethod + "    public int update("+FirstCharToU(entity.Code)+" "+FirstCharToL(entity.Code)+"){" + vbCrLf
	 AddImplMethod = AddImplMethod + "        return "+FirstCharToL(entity.Code)+"Mapper.updateByPrimaryKey("+FirstCharToL(entity.Code)+");" + vbCrLf
	 AddImplMethod = AddImplMethod + "    }" + vbCrLf
	 AddImplMethod = AddImplMethod + vbCrLf
	 AddImplMethod = AddImplMethod + "	/**" + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * 根据id查找" +entity.name + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @param id" + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @return " +FirstCharToU(entity.Code) + vbCrLf
	 AddImplMethod = AddImplMethod + "	 */" + vbCrLf
	 AddImplMethod = AddImplMethod + "    public "+FirstCharToU(entity.Code) +" searchById(String id){" + vbCrLf
	 AddImplMethod = AddImplMethod + "        return "+FirstCharToL(entity.Code)+"Mapper.selectByPrimaryKey(id);" + vbCrLf
	 AddImplMethod = AddImplMethod + "    }" + vbCrLf
	 AddImplMethod = AddImplMethod + vbCrLf
	 AddImplMethod = AddImplMethod + "	/**" + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * 查询全部" +entity.name + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @param " + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @return List<" +FirstCharToU(entity.Code) +">" + vbCrLf
	 AddImplMethod = AddImplMethod + "	 */" + vbCrLf
	 AddImplMethod = AddImplMethod + "    public List<"+FirstCharToU(entity.Code) +"> queryAll(){" + vbCrLf
	 AddImplMethod = AddImplMethod + "        return "+FirstCharToL(entity.Code)+"Mapper.search(null);" + vbCrLf
	 AddImplMethod = AddImplMethod + "    }" + vbCrLf
	 AddImplMethod = AddImplMethod + vbCrLf
	 AddImplMethod = AddImplMethod + "	/**" + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * 分页查询" +entity.name + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @param " +FirstCharToL(entity.Code) + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @param pageNo 页号" + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @param pageSize 每页条数" + vbCrLf
	 AddImplMethod = AddImplMethod + "	 * @return PageInfo" + vbCrLf
	 AddImplMethod = AddImplMethod + "	 */" + vbCrLf
	 AddImplMethod = AddImplMethod + "    public PageInfo<"+FirstCharToU(entity.Code)+"> queryPages("+FirstCharToU(entity.Code)+" "+FirstCharToL(entity.Code)+",int pageNo,int pageSize){" + vbCrLf	 
     AddImplMethod = AddImplMethod + "        // 设置分页条件" + vbCrLf
     AddImplMethod = AddImplMethod + "        PageHelper.startPage(pageNo, pageSize);" + vbCrLf
     AddImplMethod = AddImplMethod + "        List<"+FirstCharToU(entity.Code)+"> list = "+FirstCharToL(entity.Code)+"Mapper.search("+FirstCharToL(entity.Code)+");" + vbCrLf
     AddImplMethod = AddImplMethod + "        return new PageInfo<"+FirstCharToU(entity.Code)+">(list);" + vbCrLf
     AddImplMethod = AddImplMethod + "    }" + vbCrLf
	 AddImplMethod = AddImplMethod + vbCrLf
    
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