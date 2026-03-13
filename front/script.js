const ctx = document.getElementById('myChart');
let chart; // IMPORTANT

createChart();

async function getRoverPositions() {

    try {
        const response = await fetch("http://localhost:8080/rovers");
        const data = await response.json();

        console.log(data);

        return data.map(rover => ({
            x: rover.position.x,
            y: rover.position.y
        }));

    } catch (error) {
        console.error("Erreur récupération rover :", error);
        return [];
    }
}

async function createChart() {

    const rovers = await getRoverPositions();

    chart = new Chart(ctx, {
        type: 'scatter',
        data: {
            datasets: [{
                label: 'Rovers',
                data: rovers,
            }]
        },
        options: {
            scales: {
                x: {
                    type: 'linear',
                    position: 'bottom',
                    title: {
                        display: true,
                        text: 'Axe X'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Axe Y'
                    }
                }
            }
        }
    });
}

async function updateChart() {

    const rovers = await getRoverPositions();

    chart.data.datasets[0].data = rovers;

    chart.update();
}

async function moveRover() {

    const command = document.getElementById("getCommand").value;
    const id = document.getElementById("getId").value;

    const response = await fetch(
        `http://localhost:8080/rovers/${id}/move?commands=${command}`,
        {
            method: "POST"
        }
    );

    const rover = await response.json();

    await updateChart();

    return rover;
}

async function createRover(x, y, direction) {

    const response = await fetch(
        `http://localhost:8080/rovers?x=${x}&y=${y}&direction=${direction}`,
        {
            method: "POST"
        }
    );

    const rover = await response.json();

    console.log(rover);

    await updateChart();

    return rover;
}