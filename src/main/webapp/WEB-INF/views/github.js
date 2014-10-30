var GH = {
  regWindow: null,
  stateVar: null,
  generateState: function() {
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for(var i = 0; i < 10; i++) {
      text += possible.charAt(Math.floor(Math.random() * possible.length));
    }

    return text;  
  },
  register: function() {
    GH.stateVar = GH.generateState();
    
    var redirectUri = "https://forgeide.org/github_auth_callback.html";

    var url = "https://github.com/login/oauth/authorize?" +
      "client_id=dcd7edcdc0b37c91f37d" +
      "&scope=public_repo" +
      "&state=" + state +
      "&redirect_url=" + encodeURIComponent(redirectUri);
           
    var features = "location=yes,height=650,width=1020,scrollbars=yes,status=yes";
    GH.regWindow = window.open(url, "_blank", features); 
  }
};
