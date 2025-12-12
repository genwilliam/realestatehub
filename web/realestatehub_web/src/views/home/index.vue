<template>
  <div class="page">
    <header class="topbar">
      <div class="brand">RealEstateHub</div>
      <div class="nav-actions">
        <span v-if="userInfo">已登录：{{ userInfo.name }} ({{ userInfo.role }})</span>
        <button v-if="userInfo" class="ghost" @click="logout">退出</button>
        <button v-else class="ghost" @click="$router.push('/login')">登录 / 注册</button>
      </div>
    </header>

    <main class="layout">
      <section class="card">
        <h2>房源搜索</h2>
        <div class="form-grid">
          <label>
            <span>区域</span>
            <input v-model="query.region" type="text" placeholder="区域" />
          </label>
          <label>
            <span>户型</span>
            <input v-model="query.layout" type="text" placeholder="户型" />
          </label>
          <label>
            <span>最低价</span>
            <input v-model.number="query.minPrice" type="number" placeholder="最低价" />
          </label>
          <label>
            <span>最高价</span>
            <input v-model.number="query.maxPrice" type="number" placeholder="最高价" />
          </label>
          <button class="primary" @click="search">搜索</button>
        </div>
      </section>

      <section class="card">
        <div class="section-header">
          <h2>房源列表</h2>
          <span class="muted">点击卡片查看详情</span>
        </div>
        <div class="list">
          <div
            v-for="house in houses"
            :key="house.id"
            class="list-item"
            @click="selectHouse(house.id)"
          >
            <h3>{{ house.title || '未命名房源' }}</h3>
            <p class="meta">价格：{{ display(house.price) }} | 面积：{{ display(house.area) }}㎡</p>
            <p class="meta">区域：{{ house.region || '-' }} | 户型：{{ house.layout || '-' }}</p>
          </div>
          <div v-if="!houses.length" class="empty">暂无房源</div>
        </div>
      </section>

      <section class="card">
        <div class="section-header">
          <h2>房源详情</h2>
          <span class="muted">包含收藏与预约操作</span>
        </div>
        <div v-if="selectedHouse" class="detail">
          <h3>{{ selectedHouse.title }}</h3>
          <p class="muted">{{ selectedHouse.description || '暂无描述' }}</p>
          <p>价格：{{ display(selectedHouse.price) }} | 面积：{{ display(selectedHouse.area) }}㎡ | 户型：{{ selectedHouse.layout || '-' }}</p>
          <p>区域：{{ selectedHouse.region || '-' }} | 地址：{{ selectedHouse.address || '-' }}</p>
          <p>经纪人：{{ selectedHouse.agentName || '-' }} ({{ selectedHouse.agentPhone || '-' }})</p>
          <p>标签：{{ (selectedHouse.tags || []).join('、') || '无' }}</p>
          <div class="actions">
            <input v-model="appointmentTime" type="datetime-local" />
            <button class="primary" :disabled="!userInfo" @click="createAppointment">提交预约</button>
            <button class="secondary" :disabled="!userInfo" @click="toggleFavorite">收藏 / 取消收藏</button>
          </div>
        </div>
        <div v-else class="empty">请选择房源查看详情</div>
      </section>
    </main>

    <div v-if="toastMsg" class="toast" :class="{ show: toastMsg }">{{ toastMsg }}</div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { api } from '../../api/http';
import { getToken, getUserInfo, setToken, setUserInfo, clearAuth } from '../../utils/auth';

interface House {
  id: number;
  title: string;
  description?: string;
  price?: number;
  area?: number;
  region?: string;
  address?: string;
  layout?: string;
  tags?: string[];
  agentName?: string;
  agentPhone?: string;
}

const houses = ref<House[]>([]);
const selectedHouse = ref<House | null>(null);
const appointmentTime = ref('');
const toastMsg = ref('');

const query = reactive({
  region: '',
  layout: '',
  minPrice: null as number | null,
  maxPrice: null as number | null,
});

const userInfo = ref(getUserInfo());

function showToast(msg: string) {
  toastMsg.value = msg;
  setTimeout(() => (toastMsg.value = ''), 2000);
}

function display(val: unknown) {
  return val ?? '-';
}

async function search() {
  const params = new URLSearchParams();
  if (query.region) params.append('region', query.region);
  if (query.layout) params.append('layout', query.layout);
  if (query.minPrice != null) params.append('minPrice', String(query.minPrice));
  if (query.maxPrice != null) params.append('maxPrice', String(query.maxPrice));
  params.append('page', '0');
  params.append('size', '12');
  try {
    const page = await api(`/api/houses?${params.toString()}`);
    houses.value = page.content || [];
    if (houses.value.length) {
      await selectHouse(houses.value[0].id);
    } else {
      selectedHouse.value = null;
    }
  } catch (e: any) {
    showToast(e.message || '加载失败');
  }
}

async function selectHouse(id: number) {
  try {
    const detail = await api(`/api/houses/${id}`);
    selectedHouse.value = detail;
  } catch (e: any) {
    showToast(e.message || '加载失败');
  }
}

async function toggleFavorite() {
  if (!selectedHouse.value) return showToast('请先选择房源');
  try {
    await api(`/api/favorites/${selectedHouse.value.id}`, { method: 'POST' });
    showToast('收藏成功（若已收藏则保持收藏）');
  } catch {
    try {
      await api(`/api/favorites/${selectedHouse.value.id}`, { method: 'DELETE' });
      showToast('已取消收藏');
    } catch (err: any) {
      showToast(err.message || '操作失败');
    }
  }
}

async function createAppointment() {
  if (!selectedHouse.value) return showToast('请先选择房源');
  if (!appointmentTime.value) return showToast('请选择预约时间');
  try {
    await api('/api/appointments', {
      method: 'POST',
      body: JSON.stringify({
        houseId: selectedHouse.value.id,
        scheduledAt: appointmentTime.value,
      }),
    });
    showToast('预约提交成功');
  } catch (e: any) {
    showToast(e.message || '提交失败');
  }
}

function logout() {
  clearAuth();
  userInfo.value = null;
  setToken(null);
  setUserInfo(null);
  showToast('已退出登录');
}

onMounted(() => {
  const token = getToken();
  if (token) {
    setToken(token); // ensure header uses existing token
  }
  search();
});
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f5f6f8;
  color: #1f2937;
}
.topbar {
  position: sticky;
  top: 0;
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  background: #fff;
  border-bottom: 1px solid #e5e7eb;
}
.brand {
  font-weight: 700;
  font-size: 18px;
}
.nav-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}
.layout {
  max-width: 1140px;
  margin: 20px auto;
  padding: 0 16px 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.card {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}
.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 12px;
  align-items: end;
}
label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-weight: 600;
  color: #374151;
}
input {
  padding: 10px 12px;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  font-size: 14px;
}
.primary,
.secondary,
.ghost {
  padding: 10px 14px;
  border-radius: 6px;
  border: 1px solid transparent;
  cursor: pointer;
  font-weight: 600;
}
.primary {
  background: #2563eb;
  color: #fff;
}
.secondary {
  background: #10b981;
  color: #fff;
}
.ghost {
  background: #fff;
  border-color: #e5e7eb;
}
.list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 12px;
}
.list-item {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 12px;
  background: #fff;
  cursor: pointer;
}
.list-item h3 {
  margin: 0 0 6px;
  font-size: 16px;
}
.meta {
  color: #6b7280;
  margin: 4px 0;
}
.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
}
.muted {
  color: #9ca3af;
  font-size: 14px;
}
.detail {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 8px;
}
.actions input {
  max-width: 240px;
}
.empty {
  padding: 12px;
  color: #9ca3af;
}
.toast {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background: #111827;
  color: #fff;
  padding: 12px 16px;
  border-radius: 8px;
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.2s, transform 0.2s;
}
.toast.show {
  opacity: 0.94;
  transform: translateY(0);
}
</style>

