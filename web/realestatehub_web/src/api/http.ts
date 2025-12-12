const API_BASE = import.meta.env.VITE_API_BASE || '';

let token: string | null = null;

export function setToken(t: string | null) {
  token = t;
}

export async function api(path: string, options: RequestInit = {}) {
  const res = await fetch(API_BASE + path, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers || {}),
    },
  });

  const data = await res.json().catch(() => ({}));
  if (!res.ok || data.success === false) {
    throw new Error(data.message || '请求失败');
  }
  return data.data !== undefined ? data.data : data;
}

