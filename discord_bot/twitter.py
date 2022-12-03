import requests
import snscrape.modules.twitter as sntwitter
import pandas as pd


def get_twitter(username, inputPath) -> list:
    # username = input('Enter the twitter account you want to search: ')
    # username = 'MasterDaye'

    query = f"(from:{username}) until:2022-12-02" \
            "since:2022-11-01"
    tweets = []
    limits = 10
    # path = '/Users/young_y2m/Desktop/discord/file'
    path = inputPath + '/image'
    i = 1
    images = []

    for tweet in sntwitter.TwitterSearchScraper(query).get_items():
        if len(tweets) == limits:
            break
        else:
            if not tweet.media:
                continue
            for medium in tweet.media:
                if isinstance(medium, sntwitter.Photo):
                    r = requests.get(medium.fullUrl)
                    if r.status_code == 200:
                        with open(path + str(i) + '.jpg', 'wb') as fp:
                            fp.write(r.content)
                    tweets.append([tweet.date, tweet.user.username, medium.fullUrl])
                    images.append([medium.fullUrl])
                    i += 1
                    r.close()

    df = pd.DataFrame(tweets, columns=['Date', 'User', 'Tweet'])
    print(df)
    # df.to_csv(f'here.csv', index=False, encoding='utf-8')
    return images
