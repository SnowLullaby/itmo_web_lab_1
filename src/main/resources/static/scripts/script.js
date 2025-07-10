const stat = {
    year: '2-digit',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
}

function getFormData() {
    const x = document.querySelector('input[name="x"]:checked')?.value;
    const y = document.querySelector('input[name="y"]').value;
    const rCheckboxes = document.querySelectorAll('input[name="r"]:checked');
    const rValues = Array.from(rCheckboxes).map(cb => cb.value);
    const method = document.querySelector('input[name="method"]:checked')?.value || 'POST';
    return { x, y, rValues, method };
}

function handleFormSubmit() {
    const { x, y, rValues, method } = getFormData();

    if (!validateInput(x, y, rValues)) return;
    const data = { x: parseFloat(x), y: parseFloat(y), r: rValues.map(r => parseFloat(r)) };

    let fetchOptions;

    if (method === 'GET') {
        const queryString = Object.entries(data)
            .map(([key, value]) => {
                const strValue = Array.isArray(value) ? value.join(',') : value;
                return `${key}=${encodeURIComponent(strValue)}`;
            })
            .join('&');
        fetchOptions = {
            method: 'GET',
            url: `/fcgi-bin/fcgi-server.jar?${queryString}`,
            headers: { 'Content-Type': 'application/json' } };
    } else {
        fetchOptions = {
            method: 'POST',
            url: '/fcgi-bin/fcgi-server.jar',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        };
    }

    fetch(fetchOptions.url, fetchOptions)
        .then(handleResponse)
        .then(results => {
            updateTable(results);
        })
        .catch(error => alert("Ошибка " + error.message));
}

function handleResponse(response) {
    if (!response.ok) {
        return response.json()
            .catch(() => {
                const errorMsg = `${response.status} - ${response.statusText || 'Unknown Error'}`;
                throw new Error(`${errorMsg}`);
            })
            .then(results => {
                const errorDetail = results && results.error ? `: ${results.error}` : '';
                const errorMsg = `${response.status} - ${response.statusText}${errorDetail}`;
                if (response.status >= 400 && response.status < 500) {
                    throw new Error(`клиента: ${errorMsg}`);
                } else if (response.status >= 500 && response.status < 600) {
                    throw new Error(`сервера: ${errorMsg}`);
                } else {
                    throw new Error(errorMsg);
                }
            });
    }
    return response.json();
}

function updateTable(results) {
    const tbody = document.querySelector("#resultsTable tbody");
    results.forEach(result => {
        const date = new Date(result.currentTime);
        const formattedTime = date.toLocaleString('ru-RU', stat).replace(/,/, ' ');

        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${result.x}</td>
            <td>${result.y}</td>
            <td>${result.r}</td>
            <td data-result="${result.hit}">${result.hit ? "Попадание" : "Промах"}</td>
            <td>${formattedTime}</td>
            <td>${result.executionTime} ns</td>
        `;
        tbody.insertBefore(row, tbody.firstChild);
    });
}