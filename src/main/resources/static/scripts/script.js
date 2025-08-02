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

async function handleFormSubmit() {
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

    try {
        const response = await fetch(fetchOptions.url, fetchOptions);
        const results = await handleResponse(response);
        updateTable(results);
    } catch (error) {
        showNotification("Ошибка: " + error.message);
    }
}

async function handleResponse(response) {
    if (response.ok) {
        return await response.json();
    }

    let errorMsg = `${response.status} - ${response.statusText || 'Unknown Error'}`;
    let errorDetail = '';

    try {
        const errorData = await response.json();
        if (errorData?.error) {
            errorDetail = `: ${errorData.error}`;
        }
    } catch (e) {}

    errorMsg += errorDetail;

    if (response.status >= 400 && response.status < 500) {
        throw new Error(`Ошибка клиента: ${errorMsg}`);
    } else if (response.status >= 500 && response.status < 600) {
        throw new Error(`Ошибка сервера: ${errorMsg}`);
    } else {
        throw new Error(errorMsg);
    }
}

function updateTable(results) {
    const tbody = document.querySelector("#resultsTable tbody");
    results.forEach(result => {
        const date = new Date(result.currentTime);
        const formattedTime = date.toLocaleString('ru-RU', stat).replace(/,/, ' ');

        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${Number(result.x).toFixed(4).replace(/\.?0+$/, '')}</td>
            <td>${Number(result.y).toFixed(4).replace(/\.?0+$/, '')}</td>
            <td>${Number(result.r).toFixed(4).replace(/\.?0+$/, '')}</td>
            <td data-result="${result.hit}">${result.hit ? "Попадание" : "Промах"}</td>
            <td>${formattedTime}</td>
            <td>${result.executionTime} ns</td>
        `;
        tbody.insertBefore(row, tbody.firstChild);
    });
}