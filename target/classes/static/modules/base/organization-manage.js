var currentOrgNode = null;
var currentRoleNode = null;

$(function () {
	
	//初始化日期控件
	$(".datetimepicker").datetimepicker({
        format: "yyyy-mm-dd hh:ii",
        language: "zh-CN",
        autoclose: true,
        clearBtn: true,
        todayBtn: true,
        minuteStep: 5
	});
	
    //初始化div高度
    $("#org_tree_content").height($(window).height()-130);
    //初始化组织架构树
    initOrgMenuTree();
    //初始化角色树
    initRoleMenuTree();
    //初始化组织架构表格
    var orgTableInit = new OrgTableInit();
    orgTableInit.Init();
    // //初始化角色表格
    // var roleTableInit = new RoleTableInit();
    // roleTableInit.Init();
    //初始化显示
    $("#orgTable").show();
    $("#roleTable").hide();

    //新增编辑公司
    //初始化公司表单验证
    $('#useunitForm').bootstrapValidator({
        message: '此处输入有误',
        excluded: [':disabled'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            'useunitType.id': {
                message: '企业类型不合法',
                validators: {
                    notEmpty: {
                        message: '请选择企业类型'
                    }
                }
            },
            'name': {
                message: '名称输入不合法',
                validators: {
                    notEmpty: {
                        message: '企业名称不可为空'
                    },
                    stringLength: {
                        max: 20,
                        message: '企业名称长度必须在20个字内'
                    }
                }
            },
            'shortName': {
                message: '简称输入不合法',
                validators: {
                    notEmpty: {
                        message: '企业简称不可为空'
                    },
                    stringLength: {
                        max: 20,
                        message: '企业简称长度必须在20个字内'
                    }
                }
            },
            'hireInfo.maxUserNum': {
                message: '受限人数输入不合法',
                validators: {
                    notEmpty: {
                        message: '受限人数不可为空'
                    }
                }
            }
        }
    });
    //初始化公司模态窗口关闭事件
    $('#useunit_edit_modalwin').on('hidden.bs.modal', function (e) {
        resetUseunitForm();
    });
    $(document).on('change', '#photo_file', function () {
        readFile(this);
    });
    
    //同步uuv组织模态窗口关闭事件
    $('#sync_uuv_modalwin').on('hidden.bs.modal', function (e) {
    	$("#s_time").val('');
    	$("#e_time").val('');
    });

    //新增编辑部门
    //初始化部门表单验证
    $('#deptForm').bootstrapValidator({
        message: '此处输入有误',
        excluded: [':disabled'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            'name': {
                message: '名称输入不合法',
                validators: {
                    notEmpty: {
                        message: '部门名称不可为空'
                    },
                    stringLength: {
                        max: 20,
                        message: '部门名称长度必须在20个字内'
                    }
                }
            }
        }
    });
    //初始化部门模态窗口关闭事件
    $('#dept_edit_modalwin').on('hidden.bs.modal', function (e) {
        resetDetpForm();
    });

    //新增编辑用户
    //TODO 初始化用户表单难证
    $('#sysuserForm').bootstrapValidator({
        message: '此处输入有误',
        excluded: [':disabled'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            'loginPwd':{
                message : '密码不合法',
                validators: {
                    identical: {
                        field: 'sureloginPwd',
                        message: '两次密码不一致'
                    }
                }
            },
            'sureloginPwd':{
                message : '密码不合法',
                validators: {
                    identical: {
                        field: 'loginPwd',
                        message: '两次密码不一致'
                    }
                }
            },
            'name':{
                message: '用户名称不合法',
                validators: {
                    notEmpty: {
                        message: '用户名称不能为空'
                    },
                    stringLength: {
                        max: 20,
                        message: '用户名称长度必须在20个字内'
                    }
                }
            },
            'mobile':{
                message:'手机号码不合法',
                validators:{
                    regexp: {
                        regexp: /^1\d{10}$/,
                        message: '手机号码不合法'
                    }
                }
            },
            'sysuserDept': {
                message: '角色部门不合法',
                trigger:"change",
                validators: {
                    notEmpty: {
                        message: '请选择用户所属部门'
                    }
                }
            }
        }
    });
    //初始化用户模态窗口关闭事件
    $('#sysuser_add_modalwin').on('hidden.bs.modal', function (e) {
        resetSysuserForm();
    });
    $(document).on('change', '#sysuser_photo_file', function () {
        readSysuserFile(this);
    });
    //初始化用户角色选择模态窗口关闭事件
    $("#sysuser_select_role_modal").on('hidden.bs.modal', function (e) {
        removeAllRole();
    })

    //编辑角色下属用户
    //初始化编辑角色下属成员模态窗口关闭事件
    $('#role_edit_sysuser_modalwin').on('hidden.bs.modal', function (e) {
        resetRoleSysuserForm();
    });

    //新增角色组
    //初始化角色组表单验证
    $('#roleGroupForm').bootstrapValidator({
        message: '此处输入有误',
        excluded: [':disabled'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            'roleGroupName': {
                message: '名称输入不合法',
                validators: {
                    notEmpty: {
                        message: '角色组名称不可为空'
                    },
                    stringLength: {
                        max: 20,
                        message: '角色组名称长度必须在20个字内'
                    }
                }
            }
        }
    });
    //初始化角色组模态窗关闭事件
    $("#roleGroup_edit_modalwin").on('hidden.bs.modal', function (e) {
        resetRoleGroupForm();
    });

    //新增角色
    //初始化新增角色表单验证
    $('#roleForm').bootstrapValidator({
        message: '此处输入有误',
        excluded: [':disabled'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            'roleName': {
                message: '名称输入不合法',
                validators: {
                    notEmpty: {
                        message: '角色名称不可为空'
                    },
                    stringLength: {
                        max: 20,
                        message: '角色名称长度必须在20个字内'
                    }
                }
            },
            'roleGroupId': {
                message: '角色分组不合法',
                validators: {
                    notEmpty: {
                        message: '请选择角色分组'
                    }
                }
            },
            'roleDept': {
                message: '角色部门不合法',
                trigger:"change",
                validators: {
                    notEmpty: {
                        message: '请选择角色所属部门'
                    }
                }
            }
        }
    });
    //初始化新增角色模态窗关闭事件
    $("#role_edit_modalwin").on('hidden.bs.modal', function (e) {
        resetRoleForm();
    });
    //初始化新增角色分组下拉框数据
    var lesseeId = $("#role_form_lessedId").val();
    var useunitId = $("#role_form_useunitId").val();
    //初始化新增角色权限选择模态窗
    $("#role_select_auth_modal").on('hidden.bs.modal', function (e) {
        removeAllAuth();
    });
    
    $('#orgtree_div').height($(window).height()-120);
    
});

//同步uuv组织用户
function goSyncUUV(_this){
	var startTime = $('#s_time').val();
	if(startTime.length <1 ){
		alert("请选择开始时间!");
		return;
	}
	$(_this).attr('disabled', 'disabled');
    $(_this).text('保存中...');
	$.ajax({
        url: CTX + 'organizationManage/synchronize-uuv',
        method: 'post',
        dataType: 'json',
        data: $("#uuvForm").serialize(),
        success: function (json) {
            alert(json.msg);
            if (json.success) {
                $('#sync_uuv_modalwin').modal('toggle');
                initOrgMenuTree();
            }
            $(_this).removeAttr('disabled');
            $(_this).text('保存');
        },
        error: function () {
            alert('请求超时或系统出错!');
        }
    });
}

//初始化组织架构树
function initOrgMenuTree() {
    loading("正在加载...");
    var r = Math.random().toString().substring(5);
    $.ajax({
        url: CTX + 'organizationManage/query-org-tree?Mathnum=' + r,
        method: 'GET',
        dataType: 'json',
        success: function (json) {
            $('#orgMenuTree').treeview({
                showTags: true,
                data: json,
                onNodeSelected: function (event, node) {
                    //新增、删除成员按钮恢复可用状态
                    $("#btn_add_member").attr('disabled',false);
                    $('#btn_delete_member').attr('disabled', false);
                    clickOrgTree(event, node);
                }
            });
            // $('#orgMenuTree').treeview('expandAll');
            initOrgTreeContextmenu();
            loaded();
        },
        error: function () {
            loaded();
            alert('请求超时或系统出错');
        }
    });
}

//初始化角色树
function initRoleMenuTree() {
    loading("正在加载...");
    var r = Math.random().toString().substring(5);
    $.ajax({
        url: CTX + 'organizationManage/query-role-tree?Mathnum=' + r,
        method: 'GET',
        dataType: 'json',
        success: function (json) {
            $('#roleMenuTree').treeview({
                showTags: true,
                data: json,
                onNodeSelected: function (event, node) {
                    //角色编辑成员按钮恢复可用状态
                    $("#btn_add_role_member").attr('disabled',false);
                    clickRoleTree(event, node);
                }
            });
            // $('#roleMenuTree').treeview('expandAll');
            initRoleTreeContextmenu();
            loaded();
        },
        error: function () {
            loaded();
            alert('请求超时或系统出错');
        }
    });
}

//初始化组织架构表格
var OrgTableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#orgTableContent').bootstrapTable({
            url: CTX + 'organizationManage/query-systemuser-page',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            //toolbarAlign:'left',				//工具栏位置
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            queryParamsType: "a",
            search: true,                       //是否显示表格搜索
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: $(window).height()-120,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            showExport: false,                     //是否显示导出
            exportDataType: "basic",              //basic', 'all', 'selected'.
            exportTypes: ['excel', 'doc', 'xml', 'csv', 'json'],  //导出文件类型
            exportOptions: {
                ignoreColumn: [0],  //忽略某一列的索引 ,如 [0,1]
                fileName: '现场人员信息导出',  //文件名称设置
                worksheetName: '信息',  //表格工作区名称
                tableName: '现场人员信息导出文件'
            },
            columns: [
                {
                    checkbox:true
                },
                {
                    field: 'name',
                    title: '姓名',
                    sortable: false
                }, {
                    field: 'loginName',
                    title: '账号',
                    sortable: false
                }, {
                    field: 'mobile',
                    title: '手机',
                    sortable: false
                }, {
                    field: 'isValid',
                    title: '状态',
                    sortable: false,
                    formatter: function (value) {
                        return value == true ? "在用" : "禁用";
                    }
                }, {
                    field : 'operate',
                    title : '操作',
                    align : 'center',
                    valign : 'middle',
                    clickToSelect : false,
                    formatter: function (value, row, index) {
                        if(row.isOpt){
                            var operateHtml = '<button type="button" onclick="goEditSysuser(\''+row.id+'\')" style=" background: none; border:none;"><i title="编辑" class="fa fa-edit" style="color:green;"></i>&nbsp;</button>' +
                                '<button type="button" class="btn btn-default btn-link" onclick="goAllowSysuser(\''+ row.id + '\', \'' + row.isValid + '\')">'+(row.isValid?'<i class="glyphicon glyphicon-ban-circle text-red" title="禁用"></i>':'<i class="glyphicon glyphicon-ok text-success" title="启用"></i>')+'</button>';
                            return operateHtml;
                        }else{
                            return null;
                        }
                    }
                }
            ]
        });
        $('#orgTableContent .search input').attr('placeholder', '姓名');
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var data = JSON.stringify($('#formSearch').serializeJSON());
        var temp = {   //这里的键的名字和控制器的变量名必须一致，这边改动，控制器也需要改成一样的
            pageNum: params.pageNumber,//页码params.offset
            size: params.pageSize,//页面大小params.limit
            order: params.sortOrder,//排序 params.order
            ordername: params.sortName,//排序字段 params.sort
            from: data,
            search: params.searchText
        };
        return temp;
    };
    return oTableInit;
};

//初始化角色表格
var RoleTableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#roleTableContent').bootstrapTable({
            url: CTX + 'organizationManage/query-sysuser-by-role-page',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#role_toolbar',                //工具按钮用哪个容器
            //toolbarAlign:'left',				//工具栏位置
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            queryParamsType: "a",
            search: false,                       //是否显示表格搜索
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: $(window).height()-80,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            showExport: false,                     //是否显示导出
            exportDataType: "basic",              //basic', 'all', 'selected'.
            exportTypes: ['excel', 'doc', 'xml', 'csv', 'json'],  //导出文件类型
            exportOptions: {
                ignoreColumn: [0],  //忽略某一列的索引 ,如 [0,1]
                fileName: '角色人员信息导出',  //文件名称设置
                worksheetName: '信息',  //表格工作区名称
                tableName: '角色人员信息导出文件'
            },
            columns: [
                {
                    field: 'name',
                    title: '姓名',
                    sortable: false
                }, {
                    field: 'useunit',
                    title: '部门',
                    sortable: false
                }, {
                    field: 'isValid',
                    title: '状态',
                    sortable: false,
                    formatter: function (value) {
                        return value == true ? "在用" : "禁用";
                    }
                }
            ]
        });
        // $('#roleTableContent .search input').attr('placeholder', '姓名');
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var data = JSON.stringify($('#rold_formSearch').serializeJSON());
        var temp = {   //这里的键的名字和控制器的变量名必须一致，这边改动，控制器也需要改成一样的
            pageNum: params.pageNumber,//页码params.offset
            size: params.pageSize,//页面大小params.limit
            order: params.sortOrder,//排序 params.order
            ordername: params.sortName,//排序字段 params.sort
            from: data,
            search: params.searchText
        };
        return temp;
    };
    return oTableInit;
}

//初始化组织架构树右键菜单
function initOrgTreeContextmenu() {
    $('#orgMenuTree').contextmenu({
        target: '#org-context-menu',
        before: function (e, context) {
            var nodeId = e.target.getAttribute("data-nodeid");
            var node = $('#orgMenuTree').treeview('getNode', nodeId);
            currentOrgNode = node;
            $('#synchronize,#addCompany,#addDept,#editCompany,#editDept,#downMenu,#upMenu,#removeMenu').show();
            $('#addRole, #editRole, #removeRole').hide();
            //根据类型显示菜单
            if (currentOrgNode.nodeType == 'typeManageCompany') {
                $('#addCompany, #addDept').show();
                $('#editCompany, #editDept, #downMenu, #upMenu, #removeMenu').hide();
            } else if (currentOrgNode.nodeType == 'typeCompany') {
                $("#addCompany, #addDept, #editCompany, #downMenu, #upMenu, #removeMenu").show();
                $("#editDept").hide();
            } else if (currentOrgNode.nodeType == 'typeDetp') {
                $("#addDept, #editDept, #downMenu, #upMenu, #removeMenu").show();
                $("#addCompany, #editCompany").hide();
            }
        },
        onItem: function (context, e) {
            var operId = e.target.getAttribute('id');
            if (operId == 'addCompany' || operId == '') {
                addCompany();
            }
            if (operId == 'editCompany') {
                editCompany();
            }
            if (operId == 'addDept') {
                addDept();
            }
            if (operId == 'editDept') {
                editDept();
            }
            if (operId == 'upMenu' || operId == 'downMenu') {
                goUporDownMenu('company', operId);
            }
            if (operId == 'removeMenu') {
                removeMenu('company');
            }
            if (operId == 'synchronize') {
            	synchronizeUUV();
            }
            
        }
    });
}

//初始化角色树右键菜单
function initRoleTreeContextmenu() {
    $('#roleMenuTree').contextmenu({
        target: '#org-context-menu',
        before: function (e, context) {
            var nodeId = e.target.getAttribute("data-nodeid");
            var node = $('#roleMenuTree').treeview('getNode', nodeId);
            currentRoleNode = node;
            $('#synchronize,#addCompany, #addDept, #editCompany, #editDept, #downMenu, #upMenu, #removeMenu').hide();
            $('#addRole, #editRole, #removeRole').show();
            //根据类型显示菜单
            if (!currentRoleNode.selectable) {
                $('#editRole, #removeRole').hide();
            }
        },
        onItem: function (context, e) {
            var operId = e.target.getAttribute('id');
            if(operId == 'addRole'){
                goEditRole(null);
            }
            if (operId == 'editRole') {
                editRole();
            }
            if (operId == 'removeRole') {
                removeRole();
            }
        }
    });
}

//切换组织架构\角色管理界面
function changeView(viewType) {
    if (viewType == 'org') {
        $("#orgTable").show();
        $("#roleTable").hide();
    }
    if (viewType == 'role') {
        $("#role_tree_content").height($(window).height()-96);
        //初始化角色表格
        var roleTableInit = new RoleTableInit();
        roleTableInit.Init();
        $("#orgTable").hide();
        $("#roleTable").show();
    }
}

//同步uuv组织
function synchronizeUUV(){
	//alert("同步uuv组织");
	$('#sync_uuv_modalwin').modal('toggle');
}

//添加公司
function addCompany() {
    goEditUseunit("", currentOrgNode.id);
}

//编辑公司
function editCompany() {
    goEditUseunit(currentOrgNode.id, "");
}

//添加部门
function addDept() {
    goEditDept("", currentOrgNode.id);
}

//编辑部门
function editDept() {
    goEditDept(currentOrgNode.id, "");
}

//上移或下移单位节点
function goUporDownMenu(type, operId) {
    alert("上移或下移" + type + "#" + operId);
}

//删除单位节点
function removeMenu(type) {
    if("typeCompany" == currentOrgNode.nodeType){
        $('#delete_useunit_title').html('删除公司');
        $('#delete_useunit_tip').html('确认删除公司？');
    }else if("typeDetp" == currentOrgNode.nodeType){
        $('#delete_useunit_title').html('删除部门');
        $('#delete_useunit_tip').html('确认删除部门？');
    }
    $('#delete_useunit_id').val(currentOrgNode.id);
    $('#delete_useunit_modalwin').modal('toggle');
}

//编辑角色
function editRole() {
    goEditRole(currentRoleNode.id);
}

//删除角色
function removeRole() {
    $('#delete_role_id').val(currentRoleNode.id);
    $('#delete_role_modalwin').modal('toggle');
}

//组织结构树节点选中事件
function clickOrgTree(event, node) {
    $("#orgId").val(node.id);
    $("#orgName").html(node.text);
    $("#orgTableContent").bootstrapTable('refresh');
}

//角色树节点选中事件
function clickRoleTree(event, node) {
    $("#roleId").val(node.id);
    $("#roleName").html(node.text);
    $("#roleType").val(node.nodeType);
    $('#role_sysuser_roleid').val(node.id);
    $("#roleTableContent").bootstrapTable('refresh');
}

//新增编辑企业
function fnSelectImg() {
    document.getElementById('photo_file').click();
}

function readFile(_this) {
    var file = _this.files[0];
    if (file == null) {
        return false;
    }
    if (file.size / 1024 > 200) {
        alert('大小不能超过200KB');
        return false;
    }
    //判断是否是图片类型
    if (!/image\/\w+/.test(file.type)) {
        alert("只能选择图片");
        return false;
    }
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function (e) {
        $('#logo_img .img-rounded').attr('src', this.result);
        $("input[name='hireInfo.logo']").val(this.result);
    }
}

function goEditUseunit(useunitId, parentId) {
    $('#useunit_edit_modalwin').modal('toggle');
    if (useunitId) {
        $('#add_company_titile').text('编辑公司');
        var r = Math.random().toString().substring(5);
        $.ajax({
            url: CTX + 'organizationManage/query-useunit-by-id?Mathnum=' + r,
            method: 'get',
            dataType: 'json',
            data: {useunitId: useunitId},
            success: function (json) {
                if (json) {
                    var useunit = json.useunit;
                    $('#useunitForm').find("input[name='id']").val(useunit.id);
                    $('#useunitForm').find("input[name='parentId']").val(useunit.parentId);
                    $('#useunitForm').find("select[name='useunitType.id']").val(useunit.useunitType.id);
                    $('#useunitForm').find("input[name='name']").val(useunit.name);
                    $('#useunitForm').find("input[name='shortName']").val(useunit.shortName);
                    $('#useunitForm').find("input[name='remark']").val(useunit.remark);
                }
            },
            error: function () {
                alert('请求超时或系统出错!');
            }
        });
    } else {
        $('#add_company_titile').text('新建子公司');
        $('#useunitForm').find("input[name='parentId']").val(parentId);
        if (null == currentOrgNode.lesseeId || "" == currentOrgNode.lesseeId) {
            $('#useunitForm').find("input[name='lesseeId']").val(currentOrgNode.id);
        } else {
            $('#useunitForm').find("input[name='lesseeId']").val(currentOrgNode.lesseeId);
        }

    }
}

function goSaveUseunit(_this) {
    var result = $('#useunitForm').data('bootstrapValidator');
    result.validate();
    if (result.isValid()) {
        $(_this).attr('disabled', 'disabled');
        $(_this).text('保存中...');
        $.ajax({
            url: CTX + 'organizationManage/save-useunit',
            method: 'post',
            dataType: 'json',
            data: $("#useunitForm").serialize(),
            success: function (json) {
                alert(json.msg);
                if (json.success) {
                    $('#useunit_edit_modalwin').modal('toggle');
                    initOrgMenuTree();
                }
                $(_this).removeAttr('disabled');
                $(_this).text('保存');
            },
            error: function () {
                alert('请求超时或系统出错!');
                $(_this).removeAttr('disabled');
                $(_this).text('保存');
            }
        });
    }
}

function resetUseunitForm() {
    $('#useunitForm').bootstrapValidator('resetForm', true);
    $("#useunitForm input[name='id']").val('');
    $("#useunitForm input[name='hireInfo.id']").val('');
    $('#useunitForm').find("input[name='remark']").val('');
    $('#useunitForm').find("input[name='parentId']").val('');
    $('#useunitForm').find("input[name='lesseeId']").val('');
    $('#logo_img .img-rounded').attr('src', STATIC + 'images/u331.png');
    $("#photo_file").remove();
    $('#logo_img').append('<input type="file" id="photo_file" style="display:none;"/>');
}

//新增编辑部门
function goEditDept(deptId, parentId) {
    $("#dept_edit_modalwin").modal("toggle");
    if (deptId) {
        $("#dept_mode_title").html("编辑部门");
        var r = Math.random().toString().substring(5);
        $.ajax({
            url: CTX + 'organizationManage/query-useunit-by-id?Mathnum=' + r,
            method: 'get',
            dataType: 'json',
            data: {useunitId: deptId},
            success: function (json) {
                if (json) {
                    var useunit = json.useunit;
                    $('#deptForm').find("input[name='id']").val(useunit.id);
                    $('#deptForm').find("input[name='parentId']").val(useunit.parentId);
                    $('#deptForm').find("input[name='name']").val(useunit.name);
                }
            },
            error: function () {
                alert('请求超时或系统出错!');
            }
        });
    } else {
        $("#dept_mode_title").html("新增部门");
        $("#deptForm").find("input[name='parentId']").val(parentId);
        if (null == currentOrgNode.lesseeId || "" == currentOrgNode.lesseeId) {
            $('#deptForm').find("input[name='lesseeId']").val(currentOrgNode.id);
        } else {
            $('#deptForm').find("input[name='lesseeId']").val(currentOrgNode.lesseeId);
        }
    }
}

function goSaveDept(_this) {
    var result = $("#deptForm").data("bootstrapValidator");
    result.validate();
    if (result.isValid()) {
        $(_this).attr('disabled', 'disabled');
        $(_this).text('保存中...');
        $.ajax({
            url: CTX + 'organizationManage/save-dept',
            method: 'post',
            dataType: 'json',
            data: $("#deptForm").serialize(),
            success: function (json) {
                alert(json.msg);
                if (json.success) {
                    $('#dept_edit_modalwin').modal('toggle');
                    initOrgMenuTree();
                }
                $(_this).removeAttr('disabled');
                $(_this).text('保存');
            },
            error: function () {
                alert('请求超时或系统出错!');
                $(_this).removeAttr('disabled');
                $(_this).text('保存');
            }
        });
    }
}

function resetDetpForm() {
    $('#deptForm').bootstrapValidator('resetForm', true);
    $("#deptForm input[name='id']").val('');
    $('#deptForm').find("input[name='parentId']").val('');
    $('#deptForm').find("input[name='lesseeId']").val('');
}

function deleteUseunit() {
    var useunitId = $('#delete_useunit_id').val();
    var r = Math.random().toString().substring(5);
    $.ajax({
        url: CTX + 'organizationManage/delete-useunit-by-id?Mathnum=' + r,
        method: 'get',
        dataType: 'json',
        data: {useunitId:useunitId},
        success: function (json) {
            $('#delete_useunit_modalwin').modal('toggle');
            if (!json.success) {
                alert(json.msg);
            }else{
                //删除成功后，展示界面回归组织架构
                $('#orgName').html('组织架构');
                $('#orgId').val('');
                //新增、删除成员按钮恢复可用状态
                $("#btn_add_member").attr('disabled',true);
                $('#btn_delete_member').attr('disabled', true);
                $('#orgTableContent').bootstrapTable('refresh');
                initOrgMenuTree();
                alert(json.msg);
            }
        },
        error: function () {
            alert('请求超时或系统出错!');
        }
    });

}

//新增编辑用户
function fnSelectSysuserImg() {
    document.getElementById('sysuser_photo_file').click();
}

function readSysuserFile(_this) {
    var file = _this.files[0];
    if (file == null) {
        return false;
    }
    if (file.size / 1024 > 200) {
        alert('大小不能超过200KB');
        return false;
    }
    //判断是否是图片类型
    if (!/image\/\w+/.test(file.type)) {
        alert("只能选择图片");
        return false;
    }
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function (e) {
        $('#sysuser_img .img-rounded').attr('src', this.result);
        $("input[name='sysuser.img']").val(this.result);
    }
}

function goAddSysuer() {
    //新增成员校验上限人数
	var r = Math.random().toString().substring(5);
    $.ajax({
        url: CTX + 'organizationManage/check-sysuser-num?Mathnum=' + r,
        type: 'get',
        dataType: '',
        success: function (data) {
            if(!data.success){
                alert(data.msg);
            }else{
                goEditSysuser(null);
            }
        },
        error: function () {
            alert('请求超时或系统出错!');
        }
    });
}

function goEditSysuser(sysuserId) {
    $('#add_sysuser_title').text('新建用户');
    $("#sysuser_add_modalwin").modal("toggle");
    var r = Math.random().toString().substring(5);
    //用户选择部门下拉树
    $.ajax({
        url: CTX + 'organizationManage/query-org-tree?Mathnum=' + r, // 请求的URL
        dataType: 'json',
        type: "get",
        success: function (data) {
            $('#sysuser_select_dept_tree').treeview({
                data: data,
                onNodeSelected: function (event, node) {
                    $("#sysuser_select_dept_text").val(node.text).change();
                    $("#sysuser_select_dept_id").val(node.id);
                    $("#sysuser_select_dept_tree_div").hide();
                }
            });
        }
    });
    $('#sysuser_select_dept_id').val($('#orgId').val());
    $('#sysuser_select_dept_text').val($('#orgName').html());
    if(null != sysuserId){
        $('#add_sysuser_title').text('编辑用户');
        //隐藏账号相关信息,并去除校验
        $('#edit_sysuser_count_info').hide();
        $("#sysuserForm").bootstrapValidator('removeField','loginName');
        var r = Math.random().toString().substring(5);
        $.ajax({
            url: CTX + 'organizationManage/query-sysuser-by-id?Mathnum=' + r,
            method: 'get',
            dataType: 'json',
            data: {sysuserId:sysuserId},
            success: function (json) {
                if (json) {
                    var sysuser = json.sysuser;
                    $('#sysuserForm').find("input[name='id']").val(sysuser.id);
                    $('#sysuserForm').find("input[name='loginName']").val(sysuser.loginName);
                    $('#sysuserForm').find("input[name='loginPwd']").val('');
                    $('#sysuserForm').find("input[name='sureloginPwd']").val('');
                    $('#sysuserForm').find("input[name='name']").val(sysuser.name);
                    $('#sysuserForm').find("input[name='mobile']").val(sysuser.mobile != null ? sysuser.mobile: '');
                    $('#sysuserForm').find("input[name='useunitId']").val(json.useunitId != null ? json.useunitId : '');
                    $('#sysuserForm').find("input[name='sysuserDept']").val(json.useunitName != null ? json.useunitName : '');
                    $('#sysuserForm').find("input[name='roleIds']").val(json.roleIds);
                    $('#sysuserForm').find("input[name='roleNames']").val(json.roleNames);
                    var roleList = json.roleInfoList;
                    var roleNamesHtml = "";
                    for(var i = 0; i < roleList.length; i++){
                        var roleInfo = roleList[i].split(',');
                        roleNamesHtml +=
                            '<div style="background-color:#3daae8;color:white;width:auto;hight:60px;float:left;margin:5px;">' +
                            '	<input type="hidden" name="auth_id" class="authId" value="' + roleInfo[0] + '"/>' +
                            '   <spand style="padding-left: 5px;padding-right: 5px;">' + roleInfo[1] + '</spand>' +
                            '</div>';
                    }
                    //将权限节点信息保存角色窗体
                    $("#sysuser_selected_role").html('');
                    $("#sysuser_selected_role").append(roleNamesHtml);
                }
            },
            error: function () {
                alert('请求超时或系统出错!');
            }
        });
    }else{
        $('#edit_sysuser_count_info').show();
        $("#sysuserForm").bootstrapValidator("addField", "loginName", {
            validators: {
                notEmpty: {
                    message: '帐号不可为空'
                },
                stringLength: {
                    max: 20,
                    message: '帐号长度必须在20个字内'
                },
                regexp: {
                    regexp: /^[a-zA-Z0-9]+$/,
                    message: '账号需由英文字母或数字组成'
                },
                remote: {//ajax验证,{"valid",true|false}
                    url: CTX + 'organizationManage/is-loginname-exist',//验证地址
                    message: '帐号已存在',//提示消息
                    // delay :  500,//设置0.5秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大
                    type: 'POST'//请求方式
                }
            }
        });
    }
}

function goSaveSysuser(_this) {
    var result = $('#sysuserForm').data('bootstrapValidator');
    result.validate();
    if (result.isValid()) {
        $(_this).attr('disabled', 'disabled');
        $(_this).text('保存中...');
        $.ajax({
            url: CTX + 'organizationManage/save-sysuser',
            method: 'post',
            dataType: 'json',
            data: $("#sysuserForm").serialize(),
            success: function (json) {
                alert(json.msg);
                if (json.success) {
                    $('#sysuser_add_modalwin').modal('toggle');
                    //刷新表格
                    $("#orgTableContent").bootstrapTable('refresh');
                }
                $(_this).removeAttr('disabled');
                $(_this).text('保存');
            },
            error: function () {
                alert('请求超时或系统出错!');
                $(_this).removeAttr('disabled');
                $(_this).text('保存');
            }
        });
    }
}

function resetSysuserForm() {
    $('#edit_sysuser_id').val('');
    $('#sysuser_select_dept_id').val('');
    $('#sysuser_select_dept_text').val('');
    $('#sysuser_role_ids').val('');
    $('#sysuser_role_names').val('');
    $('#sysuser_selected_role').html('');

    $('#sysuser_img .img-rounded').attr('src', STATIC + 'images/u331.png');
    $("#sysuser_photo_file").remove();
    $('#sysuser_img').append('<input type="file" id="sysuser_photo_file" style="display:none;"/>');

    $('#sysuserForm').find("input[name='loginName']").val('');
    $('#sysuserForm').find("input[name='loginPwd']").val('');
    $('#sysuserForm').find("input[name='sureloginPwd']").val('');
    $('#sysuserForm').find("input[name='name']").val('');
    $('#sysuserForm').find("input[name='mobile']").val('');
    $('#sysuserForm').find("input[name='useunitId']").val( '');

    $('#sysuserForm').bootstrapValidator('resetForm', true);
}

function sysuserSelectRole() {
    $("#sysuser_select_role_modal").modal("toggle");
    var sysuserId = $("#now_user_id").val();
    var r = Math.random().toString().substring(5);
    $.ajax({
        url: CTX + 'organizationManage/query-sysuser-role-tree?Mathnum=' + r,
        method: 'GET',
        dataType: 'json',
        data: {sysuserId: sysuserId},
        success: function (json) {
            $('#sysuserRoleTree').treeview({
                showTags: true,
                showCheckbox: true,
                data: json,
                onNodeChecked: function (event, node) {
                    if(node.roleType == 'admin' || node.roleType == 'sys'){
                        //管理员类型角色不可勾选
                        $('#sysuserRoleTree').treeview('uncheckNode',[node.nodeId,{silent:true}]);
                    }else{
                        addSysuserRole(node.id, node.text);
                    }
                },
                onNodeUnchecked: function (event, node) {
                    removeSysuserRole(node.id, null);
                }
            });
            // $('#sysuserRoleTree').treeview('collapseAll');
            var roleNames = $("#sysuser_role_names").val();
            var roles = roleNames.split(",");
            for(var i = 0; i < roles.length; i++){
                if("" != roles[i]){
                    var checkableNodes = $("#sysuserRoleTree").treeview('search', [roles[i], {ignoreCase: false, exactMatch: true}]);
                    $("#sysuserRoleTree").treeview('checkNode', [checkableNodes, { silent: true }]);
                    addSysuserRole(checkableNodes[0].id, checkableNodes[0].text);
                }
            }
            $("#sysuserRoleTree").treeview("clearSearch");
            loaded();
        },
        error: function () {
            loaded();
            alert('请求超时或系统出错');
        }
    });
}

function addSysuserRole(id, text) {
    var htmlStr = '';
    if (id != null && text != null) {
        htmlStr +=
            '<div id="selected_role' + id + '" style="background-color:#3daae8;color:white;width:auto;hight:60px;float:left;margin:5px;">' +
            '	<input type="hidden" name="role_id" value="' + id + '"/>' +
            '   <spand style="padding-left: 5px;padding-right: 5px;">' + text + '</spand>' +
            '   <input type="button" class="closeDiv" onclick="removeSysuserRole(\'' + id + '\', \'' + text + '\')" value="X" style="background:transparent;border:none;"/>' +
            '</div>';
        $("#sysuser_select_roleids").append(htmlStr);
    }
}

function removeSysuserRole(id, text) {
    //从展示框中移除
    $('#selected_role' + id).remove();
    //从权限树中取消选中
    if (null != text || "" != text) {
        var checkableNodes = $("#sysuserRoleTree").treeview('search', [text, {ignoreCase: false, exactMatch: true}]);
        $("#sysuserRoleTree").treeview("uncheckNode", [checkableNodes]);
    }
}

function removeAllRole() {
    //取消所有的选中权限节点
    $("#sysuserRoleTree").treeview("uncheckAll");
}

function doSaveSysuserRole() {
    var roleIds = "";
    var roleNames = "";
    var roleNamesHtml = "";
    //获取所有选中权限树节点
    var checkedNodes = $('#sysuserRoleTree').treeview('getChecked');
    for (var i = 0; i < checkedNodes.length; i++) {
        var node = checkedNodes[i];
        roleIds = roleIds + "," + node.id;
        roleNames += "," + node.text;
        roleNamesHtml +=
            '<div id="sysuser_selected_role' + node.id + '" class="allAuths" style="background-color:#3daae8;color:white;width:auto;hight:60px;float:left;margin:5px;">' +
            '	<input type="hidden" name="auth_id" class="authId" value="' + node.id + '"/>' +
            '   <spand style="padding-left: 5px;padding-right: 5px;">' + node.text + '</spand>' +
            '</div>';
    }
    //将权限节点信息保存到新增角色窗体
    $("#sysuser_selected_role").html('');
    $("#sysuser_selected_role").append(roleNamesHtml);
    $("#sysuser_role_names").val(roleNames);
    $("#sysuser_role_ids").val(roleIds);
    //清空权限树选中节点
    removeAllRole();
    $("#sysuser_select_role_modal").modal("toggle");
}

//新增角色组
function goEditRoleGroup() {
    $("#roleGroup_edit_modalwin").modal("toggle");
}

function goSaveRoleGroup(_this) {
    var result = $("#roleGroupForm").data("bootstrapValidator");
    result.validate();
    if (result.isValid()) {
        $(_this).attr('disabled', 'disabled');
        $(_this).text('保存中...');
        $.ajax({
            url: CTX + 'organizationManage/add-roleGroup',
            method: 'post',
            dataType: 'json',
            data: $("#roleGroupForm").serialize(),
            success: function (json) {
                alert(json.msg);
                if (json.success) {
                    $('#roleGroup_edit_modalwin').modal('toggle');
                    initRoleMenuTree();
                }
                $(_this).removeAttr('disabled');
                $(_this).text('保存');
            },
            error: function () {
                alert('请求超时或系统出错!');
                $(_this).removeAttr('disabled');
                $(_this).text('保存');
            }
        });
    }
}

function resetRoleGroupForm() {
    $('#roleGroupForm').bootstrapValidator('resetForm', true);
}

//新增角色
function goEditRole(roleId) {
    $("#role_edit_modalwin").modal("toggle");
    $('#role_parent_id').val(currentRoleNode.id);
    var r = Math.random().toString().substring(5);
    //角色选择部门下拉树
    $.ajax({
        url: CTX + 'organizationManage/query-org-tree?Mathnum=' + r, // 请求的URL
        dataType: 'json',
        type: "get",
        success: function (data) {
            $('#role_select_dept_tree').treeview({
                data: data,
                onNodeSelected: function (event, node) {
                    $("#role_select_dept_text").val(node.text).change();
                    $("#role_select_dept_id").val(node.id);
                    $("#role_select_dept_tree_div").hide();
                }
            });
        }
    });
    if(null != roleId){
        $("#role_form_title").html("编辑角色");
        var r = Math.random().toString().substring(5);
        $.ajax({
            url: CTX + 'organizationManage/query-role-by-id?Mathnum=' + r,
            method: 'get',
            dataType: 'json',
            data: {roleId: roleId},
            success: function (json) {
                if (json) {
                    $("#role_form_role_id").val(json.roleId);
                    $("#role_name").val(json.roleName);
                    $("#role_group_ids").val(json.parentId);
                    $("#role_select_dept_id").val(json.useunitId);
                    $("#role_select_dept_text").val(json.useunitName);
                    var auths = json.authoritys;
                    var authIds = "";
                    var authNames = "";
                    var authNamesHtml = "";
                    for(var i=0; i < auths.length; i++){
                        authIds = authIds + "," + auths[i].id;
                        authNames += "," + auths[i].name;
                        authNamesHtml +=
                            '<div id="role_selected_auth' + auths[i].id + '" class="allAuths" style="background-color:#3daae8;color:white;width:auto;hight:60px;float:left;margin:5px;">' +
                            '	<input type="hidden" name="auth_id" class="authId" value="' + auths[i].id + '"/>' +
                            '   <spand style="padding-left: 5px;padding-right: 5px;">' + auths[i].name + '</spand>' +
                            '</div>';
                    }
                    $("#selected_auth").append(authNamesHtml);
                    $("#role_auth_ids").val(authIds);
                    $("#role_auth_names").val(authNames);
                }
            },
            error: function () {
                alert('请求超时或系统出错!');
            }
        });
    }else{
        $("#role_form_title").html("新建角色");
    }
}

function roleSelectAuth() {
    $("#role_select_auth_modal").modal("toggle");
    var r = Math.random().toString().substring(5);
    $.ajax({
        url: CTX + 'organizationManage/query-auth-tree-by-sysuser?Mathnum=' + r,
        method: 'GET',
        dataType: 'json',
        success: function (json) {
            $('#authMenuTree').treeview({
                showTags: true,
                showCheckbox: true,
                data: json,
                onNodeChecked: function (event, node) {
                    //左侧桌面、桌面功能块不作处理
                    if(node.id == 'LEFTMENUNODE' || node.id == 'DESKTOPNODE'){
                    }else{
                        addAuth(node.id, node.text);
                        //级联选中父节点
                        checkAuthPartenNode(node);
                        //级联选中节节点
                        checkAuthSubNode(node);
                    }
                },
                onNodeUnchecked: function (event, node) {
                    removeAuth(node.id, null);
                    uncheckAuthSubNode(node);
                }
            });
            //初始化选中事件
            var authNames = $("#role_auth_names").val();
            var auths = authNames.split(",");
            var authIdsValue = $('#role_auth_ids').val();
            var authIds = authIdsValue.split(',');
            for(var i = 0; i < auths.length; i++){
                if("" != auths[i]){
                    var checkableNodes = $("#authMenuTree").treeview('search', [auths[i], {ignoreCase: false, exactMatch: true}]);
                    for(var j = 0; j < checkableNodes.length; j++){
                        if(checkableNodes[j].id == authIds[i]){
                            $("#authMenuTree").treeview('checkNode', [checkableNodes, { silent: true }]);
                            addAuth(checkableNodes[j].id, checkableNodes[j].text);
                        }
                    }
                }
            }
            $("#authMenuTree").treeview("clearSearch");
            loaded();
        },
        error: function () {
            loaded();
            alert('请求超时或系统出错');
        }
    });
}

function goSaveRole(_this) {
    var result = $("#roleForm").data("bootstrapValidator");
    result.validate();
    if (result.isValid()) {
        $(_this).attr('disabled', 'disabled');
        $(_this).text('保存中...');
        $.ajax({
            url: CTX + 'organizationManage/save-role',
            method: 'post',
            dataType: 'json',
            data: $("#roleForm").serialize(),
            success: function (json) {
                alert(json.msg);
                if (json.success) {
                    $('#role_edit_modalwin').modal('toggle');
                    resetRoleForm();
                    initRoleMenuTree();
                }
                $(_this).removeAttr('disabled');
                $(_this).text('保存');
            },
            error: function () {
                alert('请求超时或系统出错!');
                $(_this).removeAttr('disabled');
                $(_this).text('保存');
            }
        });
    }
}

function resetRoleForm() {
    $("#roleForm").bootstrapValidator("resetForm", true);
    $("#selected_auth").html("");
    $("#role_form_role_id").val("");
    $("#role_select_dept_text").val("");
    $("#role_select_dept_id").val("");
    $("#role_auth_ids").val('');
    $("#role_auth_names").val('');
}

//级联选中权限子节点
function checkAuthSubNode(node) {
    if(!node.state.checked){
        $('#authMenuTree').treeview('checkNode',node.nodeId,{silent:true});
        addAuth(node.id, node.text);
    }
    if(node.nodes != null && node.nodes.length>0 ){
        for(var i = 0; i < node.nodes.length; i++){
            checkAuthSubNode(node.nodes[i]);
        }
    }
}

//级联取消选中权限子节点
function uncheckAuthSubNode(node) {
    if(node.state.checked){
        $('#authMenuTree').treeview('uncheckNode',[node.nodeId,{silent:true}]);
        removeAuth(node.id, null);
    }
    if(node.nodes != null && node.nodes.length > 0){
        for(var i = 0; i < node.nodes.length; i++){
            uncheckAuthSubNode(node.nodes[i]);
        }
    }
}

//级联选中权限父级节点
function checkAuthPartenNode(node) {
    var parentNode = $('#authMenuTree').treeview('getParent', node);
    if(parentNode.nodes){
        if(!parentNode.state.checked){
            $('#authMenuTree').treeview('checkNode', [parentNode, { silent: true}]);
            //左侧桌面、桌面功能块不作处理
            if(parentNode.id == 'LEFTMENUNODE' || parentNode.id == 'DESKTOPNODE'){

            }else{
                addAuth(parentNode.id, parentNode.text);
            }
        }
        checkAuthPartenNode(parentNode);
    }else{
        return;
    }
}

//级联取消选中父级节点
function unCheckAuthParten(node) {
    var brotherNodes = $('#authMenuTree').treeview('getSiblings', node);
    if(brotherNodes.length == 0){
        //只有一个节点，直接取消选中父节点
        var parentNode = $('#authMenuTree').treeview('getParent', node);
        if(parentNode.nodes){
            $('#authMenuTree').treeview('uncheckNode', [parentNode, { silent: true}]);
        }
    }else {
        //多个兄弟节点，所有节点都取消选中才取消选中父节点
        var flag = true;
        for(var i = 0; i < brotherNodes.length; i++){
            if(brotherNodes[i].state.checked){
                flag = false;
                break;
            }
        }
        if(flag){
            var parentNode = $('#authMenuTree').treeview('getParent', node);
            if(parentNode.nodes){
                $('#authMenuTree').treeview('uncheckNode', [parentNode, { silent: true}]);
                removeAuth(parentNode.id, parentNode.text);
            }
        }
    }
}

var authList = new Array();
function addAuth(id, name) {
    var index = $.inArray(id, authList);
    if(index == -1){
        authList.push(id);
        var htmlStr = '';
        if (id != null && name != null) {
            htmlStr +=
                '<div id="selected_auth' + id + '" class="allAuths" style="background-color:#3daae8;color:white;width:auto;hight:60px;float:left;margin:5px;">' +
                '	<input type="hidden" name="auth_id" class="authId" value="' + id + '"/>' +
                '   <spand style="padding-left: 5px;padding-right: 5px;">' + name + '</spand>' +
                '   <input type="button" class="closeDiv" onclick="removeAuth(\'' + id + '\', \'' + name + '\')" value="X" style="background:transparent;border:none;"/>' +
                '</div>';
            $("#role_select_authids").append(htmlStr);
        }
    }
}

function removeAuth(id, text) {
    var index = $.inArray(id, authList);
    authList.splice(index, 1);
    //从展示框中移除
    $('#selected_auth' + id).remove();
    //从权限树中取消选中
    if (null != text || "" != text) {
        var checkableNodes = $("#authMenuTree").treeview('search', [text, {ignoreCase: false, exactMatch: true}]);
        $("#authMenuTree").treeview("uncheckNode", [checkableNodes]);
    }
    $('#authMenuTree').treeview('clearSearch');
}

function removeAllAuth() {
    authList.splice(0, authList.length);
    //取消所有的选中权限节点
    $("#authMenuTree").treeview("uncheckAll");
}

function doSaveRoleAuth() {
    var authIds = "";
    var authNames = "";
    var authNamesHtml = "";
    //获取所有选中权限树节点
    var checkedNodes = $('#authMenuTree').treeview('getChecked');
    for (var i = 0; i < checkedNodes.length; i++) {
        var node = checkedNodes[i];
        if(node.id == 'LEFTMENUNODE' || node.id == 'DESKTOPNODE'){
        }
        else{
            authIds = authIds + "," + node.id;
            authNames += "," + node.text;
            authNamesHtml +=
                '<div id="role_selected_auth' + node.id + '" class="allAuths" style="background-color: rgba(60, 143, 224, 0.7);color:white;width:auto;hight:60px;float:left;margin:5px;">' +
                '	<input type="hidden" name="auth_id" class="authId" value="' + node.id + '"/>' +
                '   <spand style="padding-left: 5px;padding-right: 5px;">' + node.text + '</spand>' +
                '</div>';
        }
    }
    //将权限节点信息保存到新增角色窗体
    $("#selected_auth").html('');
    $("#selected_auth").append(authNamesHtml);
    $("#role_auth_names").val(authNames);
    $("#role_auth_ids").val(authIds);
    // $("#role_auth_ids").val();
    //清空权限树选中节点
    removeAllAuth();
    $("#role_select_auth_modal").modal('toggle');
}

function deleteRole() {
    var roleId = $('#delete_role_id').val();
    var r = Math.random().toString().substring(5);
    $.ajax({
        url: CTX + 'organizationManage/delete-role-by-id?Mathnum=' + r,
        method: 'get',
        dataType: 'json',
        data: {roleId:roleId},
        success: function (json) {
            $('#delete_role_modalwin').modal('toggle');
            if (!json.success) {
                alert(json.msg);
            }else{
                //删除角色成功后，表格展示回归角色
                $('#roleName').html('角色');
                $('#roleId').val('');
                $('#roleTableContent').bootstrapTable('refresh');
                //编辑按钮变更为禁用
                //角色编辑成员按钮恢复可用状态
                $("#btn_add_role_member").attr('disabled',true);
                initRoleMenuTree();
                alert(json.msg);
            }
        },
        error: function () {
            alert('请求超时或系统出错!');
        }
    });
}

//编辑角色下属用户
var roleSysuser = new Array();

function goEditRoleSysuser() {
    var sysuserId = $('#now_user_id').val();
    var lesseeId = $('#now_user_lesseeId').val();
    $("#role_edit_sysuser_modalwin").modal("toggle");
    var roleId = $('#role_sysuser_roleid').val();
    roleSysuser.splice(0, roleSysuser.length);
    //初始化组织结构树
    var r = Math.random().toString().substring(5);
    $.ajax({
        url: CTX + 'organizationManage/query-user-sub-org-tree?Mathnum=' + r,
        method: 'GET',
        dataType: 'json',
        data: {sysuserId: sysuserId},
        success: function (json) {
            $('#role_edit_sysuser_orgtree').treeview({
                showTags: true,
                data: json,
                onNodeSelected: function (event, node) {
                    $('#role_edit_sysuser_useunit').val(node.id);
                    $("#role_edit_sysuser_orgusertable").bootstrapTable('refresh');
                }
            });
            loaded();
        },
        error: function () {
            loaded();
            alert('请求超时或系统出错');
        }
    });
    //加载角色下属所有成员信息
    var r = Math.random().toString().substring(5);
    $.ajax({
        url: CTX + 'organizationManage/query-sysuser-by-role?Mathnum=' + r,
        method: 'GET',
        dataType: 'json',
        data: {roleId:roleId, lesseeId:lesseeId},
        success: function (json) {
            var roleSysuserList = json.roleSystemUserList;
            for(var i = 0; i < roleSysuserList.length; i++){
                var roleSysuserInfo = roleSysuserList[i].split(",");
                roleSysuser.push(roleSysuserInfo[0]);
                //展示框初始化
                addRolwSysuserShow(roleSysuserInfo[0], roleSysuserInfo[1]);
            }
            //初始化用户表格
            var orgTableInit = new RoleSysuserOrgTableInit();
            orgTableInit.Init();
            //表格加载完成后初始化选中状态
            $('#role_edit_sysuser_orgusertable').on('load-success.bs.table', function (e, data) {
                for(var i = 0; i < roleSysuser.length; i++){
                    var index = -1;
                    $('#role_edit_sysuser_orgusertable tr').each(function(){
                        if($(this).attr('data-uniqueid')==roleSysuser[i]){
                            index = $(this).attr('data-index');
                        }
                    });
                    if(index!=-1){
                        $('#role_edit_sysuser_orgusertable').bootstrapTable('check',index);//根据下标index，修改选中状态
                    }
                }
            })
        },
        error: function () {
            loaded();
            alert('请求超时或系统出错');
        }
    });
}

var RoleSysuserOrgTableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#role_edit_sysuser_orgusertable').bootstrapTable({
            url: CTX + 'organizationManage/query-sub-sysuser',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            // toolbar: '#toolbar',                //工具按钮用哪个容器
            //toolbarAlign:'left',				//工具栏位置
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            queryParamsType: "a",
            search: true,                       //是否显示表格搜索
            strictSearch: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: $(window).height()-80,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            columns: [
                 {
                    field: 'name',
                    title: '姓名',
                    sortable: false
                },
                {
                    checkbox: true
                }
            ],
            onCheckAll:function(rows){
                for(var i = 0; i < rows.length; i++){
                    var row = rows[i];
                    addRoleSysuserByRow(row);
                }
            },
            onUncheckAll:function(rows){
                for(var i = 0; i < rows.length; i++){
                    var row = rows[i];
                    removeRoleSysuserByRow(row);
                }
            },
            onCheck:function(row){
                addRoleSysuserByRow(row)
            },
            onUncheck:function(row){
                removeRoleSysuserByRow(row);
            }
        });
        $('#role_edit_sysuser_orgusertable .search input').attr('placeholder', '姓名');
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var data = JSON.stringify($('#roleSysuserOrgFormSearch').serializeJSON());
        var temp = {   //这里的键的名字和控制器的变量名必须一致，这边改动，控制器也需要改成一样的
            pageNum: params.pageNumber,//页码params.offset
            size: params.pageSize,//页面大小params.limit
            order: params.sortOrder,//排序 params.order
            ordername: params.sortName,//排序字段 params.sort
            from: data,
            search: params.searchText
        };
        return temp;
    };
    return oTableInit;
}

function goSaveRoleSysuser(_this) {
    var roleId = $('#role_sysuser_roleid').val();
    var sysuserIds = "";
    for(var i = 0; i < roleSysuser.length; i++){
        sysuserIds = sysuserIds + roleSysuser[i] + ",";
    }
    $(_this).attr('disabled', 'disabled');
    $(_this).text('保存中...');
    $.ajax({
        url: CTX + 'organizationManage/save-role-sysuser',
        method: 'post',
        dataType: 'json',
        data: {roleId:roleId, systemUserIds:sysuserIds},
        success: function (json) {
            alert(json.msg);
            if (json.success) {
                $('#role_edit_sysuser_modalwin').modal('toggle');
                resetRoleSysuserForm();
                $('#roleTableContent').bootstrapTable("refresh");
            }
            $(_this).removeAttr('disabled');
            $(_this).text('保存');
        },
        error: function () {
            alert('请求超时或系统出错!');
            $(_this).removeAttr('disabled');
            $(_this).text('保存');
        }
    });
}

function resetRoleSysuserForm() {
    //所有选中取消选中
    var roleSysuserTemp = new Array();
    for(var i = 0; i < roleSysuser.length; i++){
        roleSysuserTemp.push(roleSysuser[i]);
    }
    for(var i = 0; i < roleSysuserTemp.length; i++){
        var index = -1;
        $('#role_edit_sysuser_orgusertable tr').each(function(){
            if($(this).attr('data-uniqueid')==roleSysuserTemp[i]){
                index = $(this).attr('data-index');
            }
        });
        if(index!=-1){
            $('#role_edit_sysuser_orgusertable').bootstrapTable('uncheck',index);//根据下标index，修改选中状态
        }
    }
    //角色成员数组清空
    roleSysuser.splice(0, roleSysuser.length);
    //角色成员展示框清空
    $('#role_edit_sysuser_user_name').html('');
    //清空表格数据
    $('#role_edit_sysuser_useunit').val('')
    $('#role_edit_sysuser_orgusertable').bootstrapTable('refresh');
}

function addRoleSysuserByRow(row) {
    var index = $.inArray(row.id, roleSysuser);
    if(index == -1){
        roleSysuser.push(row.id);
        addRolwSysuserShow(row.id, row.name);
    }
}

function addRolwSysuserShow(id, name) {
    var htmlStr = '';
    htmlStr +=
        '<div id="selected_role_edit_sysuser_' + id + '" class="allRoleSysuser" style="background-color:#3daae8;color:white;width:auto;hight:60px;float:left;margin:5px;">' +
        '	<input type="hidden" name="sysuserid" class="sysuserid" value="' + id + '"/>' +
        '   <spand style="padding-left: 5px;padding-right: 5px;">' + name + '</spand>' +
        '   <input type="button" class="closeDiv" onclick="removeRoleSysuser(\'' + id + '\', \'' + name + '\')" value="X" style="background:transparent;border:none;"/>' +
        '</div>';
    $("#role_edit_sysuser_user_name").append(htmlStr);
}

function removeRoleSysuserByRow(row) {
    var index = $.inArray(row.id, roleSysuser);
    roleSysuser.splice(index, 1);
    //从展示框中移除
    $('#selected_role_edit_sysuser_' + row.id).remove();
}

function removeRoleSysuser(id, name) {
    //修改表格的复选框状态
    var index = -1;
    $('#role_edit_sysuser_orgusertable tr').each(function(){
        if($(this).attr('data-uniqueid')==id){
            index = $(this).attr('data-index');
        }
    });
    if(index!=-1){
        $('#role_edit_sysuser_orgusertable').bootstrapTable('uncheck',index);//根据下标index，修改选中状态
    }else{
        var index = $.inArray(id, roleSysuser);
        roleSysuser.splice(index, 1);
        //从展示框中移除
        $('#selected_role_edit_sysuser_' + id).remove();
    }
}

//启用、禁用成员操作
function goAllowSysuser(id, isValid) {
	var r = Math.random().toString().substring(5);
    $.ajax({
        url: CTX + 'organizationManage/allow-sysuser?Mathnum=' + r,
        method: 'GET',
        dataType: 'json',
        data:{sysuserId:id, isValid:isValid},
        success: function (json) {
            alert(json.msg);
            if(json.success){
                //更新单行数据
                var sysuserStatus;
                if(isValid == 'true'){
                    sysuserStatus = false;
                }else{
                    sysuserStatus = true;
                }
                $('#orgTableContent').bootstrapTable('updateByUniqueId', {
                    id: id,
                    row: {
                        isValid:sysuserStatus
                    }
                });
            }
        },
        error: function () {
            loaded();
            alert('请求超时或系统出错');
        }
    });
}

//批量禁用成员操作
function disableSysuser() {
    var seldatas = $('#orgTableContent').bootstrapTable('getSelections');
    if(seldatas.length<=0){
        alert('请选择一项来删除');
        return;
    }
    var sysuserIds = new Array();
    for(var i = 0; i < seldatas.length; i++){
        sysuserIds.push(seldatas[i].id);
    }
    $.ajax({
        type: "post",
        url: CTX + "/organizationManage/disable-sysuer",
        data: {sysuserIds:sysuserIds},
        dataType: 'JSON',
        success: function (data, status) {
            if (data.success) {
                alert(data.msg);
                $('#orgTableContent').bootstrapTable('refresh');
            }else{
                alert(data.msg);
            }
        },
        error: function () {
            alert('请求超时或系统出错');
        }
    });
}