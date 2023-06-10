### 启动

启动三台虚拟机：Master，Slave1，Slave2

对每台虚拟机，输入以下命令，启动zookeeper：

```
zkServer.sh start
```

在master机器上，输入以下命令启动kafka集群（已写好启动脚本）

```
kf.sh start
```

到达/home/hadoop/BI/spark/sbin下，使用命令启动spark集群：

```
./start-all.sh
```

到达/home/hadoop/BI/flume下，输入命令启动flume监听4444端口:

```
./bin/flume-ng agent --conf conf/ --name agent --conf-file conf/flume-conf.properties -Dflume.root.logger==INFO,console
```

到达/home/hadoop/BI目录下，输入命令启动模拟脚本：

```
python3 dataToFlume.py
```

启动sparkstreaming项目，实现数据的实时处理