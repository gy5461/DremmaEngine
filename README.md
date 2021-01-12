# 梦玛引擎（GPL开源协议）
## 英文名：Dremma Engine

![](DremmaEngine/res/logo/DremmaIcon.png)

### 游戏窗体

Game继承java.awt.canvas

> Canvas是AWT组件, JPanel是Swing组件，Swing组件是以AWT组件为基础的，从理论上来说，Canvas要比JPanel更轻量些。如果canvas能满足需求,就用canvas。



Game中包含JFrame frame，frame作为窗体，构建游戏主体



GameLauncher继承java.applet.Applet，支持引擎构建applet网页游戏



### 游戏帧数

游戏帧数指每秒渲染的画面张数，人眼上限为60帧，多余的渲染帧造成性能浪费，故本游戏引擎采用默认60帧的方案。计算每次游戏循环经过的纳秒数，若达到每秒60帧渲染一帧画面所需的纳秒数，则进行画面渲染，否则不进行画面渲染。由于1e9除以60.0是无限不循环小数，有一定误差，经测试，设置为60.0可以将游戏帧数稳定在59～61的范围内。

> 1s = 1e9ns = 1e3ms
>
> 1ms = 1e6ns

### 游戏调试

调试支持三种信息：绿色的信息INFO、黄色的警告WARNING以及红色的严重警告SERVER，在游戏开发时，打开调试，游戏发布时，将Game.debug设为false关闭调试信息。

![1.png](https://i.loli.net/2021/01/06/bRgeuQN8MS1Hj53.png)



如果开发者需要看到有颜色的文字，请在eclipse中安装ANSIconsole插件

> 该插件在线安装地址：http://www.mihai-nita.net/eclipse 
>
> Git：https://github.com/mihnita/ansi-econsole

### 渲染系统

BufferStrategy类：处理双缓存、翻页和显示器刷新等待，它会根据系统功能选择最佳的缓存方法。首先，尝试进行翻页，若无法翻页则尝试进行双缓存。另外，翻页之前需要等待显示器刷新完毕，限制每秒可以显示的帧数最多为75，这样就不能用游戏帧速率作为测试系统运行速度的基准。

使用getDrawGraphics()方法取得**绘图缓存区的图形描述表**，绘图完成后，调用show()方法显示绘图缓存区，这样就可以使用翻页，也可以将绘图缓存区复制到显示缓存区，实现代码如下：

~~~java
BufferStrategy strategy = frame.getBufferStartegy();
Graphics g = strategy.getDrawGraphics();
draw(g);
g.dispose();
strategy.show();
~~~

* 双缓存

  不断直接绘制屏幕会导致屏幕闪烁，当要用背景擦除角色，然后重画角色，在某个瞬间，可能在角色位置看到背景，由于一切发生得太快，所以会出现闪烁。避免出现闪烁可以使用双缓存技术，缓存区是绘图时使用的屏外内存区。使用双缓存不是直接绘制屏幕，而是绘制到后缓存区，然后将整个缓存区复制到屏幕上，这样就可以同时更新整个屏幕。

* 翻页

  使用双缓存的一个缺点是将后缓存区复制到屏幕上需要较长时间，以分辨率为800\*600，位深度为16，那么就需要800\*600\*2字节，即938K byte，这样每秒钟30帧要交换将近1M byte的内存，尽管复制如此大的内存量在许多游戏中也是相当快的，但如果不用复制缓存区，而能直接把后缓存区变成显示缓存区，那该多好。

  翻页技术使用显示指针解决这一问题，显示指针指向要显示的缓存区，大多数现代系统都可以改变这个显示指针。向后缓存区绘制完毕后，显示指针可以从当前显示缓存区切换到后缓存区上。当显示指针改变时，显示缓存区立即变为后缓存区，而后缓存区则会立即变成显示缓存区。改变指针比复制大块内存要快得多。

* 监视刷新与裂开

  显示器的刷新频率通常在75Hz左右，表示显示器每秒钟刷新75次，但如果显示器刷新途中发生翻页或者缓存区复制等操作，显示器将会同时显示一部分旧缓存区和一部分新缓存区，即发生裂开，为了解决这个问题，可以在显示器刷新完成之前翻页，在Java运行环境中可以使用BufferStrategy类完成该工作。

### 事件监听

* 鼠标事件

* 键盘事件

* 窗体事件

![截屏2021-01-11下午7.56.32.png](https://i.loli.net/2021/01/11/5NH2UqO1CsZVInd.png)

### 音频播放

支持的文件格式有`wav，au，和aiff`

直接实现音频播放时，游戏帧数会下降到30-40帧

![截屏2021-01-11下午10.15.52.png](https://i.loli.net/2021/01/11/toCF8xcK7irmIYR.png)

而使用多线程重构播放器后，帧数会重新恢复到60帧左右

![截屏2021-01-11下午10.17.39.png](https://i.loli.net/2021/01/11/3uZiwThC5zb61py.png)

经测试，解除shouldRender限制后

* 游戏在不进行任何音乐播放的情况下帧数为340-350
* 不使用多线程播放音乐，游戏帧数为30-40
* 使用多线程播放音乐，游戏帧数为280-290
* 最终解除了Thread.sleep休息的2ms，发现在不进行音乐播放的情况下帧数为470-630，多线程播放音乐的情况下达到了350-480，解除2ms、打开shouldRender的限制以后，惊喜地发现游戏帧数固定在了60帧

故使用多线程进行音乐播放可以在很大程度上优化音乐播放器性能

#### 音乐播放模块在Sandbox中用法

~~~java
package priv.sandbox.game;

import priv.dremma.game.Game;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.util.Res;

@SuppressWarnings("serial")
public class Sandbox extends Game {
	
	public void onStart() {
		this.setName("SandBox");
		
    // 加载音乐资源
		Res.loadRes(Res.ResType.Music, "backgroundMusic", "res/vow to virtue.wav");
		Res.loadRes(Res.ResType.Music, "moneySound", "res/money.wav");
	}

	public void onUpdate() {
    // 播放音乐
		AudioManager.getInstance().playOnce("moneySound");
		AudioManager.getInstance().playLoop("backgroundMusic");
	}

}
~~~



# DremmaSandbox

基于DremmaEngine开发的游戏，展现本游戏引擎的易用性

绘制两个矩形

![2.png](https://i.loli.net/2021/01/10/3OE1uDQ2sU4Zehg.png)

