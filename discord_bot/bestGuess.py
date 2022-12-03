def detect_web(path):
    """Detects web annotations given an image."""
    from google.cloud import vision
    import io
    client = vision.ImageAnnotatorClient()

    with io.open(path, 'rb') as image_file:
        content = image_file.read()

    image = vision.Image(content=content)

    response = client.web_detection(image=image)
    annotations = response.web_detection

    if annotations.best_guess_labels:
        print('\n Best guess label: {}'.format(annotations.best_guess_labels[0].label))
        # for label in annotations.best_guess_labels:
        #     print('\nBest guess label: {}'.format(label.label))

    if response.error.message:
        raise Exception(
            '{}\nFor more info on error messages, check: '
            'https://cloud.google.com/apis/design/errors'.format(
                response.error.message))


detect_web('/Users/young_y2m/Desktop/CSE/Tower.jpg')
