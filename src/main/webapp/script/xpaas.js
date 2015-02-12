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

var xp = {
  createProjectCallback: function(response) {
    xw.open("projects.xw", null, "content");
  },
  parseProjects: function(result) {
    return result;
  },
  createProject: function(props) {
    var cb = function(response) {
      xp.createProjectCallback(JSON.parse(response));
    };
    xw.Sys.getWidget("projectService").post({content:JSON.stringify(props), callback: cb});
  },
  register: function(params) {
    var cb = function(response) {
      var result = JSON.parse(response);
      alert(result);
    }
    xw.Sys.getWidget("registerService").post({content:JSON.stringify(params), callback: cb});
  }
};

