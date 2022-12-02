import requests
import snscrape.modules.twitter as sntwitter
import pandas as pd
import requests

query = "(from:MasterDaye) until:2022-12-02" \
        "since:2022-11-01"
tweets = []
limits = 10

for tweet in sntwitter.TwitterSearchScraper(query).get_items():
    if len(tweets) == limits:
        break
    else:
        if not tweet.media:
            continue
        for medium in tweet.media:
            if isinstance(medium, sntwitter.Photo):
                r = requests.get(medium.fullUrl)
                with open('filename.jpg', 'wb') as fp:
                    fp.write(r.content)
                tweets.append([tweet.date, tweet.user.username, medium.fullUrl])
                r.close()

df = pd.DataFrame(tweets, columns=['Date', 'User', 'Tweet'])
print(df)
# df.to_csv(f'here.csv', index=False, encoding='utf-8')