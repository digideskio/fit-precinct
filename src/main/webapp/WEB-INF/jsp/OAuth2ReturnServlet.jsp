<!DOCTYPE html>
<%@ page language="java"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title></title>
<script type="text/javascript">
	console.log(parent);
	parent.postMessage('{"hello":"world!"}', parent.window.location.origin);

// Called sometime after postMessage is called
function receiveMessage(event)
{
	var thisOrigin = window.location.protocol + '//' + window.location.hostname;
	var debugOrigin = thisOrigin + ':9000';
	// window.location.host is hostname + port
	thisOrigin += ':' + window.location.port;
  // Do we trust the sender of this message? "http://example.com:8080"
  if (event.origin !== thisOrigin && event.origin !== debugOrigin)
    return;

  console.log(event.origin, event.data, event.source, event);

  var data = JSON.parse(event.data);
  switch(data.type){
  	case 'close':
	  	var response = {'response': 'close', 'msg': 'ok'};
  		event.source.postMessage(JSON.stringify(response), event.origin);
  		window.close();
  	break;
  	case 'session':
	  	var response = {'response': 'session', 'msg': '${model.sessionId}'};
	    event.source.postMessage(JSON.stringify(response), event.origin);
  	break;
  	default:
  	break;
  }
}

window.addEventListener("message", receiveMessage, false);
</script>
</head>
<body>
This window should close in a few seconds. If it doesn't please close this window and reload the main page.
</body>
</html>