var inputState=0;

function openHandInput(){
    var objShell = new ActiveXObject("wscript.shell");
    if(inputState === 0){        
//        objShell.exec("taskkill /t /f /im osk.exe");
        objShell.run("c:\\input\\HandInput.exe");
//        inputState=1;
    }
    else{
        objShell.exec("taskkill /t /f /im HandInput.exe");
//        objShell.run("osk");
        inputState=0;
    }    
}