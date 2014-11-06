var GH = {
  regWindow: null,
  stateVar: null,
  wss: null, // The websocket service
  registerWSService: function(wss) {
    GH.wss = wss;
  },
  processMessage: function(data) {
    var msg = JSON.parse(data);
       
    if (msg.cat == "GITHUB") {
      if (msg.op == "STATE") {
        GH.stateVar = msg.payload.state;
        GH.redirectToGitHub();
      } else if (msg.op == "AUTHORIZING") {
        GH.regWindow.close();
        alert("Authorizing...");
      } else if (msg.op == "AUTHORIZED") {
        alert("Authorization complete.");      
      }
    }
  },
  register: function() {
     if (!GH.wss.isConnected()) {
       GH.wss.connect();
     }
     
     GH.wss.send(GH.createMessage("github", "generateState", {}));
  },
  redirectToGitHub: function() {   
    var redirectUri = "https://forgeide.org/github_auth_callback.html";

    var url = "https://github.com/login/oauth/authorize?" +
      "client_id=dcd7edcdc0b37c91f37d" +
      "&scope=public_repo" +
      "&state=" + GH.stateVar +
      "&redirect_url=" + encodeURIComponent(redirectUri);
           
    var features = "location=yes,height=650,width=1020,scrollbars=yes,status=yes";
    GH.regWindow = window.open(url, "_blank", features);
  },
  createMessage: function(cat, op, payload) {
    return JSON.stringify({
      cat: cat,
      op: op,
      payload: payload
    });
  }
};

//# sourceURL=github.js
