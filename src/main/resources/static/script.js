function validateInput(x, y, rValues) {
    const xVal = parseFloat(x);
    const yVal = parseFloat(y);
    const errorMessage = document.querySelector('.error-message');

    if (!x || !y || !rValues || rValues.length === 0) {
        errorMessage.textContent = "Не задано значение X, Y или R";
        errorMessage.classList.add('show');
        return false;
    }

    if (isNaN(xVal) || xVal < -2 || xVal > 2) {
        errorMessage.textContent = "X должен быть числом от -2 до 2";
        errorMessage.classList.add('show');
        return false;
    }

    if (isNaN(yVal) || yVal < -5 || yVal > 5) {
        errorMessage.textContent = "Y должен быть числом от -5 до 5";
        errorMessage.classList.add('show');
        return false;
    }

    rValues.forEach(rVal => {
        const rValParsed = parseFloat(rVal);
        if (isNaN(rValParsed) || rValParsed < 1 || rValParsed > 3) {
            errorMessage.textContent = "R должен быть числом от 1 до 3";
            errorMessage.classList.add('show');
            return false;
        }
    });

    errorMessage.classList.remove('show');
    return true;
}

document.getElementById('pointForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const x = document.querySelector('input[name="x"]:checked')?.value;
    const y = document.querySelector('input[name="y"]').value;

    const rCheckboxes = document.querySelectorAll('input[name="r"]:checked');
    const rValues = Array.from(rCheckboxes).map(cb => cb.value);

    if (!validateInput(x, y, rValues)) return;

    const data = { x: parseFloat(x), y: parseFloat(y), r: rValues.map(r => parseFloat(r)) };

    fetch('/fcgi-bin/fcgi-server.jar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                return response.json()
                    .then(errorData => {
                        const errorDetail = errorData && errorData.error ? `: ${errorData.error}` : '';
                        const errorMsg = `${response.status} - ${response.statusText}${errorDetail}`;
                        if (response.status >= 400 && response.status < 500) {
                            throw new Error(`клиентской части: ${errorMsg}`);
                        } else if (response.status >= 500 && response.status < 600) {
                            throw new Error(`серверной части: ${errorMsg}`);
                        } else {
                            throw new Error(errorMsg);
                        }
                    })
                    .catch(() => {
                        const errorMsg = `${response.status} - ${response.statusText || 'Unknown Error'}`;
                        throw new Error(`${errorMsg}`);
                    });
            }
            return response.json();
        })
        .then(results => {
            const tbody = document.querySelector("#resultsTable tbody");
            results.forEach(result => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${result.x}</td>
                    <td>${result.y}</td>
                    <td>${result.r}</td>
                    <td data-result="${result.hit}">${result.hit ? "Попадание" : "Промах"}</td>
                    <td>${result.currentTime}</td>
                    <td>${result.executionTime} ms</td>
                    `;
                tbody.insertBefore(row, tbody.firstChild);
            });
        })
        .catch(error => alert("Ошибка " + error.message));
});