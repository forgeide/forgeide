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
        meta: meta
      }
    });
  },
  executeCommand: function(command) {
    var cb = function(meta) {
      Forge.commandMetadataCallback(command, JSON.parse(meta));
    }
    xw.Sys.getWidget("commandMetadataService").invoke({command:command}, cb);
  }
};

