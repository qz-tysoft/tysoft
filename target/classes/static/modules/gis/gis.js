var map = null;//map对象
var ProjectInfoMarkers = [];
var VidiconMarkers = [];
$(function(){
	//初始化地图
	initMyMap();
	//初始化左边pannel
	initWestPanel();
	
	//初始项目总览数据
	$('#view_panel1').attr('url',CTX+'gis/sub-page?page=project-info-all');
	$.get(CTX+'gis/sub-page?page=project-info-all',function(data){
		$('#view_panel1').html(data);
	});
	
	
	//初始化通用frame弹窗关闭事件
	$('#common_frame_win').on('hidden.bs.modal',function(){
		$('#common_frame_win iframe').removeAttr('src');
		//还原窗口大小
		$('#common_frame_win').css({'overflow-y':'auto'});
		$('#common_frame_win').find('.modal-dialog').css({width:oldWinW+'px',margin:'8px auto'});
		$('#common_frame_win').find('.modal-body').css({height:oldWinH+'px'});
		$('#common_frame_win .max-btn').find('i').removeClass('fa-clone');
		$('#common_frame_win .max-btn').find('i').addClass('fa-square-o');
	});
	$('#common_frame_win').on('show.bs.modal',function(){
		if(oldWinH>($(window).height()-80)){
			oldWinH = $(window).height()-80;
			$('#common_frame_win .modal-body').height(oldWinH);
		}
	});
	$('#common_frame_win').find('iframe').on('load',function(){
		$('#common_frame_win .modal-title').html($(this).contents().attr("title"));
	});
	
	//初始化大华视频播放窗口关闭事件
	$('#dhvideo_win').on('hidden.bs.modal',function(){
		DPSDK_StopRealplayByWndNo();
	});
});
/**
 * 初始化地图
 * @returns
 */
function initMyMap(){
	var cityStr = null;
	var	pcity = PCITY!=null?PCITY.split(','):null;
	if(pcity!=null){
		if(pcity.length==1){
			cityStr = pcity[0];
		}
		if(pcity.length==2){
			cityStr = pcity[1];
		}
		if(pcity.length==3){
			cityStr = pcity[2];
		}
	}
	var toolBar = new AMap.ToolBar({
	    visible: false
	});
	var mapStyle = $.cookie('mapStyle')==null?'mormal':$.cookie('mapStyle');
	$(":radio[name='mapStyle'][value='" + mapStyle + "']").prop("checked", "checked");
	map = new AMap.Map('allmap', {
        resizeEnable: true,
        mapStyle: 'amap://styles/'+mapStyle
    });
	if(cityStr!=null){
		map.setCity(cityStr);
	}
	map.addControl(toolBar);
	map.on('zoomend', function(e) {
		var zoom = map.getZoom();
		if(ProjectInfoMarkers.length>0){
			$.each(ProjectInfoMarkers,function(i,marker){
				var label = marker.getLabel();
				if(label!=undefined&&label!=null&&label.type=='projectInfoLabel'){
					if(zoom<16){
						label.content = label.content.replace('display:block','display:none');
						label.offset.y = -28;
					}else{
						label.content = label.content.replace('display:none','display:block');
						label.offset.y = -50;
					}
					marker.setLabel(label);
				}
			});
		}
		if(VidiconMarkers.length>0){
			map.remove(VidiconMarkers);
		}
		if(zoom>=16){
			distanceVidiconList();
		}
    });
	//地图拖拽结束事件
	map.on('dragend',function(e){
		var zoom = map.getZoom();
		if(VidiconMarkers.length>0){
			map.remove(VidiconMarkers);
		}
		if(zoom>=16){
			distanceVidiconList();
		}
	});
	//初始化用户项目地图展示
	fnQueryUserProjectInfos();
}
/**
 * 刷新地图样式
 * @param enName
 * @returns
 */
function refresh(enName){
	map.setMapStyle('amap://styles/'+enName);
	$.cookie('mapStyle', enName);   
	document.cookie="mapStyle="+enName;
}


$(window).resize(function () {
	resizeWestPanel();
});
/**
 * 重构左边panel高度
 * @returns
 */
function resizeWestPanel(){
	$('#west-panel-box .box-body').height($('#west-panel-box').height()-42);
	$('#west_tab .tab-content').height($('#west_tab').height()-32);
}
/**
 * 初始化左边数据展示panel
 * @returns
 */
function initWestPanel(){
	resizeWestPanel();
	$('#west-panel-box .btn-box-tool').on('click',function(){
		if($(this).attr('title')=='刷新'){
			reloadPanel();
		}
		if($(this).attr('title')=='关闭'){
			$('#west-panel').animate({left: '-'+$('#west-panel').width()+'px'}, "fast",function(){
				$('#west-panel-tiny').show();
			});
		}
	});
	$('#west-panel-tiny').on('click',function(){
		$('#west-panel').animate({left: '0px'}, "fast",function(){
			$('#west-panel-tiny').hide();
		});
	});
	//返回总览按钮事件
	$('.rel-back').on('click',function(){
		var curPanel = null;
		$('#west-panel-body .sub-page').each(function(){
			if(!$(this).is(":hidden")){
				curPanel = $(this);
			}
		});	
		curPanel.animate({left: $('#west_tab').width()+'px'}, "fast",function(){
			curPanel.hide();
			if(curPanel.attr('id')!='west_tab'){
				curPanel.empty();
			}
		});
		var backbtn = $(this);
		if(backbtn.find('span').html()=='总览'){
			$('#west_tab').show();
			$('#west_tab').animate({right:'0px'},'fast',function(){
				$('#west-box-header .box-title').html('项目总览');
				$('#west-box-header .pull-left').hide();
				$('#west-box-header .pull-right button:first').show();
			});
			map.setZoom(12);
		}
	});
}
/**
 * 刷新pannel
 * @returns
 */
function reloadPanel(){
	var curPanel = null;
	$('#west-panel-body .sub-page').each(function(){
		if(!$(this).is(":hidden")){
			curPanel = $(this);
		}
	});	
	if(curPanel!=null){
		var tabPanel = curPanel.find(".tab-content .active");
		var tabPanelUrl = tabPanel.attr('url');
		if(tabPanelUrl!=undefined&&tabPanelUrl!=null){
			tabPanel.find('.mCSB_container').empty();
			$.get(tabPanelUrl+'&timetemp='+Date.parse(new Date()),function(data){
				tabPanel.find('.mCSB_container').html(data);
			});
		}else{
			tabPanel = curPanel.find('#west_tab_projectinfo').find(".tab-content .active");
			tabPanelUrl = tabPanel.attr('url');
			if(tabPanelUrl!=undefined&&tabPanelUrl!=null){
				tabPanel.find('.mCSB_container').empty();
				$.get(tabPanelUrl+'&timetemp='+Date.parse(new Date()),function(data){
					tabPanel.find('.mCSB_container').html(data);
				});
			}
		}
	}
}

/**
 * 查询用户拥有的项目列表
 * @returns
 */
function fnQueryUserProjectInfos(){
	var r = Math.random().toString().substring(5);
	$.ajax({
		url: CTX+'gis/query-user-project-info?Mathnum=' + r,
		type: 'get',
		dataType: 'json',
		timeout: 10000,
		success:function(json){
			if(json){
				var projectInfoIds = '';
				$.each(json,function(i,data){
					var projectInfo = data.projectInfo;
					projectInfoIds += ','+projectInfo.id;
					var lnglat = projectInfo.lngLat.split(',');
					var strPX = calcStringPixelsCount(projectInfo.projectName,12);
					var labelStr = projectInfo.projectName+'<div style="text-align:center;margin-top:8px;display:none;">在职人数：'+data.personneNum+'&nbsp;&nbsp;在场人数：'+data.bepresentNum+'</div>';
					var marker = new AMap.Marker({
						icon: new AMap.Icon({            
				            image: "http://webapi.amap.com/theme/v1.3/markers/n/mark_rs.png"
				        }), 
						position: [Number(lnglat[0]),Number(lnglat[1])],
						label: {
							type: 'projectInfoLabel',
							content: labelStr,
							offset: new AMap.Pixel(-(strPX/2)+20,-28),//修改label相对于maker的位置
						}
					}).on('click', function() {
						fnShowMarkerDetail(marker,projectInfo);
				        fnShowProjectDetail(projectInfo);
				    });
					marker.setMap(map);
					ProjectInfoMarkers.push(marker);
					
				});
				initVidiconList(projectInfoIds.substring(1));
			}
		}
	});
}

/**
 * 加载项目摄像机列表
 * @param projectInfoIds
 * @returns
 */
var VidiconList_All = null;
function initVidiconList(projectInfoIds){
	var r = Math.random().toString().substring(5);
	$.ajax({
		url: CTX+'gis/query-vidicon?Mathnum=' + r,
		type: 'get',
		dataType: 'json',
		data: {projectInfoIds:projectInfoIds},
		success: function(json){
			if(json&&json.length>0){
				VidiconList_All = json;
			}
		}
	});
}


/**
 * 查找中心点500米范围内的摄像头列表
 * @returns
 */
function distanceVidiconList(){
	var centerLatlng = map.getCenter();
	var lat1 = centerLatlng.getLat();
	var lng1 = centerLatlng.getLng();
	VidiconMarkers = [];
	if(VidiconList_All!=null){
		$.each(VidiconList_All,function(i,video){
			if(video.lngLat!=null){
				var lngLat = video.lngLat.split(',');
				//查找中心点500米范围内的摄像头
        		if(fnDistance(lat1,lng1,Number(lngLat[1]),Number(lngLat[0]))<=500){
        			var icon = STATIC+'images/gis/camara-qiang.png';
        			if(video.vidiconType==2){
        				icon = STATIC+'images/gis/camara-qiu.png';
        			}
        			var marker = new AMap.Marker({
        				map: map,
        				icon: new AMap.Icon({            
        		            size: new AMap.Size(50, 50),  //图标大小
        		            image: icon,
        		            imageOffset: new AMap.Pixel(0, 0)
        		        }),
						position: [Number(lngLat[0]),Number(lngLat[1])]
					}).on('mouseover',function(){
						showVideoInfobox('show',marker,video);
					}).on('mouseout',function(){
						showVideoInfobox('hide',marker,video);
					}).on('click',function(){
						openVidiconWin(video);
					});
        			VidiconMarkers.push(marker);
        		}
			}
		});
	}
}

var videolist_infobox = null;
function showVideoInfobox(type,marker,video){
	var title = video.name,content = [];
	content.push("<img src='"+STATIC+"capture/"+video.vidiconId+"_capture.jpg' onerror=\"this.src='"+STATIC+"images/noimage.jpg';\"/>");
	if(videolist_infobox==null){
		 //实例化信息窗体
		videolist_infobox = new AMap.InfoWindow({
	        isCustom: true,  //使用自定义窗体
	        content: createInfoWindow(title, content),
	        offset: new AMap.Pixel(0, -35)
	    });
	}else{
		videolist_infobox.setContent(createInfoWindow(title, content));
	}
	if(type=='show'){
		videolist_infobox.open(map, marker.getPosition());
	}else{
		map.clearInfoWindow();
	}
}

//构建自定义信息窗体
function createInfoWindow(title, content) {
    var info = document.createElement("div");
    info.className = "info";
    //可以通过下面的方式修改自定义窗体的宽高
    info.style.width = "320px";
    // 定义顶部标题
    var top = document.createElement("div");
    var titleD = document.createElement("div");
    top.className = "info-top";
    titleD.innerHTML = title;
    top.appendChild(titleD);
    info.appendChild(top);
    // 定义中部内容
    var middle = document.createElement("div");
    middle.className = "info-middle";
    middle.innerHTML = content;
    info.appendChild(middle);
    return info;
}

/**
 * 摄像机打开视频窗口
 * @param video
 * @returns
 */
function openVidiconWin(vidicon){
	$('#dhvideo_win').modal('toggle');
	$('#dhvideo_win .modal-title').html(vidicon.name+'-实时视频');
	window.setTimeout(function(){
		if(!initDHOcxHandle){
			DPSDK_Init();
		}	
		if(!initDHOcxHandle){
			$('#dhvideo_win').modal('toggle');
			return;
		}
		var playocx = document.getElementById("DPSDK_OCX");
		//如果未登录平台则先登录平台
		if(DH_LoginHandle!=0){
			DH_SmartWndId = playocx.DPSDK_CreateSmartWnd(0, 0, 100, 100);
			playocx.DPSDK_SetWndCount(DH_SmartWndId,1);//设置单个窗口
			playocx.DPSDK_SetSelWnd(DH_SmartWndId,0);	//选中一个窗口
			playocx.DPSDK_SetWndStyle(1);				//窗口定制风格
			if(DH_serverIP==null){
				DH_serverIP = vidicon.serverOutIp;
				DPSDK_Login(DH_serverIP,vidicon.serverOutPort,vidicon.userName,vidicon.userPwd);//server.extranetipaddress外网  server.userpwd
			}
		}
		//如果已登录平台且服务平台不一样则重新登录
		else if(DH_serverIP != vidicon.serverOutIp){
			DPSDK_Logout();
			DH_serverIP = vidicon.serverOutIp;
			DPSDK_Login(DH_serverIP,vidicon.serverOutPort,vidicon.userName,vidicon.userPwd);//server.extranetipaddress外网  server.userpwd
		}
		if(DH_LoginHandle!=0){return;}
		DPSDK_PlayVideo(vidicon.vidiconId);
	},500);
}

function fnShowMarkerDetail(marker,projectInfo){
	map.setZoomAndCenter(18,marker.getPosition());
}

function fnShowProjectDetail(projectInfo){
	if(!$('#west-panel-tiny').is(":hidden")){
		$('#west-panel').animate({left: '0px'}, "fast",function(){
			$('#west-panel-tiny').hide();
		});
	}
	var curPanel = null;
	$('#west-panel-body .sub-page').each(function(){
		if(!$(this).is(":hidden")){
			curPanel = $(this);
		}
	});	
	if(curPanel.attr('id')!='detail_panel'){
		curPanel.animate({right: $('#west_tab').width()+'px'}, "fast",function(){
			if(curPanel.attr('id')!='west_tab'){
				curPanel.empty();
			}
			curPanel.hide();
		});
		$('#detail_panel').css({left:$('#west_tab').width()+'px'});
		$('#detail_panel').show();
		$('#detail_panel').animate({left: '0px'}, "fast",function(){
			$('#west-box-header .box-title').html(projectInfo.projectName);
			$('#west-box-header .pull-left').show();
			$.get(CTX+'gis/sub-page?page=project-info-detail&projectInfoId='+projectInfo.id,function(data){
				$('#detail_panel').html(data);
			});
		});
	}else{
		$('#west-box-header .box-title').html(projectInfo.projectName);
		$('#west-box-header .pull-left').show();
		$.get(CTX+'gis/sub-page?page=project-info-detail&projectInfoId='+projectInfo.id,function(data){
			$('#detail_panel').html(data);
		});
	}
}

var COMMON_CALLBACK = null;
function openCommonFrameWin(url,width,height,callback){
	if(width!=null){
		oldWinW = width;
	}
	if(height!=null){
		oldWinH = height;
	}
	$('#common_frame_win .modal-dialog').width(oldWinW);
	$('#common_frame_win .modal-body').height(oldWinH);
	$('#common_frame_win').modal('toggle');
	window.setTimeout(function(){
		$('#common_frame_win iframe').attr('src',url);	
	},300);
	COMMON_CALLBACK = callback;
}
/**
 * 最大最小化modal窗口
 * @param _this
 * @returns
 */
var oldWinW = window.screen.availWidth-100,oldWinH = window.screen.availHeight-100;
function fnMaxModalWin(_this){
	if($(_this).find('i').hasClass('fa-square-o')){
		//执行最大化窗口
		$(_this).parent().parent().parent().parent().css({'overflow-y':'hidden'});
		$(_this).parent().parent().parent().css({width:$(window).width()+'px',margin:'0px auto'});
		$(_this).parent().next().css({height:($(window).height()-$(_this).parent().outerHeight())+'px'});
		$(_this).find('i').removeClass('fa-square-o');
		$(_this).find('i').addClass('fa-clone');
	}else{
		//还原窗口大小
		$(_this).parent().parent().parent().parent().css({'overflow-y':'auto'});
		$(_this).parent().parent().parent().css({width:oldWinW+'px',margin:'8px auto'});
		$(_this).parent().next().css({height:oldWinH+'px'});
		$(_this).find('i').removeClass('fa-clone');
		$(_this).find('i').addClass('fa-square-o');
	}
}

/**
 * 字符长度计算像素
 * @param str
 * @param strFontSize
 * @returns
 */
function calcStringPixelsCount(str, strFontSize) {
    // 字符串字符个数
    var stringCharsCount = str.length;
    // 字符串像素个数
    var stringPixelsCount = 0;
    // JS 创建HTML元素：span
    var elementPixelsLengthRuler = document.createElement("span");
    elementPixelsLengthRuler.style.fontSize = strFontSize;  // 设置span的fontsize
    elementPixelsLengthRuler.style.visibility = "hidden";  // 设置span不可见
    elementPixelsLengthRuler.style.display = "inline-block";
    elementPixelsLengthRuler.style.wordBreak = "break-all !important";  // 打断单词
    // 添加span
    document.body.appendChild(elementPixelsLengthRuler);
    for (var i =0; i < stringCharsCount; i++) {
        // 判断字符是否为空格，如果是用&nbsp;替代，原因如下：
        // 1）span计算单个空格字符（ ），其像素长度为0
        // 2）空格字符在字符串的开头或者结果，计算时会忽略字符串
        if (str[i] == " ") {
            elementPixelsLengthRuler.innerHTML = "&nbsp;";
        } else {
            elementPixelsLengthRuler.innerHTML = str[i];
        }

        stringPixelsCount += elementPixelsLengthRuler.offsetWidth;
    }
    return stringPixelsCount;
}
