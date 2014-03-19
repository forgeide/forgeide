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
  executeCommand: function(command) {
    alert("execute forge command [" + command + "]");  
  }
};

