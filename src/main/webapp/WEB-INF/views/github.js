var GH = {
  regWindow: null, // Reference to the window object
  stateVar: null,
  redirectUri: null, // The local URI to redirect to after visiting github
  wss: null, // The websocket service
  registerWSService: function(wss) {
    GH.wss = wss;
    
    if (!GH.wss.isConnected()) {
      GH.wss.connect();
    }    
    GH.wss.send(GH.createMessage("identity.login", {jwtToken: pl.getToken()}));
  },
  processMessage: function(data) {
    var msg = JSON.parse(data);
       
    if (msg.key == "github.state") {
      GH.stateVar = msg.payload.state;
      GH.redirectUri = msg.payload.redirectUri;
      GH.redirectToGitHub();
    } else if (msg.key == "github.authorizing") {
      GH.regWindow.close();
      alert("Authorizing...");
    } else if (msg.key == "github.authorized") {
      alert("Authorization complete.");      
    }
  },
  register: function() {
    if (!GH.wss.isConnected()) {
      GH.wss.connect();
    }
     
    GH.wss.send(GH.createMessage("github.generateState", {}));
  },
  redirectToGitHub: function() {   
    var url = "https://github.com/login/oauth/authorize?" +
      "client_id=dcd7edcdc0b37c91f37d" +
      "&scope=public_repo" +
      "&state=" + GH.stateVar +
      "&redirect_url=" + encodeURIComponent(GH.redirectUri);
           
    var features = "location=yes,height=650,width=1020,scrollbars=yes,status=yes";
    GH.regWindow = window.open(url, "_blank", features);
  },
  createMessage: function(key, payload) {
    return JSON.stringify({
      key: key,
      payload: payload
    });
  }
};

//# sourceURL=github.js
