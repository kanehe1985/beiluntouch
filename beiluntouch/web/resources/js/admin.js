 
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
      
  var playRecord = function(base64){
      var blob = base64ToBlobSync(base64);
      var url = URL.createObjectURL(blob);
      var audioPlayer = document.getElementById('audioPlayer');
      audioPlayer.src=url;
      audioPlayer.play();
  }

  function createDownloadLink() {
    recorder && recorder.exportWAV(function(blob) {
        
        var reader = new FileReader();

        reader.onload = function(e) {
          var text = reader.result;
//          $('#detailForm:txtRecord').val(text.split(',')[1]);
//           PF('txtRecord').val(text.split(',')[1]);
            document.getElementById('detailForm:j_idt45').value=text.split(',')[1];
          bl = base64ToBlobSync(text.split(',')[1]);
          
          var url = URL.createObjectURL(bl);
            var li = document.createElement('li');
            var au = document.createElement('audio');
            var hf = document.createElement('a');

            au.controls = true;
            au.src = url;
            hf.href = url;
            hf.download = new Date().toISOString() + '.wav';
            hf.innerHTML = hf.download;
            li.appendChild(au);
            li.appendChild(hf);
            recordingslist.appendChild(li);
        }

        reader.readAsDataURL(blob);
        
      var url = URL.createObjectURL(blob);
      var li = document.createElement('li');
      var au = document.createElement('audio');
      var hf = document.createElement('a');
      
      au.controls = true;
      au.src = url;
      hf.href = url;
      hf.download = new Date().toISOString() + '.wav';
      hf.innerHTML = hf.download;
      li.appendChild(au);
      li.appendChild(hf);
      recordingslist.appendChild(li);
    });
  }