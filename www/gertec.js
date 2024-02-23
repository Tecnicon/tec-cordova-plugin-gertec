var exec = require('cordova');

function gertec () {}

gertec.imprimir = function (text, onSuccess, onFail) {
    if (typeof text === "undefined" || text === null) text = "";
	cordova.exec(onSuccess, onFail, "gertec", "imprimir", [text]);
};

gertec.inserir = function (text, onSuccess, onFail) {
    if (typeof text === "undefined" || text === null) text = "";
	cordova.exec(onSuccess, onFail, "gertec", "inserir", [text]);
};

gertec.aproximar = function (text, onSuccess, onFail) {
    if (typeof text === "undefined" || text === null) text = "";
	cordova.exec(onSuccess, onFail, "gertec", "aproximar", [text]);
};


var gertec = new gertec();
module.exports = gertec;

