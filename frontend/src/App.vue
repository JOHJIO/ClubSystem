<template>
  <div v-if="!currentUser" class="login-screen">
    <form class="login-panel" @submit.prevent="login">
      <div>
        <p class="eyebrow">SSM + Vue3 实训</p>
        <h1>校园社团管理系统</h1>
      </div>
      <label>
        <span>账号</span>
        <input v-model="loginForm.username" autocomplete="username" />
      </label>
      <label>
        <span>密码</span>
        <input v-model="loginForm.password" type="password" autocomplete="current-password" />
      </label>
      <p v-if="loginError" class="error">{{ loginError }}</p>
      <button class="primary" type="submit">登录</button>
    </form>
  </div>

  <div v-else class="app-shell">
    <aside class="sidebar">
      <div class="brand">
        <School />
        <div>
          <strong>社团管理</strong>
          <span>{{ currentUser.role }}</span>
        </div>
      </div>
      <nav>
        <button
          v-for="item in navItems"
          :key="item.key"
          :class="{ active: activeKey === item.key }"
          @click="setActive(item.key)"
        >
          <component :is="item.icon" />
          <span>{{ item.label }}</span>
        </button>
      </nav>
    </aside>

    <main>
      <header class="topbar">
        <div>
          <p class="eyebrow">{{ activeTitle }}</p>
          <h2>校园社团综合管理平台</h2>
        </div>
        <div class="user-box">
          <span>{{ currentUser.username }}</span>
          <button class="ghost" @click="logout">退出</button>
        </div>
      </header>

      <section v-if="activeKey === 'dashboard'" class="dashboard">
        <div class="stat-grid">
          <article v-for="card in statsCards" :key="card.label" class="stat-card">
            <span>{{ card.label }}</span>
            <strong>{{ card.value }}</strong>
          </article>
        </div>
        <div class="panel">
          <div class="panel-head">
            <h3>近期活动</h3>
            <button class="ghost" @click="loadDashboard"><RefreshCw /></button>
          </div>
          <table>
            <thead>
              <tr>
                <th>活动名称</th>
                <th>地点</th>
                <th>开始时间</th>
                <th>容量</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in dashboard.recentActivities || []" :key="item.id">
                <td>{{ item.title }}</td>
                <td>{{ item.location }}</td>
                <td>{{ formatDate(item.startTime) }}</td>
                <td>{{ item.capacity }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <section v-else class="panel">
        <div class="panel-head">
          <div>
            <h3>{{ currentModule.label }}</h3>
            <p>{{ currentModule.description }}</p>
          </div>
          <div class="actions">
            <input v-model="keyword" placeholder="搜索关键字" @keyup.enter="loadList" />
            <button class="ghost" @click="loadList"><Search /></button>
            <button class="primary" @click="openCreate"><Plus />新增</button>
          </div>
        </div>

        <table>
          <thead>
            <tr>
              <th v-for="col in currentModule.columns" :key="col.key">{{ col.label }}</th>
              <th class="ops">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="row in rows" :key="row.id">
              <td v-for="col in currentModule.columns" :key="col.key">
                {{ displayValue(row[col.key], col) }}
              </td>
              <td class="ops">
                <button class="icon-btn" title="编辑" @click="openEdit(row)"><Pencil /></button>
                <button class="icon-btn danger" title="删除" @click="removeRow(row)"><Trash2 /></button>
              </td>
            </tr>
          </tbody>
        </table>

        <div class="pager">
          <span>共 {{ total }} 条</span>
          <button class="ghost" :disabled="page <= 1" @click="changePage(page - 1)">上一页</button>
          <span>{{ page }} / {{ pageCount }}</span>
          <button class="ghost" :disabled="page >= pageCount" @click="changePage(page + 1)">下一页</button>
        </div>
      </section>
    </main>

    <div v-if="editing" class="modal-mask" @click.self="closeEditor">
      <form class="modal" @submit.prevent="saveRow">
        <div class="modal-head">
          <h3>{{ editing.id ? '编辑' : '新增' }}{{ currentModule.label }}</h3>
          <button type="button" class="icon-btn" @click="closeEditor"><X /></button>
        </div>
        <div class="form-grid">
          <label v-for="field in currentModule.fields" :key="field.key">
            <span>{{ field.label }}</span>
            <select v-if="field.options" v-model="editing[field.key]">
              <option v-for="option in field.options" :key="option.value" :value="option.value">
                {{ option.label }}
              </option>
            </select>
            <textarea v-else-if="field.type === 'textarea'" v-model="editing[field.key]" rows="3" />
            <input v-else v-model="editing[field.key]" :type="field.type || 'text'" />
          </label>
        </div>
        <div class="modal-actions">
          <button type="button" class="ghost" @click="closeEditor">取消</button>
          <button class="primary" type="submit">保存</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import {
  Bell,
  CalendarDays,
  CircleDollarSign,
  LayoutDashboard,
  MapPin,
  Pencil,
  Plus,
  RefreshCw,
  School,
  Search,
  ShieldCheck,
  Trash2,
  UserRound,
  UsersRound,
  X
} from '@lucide/vue'

const API = '/api'

const loginForm = reactive({ username: 'admin', password: '114514' })
const loginError = ref('')
const currentUser = ref(JSON.parse(localStorage.getItem('club_user') || 'null'))
const activeKey = ref('dashboard')
const dashboard = ref({})
const keyword = ref('')
const rows = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(8)
const editing = ref(null)

const statusOptions = [
  { label: '启用', value: 1 },
  { label: '停用', value: 0 }
]

const activityStatusOptions = [
  { label: '筹备', value: 0 },
  { label: '进行中', value: 1 },
  { label: '已结束', value: 2 },
  { label: '取消', value: 3 }
]

const financeTypeOptions = [
  { label: '收入', value: 1 },
  { label: '支出', value: 2 }
]

const bookingStatusOptions = [
  { label: '待审核', value: 0 },
  { label: '通过', value: 1 },
  { label: '拒绝', value: 2 }
]

const modules = {
  clubs: {
    label: '社团管理',
    description: '维护社团基础信息、负责人和社团状态。',
    endpoint: 'clubs',
    icon: UsersRound,
    columns: [
      { key: 'name', label: '社团名称' },
      { key: 'category', label: '分类' },
      { key: 'leader', label: '负责人' },
      { key: 'contact', label: '联系方式' },
      { key: 'status', label: '状态', map: { 1: '正常', 0: '停用' } }
    ],
    fields: [
      { key: 'name', label: '社团名称' },
      { key: 'category', label: '分类' },
      { key: 'leader', label: '负责人' },
      { key: 'contact', label: '联系方式' },
      { key: 'status', label: '状态', options: statusOptions },
      { key: 'description', label: '简介', type: 'textarea' }
    ],
    defaults: { status: 1 }
  },
  members: {
    label: '成员管理',
    description: '维护学生社团成员、学院专业和入团状态。',
    endpoint: 'members',
    icon: UserRound,
    columns: [
      { key: 'name', label: '姓名' },
      { key: 'studentNo', label: '学号' },
      { key: 'college', label: '学院' },
      { key: 'major', label: '专业' },
      { key: 'clubId', label: '社团ID' },
      { key: 'status', label: '状态', map: { 1: '在团', 0: '退出' } }
    ],
    fields: [
      { key: 'name', label: '姓名' },
      { key: 'studentNo', label: '学号' },
      { key: 'gender', label: '性别' },
      { key: 'phone', label: '电话' },
      { key: 'college', label: '学院' },
      { key: 'major', label: '专业' },
      { key: 'grade', label: '年级' },
      { key: 'clubId', label: '社团ID', type: 'number' },
      { key: 'joinDate', label: '入团日期', type: 'date' },
      { key: 'status', label: '状态', options: [{ label: '在团', value: 1 }, { label: '退出', value: 0 }] }
    ],
    defaults: { status: 1 }
  },
  activities: {
    label: '活动管理',
    description: '登记社团活动、时间地点、容量和活动状态。',
    endpoint: 'activities',
    icon: CalendarDays,
    columns: [
      { key: 'title', label: '活动名称' },
      { key: 'clubId', label: '社团ID' },
      { key: 'location', label: '地点' },
      { key: 'startTime', label: '开始时间', date: true },
      { key: 'capacity', label: '容量' },
      { key: 'status', label: '状态', map: { 0: '筹备', 1: '进行中', 2: '已结束', 3: '取消' } }
    ],
    fields: [
      { key: 'clubId', label: '社团ID', type: 'number' },
      { key: 'title', label: '活动名称' },
      { key: 'location', label: '地点' },
      { key: 'startTime', label: '开始时间', type: 'datetime-local' },
      { key: 'endTime', label: '结束时间', type: 'datetime-local' },
      { key: 'capacity', label: '容量', type: 'number' },
      { key: 'status', label: '状态', options: activityStatusOptions },
      { key: 'description', label: '说明', type: 'textarea' }
    ],
    defaults: { status: 0, capacity: 30 }
  },
  announcements: {
    label: '公告管理',
    description: '发布校级和社团级通知公告。',
    endpoint: 'announcements',
    icon: Bell,
    columns: [
      { key: 'title', label: '标题' },
      { key: 'clubId', label: '社团ID' },
      { key: 'publishTime', label: '发布时间', date: true },
      { key: 'status', label: '状态', map: { 1: '发布', 0: '草稿' } }
    ],
    fields: [
      { key: 'clubId', label: '社团ID', type: 'number' },
      { key: 'title', label: '标题' },
      { key: 'content', label: '内容', type: 'textarea' },
      { key: 'publishTime', label: '发布时间', type: 'datetime-local' },
      { key: 'status', label: '状态', options: [{ label: '发布', value: 1 }, { label: '草稿', value: 0 }] }
    ],
    defaults: { status: 1 }
  },
  finance: {
    label: '经费管理',
    description: '记录社团收入、支出、经办人和备注。',
    endpoint: 'finance',
    icon: CircleDollarSign,
    columns: [
      { key: 'clubId', label: '社团ID' },
      { key: 'type', label: '类型', map: { 1: '收入', 2: '支出' } },
      { key: 'amount', label: '金额' },
      { key: 'item', label: '事项' },
      { key: 'handler', label: '经办人' },
      { key: 'recordTime', label: '时间', date: true }
    ],
    fields: [
      { key: 'clubId', label: '社团ID', type: 'number' },
      { key: 'type', label: '类型', options: financeTypeOptions },
      { key: 'amount', label: '金额', type: 'number' },
      { key: 'item', label: '事项' },
      { key: 'handler', label: '经办人' },
      { key: 'recordTime', label: '时间', type: 'datetime-local' },
      { key: 'remark', label: '备注', type: 'textarea' }
    ],
    defaults: { type: 1, amount: 0 }
  },
  venues: {
    label: '场地管理',
    description: '维护活动场地、位置、容量和使用状态。',
    endpoint: 'venues',
    icon: MapPin,
    columns: [
      { key: 'name', label: '场地名称' },
      { key: 'location', label: '位置' },
      { key: 'capacity', label: '容量' },
      { key: 'status', label: '状态', map: { 1: '可用', 0: '停用' } }
    ],
    fields: [
      { key: 'name', label: '场地名称' },
      { key: 'location', label: '位置' },
      { key: 'capacity', label: '容量', type: 'number' },
      { key: 'status', label: '状态', options: statusOptions },
      { key: 'remark', label: '备注', type: 'textarea' }
    ],
    defaults: { status: 1, capacity: 30 }
  },
  bookings: {
    label: '场地预约',
    description: '处理社团活动场地预约和审核状态。',
    endpoint: 'bookings',
    icon: ShieldCheck,
    columns: [
      { key: 'venueId', label: '场地ID' },
      { key: 'clubId', label: '社团ID' },
      { key: 'activityId', label: '活动ID' },
      { key: 'startTime', label: '开始时间', date: true },
      { key: 'status', label: '状态', map: { 0: '待审核', 1: '通过', 2: '拒绝' } }
    ],
    fields: [
      { key: 'venueId', label: '场地ID', type: 'number' },
      { key: 'clubId', label: '社团ID', type: 'number' },
      { key: 'activityId', label: '活动ID', type: 'number' },
      { key: 'startTime', label: '开始时间', type: 'datetime-local' },
      { key: 'endTime', label: '结束时间', type: 'datetime-local' },
      { key: 'status', label: '状态', options: bookingStatusOptions },
      { key: 'remark', label: '备注', type: 'textarea' }
    ],
    defaults: { status: 0 }
  }
}

const navItems = computed(() => [
  { key: 'dashboard', label: '数据看板', icon: LayoutDashboard },
  ...Object.entries(modules).map(([key, item]) => ({ key, label: item.label, icon: item.icon }))
])

const currentModule = computed(() => modules[activeKey.value])
const activeTitle = computed(() => activeKey.value === 'dashboard' ? '数据看板' : currentModule.value.label)
const pageCount = computed(() => Math.max(1, Math.ceil(total.value / size.value)))
const statsCards = computed(() => [
  { label: '社团数量', value: dashboard.value.clubCount || 0 },
  { label: '成员数量', value: dashboard.value.memberCount || 0 },
  { label: '活动数量', value: dashboard.value.activityCount || 0 },
  { label: '经费收入', value: dashboard.value.incomeTotal || 0 },
  { label: '经费支出', value: dashboard.value.expenseTotal || 0 }
])

async function request(path, options = {}) {
  const response = await fetch(`${API}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  })
  const result = await response.json()
  if (result.code !== 200) throw new Error(result.message || '请求失败')
  return result.data
}

async function login() {
  loginError.value = ''
  try {
    const user = await request('/auth/login', {
      method: 'POST',
      body: JSON.stringify(loginForm)
    })
    currentUser.value = user
    localStorage.setItem('club_user', JSON.stringify(user))
    await loadDashboard()
  } catch (error) {
    loginError.value = error.message
  }
}

function logout() {
  localStorage.removeItem('club_user')
  currentUser.value = null
}

async function loadDashboard() {
  dashboard.value = await request('/dashboard/stats')
}

async function loadList() {
  if (activeKey.value === 'dashboard') return
  const endpoint = currentModule.value.endpoint
  const params = new URLSearchParams({ page: page.value, size: size.value })
  if (keyword.value) params.set('keyword', keyword.value)
  const data = await request(`/${endpoint}?${params}`)
  rows.value = data.rows || []
  total.value = data.total || 0
}

function setActive(key) {
  activeKey.value = key
  keyword.value = ''
  page.value = 1
  editing.value = null
  if (key === 'dashboard') loadDashboard()
  else loadList()
}

function openCreate() {
  editing.value = { ...(currentModule.value.defaults || {}) }
}

function openEdit(row) {
  editing.value = { ...row }
  currentModule.value.fields.forEach((field) => {
    if (field.type === 'date') editing.value[field.key] = toDateInput(row[field.key])
    if (field.type === 'datetime-local') editing.value[field.key] = toDateTimeInput(row[field.key])
  })
}

function closeEditor() {
  editing.value = null
}

async function saveRow() {
  const endpoint = currentModule.value.endpoint
  const body = normalizeFormBody(editing.value)
  const method = body.id ? 'PUT' : 'POST'
  const path = body.id ? `/${endpoint}/${body.id}` : `/${endpoint}`
  await request(path, { method, body: JSON.stringify(body) })
  editing.value = null
  await loadList()
  if (activeKey.value !== 'dashboard') await loadDashboard()
}

function normalizeFormBody(source) {
  const body = { ...source }
  currentModule.value.fields.forEach((field) => {
    const value = body[field.key]
    if (value === '') {
      body[field.key] = null
      return
    }
    if (field.type === 'datetime-local' && value) {
      body[field.key] = normalizeDateTime(value)
      return
    }
    if (field.type === 'date' && value) {
      body[field.key] = `${value} 00:00:00`
      return
    }
    if (field.type === 'number' && value !== null && value !== undefined) {
      body[field.key] = Number(value)
    }
  })
  return body
}

function normalizeDateTime(value) {
  const text = String(value).replace('T', ' ')
  return text.length === 16 ? `${text}:00` : text
}

async function removeRow(row) {
  const ok = window.confirm(`确认删除 ID ${row.id} 的记录？`)
  if (!ok) return
  await request(`/${currentModule.value.endpoint}/${row.id}`, { method: 'DELETE' })
  await loadList()
}

function changePage(target) {
  page.value = target
  loadList()
}

function displayValue(value, col) {
  if (col.map) return col.map[value] ?? value
  if (col.date) return formatDate(value)
  return value ?? ''
}

function formatDate(value) {
  if (!value) return ''
  return String(value).replace('T', ' ').slice(0, 19)
}

function toDateInput(value) {
  if (!value) return ''
  return String(value).slice(0, 10)
}

function toDateTimeInput(value) {
  if (!value) return ''
  return String(value).replace(' ', 'T').slice(0, 16)
}

onMounted(() => {
  if (currentUser.value) loadDashboard()
})
</script>
