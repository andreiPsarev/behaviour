const validation = [];

function addPlaceholder(editable, placeholder) {
    editable.setAttribute("data-placeholder", placeholder);
    editable.addEventListener("input", event => {
        if (event.target.innerText === "\n") {
            event.target.innerText = "";
        }
    });
}

function validate(editable, validator) {
    const classList = editable.parentNode.classList;
    const feedback = editable.parentNode.querySelector(".form__feedback");
    const message = validator(editable);

    if (validator(editable)) {
        classList.add("form__row--error");
        classList.remove("form__row--success");
    } else {
        classList.add("form__row--success");
        classList.remove("form__row--error");
    }

    feedback.innerText = message;
}

function applyValidation(editable, validator) {
    editable.addEventListener("input", event => {
        validate(editable, validator);
    });

    validation.push({
        "editable": editable,
        "validator": validator,
    });
}

function isValid(editable) {
    return editable.parentNode.classList.contains("form__row--success");
}

function hasFeedback(editable) {
    const classList = editable.parentNode.classList;
    return classList.contains("form__row--success") || classList.contains("form__row--error");
}

const form = document.querySelector(".post-form");
const titleInput = form["title"];
const descriptionInput = form["description"];
const titleEditable = form.querySelector("h2.form__control");
const descriptionEditable = form.querySelector("p.form__control");

addPlaceholder(titleEditable, "Enter post title");
addPlaceholder(descriptionEditable, "Enter post body");

applyValidation(titleEditable, e => e.innerText === "\n" || e.innerText === "" ? "Enter post title" : null);
applyValidation(descriptionEditable, e => e.innerText === "\n" || e.innerText === "" ? "Enter post body" : null);

form.addEventListener("submit", event => {
    event.preventDefault();
    titleInput.value = titleEditable.innerText;
    descriptionInput.value = descriptionEditable.innerText;

    validation.forEach(validationInfo => {
        if (!hasFeedback(validationInfo["editable"])) {
            validate(validationInfo["editable"], validationInfo["validator"]);
        }
    });
    if (validation.map(o => o["editable"]).every(isValid)) {
        form.submit();
    }
});
