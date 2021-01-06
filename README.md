# 梦玛引擎（GPL开源协议）
## 英文名：Dremma Engine

![](res/logo/DremmaIcon.png)

### 游戏窗体

Game继承java.awt.canvas

> Canvas是AWT组件, JPanel是Swing组件，Swing组件是以AWT组件为基础的，从理论上来说，Canvas要比JPanel更轻量些。如果canvas能满足需求,就用canvas。



Game中包含JFrame frame，frame作为窗体，构建游戏主体



GameLauncher继承java.applet.Applet，支持引擎构建applet网页游戏



### 游戏帧数

游戏帧数指每秒渲染的画面张数，人眼上限为60帧，多余的渲染帧造成性能浪费，故本游戏引擎采用默认60帧的方案。计算每次游戏循环经过的纳秒数，若达到每秒60帧渲染一帧画面所需的纳秒数，则进行画面渲染，否则不进行画面渲染。由于1e9除以60.0是无限不循环小数，有一定误差，经测试，设置为60.0可以达到游戏帧数稳定在59～61的范围内。