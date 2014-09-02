/* INITIALIZATION */
xw.Ajax.loadingCallback = function(requests) {
  var ctl = xw.Sys.getObject("ajax");
  if (requests > 0) {
    ctl.style.display = "block";
  } else {
    ctl.style.display = "none";
  }
};

/* Set the Log level */
xw.Log.logLevel = "DEBUG";

var ForgeIDE = {
  projects: [],
  projectExplorer: null,
  messageHandler: {},
  createProjectCallback: function(response) {
    xw.Popup.close();
  },
  selectedProject: function() {
    var n = ForgeIDE.projectExplorer.selectedNode;
    while (n.parent != null) {
      n = n.parent;
    }
    return n.userObject;
  },
  selectedResource: function() {
    var n = ForgeIDE.projectExplorer.selectedNode;
    return n.parent != null ? n.userObject : null;
  },
  getProjectById: function(projectId) {
    for (var i = 0; i < ForgeIDE.projects.length; i++) {
      if (ForgeIDE.projects[i].id == projectId) {
        return ForgeIDE.projects[i];
      }
    }  
  },
  getResourceById: function(projectId, resourceId) {
    var p = ForgeIDE.getProjectById(projectId);
    for (var i = 0; i < p.resources.length; i++) {
      if (p.resources[i].id == resourceId) {
        return p.resources[i];
      }
    }
  },
  createProject: function(props) {
    var cb = function(response) {
      ForgeIDE.createProjectCallback(JSON.parse(response));
    };
    xw.Sys.getWidget("projectService").post({content:JSON.stringify(props), callback: cb});
  },
  setProjectExplorer: function(tree) {
    ForgeIDE.projectExplorer = tree;
  },
  processMessage: function(data) {
    var msg = JSON.parse(data);
    var handler = ForgeIDE.messageHandler[msg.cat];
    if (xw.Sys.isUndefined(handler)) {
      throw "No message handler registered for message category [" + msg.cat + "]";
    }
    if (xw.Sys.isDefined(handler[msg.op]) && typeof handler[msg.op] == "function") {
      handler[msg.op].call(null, msg);
    } else {
      throw "No operation [" + msg.op + "] defined for message handler [" + handler + "]";
    }
  },
  createFolderCallback: function(response) {
    xw.Popup.close();
  },
  createFolder: function(props) {
    var cb = function(response) {
      ForgeIDE.createFolderCallback(JSON.parse(response));
    };
    props.projectId = ForgeIDE.selectedProject().id;
    var r = ForgeIDE.selectedResource();
    if (r != null) {
      props.parentResourceId = r.id;
    }
    xw.Sys.getWidget("projectService").get({command: "newfolder"}, JSON.stringify(props), cb);
  },
  createClassCallback: function(response) {
    xw.Popup.close();
  },
  createClass: function(props) {
    var cb = function(response) {
      ForgeIDE.createClassCallback(JSON.parse(response));
    };
    props.projectId = ForgeIDE.selectedProject().id;
    xw.Sys.getWidget("projectService").get({command: "newclass"}, JSON.stringify(props), cb);
  
  },
  addProjectNode: function(project, select) {
    var n = new org.xwidgets.core.TreeNode(project.name, false, project);
    ForgeIDE.projects.push({
      id: project.id,
      name: project.name,
      node: n,
      resources: []});
    ForgeIDE.projectExplorer.model.addRootNode(n); 
    if (select) {
      ForgeIDE.projectExplorer.selectNode(n);
    }
  },
  addResourceNode: function(resource, select) {
    var leaf = resource.resourceType != "DIRECTORY";
    var n = new org.xwidgets.core.TreeNode(resource.name, leaf, resource);  

    var p = ForgeIDE.getProjectById(resource.project.id);
    p.resources.push({
      id: resource.id,
      name: resource.name,
      type: resource.type,
      node: n
    });

    if (resource.parent != null) {
      var r = ForgeIDE.getResourceById(resource.project.id, resource.parent.id);
      r.node.add(n);
    } else {
      p.node.add(n);
    }
    
    if (select) {
      ForgeIDE.projectExplorer.selectNode(n);
    }
  },
  openResource: function(id) {
    xw.Sys.getWidget("projectListener").send(ForgeIDE.createMessage("resource", "open", {id:(id + "")}));
  },
  createMessage: function(cat, op, payload) {
    return JSON.stringify({
      cat: cat,
      op: op,
      payload: payload
    });
  }
};

ForgeIDE.resourceManager = {
  openResources: [],
  openResource: function(id) {
    ForgeIDE.openResource(id);
  }
};

ForgeIDE.messageHandler.RESOURCE = {
  OPEN: function(msg) {
    ForgeIDE.openResourceEditor();
  }
};

ForgeIDE.messageHandler.PROJECT = {
  NEW: function(msg) {
    ForgeIDE.addProjectNode(msg.payload.project, true);
  },
  LIST: function(msg) {
    if (msg.payload.projects) {
      for (var i = 0; i < msg.payload.projects.length; i++) {
        ForgeIDE.addProjectNode(msg.payload.projects[i], false);
      }
    }
    if (msg.payload.resources) {
      for (var i = 0; i < msg.payload.resources.length; i++) {
        ForgeIDE.addResourceNode(msg.payload.resources[i], false);
      }
    }
  }
};

ForgeIDE.messageHandler.RESOURCE = {
  NEW: function(msg) {
    ForgeIDE.addResourceNode(msg.payload.resource, true);
  }
};


/* FORGE OPERATIONS */
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
  parseProjects: function(result) {
    return result;
  },
  commandMetadataCallback: function(command, meta) {
    xw.Popup.open("ide_commandui.xw", {
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
    };
    xw.Sys.getWidget("commandMetadataService").invoke({command:command}, null, cb);
  },
  executeCommandCallback: function(response) {
    if (response.passed) {
      Forge.updateCommandStatus("successful");
    } else {
      var d = xw.Sys.getObject("command_message");
      xw.Sys.clearChildren(d);
      var m = document.createTextNode(response.message);
      d.appendChild(m);
      Forge.updateCommandStatus("failure");
    }
  },
  executeCommand: function(command, params, view) {  
    Forge.commandView = view;
    Forge.updateCommandStatus("executing");
    
    var cb = function(response) {
      Forge.executeCommandCallback(JSON.parse(response));
    }    
    xw.Sys.getWidget("commandExecutionService").invoke({command:command}, JSON.stringify(params), cb);
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
    } else if (status == "failure") {
      be.disable();
      bc.enable();
      ce.style.display = "none";
      cs.style.display = "none";
      cf.style.display = "block";
      o.style.display = "block";
    }
  }  
};

