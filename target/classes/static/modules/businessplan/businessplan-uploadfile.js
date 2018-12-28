var ANNEX_CALLBACK_BP = null;
//打开附件管理窗口
function openUploadAnnexWin(jsonData,callback){
	$('#upload_annex_win .modal-title').html('附件管理-'+jsonData.folderName);
	$('#upload_annex_win').modal('toggle');
	window.setTimeout(function(){
		var url = CTX+'annex/businessplan.html?folderId='+jsonData.folderId+'&annexIds='+jsonData.annexIds;
		$('#upload_annex_win iframe').attr('src',url);	
	},300);
	ANNEX_CALLBACK_BP = callback;
}

//附件子页面回调
function closeUploadAnnexWin(){
	$('#upload_annex_win').modal('toggle');
}

//附件管理弹窗关闭事件
$('#upload_annex_win').on('hidden.bs.modal',function(){
	var returnData = window.frames['annexFrame'].window.parentCallBack();;
	$('#upload_annex_win iframe').removeAttr('src');
	if(ANNEX_CALLBACK_BP){
		ANNEX_CALLBACK_BP(returnData);
	}
	ANNEX_CALLBACK_BP = null;
});

//打开图片查看窗口
function openCommonViewImgWin(annexIds){
	if(STATIC.indexOf(";") > -1){
		STATIC = STATIC.substring(0,STATIC.indexOf(";"));
	}
	console.log("STATIC:"+ STATIC);
	if(annexIds==null||annexIds==''){
		alert('附件不存在');
		return;
	}
	loading('加载中...');
	$.ajax({
		url:CTX+'annex/query-annex',
		type:'get',
		dataType:'json',
		timeout:10000,
		data: {annexIds:annexIds},
		success:function(json){
			loaded();
			if(json&&json.length>0){
				var liStr = '',imgStr = '';
				$.each(json,function(i,annex){
					if(annex.extendName.toLowerCase()=='jpg'||annex.extendName.toLowerCase()=='jpeg'||
							annex.extendName.toLowerCase()=='bmp'||annex.extendName.toLowerCase()=='gif'||
							annex.extendName.toLowerCase()=='png'){
						liStr += '<li data-target="#myCarousel" data-slide-to="'+i+'" '+(i==0?'class="active"':'')+'></li>';
						imgStr += 
							'<div class="item '+(i==0?'active':'')+'">'+
							'	<img src="'+STATIC+annex.relativePath+'" alt="'+annex.name+'" onerror="this.src=\''+STATIC+'images/noPicture.jpg\';" style="margin:0 auto;max-width:100%;"/>'+
							'	<div class="carousel-caption">'+annex.name+'</div>'+
							'</div>';
					}
				});
				if(liStr!=''&&imgStr!=''){
					$('#common_viewimg_win .modal-body').html(
							'<div id="myCarousel" class="carousel slide">'+
							'<ol class="carousel-indicators">'+
							liStr+
							'</ol>'+
							'<div class="carousel-inner">'+
							imgStr+
							'</div>'+
							'<a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">'+
							'    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>'+
							'    <span class="sr-only">Previous</span>'+
							'</a>'+
							'<a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">'+
							'    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>'+
							'    <span class="sr-only">Next</span>'+
							'</a>'+
							'</div>'
					);
					$('#common_viewimg_win').modal('toggle');
				}else{
					$.each(json,function(i,annex){
						window.location.href = STATIC+annex.relativePath;
					});
				}
			}else{
				$('#common_viewimg_win').modal('toggle');
				$('#common_viewimg_win .modal-body').html('<div class="img_div" style="width:100%; height:100%;text-align:center;"><img src="'+STATIC+'images/noPicture.jpg" style="max-width:100%;height:auto;"/></div>');
			}
		},
		error:function(){
			alert('请求超时或系统错误');
			loaded();
		}
	});
}