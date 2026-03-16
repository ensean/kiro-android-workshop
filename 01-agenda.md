# Workshop 议程

## 时间线总览

```
┌─────────────────────────────────────────────────────────────────┐
│  09:00        10:00        11:00        12:00        12:30      │
│    │            │            │            │            │        │
│    ▼            ▼            ▼            ▼            ▼        │
│  ┌────┐      ┌──────┐     ┌──────┐     ┌────┐      ┌────┐      │
│  │基础│      │ Spec │     │ 高级 │     │实践│      │总结│      │
│  │入门│      │ 实战 │     │ 功能 │     │挑战│      │Q&A│       │
│  └────┘      └──────┘     └──────┘     └────┘      └────┘      │
│   45min       75min        45min       30min       15min       │
└─────────────────────────────────────────────────────────────────┘
```

---

## Part 1: Kiro 基础入门（45 分钟）

### 1.1 开场介绍（10 分钟）
- Kiro 是什么？与传统 IDE 的区别
- 核心价值：Spec 驱动开发、AI 辅助、自动化工作流
- 今日目标预览

### 1.2 界面与基础交互（15 分钟）
- Chat 面板使用
- 上下文引用：`#File`、`#Folder`、`#Problems`
- 图片/文档附件
- Autopilot vs Supervised 模式

### 1.3 Hands-on: 代码理解（20 分钟）
> 📋 配合 [03-hands-on-basics.md](./03-hands-on-basics.md)

- 练习 1：让 Kiro 解释水印渲染逻辑
- 练习 2：查找潜在 bug
- 练习 3：生成代码注释

---

## Part 2: Spec 驱动开发实战（75 分钟）

### 2.1 Spec 概念讲解（15 分钟）
- 什么是 Spec？为什么需要它？
- Spec 三件套：Requirements → Design → Tasks
- Feature Spec vs Bugfix Spec

### 2.2 Hands-on: 新功能 Spec（30 分钟）
> 📋 配合 [04-hands-on-spec.md](./04-hands-on-spec.md) - Part A

**场景：添加「自定义水印模板」功能**

- 创建 Feature Spec
- 选择 Requirements-First 流程
- 完成 Requirements → Design → Tasks
- 执行第一个 Task

### 2.3 Hands-on: Bugfix Spec（30 分钟）
> 📋 配合 [04-hands-on-spec.md](./04-hands-on-spec.md) - Part B

**场景：修复「水印在横屏模式下位置偏移」**

- 创建 Bugfix Spec
- 理解 Bug Condition 方法论
- 编写探索性测试
- 实现修复

---

## Part 3: 高级功能探索（45 分钟）

### 3.1 Steering 规则（15 分钟）
> 📋 配合 [05-hands-on-advanced.md](./05-hands-on-advanced.md) - Part A

- 什么是 Steering？
- 创建团队代码规范
- 文件匹配模式

### 3.2 Hooks 自动化（15 分钟）
> 📋 配合 [05-hands-on-advanced.md](./05-hands-on-advanced.md) - Part B

- Hook 事件类型
- 实战：保存时自动 lint
- 实战：提交前代码检查

### 3.3 MCP 集成（15 分钟）
> 📋 配合 [05-hands-on-advanced.md](./05-hands-on-advanced.md) - Part C

- MCP 是什么？
- 配置示例：AWS 文档查询
- 团队可用的 MCP 服务器

---

## Part 4: 团队实践 & 总结（45 分钟）

### 4.1 分组挑战（30 分钟）

每组选择一个真实需求，用 Kiro 完成 Spec：

| 组别 | 可选场景 |
|------|----------|
| A 组 | 批量水印功能 |
| B 组 | 水印模板市场 |
| C 组 | GPS 定位水印 |
| D 组 | 性能优化重构 |

### 4.2 成果分享（10 分钟）
- 每组 2 分钟展示
- 讨论遇到的问题

### 4.3 Q&A & 总结（5 分钟）
- 答疑
- 后续学习资源
- 反馈收集

---

## 讲师备注

### 时间把控
- 每个 Hands-on 环节预留 5 分钟 buffer
- 如果时间紧张，Part 3 可精简为演示

### 常见问题准备
- Kiro 与 Cursor/Copilot 的区别？
- Spec 文件可以提交到 Git 吗？
- 如何处理大型遗留代码库？

### 备用练习
如果进度快，可增加：
- 多文件重构练习
- 自定义 MCP 服务器配置
