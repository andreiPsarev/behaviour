if (serverData["alert"]) {
    const title = serverData["alert"]["title"];
    const description = serverData["alert"]["description"];
    const type = serverData["alert"]["type"].toLowerCase();
    const iconClassByType = {
        "error": "fa-circle-exclamation",
        "warning": "fa-triangle-exclamation",
        "info": "fa-circle-info",
        "success": "fa-circle-check",
    };

    const alert = document.querySelector(".alert");
    const alertIconClassList = alert.querySelector(".alert__icon").classList;
    const alertOKButton = alert.querySelector(".alert__button");
    const alertCloseButton = alert.querySelector(".alert__close-button");

    alert.querySelector(".alert__heading").innerText = title;
    alert.querySelector(".alert__description").innerText = description;
    alertIconClassList.add(iconClassByType[type]);
    alertIconClassList.add("alert__icon--" + type);
    [alertOKButton, alertCloseButton].forEach(e => e.addEventListener("click", event => {
        alert.classList.remove("alert--visible");
    }));
    alert.classList.add("alert--visible");
    window.addEventListener("keydown", function listener(event) {
        if (event.key === "Escape") {
            alert.classList.remove("alert--visible");
            window.removeEventListener("keydown", listener);
        }
    });
}
