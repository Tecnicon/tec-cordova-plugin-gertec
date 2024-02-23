var exec = require('cordova');

function gertec () {}

gertec.imprimir = function (text, onSuccess, onFail) {
    if (typeof text === "undefined" || text === null) text = "";
	cordova.exec(onSuccess, onFail, "gertec", "imprimir", [text]);
};

gertec.inicializarPinPad = function (text, onSuccess, onFail) {
    if (typeof text === "undefined" || text === null) text = "";
	cordova.exec(onSuccess, onFail, "gertec", "inicializarPinPad", [text]);
};


var gertec = new gertec();
module.exports = gertec;

