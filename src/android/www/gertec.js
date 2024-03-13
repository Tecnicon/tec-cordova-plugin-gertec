var exec = require('cordova');

function gertec () {}

gertec.imprimir = function (text, onSuccess, onFail) {
    if (typeof text === "undefined" || text === null) text = "";
	cordova.exec(onSuccess, onFail, "gertec", "imprimir", [text]);
};

gertec.inicializar = function (text, onSuccess, onFail) {
    if (typeof text === "undefined" || text === null) text = "";
	cordova.exec(onSuccess, onFail, "gertec", "inicializar", [text]);
};

gertec.aproximar = function (text, onSuccess, onFail) {
    if (typeof text === "undefined" || text === null) text = "";
	cordova.exec(onSuccess, onFail, "gertec", "aproximar", [text]);
};




var gertec = new gertec();
module.exports = gertec;

