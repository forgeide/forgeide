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
  executeCommandCallback: function(response) {
      Forge.updateCommandStatus("successful");
  },
  executeCommand: function(command, params, view) {  
    Forge.commandView = view;
    Forge.updateCommandStatus("executing");
    xw.Sys.getWidget("commandExecutionService").invoke({command:command}, JSON.stringify(params), Forge.executeCommandCallback);
  },
  updateCommandStatus: function(status) {
    // valid status values - executing, successful, failure
    
    var o = xw.Sys.getObject("popupOverlay");
    var ce = xw.Sys.getObject("command_executing");
    var cs = xw.Sys.getObject("command_successful");
    var cf = xw.Sys.getObject("command_failed");
    var be = Forge.commandView.getWidgetById("commandExecute");
    var bc = Forge.commandView.getWidgetById("commandCancel");
    
    if (status == "executing") {
      be.disable();
      bc.disable();
      ce.style.display = "block";
      cs.style.display = "none";
      cf.style.display = "none";
      o.style.display = "block";
    } else if (status == "successful") {
      be.disable();
      bc.enable();
      ce.style.display = "none";
      cs.style.display = "block";
      cf.style.display = "none";
      o.style.display = "block";
    }
  }  
};

