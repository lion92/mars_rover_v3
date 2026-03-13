const API = "http://localhost:8080/rovers";

// ── Grid config ──────────────────────────────────────────────────────────────
const GRID_HALF = 10;          // grid goes from -10 to +10
const GRID_SIZE = GRID_HALF * 2 + 1; // 21 cells
const CELL = 30;               // px per cell
const CANVAS_PX = GRID_SIZE * CELL; // 630 px

const canvas = document.getElementById("roverCanvas");
const ctx    = canvas.getContext("2d");
canvas.width  = CANVAS_PX;
canvas.height = CANVAS_PX;

// ── Rover colors ─────────────────────────────────────────────────────────────
const PALETTE = [
    "#00e676", "#2196f3", "#ff5722",
    "#ffeb3b", "#e91e63", "#00bcd4",
    "#ff9800", "#9c27b0"
];
let colorIdx = 0;

// rovers map: id -> { id, x, y, direction, color, trail[] }
const rovers = {};

// ── Direction helpers ─────────────────────────────────────────────────────────
const DIR_ANGLE  = { N: -Math.PI/2, S: Math.PI/2, E: 0, W: Math.PI };
const TURN_LEFT  = { N:"W", W:"S", S:"E", E:"N" };
const TURN_RIGHT = { N:"E", E:"S", S:"W", W:"N" };
const MOVE_DELTA = { N:[0,1], S:[0,-1], E:[1,0], W:[-1,0] };

// ── Coordinate conversion ─────────────────────────────────────────────────────
// grid (gx, gy) → canvas pixel (cx, cy), Y-axis flipped (screen Y down)
function g2c(gx, gy) {
    return {
        cx: (gx + GRID_HALF) * CELL + CELL / 2,
        cy: (GRID_HALF - gy) * CELL + CELL / 2
    };
}

// ── Mars craters (fixed seed) ─────────────────────────────────────────────────
const craters = [];
(function seedCraters() {
    let s = 42;
    function rand() { s = (s * 1664525 + 1013904223) & 0xffffffff; return (s >>> 0) / 0xffffffff; }
    for (let i = 0; i < 28; i++) {
        craters.push({
            x: rand() * CANVAS_PX,
            y: rand() * CANVAS_PX,
            r: 3 + rand() * 9
        });
    }
})();

// ── Drawing ───────────────────────────────────────────────────────────────────
function drawBackground() {
    // Mars surface gradient
    const grad = ctx.createRadialGradient(
        CANVAS_PX/2, CANVAS_PX/2, 80,
        CANVAS_PX/2, CANVAS_PX/2, CANVAS_PX
    );
    grad.addColorStop(0, "#1f0d05");
    grad.addColorStop(1, "#0a0502");
    ctx.fillStyle = grad;
    ctx.fillRect(0, 0, CANVAS_PX, CANVAS_PX);

    // Craters
    craters.forEach(c => {
        ctx.beginPath();
        ctx.arc(c.x, c.y, c.r, 0, Math.PI*2);
        ctx.strokeStyle = "#3a1a08";
        ctx.lineWidth = 1.5;
        ctx.stroke();
        ctx.fillStyle = "#130804";
        ctx.fill();
    });
}

function drawGrid() {
    // Minor grid lines
    ctx.strokeStyle = "#2a1206";
    ctx.lineWidth = 0.5;
    for (let i = 0; i <= GRID_SIZE; i++) {
        const px = i * CELL;
        ctx.beginPath(); ctx.moveTo(px, 0); ctx.lineTo(px, CANVAS_PX); ctx.stroke();
        ctx.beginPath(); ctx.moveTo(0, px); ctx.lineTo(CANVAS_PX, px); ctx.stroke();
    }

    // Axes
    ctx.strokeStyle = "#6a3010";
    ctx.lineWidth = 1;
    const ox = GRID_HALF * CELL;
    ctx.beginPath(); ctx.moveTo(ox, 0); ctx.lineTo(ox, CANVAS_PX); ctx.stroke();
    const oy = GRID_HALF * CELL;
    ctx.beginPath(); ctx.moveTo(0, oy); ctx.lineTo(CANVAS_PX, oy); ctx.stroke();

    // Axis labels every 5 cells
    ctx.fillStyle = "#5a3020";
    ctx.font = "9px 'Courier New'";
    ctx.textAlign = "center";
    for (let g = -GRID_HALF; g <= GRID_HALF; g += 5) {
        if (g === 0) continue;
        const { cx } = g2c(g, 0);
        const { cy } = g2c(0, g);
        // X axis
        ctx.fillText(g, cx, GRID_HALF * CELL + 11);
        // Y axis
        ctx.textAlign = "right";
        ctx.fillText(g, GRID_HALF * CELL - 4, cy + 3);
        ctx.textAlign = "center";
    }

    // Origin label
    ctx.fillStyle = "#7a4030";
    ctx.fillText("0", GRID_HALF * CELL + 10, GRID_HALF * CELL + 11);
}

function drawTrail(rover) {
    if (rover.trail.length < 2) return;
    ctx.lineWidth = 2;
    ctx.strokeStyle = rover.color + "55";
    ctx.setLineDash([4, 4]);
    ctx.beginPath();
    const p0 = g2c(rover.trail[0].x, rover.trail[0].y);
    ctx.moveTo(p0.cx, p0.cy);
    for (let i = 1; i < rover.trail.length; i++) {
        const p = g2c(rover.trail[i].x, rover.trail[i].y);
        ctx.lineTo(p.cx, p.cy);
    }
    ctx.stroke();
    ctx.setLineDash([]);
}

function drawRover(rover) {
    const { cx, cy } = g2c(rover.x, rover.y);
    const angle = DIR_ANGLE[rover.direction];

    ctx.save();
    ctx.translate(cx, cy);
    ctx.rotate(angle);

    // Glow
    ctx.shadowBlur = 12;
    ctx.shadowColor = rover.color;

    // Body
    ctx.fillStyle = rover.color;
    ctx.beginPath();
    ctx.roundRect(-9, -6, 18, 12, 2);
    ctx.fill();

    // Direction arrow on front
    ctx.fillStyle = "#000a";
    ctx.beginPath();
    ctx.moveTo(12, 0);
    ctx.lineTo(6, -5);
    ctx.lineTo(6, 5);
    ctx.closePath();
    ctx.fill();

    // Wheels (4)
    ctx.fillStyle = "#000";
    ctx.shadowBlur = 0;
    [[-10, -9], [-10, 5], [4, -9], [4, 5]].forEach(([wx, wy]) => {
        ctx.fillRect(wx, wy, 6, 4);
    });

    // Solar panel bar
    ctx.fillStyle = "#1a6aaa";
    ctx.fillRect(-2, -10, 4, 3);
    ctx.fillStyle = "#2288cc";
    ctx.fillRect(-6, -12, 12, 4);

    ctx.restore();

    // ID label above rover
    ctx.save();
    ctx.shadowBlur = 6;
    ctx.shadowColor = rover.color;
    ctx.fillStyle = rover.color;
    ctx.font = "bold 10px 'Courier New'";
    ctx.textAlign = "center";
    ctx.fillText(`#${rover.id}`, cx, cy - 18);
    ctx.restore();
}

function render() {
    drawBackground();
    drawGrid();
    Object.values(rovers).forEach(r => drawTrail(r));
    Object.values(rovers).forEach(r => drawRover(r));
}

// ── Step simulation (client-side for animation) ───────────────────────────────
function computeSteps(rover, commands) {
    let x = rover.x, y = rover.y, dir = rover.direction;
    return [...commands.toUpperCase()].map(c => {
        if (c === "F") { const [dx, dy] = MOVE_DELTA[dir]; x += dx; y += dy; }
        else if (c === "B") { const [dx, dy] = MOVE_DELTA[dir]; x -= dx; y -= dy; }
        else if (c === "L") dir = TURN_LEFT[dir];
        else if (c === "R") dir = TURN_RIGHT[dir];
        return { x, y, direction: dir };
    });
}

function sleep(ms) { return new Promise(r => setTimeout(r, ms)); }

async function animateSteps(roverId, steps) {
    const rover = rovers[roverId];
    const delay = parseInt(document.getElementById("speed").value);
    for (const step of steps) {
        rover.x = step.x;
        rover.y = step.y;
        rover.direction = step.direction;
        rover.trail.push({ x: step.x, y: step.y });
        render();
        updateCoordsDisplay(rover);
        await sleep(delay);
    }
}

// ── Legend ────────────────────────────────────────────────────────────────────
function updateLegend() {
    const el = document.getElementById("legend");
    el.innerHTML = Object.values(rovers).map(r =>
        `<div class="legend-item">
            <div class="legend-dot" style="background:${r.color}"></div>
            <span>Rover #${r.id} — (${r.x},${r.y}) ${r.direction}</span>
        </div>`
    ).join("") || '<span style="font-size:.7rem;color:#555">Aucun rover déployé</span>';
}

function updateCoordsDisplay(rover) {
    document.getElementById("coordsInfo").textContent =
        rover ? `#${rover.id} → (${rover.x}, ${rover.y}) cap ${rover.direction}` : "—";
}

// ── API calls ─────────────────────────────────────────────────────────────────
async function createRover() {
    const x         = parseInt(document.getElementById("x").value) || 0;
    const y         = parseInt(document.getElementById("y").value) || 0;
    const direction = document.getElementById("direction").value;

    try {
        const res  = await fetch(`${API}?x=${x}&y=${y}&direction=${direction}`, { method: "POST" });
        const data = await res.json();
        showResult(data);

        rovers[data.id] = {
            id:        data.id,
            x:         data.position.x,
            y:         data.position.y,
            direction: data.direction,
            color:     PALETTE[colorIdx++ % PALETTE.length],
            trail:     [{ x: data.position.x, y: data.position.y }]
        };

        document.getElementById("roverId").value = data.id;
        render();
        updateLegend();
        updateCoordsDisplay(rovers[data.id]);
    } catch (e) {
        showResult({ error: e.message });
    }
}

async function moveRover() {
    const id       = parseInt(document.getElementById("roverId").value);
    const commands = document.getElementById("commands").value.trim();

    if (!commands) return;
    if (!rovers[id]) {
        showResult({ error: `Rover #${id} introuvable. Créez-le d'abord.` });
        return;
    }

    const btn = document.getElementById("moveBtn");
    btn.disabled = true;

    const steps = computeSteps(rovers[id], commands);
    await animateSteps(id, steps);

    try {
        const res  = await fetch(`${API}/${id}/move?commands=${commands}`, { method: "POST" });
        const data = await res.json();
        showResult(data);

        // Sync with server state
        rovers[id].x         = data.position.x;
        rovers[id].y         = data.position.y;
        rovers[id].direction = data.direction;
        render();
        updateLegend();
        updateCoordsDisplay(rovers[id]);
    } catch (e) {
        showResult({ error: e.message });
    }

    btn.disabled = false;
}

function showResult(data) {
    document.getElementById("result").textContent = JSON.stringify(data, null, 2);
}

// ── Speed slider ──────────────────────────────────────────────────────────────
document.getElementById("speed").addEventListener("input", function () {
    document.getElementById("speedLabel").textContent = this.value + "ms";
});

// ── Canvas hover: show coords ─────────────────────────────────────────────────
canvas.addEventListener("mousemove", function (e) {
    const rect = canvas.getBoundingClientRect();
    const mx = e.clientX - rect.left;
    const my = e.clientY - rect.top;
    const gx = Math.floor(mx / CELL) - GRID_HALF;
    const gy = GRID_HALF - Math.floor(my / CELL);
    canvas.title = `(${gx}, ${gy})`;
});

// ── Init : charge les rovers depuis la BDD ────────────────────────────────────
async function loadRoversFromDB() {
    try {
        const res  = await fetch(API);
        const list = await res.json();

        list.forEach(data => {
            rovers[data.id] = {
                id:        data.id,
                x:         data.position.x,
                y:         data.position.y,
                direction: data.direction,
                color:     PALETTE[colorIdx++ % PALETTE.length],
                trail:     [{ x: data.position.x, y: data.position.y }]
            };
        });

        if (list.length > 0) {
            const last = list[list.length - 1];
            document.getElementById("roverId").value = last.id;
            updateCoordsDisplay(rovers[last.id]);
        }

        render();
        updateLegend();
    } catch (e) {
        showResult({ error: "Impossible de charger les rovers : " + e.message });
        render();
        updateLegend();
    }
}

loadRoversFromDB();
