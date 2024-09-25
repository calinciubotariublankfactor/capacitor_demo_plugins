import { PaysafePlugin } from 'test-plugin';

window.testEcho = () => {
    // const inputValue = document.getElementById("echoInput").value;
    PaysafePlugin.echo()
}

window.venmoFlow = () => {
    const consumerId = document.getElementById("venmoConsumerIdInput").value;

    if (!consumerId) {
        console.log("Empty/null consumer id.")
        return
    }

    PaysafePlugin.startVenmo({ consumerId: consumerId })
        .then((response) => {
            console.log('venmoFlow resolved with value: ' + response["paymentToken"]);
        })
        .catch((error) => {
            console.error('venmoFlow rejected with error: ' + error);
        });
}
