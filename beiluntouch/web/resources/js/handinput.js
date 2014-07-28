function openHandInput(){    
    var objShell = new ActiveXObject("WScript.Shell");
    objShell.run("HandInput.exe");
}

function closeHandInput(){
    var objShell = new ActiveXObject("WScript.Shell");
    objShell.exec("taskkill /t /f /im HandInput.exe");
}

function __log(e, data) {
    log.innerHTML += "\n" + e + " " + (data || '');
  }

  var audio_context;
  var recorder;
  var localMediaStream;
  
  function startUserMedia(stream) {
      localMediaStream=stream;
    var input = audio_context.createMediaStreamSource(stream);
//    __log('Media stream created.');
    
    input.connect(audio_context.destination);
//    __log('Input connected to audio context destination.');
    
    recorder = new Recorder(input);
//    __log('Recorder initialised.');
    startRecording(document.getElementById("btnRecord"));
  }

  function startRecording(button) {
      
    document.getElementById('lblSaved').style.display="none";
    recorder && recorder.record();
    button.disabled = true;
    button.nextElementSibling.disabled = false;
//    __log('Recording...');
  }

  function stopRecording(button) {
    recorder && recorder.stop();
    button.disabled = true;
    button.previousElementSibling.disabled = false;
//    __log('Stopped recording.');
    
    // create WAV download link using audio data blob
    createDownloadLink();
    
    recorder.clear();
    localMediaStream.stop();
  }
  
  var base64ToBlobSync = function(base64) {
      var binary = atob(base64);
      var len = binary.length;
      var buffer = new ArrayBuffer(len);
      var view = new Uint8Array(buffer);
      for (var i = 0; i < len; i++) {
        view[i] = binary.charCodeAt(i);
      }
      var blob = new Blob([view]);
      return blob;
      };

  function createDownloadLink() {
    recorder && recorder.exportWAV(function(blob) {
        
        var reader = new FileReader();

        reader.onload = function(e) {
          var text = reader.result;
//          $('#detailForm:txtRecord').val(text.split(',')[1]);
//           PF('txtRecord').val(text.split(',')[1]);
            document.getElementById('detailForm:j_idt42').value=text.split(',')[1];
        }
        reader.readAsDataURL(blob);
        
        var url = URL.createObjectURL(blob);
        var player = document.getElementById('audioPlayer');
        player.controls=true;
        player.src=url;
        document.getElementById('lblSaved').style.display="inline";
        document.getElementById('detailForm:reasonArea').value="请听录音。";        
        
//        player.style.display="block";
        
//      var li = document.createElement('li');
//      var au = document.createElement('audio');
//      var hf = document.createElement('a');
//      
//      au.controls = true;
//      au.src = url;
//      hf.href = url;
//      hf.download = new Date().toISOString() + '.wav';
//      hf.innerHTML = hf.download;
//      li.appendChild(au);
//      li.appendChild(hf);
//      recordingslist.appendChild(li);
    });
  }

function init(){
//  window.onload = function init() {
    try {
      // webkit shim
      window.AudioContext = window.AudioContext || window.webkitAudioContext;
      navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia;
      window.URL = window.URL || window.webkitURL;
      
      audio_context = new AudioContext;
//      __log('Audio context set up.');
//      __log('navigator.getUserMedia ' + (navigator.getUserMedia ? 'available.' : 'not present!'));
    } catch (e) {
//      alert('No web audio support in this browser!');
    }
    
    navigator.getUserMedia({audio: true}, startUserMedia, function(e) {
//      __log('No live audio input: ' + e);
    });
  };
  
  