import { PaysafePlugin } from 'test-plugin';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    PaysafePlugin.echo({ value: inputValue })
}
