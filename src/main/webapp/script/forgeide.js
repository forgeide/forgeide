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
  commandMetadataCallback: function(meta) {
    alert("Got command metadata: " + meta);
  },
  executeCommand: function(command) {
    xw.Sys.getWidget("commandMetadataService").invoke({command:command}, Forge.commandMetadataCallback);
  }
};

