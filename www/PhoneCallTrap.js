var PhoneCallTrap = {
    onCall: function(successCallback, errorCallback) {
        errorCallback = errorCallback || this.errorCallback;
        cordova.exec(successCallback, errorCallback, 'PhoneCallTrap', 'onCall', []);
    },
   endCall: function(successCallback, errorCallback) {
        errorCallback = errorCallback || this.errorCallback;
        cordova.exec(successCallback, errorCallback, 'PhoneCallTrap', 'endCall', []);
    },
    errorCallback: function() {
        console.log("WARNING: PhoneCallTrap errorCallback not implemented");
    }
};

module.exports = PhoneCallTrap;
