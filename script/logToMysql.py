import pandas as pd
from sqlalchemy import create_engine

print("开始读取数据...")
# Step 1: Read the TSV file and clean data
df = pd.read_csv('valid.tsv', sep='\t')

print("开始处理数据...")
# Split the space-separated values into list of values
df['ClicknewsID'] = df['ClicknewsID'].str.split(' ')
df['dwelltime'] = df['dwelltime'].str.split(' ').apply(lambda x: [int(i) for i in x])
df['exposure_time'] = df['exposure_time'].str.split('#TAB#')
df['pos'] = df['pos'].str.split(' ')
df['neg'] = df['neg'].str.split(' ')
df['dwelltime_pos'] = df['dwelltime_pos'].str.split(' ').apply(lambda x: [int(i) for i in x])

# Convert start and end time to datetime format
df['start'] = pd.to_datetime(df['start'])
df['end'] = pd.to_datetime(df['end'])

print("开始创建表数据...")
# Create separate dataframes for each table
users = pd.DataFrame(df['UserID'].unique(), columns=['user_id'])
click_history = df.explode('ClicknewsID')[['UserID', 'ClicknewsID']].copy()
click_history['dwell_time'] = df.explode('dwelltime')['dwelltime'].values
click_history['exposure_time'] = df.explode('exposure_time')['exposure_time'].values
click_history['exposure_time'] = pd.to_datetime(click_history['exposure_time'], format='%m/%d/%Y %I:%M:%S %p')
print("最耗时的时间转换完成...")
click_history.columns = ['user_id','news_id','dwell_time','exposure_time']
impressions = df[['UserID', 'start', 'end']].copy().reset_index()
impressions.columns = ['impression_id', 'user_id', 'start_time', 'end_time']
pos_impression_news = df.explode('pos')[['UserID', 'pos']].copy().reset_index()
pos_impression_news.columns = ['impression_id', 'user_id', 'news_id']
pos_impression_news['clicked'] = True
pos_impression_news['dwell_time'] = df.explode('dwelltime_pos')['dwelltime_pos'].values
neg_impression_news = df.explode('neg')[['UserID', 'neg']].copy().reset_index()
neg_impression_news.columns = ['impression_id', 'user_id', 'news_id']
neg_impression_news['clicked'] = False
neg_impression_news['dwell_time'] = 0
impression_news = pd.concat([pos_impression_news, neg_impression_news])


# Data validation and cleaning
def validate_and_clean_data(df, table_name):
    # Remove columns with missing or invalid values
    df.dropna(inplace=True)
    
    if table_name == 'click_history':
        # Remove rows with invalid exposure_time
        df = df[df['exposure_time'].notnull()]
    
    # Check data types and convert if necessary
    if table_name == 'click_history':
        df['dwell_time'] = df['dwell_time'].astype(int)
    elif table_name == 'impression_news':
        df['dwell_time'] = df['dwell_time'].astype(int)
    
    return df

# Validate and clean data for each table
def process_table_data(df, table_name):
    print(f"Processing data for {table_name}...")
    
    df = validate_and_clean_data(df, table_name)
    
    print(f"Data processing completed for {table_name}.")
    
    return df

# Process data for each table
users = process_table_data(users, 'users')
click_history = process_table_data(click_history, 'click_history')
impressions = process_table_data(impressions, 'impressions')
impression_news = process_table_data(impression_news, 'impression_news')

# Step 2: Create database connection
engine = create_engine('mysql+pymysql://yfy:yfy123@110.42.189.226:3306/BI_DATABASE')

# Step 3: Import data to MySQL
users.to_sql('users', con=engine, if_exists='replace', index=False)
# 筛选click_history在的news_id要在news表里面
news_ids = pd.read_sql_query('SELECT news_id FROM news', con=engine)
click_history = click_history[click_history['news_id'].isin(news_ids['news_id'])]
click_history.to_sql('click_history', con=engine, if_exists='replace', index=False)
print("click_history OK...")
impressions.to_sql('impressions', con=engine, if_exists='replace', index=False)
print("impressions OK...")
# impression_news = impression_news[impression_news['news_id'].isin(news_ids['news_id'])]
impression_news.to_sql('impression_news', con=engine, if_exists='replace', index=False)
print("impression_news OK...")

