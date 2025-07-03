document.addEventListener('DOMContentLoaded', () => {
    const canvas = document.getElementById('graphCanvas');
    const ctx = canvas.getContext('2d');

    function getLabel(val, R) {
        if (val === -R) return `-R`;
        if (val === -R / 2) return `-R/2`;
        if (val === R / 2) return `R/2`;
        if (val === R) return `R`;
        return "";
    }

    function drawGraph() {
        const width = canvas.width;
        const height = canvas.height;
        const centerX = width / 2;
        const centerY = height / 2;
        const scaleX = width / 11;
        const scaleY = height / 11;
        const R = 4;

        ctx.clearRect(0, 0, width, height);

        ctx.lineWidth = 2;

        // фигуры
        ctx.fillStyle = 'rgba(163,65,161,0.55)';

        ctx.beginPath();
        ctx.moveTo(centerX, centerY + R * scaleY);
        ctx.lineTo(centerX + (R / 2) * scaleX, centerY);
        ctx.lineTo(centerX, centerY);
        ctx.closePath();
        ctx.fill();

        ctx.beginPath();
        ctx.arc(centerX, centerY, R / 2 * scaleY, 1/2 * Math.PI, -Math.PI);
        ctx.lineTo(centerX, centerY);
        ctx.closePath();
        ctx.fill();

        ctx.beginPath();
        ctx.moveTo(centerX + R / 2 * scaleX, centerY);
        ctx.lineTo(centerX, centerY);
        ctx.lineTo(centerX, centerY - R * scaleY);
        ctx.lineTo(centerX + R / 2 * scaleX, centerY - R * scaleY);
        ctx.closePath();
        ctx.fill();

        ctx.strokeStyle = '#2d4057';
        ctx.fillStyle = '#2d4057';
        ctx.font = '16px Arial';

        // ось x + стрелка
        ctx.beginPath();
        ctx.moveTo(0, centerY);
        ctx.lineTo(width - 5, centerY);
        ctx.stroke();

        ctx.beginPath();
        ctx.moveTo(width - 15, centerY - 5);
        ctx.lineTo(width - 5, centerY);
        ctx.lineTo(width - 15, centerY + 5);
        ctx.fill();

        [-5, -4, -3, -2, -1, 1, 2, 3, 4, 5].forEach(x => {
            const xPos = centerX + x * scaleX;
            ctx.beginPath();
            ctx.moveTo(xPos, centerY - 5);
            ctx.lineTo(xPos, centerY + 5);
            ctx.stroke();

            const label = getLabel(x, R);

            ctx.fillText(label, xPos - 5, centerY + 20);
        });

        // ось y + стрелка
        ctx.beginPath();
        ctx.moveTo(centerX, height);
        ctx.lineTo(centerX, 5);
        ctx.stroke();

        ctx.beginPath();
        ctx.moveTo(centerX - 5, 15);
        ctx.lineTo(centerX, 5);
        ctx.lineTo(centerX + 5, 15);
        ctx.fill();

        [-5, -4, -3, -2, -1, 1, 2, 3, 4, 5].forEach(y => {
            const yPos = centerY - y * scaleY;
            ctx.beginPath();
            ctx.moveTo(centerX - 5, yPos);
            ctx.lineTo(centerX + 5, yPos);
            ctx.stroke();

            const label = getLabel(y, R);

            ctx.fillText(label, centerX + 10, yPos + 5);
        });

        ctx.beginPath();
        ctx.arc(centerX, centerY, 5, 0, Math.PI * 2);
        ctx.fill();
        ctx.moveTo(centerX, centerY);
        ctx.stroke();
        ctx.fillText("0", centerX + 10, centerY + 20);
    }

    drawGraph();
});