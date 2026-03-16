# Hands-on: Spec 驱动开发实战

## Part A: 新功能 Spec

### 场景描述

> 产品需求：用户希望能够创建和保存自定义水印模板，包括文字、字体、颜色、位置等设置，方便下次快速使用。

### 步骤 1：启动 Spec 创建

在 Chat 面板输入：
```
帮我创建一个 spec，实现自定义水印模板功能。

用户需求：
- 用户可以创建自定义水印模板
- 模板包含：文字内容、字体、颜色、大小、位置
- 模板可以保存、编辑、删除
- 最多保存 20 个模板
- 模板数据本地存储
```

### 步骤 2：选择 Spec 类型

Kiro 会询问这是新功能还是 Bug 修复：
- 选择 **「Build a Feature」**

### 步骤 3：选择工作流

Kiro 会询问从哪里开始：
- 选择 **「Requirements」**（推荐）

### 步骤 4：完善 Requirements

Kiro 会生成 requirements.md，包含：
- 用户故事
- 验收标准
- 正确性属性（Correctness Properties）

**检查要点：**
- 用户故事是否覆盖所有场景？
- 验收标准是否可测试？
- 边界条件是否考虑？（如模板数量上限）

如需修改，直接告诉 Kiro：
```
请补充以下场景：
- 模板导入/导出功能
- 模板排序功能
```

确认后回复：
```
looks good，继续
```

### 步骤 5：生成 Design

Kiro 会生成 design.md，包含：
- 系统架构
- 数据模型（Room Entity / SharedPreferences）
- Repository 设计
- ViewModel 设计

**检查要点：**
- 数据结构是否合理？
- 存储方案是否满足需求？
- 组件拆分是否清晰？

确认后继续。

### 步骤 6：生成 Tasks

Kiro 会生成 tasks.md，包含：
- 实现任务列表
- 任务依赖关系
- 预估工作量

**查看生成的文件：**
```
.kiro/specs/custom-watermark-template/
├── requirements.md
├── design.md
└── tasks.md
```

### 步骤 7：执行任务

选择第一个任务开始实现：
```
执行 task 1
```

观察 Kiro 如何：
1. 理解任务上下文
2. 生成实现代码
3. 更新任务状态

---

## Part B: Bugfix Spec

### 场景描述

> Bug 报告：在横屏模式下拍照，水印位置会向右偏移约 50 像素，导致水印部分超出图片边界。

### 步骤 1：启动 Bugfix Spec

在 Chat 面板输入：
```
帮我创建一个 spec 来修复这个 bug：

问题描述：
- 横屏模式下拍照，水印位置向右偏移约 50px
- 水印可能超出图片边界
- 竖屏模式正常

复现步骤：
1. 打开相机
2. 旋转手机到横屏
3. 添加水印并拍照
4. 查看照片，水印位置偏移
```

### 步骤 2：选择 Spec 类型

- 选择 **「Fix a Bug」**

### 步骤 3：理解 Bug Condition 方法论

Kiro 会引导你定义：

**Bug Condition C(X)：** 描述 bug 触发的条件
```
当设备方向为横屏（landscape）且水印位置计算时，
位置偏移量未考虑屏幕旋转导致的坐标系变化
```

**Preservation Check：** 确保修复不破坏现有功能
```
竖屏模式下的水印位置计算应保持不变
```

**Fix Check：** 验证 bug 已被修复
```
横屏模式下水印位置应正确，不超出边界
```

### 步骤 4：生成 Bugfix Requirements

Kiro 会生成 bugfix.md（替代 requirements.md），包含：
- Bug 描述
- 根因分析
- 修复策略
- 测试用例

### 步骤 5：编写探索性测试

Kiro 会建议编写测试来验证 bug：

```kotlin
// 探索性测试：验证 bug 存在
class WatermarkPositionBugTest {
    
    @Test
    fun `landscape mode should not cause position overflow`() {
        // 测试横屏模式下各种位置预设
        val presets = listOf(
            PositionPreset.TOP_LEFT,
            PositionPreset.TOP_RIGHT,
            PositionPreset.BOTTOM_LEFT,
            PositionPreset.BOTTOM_RIGHT,
            PositionPreset.CENTER
        )
        
        presets.forEach { preset ->
            val result = WatermarkPosition.calculatePosition(
                preset = preset,
                orientation = Orientation.LANDSCAPE,
                imageWidth = 1920,
                imageHeight = 1080
            )
            
            // Bug: 横屏时位置可能超出边界
            assertTrue(
                "Position x=${result.x} should be within image width",
                result.x >= 0 && result.x <= 1920
            )
        }
    }
}
```

### 步骤 6：实现修复

确认 bugfix.md 后，Kiro 会生成修复任务并执行：

1. 定位问题代码
2. 实现修复
3. 运行测试验证
4. 更新任务状态

---

## Spec 文件结构说明

### Feature Spec 结构
```
.kiro/specs/{feature-name}/
├── .config.kiro          # 配置（specType, workflowType）
├── requirements.md       # 需求文档
├── design.md            # 设计文档
└── tasks.md             # 任务列表
```

### Bugfix Spec 结构
```
.kiro/specs/{bugfix-name}/
├── .config.kiro          # 配置
├── bugfix.md            # Bug 分析文档（替代 requirements.md）
├── design.md            # 修复设计
└── tasks.md             # 修复任务
```

---

## 小结

通过这些练习，你已经掌握了：

| 技能 | 说明 |
|------|------|
| Feature Spec | Requirements → Design → Tasks 流程 |
| Bugfix Spec | Bug Condition 方法论 |
| Property-Based Testing | 用属性测试验证正确性 |
| 任务执行 | 让 Kiro 实现具体任务 |

---

## 下一步

继续 [05-hands-on-advanced.md](./05-hands-on-advanced.md) 学习高级功能
