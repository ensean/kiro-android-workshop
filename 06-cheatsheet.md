# Kiro 快速参考卡片

## 上下文引用

| 语法 | 说明 | 示例 |
|------|------|------|
| `#File` | 引用单个文件 | `#File WatermarkRenderer.kt` |
| `#Folder` | 引用整个文件夹 | `#Folder watermark` |
| `#Problems` | 当前文件的问题 | `#Problems 帮我修复` |
| `#Terminal` | 终端输出 | `#Terminal 解释这个错误` |
| `#Git Diff` | Git 变更 | `#Git Diff 检查这些改动` |

---

## Spec 工作流

### 创建 Spec
```
帮我创建一个 spec，实现 [功能描述]
```

### Spec 类型选择
- **Build a Feature** → 新功能开发
- **Fix a Bug** → Bug 修复

### 工作流选择（仅新功能）
- **Requirements** → 需求优先（推荐）
- **Design** → 设计优先

### 执行任务
```
执行 task 1
执行所有任务
```

### Spec 文件位置
```
.kiro/specs/{feature-name}/
├── .config.kiro           # Spec 配置
├── requirements.md        # 或 bugfix.md
├── design.md
└── tasks.md
```

---

## Steering 规则

### 文件位置
```
.kiro/steering/*.md
```

### 引用模式

| 模式 | Front-matter | 说明 |
|------|--------------|------|
| 始终加载 | `inclusion: always` | 所有对话都生效 |
| 文件匹配 | `inclusion: fileMatch` | 打开匹配文件时生效 |
| 手动引用 | `inclusion: manual` | 用 `#规则名` 引用 |

### 示例
```markdown
---
inclusion: fileMatch
fileMatchPattern: "**/*Test.kt"
---

# 测试文件规范
- 使用 @Test 注解标记测试方法
- 测试名称使用反引号描述行为
- 使用 AAA 模式（Arrange-Act-Assert）
```

---

## Hooks 配置

### 文件位置
```
.kiro/hooks/*.json
```

### 基本结构
```json
{
  "name": "Hook 名称",
  "version": "1.0.0",
  "when": {
    "type": "事件类型",
    "patterns": ["文件模式"]
  },
  "then": {
    "type": "askAgent | runCommand",
    "prompt": "提示词",
    "command": "命令"
  }
}
```

### 事件类型
| 类型 | 触发时机 |
|------|----------|
| `fileEdited` | 文件保存 |
| `fileCreated` | 新建文件 |
| `fileDeleted` | 删除文件 |
| `promptSubmit` | 发送消息 |
| `agentStop` | Agent 完成 |
| `preToolUse` | 工具执行前 |
| `postToolUse` | 工具执行后 |
| `preTaskExecution` | 任务开始前 |
| `postTaskExecution` | 任务完成后 |
| `userTriggered` | 手动触发 |

---

## MCP 配置

### 文件位置
```
.kiro/settings/mcp.json      # 项目级
~/.kiro/settings/mcp.json    # 用户级
```

### 配置结构
```json
{
  "mcpServers": {
    "server-name": {
      "command": "uvx",
      "args": ["package-name@latest"],
      "env": {},
      "disabled": false,
      "autoApprove": []
    }
  }
}
```

---

## 常用命令

### 命令面板（Cmd/Ctrl + Shift + P）
- `Open Kiro Hook UI` - 打开 Hook 配置界面
- `MCP: Reconnect` - 重连 MCP 服务器

### Chat 快捷操作
- 拖拽图片/文档到 Chat 面板
- 点击附件图标上传文件

---

## 模式切换

| 模式 | 说明 |
|------|------|
| **Autopilot** | Kiro 自动应用修改 |
| **Supervised** | 修改前需确认 |

---

## 常见问题速查

| 问题 | 解决方案 |
|------|----------|
| Kiro 不理解上下文 | 使用 `#File` 或 `#Folder` 明确引用 |
| 生成代码不符合规范 | 检查 Steering 规则是否正确配置 |
| MCP 工具不可用 | 检查 mcp.json 配置，确认服务器已连接 |
| Hook 不触发 | 检查 patterns 是否匹配，事件类型是否正确 |
| Spec 任务执行失败 | 检查 tasks.md 格式，确认依赖任务已完成 |

---

## 快捷键

| 操作 | macOS | Windows |
|------|-------|---------|
| 命令面板 | `Cmd + Shift + P` | `Ctrl + Shift + P` |
| 快速打开文件 | `Cmd + P` | `Ctrl + P` |
| 打开 Chat | `Cmd + L` | `Ctrl + L` |

---

## 学习资源

- 官方文档：https://kiro.dev/docs
- Steering 指南：https://kiro.dev/docs/steering
- MCP 配置：https://kiro.dev/docs/mcp
- 社区论坛：https://kiro.dev/community
