# Hands-on: 高级功能探索

## Part A: Steering 规则

### 什么是 Steering？

Steering 是 Kiro 的「指导规则」系统，让你可以：
- 定义团队代码规范
- 提供项目特定上下文
- 自动应用到相关文件

### 练习 1：创建团队代码规范

**目标：** 为水印相机项目创建 Kotlin 编码规范

**步骤：**

1. 在项目根目录创建 `.kiro/steering/` 文件夹

2. 创建 `coding-standards.md`：

```markdown
---
inclusion: always
---

# 水印相机项目编码规范

## 命名约定
- 类文件：PascalCase（如 WatermarkRenderer.kt）
- 函数：camelCase（如 calculatePosition）
- 常量：UPPER_SNAKE_CASE（如 MAX_TEMPLATE_COUNT）
- 包名：全小写（如 com.example.watermarkcamera）

## Kotlin 代码风格
- 优先使用 data class 定义数据模型
- 使用 sealed class 处理有限状态
- 优先使用扩展函数而非工具类
- 使用 Kotlin Coroutines 处理异步

## 错误处理
- 使用 Result<T> 或 sealed class 表示操作结果
- 避免使用 !! 强制解包
- 使用 runCatching 包装可能抛异常的操作

## 测试要求
- 每个公共函数必须有单元测试
- 新测试使用 JUnit 5 + Kotest（项目已配置依赖）
- 现有测试（如 WatermarkPositionTest.kt）使用 JUnit 4，可逐步迁移
- 测试覆盖率目标：80%
```

3. 验证效果：在 Chat 中请求生成代码，观察是否遵循规范

### 练习 2：文件匹配模式

**目标：** 为特定文件类型创建专属规则

**步骤：**

1. 创建 `watermark-module.md`：

```markdown
---
inclusion: fileMatch
fileMatchPattern: "**/watermark/**/*.kt"
---

# 水印模块开发规则

## 性能要求
- 大图片（>2MB）处理使用 BitmapFactory.Options 降采样
- 避免在主线程进行 Bitmap 操作
- 使用 inBitmap 复用 Bitmap 内存

## 坐标系统
- 所有坐标使用相对值（0-1）
- 转换为绝对像素时考虑屏幕密度
- 支持横屏/竖屏自动适配

## 内存管理
- Bitmap 使用后及时 recycle()
- 避免在循环中创建新 Bitmap
- 使用 LruCache 缓存常用水印图片
```

2. 打开 `app/src/main/java/com/example/watermarkcamera/watermark/` 下的文件，Kiro 会自动加载这些规则

### 练习 3：手动引用规则

**目标：** 创建按需引用的规则

**步骤：**

1. 创建 `performance-checklist.md`：

```markdown
---
inclusion: manual
---

# 性能优化检查清单

在进行性能优化时，检查以下项目：

## 渲染性能
- [ ] 避免在主线程进行 Bitmap 操作
- [ ] 使用 BitmapFactory.Options 降采样处理大图
- [ ] 使用 inBitmap 复用 Bitmap 内存

## 内存优化
- [ ] Bitmap 使用后及时 recycle()
- [ ] 避免在循环中创建新 Bitmap
- [ ] 使用 LruCache 缓存常用水印图片
- [ ] 检查 Activity/Fragment 泄漏（LeakCanary）

## I/O 优化
- [ ] 文件读写使用 Coroutines 或线程池
- [ ] SharedPreferences 使用 apply() 而非 commit()
- [ ] 图片保存使用合适的压缩质量
```

2. 在 Chat 中手动引用：
```
#performance-checklist

帮我检查 WatermarkRenderer.kt 的性能问题
```

---

## Part B: Hooks 自动化

### 什么是 Hooks？

Hooks 让 Kiro 在特定事件发生时自动执行操作，如：
- 文件保存时运行 lint
- 提交前检查代码
- 任务完成后运行测试

### 练习 1：保存时自动 Lint

**目标：** Kotlin 文件保存时自动运行 ktlint

**步骤：**

1. 打开命令面板（Cmd/Ctrl + Shift + P）
2. 搜索「Open Kiro Hook UI」
3. 创建新 Hook：

```json
{
  "name": "Lint on Save",
  "version": "1.0.0",
  "description": "Kotlin 文件保存时自动运行 ktlint",
  "when": {
    "type": "fileEdited",
    "patterns": ["*.kt"]
  },
  "then": {
    "type": "runCommand",
    "command": "./gradlew ktlintCheck"
  }
}
```

4. 保存一个 .kt 文件，观察 lint 自动运行

### 练习 2：提交前代码检查

**目标：** 在提交代码前让 Kiro 检查代码质量

**步骤：**

1. 创建 Hook：

```json
{
  "name": "Pre-commit Review",
  "version": "1.0.0",
  "description": "提交前让 Kiro 检查代码变更",
  "when": {
    "type": "userTriggered"
  },
  "then": {
    "type": "askAgent",
    "prompt": "请检查 #Git Diff 中的代码变更，确认：\n1. 是否有明显 bug\n2. 是否符合编码规范\n3. 是否需要添加测试"
  }
}
```

2. 在需要时手动触发这个 Hook

### 练习 3：任务完成后运行测试

**目标：** Spec 任务完成后自动运行相关测试

**步骤：**

1. 创建 Hook：

```json
{
  "name": "Test After Task",
  "version": "1.0.0",
  "description": "任务完成后运行测试",
  "when": {
    "type": "postTaskExecution"
  },
  "then": {
    "type": "runCommand",
    "command": "./gradlew test"
  }
}
```

### Hook 事件类型参考

| 事件类型 | 触发时机 |
|----------|----------|
| `fileEdited` | 文件保存时 |
| `fileCreated` | 新建文件时 |
| `fileDeleted` | 删除文件时 |
| `promptSubmit` | 发送消息时 |
| `agentStop` | Agent 执行完成时 |
| `preToolUse` | 工具执行前 |
| `postToolUse` | 工具执行后 |
| `preTaskExecution` | 任务开始前 |
| `postTaskExecution` | 任务完成后 |
| `userTriggered` | 手动触发 |

---

## Part C: MCP 集成

### 什么是 MCP？

MCP（Model Context Protocol）让 Kiro 连接外部工具和服务，扩展能力边界。

### 练习 1：查看已配置的 MCP

**步骤：**

1. 打开 Kiro 侧边栏的「MCP Servers」面板
2. 查看当前已连接的服务器
3. 点击某个服务器查看可用工具

### 练习 2：配置 AWS 文档查询

**目标：** 添加 AWS 文档 MCP 服务器

**步骤：**

1. 打开 `.kiro/settings/mcp.json`（项目级，如不存在则创建）

> 💡 **提示：** MCP 配置有两个级别：项目级 `.kiro/settings/mcp.json` 和用户级 `~/.kiro/settings/mcp.json`。项目级优先级更高，会覆盖用户级的同名配置。

2. 添加配置：

```json
{
  "mcpServers": {
    "aws-docs": {
      "command": "uvx",
      "args": ["awslabs.aws-documentation-mcp-server@latest"],
      "env": {
        "FASTMCP_LOG_LEVEL": "ERROR"
      },
      "disabled": false,
      "autoApprove": []
    }
  }
}
```

3. 保存后 MCP 服务器会自动连接

4. 测试使用：
```
帮我查询 AWS S3 的图片处理最佳实践
```

### 练习 3：使用 MCP 工具

**场景：** 查询 AWS Lambda 图片处理相关文档

**步骤：**

1. 在 Chat 中输入：
```
使用 AWS 文档工具，查询如何用 Lambda 处理图片水印
```

2. 观察 Kiro 如何调用 MCP 工具获取信息

### 常用 MCP 服务器

| 服务器 | 用途 |
|--------|------|
| aws-documentation | AWS 文档查询 |
| github | GitHub 仓库操作 |
| postgres | 数据库查询 |
| filesystem | 文件系统操作 |

---

## 小结

通过这些练习，你已经掌握了：

| 功能 | 说明 |
|------|------|
| Steering | 定义团队规范，自动应用到代码生成 |
| Hooks | 事件驱动的自动化工作流 |
| MCP | 连接外部工具扩展能力 |

---

## 下一步

查看 [06-cheatsheet.md](./06-cheatsheet.md) 获取快速参考卡片
