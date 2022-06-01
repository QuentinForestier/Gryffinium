export function openWebSocket(id, listener){

    const socket = new WebSocket('ws://localhost:9000/socket/' + id);
    function sendCommand(command){
        socket.send(JSON.stringify(command));
    }

    socket.onopen = function () {

        socket.onmessage = function (event) {
            listener(event);
        };

        socket.onclose = function () {
            console.log('WebSocket closed');
        };
    }

    socket.onclose = function (e) {
        console.log(e);
    }
}