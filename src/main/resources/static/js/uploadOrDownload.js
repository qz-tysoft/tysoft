/**
 * 
 */
//为空是默认上传的文件数量为5个,文件类型为file
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
        ,btn: ['取消']
        ,end: function(index, layero){
         var iframeWindow = window['layui-layer-iframe'+ index]
        //,submitID = 'annexUpload-submit'
        //,submit = layero.find('iframe').contents().find('#'+ submitID);
        //监听提交
         //iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
            // var field = data.field; //获取提交的字段
            // var annexArray=field.annexArray;
           //  uploadCallBack(annexArray);
             
         //  });
/*         var arr=$(layero).find('iframe')[0].contentWindow.callBackData();
         alert(arr.test);*/
         var body = layer.getChildFrame('body', index);
         var companyId=body.document.getElementById("annexArray").value;
         console.log(companyId);
       }
     });
	  layer.full(index);
	
	
	
}