const evtSource = new EventSource('/notifications', { headers: { Authorization: 'plz' } })

evtSource.onmessage = function(event) {
  console.log(event)
  alert(event);
}