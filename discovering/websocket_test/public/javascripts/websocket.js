const socket = new WebSocket('ws://localhost:9000/ws');

socket.onopen = function(e) {
    socket.send(JSON.stringify("hello"));

    socket.onmessage = function(event){
        socket.send(JSON.stringify("olleh"));
        console.log(event.data);
    };
}


