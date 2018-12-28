var initDHOcxHandle = false;
var DH_SmartWndId = null;
var DH_LoginHandle = -1;
var DH_PlayHandle = -1;
var DH_serverIP = null;
$(window).unload(function(){
	//大华
	DPSDK_Logout();
});

function DPSDK_Init(){
	try{
		var obj = new ActiveXObject("DPSDK_OCX.DPSDK_OCXCtrl.1");
		initDHOcxHandle = true;
	}catch(e){
		if(window.confirm("大华控件未安装或浏览器不支持，请先安装控件，用IE打开！\n现在是否下载安装？")){
			window.open(STATIC+'modules/video/dh/DPSDK_OCX_Win32.exe');
		}
	}
}
/**
 * 登录大华平台
 * @param ip
 * @param port
 * @param user
 * @param pwd
 * @returns
 */
function DPSDK_Login(ip,port,user,pwd){
	var playocx = document.getElementById("DPSDK_OCX");
	DH_LoginHandle = playocx.DPSDK_Login(ip, port, user, pwd);
	if(DH_LoginHandle != 0){
		alert('登录视频服务器失败!');
	}
}
/**
 * 登出大华平台
 * @returns
 */
function DPSDK_Logout(){
	if(DH_LoginHandle != -1){
		var playocx = document.getElementById("DPSDK_OCX");
		if(DH_PlayHandle != -1){
			DPSDK_StopRealplayByWndNo();
		}
		playocx.DPSDK_Logout();
		DH_LoginHandle = -1;
	}
}

/**
 * 播放视频
 * @param cameraId
 * @returns
 */
function DPSDK_PlayVideo(cameraId){
	var playocx = document.getElementById("DPSDK_OCX");
	var nWndNo = playocx.DPSDK_GetSelWnd(DH_SmartWndId);
	// 开始预览 
	DH_PlayHandle = playocx.DPSDK_StartRealplayByWndNo(DH_SmartWndId, nWndNo, cameraId, 1, 1, 1);
	if(DH_PlayHandle!=0){
		alert('播放视频失败,错误码:'+DH_PlayHandle);
	}
}
/**
 * 停止播放视频
 * @returns
 */
function DPSDK_StopRealplayByWndNo(){
	if(DH_PlayHandle != -1){
		var playocx = document.getElementById("DPSDK_OCX");
		// 获取当前活动窗口号, 其中m_nSmartWndId由DPSDK_CreateSmartWnd创建的窗口控件id
	    var nWndNo = playocx.DPSDK_GetSelWnd(DH_SmartWndId);
	    // 停止预览
	    playocx.DPSDK_StopRealplayByWndNo(DH_SmartWndId,nWndNo);
	    DH_PlayHandle = -1;
	}
}