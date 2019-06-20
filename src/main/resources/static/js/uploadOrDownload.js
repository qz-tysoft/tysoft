/**
 * 
 */
//为空是默认上传的文件数量为5个,文件类型为file
var callBackAnnexId;
function openUploadView(fileNum,fileType){
	  var $ = layui.$;
	  var layer = layui.layer;
	  //先进行判断是否为空
	  if(fileNum==null){
		  fileNum=5;
	  }
	  if(fileType==null){
		  fileType='file';
	  }
	  var index=layer.open({
         type: 2
        ,title: '上传附件'
        ,content: '../annex/annexView?fileNum='+fileNum+'&amp;fileType='+fileType+''
        ,maxmin: true
        ,btn: ['确定','关了']
	    //,closeBtn:0
        ,yes: function(index,layero){
         var iframeWindow = window['layui-layer-iframe'+ index];
         var data=$(layero).find('iframe')[0].contentWindow.callBackData();
         callBackAnnexId=data;
         var annexArray=data.annexArray;
         uploadCallBack(annexArray);
         layer.close(index);
       },
       //点击关闭按钮
       btn2: function (index,layero) {
    	   var iframeWindow = window['layui-layer-iframe'+ index];
           var data=iframeWindow.callBackData();
           var annexArray=data.annexArray;
           if(annexArray!=null&&annexArray!=''){
        	   //ajax进行删除
        	   iframeWindow.batchDelAnnex(annexArray);
           }
       }
     });
	  layer.full(index);
}

