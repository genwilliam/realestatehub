<template>
  <div class="page">
    <div class="card">
      <h2>{{ mode === 'login' ? '登录' : '注册' }}</h2>
      <label>
        <span>手机号</span>
        <input v-model="form.phone" type="text" placeholder="请输入手机号" />
      </label>
      <label>
        <span>密码</span>
        <input v-model="form.password" type="password" placeholder="请输入密码" />
      </label>
      <label v-if="mode === 'register'">
        <span>姓名</span>
        <input v-model="form.name" type="text" placeholder="请输入姓名" />
      </label>
      <div class="actions">
        <button class="primary" @click="submit">{{ mode === 'login' ? '登录' : '注册并登录' }}</button>
        <button class="ghost" @click="toggleMode">
          切换到{{ mode === 'login' ? '注册' : '登录' }}
        </button>
        <button class="ghost" @click="$router.push('/')">返回首页</button>
      </div>
      <p v-if="msg" class="msg">{{ msg }}</p>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue';
import { api } from '../../api/http';
import { setToken, setUserInfo } from '../../utils/auth';
import { useRouter } from 'vue-router';

const router = useRouter();
const mode = ref<'login' | 'register'>('login');
const msg = ref('');
const form = reactive({
  phone: '',
  password: '',
  name: '',
});

async function submit() {
  msg.value = '';
  try {
    const path = mode.value === 'login' ? '/api/auth/login' : '/api/auth/register';
    const payload: any = { phone: form.phone, password: form.password };
    if (mode.value === 'register') payload.name = form.name;
    const data = await api(path, { method: 'POST', body: JSON.stringify(payload) });
    setToken(data.token);
    setUserInfo({ name: data.name, role: data.role });
    router.push('/');
  } catch (e: any) {
    msg.value = e.message || '请求失败';
  }
}

function toggleMode() {
  mode.value = mode.value === 'login' ? 'register' : 'login';
  msg.value = '';
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f5f6f8;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
}
.card {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 20px;
  width: min(420px, 100%);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.06);
  display: flex;
  flex-direction: column;
  gap: 12px;
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
.actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
.primary {
  padding: 10px 14px;
  border-radius: 6px;
  border: none;
  background: #2563eb;
  color: #fff;
  cursor: pointer;
  font-weight: 600;
}
.ghost {
  padding: 10px 14px;
  border-radius: 6px;
  border: 1px solid #e5e7eb;
  background: #fff;
  cursor: pointer;
  font-weight: 600;
}
.msg {
  color: #ef4444;
  margin: 0;
}
</style>
