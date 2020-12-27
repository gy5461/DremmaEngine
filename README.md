# 梦玛引擎
## 英文名：Dremma Engine



### 游戏窗体

Game继承java.awt.canvas

> Canvas是AWT组件, JPanel是Swing组件，Swing组件是以AWT组件为基础的，从理论上来说，Canvas要比JPanel更轻量些。如果canvas能满足需求,就用canvas。



Game中包含JFrame frame，frame作为窗体，构建游戏主体



GameLauncher继承java.applet.Applet，支持引擎构建applet网页游戏