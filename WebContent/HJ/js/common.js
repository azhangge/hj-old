function validateNum(e) {
	if (event.keyCode < 45 || event.keyCode > 57) {
		event.returnValue = false;
	}
}

function validateString(e) {
	var keynum;
	var keychar;
	var numcheck;
	if (window.event) // IE
	{
		keynum = e.keyCode
	} else if (e.which) // Netscape/Firefox/Opera
	{
		keynum = e.which
	}
	keychar = String.fromCharCode(keynum)
	numcheck = /\d/
	return !numcheck.test(keychar)
}