function validateInput(x, y, r) {
    const xVal = parseFloat(x);
    const yVal = parseFloat(y);
    const rVal = parseFloat(r);

    if (isNaN(xVal) || xVal < -2 || xVal > 2) {
        alert("X должен быть числом от -2 до 2");
        return false;
    }
    if (isNaN(yVal) || yVal < -5 || yVal > 5) {
        alert("Y должен быть числом от -5 до 5");
        return false;
    }
    if (isNaN(rVal) || rVal < 1 || rVal > 3) {
        alert("R должен быть числом от 1 до 3");
        return false;
    }
    return true;
}

function submitForm(event) {
    event.preventDefault();
    const x = document.getElementById("x").value;
    const y = document.getElementById("y").value;
    const r = document.getElementById("r").value;

    if (!validateInput(x, y, r)) return;

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