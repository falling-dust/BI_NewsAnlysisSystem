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

# 定义user_interest表的模型
class UserInterest(Base):
    __tablename__ = 'user_interest'
    id = Column(Integer, primary_key=True)
    user_id = Column(String)
    category = Column(String)
    date = Column(Date)
    interest_clicks = Column(Integer)
    

# 定义click_history表的模型
class ClickHistory(Base):
    __tablename__ = 'click_history'
    user_id = Column(String, primary_key=True)
    news_id = Column(String, primary_key=True)
    dwell_time = Column(Integer)
    exposure_time = Column(Date)

# 遍历click_history表的记录，统计点击次数并导入user_interest表
def process_click_history():
    """
    遍历click_history表的记录，统计每个新闻类别在不同天数的点击次数（dwell_time大于30）并导入user_interest表。
    """
    click_histories = session.query(ClickHistory).limit(100000).all()
    user_interest_dict = {}

    for click_history in click_histories:
        print(click_history.news_id)
        user_id = click_history.user_id
        date = click_history.exposure_time.date()
        if click_history.dwell_time > 20:
            # 从news表中匹配新闻的类别
            news = session.query(News).filter(News.news_id == click_history.news_id).first()
            if news:
                category = news.category

                # 使用(user_id, date, category)作为字典的键来区分不同天数的点击次数
                key = (user_id, date, category)
                if key not in user_interest_dict:
                    user_interest_dict[key] = 0
                user_interest_dict[key] += 1

    for (user_id, date, category), interest_clicks in user_interest_dict.items():
        user_interest = UserInterest(category=category,user_id=user_id, interest_clicks=interest_clicks, date=date)
        session.add(user_interest)

    session.commit()

# 执行处理函数
process_click_history()

# 关闭数据库连接
session.close()