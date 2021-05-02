

# Dremma开发者文档

## 基本概念

### 游戏引擎是什么？

游戏引擎是帮助游戏创作者创作游戏的软件

### 游戏引擎存在的目的：

游戏引擎保障基础设施，激发游戏创作的无限可能

### 游戏开发者为什么要使用游戏引擎呢？

为了在保证质量的前提下高效完成游戏开发需求

### 小型游戏引擎的价值在哪里？

站在游戏开发者的角度，我既可以从零开始开发，又可以基于引擎，而学习庞大复杂的引擎需要很高的学习成本，但自己从零开发又会拖慢开发进度，有没有五脏俱全、容易上手的小引擎呢？

## 梦玛引擎和别的游戏引擎相比有什么优势呢？

* 与大型游戏引擎比起来非常小巧、轻量（Jar包139KB）

* 可以打包为可执行Jar包和Applet小程序，利用Java的跨平台特性

* 默认解决人树渲染顺序问题，不需要对每个物体复杂的配置

* 与其他小型纯游戏引擎相比，很多小型游戏引擎不提供场景编辑功能，只能在代码中进行位置指定，碰撞盒位置调整等内容，DremmaEngine提供可视化的碰撞盒调整以及可视化的位置调整，调整逻辑与unity相似，容易上手

* 可以处理两个运动物体间的碰撞

* 由于小巧，学习成本低，也方便对游戏引擎功能实现感兴趣的人分析源码学习



作为一个仅有129KB的Jar包，游戏开发者如何利用梦玛引擎达成自己的2D游戏开发目标呢？

## 2D游戏开发需求洞察&如何使用

### 核心配置

基于Dremma的游戏需要继承GameCore和GameLauncher，重写钩子函数

~~~java
// ---------------GameCore----------------
public class Sandbox extends GameCore {
  @Override
	public void onStart() {
    super.onStart();
    // 游戏开始前调用
    // 进行资源加载、按键绑定等配置，处理游戏开始前的初始化等准备工作
  }
  @Override
	public void onUpdate() {
    super.onUpdate();
    // 游戏运行中的每帧调用
    // 游戏更新逻辑
  }
  @Override
  public void onDestroy() {
    super.onDestroy();
    // 游戏关闭前调用
    // 可对一些游戏数据进行持久化保存
  }
}

// ---------------GameLauncher----------------
public class SandboxLauncher extends GameLauncher {
	public void onStart() {
    // 在Applet小程序平台指定打包的游戏为Sandbox实例
		GameLauncher.game = new Sandbox();
	}
	
	public static void main(String[] args) {
    // 在Java Application平台指定打包的游戏为Sandbox实例
		GameLauncher.game = new Sandbox();
		lauchToApplication();	// 加载Java Application平台设置
	}
}
~~~

是否开启Debug日志：查看游戏帧数，游戏开发过程中输出的信息、警告及错误

> 配置方法：引擎默认为Debug模式
>
> ~~~java
> GameCore.debug = true;  // 关闭Release模式，开启Debug模式
> GameCore.debug = false; // 关闭Debug模式，开启Release模式
> ~~~

游戏目标平台：利用Java的跨平台特性：可配置打包成Applet小程序及Java Application

> 配置方法：引擎默认为Java Application
>
> ~~~java
> GameCore.isApplet = true;	  // 设置平台为Applet小程序
> GameCore.isApplet = false;	// 设置平台为Java Application
> ~~~

游戏窗体的长宽，放大倍数

>默认配置如下，可根据需求自定义
>
>~~~java
>GameCore.width = 160;							// 游戏窗体宽度
>GameCore.height = width / 12 * 9;	// 游戏窗体高度
>GameCore.Scale = 6;								// 游戏窗体放大倍数
>~~~

游戏名称：决定日志中显示的名称的及游戏窗体标题

>默认为DremmaEngine，可根据需求自定义
>
>~~~java
>GameCore.setName(your name string);
>~~~

游戏视角：

>默认为2.5D斜视角，可根据需求自定义
>
>~~~java
>GameCore.viewAngle = GameViewAngle.ViewAngle2DOT5;	// 将视角设置为2.5D斜视角
>GameCore.viewAngle = GameViewAngle.ViewAngle2;			// 将视角设置为2D直视角
>~~~

### 核心工具

#### 调试支持

Debug.log输出日志

![截屏2021-04-24上午9.23.14.png](https://i.loli.net/2021/04/24/46bZfI29OSdCkNx.png)

用法：

~~~java
Debug.log(Debug.DebugLevel.INFO, info string);				// 打印调试信息，为绿色
Debug.log(Debug.DebugLevel.WARNING, warning string);	// 打印调试警告，为黄色
Debug.log(Debug.DebugLevel.SERVERE, servere string);	// 打印调试严重警告，为红色
~~~

#### 事件处理

![截屏2021-04-18下午1.01.17.png](https://i.loli.net/2021/04/18/h1yxwiLZbRzNcTU.png)

##### 键盘事件

按键绑定（多个键盘可以绑定一个虚拟键，如w键及上箭头键绑定名为up的虚拟键，虚拟键名称可自定义）

~~~java
// 为名称为up的虚拟键绑定键盘上的W键以及上箭头键
ArrayList<Integer> upKeyCodes = new ArrayList<Integer>();
upKeyCodes.add(KeyEvent.VK_W);	// W键
upKeyCodes.add(KeyEvent.VK_UP);	// 上箭头键
this.keyInputHandler.setVirtualKey("up", upKeyCodes);
~~~

捕获键盘事件

~~~java
// 在游戏核心类(继承GameCore的类)中，例如Sandbox
// 若非核心类，需要从核心类传入this.keyInputHandler使用
if(this.keyInputHandler.getVirtualKey("bag").isPressed())
{
  // 当虚拟键bag被按时
}

// 获取虚拟键bag被按次数
int pressedTimes = this.keyInputHandler.getVirtualKey("bag").getPressedTimes();
~~~

##### 鼠标事件

捕获鼠标事件

~~~java
// 游戏核心类中，若非核心类，需要从核心类传入this.mouseInputHandler使用
if(this.mouseInputHandler.mouse.isPressed())
{
  // 当鼠标被点击时
  
  // 获取鼠标点击位置
  Vector2 mouseLocation = this.mouseInputHandler.mouse.getLocation();
	// 获取鼠标点击次数
  int mousePressedTimes = this.mouseInputHandler.mouse.getPressedTimes();
}

if(this.mouseInputHandler.worldCurPosIsInRect(Rect类实例))
{
  // 当点击位置的世界坐标在世界中的矩形中时
}

if(this.mouseInputHandler.screenCurPosIsInRect(Rect类实例))
{
  // 当点击位置的屏幕坐标在屏幕上的矩形中时
}

// 获取当前帧鼠标位置
Vector2 curPos = this.mouseInputHandler.getCurPos();
// 获取上一帧的鼠标位置
Vector2 lastPos = this.mouseInputHandler.getLastPos();
~~~

##### 窗体事件

~~~java
// 游戏核心类中，重写钩子函数
@Override
public void onWindowOpened() {
  // 在窗体打开时调用
}

@Override
public void onWindowClose() {
  // 在游戏结束关闭窗体时调用
}
~~~

#### 时间运转

可通过时间类配置是否开启帧数锁定，帧数锁定到多少，获取每帧经过的时长，已经过去的时长，当前时间等等

>游戏中的时间工具非常重要，决定着游戏的帧数控制，避免性能浪费，以及游戏中物体的运动，插值运算等都紧密相关
>
>Dremma的时间工具提供帧数控制，关闭帧数控制等配置项，配置方法如下
>
>~~~java
>Time.gameFrames = 60;	// 游戏帧数默认控制在60帧，可自定义
>Time.shouldRender = true;	// 默认为false, 设置为true时不进行帧数限制（需在引擎源码中GameCore 118行进行该配置）
>~~~

时间获取

| 取值方法             | 含义                                                         |
| -------------------- | ------------------------------------------------------------ |
| Time.lastTime        | 游戏上一秒的毫秒数                                           |
| Time.lastnsTime      | 上一次时间循环纳秒数                                         |
| Time.lastFramensTime | 上一帧的纳秒数                                               |
| *Time.deltaTime      | 本帧的用时，单位为秒                                         |
| Time.deltaFrame      | 纳秒数的变化除以Time.NSPERFRAME，即变化的帧数                |
| Time.NSPERFRAME      | 每秒1e9纳秒，指在每秒渲染指定帧数画面的前提下，每帧需要多少纳秒 |
| *Time.elapsedTime    | 过去的时间，单位为秒                                         |
| Time.curnsTime       | 当前纳秒数                                                   |

~~~java
Time.printFrames();	// 打印运行时帧数
~~~

#### FloatCompare

浮点数的有效数字是6～7位，会在比较时引起误差，比如判别两个浮点数是否相等时，应该是判别它们是否在一定精度范围内近似相等，梦玛引擎原装的浮点数比较工具类FloatCompare默认精度为`1e-8`，提供给开发者的接口如下

| 调用方法                                       | 含义                                        |
| ---------------------------------------------- | ------------------------------------------- |
| FloatCompare.isEqual(float a, float b)         | 返回boolean值，比较传入函数的a与b是否相等   |
| FloatCompare.isNotEqual(float a, float b)      | 返回boolean值，比较传入函数的a与b是否不相等 |
| FloatCompare.isLess(float a, float b)          | 返回boolean值，比较传入函数的a是否小于b     |
| FloatCompare.isLessOrEqual(float a, float b)   | 返回boolean值，比较传入函数的a是否小于等于b |
| FloatCompare.isBigger(float a, float b)        | 返回boolean值，比较传入函数的a是否大于b     |
| FloatCompare.isBiggerOrEqual(float a, float b) | 返回boolean值，比较传入函数的a是否大于等于b |

#### Vector2

二维向量的封装，提供插值、运算、比较、模长、归一化等方法

【注】二维向量实现了Serializable接口，可对其进行对象序列化

> 公有属性：
>
> float x: 向量在x轴方向上的分量
>
> float y: 向量在y轴方向上的分量
>
> ------------------------------------------------
>
> 公有方法：
>
> | 方法                                                         | 含义                                                         |
> | :----------------------------------------------------------- | ------------------------------------------------------------ |
> | Vector2.zero()                                               | 创建并获取向量（0，0）                                       |
> | Vector2.one()                                                | 创建并获取向量（1，1）                                       |
> | **public** Vector2()                                         | 默认无参构造函数，将创建并获取向量（0，0）                   |
> | **public** Vector2(**float** x, **float** y)                 | 带参构造函数，构造向量（x，y）                               |
> | **public** **float** magnitude()                             | 返回向量模长                                                 |
> | **public** **float** sqrMagnitude()                          | 返回向量模长的平方，比magnitude快（无需进行开方运算）        |
> | **public** **float** dot(Vector2 other)                      | 返回本向量点乘另一个向量的结果：a·b = \|a\|\|b\|cosθ         |
> | **public** Vector2 normalized()                              | 将向量归一化                                                 |
> | **public** Vector2 rotate(**float** angle)                   | 旋转向量，angle为顺时针旋转角度                              |
> | **public** Vector2 add(Vector2 other)                        | 向量相加，返回和向量                                         |
> | **public** Vector2 add(**float** other)                      | 向量加数字                                                   |
> | **public** Vector2 sub(Vector2 other)                        | 向量相减，返回本向量减去other向量的结果向量                  |
> | **public** Vector2 sub(**float** other)                      | 向量减数字                                                   |
> | **public** Vector2 mul(Vector2 other)                        | 向量相乘，返回乘积向量                                       |
> | **public** Vector2 mul(**float** other)                      | 向量乘数字                                                   |
> | **public** Vector2 div(Vector2 other)                        | 向量相除，返回本向量除以other向量的结果向量                  |
> | **public** Vector2 div(**float** other)                      | 向量除数字                                                   |
> | **public** Vector2 abs()                                     | 返回向量绝对值                                               |
> | **public** String toString()                                 | 重写了toString函数，可以直接打印向量，<br />格式为Vector2 [x=0, y=0] |
> | **public** **boolean** isEqual(Vector2 other)                | 判断向量是否相等                                             |
> | **public** **boolean** isBigger(Vector2 other)               | 判断向量是否大于other                                        |
> | **public** **boolean** isBiggerOrEqual(Vector2 other)        | 判断向量是否大于等于other                                    |
> | **public** **boolean** isLess(Vector2 other)                 | 判断向量是否小于other                                        |
> | **public** **boolean** isLessOrEqual(Vector2 other)          | 判断向量是否小于等于other                                    |
> | **public** **boolean** isInRect(Vector2 leftUp, Vector2 rightDown) | 判断当前坐标是否在一个矩形内                                 |
> | **public** **static** Vector2 lerp(Vector2 startPos, Vector2 endPos, **float** time) | 二维向量的插值函数<br />startPos：开始位置<br />endPos  结束位置<br />time   插值时间（0.0f～1.0f） |

#### 坐标转换

GUtils类

> ~~~java
> // 视口坐标转换成世界像素坐标
> Vector2 worldPos = GUtils.viewPortToWorldPixel(viewportPos);
> // 世界像素坐标转换成视口坐标
> Vector2 viewportPos = GUtils.worldPixelToViewPort(worldPos);
> 
> // 世界像素坐标转换成世界地砖坐标
> // tileWidth:  单个地砖宽度
> // tileHeight: 单个地砖长度
> Vector2 worldTilePos = GUtils.worldPixelToWorldTile(worldPos, tileWidth, tileHeight);
> // 世界某地砖的中心点转换成世界像素坐标
> // scale地图放大倍数
> Vector2 tilePos = GUtils.worldTileCenterToWorldPixel(tileCenterPos, tileWidth, tileHeight, scale);
> 
> // 世界某地砖的中心点转换成视口坐标
> Vector2 viewportPos = GUtils.worldTileCenterToViewPort(tileCenterPos, tileWidth, tileHeight);
> ~~~

### 资源加载

巧妇难为无米之炊

资源包括贴图、音频、动画、地图配置文件、对话内容等

开发者可调用Resource类提供的API方法便捷地加载及获取各类资源文件

> 资源目录：与项目下src文件夹同级的被命名为res的目录为资源存放目录
>
> Resources.path代表资源目录，在不同平台上Resources.path不同，开发者只需调用Resources.path即可获取资源存放目录
>
> 资源类型
>
> Resources.ResourceType
>
> * Music: 音频资源
>
> * Tile:基础地砖资源
>
> Resources.res，资源哈希表（名称-路径）
>
> ~~~java
> // 资源加载方法
> // 图片资源
> Image img = Resources.loadImage(filePath);
> 
> // 音频资源，以背景音乐为例，支持格式：wav, au
> // 音频格式转换：mp3转换为wav在线转换网站：https://cloudconvert.com/mp3-to-wav
> Resources.load(Resources.ResourceType.Music, "backgroundSound", Resources.path + "music/background.wav");
> 
> // 基础地砖资源
> // "0"：基础地砖编号
> // Resources.path + "images/tiles/floor_0.png"：对应地砖图像资源路径
> Resources.load(Resources.ResourceType.Tile, "0", Resources.path + "images/tiles/floor_0.png");
> ~~~

### 地图设计

地图是游戏的世界，游戏中的一草一木、飞禽走兽以及玩家的化身都在地图之中

开发者可以通过资源加载提供的方法绑定地砖编号与对应贴图，然后通过由地砖编号构成的配置文件即可由引擎生成游戏地图。

>使用加载进游戏中的基础地砖及地图文件（游戏中地图的布局：指定每块地砖采取哪个基础地砖，地砖位置即地砖坐标）构造
>
>~~~java
>// 基础地砖加载方法
>Resources.load(Resources.ResourceType.Tile, "0", Resources.path + "images/tiles/floor_0.png");
>Resources.load(Resources.ResourceType.Tile, "1", Resources.path + "images/tiles/floor_1.png");
>Resources.load(Resources.ResourceType.Tile, "2", Resources.path + "images/tiles/floor_2.png");
>Resources.load(Resources.ResourceType.Tile, "3", Resources.path + "images/tiles/floor_3.png");
>Resources.load(Resources.ResourceType.Tile, "4", Resources.path + "images/tiles/floor_4.png");
>
>// 地图文件内容编写指引，以"maps/map1.txt"为例
># tileMap ---> '#'号之后写注释
>0 0 0 0 0 0 0
>0 0 0 0 0 0 0
>0 0 1 2 3 4 4 ----> 指定地图中第3排地砖的地砖编号从左到右为'0 0 1 2 3 4 4'
>0 0 0 0 0 0 0
>0 0 0 0 0 0 0
>  
>// 根据基础地砖从文件中加载地图
>map = TileMap.loadTileMap(Resources.path + "maps/map1.txt");
>~~~
>
>地图中包含两类实体：玩家在游戏中的化身以及其他所有实体
>
>~~~java
>player = new Player(this.keyInputHandler, 60f);
>
>player.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.height / 2f);	// 设置玩家初始时位于屏幕中心
>player.setScale(new Vector2(2f, 2f));
>map.setPlayer(player);	// 设置地图中的主角为player，player需要为Entity或Entity子类的实例
>
>// 向地图中添加名为tree1_1的树木
>Entity tree1Entity = new Entity();
>tree1Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/tree1.png"));	// 树木为静态实体，设置静态图片即可，不需要进行动画更新
>tree1Entity.setScale(new Vector2(3f, 3f));	// 设置tree1_1的放大倍数
>tree1Entity.name = "tree1_1";
>map.addEntity(tree1Entity, new Vector2(200, 500));	// 将tree1_1添加到世界坐标为（200，500）的位置
>[注]以tree1Entity为实体可以进行旋转、缩放，取新的名字，将克隆实体加入到地图中其他位置，该功能类似Unity中的Prefabs预制体
>
>// 向地图中添加NPC
>// 南极仙翁（对话NPC）
>ConversationalNPC talkNPC = new ConversationalNPC(this.keyInputHandler, 0, "南极仙翁");	// NPC为游戏开发者构建的对象，以Sandbox中构建的对话型NPC为例，需要进行键盘监听，速度为0，名称为"南极仙翁"
>talkNPC.setScale(new Vector2(2f, 2f));	// 设置该NPC缩放倍数
>map.addNPC(talkNPC, new Vector2(1043, 275));	// 将该NPC添加到地图中，世界坐标为（1043，275）
>
>// 野鬼（打斗NPC）
>FightingNPC fightingNPC = new FightingNPC(30f);	// 速度为(30像素/s)
>fightingNPC.name = "野鬼";	// 设置NPC名称
>fightingNPC.setScale(new Vector2(2f, 2f));	// 设置该NPC缩放倍数
>map.addNPC(fightingNPC, new Vector2(800, 600)); // 将该NPC添加到地图中，世界坐标为（800，600）
>~~~
>
>聪明的你，到这里可能会疑惑：为什么地图中的主角与其他实体都是Entity或者Entity的孩子，但却要把主角单独分出来呢？
>
>**这是因为玩家的化身在地图中具有很大的特殊性，以镜头跟随为例，一般为了玩家在游戏时可以一眼看到主角，会在主角偏离屏幕中心时滚动屏幕，使主角回到屏幕中心的位置，这时候直接使用TileMap.player.position去计算会比在地图中所有实体里寻找player对象再获取其位置进行计算就要高效很多**

### 游戏世界的生机

草木飞禽、npc以及玩家，这些都是地图的重要组成部分，有了它们游戏世界才能焕发生机。

在梦玛引擎中，Entity是所有实体的父类，它提供可视性、是否静态等配置，开发者可以通过Animator为非静态Entity添加动画状态机，设置动画状态的转换有无退出时间，静态实体只需设置贴图即可，梦玛引擎通过继承多态扩展功能，开发者只需继承Entity，重写其生命周期函数即可，Entity主要提供构造函数、onStart，onUpdate及OnDestroy，分别在实体构造时，进入游戏地图中时，更新时及被销毁时调用。

### 碰撞处理

开发者终于做出了一个丰富的游戏世界，但各个物体间都可以通过，而开发者如果不想让某些物体间直接穿过，要怎么办呢？这就需要用到碰撞系统的检测与处理了，梦玛引擎提供了可视化可调整的碰撞盒，开发者将碰撞盒添加到实体身上，指定自己希望的碰撞盒模式：触发器与非触发器，在发生碰撞时，引擎会做相应的处理，保证非触发器模式不穿模，此外，梦玛引擎提供了onCollision、onTriggerEnter及onTriggerExit等钩子函数给开发者做自己的碰撞处理，分别代表发生非触发器碰撞、进入触发器碰撞盒、退出触发器碰撞盒

### 移动帮助

很多小型游戏引擎需要开发者在代码中指定实体摆放位置，但实际上在代码中修改后需要重新运行才能看到效果，对于物体位置调整来说非常不便，梦玛引擎提供可视化的实体移动帮助工具，开发者无需在代码中调整，只需在引擎运行时拖动移动工具 配图 调整位置即可，引擎会在关闭时序列化存入移动帮助及碰撞盒调整数据，在下次启动时载入

### 动画系统

不管是人物的行走，还是怪物的死亡，都需要有动画的播放及动画状态的转换，对于从零开始开发游戏的开发者而言，每个角色都需要写一套有限动画状态机，非常麻烦而且混乱。梦玛引擎提供关键帧动画Animation类及动画状态机Animator类，Animation可以通过添加图片及其对应播放时间自动维护关键帧动画的播放，Animator管理动画状态，可向其中增删动画状态，进行状态转换，配置转换过程有无退出时间

### 音频播放

音频播放支持可以直接通过加载音乐文件的名字调用播放一次、播放多次、循环播放、设置音量等函数，支持wav、au格式

### UI

UI是玩家与游戏用户界面交互的载体，比如血条、背包图标、背包等，梦玛引擎提供UIEntity及UIManager类处理UI系统的任务，UIEntity是Entity的孩子，可以使用移动帮助，并且自身提供UI对齐、模式等配置；UIManager可以进行UI绑定、解绑、可视性设置、添加、删除等操作。Text继承自UIEntity，用于针对文字的处理，可配置文字的大小、颜色、字体及行宽等