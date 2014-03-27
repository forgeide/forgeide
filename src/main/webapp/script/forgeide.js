xw.Ajax.loadingCallback = function(requests) {
  var ctl = xw.Sys.getObject("ajax");
  if (requests > 0) {
    ctl.style.display = "block";
  } else {
    ctl.style.display = "none";
  }
};

xw.EL.setValue("location", location);    

var Forge = {
  parseCommands: function(result) { 
    var definition = {};
    
    for (var category in result) {
      var commands = [];
      for (var i = 0; i < result[category].length; i++) {
        commands.push({label: result[category][i]});
      }
      definition[category] = {label: category, children: commands};
    }
    
    return definition;
  },
  commandMetadataCallback: function(command, meta) {
    xw.Popup.open("commandui.xw", {
      title: command,
      params: {
        command: command, 
        meta: meta,
        height: 300
      }
    });
  },
  initiateCommand: function(command) {
    var cb = function(meta) {
      Forge.commandMetadataCallback(command, JSON.parse(meta));
    }
    xw.Sys.getWidget("commandMetadataService").invoke({command:command}, null, cb);
  },
  executeCommandCallback: function(command) {
  
  },
  executeCommand: function(command, params) {
    var cb = function() {
      Forge.executeCommandCallback(command, JSON.parse(meta));
    }
  
    xw.Sys.getWidget("commandExecutionService").invoke({command:command}, JSON.stringify(params), cb);
  }
};

