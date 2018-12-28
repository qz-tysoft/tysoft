function getPrintScript(){
	var funcStr = "function doPrint() {"
						+"var hkey_root, hkey_path, hkey_key;"
						+"hkey_root = \"HKEY_CURRENT_USER\""
						+	"hkey_path = \"\\Software\\Microsoft\\Internet Explorer\\PageSetup\\\";"
						+"try {" 
						+	"var RegWsh = new ActiveXObject(\"WScript.Shell\");"
						+	"hkey_key = \"header\";"
						+	"RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, \"\");"
						+	"hkey_key = \"footer\";"
						+	"RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, \"\");"
						+"} catch (e) {"
						+"}"
						+"if (document.execCommand(\"print\")) {"
						+"window.close();"
						+"}"
					+"}";
	return funcStr;
}
function pagesetup_null() {
	var hkey_root, hkey_path, hkey_key;
	hkey_root = "HKEY_CURRENT_USER"
		hkey_path = "\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
	try {
		var RegWsh = new ActiveXObject("WScript.Shell");
		hkey_key = "header";
		RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");
		hkey_key = "footer";
		RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");
	} catch (e) {

	}
}

function doPrint() {
	pagesetup_null();
	if (document.execCommand("print")) {
		window.close();
	}
}

