"use strict";
var exec = require('cordova/exec');

var Hcaptcha = {
  verify: function(successCallback, errorCallback, args) {
    exec(successCallback, errorCallback, "Hcaptcha", "verify", [args]);
  }
};

module.exports = Hcaptcha;