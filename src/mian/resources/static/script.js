function validateInput(x, y, r) {

    const xVal = parseFloat(x);
    const yVal = parseFloat(y);
    const rVal = parseFloat(r);

    const errorMessage = document.querySelector('.error-message');
    if (!errorMessage) {
        console.error('Error: .error-message element not found');
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
    if (isNaN(rVal) || rVal < 1 || rVal > 3) {
        errorMessage.textContent = "R должен быть числом от 1 до 3";
        errorMessage.classList.add('show');
        return false;
    }

    errorMessage.classList.remove('show');
    return true;
}

document.getElementById('pointForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const x = document.querySelector('input[name="x"]:checked')?.value;
    const y = document.querySelector('input[name="y"]').value;
    const r = document.querySelector('input[name="r"]:checked')?.value;

    if (!x || !y || !r) {
        const errorMessage = document.querySelector('.error-message');
        errorMessage.textContent = "Выберите все значения";
        errorMessage.classList.add('show');
        return;
    }

    if (validateInput(x, y, r)) {
        console.log('Data valid, proceeding...');
        const data = { x: parseFloat(x), y: parseFloat(y), r: parseFloat(r) };
        fetch('/cgi-bin/fastcgi-server', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) throw new Error('Ошибка сервера');
                return response.json();
            })
            .then(result => {
                const tbody = document.querySelector("#resultsTable tbody");
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
            })
            .catch(error => alert("Ошибка: " + error.message));
    }
});