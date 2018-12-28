//非模态窗口
function myShowModalDialogBR(url,title, width, height, fn) {
	window.returnCallBackValue354865588 = fn;
	var params = "";
    if (navigator.userAgent.indexOf("Chrome") > 0) {
        params = 'height=' + height + ', width=' + width + ', top=' + (window.screen.height - height>100?((window.screen.height - height) / 2) - 50:0) +
            ',left=' + ((window.screen.width - width) / 2) + ',toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no';
    }
    else{
    	params = 'width=' + width + ',height=' + height + ',status:no,channelmode:yes,fullscreen:yes,scrollbars=yes';
    	
    }
    window.open(url, title, params);
}
function myReturnValueBR(value) {
	window.opener.returnCallBackValue354865588.call(window.opener, value);
}