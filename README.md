# elasticsearch-study
开个新坑学习ElasticSearch，过几天写个项目把最近学的都整合进去

***\*ElasticSearch\****（大数据无非解决两个问题：存储+计算！）

版本:ElasticSearch 7.6.1（全网最新了)

6.X,7.X的区别十分大，6.x的API（原生API、RestFul高级!)

 

 

***\*我们要讲解什么?\**** 

SQL : like %zsp%，如果是的大数据，就十分慢!索引!ElasticSearch:搜索!(百度、 github、淘宝电商!)

1、聊一个人

2、货比三家

3、安装

4、生态圈

5、分词器ik

6、RestFul操作ES

7、CRUD（当成数据库，但比较少）

8、SpringBoot集成ElasticSearch(从原理分析!)

9、爬虫爬取数据！

10、实战，模拟全文搜索

 

以后你只要，需要用到搜索，就可以使用ES!(大数据量的情况下使用!)后续可以了解一下ELK，做数据分析。

 

***\*聊聊Doug Cutting\**** ***\*（Hadoop也是这个人创的）\****

1998年9月4日，Google公司在美国硅谷成立。正如大家所知，它是一家做搜索引擎起家的公司。

![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\ksohtml8992\wps1.jpg) 

无独有偶，一位名叫Doug Cutting的美国工程师，也迷上了搜索引擎。他做了一个用于文本搜索的函数库（姑且理解为软件的功能组件），命名为Lucene。

![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\ksohtml8992\wps2.jpg) 

Lucene是用JAVA写成的，目标是为各种中小型应用软件加入全文检索功能。因为好用雨且开源(代码公开），非常受程序员们的欢迎。

早期的时候，这个项目被发布在Doug Cutting的个人网站和SourceForge (一个开源软件网站)。后来，2001年底，Lucene成为Apache软件基金会jakarta项目的一个子项目。

![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\ksohtml8992\wps3.jpg) 

2004年，Doug Cutting再接再励，在Lucene的基础上，和Apache开源伙伴Mike Cafarella合作，开发了一款可以代替当时的主搜索的开源搜索引擎，命名为Nutch。

Nutch是一个建立在Lucene核心之上的网页搜索应用程序，可以下载下来直接使用。它在Lucene的基础上加了网络爬虫和一些网页相关的功能，目的就是从一个简单的站内检索推广到全球网络的搜索上，就像Google一样。

Nutch在业界的影响力比Lucene更大。

大批网站采用了Nutch平台，大大降低了技术门槛，使低成本的普通计算机取代高价的Web服务器成为可能。甚至有一段时间，在硅谷有了一股用Nutch低成本创业的潮流。（大数据!)

随着时间的推移，无论是Google还是Nutch，都面临搜索对象"体积"不断增大的问题。

尤其是Google，作为互联网搜索引擎，需要存储大量的网页，并不断优化自己的搜索算法，提升搜索效率。

![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\ksohtml8992\wps4.jpg) 

在这个过程中，Google确实找到了不少好办法，并且无私地分享了出来。

2003年，Google发表了一篇技术学术论文，公开介绍了自己的谷歌文件系统GFS ( Google File System )。这是Google公司为了存储海量搜索数据而设计的专用文件系统。

第二年，也就是2004年，Doug Cutting基于Google的GFS论文，实现了分布式文件存储系统，并将它命名为NDFS ( NutchDistributed File system )。

![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\ksohtml8992\wps5.jpg) 

还是2004年，Google又发表了一篇技术学术论文，介绍自己的MapReduce编程模型。这个编程模型，用于大规模数据集（大于1TB)的并行分析运算。

第二年(2005年)，Doug Cutting又基于MapReduce，在Nutch搜索引擎实现了该功能。

![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\ksohtml8992\wps6.jpg) 

2006年，当时依然很厉害的Yahoo(雅虎）公司，招安了Doug cutting。

![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\ksohtml8992\wps7.jpg)
加盟Yahoo之后，Doug Cutting将NDFS和MapReduce进行了升级改造，并重新命名为Hadoop (NDFS也改名为HDFS ，HadoopDistributed File System )。

这个，就是后来大名鼎鼎的大数据框架系统―—Hadoop的由来。而Doug Cutting，则被人们称为Hadoop之父。

 

Hadoop这个名字，实际上是Doug Cutting他儿子的黄色玩具大象的名字。所以，Hadoop的Logo，就是一只奔跑的黄色大象。

![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\ksohtml8992\wps8.jpg) 

我们继续往下说。

还是2006年，Google又发论文了。

这次，它们介绍了自己的BigTable。这是一种分布式数据存储系统，一种用来处理海量数据的非关系型数据库。Doug Cutting当然没有放过，在自己的hadoop系统里面，引入了BigTable，并命名为HBase。

![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\ksohtml8992\wps9.jpg) 

好吧，反年就是紧跟Google时代步伐，你出什么，我学什么。所以，Hadoop的核心部分，基本上都有Google的影子。

2008年1月，Hadoop成功上位，正式成为Apache基金会的顶级项目。

同年2月，Yahoo宣布建成了一个拥有1万个内核的Hadoop集群，并将自己的搜索引擎产品部署在上面。7月，Hadoop打破世界纪录，成为最快排序1TB数据的系统，用时209秒。

 

Lucene是一套信息检索工具包! jar包!不包含搜索引擎系统!包含的︰索引结构!读写索引的工具!排序，搜索规则....工具类!Lucene 和Elasticsearch关系︰

ElasticSearch是基于Lucene做了一些封装和增强（我们上手是十分简单! )

 

 ***\*ElasticSearch概述\****

多年前，一个叫做Shay Banon的刚结婚不久的失业开发者，由于妻子要去伦敦学习厨师，他便跟着也去了。在他找工作的过程中，为了给妻子构建一个食谱的搜索引擎，他开始构建一个早期版本的Lucene。

直接基于Lucene工作会比较困难，所以Shay开始抽象Lucene代码以便Java程序员可以在应用中添加搜索功能。他发布了他的第一个开源项目，叫做"Compass"。

后来Shay找到一份工作，这份工作处在高性能和内存数据网格的分布式环境中，因此高性能的、实时的、分布式的搜索引擎也是理所当然需要的。然后他决定重写Compass库使其成为一个独立的服务叫做Elasticsearch,

第一个公开版本出现在2010年2月，在那之后Elasticsearch已经成为Github上最受欢迎的项目之一，代码贡献者超过300人。一家主营Elasticsearch的公司就此成立，他们一边提供商业支持一边开发新功能，不过Elasticsearch将永远开源且对所有人可用。

Shay的妻子依旧等待着她的食谱搜索......

 

***\*谁在使用︰\****

1、维基百科，类似百度百科，全文检索，高亮，搜索推荐/2

2、The Guardian(国外新闻网站），类似搜狐新闻，用户行为日志(点击，浏览，收藏，评论）+社交网络数据（对某某新闻的相关看法），数据分析，给到每篇新闻文章的作者，让他知道他的文章的公众反馈(好，坏，热门，垃圾，鄙视，崇拜)

3、Stack Overflow(国外的程序异常讨论论坛），IT问题，程序的报错，提交上去，有人会跟你讨论和回答，全文检索，搜索相关问题和答案，程序报错了，就会将报错信息粘贴到里面去，搜索有没有对应的答案

4、GitHub(开源代码管理），搜索上千亿行代码

5、电商网站，检索商品

6、日志数据分析，logstash采集日志，ES进行复杂的数据分析，ELK技术，elasticsearch+logstash+kibana

7、商品价格监控网站，用户设定某商品的价格阈值，当低于该阈值的时候，发送通知消息给用户，比如说订阅牙膏的监控，如果高露洁牙膏的家庭套装低于50块钱，就通知我，我就去买

8、Bl系统，商业智能，Business Intelligence。比如说有个大型商场集团，Bl，分析一下某某区域最近3年的用户消费金额的趋势以及用户群体的组成构成，产出相关的数张报表，**区，最近3年，每年消费金额呈现100%的增长，而且用户群体85%是高级白领，开一个新商场。ES执行数据分析和挖掘，Kibana进行数据可视化

9、国内︰站内搜索（电商，招聘，门户，等等），IT系统搜索(OA，CRM，ERP，等等），数据分析（ES热门的一个使用场景)