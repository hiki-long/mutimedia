类——ReadDir
ReadDir中的实例化对象为dr
dr可以获取的信息如下：
List的遍历自行上网查找
dr.get_MusicName()——获取所有音乐名称列表，便于设置标题,不包含.mp3后缀
例如：冠世一战.mp3 储存为：冠世一战

dr.get_VideoName()——获取所有视频名称列表，不包含.mp4后缀
同上

dr.get_AllMedia()——获取所有可以识别的文件列表，这个是.m3u(播放列表文件的信息)
get_AllMedia存储的是绝对路径
存储形式如下：
D:\Maplemusic\Bgm00.img.DragonDream.mp3
D:\Maplemusic\Bgm03 Elfwood.mp3
D:\Maplemusic\Bgm15.img.ElinForest.mp3

rd.getClear()——情况所有存储数据，用于导入其他文件夹的音频时使用

类——ReadLrc
ReadLrc的实例化对象为数组store_lrc[]
使用：
store_lrc[index].getLyric()
——获取一个Map<Long,String>映射哈希表，
输入歌词的对应的毫秒数，既可以取出对应歌词String

例子：

store_lrc[index].getLyric().getKey((Long)1000)——获得[00:01.00]，第一秒时候对应的歌词
[mm:ss.xx] 毫秒数 = mm*60*1000 + ss*1000 + xx*10
注意输入的数字要强制转化为Long类型，如果是int则返回空值
要是取不到歌词，请自行搜索Map类的遍历，打印查看Map表是否有值，或者是哪部取歌词的步骤有误

以下名称都不带.mp3后缀
store_lrc[index].getLyric().getSingerName()——获取当前正在播放mp3的歌手名

store_lrc[index].getLyric().isPureMusic()——获取当前播放的是否有歌词，无即为纯音乐

store_lrc[index].getLyric().getMusicName()——获取当前正在播放mp3的歌曲名
以上数组记得根据情况改下标

歌曲index
在MainController中，index的基本改变功能已经写好，请在源码基础上修改，不要再来一个index

音乐的封面变量为ImageView类型，变量名为cover，
修改封面的布局请自行调整，并且修改对应改变封面语句中的变量名

题外话：
UI界面请模仿QQ音乐、网易云音乐等已有界面的特效，我们暂时不做Album和歌曲类型的功能
但是要考虑历史播放记录和播放列表的功能和UI界面要做出来
进度条要好看一点，不要javafx原生的进度条，很难看
按钮的背景尽量用图片填充而不是文字，放在项目的/img文件夹进行引用,可以参考使用放在当前img目录的图片

歌词文件要和音频放在同一文件夹，否则无法正常读取
歌曲格式统一为 歌曲名 - 歌手.mp3
例如：earthmind - Another Heaven.mp3
歌词格式统一为 歌曲名 - 歌手.lrc
例如：earthmind - Another Heaven.lrc

下载下来的歌词要先打开，删除里面的空白歌词行以及歌手和歌曲信息行，只留下歌词
[xx:xx.xx]演唱：xxx
[xx:xx.xx]歌曲：xxx
//删掉上面几行，保留歌词
[00:12.92]何を抱き締める? 欲望の影で,浸身于欲望之影的你在紧紧拥抱着什么呢？
[00:18.03]痛みに塗れた理想を捨てて,丢掉了伤痕累累的理想
[xx:xx.xx]				//有时候中间夹带的空白歌词行也删掉
[00:23.46]まるで別世界 それでも真実,仿佛身处另一个世界，真实又虚幻

store_lrc[index].getLyric().printLrc()——打印出歌词，非按顺序排列，可以查看是否正确导入歌词

视频相关类
MediaView