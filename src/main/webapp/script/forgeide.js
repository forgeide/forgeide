function parseCommands(result) { 
  var definition = {};
  
  for (var category in result) {
    var commands = [];
    for (var i = 0; i < result[category].length; i++) {
      commands.push({label: result[category][i]});
    }
    definition[category] = {label: category, children: commands};
  }
  
  return definition;
}

function executeForgeCommand(category, command) {
  alert("execute forge command [" + category + "] : [" + command + "]");
}
