window.copyToClipboard = (str) => {
    const textArea = document.createElement("textArea");
    textArea.value = str;
    textArea.style.position = "absolute";
    textArea.style.opacity = "0";
    document.body.appendChild(textArea);

    textArea.select();
    textArea.setSelectionRange(0, 99999); // For mobile devices

    navigator.clipboard.writeText(textArea.value);
    document.body.removeChild(textArea);
};
