var xlsx = require('./lib/xlsx-to-json.js');
var path = require('path');
var shell = require('child_process');
var fs = require('fs');
var glob = require('glob');
var config = require('./config.json');
var commands = {
    "--help": {
        "alias": ["-h"],
        "desc": "show this help manual.",
        "action": showHelp
    },
    "--export": {
        "alias": ["-e"],
        "desc": "export excel to json. --export [files]",
        "action": exportJson,
        "default": true
    }
};
var alias_map = {};
var parsed_cmds = [];
var keys = Object.keys(commands);
for (var key in commands) {
    var alias_array = commands[key].alias;
    alias_array.forEach(function(e) {
        alias_map[e] = key;
    });
}
parsed_cmds = parseCommandLine(process.argv);
parsed_cmds.forEach(function(e) {
    exec(e);
});
function exportJson(args) {
    if (typeof args === 'undefined' || args.length === 0) {
        glob(config.xlsx.src, function(err, files) {
            if (err) {
                console.error("exportJson error:", err);
                throw err;
            }
            files.forEach(function(element, index, array) {
                xlsx.toJson(path.join(__dirname, element), path.join(__dirname, config.xlsx.dest), config.xlsx.head, config.xlsx.arraySeparator);
            });
        });
    } else {
        if (args instanceof Array) {
            args.forEach(function(element, index, array) {
                xlsx.toJson(path.join(__dirname, element), path.join(__dirname, config.xlsx.dest), config.xlsx.head, config.xlsx.arraySeparator);
            });
        }
    }
}
function showHelp() {
    var usage = "usage: \n";
    for (var p in commands) {
        if (typeof commands[p] !== "function") {
            usage += "\t " + p + "\t " + commands[p].alias + "\t " + commands[p].desc + "\n ";
        }
    }
    usage += "\nexamples: ";
    usage += "\n\n $node index.js --export\n\tthis will export all files configed to json.";
    usage += "\n\n $node index.js --export ./excel/foo.xlsx ./excel/bar.xlsx\n\tthis will export foo and bar xlsx files.";
    console.log(usage);
}
function exec(cmd) {
    if (typeof cmd.action === "function") {
        cmd.action(cmd.args);
    }
}
function parseCommandLine(args) {
    var parsed_cmds = [];
    if (args.length <= 2) {
        parsed_cmds.push(defaultCommand());
    } else {
        var cli = args.slice(2);
        var pos = 0;
        var cmd;
        cli.forEach(function(element, index, array) {
            if (element.indexOf('--') === -1 && element.indexOf('-') === 0) {
                cli[index] = alias_map[element];
            }
            if (cli[index].indexOf('--') === -1) {
                cmd.args.push(cli[index]);
            } else {
                if (keys[cli[index]] == "undefined") {
                    throw new Error("not support command:" + cli[index]);
                }
                pos = index;
                cmd = commands[cli[index]];
                if (typeof cmd.args == 'undefined') {
                    cmd.args = [];
                }
                parsed_cmds.push(cmd);
            }
        });
    }
    return parsed_cmds;
}
function defaultCommand() {
    if (keys.length <= 0) {
        throw new Error("Error: there is no command at all!");
    }
    for (var p in commands) {
        if (commands[p]["default"]) {
            return commands[p];
        }
    }
    if (keys["--help"]) {
        return commands["--help"];
    } else {
        return commands[keys[0]];
    }
}