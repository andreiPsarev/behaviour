const validation = [];
const EMAIL_REGEX = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/;
const PASSWORD_REGEX = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-z0-9]).{8,30}$/;

function validate(input, validator) {
    const errorMessage = validator(input);
    const feedback = input.parentNode.querySelector(".form__feedback");
    const classList = input.parentNode.classList;
    if (errorMessage) {
        classList.add(classList[0] + "--error");
        classList.remove(classList[0] + "--success")
    } else {
        classList.remove(classList[0] + "--error");
        classList.add(classList[0] + "--success");
    }
    feedback.innerText = errorMessage;
}

function hasFeedback(input) {
    const classList = input.parentNode.classList;
    return classList.contains(classList[0] + "--success") || classList.contains(classList[0] + "--error");
}

function isValid(input) {
    const classList = input.parentNode.classList;
    return classList.contains(classList[0] + "--success");
}

function hasFieldDataFromServer(errorBagName, fieldName) {
    return serverData["errorBags"] !== undefined
        && serverData["errorBags"][errorBagName] !== undefined
        && serverData["errorBags"][errorBagName][fieldName] !== undefined;
}

function getFieldOldValue(errorBagName, fieldName) {
    return serverData["errorBags"][errorBagName][fieldName]["oldValue"];
}

function applyValidation(form, fieldName, validator) {
    const input = form[fieldName];
    const feedback = input.parentNode.querySelector(".form__feedback");
    const classList = input.parentNode.classList;
    const errorBagName = form.classList[0].replace(/-form$/, "");

    if (hasFieldDataFromServer(errorBagName, fieldName)) {
        input.value = serverData["errorBags"][errorBagName][fieldName]["oldValue"];
        feedback.innerText = serverData["errorBags"][errorBagName][fieldName]["errorMessage"];

        if (serverData["errorBags"][errorBagName][fieldName]["errorMessage"] === "") {
            if (input.getAttribute("type") !== "password") {
                classList.add(classList[0] + "--success");
            }
        } else {
            classList.add(classList[0] + "--error");
        }
    } else if (input.value !== "") {
        validate(input, validator);
    }

    input.addEventListener("input", () => {
        validate(input, validator);
    });

    input.addEventListener("blur", () => {
        if ((input.getAttribute("type") === "password" && serverData["errorBags"] !== undefined
                && (serverData["errorBags"][errorBagName][fieldName] === undefined
                    || input.value !== getFieldOldValue(errorBagName, fieldName)))
            || (!hasFeedback(input))) {
            validate(input, validator);
        }
    });

    if (!validation[form]) {
        validation[form] = [];
    }

    validation[form].push({
        "input": input,
        "validator": validator,
    });
}

function applyFormValidation(form) {
    form.addEventListener("submit", event => {
        event.preventDefault();
        validation[form].forEach(validationInfo => {
            if (!hasFeedback(validationInfo["input"])) {
                validate(validationInfo["input"], validationInfo["validator"]);
            }
        });

        if (validation[form].map(o => o["input"]).every(isValid)) {
            form.submit();
        }
    });
}
