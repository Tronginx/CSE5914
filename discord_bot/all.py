import bestGuess
import twitter


def all_in(username, inputPath) -> list:
    images = twitter.get_twitter(username, inputPath)
    paths = [inputPath + '/image1.jpg', inputPath + '/image2.jpg', inputPath + '/image3.jpg']
    keywords = bestGuess.detect_web(paths)
    return keywords
