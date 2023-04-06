const inputArea = document.getElementById("input");
const statusInfo = document.getElementById("status");

const copyButton = document.getElementById("copy");
const reformatButton = document.getElementById("reformat");

copyButton.onclick = () => {
  inputArea.focus();
  inputArea.select();

  if (navigator.clipboard) {
    navigator.clipboard.writeText(inputArea.value);
  } else {
    // fallback
    document.execCommand("copy");
  }
};

reformatButton.onclick = (event) => {
  inputArea.disabled = true;
  copyButton.disabled = true;
  reformatButton.disabled = true;
  statusInfo.innerHTML = "<span>Formatting...</span>";

  const req = new XMLHttpRequest();
  req.open("POST", `${location.href}api/v1/format`, true);
  req.onreadystatechange = () => {
    if (req.readyState != 4) {
      return;
    }

    inputArea.disabled = false;
    copyButton.disabled = false;
    reformatButton.disabled = false;

    if (req.status != 200) {
      statusInfo.innerHTML = `<span>Error ${req.status} while reformatting: </span><code>${req.responseText}</code>`;
      return;
    }

    statusInfo.innerHTML = "";
    inputArea.value = req.responseText;
  };

  req.setRequestHeader("Content-Type", "text/plain");
  req.send(inputArea.value);
};
