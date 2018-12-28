var num_lastYearAccumu = ""; 
var num_thisYearPlan = ""; 
var num_thisMonthAmount = ""; 
var num_thisMonthAccumu = "";
var num_completionRate = "";
var num_initialBalance = ""; 
var num_currentRevenue = ""; 
var num_currentPayment = ""; 
var num_endBalance = "";

//清空数据
function cleanValue(){
	num_lastYearAccumu = ""; 
	num_thisYearPlan = ""; 
	num_thisMonthAmount = ""; 
	num_thisMonthAccumu = "";
	num_completionRate = "";
	num_initialBalance = ""; 
	num_currentRevenue = ""; 
	num_currentPayment = ""; 
	num_endBalance = "";
}

//清除表格
function cleanCompList_body(){
	var tbody = document.getElementById("compList_body");
	var children = tbody.childNodes;
	for(var i = children.length -1; i >= 0; i--){
		tbody.removeChild(children[i]);
	}
}

//绘制表格
function paintCompList_body(comps){
	cleanCompList_body();
	removeCompForm();
	var fillUseunit = $("#fillUseunit").val();
	var readonly = $("#readonly").val();
	var tBody = document.getElementById("compList_body");

	if(fillUseunit == '财务部'){
		for(var i=0,len=comps.length; i<len; i++){
			
			num_initialBalance = comps[i].comp.initialBalance==null || comps[i].comp.initialBalance=='null' ? "" : comps[i].comp.initialBalance; 
			num_currentRevenue = comps[i].comp.currentRevenue==null || comps[i].comp.currentRevenue=='null' ? "" : comps[i].comp.currentRevenue; 
			num_currentPayment = comps[i].comp.currentPayment==null || comps[i].comp.currentPayment=='null' ? "" : comps[i].comp.currentPayment; 
			num_endBalance = comps[i].comp.endBalance==null || comps[i].comp.endBalance=='null' ? "" : comps[i].comp.endBalance; 

			//添加表单
			addCompForm(comps[i]);
			
			var indicNos = comps[i].comp.indicator.indicNo.split(".");
			var blank = "";
			if(indicNos.length > 0){
				for(var j=1,noLen=indicNos.length; j<noLen; j++){
					blank += "&emsp;";
				}
			}

			var disabledStr = "";
			if(comps[i].disabled == 'disabled'){
				disabledStr = "disabled=\'disabled\' ";
			}

			if(comps[i].comp.indicator.indicName.indexOf("资产总额") > -1){
				var newRow = tBody.insertRow();
				var newCell0 = newRow.insertCell();
				newCell0.colSpan = 6;
				newCell0.innerHTML = "<span></span>";

				var newRow2 = tBody.insertRow();
				var newCell20 = newRow2.insertCell();
				var newCell21 = newRow2.insertCell();
				var newCell22 = newRow2.insertCell();
				var newCell23 = newRow2.insertCell();
				var newCell24 = newRow2.insertCell();

				newCell21.align = "center";
				newCell22.align = "center";
				newCell23.align = "center";
				newCell23.colSpan = '2';
				newCell24.align = "center";

				var year = $("#year").val();
				newCell20.innerHTML = "<span></span>";
				newCell21.innerHTML = "<span>单位</span>";
				newCell22.innerHTML = "<span>"+(year-1)+"年末</span>";
				newCell23.innerHTML = "<span>本月止净增减（增+，减-）</span>";
				newCell24.innerHTML = "<span>期末</span>";

				var newRow3 = tBody.insertRow();
				var newCell30 = newRow3.insertCell();
				var newCell31 = newRow3.insertCell();
				var newCell32 = newRow3.insertCell();
				var newCell33 = newRow3.insertCell();
				var newCell34 = newRow3.insertCell();

				newCell31.align = "center";
				newCell32.align = "center";
				newCell33.align = "center";
				newCell33.colSpan = '2';
				newCell34.align = "center";
				//指标名称
				newCell30.innerHTML = "<span id=\'indicNo_" + comps[i].comp.id + "\'>"+blank + comps[i].comp.indicator.indicNo + "&nbsp;" + comps[i].comp.indicator.indicName +"</span>";
				//单位
				newCell31.innerHTML = "<span id=\'unit_"+ comps[i].comp.id + "\'>"+ comps[i].comp.indicator.unit +"</span>";

				if(readonly == "readonly" || comps[i].comp.indicator.indicNo.indexOf(".") < 0){
					//年末
					newCell32.innerHTML = "<span id='initialBalance_"+ comps[i].comp.id +"'>" + num_initialBalance + "</span>";
					//本月止净增减
					newCell33.innerHTML = "<span id='currentRevenue_"+ comps[i].comp.id +"'>" + num_currentRevenue + "</span>";
					//期末
					newCell34.innerHTML = "<span id='endBalance"+ comps[i].comp.id +"'>" + num_endBalance + "</span>";

				}else{
					//年末
					newCell32.innerHTML = "<input "+disabledStr+"class=\"form-control\" style=\"width: 120px;\" id=\"initialBalance_"+ comps[i].comp.id 
					+"\" value=\""+ num_initialBalance + "\" onchange=\"fnSetValue(\'"+comps[i].comp.indicator.unit+"\',this)\" />";
					//本月止净增减
					newCell33.innerHTML = "<input "+disabledStr+"class=\"form-control\" style=\"width: 120px;\" id=\"currentRevenue_"+ comps[i].comp.id 
					+"\" value=\""+ num_currentRevenue + "\" onchange=\"fnCurrentRevenue(this,"+comps[i].isAutoCollect+",\'"+comps[i].parentId+"\')\" />";
					//期末
					newCell34.innerHTML = "<input "+disabledStr+"class=\"form-control\" style=\"width: 120px;\" id=\"endBalance_"+ comps[i].comp.id 
					+"\" value=\""+ num_endBalance + "\" onchange=\"fnSetValue(\'"+comps[i].comp.indicator.unit+"\',this,\'\',"+comps[i].isAutoCollect+",\'"+comps[i].parentId+"\')\" />";
				}

			}else{
				var newRow = tBody.insertRow();
				var newCell0 = newRow.insertCell();
				var newCell1 = newRow.insertCell();
				var newCell2 = newRow.insertCell();
				var newCell3 = newRow.insertCell();
				var newCell4 = newRow.insertCell();
				var newCell5 = newRow.insertCell();

				newCell1.align = "center";
				newCell2.align = "center";
				newCell3.align = "center";
				newCell4.align = "center";
				newCell5.align = "center";
				//指标名称
				newCell0.innerHTML = "<span id=\'indicNo_" + comps[i].comp.id + "\'>"+blank + comps[i].comp.indicator.indicNo + "&nbsp;" + comps[i].comp.indicator.indicName +"</span>";
				//单位
				newCell1.innerHTML = "<span id=\'unit_"+ comps[i].comp.id + "\'>"+ comps[i].comp.indicator.unit +"</span>";

				if(readonly == "readonly" || comps[i].comp.indicator.indicNo.indexOf(".") < 0){
					//期初余额
					newCell2.innerHTML = "<span id='initialBalance_"+ comps[i].comp.id +"'>" + num_initialBalance + "</span>";
					//本期收入
					newCell3.innerHTML = "<span id='currentRevenue_"+ comps[i].comp.id +"'>" + num_currentRevenue + "</span>";
					//本期支付
					newCell4.innerHTML = "<span id='currentPayment_"+ comps[i].comp.id +"'>" + num_currentPayment + "</span>";
					//期末余额
					newCell5.innerHTML = "<span id='endBalance_"+ comps[i].comp.id +"'>" + num_endBalance + "</span>";

				}else{
					//期初余额
					newCell2.innerHTML = "<input "+disabledStr+"class=\"form-control\" style=\"width: 120px;\" id=\"initialBalance_"+ comps[i].comp.id 
					+"\" value=\""+ num_initialBalance + "\" onchange=\"fnSetValue(\'"+comps[i].comp.indicator.unit+"\',this,\'\',"+comps[i].isAutoCollect+",\'"+comps[i].parentId+"\')\" />";
					//本期收入
					newCell3.innerHTML = "<input "+disabledStr+"class=\"form-control\" style=\"width: 120px;\" id=\"currentRevenue_"+ comps[i].comp.id 
					+"\" value=\""+ num_currentRevenue + "\" onchange=\"fnSetValue(\'"+comps[i].comp.indicator.unit+"\',this,\'\',"+comps[i].isAutoCollect+",\'"+comps[i].parentId+"\')\" />";
					//本期支付
					newCell4.innerHTML = "<input "+disabledStr+"class=\"form-control\" style=\"width: 120px;\" id=\"currentPayment_"+ comps[i].comp.id 
					+"\" value=\""+ num_currentPayment + "\" onchange=\"fnSetValue(\'"+comps[i].comp.indicator.unit+"\',this,\'\',"+comps[i].isAutoCollect+",\'"+comps[i].parentId+"\')\" />";
					//期末余额
					newCell5.innerHTML = "<input "+disabledStr+"class=\"form-control\" style=\"width: 120px;\" id=\"endBalance_"+ comps[i].comp.id 
					+"\" value=\""+ num_endBalance + "\" onchange=\"fnSetValue(\'"+comps[i].comp.indicator.unit+"\',this,\'\',"+comps[i].isAutoCollect+",\'"+comps[i].parentId+"\')\" />";
				}
			}
			
			cleanValue();
		}
	}else{//非财务指标
		for(var i=0,len=comps.length; i<len; i++){
			num_lastYearAccumu = comps[i].comp.lastYearAccumu==null || comps[i].comp.lastYearAccumu=='null' ? "" : comps[i].comp.lastYearAccumu; 
			num_thisYearPlan = comps[i].comp.thisYearPlan==null || comps[i].comp.thisYearPlan=='null' ? "" : comps[i].comp.thisYearPlan; 
			num_thisMonthAmount = comps[i].comp.thisMonthAmount==null || comps[i].comp.thisMonthAmount=='null' ? "" : comps[i].comp.thisMonthAmount; 
			num_thisMonthAccumu = comps[i].comp.thisMonthAccumu==null || comps[i].comp.thisMonthAccumu=='null' ? "" : comps[i].comp.thisMonthAccumu;
			num_completionRate = comps[i].comp.completionRate==null || comps[i].comp.completionRate=='null' ? "" : comps[i].comp.completionRate;
			
			//添加表单
			addCompForm(comps[i]);
			
			var disabledStr = "";
			if(comps[i].disabled == 'disabled'){
				disabledStr = "disabled=\'disabled\' ";
			}
			
			var rateDisable = "";
			if(comps[i].comp.indicator.unit != '%'){
				rateDisable = "disabled=\'disabled\' ";
			}
			
			var newRow = tBody.insertRow();
			var newCell0 = newRow.insertCell();
			var newCell1 = newRow.insertCell();
			var newCell2 = newRow.insertCell();
			var newCell3 = newRow.insertCell();
			var newCell4 = newRow.insertCell();
			var newCell5 = newRow.insertCell();
			var newCell6 = newRow.insertCell();

			newCell1.align = "center";
			newCell2.align = "center";
			newCell3.align = "center";
			newCell4.align = "center";
			newCell5.align = "center";
			newCell6.align = "center";

			var indicNos = comps[i].comp.indicator.indicNo.split(".");
			var blank = "";
			if(indicNos.length > 0){
				for(var j=1,noLen=indicNos.length; j<noLen; j++){
					blank += "&emsp;";
				}
			}

			//指标名称
			newCell0.innerHTML = "<span id=\'indicNo_" + comps[i].comp.id + "\'>"+blank + comps[i].comp.indicator.indicNo + "&nbsp;" + comps[i].comp.indicator.indicName +"</span>";
			//单位
			newCell1.innerHTML = "<span id=\'unit_"+ comps[i].comp.id + "\'>"+ comps[i].comp.indicator.unit +"</span>";

			if(readonly == "readonly" || comps[i].comp.indicator.indicNo.indexOf(".") < 0){
				//上一年同期累计
				newCell2.innerHTML = "<span id='lastYearAccumu_"+ comps[i].comp.id +"'>" + num_lastYearAccumu + "</span>";
				//今年计划
				newCell3.innerHTML = "<span id='thisYearPlan_"+ comps[i].comp.id +"'>" + num_thisYearPlan + "</span>";
				//本月发生
				newCell4.innerHTML = "<span id='thisMonthAmount_"+ comps[i].comp.id +"'>" + num_thisMonthAmount + "</span>";
				//本月止年累
				newCell5.innerHTML = "<span id='thisMonthAccumu_"+ comps[i].comp.id +"'>" + num_thisMonthAccumu + "</span>";
				//计划完成率
				newCell6.innerHTML = "<span id='completionRate_"+ comps[i].comp.id +"'>" + num_completionRate + "</span>";

			}else{
				//上一年同期累计
				newCell2.innerHTML = "<input "+disabledStr+"class=\"form-control\" style=\"width: 120px;\" id=\"lastYearAccumu_"+ comps[i].comp.id 
				+"\" value=\""+ num_lastYearAccumu + "\" onchange=\"fnSetValue(\'"+comps[i].comp.indicator.unit+"\',this)\" />";
				//今年计划
				newCell3.innerHTML = "<input "+disabledStr+"class=\"form-control\" style=\"width: 120px;\" id=\"thisYearPlan_"+ comps[i].comp.id 
				+"\" value=\""+ num_thisYearPlan + "\" onchange=\"fnSetValue(\'"+comps[i].comp.indicator.unit+"\',this,\'\',"+comps[i].isAutoCollect+",\'"+comps[i].parentId+"\')\" />";
				//本月发生
				newCell4.innerHTML = "<input "+disabledStr+"class=\"form-control\" style=\"width: 120px;\" id=\"thisMonthAmount_"+ comps[i].comp.id 
				+"\" value=\""+ num_thisMonthAmount + "\" onchange=\"fnSetValue(\'"+comps[i].comp.indicator.unit+"\',this,\'"+comps[i].last_thisMonthAccumu+"\',"+comps[i].isAutoCollect+",\'"+comps[i].parentId+"\')\" />";
				//本月止年累
				newCell5.innerHTML = "<input "+disabledStr+"class=\"form-control\" style=\"width: 120px;\" id=\"thisMonthAccumu_"+ comps[i].comp.id 
				+"\" value=\""+ num_thisMonthAccumu + "\" onchange=\"fnSetValue(\'"+comps[i].comp.indicator.unit+"\',this,\'\',"+comps[i].isAutoCollect+",\'"+comps[i].parentId+"\')\" />";
				//计划完成率
				newCell6.innerHTML = "<input "+rateDisable+"class=\"form-control\" style=\"width: 120px;\" id=\"completionRate_"+ comps[i].comp.id 
				+"\" value=\""+ num_completionRate + "\" onchange=\"fnSetValue(\'"+comps[i].comp.indicator.unit+"\',this)\" />";
			}
			cleanValue();
		}
	}
	loaded();
}

//移除表单
function removeCompForm(){
	$(".compForm").remove();
}

//绘制表单
function addCompForm(obj){
	var formDiv = document.getElementById("compFormDiv");
	
	var compForm = document.createElement("form");
	compForm.id = "myform_comp_" + obj.comp.id;
	compForm.className = "form-horizontal compForm";
	
	var parentId = document.createElement("input");
	parentId.setAttribute("id","parentId_" + obj.comp.id);
	parentId.setAttribute("type","hidden");
	parentId.setAttribute("value",obj.parentId);
	compForm.appendChild(parentId);
	
	var idInput = document.createElement("input");
	idInput.setAttribute("name","id");
	idInput.setAttribute("type","hidden");
	idInput.setAttribute("value",obj.comp.id);
	compForm.appendChild(idInput);
	
	var reportMonth_Input = document.createElement("input");
	reportMonth_Input.setAttribute("name","reportMonth");
	reportMonth_Input.setAttribute("type","hidden");
	reportMonth_Input.setAttribute("value",obj.comp.reportMonth);
	compForm.appendChild(reportMonth_Input);

	var lastYearAccumu_Input = document.createElement("input");
	lastYearAccumu_Input.setAttribute("name","lastYearAccumu");
	lastYearAccumu_Input.setAttribute("type","hidden");
	lastYearAccumu_Input.setAttribute("value",num_lastYearAccumu);
	compForm.appendChild(lastYearAccumu_Input);

	var thisYearPlan_Input = document.createElement("input");
	thisYearPlan_Input.setAttribute("name","thisYearPlan");
	thisYearPlan_Input.setAttribute("type","hidden");
	thisYearPlan_Input.setAttribute("value",num_thisYearPlan);
	compForm.appendChild(thisYearPlan_Input);

	var thisMonthAmount_Input = document.createElement("input");
	thisMonthAmount_Input.setAttribute("name","thisMonthAmount");
	thisMonthAmount_Input.setAttribute("type","hidden");
	thisMonthAmount_Input.setAttribute("value",num_thisMonthAmount);
	compForm.appendChild(thisMonthAmount_Input);

	var thisMonthAccumu_Input = document.createElement("input");
	thisMonthAccumu_Input.setAttribute("name","thisMonthAccumu");
	thisMonthAccumu_Input.setAttribute("type","hidden");
	thisMonthAccumu_Input.setAttribute("value",num_thisMonthAccumu);
	compForm.appendChild(thisMonthAccumu_Input);

	var completionRate_Input = document.createElement("input");
	completionRate_Input.setAttribute("name","completionRate");
	completionRate_Input.setAttribute("type","hidden");
	completionRate_Input.setAttribute("value",num_completionRate);
	compForm.appendChild(completionRate_Input);
	
	var initialBalance_Input = document.createElement("input");
	initialBalance_Input.setAttribute("name","initialBalance");
	initialBalance_Input.setAttribute("type","hidden");
	initialBalance_Input.setAttribute("value",num_initialBalance);
	compForm.appendChild(initialBalance_Input);
	
	var currentRevenue_Input = document.createElement("input");
	currentRevenue_Input.setAttribute("name","currentRevenue");
	currentRevenue_Input.setAttribute("type","hidden");
	currentRevenue_Input.setAttribute("value",num_currentRevenue);
	compForm.appendChild(currentRevenue_Input);
	
	var currentPayment_Input = document.createElement("input");
	currentPayment_Input.setAttribute("name","currentPayment");
	currentPayment_Input.setAttribute("type","hidden");
	currentPayment_Input.setAttribute("value",num_currentPayment);
	compForm.appendChild(currentPayment_Input);

	var endBalance_Input = document.createElement("input");
	endBalance_Input.setAttribute("name","endBalance");
	endBalance_Input.setAttribute("type","hidden");
	endBalance_Input.setAttribute("value",num_endBalance);
	compForm.appendChild(endBalance_Input);
	
	var indicator_id_Input = document.createElement("input");
	indicator_id_Input.setAttribute("name","indicator.id");
	indicator_id_Input.setAttribute("type","hidden");
	indicator_id_Input.setAttribute("value",obj.comp.indicator.id);
	compForm.appendChild(indicator_id_Input);
	
	var version_Input = document.createElement("input");
	version_Input.setAttribute("name","version");
	version_Input.setAttribute("type","hidden");
	version_Input.setAttribute("value",obj.comp.version);
	compForm.appendChild(version_Input);
	
	document.body.appendChild(compForm);
}

//设置本月止净增减（增+，减-）
function fnCurrentRevenue(obj,isAutoCollect,parentId){
	var objId = obj.id;
	var value = obj.value;
	var index = objId.substring(objId.indexOf('_')+1);
	var idStr = objId.substring(0,objId.indexOf('_'));
	var pattern = /^(-?\d+)(\.\d+)?$/;
	var hasSymbol = false;
	if(value.indexOf('+') == 0){
		value = value.substring(1);
		hasSymbol = true;
	}
	if(value != '-' && value != '' && !pattern.test(value)){
		alert("输入的字符不符合规范！");
		$('#'+objId).val('');
		return;
	}
	if(hasSymbol){
		value = "+" + value;
	}
	
	$('#myform_comp_'+ index).find("input[name='"+idStr+"']").val(value);
	if(value == ''){
		value = '0';
	}
	var initialBalance = $('#initialBalance_'+index).val();
	var endBalance = parseFloat(initialBalance) + parseFloat(value);
	
	$('#endBalance_'+index).val(endBalance.toFixed(2));
	$('#myform_comp_'+ index).find("input[name='endBalance']").val(endBalance.toFixed(2));
	
	if(isAutoCollect){
		var parentForm = document.getElementById("myform_comp_" + parentId);
		if(parentForm != null){
			
		}
	}
}

//设值
function fnSetValue(unit,obj,last_thisMonthAccumu,isAutoCollect,parentId){
	var objId = obj.id;
	var value = obj.value;
	var index = objId.substring(objId.indexOf('_')+1);
	var idStr = objId.substring(0,objId.indexOf('_'));
	var pattern = /^(-?\d+)(\.\d+)?$/;
	var toSet = true;
	var hasSymbol = false;
	if(value.indexOf('+') == 0){
		value = value.substring(1);
		hasSymbol = true;
	}
	if(value.indexOf('%') > -1){
		var subValue = value.substring(0,value.indexOf('%'));
		if(!pattern.test(subValue)){
			toSet = false;
		}
	}else if(value != '-' && value != '' && !pattern.test(value)){
		toSet = false;
	}
	if(!toSet){
		alert("输入的字符不符合规范！");
		$('#'+objId).val('');
		return;
	}
	if(hasSymbol){
		value = "+" + value;
	}
	$('#myform_comp_'+ index).find("input[name='"+idStr+"']").val(value);
	if(unit != "%"){
		if(value == ''){
			value = '0';
		}
		if(idStr=='thisMonthAmount' && value != '-'){//本月发生,计算本月止年累
			//value = parseFloat(value) + parseFloat(last_thisMonthAccumu);
			value = parseFloat(value);
			if(last_thisMonthAccumu != null && last_thisMonthAccumu != 'null'){
				value = value + parseFloat(last_thisMonthAccumu);
			}
			
			$('#thisMonthAccumu_'+index).val(value);
			$('#myform_comp_'+ index).find("input[name='thisMonthAccumu']").val(value);
			fnSetAccuAndRate(index);
		}
		
		if(idStr=='thisMonthAccumu'||idStr=='thisYearPlan'){
			fnSetAccuAndRate(index);
		}
		
		//计算期末余额
		if(idStr=='currentPayment' || idStr=='initialBalance' || idStr == "currentRevenue"){
			var initialBalance = ($('#initialBalance_'+index).val()==""||$('#initialBalance_'+index).val()==" ") ? 0 : $('#initialBalance_'+index).val();
			var currentRevenue = ($('#currentRevenue_'+index).val()==""||$('#currentRevenue_'+index).val()==" ") ? 0 : $('#currentRevenue_'+index).val();
			var currentPayment_ = '0';
			var obj_payment = document.getElementById('currentPayment_'+index);
			if(obj_payment != null){
				currentPayment_ = ($('#currentPayment_'+index).val()==""||$('#currentPayment_'+index).val()==" ") ? 0 : $('#currentPayment_'+index).val();
			}
			var currentPayment = parseFloat(currentPayment_);
			var endBalance = parseFloat(initialBalance) + parseFloat(currentRevenue) - currentPayment;
			$('#endBalance_'+index).val(endBalance.toFixed(2));
			$('#myform_comp_'+ index).find("input[name='endBalance']").val(endBalance.toFixed(2));
		}
		
		//自动汇总
		if(isAutoCollect){
			//保存本指标的完成情况
			fnSaveIndicCompletion("myform_comp_"+index);
			
			var parentForm = document.getElementById("myform_comp_" + parentId);
			if(parentForm != null){
				var unit = document.getElementById("unit_" + parentId).innerText;
				if(unit != '%'){
					var parentIndicNo = document.getElementById("indicNo_" + parentId).innerText;
					console.log("parentIndicNo:" + parentIndicNo);
					//设置父级指标的值
					setTimeout(function(){
						queryParentIndicComplement(index,parentId);
					},100);
				}
			}
		}
	}
}

//设置本月止年累及完成率
function fnSetAccuAndRate(index){
	var plan = $('#thisYearPlan_'+ index).val();
	var value = $('#thisMonthAccumu_'+ index).val();
	if(plan && plan>0){
		if(value == '-'){
			$('#completionRate_'+index).val('-');
			$('#myform_comp_'+ index).find("input[name='completionRate']").val('-');
			return;
		}
		if(value == ''){
			$('#completionRate_'+index).val('');
			$('#myform_comp_'+ index).find("input[name='completionRate']").val('');
			return;
		}
		plan = parseFloat(plan);
		value = parseFloat(value);
		var rate = value/plan;
		rate = rate*100;
		var rateStr = rate.toFixed(3)+"%";
		$('#completionRate_'+index).val(rateStr);
		$('#myform_comp_'+ index).find("input[name='completionRate']").val(rate.toFixed(3)+"%");
	}
}

//设置父级指标完成情况
function queryParentIndicComplement(childId,parentId){
	var r = Math.random().toString().substring(5);
	$.ajax({
        url: CTX + 'indicator-completion/query-parent-completion?Mathnum=' + r,
        method: 'GET',
        dataType: 'json',
        data: {
        	childId:childId,
        	parentId:parentId,
        	opt:'audit',
        	reportId:$('#myform_report').find("input[name='id']").val()
        },
        async: false,
        success: function (json) {
        	refreshParentValue(json);
        },
        error: function () {
            loaded();
            alert('请求超时或系统出错');
        }
    });
}

//更新父级指标的完成情况
function refreshParentValue(comp){
	if(comp.indicator.indicNo.indexOf(".") > -1){//非顶级指标
		if(comp.indicator.fillDepartment == null || comp.indicator.fillDepartment.name.indexOf('财务') < 0){
			$("#lastYearAccumu_" + comp.id).val(comp.lastYearAccumu);
			$("#thisYearPlan_" + comp.id).val(comp.thisYearPlan);
			$("#thisMonthAmount_" + comp.id).val(comp.thisMonthAmount);
			$("#thisMonthAccumu_" + comp.id).val(comp.thisMonthAccumu);
			$("#completionRate_" + comp.id).val(comp.completionRate);
		}else{//财务指标
			$("#initialBalance_" + comp.id).val(comp.initialBalance);
			$("#currentRevenue_" + comp.id).val(comp.currentRevenue);
			if(comp.indicator.indicName.indexOf("资产总额") < 0){
				$("#currentPayment_" + comp.id).val(comp.currentPayment);
			}
			$("#endBalance_" + comp.id).val(comp.endBalance);
		}
	}else{//顶级指标
		if(comp.indicator.fillDepartment == null || comp.indicator.fillDepartment.name.indexOf('财务') < 0){
			$("#lastYearAccumu_" + comp.id).html(comp.lastYearAccumu);
			$("#thisYearPlan_" + comp.id).html(comp.thisYearPlan);
			$("#thisMonthAmount_" + comp.id).html(comp.thisMonthAmount);
			$("#thisMonthAccumu_" + comp.id).html(comp.thisMonthAccumu);
			$("#completionRate_" + comp.id).html(comp.completionRate);
		}else{//财务指标
			$("#initialBalance_" + comp.id).html(comp.initialBalance);
			$("#currentRevenue_" + comp.id).html(comp.currentRevenue);
			if(comp.indicator.indicName.indexOf("资产总额") < 0){
				$("#currentPayment_" + comp.id).html(comp.currentPayment);
			}
			$("#endBalance_" + comp.id).html(comp.endBalance);
		}
	}
	
	$('#myform_comp_'+ comp.id).find("input[name='id']").val(comp.id);
	$('#myform_comp_'+ comp.id).find("input[name='reportMonth']").val(comp.reportMonth);
	$('#myform_comp_'+ comp.id).find("input[name='lastYearAccumu']").val(comp.lastYearAccumu);
	$('#myform_comp_'+ comp.id).find("input[name='thisYearPlan']").val(comp.thisYearPlan);
	$('#myform_comp_'+ comp.id).find("input[name='thisMonthAmount']").val(comp.thisMonthAmount);
	$('#myform_comp_'+ comp.id).find("input[name='thisMonthAccumu']").val(comp.thisMonthAccumu);
	$('#myform_comp_'+ comp.id).find("input[name='completionRate']").val(comp.completionRate);
	$('#myform_comp_'+ comp.id).find("input[name='initialBalance']").val(comp.initialBalance);
	$('#myform_comp_'+ comp.id).find("input[name='currentRevenue']").val(comp.currentRevenue);
	$('#myform_comp_'+ comp.id).find("input[name='currentPayment']").val(comp.currentPayment);
	$('#myform_comp_'+ comp.id).find("input[name='endBalance']").val(comp.endBalance);
	$('#myform_comp_'+ comp.id).find("input[name='indicator.id']").val(comp.indicator.id);
	$('#myform_comp_'+ comp.id).find("input[name='version']").val(comp.version);
	
	//祖父级指标
	var parentId = $("#parentId_"+comp.id).val();
	if(parentId != null && parentId != ""){
		var parentForm = document.getElementById("myform_comp_" + parentId);
		if(parentForm != null){
			var unit = document.getElementById("unit_" + parentId).innerText;
			if(unit != '%'){
				var parentIndicNo = document.getElementById("indicNo_" + parentId).innerText;
				console.log("parentIndicNo:" + parentIndicNo);
				//设置父级指标的值
				queryParentIndicComplement(comp.id,parentId);
			}
		}
	}
	
}