# 水印相机 App 开发团队 Kiro Workshop

## Workshop 概览

| 项目 | 内容 |
|------|------|
| **目标受众** | 水印相机 App 开发团队 |
| **时长** | 3.5 小时 |
| **形式** | 讲解 + Hands-on 实操 |
| **目标** | 掌握 Kiro 核心功能，应用到日常开发 |

## 资料清单

| 文件 | 说明 |
|------|------|
| [01-agenda.md](./01-agenda.md) | 完整议程 |
| [02-setup-guide.md](./02-setup-guide.md) | 环境准备指南 |
| [03-hands-on-basics.md](./03-hands-on-basics.md) | 基础功能实操 |
| [04-hands-on-spec.md](./04-hands-on-spec.md) | Spec 驱动开发实战 |
| [05-hands-on-advanced.md](./05-hands-on-advanced.md) | 高级功能探索 |
| [06-cheatsheet.md](./06-cheatsheet.md) | 快速参考卡片 |

## 快速开始

1. 参与者提前完成 [环境准备](./02-setup-guide.md)
2. 讲师按 [议程](./01-agenda.md) 推进
3. 每个环节配合对应的 Hands-on 文档

## 练习项目

Workshop 使用一个简化的水印相机 Android App 代码库作为练习素材，包含：

```
sample-watermark-app/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/java/com/example/watermarkcamera/
│       │   ├── watermark/           # 水印渲染核心
│       │   │   ├── WatermarkConfig.kt
│       │   │   ├── WatermarkPosition.kt
│       │   │   └── WatermarkRenderer.kt
│       │   └── template/            # 模板管理
│       │       ├── TemplateManager.kt
│       │       └── DefaultTemplates.kt
│       └── test/java/               # 单元测试
└── settings.gradle.kts
```

---

*Workshop 设计 by Kiro*
