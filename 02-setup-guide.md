# 环境准备指南

## 前置要求

### 1. 安装 Kiro

**macOS:**
```bash
# 从官网下载
https://kiro.dev/download

# 或使用 Homebrew
brew install --cask kiro
```

**Windows:**
```
从 https://kiro.dev/download 下载安装包
```

### 2. 登录账号

1. 打开 Kiro
2. 点击右下角登录按钮
3. 使用 AWS Builder ID 或 GitHub 账号登录

### 3. 验证安装

打开 Kiro，在 Chat 面板输入：
```
你好，请介绍一下你自己
```

如果收到回复，说明安装成功 ✅

---

## 练习项目准备

### 克隆示例代码库

```bash
git clone https://github.com/your-org/watermark-camera-workshop.git
cd watermark-camera-workshop
```

### 用 Android Studio 打开项目

1. 打开 Android Studio
2. File → Open → 选择 `sample-watermark-app` 文件夹
3. 等待 Gradle 同步完成

### 用 Kiro 打开项目

```bash
# 方式 1：命令行
kiro sample-watermark-app

# 方式 2：在 Kiro 中 File → Open Folder
```

---

## 项目结构预览

```
sample-watermark-app/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/java/com/example/watermarkcamera/
│       │   ├── watermark/
│       │   │   ├── WatermarkConfig.kt      # 配置数据类
│       │   │   ├── WatermarkPosition.kt    # 位置计算（含 bug）
│       │   │   └── WatermarkRenderer.kt    # 水印渲染核心
│       │   └── template/
│       │       ├── TemplateManager.kt      # 模板管理
│       │       └── DefaultTemplates.kt     # 默认模板
│       └── test/java/
│           └── WatermarkPositionTest.kt    # 单元测试
└── settings.gradle.kts
```

---

## 检查清单

在 Workshop 开始前，请确认：

- [ ] Kiro 已安装并能正常启动
- [ ] 已登录账号
- [ ] 示例项目已克隆
- [ ] Android Studio 能正常打开项目（可选）
- [ ] Gradle 同步成功
- [ ] 能用 Kiro 打开项目

---

## 常见问题

### Q: 登录失败怎么办？
检查网络连接，尝试使用其他登录方式（AWS Builder ID / GitHub）

### Q: 项目打开后没有代码提示？
等待 Kiro 完成索引（右下角会显示进度）

### Q: Gradle 同步失败？
确保 JDK 版本 >= 17，可在 Android Studio 中检查 File → Project Structure → SDK Location

---

## 联系支持

如有问题，请联系：
- Workshop 讲师：[讲师邮箱]
- Kiro 官方文档：https://kiro.dev/docs
