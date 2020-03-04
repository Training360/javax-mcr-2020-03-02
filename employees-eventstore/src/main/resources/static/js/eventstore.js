let list = document.querySelector("#event-list");

let clearButton = document.querySelector("#clear-button");

window.onload = function() {
    fetch('api/events')
        .then(response => response.json())
        .then(events => printEvents(events));

    let evtSource = new EventSource("api/events/stream");
    evtSource.addEventListener("event",
        function(event) {
            print(JSON.parse(event.data))
        });

    clearButton.onclick = function () {
       list.innerHTML = "";
    };

}

function printEvents(events) {
    for (let event of events) {
        print(event);
    }
}

function print(event) {
    let content = `
          <li class="list-group-item">
          <h2>New event</h2>
          <b>Source:</b> ${event.source}<br />
          <b>Time:</b> ${event.time}<br />
          <b>Message:</b> ${event.message}</li>
        `

    list.insertAdjacentHTML( 'afterbegin', content );
}