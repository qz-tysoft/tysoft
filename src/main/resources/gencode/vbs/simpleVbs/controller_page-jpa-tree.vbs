' 从模型类生成实体bean

Option explicit
Do 
'rootPath -- 代码包根目录的存放路径
'rootPage -- 基础包路径
'tempPath -- 代码生成的临时目录
Dim rootPath,rootPage,tempPath,entityPage,servicePage,webPage
'本实体下的包名
Dim classPage,controllerPackage
Dim templateNo
Dim modelPage,treePage
modelPage = "pageModels\list.html" '模板页面路径
treePage = "pageModels\tree.html" '树模板页面路径
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
If author="" Then
	author = wshNetwork.UserName
End If

templateNo = "page"'InputBox("1.一般页面，2.树页面,3.树和列表页面","输入生成的模板编号","1")

'If (templateNo<>"1" And templateNo<>"2" And templateNo<>"3") Then
'	MsgBox("页面模板必须选择1或2，请重新执行！！")
'	Exit Do  
'End If


  
'临时目录的生成
tempPath = "_temp1"
If fso.FolderExists(tempPath) then
	fso.DeleteFolder(tempPath)
End If
fso.CreateFolder(tempPath)
Output "临时文件目录:"+tempPath


If init() Then
  '目录的创建
   entityPath = CreateEntityPageFolder(fso, rootPath, classPage)
   'CreateFolder fso, webPage
   CreateSelectEntityToJava()

End If
fso.DeleteFolder (tempPath)
Exit Do  
Loop 

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
		controllerPackage = defaultRootPackage+FirstCharToL(ActiveModel.code)+".controller"
		CreateEntityPageFolder fso,rootPath,controllerPackage
	    Output "controller包路径:" + controllerPackage
		classPage =  getClassPage(ActiveDiagram.GetPackage())
		output "classPage:" + classPage
		entityPage = defaultRootPackage+FirstCharToL(ActiveModel.code) +".entity." +  FirstCharToL(ActivePackage.Code)
		servicePage = defaultRootPackage+FirstCharToL(ActiveModel.code) +".service." + FirstCharToL(ActivePackage.Code)
		webPage = defaultWebPage+"\" + FirstCharToL(ActivePackage.Code)
		output "网页路径= " + webPage
		CreateRootPahtFolder fso, webPage
   Else
		rootPath = iniArray(0)
		Output "源代码根路径=" + rootPath
		rootPage = Mid(iniArray(1),2)
		Output "页面代码根路径=" + rootPage
		CreateRootPahtFolder fso,rootPath
		controllerPackage = Mid(iniArray(2),2)+".controller"
		CreateEntityPageFolder fso,rootPath,controllerPackage
		classPage = getClassPage(ActiveDiagram.GetPackage())
		output "classPage= " + classPage
		entityPage = Mid(iniArray(2),2) +".entity." + FirstCharToL(ActivePackage.Code)
		servicePage = Mid(iniArray(2),2) +".service." + FirstCharToL(ActivePackage.Code)
		webPage = Mid(iniArray(1),2)+"\" + FirstCharToL(ActivePackage.Code)
		output "网页路径= " + webPage
		CreateRootPahtFolder fso, webPage
   End If
   init = True
End Function


Function getClassPage(pPage)
	Dim currentPage
   getClassPage = ""
   Set currentPage = pPage
  ' Do While currentPage.ClassName <> "Conceptual Data Model"'去掉循环，只取上级的包信息
	  getClassPage = LCase(currentPage.code) + "." + getClassPage 
      
	  Set currentPage = currentPage.GetPackage()
  ' Loop
   If getClassPage <> "" Then
	 getClassPage =  controllerPackage + "." + Left(getClassPage,Len(getClassPage)-1)
   Else
     getClassPage = controllerPackage
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
	  Output "controller目录：" + CreateEntityPageFolder
    Next
    
End Function

'创建文件夹
Function CreateFolder (vfso, vpath)
   Set CreateFolder = nothing
   If vfso.FolderExists(vpath) then
     Set CreateFolder = vfso.GetFolder(vpath)
	 'Output "已存在文件夹: " + vpath
	 Exit Function
   Else
      Output "创建文件夹: " + vpath
     Set CreateFolder = vfso.CreateFolder(vpath)
	 
   End If 
End Function
'END-------------------目录的创建



'对选择的对象生成java代码
Function CreateSelectEntityToJava()
   dim entity
   For Each entity In ActiveSelection 'ActiveModel.Entities
	If entity.ClassName = "Entity" Then
		EntityToJava entity
		EntityToPage entity
    End If
  Next
End Function


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

'生成实体类代码
Function EntityToJava(pEntity)
   Dim filepath,filepathUTF8
   filepath = tempPath + "\" + FirstCharToU(pEntity.Code) + "Controller.java.gbk"
   filepathUTF8 = entityPath + "\" + FirstCharToU(pEntity.Code) + "Controller.java"
'   If BackUpFile(filepathUTF8)=false then
'	   Exit Function
'   End If
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
   '写imports
   insertImportPage file,pEntity

   file.Write "/**"	+vbCrLf
   file.Write " * " + pEntity.Name +"管理" + vbCrLf
   file.Write " */"	+vbCrLf
   file.Write "@Controller"	+vbCrLf
   file.Write "@RequestMapping(""/"+FirstCharToL(ActivePackage.Code)+"/"+GetBarStr(FirstCharToL(pEntity.Code))+""")"	+vbCrLf
   file.Write "public class " + FirstCharToU(pEntity.code) + "Controller extends BaseController {" + vbCrLf
   
   '写属性
   file.Write  getAttrib(pEntity)
  
   '写方法
   file.Write  AddMethod(pEntity)
   'file.Write  rsMethodStr

   
   file.Write "}"
   'UTF-8转换
   ConvertToUTF8 filepath,filepathUTF8
End Function


Function EntityToPage(pEntity)
    Dim listPath,listPathUTF8,treePath,treePathUTF8
	Dim listModelFile,listFile,treeModelFile,treeFile
	listPath = tempPath+"\" + GetPageName(pEntity.code) + ".html.gbk"
	treePath = tempPath+"\" + GetPageName(pEntity.code) + "-tree.html.gbk"
    listPathUTF8 = webPage + "\" + GetPageName(pEntity.Code) + ".html"
    treePathUTF8 = webPage + "\" + GetPageName(pEntity.Code) + "-tree.html"
	Output "web路径="+listPathUTF8
'	If BackUpFile(listPathUTF8)=false Then
	   'If MsgBox(listPathUTF8+" 文件已经存在,你确定要覆盖吗？",vbYesNo)=vbNo Then
'	     Exit Function
	
	   'End If
	   'Dim oldFile
	   'Set oldFile = fso.GetFile(listPathUTF8)
	   'oldFile.Move (BackUpFile() + GetPageName(pEntity.Code) + ".jsp")
'  End If

'	Set listModelFile = fso.opentextfile(modelPage)
'	On   Error   Resume   Next
	Set listModelFile = fso.opentextfile(modelPage)
	Set treeModelFile = fso.opentextfile(treePage)
	If   Err   <>   0   Then 
		'MsgBox   "An   error   occurred:   "   &   Err.Description 
		Set listModelFile = fso.opentextfile("..\"+modelPage)
		Set treeModelFile = fso.opentextfile("..\"+treePage)
	Else 
		'MsgBox   "Success! " 
	End If
    Set listFile = fso.CreateTextFile(listPath,2)
    Set treeFile = fso.CreateTextFile(treePath,2)

	Dim strList,strTree,attr
    Dim queryCodeHtml,modRow,countNum,searchNames
    countNum = 0
	modRow = countNum Mod 2
	For Each attr In pEntity.Attributes
        If Not attr.IsShortcut Then  
			If Not attr.PrimaryIdentifier  Then
			   'Output "字段类型：" + attr.DataType
			   If GetType(attr.DataType) = "java.lang.String" Then
'				If modRow =0 Then 
'				    queryCodeHtml = queryCodeHtml + "						<div class=""form-group"" style=""margin-top:1px"">"+ vbCrLf
'				End If   
					queryCodeHtml = queryCodeHtml + "							<label class=""control-label col-sm-1"" for=""query_"+FirstCharToL(attr.Code)+""">"+attr.Name + "</label>"+ vbCrLf
					queryCodeHtml = queryCodeHtml + "							<div class=""col-sm-2""> <input type=""text"" class=""form-control"" id=""query_"+FirstCharToL(attr.Code)+""" name=""query_"+FirstCharToL(attr.Code)+""" /> </div>"+ vbCrLf
'				If modRow =1 Then 
'				    queryCodeHtml = queryCodeHtml + "						</div>"+ vbCrLf
'				End If
'				countNum = countNum + 1
'				modRow = countNum Mod 2
				   If searchNames <> "" Then
					   searchNames = searchNames+"、"
				   End If
                   searchNames = searchNames+attr.Name
               End If
			End If
		End If
	Next
'	If modRow =1 Then 
'			queryCodeHtml = queryCodeHtml + "                     </div>"+ vbCrLf
'	End If

	Dim attrHtmlHidden,attrHtml,attrColumn,attrColumnVal,attrColumnEmp,entityCode
	entityCode = FirstCharToL(pEntity.Code)
    For Each attr In pEntity.Attributes
        If Not attr.IsShortcut Then  
			If Not attr.PrimaryIdentifier And attr.Code <> "parentId"  Then
			    If GetType(attr.DataType) = "java.util.Date" Then
				attrHtmlHidden = attrHtmlHidden + "		            		<input type=""hidden"" id="""+ attr.code + """ name="""+ attr.code + """ value=""""/>" + vbCrLf
				Else 				
				attrHtml = attrHtml + "							<div class=""form-group"">"+ vbCrLf	
				attrHtml = attrHtml + "								<label class=""col-sm-3 control-label"">"+ attr.name + "：</label>"+ vbCrLf	
				attrHtml = attrHtml + "								<div class=""col-sm-9"">"+ vbCrLf	
				attrHtml = attrHtml + "									<input type=""text"" class=""form-control"" name="""+FirstCharToL(attr.Code)+""" id="""+FirstCharToL(attr.Code)+""" placeholder=""请输入"+ attr.name + """/>"+ vbCrLf	
				attrHtml = attrHtml + "								</div>"+ vbCrLf	
				attrHtml = attrHtml + "							</div>"+ vbCrLf	
				End If
				attrColumn = attrColumn + ",{"+ vbCrLf
				attrColumn = attrColumn + "				    field: '"+FirstCharToL(attr.Code)+"',"+ vbCrLf
				attrColumn = attrColumn + "				    title:'"+ attr.name + "',"+ vbCrLf
				attrColumn = attrColumn + "				    sortable:true"+ vbCrLf
				attrColumn = attrColumn + "				}"
			End If
				attrColumnVal = attrColumnVal + "					$('#inputForm').find(""input[name='"+FirstCharToL(attr.Code)+"']"").val("+entityCode+"."+FirstCharToL(attr.Code)+");"+ vbCrLf
				attrColumnEmp = attrColumnEmp + "       $('#inputForm').find(""input[name='"+FirstCharToL(attr.Code)+"']"").val('');"+ vbCrLf
		End If
	Next
	
	strList = listModelFile.readall()
	strList=Replace(strList,"%pageCopyrightTop%",getPageCopyrightTop(pEntity.Name))
	strList=Replace(strList,"%packageCode%",GetBarStr(FirstCharToL(ActivePackage.Code)))
	strList=Replace(strList,"%controlllerCode%",GetBarStr(FirstCharToL(pEntity.Code)))
	strList=Replace(strList,"%entityCode%",FirstCharToL(pEntity.Code))
	strList=Replace(strList,"%entityName%",pEntity.Name)
	strList=Replace(strList,"%queryCode%",queryCodeHtml)
	strList=Replace(strList,"%searchNames%",searchNames)
    strList=Replace(strList,"%attrHtmlHidden%",attrHtmlHidden)
    strList=Replace(strList,"%attrHtml%",attrHtml)
    strList=Replace(strList,"%attrColumn%",attrColumn)
    strList=Replace(strList,"%attrColumnVal%",attrColumnVal)
    strList=Replace(strList,"%attrColumnEmp%",attrColumnEmp)
	
	strTree = treeModelFile.readall()
	strTree=Replace(strTree,"%pageCopyrightTop%",getPageCopyrightTop(pEntity.Name))
	strTree=Replace(strTree,"%packageCode%",GetBarStr(FirstCharToL(ActivePackage.Code)))
	strTree=Replace(strTree,"%controlllerCode%",GetBarStr(FirstCharToL(pEntity.Code)))
	strTree=Replace(strTree,"%entityCode%",FirstCharToL(pEntity.Code))
	strTree=Replace(strTree,"%entityName%",pEntity.Name)
	strTree=Replace(strTree,"%queryCode%",queryCodeHtml)
	strTree=Replace(strTree,"%searchNames%",searchNames)
    strTree=Replace(strTree,"%attrHtmlHidden%",attrHtmlHidden)
    strTree=Replace(strTree,"%attrHtml%",attrHtml)
    strTree=Replace(strTree,"%attrColumn%",attrColumn)
    strTree=Replace(strTree,"%attrColumnVal%",attrColumnVal)
    strTree=Replace(strTree,"%attrColumnEmp%",attrColumnEmp)

	listFile.write  strList
	treeFile.write  strTree
    ConvertToUTF8 listPath,listPathUTF8
    ConvertToUTF8 treePath,treePathUTF8
End Function

'写文件头注释
Function WriteJavaTop(file)
	file.Write "/**" + vbCrLf
 	file.Write "* <p>Description: "+ActiveModel.name+" "+ ActiveModel.code +" Controller</p>" + vbCrLf
 	file.Write "* <p>Copyright: Copyright (c) "+ CStr(DatePart("yyyy",Date)) +"</p>" + vbCrLf
 	file.Write "* <p>Company: 厦门路桥信息股份有限公司</p>" + vbCrLf
 	file.Write "*" + vbCrLf
 	file.Write "* @author :" + author + vbCrLf
 	file.Write "* @version 1.0" + vbCrLf
 	file.Write "*/" + vbCrLf
End Function

'写html,jsp文件头版本注释
Function getPageCopyrightTop(eName)
 	getPageCopyrightTop  =  "* @Copyright(c): 厦门路桥信息股份有限公司  版权所有" + vbCrLf
 	getPageCopyrightTop  = getPageCopyrightTop + "* @author :" + author + vbCrLf
 	getPageCopyrightTop  = getPageCopyrightTop + "* @Description:" + eName+ "管理 " + vbCrLf
 	getPageCopyrightTop  = getPageCopyrightTop + "* @createDate:" +CStr(DatePart("yyyy",Date))+"-"+CStr(DatePart("m",Date))+"-"+CStr(DatePart("d",Date))
End Function

'写实体注释
Function getAttrib(pEntity)
   getAttrib = getAttrib +"	@Autowired"  + vbCrLf
   getAttrib = getAttrib +"	private "+FirstCharToU(pEntity.Code)+"Service " + FirstCharToL(pEntity.Code)+"Service;"  + vbCrLf+ vbCrLf+ vbCrLf

End Function


Function insertImportPage(file,pEntity)
'	file.Write  vbCrLf
	file.Write   "import java.util.HashMap;"+vbCrLf
	file.Write   "import java.util.List;"+vbCrLf
	file.Write   "import java.util.ArrayList;"+vbCrLf
	file.Write   "import java.util.Date;"+vbCrLf
	file.Write   "import java.util.Map;"+vbCrLf
	file.Write   "import javax.servlet.http.HttpServletRequest;"+vbCrLf
	file.Write  vbCrLf
	file.Write   "import org.apache.commons.lang.StringUtils;"+vbCrLf
	file.Write   "import org.springframework.beans.factory.annotation.Autowired;"+vbCrLf
	file.Write   "import org.springframework.data.domain.PageRequest;"+vbCrLf
	file.Write   "import org.springframework.data.domain.Pageable;"+vbCrLf
	file.Write   "import org.springframework.data.domain.Sort;"+vbCrLf
	file.Write   "import org.springframework.data.domain.Sort.Direction;"+vbCrLf
'	file.Write   "import org.springframework.data.domain.Sort.Order;"+vbCrLf
	file.Write   "import org.springframework.stereotype.Controller;"+vbCrLf
	file.Write   "import org.springframework.ui.Model;"+vbCrLf
	file.Write   "import org.springframework.web.bind.annotation.RequestMapping;"+vbCrLf
	file.Write   "import org.springframework.web.bind.annotation.RequestParam;"+vbCrLf
	file.Write   "import org.springframework.web.bind.annotation.ResponseBody;"+vbCrLf
'	file.Write   "import net.sf.json.JSON;"+vbCrLf
	file.Write   "import net.sf.json.JSONObject;"+vbCrLf
	file.Write   "import com.xmrbi.iams.controller.BaseController;"+vbCrLf
    file.Write   "import "+entityPage + "."+FirstCharToU(pEntity.Code)+";"+vbCrLf
	file.Write   "import "+servicePage + "."+FirstCharToU(pEntity.Code)+"Service;"+vbCrLf
    file.Write vbCrLf+ vbCrLf
    
End Function



'生成方法
Function AddMethod(pEntity)
	 Dim attr
     AddMethod =              "   /**" + vbCrLf
	 AddMethod = AddMethod  + "    * 进入展示列表" + vbCrLf
	 AddMethod = AddMethod  + "    */" + vbCrLf
	 AddMethod = AddMethod  + "   @RequestMapping(""list"")" + vbCrLf
	 AddMethod = AddMethod  + "   public String list(HttpServletRequest request,Model model) {"+ vbCrLf
	 AddMethod = AddMethod  + "       return """+GetBarStr(FirstCharToL(ActivePackage.Code))+"/"+GetBarStr(FirstCharToL(pEntity.Code))+""";"+ vbCrLf
	 AddMethod = AddMethod  + "   }"+ vbCrLf
	 AddMethod = AddMethod  + "   "+ vbCrLf

     AddMethod = AddMethod  + "   /**" + vbCrLf
	 AddMethod = AddMethod  + "    * 进入展示树" + vbCrLf
	 AddMethod = AddMethod  + "    */" + vbCrLf
	 AddMethod = AddMethod  + "   @RequestMapping(""tree"")" + vbCrLf
	 AddMethod = AddMethod  + "   public String tree(HttpServletRequest request,Model model) {"+ vbCrLf
	 AddMethod = AddMethod  + "       return """+GetBarStr(FirstCharToL(ActivePackage.Code))+"/"+GetBarStr(FirstCharToL(pEntity.Code))+"-tree"";"+ vbCrLf
	 AddMethod = AddMethod  + "   }"+ vbCrLf
	 AddMethod = AddMethod  + "   "+ vbCrLf

	 AddMethod = AddMethod  + "   /**" + vbCrLf
	 AddMethod = AddMethod  + "    * 查询列表" + vbCrLf
	 AddMethod = AddMethod  + "    */" + vbCrLf
'	 AddMethod = AddMethod  + "   @RequestMapping(""query-"+GetBarStr(FirstCharToL(pEntity.Code))+"-page"")" + vbCrLf
	 AddMethod = AddMethod  + "   @RequestMapping(""query-page"")" + vbCrLf
	 AddMethod = AddMethod  + "   @ResponseBody"+ vbCrLf
	 AddMethod = AddMethod  + "   public Map<String,Object> queryPage(HttpServletRequest request) {"+ vbCrLf
	 AddMethod = AddMethod  + "       String pageNum = request.getParameter(""pageNum"");"+ vbCrLf
	 AddMethod = AddMethod  + "       String size = request.getParameter(""size"");"+ vbCrLf
	 AddMethod = AddMethod  + "       String searchForm = request.getParameter(""searchForm"");"+ vbCrLf
	 AddMethod = AddMethod  + "       String searchText = request.getParameter(""searchText"");"+ vbCrLf
	 AddMethod = AddMethod  + "       String ordername = request.getParameter(""ordername"");"+ vbCrLf
	 AddMethod = AddMethod  + "       String order = request.getParameter(""order"");"+ vbCrLf

	 AddMethod = AddMethod  + "       "+ vbCrLf
	 AddMethod = AddMethod  + "       Sort sort = new Sort(Direction.DESC, ""id"");"+ vbCrLf
	 AddMethod = AddMethod  + "       if(this.isNotBlank(ordername)&&this.isNotBlank(order)) {"+ vbCrLf
	 AddMethod = AddMethod  + "           sort = new Sort(order.equals(""asc"")?Direction.ASC:Direction.DESC, ordername);"+ vbCrLf
	 AddMethod = AddMethod  + "       }"+ vbCrLf
	 AddMethod = AddMethod  + "       Pageable pageable = new PageRequest(new Integer(pageNum)-1, new Integer(size), sort);"+ vbCrLf
	 AddMethod = AddMethod  + "       "+ vbCrLf

	 AddMethod = AddMethod  + "       //通过Map方式查询"+ vbCrLf
	 AddMethod = AddMethod  + "       Map<String,String> queryMap = new HashMap<String,String>();"+ vbCrLf
	 AddMethod = AddMethod  + "       if(StringUtils.isNotBlank(searchForm)) {"+ vbCrLf '//通过页面""query_""开头命名的属性可自动过滤查询
	 AddMethod = AddMethod  + "           queryMap = (Map<String,String>)JSONObject.fromObject(searchForm);//将获取到的界面传进来的json字符串变成map"+ vbCrLf
	 AddMethod = AddMethod  + "       }"+ vbCrLf
	 AddMethod = AddMethod  + "//       Map<String,Object> resultMap = this."+FirstCharToL(pEntity.Code)+"Service.queryPagesByMap(queryMap,searchText,pageable);"+ vbCrLf
	 AddMethod = AddMethod  + "       "+ vbCrLf

	 AddMethod = AddMethod  + "       Map<String,Object> resultMap = this."+FirstCharToL(pEntity.Code)+"Service.queryPages(searchText,pageable);"+ vbCrLf
	 AddMethod = AddMethod  + "       return resultMap;"+ vbCrLf
	 AddMethod = AddMethod  + "   }"+ vbCrLf
	 AddMethod = AddMethod  + "   "+ vbCrLf


     AddMethod = AddMethod  + "   /**" + vbCrLf
	 AddMethod = AddMethod  + "    * 保存"+pEntity.Name + vbCrLf
	 AddMethod = AddMethod  + "    */" + vbCrLf
	 AddMethod = AddMethod  + "   @RequestMapping(""save"")" + vbCrLf
	 AddMethod = AddMethod  + "   @ResponseBody" + vbCrLf
	 AddMethod = AddMethod  + "   public String save("+FirstCharToU(pEntity.Code)+" "+FirstCharToL(pEntity.Code)+", HttpServletRequest request) {" + vbCrLf
     AddMethod = AddMethod  + "		  String id = request.getParameter(""id"");" + vbCrLf
     AddMethod = AddMethod  + "		  if(StringUtils.isBlank(id)) {" + vbCrLf
	 For Each attr In pEntity.Attributes
	     If Not attr.IsShortcut Then
		    If attr.code = "createTime" Then
	 AddMethod = AddMethod  + "		      "+FirstCharToL(pEntity.Code)+".setCreateTime(new Date());" + vbCrLf
		    End If
		    If attr.code = "isValid" Then
	 AddMethod = AddMethod  + "		      "+FirstCharToL(pEntity.Code)+".setIsValid(true);" + vbCrLf
		    End If
		    If attr.code = "isDeleted" Then
	 AddMethod = AddMethod  + "		      "+FirstCharToL(pEntity.Code)+".setIsDeleted(false);" + vbCrLf
		    End If
         End If
     Next
	 AddMethod = AddMethod  + "			//获取最大顺序号" + vbCrLf
	 AddMethod = AddMethod  + "			//Integer maxSort = this."+FirstCharToL(pEntity.Code)+"Service.findMaxSortNum("+FirstCharToL(pEntity.Code)+".getParent().getId());" + vbCrLf
	 AddMethod = AddMethod  + "			//if (maxSort == null) {" + vbCrLf
	 AddMethod = AddMethod  + "				"+FirstCharToL(pEntity.Code)+".setSortnum(1);" + vbCrLf
	 AddMethod = AddMethod  + "			//} else {" + vbCrLf
	 AddMethod = AddMethod  + "			//	"+FirstCharToL(pEntity.Code)+".setSortnum(maxSort + 1);" + vbCrLf
	 AddMethod = AddMethod  + "			//}" + vbCrLf
     AddMethod = AddMethod  + "		  }" + vbCrLf
     AddMethod = AddMethod  + "	 	  if (StringUtils.isBlank("+FirstCharToL(pEntity.Code)+".getParent().getId())) {" + vbCrLf
     AddMethod = AddMethod  + "			  "+FirstCharToL(pEntity.Code)+".setParent(null);" + vbCrLf
     AddMethod = AddMethod  + "		  }" + vbCrLf
	 AddMethod = AddMethod  + "		  "+FirstCharToU(pEntity.Code)+" new"+FirstCharToU(pEntity.Code)+" = "+FirstCharToL(pEntity.Code)+"Service.save"+FirstCharToU(pEntity.Code)+"("+FirstCharToL(pEntity.Code)+");" + vbCrLf
	 AddMethod = AddMethod  + "		  if (" + FirstCharToL(pEntity.Code)+".getParent() == null) {" + vbCrLf
	 AddMethod = AddMethod  + "		  		new"+FirstCharToU(pEntity.Code)+".setTreeCode(new"+FirstCharToU(pEntity.Code)+".getId());" + vbCrLf
	 AddMethod = AddMethod  + "		  		this."+FirstCharToL(pEntity.Code)+"Service.save"+FirstCharToU(pEntity.Code)+"(new"+FirstCharToU(pEntity.Code)+");" + vbCrLf
	 AddMethod = AddMethod  + "		  } else {" + vbCrLf
	 AddMethod = AddMethod  + "		  		"+FirstCharToU(pEntity.Code)+" parent = this."+FirstCharToL(pEntity.Code)+"Service.find"+FirstCharToU(pEntity.Code)+"ById("+FirstCharToL(pEntity.Code)+".getParent().getId());" + vbCrLf
	 AddMethod = AddMethod  + "		  		new"+FirstCharToU(pEntity.Code)+".setParent(parent);" + vbCrLf
	 AddMethod = AddMethod  + "		  		new"+FirstCharToU(pEntity.Code)+".setTreeCode(parent.getTreeCode() + "","" + new"+FirstCharToU(pEntity.Code)+".getId());" + vbCrLf
	 AddMethod = AddMethod  + "		  		this."+FirstCharToL(pEntity.Code)+"Service.save"+FirstCharToU(pEntity.Code)+"(new"+FirstCharToU(pEntity.Code)+");" + vbCrLf
	 AddMethod = AddMethod  + "		  }" + vbCrLf
	 AddMethod = AddMethod  + "		  request.setAttribute("""+FirstCharToL(pEntity.Code)+""", new"+FirstCharToU(pEntity.Code)+");" + vbCrLf
	 AddMethod = AddMethod  + "		  request.setAttribute(""id"", id);" + vbCrLf
	 AddMethod = AddMethod  + "		  if(new"+FirstCharToU(pEntity.Code)+" != null)" + vbCrLf
	 AddMethod = AddMethod  + "		       return ""{\""success\"":true,\""msg\"":\""保存成功!\""}"";" + vbCrLf
	 AddMethod = AddMethod  + "		  else" + vbCrLf
	 AddMethod = AddMethod  + "		       return ""{\""success\"":false,\""msg\"":\""保存失败!\""}"";" + vbCrLf
	 AddMethod = AddMethod  + "    }" + vbCrLf
	 AddMethod = AddMethod  + "     " + vbCrLf


     AddMethod = AddMethod  + "   /**" + vbCrLf
	 AddMethod = AddMethod  + "    * 获取"+pEntity.Name + vbCrLf
	 AddMethod = AddMethod  + "    */" + vbCrLf
	 AddMethod = AddMethod  + "   @RequestMapping(""find"")" + vbCrLf
	 AddMethod = AddMethod  + "   @ResponseBody" + vbCrLf
	 AddMethod = AddMethod  + "   public Map<String, Object> find(HttpServletRequest request) {" + vbCrLf
	 AddMethod = AddMethod  + "       String id = request.getParameter(""id"");" + vbCrLf
	 AddMethod = AddMethod  + "       Map<String, Object> resultMap = new HashMap<String,Object>();" + vbCrLf
	 AddMethod = AddMethod  + "       if(StringUtils.isNotBlank(id)){" + vbCrLf
	 AddMethod = AddMethod  + "           "+FirstCharToU(pEntity.Code)+" "+FirstCharToL(pEntity.Code)+" = this."+FirstCharToL(pEntity.Code)+"Service.find"+FirstCharToU(pEntity.Code)+"ById(id);" + vbCrLf
	 AddMethod = AddMethod  + "           resultMap.put("""+FirstCharToL(pEntity.Code)+""","+FirstCharToL(pEntity.Code)+".poToMap());"+ vbCrLf
	 AddMethod = AddMethod  + "       }"+ vbCrLf
	 AddMethod = AddMethod  + "       return resultMap;" + vbCrLf
	 AddMethod = AddMethod  + "   }" + vbCrLf
	 AddMethod = AddMethod  + "   " + vbCrLf


	 AddMethod = AddMethod  + "   /**" + vbCrLf
	 AddMethod = AddMethod  + "     * 删除" +pEntity.Name + vbCrLf
	 AddMethod = AddMethod  + "    */" + vbCrLf
	 AddMethod = AddMethod  + "   @RequestMapping(""delete"")" + vbCrLf
	 AddMethod = AddMethod  + "   @ResponseBody" + vbCrLf
	 AddMethod = AddMethod  + "   public String delete(HttpServletRequest request) {" + vbCrLf
	 AddMethod = AddMethod  + "	    String ids = request.getParameter(""ids"");" + vbCrLf
	 AddMethod = AddMethod  + "	    try{" + vbCrLf
	 AddMethod = AddMethod  + "	        this."+FirstCharToL(pEntity.Code)+ "Service.delete"+FirstCharToU(pEntity.Code)+ "ByIds(ids);" + vbCrLf
	 AddMethod = AddMethod  + "	    }catch(Exception e){" + vbCrLf
	 AddMethod = AddMethod  + "		    return ""{\""success\"":false,\""msg\"":\""删除失败!\""}"";" + vbCrLf
	 AddMethod = AddMethod  + "	    }" + vbCrLf
	 AddMethod = AddMethod  + "	    return ""{\""success\"":true,\""msg\"":\""删除成功!\""}"";" + vbCrLf
	 AddMethod = AddMethod  + "	}" + vbCrLf
	 
	 AddMethod = AddMethod  + "   /**" + vbCrLf
	 AddMethod = AddMethod  + "     * 查询树形列表" + vbCrLf
	 AddMethod = AddMethod  + "    */" + vbCrLf
	 AddMethod = AddMethod  + "   @RequestMapping(""query"")" + vbCrLf
	 AddMethod = AddMethod  + "   @ResponseBody" + vbCrLf
	 AddMethod = AddMethod  + "   public List<Map<String, Object>> query(HttpServletRequest request," + vbCrLf
	 AddMethod = AddMethod  + "          @RequestParam(name = ""selectable"", defaultValue = ""false"") Boolean selectable) {" + vbCrLf
	 AddMethod = AddMethod  + "       String ids = request.getParameter(""ids"");" + vbCrLf
	 AddMethod = AddMethod  + "       String parentId = request.getParameter(""parentId"");" + vbCrLf
	 AddMethod = AddMethod  + "       List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();" + vbCrLf
	 AddMethod = AddMethod  + "       Map<String, String> queryMap = new HashMap<String, String>();" + vbCrLf
	 AddMethod = AddMethod  + "       queryMap.put(""no_parent"", ""true"");" + vbCrLf
	 AddMethod = AddMethod  + "       queryMap.put(""parentId"", parentId);" + vbCrLf
	 AddMethod = AddMethod  + "       Sort sort = new Sort(Direction.ASC, ""sortnum"");" + vbCrLf
	 AddMethod = AddMethod  + "       List<"+FirstCharToU(pEntity.Code)+ "> "+FirstCharToL(pEntity.Code)+ "s = this."+FirstCharToL(pEntity.Code)+ "Service.query"+FirstCharToU(pEntity.Code)+ "List(queryMap, null, sort);" + vbCrLf
	 AddMethod = AddMethod  + "       if ("+FirstCharToL(pEntity.Code)+ "s != null && "+FirstCharToL(pEntity.Code)+ "s.size() > 0) {" + vbCrLf
	 AddMethod = AddMethod  + "       	Map<String, Object> map = null;" + vbCrLf
	 AddMethod = AddMethod  + "       	Map<String, Object> selectMap = null;" + vbCrLf
	 AddMethod = AddMethod  + "       	for ("+FirstCharToU(pEntity.Code)+ " obj : "+FirstCharToL(pEntity.Code)+ "s) {" + vbCrLf
	 AddMethod = AddMethod  + "       		map = new HashMap<String, Object>();" + vbCrLf
	 AddMethod = AddMethod  + "       		map.put(""id"", obj.getId());" + vbCrLf
	 AddMethod = AddMethod  + "       		map.put(""text"", obj.getName());" + vbCrLf
	 AddMethod = AddMethod  + "       		map.put(""treeCode"", obj.getTreeCode());" + vbCrLf
	 AddMethod = AddMethod  + "       		map.put(""selectable"", selectable);" + vbCrLf
	 AddMethod = AddMethod  + "       		if(StringUtils.isNotBlank(ids)) {" + vbCrLf
	 AddMethod = AddMethod  + "       		   for (String id : ids.split("","")) {" + vbCrLf
	 AddMethod = AddMethod  + "       			   if(obj.getId().equals(id)) {" + vbCrLf
	 AddMethod = AddMethod  + "       				   selectMap = new HashMap<String,Object>();" + vbCrLf
	 AddMethod = AddMethod  + "       				   selectMap.put(""checked"", true);" + vbCrLf
	 AddMethod = AddMethod  + "       				   map.put(""state"", selectMap);" + vbCrLf
	 AddMethod = AddMethod  + "       			   }" + vbCrLf
	 AddMethod = AddMethod  + "       		    }" + vbCrLf
	 AddMethod = AddMethod  + "       	    }" + vbCrLf
	 AddMethod = AddMethod  + "       		List<Map<String, Object>> nodes = this.getChilds(obj.getId(),ids,selectable);" + vbCrLf
	 AddMethod = AddMethod  + "       		if (nodes != null && nodes.size() > 0) {" + vbCrLf
	 AddMethod = AddMethod  + "       			map.put(""nodes"", nodes);" + vbCrLf
	 AddMethod = AddMethod  + "       		}" + vbCrLf
	 AddMethod = AddMethod  + "       		listMap.add(map);" + vbCrLf
	 AddMethod = AddMethod  + "       	}" + vbCrLf
	 AddMethod = AddMethod  + "       }" + vbCrLf
	 AddMethod = AddMethod  + "       return listMap;" + vbCrLf
	 AddMethod = AddMethod  + "   }" + vbCrLf

     AddMethod = AddMethod  + "   /**" + vbCrLf
	 AddMethod = AddMethod  + "     * 递归查询树形成员" + vbCrLf
	 AddMethod = AddMethod  + "    */" + vbCrLf
	 AddMethod = AddMethod  + "   protected List<Map<String, Object>> getChilds(String parentId,String ids,boolean selectable) {" + vbCrLf
	 AddMethod = AddMethod  + "   		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();" + vbCrLf
	 AddMethod = AddMethod  + "   		Map<String, String> queryMap = new HashMap<String, String>();" + vbCrLf
	 AddMethod = AddMethod  + "   		queryMap.put(""parentId"", parentId);" + vbCrLf
	 AddMethod = AddMethod  + "   		Sort sort = new Sort(Direction.ASC, ""sortnum"");" + vbCrLf
	 AddMethod = AddMethod  + "   		List<"+FirstCharToU(pEntity.Code)+ "> "+FirstCharToL(pEntity.Code)+ "s = this."+FirstCharToL(pEntity.Code)+ "Service.query"+FirstCharToU(pEntity.Code)+ "List(queryMap, null, sort);" + vbCrLf
	 AddMethod = AddMethod  + "   		if ("+FirstCharToL(pEntity.Code)+ "s != null && "+FirstCharToL(pEntity.Code)+ "s.size() > 0) {" + vbCrLf
	 AddMethod = AddMethod  + "   			Map<String, Object> map = null;" + vbCrLf
	 AddMethod = AddMethod  + "   			Map<String, Object> selectMap = null;" + vbCrLf
	 AddMethod = AddMethod  + "   			for ("+FirstCharToU(pEntity.Code)+ " obj : "+FirstCharToL(pEntity.Code)+ "s) {" + vbCrLf
	 AddMethod = AddMethod  + "   				map = new HashMap<String, Object>();" + vbCrLf
	 AddMethod = AddMethod  + "   				map.put(""id"", obj.getId());" + vbCrLf
	 AddMethod = AddMethod  + "   				map.put(""text"", obj.getName());" + vbCrLf
	 AddMethod = AddMethod  + "   				map.put(""treeCode"", obj.getTreeCode());" + vbCrLf
	 AddMethod = AddMethod  + "   				map.put(""selectable"", selectable);" + vbCrLf
	 AddMethod = AddMethod  + "   				if(StringUtils.isNotBlank(ids)) {" + vbCrLf
	 AddMethod = AddMethod  + "   				   for (String id : ids.split("","")) {" + vbCrLf
	 AddMethod = AddMethod  + "   					   if(obj.getId().equals(id)) {" + vbCrLf
	 AddMethod = AddMethod  + "   						   selectMap = new HashMap<String,Object>();" + vbCrLf
	 AddMethod = AddMethod  + "   						   selectMap.put(""checked"", true);" + vbCrLf
	 AddMethod = AddMethod  + "   						   map.put(""state"", selectMap);" + vbCrLf
	 AddMethod = AddMethod  + "   					   }" + vbCrLf
	 AddMethod = AddMethod  + "   				    }" + vbCrLf
	 AddMethod = AddMethod  + "   			    }" + vbCrLf
	 AddMethod = AddMethod  + "   				List<Map<String, Object>> nodes = this.getChilds(obj.getId(),ids,selectable);" + vbCrLf
	 AddMethod = AddMethod  + "   				if (nodes != null && nodes.size() > 0) {" + vbCrLf
	 AddMethod = AddMethod  + "   					map.put(""nodes"", nodes);" + vbCrLf
	 AddMethod = AddMethod  + "   				}" + vbCrLf
	 AddMethod = AddMethod  + "   				listMap.add(map);" + vbCrLf
	 AddMethod = AddMethod  + "   			}" + vbCrLf
	 AddMethod = AddMethod  + "   		}" + vbCrLf
	 AddMethod = AddMethod  + "   		return listMap;" + vbCrLf
	 AddMethod = AddMethod  + "   	}" + vbCrLf
   
	 AddMethod = AddMethod  + "   /**" + vbCrLf
	 AddMethod = AddMethod  + "    * 保存功能菜单排序" + vbCrLf
	 AddMethod = AddMethod  + "    * @param request" + vbCrLf
	 AddMethod = AddMethod  + "    * @return" + vbCrLf
	 AddMethod = AddMethod  + "    */" + vbCrLf
	 AddMethod = AddMethod  + "   @RequestMapping(""save-sort"")" + vbCrLf
	 AddMethod = AddMethod  + "   @ResponseBody" + vbCrLf
	 AddMethod = AddMethod  + "   public String saveSort(HttpServletRequest request){" + vbCrLf
	 AddMethod = AddMethod  + "	   String id = request.getParameter(""id"");" + vbCrLf
	 AddMethod = AddMethod  + "	   String operId = request.getParameter(""operId"");" + vbCrLf
	 AddMethod = AddMethod  + "	   try {" + vbCrLf
	 AddMethod = AddMethod  + "		   if(!StringUtils.isEmpty(id)&&!StringUtils.isEmpty(operId)) {" + vbCrLf
	 AddMethod = AddMethod  + "			   "+FirstCharToU(pEntity.Code)+" cur"+FirstCharToU(pEntity.Code)+" = this."+FirstCharToL(pEntity.Code)+"Service.find"+FirstCharToU(pEntity.Code)+"ById(id);" + vbCrLf
	 AddMethod = AddMethod  + "			   Map<String,String> queryMap = new HashMap<String,String>();" + vbCrLf
	 AddMethod = AddMethod  + "			   if(cur"+FirstCharToU(pEntity.Code)+".getParent()==null)" + vbCrLf
	 AddMethod = AddMethod  + "				   queryMap.put(""no_parent"",""true"");" + vbCrLf
	 AddMethod = AddMethod  + "			   else" + vbCrLf
	 AddMethod = AddMethod  + "				   queryMap.put(""parentId"",cur"+FirstCharToU(pEntity.Code)+".getParent().getId());" + vbCrLf
	 AddMethod = AddMethod  + "			   Sort sort = new Sort(Direction.ASC, ""sortnum"");" + vbCrLf
	 AddMethod = AddMethod  + "			   List<"+FirstCharToU(pEntity.Code)+"> "+FirstCharToL(pEntity.Code)+"s = this."+FirstCharToL(pEntity.Code)+"Service.query"+FirstCharToU(pEntity.Code)+"List(queryMap,null,sort);" + vbCrLf
	 AddMethod = AddMethod  + "			   if("+FirstCharToL(pEntity.Code)+"s!=null&&"+FirstCharToL(pEntity.Code)+"s.size()>0) {" + vbCrLf
	 AddMethod = AddMethod  + "				   boolean isMax = false;" + vbCrLf
	 AddMethod = AddMethod  + "				   if("+FirstCharToL(pEntity.Code)+"s.get("+FirstCharToL(pEntity.Code)+"s.size()-1).getId().equals(cur"+FirstCharToU(pEntity.Code)+".getId())) {" + vbCrLf
	 AddMethod = AddMethod  + "					   isMax = true;" + vbCrLf
	 AddMethod = AddMethod  + "				   }" + vbCrLf
	 AddMethod = AddMethod  + "				   boolean isMin = false;" + vbCrLf
	 AddMethod = AddMethod  + "				   if("+FirstCharToL(pEntity.Code)+"s.get(0).getId().equals(cur"+FirstCharToU(pEntity.Code)+".getId())) {" + vbCrLf
	 AddMethod = AddMethod  + "					   isMin = true;" + vbCrLf
	 AddMethod = AddMethod  + "				   }" + vbCrLf
	 AddMethod = AddMethod  + "				   if(""upMenu"".equals(operId)&&!isMin) {" + vbCrLf
	 AddMethod = AddMethod  + "					   cur"+FirstCharToU(pEntity.Code)+".setSortnum(cur"+FirstCharToU(pEntity.Code)+".getSortnum()-1);" + vbCrLf
	 AddMethod = AddMethod  + "					   for("+FirstCharToU(pEntity.Code)+" "+FirstCharToL(pEntity.Code)+" : "+FirstCharToL(pEntity.Code)+"s) {" + vbCrLf
	 AddMethod = AddMethod  + "						   if("+FirstCharToL(pEntity.Code)+".getId()!=cur"+FirstCharToU(pEntity.Code)+".getId()&&"+FirstCharToL(pEntity.Code)+".getSortnum()==cur"+FirstCharToU(pEntity.Code)+".getSortnum()) {" + vbCrLf
	 AddMethod = AddMethod  + "							   "+FirstCharToL(pEntity.Code)+".setSortnum("+FirstCharToL(pEntity.Code)+".getSortnum()+1);" + vbCrLf
	 AddMethod = AddMethod  + "							   this."+FirstCharToL(pEntity.Code)+"Service.save"+FirstCharToU(pEntity.Code)+"("+FirstCharToL(pEntity.Code)+");" + vbCrLf
	 AddMethod = AddMethod  + "							   this."+FirstCharToL(pEntity.Code)+"Service.save"+FirstCharToU(pEntity.Code)+"(cur"+FirstCharToU(pEntity.Code)+");" + vbCrLf
	 AddMethod = AddMethod  + "							   break;" + vbCrLf
	 AddMethod = AddMethod  + "						   }" + vbCrLf
	 AddMethod = AddMethod  + "					   }" + vbCrLf
	 AddMethod = AddMethod  + "				   }" + vbCrLf
	 AddMethod = AddMethod  + "				   if(""downMenu"".equals(operId)&&!isMax) {" + vbCrLf
	 AddMethod = AddMethod  + "					   cur"+FirstCharToU(pEntity.Code)+".setSortnum(cur"+FirstCharToU(pEntity.Code)+".getSortnum()+1);" + vbCrLf
	 AddMethod = AddMethod  + "					   for("+FirstCharToU(pEntity.Code)+" "+FirstCharToL(pEntity.Code)+" : "+FirstCharToL(pEntity.Code)+"s) {" + vbCrLf
	 AddMethod = AddMethod  + "						   if("+FirstCharToL(pEntity.Code)+".getId()!=cur"+FirstCharToU(pEntity.Code)+".getId()&&"+FirstCharToL(pEntity.Code)+".getSortnum()==cur"+FirstCharToU(pEntity.Code)+".getSortnum()) {" + vbCrLf
	 AddMethod = AddMethod  + "							   "+FirstCharToL(pEntity.Code)+".setSortnum("+FirstCharToL(pEntity.Code)+".getSortnum()-1);" + vbCrLf
	 AddMethod = AddMethod  + "							   this."+FirstCharToL(pEntity.Code)+"Service.save"+FirstCharToU(pEntity.Code)+"("+FirstCharToL(pEntity.Code)+");" + vbCrLf
	 AddMethod = AddMethod  + "							   this."+FirstCharToL(pEntity.Code)+"Service.save"+FirstCharToU(pEntity.Code)+"(cur"+FirstCharToU(pEntity.Code)+");" + vbCrLf
	 AddMethod = AddMethod  + "							   break;" + vbCrLf
	 AddMethod = AddMethod  + "						   }" + vbCrLf
	 AddMethod = AddMethod  + "					   }" + vbCrLf
	 AddMethod = AddMethod  + "				   }" + vbCrLf
	 AddMethod = AddMethod  + "			   }" + vbCrLf
	 AddMethod = AddMethod  + "		   }" + vbCrLf
	 AddMethod = AddMethod  + "		   return ""success"";" + vbCrLf
	 AddMethod = AddMethod  + "	   } catch (Exception e) {" + vbCrLf
	 AddMethod = AddMethod  + "			e.printStackTrace();" + vbCrLf
	 AddMethod = AddMethod  + "			return ""error"";" + vbCrLf
	 AddMethod = AddMethod  + "	   }" + vbCrLf
	 AddMethod = AddMethod  + "   }" + vbCrLf

End Function


Function GetPageName(vstr)
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
			If  Mid(vstr, k-1,1)<>"-"   Then
				newStr = newStr + "-" 
				
			End If
	   End If
	   newStr = newStr + LCase(vLeftChar)
    Next
	'Output "bigLen=" + CStr(bigLen)
	If bigLen = strLen Then
		GetPageName = vstr
	Else
		GetPageName = newStr
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

'------------------------------------------------------------------------------------

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

'获取驼峰命名的变为横杠命名的字符串
Function GetBarStr(vstr)
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
			If  Mid(vstr, k-1,1)<>"-"   Then
				newStr = newStr + "-" 
				
			End If
	   End If
	   newStr = newStr + LCase(vLeftChar)
    Next
	'Output "bigLen=" + CStr(bigLen)
	If bigLen = strLen Then
		GetBarStr = vstr
	Else
		GetBarStr = newStr
	End If
	
End Function



'-----------------------------------------------------------------------------------------
