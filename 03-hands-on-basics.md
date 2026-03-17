# Hands-on: Kiro 基础功能

## 练习 1：代码理解

### 目标
让 Kiro 解释水印渲染的核心逻辑

### 步骤

1. 在 Kiro 中打开 `app/src/main/java/com/example/watermarkcamera/watermark/WatermarkRenderer.kt`

2. 在 Chat 面板输入：
```
#File WatermarkRenderer.kt

请解释这个文件的核心逻辑，特别是：
1. 水印是如何渲染到 Bitmap 上的？
2. 支持哪些水印类型？
3. Android Canvas 绑定方面有什么考虑？
```

> 💡 **提示：** `#File` 后面直接写文件名即可，Kiro 会自动在项目中搜索匹配的文件。也可以在 Chat 输入框中输入 `#` 后从弹出列表中选择文件。

3. 观察 Kiro 的回答，注意它如何：
   - 识别关键函数
   - 解释数据流
   - 指出潜在问题

### 预期结果
Kiro 会给出结构化的代码解释，包括函数调用关系和核心算法说明。支持的水印类型包括 `TEXT`、`IMAGE`、`DATETIME`、`LOCATION`，其中 `LOCATION` 类型会在拍照时自动读取 GPS 坐标并渲染到图片上。

---

## 练习 2：查找潜在 Bug

### 目标
使用 Kiro 分析代码中的潜在问题

### 步骤

1. 在 Chat 面板输入：
```
#Folder watermark

分析这个文件夹中的 Kotlin 代码，找出可能存在的 bug 或边界情况处理不当的地方。
```

> 💡 **提示：** `#Folder` 后面写文件夹名即可，不需要完整路径。Kiro 会自动匹配项目中的文件夹。

2. 查看 Kiro 的分析结果

3. 针对某个具体问题深入询问：
```
关于你提到的 [具体问题]，能给出修复建议吗？
```

### 预期结果
Kiro 会列出潜在问题，如：
- 空值检查缺失
- 边界条件未处理
- 类型安全问题

> 💡 **提示：** 这个项目已经修复了几个典型问题（照片保存到相册、GPS EXIF 写入、拍照后预览），可以尝试让 Kiro 分析 `LocationHelper.kt` 或 `MainActivity.kt` 中还有哪些可以改进的地方。

---

## 练习 3：生成代码注释

### 目标
让 Kiro 为关键函数添加 KDoc 注释

### 步骤

1. 打开 `app/src/main/java/com/example/watermarkcamera/watermark/WatermarkPosition.kt`

2. 在 Chat 面板输入：
```
#File WatermarkPosition.kt

为这个文件中的所有公共函数添加 KDoc 注释，包括：
- 函数描述
- @param 参数说明
- @return 返回值说明
- 使用示例
```

3. 查看 Kiro 生成的注释

4. 如果满意，让 Kiro 应用修改：
```
请应用这些修改
```

> 💡 **模式提示：** Autopilot 模式下 Kiro 会直接修改文件；Supervised 模式下会先展示变更，等你确认后再应用。

### 预期结果
Kiro 会生成规范的 KDoc 注释，并可以直接应用到代码中。

---

## 练习 4：使用 #Problems 上下文

### 目标
让 Kiro 帮助解决当前文件的问题

### 步骤

1. 打开任意有 Kotlin 编译错误的文件

2. 在 Chat 面板输入：
```
#Problems

帮我修复当前文件的这些问题
```

3. 查看 Kiro 的修复建议

### 预期结果
Kiro 会针对每个问题给出具体的修复方案。

---

## 练习 5：图片理解（可选）

### 目标
让 Kiro 分析 UI 设计稿

### 步骤

1. 准备一张水印相机的 UI 设计稿（截图或设计文件）

2. 将图片拖入 Chat 面板，或点击附件图标上传

3. 输入：
```
这是水印编辑界面的设计稿，请分析：
1. 需要哪些 UI 组件？
2. 状态管理需要考虑什么？
3. 给出组件拆分建议
```

### 预期结果
Kiro 会基于图片内容给出技术实现建议。

---

## 小结

通过这些练习，你已经掌握了：

| 技能 | 说明 |
|------|------|
| 上下文引用 | `#File`、`#Folder`、`#Problems`、`#Terminal`、`#Git Diff` |
| 代码理解 | 让 Kiro 解释复杂逻辑 |
| 问题发现 | 利用 AI 分析潜在 bug |
| 代码生成 | 自动生成注释和文档 |
| 多模态输入 | 图片/文档附件 |

---

## 下一步

继续 [04-hands-on-spec.md](./04-hands-on-spec.md) 学习 Spec 驱动开发
