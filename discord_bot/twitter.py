import snscrape.modules.twitter as sntwitter
import pandas as pd

query = "(from:elonmusk) until:2022-12-02" \
        "since:2022-11-01"
tweets = []
limits = 100

for tweet in sntwitter.TwitterSearchScraper(query).get_items():
    if len(tweets) == limits:
        break
    else:
        tweets.append([tweet.date, tweet.user.username, tweet.content])

df = pd.DataFrame(tweets, columns=['Date', 'User', 'Tweet'])
print(df)