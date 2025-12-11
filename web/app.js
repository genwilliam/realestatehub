const API_BASE = "http://localhost:8080";

const state = {
    token: null,
    selectedHouse: null
};

// 工具函数
function toast(msg) {
    const el = document.getElementById("toast");
    el.textContent = msg;
    el.classList.add("show");
    setTimeout(() => el.classList.remove("show"), 2000);
}

function setAuthStatus(text) {
    document.getElementById("auth-status").textContent = text;
}

function authHeaders() {
    return state.token ? { "Authorization": "Bearer " + state.token } : {};
}

async function api(path, options = {}) {
    const res = await fetch(API_BASE + path, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...authHeaders(),
            ...(options.headers || {})
        }
    });
    const data = await res.json().catch(() => ({}));
    if (!res.ok || data.success === false) {
        throw new Error(data.message || "请求失败");
    }
    return data.data !== undefined ? data.data : data;
}

// 认证
async function login() {
    const phone = document.getElementById("phone").value.trim();
    const password = document.getElementById("password").value;
    if (!phone || !password) return toast("请输入手机号和密码");
    try {
        const data = await api("/api/auth/login", {
            method: "POST",
            body: JSON.stringify({ phone, password })
        });
        state.token = data.token;
        setAuthStatus(`已登录：${data.name} (${data.role})`);
        toast("登录成功");
    } catch (e) {
        toast(e.message);
    }
}

async function register() {
    const phone = document.getElementById("phone").value.trim();
    const password = document.getElementById("password").value;
    const name = document.getElementById("name").value.trim();
    if (!phone || !password || !name) return toast("请填写手机号、密码、姓名");
    try {
        const data = await api("/api/auth/register", {
            method: "POST",
            body: JSON.stringify({ phone, password, name })
        });
        state.token = data.token;
        setAuthStatus(`已登录：${data.name} (${data.role})`);
        toast("注册成功");
    } catch (e) {
        toast(e.message);
    }
}

function logout() {
    state.token = null;
    setAuthStatus("未登录");
    toast("已退出");
}

// 房源
async function searchHouses() {
    const region = document.getElementById("q-region").value.trim();
    const layout = document.getElementById("q-layout").value.trim();
    const minPrice = document.getElementById("q-min").value;
    const maxPrice = document.getElementById("q-max").value;
    const params = new URLSearchParams();
    if (region) params.append("region", region);
    if (layout) params.append("layout", layout);
    if (minPrice) params.append("minPrice", minPrice);
    if (maxPrice) params.append("maxPrice", maxPrice);
    params.append("page", 0);
    params.append("size", 12);
    try {
        const page = await api(`/api/houses?${params.toString()}`);
        renderHouses(page.content || []);
        toast("搜索完成");
    } catch (e) {
        toast(e.message);
    }
}

function renderHouses(list) {
    const container = document.getElementById("houses");
    container.innerHTML = "";
    list.forEach(h => {
        const item = document.createElement("div");
        item.className = "item";
        item.innerHTML = `
            <h3>${h.title || "未命名房源"}</h3>
            <div class="meta">价格：${h.price ?? "未填"} | 面积：${h.area ?? "-"}㎡</div>
            <div class="meta">区域：${h.region || "-"} | 户型：${h.layout || "-"}</div>
        `;
        item.onclick = () => selectHouse(h.id);
        container.appendChild(item);
    });
    if (!list.length) container.innerHTML = "<div class='item'>暂无房源</div>";
}

async function selectHouse(id) {
    try {
        const h = await api(`/api/houses/${id}`);
        state.selectedHouse = h;
        renderHouseDetail(h);
    } catch (e) {
        toast(e.message);
    }
}

function renderHouseDetail(h) {
    const detail = document.getElementById("house-detail");
    detail.innerHTML = `
        <h3>${h.title}</h3>
        <p>${h.description || "暂无描述"}</p>
        <p>价格：${h.price ?? "-"} | 面积：${h.area ?? "-"}㎡ | 户型：${h.layout || "-"}</p>
        <p>区域：${h.region || "-"} | 地址：${h.address || "-"}</p>
        <p>经纪人：${h.agentName || "-"} (${h.agentPhone || "-"})</p>
        <p>标签：${(h.tags || []).join("、") || "无"}</p>
    `;
}

// 收藏与预约
async function toggleFavorite() {
    if (!state.selectedHouse) return toast("请先选择房源");
    if (!state.token) return toast("请先登录");
    const houseId = state.selectedHouse.id;
    try {
        await api(`/api/favorites/${houseId}`, { method: "POST" });
        toast("收藏成功，如已存在则保持收藏状态");
    } catch (e) {
        // 如果已收藏则尝试取消
        try {
            await api(`/api/favorites/${houseId}`, { method: "DELETE" });
            toast("已取消收藏");
        } catch (err) {
            toast(err.message);
        }
    }
}

async function makeAppointment() {
    if (!state.selectedHouse) return toast("请先选择房源");
    if (!state.token) return toast("请先登录");
    const scheduledAt = document.getElementById("appointment-time").value;
    if (!scheduledAt) return toast("请选择预约时间");
    try {
        await api("/api/appointments", {
            method: "POST",
            body: JSON.stringify({
                houseId: state.selectedHouse.id,
                scheduledAt
            })
        });
        toast("预约提交成功");
    } catch (e) {
        toast(e.message);
    }
}

// 事件绑定
document.getElementById("btn-login").onclick = login;
document.getElementById("btn-register").onclick = register;
document.getElementById("btn-logout").onclick = logout;
document.getElementById("btn-search").onclick = searchHouses;
document.getElementById("btn-fav").onclick = toggleFavorite;
document.getElementById("btn-appointment").onclick = makeAppointment;

// 初始化
searchHouses().catch(() => {});

