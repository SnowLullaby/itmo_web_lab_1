const errorMessage = document.querySelector('.error-message');

function validateInput(x, y, rValues) {
    const xVal = parseFloat(x);
    const yVal = parseFloat(y);

    if (!checkRequiredFields(x, y, rValues) || !checkXRange(xVal) || !checkYFormat(y)
        || !checkYRange(yVal) || !checkRValues(rValues)) return false;

    hideNotification();
    return true;
}

function checkRequiredFields(x, y, rValues) {
    if (!x || !y || !rValues || rValues.length === 0) {
        showNotification("Не задано значение X, Y или R");
        return false;
    }
    return true;
}

function checkXRange(xVal) {
    if (isNaN(xVal) || xVal < -2 || xVal > 2) {
        showNotification("X должен быть числом от -2 до 2");
        return false;
    }
    return true;
}

function checkYFormat(y) {
    if (y.includes(',')) {
        showNotification("Используйте точку как разделитель");
        return false;
    }
    return true;
}

function checkYRange(yVal) {
    if (isNaN(yVal) || yVal < -5 || yVal > 5) {
        showNotification("Y должен быть числом от -5 до 5");
        return false;
    }
    return true;
}

function checkRValues(rValues) {
    for (const rVal of rValues) {
        const rValParsed = parseFloat(rVal);
        if (isNaN(rValParsed) || rValParsed < 1 || rValParsed > 3) {
            showNotification("R должен быть числом от 1 до 3");
            return false;
        }
    }
    return true;
}

function showNotification(notification) {
    errorMessage.textContent = notification;
    errorMessage.classList.add('show');
}

function hideNotification() {
    errorMessage.classList.remove('show');
}