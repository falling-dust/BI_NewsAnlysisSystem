from datetime import datetime
from sqlalchemy import create_engine, Column, String, Integer, Date
from sqlalchemy.orm import sessionmaker
from sqlalchemy.ext.declarative import declarative_base

# 建立数据库连接
engine = create_engine('mysql+pymysql://yfy:yfy123@110.42.189.226:3306/BI_DATABASE')
Base = declarative_base()
Session = sessionmaker(bind=engine)
session = Session()

# 定义news表的模型
class News(Base):
    __tablename__ = 'news'
    news_id = Column(String, primary_key=True)
    category = Column(String)
    topic = Column(String)
    headline = Column(String)
    news_body = Column(String)
    # 其他列定义...

# 定义category_popularity表的模型
class CategoryPopularity(Base):
    __tablename__ = 'category_popularity'
    id = Column(Integer, primary_key=True)
    category = Column(String)
    date = Column(Date)
    click_times = Column(Integer)
    

# 定义click_history表的模型
class ClickHistory(Base):
    __tablename__ = 'click_history'
    user_id = Column(String, primary_key=True)
    news_id = Column(String, primary_key=True)
    dwell_time = Column(Integer)
    exposure_time = Column(Date)

# 遍历click_history表的记录，统计点击次数并导入category_popularity表
def process_click_history():
    """
    遍历click_history表的记录，统计每个新闻类别在不同天数的点击次数（dwell_time大于30）并导入category_popularity表。
    """
    click_histories = session.query(ClickHistory).limit(100000).all()
    category_popularity_dict = {}

    for click_history in click_histories:
        print(click_history.news_id)
        date = click_history.exposure_time.date()
        if click_history.dwell_time > 30:
            # 从news表中匹配新闻的类别
            news = session.query(News).filter(News.news_id == click_history.news_id).first()
            if news:
                category = news.category

                # 使用(date, category)作为字典的键来区分不同天数的点击次数
                key = (date, category)
                if key not in category_popularity_dict:
                    category_popularity_dict[key] = 0
                category_popularity_dict[key] += 1

    for (date, category), click_times in category_popularity_dict.items():
        category_popularity = CategoryPopularity(category=category, click_times=click_times, date=date)
        session.add(category_popularity)

    session.commit()

# 执行处理函数
process_click_history()

# 关闭数据库连接
session.close()
