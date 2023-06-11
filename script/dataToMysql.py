import pandas as pd
from sqlalchemy import create_engine

print("开始读取数据...")
# 读取tsv文件
data = pd.read_csv('news.tsv', sep='\t')

# 选取需要的列并修改列名
data = data[['News ID', 'Category', 'Topic', 'Headline', 'News body']]
data.columns = ['news_id', 'category', 'topic', 'headline', 'news_body']

print("开始清洗数据...")
# 删除重复的行
data.drop_duplicates(subset='news_id', keep='first', inplace=True)
print(f"删除重复行后，还剩{data.shape[0]}行数据。")

# 处理缺失数据
# 如果'category', 'topic', 'headline', 'news_body'其中任何一个字段的数据缺失，那么删除该行
data.dropna(subset=['category', 'topic', 'headline', 'news_body'], inplace=True)
print(f"处理缺失数据后，还剩{data.shape[0]}行数据。")

# 数据类型转换
data['news_id'] = data['news_id'].astype(str)
data['category'] = data['category'].astype('category')
data['topic'] = data['topic'].astype('category')

# 数据规范化：将部分文本字段转换为小写
data['category'] = data['category'].str.lower()
data['topic'] = data['topic'].str.lower()

print("数据清洗完毕，准备导入数据库...")

# 创建连接
engine = create_engine('mysql+pymysql://yfy:yfy123@110.42.189.226:3306/BI_DATABASE')

# 将数据导入MySQL
data.to_sql('news', engine, if_exists='append', index=False)

print("数据导入成功！")
