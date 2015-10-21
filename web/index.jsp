<%--
  Created by IntelliJ IDEA.
  User: nanmeiying
  Date: 15-10-21
  Time: 上午9:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Web Socket Demo</title>
    <script type="text/javascript">
        var ws = null;
        function startServer() {
            console.log(location);
            var url = "ws://" + location.host + "/websocketTest/echo.ws";
            if ('WebSocket' in window) {
                ws = new WebSocket(url);
            } else if ('MozWebSocket' in window) {
                ws = new MozWebSocket(url);
            } else {
                alert('Your browser Unsupported WebSocket!');
                return;
            }

            ws.onopen = function () {
                document.getElementById("content").innerHTML += 'websocket open! Welcome!<br />';
            };
            ws.onmessage = function (event) {
                document.getElementById("content").innerHTML += event.data + '<br />';
            };
            ws.onclose = function () {
                document.getElementById("content").innerHTML += 'websocket closed! Byebye!<br />';
            };
        }

        function sendMyMessage() {
            var nickName = document.getElementById('nickName');
            var textMessage = document.getElementById('textMessage');
            if (ws != null && textMessage.value != '') {
                ws.send(nickName.value + '!@#$%' + textMessage.value);
                textMessage.value = '';
            }
        }
    </script>
</head>
<body onload="startServer()">

<table cellpadding=5 cellspacing=0>
    <tr>
        <td>昵称:</td>
        <td><input type="text" id="nickName" size="20"/></td>
    </tr>
    <tr>
        <td>消息：</td>
        <td><input type="text" id="textMessage" size="20"/></td>
    </tr>
    <tr>
        <td colspan="2" align=center>
            <input type="button" onclick="sendMyMessage()" value="发送消息"/>
        </td>
    </tr>
</table>
<div id="content"></div>
</body>
</html>