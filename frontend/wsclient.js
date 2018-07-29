function onSendClick() {
    var message = document.getElementById("message").value;
    (async () => {
        const rawResponse = await fetch('http://34.216.129.111:8080', {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({"Action": "normalCommand", "Data": message})
        }).then(function (response) {
            console.log(response);
        }
            
        );
        
      })();
}