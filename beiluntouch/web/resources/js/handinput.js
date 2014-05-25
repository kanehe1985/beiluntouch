function openHandInput(){
    var objShell = new ActiveXObject("WScript.Shell");
    objShell.run("HandInput.exe");
}

function closeHandInput(){
    var objShell = new ActiveXObject("WScript.Shell");
    objShell.exec("taskkill /t /f /im HandInput.exe");
}