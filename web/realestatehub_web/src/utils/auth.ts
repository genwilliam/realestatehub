import { setToken as setHttpToken } from '../api/http';

const TOKEN_KEY = 'reh_token';
const USER_KEY = 'reh_user';

export function setToken(token: string | null) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token);
  } else {
    localStorage.removeItem(TOKEN_KEY);
  }
  setHttpToken(token);
}

export function getToken(): string | null {
  const t = localStorage.getItem(TOKEN_KEY);
  if (t) setHttpToken(t);
  return t;
}

export function setUserInfo(info: any) {
  if (info) {
    localStorage.setItem(USER_KEY, JSON.stringify(info));
  } else {
    localStorage.removeItem(USER_KEY);
  }
}

export function getUserInfo(): any {
  const val = localStorage.getItem(USER_KEY);
  return val ? JSON.parse(val) : null;
}

export function clearAuth() {
  setToken(null);
  setUserInfo(null);
}

